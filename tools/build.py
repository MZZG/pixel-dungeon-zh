import argparse
import os
import re
import shutil
import subprocess
import sys
import zipfile

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SRC_DIR = os.path.join(ROOT, "src")
ASSETS_DIR = os.path.join(ROOT, "assets_extra")
BUILD_ROOT = os.path.join(ROOT, "build")
CLASSES_DIR = os.path.join(BUILD_ROOT, "classes")
EXTRACT_DIR = os.path.join(BUILD_ROOT, "game_extract")
DEFAULT_DIST_JAR = os.path.join(ROOT, "dist", "desktop-1.9.2-steam.jar")
JAR_NAME = "desktop-1.9.2-steam.jar"


def find_steam_jar():
    env_jar = os.environ.get("PD_JAR")
    if env_jar and os.path.isfile(env_jar):
        return env_jar

    candidates = []
    if sys.platform == "win32":
        steam_roots = [
            os.path.join(os.environ.get("ProgramFiles(x86)", r"C:\Program Files (x86)"), "Steam"),
            os.path.join(os.environ.get("ProgramFiles", r"C:\Program Files"), "Steam"),
        ]
        # Check Steam libraryfolders.vdf to discover custom library drives
        for sr in list(steam_roots):
            vdf = os.path.join(sr, "steamapps", "libraryfolders.vdf")
            if os.path.isfile(vdf):
                try:
                    txt = open(vdf, "r", encoding="utf-8", errors="ignore").read()
                    for m in re.findall(r'"path"\s+"([^"]+)"', txt):
                        steam_roots.append(m.replace("\\\\", "\\"))
                except OSError:
                    pass
        for sr in steam_roots:
            candidates.append(os.path.join(sr, "steamapps", "common", "Pixel Dungeon", JAR_NAME))
    elif sys.platform == "darwin":
        home = os.path.expanduser("~")
        candidates.append(os.path.join(home, "Library", "Application Support", "Steam", "steamapps", "common", "Pixel Dungeon", JAR_NAME))
    else:
        home = os.path.expanduser("~")
        candidates.extend([
            os.path.join(home, ".local", "share", "Steam", "steamapps", "common", "Pixel Dungeon", JAR_NAME),
            os.path.join(home, ".steam", "steam", "steamapps", "common", "Pixel Dungeon", JAR_NAME),
        ])

    for p in candidates:
        if os.path.isfile(p):
            return p
    return None


def extract_game_jar(jar_path, extract_dir):
    if os.path.exists(extract_dir):
        shutil.rmtree(extract_dir)
    os.makedirs(extract_dir, exist_ok=True)
    print(f"Extracting base game JAR: {jar_path}")
    with zipfile.ZipFile(jar_path, "r") as z:
        z.extractall(extract_dir)


def strip_bom_and_collect():
    java_files = []
    for dp, _, fs in os.walk(SRC_DIR):
        for f in fs:
            if f.endswith(".java"):
                p = os.path.join(dp, f)
                with open(p, "rb") as fh:
                    raw = fh.read()
                if raw.startswith(b"\xef\xbb\xbf"):
                    with open(p, "wb") as fh:
                        fh.write(raw[3:])
                java_files.append(p)
    return sorted(java_files)


def detect_javac_target_flags():
    res = subprocess.run(["javac", "-version"], capture_output=True)
    ver_out = (res.stdout + res.stderr).decode("utf-8", errors="replace").strip()
    m = re.search(r"(\d+)(?:\.(\d+))?", ver_out)
    if m:
        major = int(m.group(1))
        if major == 1 and m.group(2):
            major = int(m.group(2))
        if major >= 9:
            return ["--release", "8", "-Xlint:-options"]
    return ["-source", "1.8", "-target", "1.8"]


def compile_sources(java_files, classpath_dir):
    if os.path.exists(CLASSES_DIR):
        shutil.rmtree(CLASSES_DIR)
    os.makedirs(CLASSES_DIR, exist_ok=True)
    sources_txt = os.path.join(BUILD_ROOT, "build_sources.txt")
    with open(sources_txt, "w", encoding="utf-8") as f:
        f.write("\n".join(java_files))

    cmd = [
        "javac",
        *detect_javac_target_flags(),
        "-encoding", "UTF-8",
        "-cp", classpath_dir,
        "-d", CLASSES_DIR,
        f"@{sources_txt}",
    ]
    print(f"Compiling {len(java_files)} modified Java files...")
    res = subprocess.run(cmd, capture_output=True)
    if res.stdout:
        print(res.stdout.decode(errors="replace"))
    if res.stderr:
        print(res.stderr.decode(errors="replace"))
    if res.returncode != 0:
        raise SystemExit(f"javac failed with exit code {res.returncode}")
    print("Compilation succeeded.")


def pack_jar(extract_dir, out_jar):
    os.makedirs(os.path.dirname(out_jar), exist_ok=True)
    file_map = {}

    def add_tree(base_dir):
        if not os.path.exists(base_dir):
            return
        for dp, _, fs in os.walk(base_dir):
            for f in fs:
                full = os.path.join(dp, f)
                rel = os.path.relpath(full, base_dir).replace("\\", "/")
                file_map[rel] = full

    add_tree(extract_dir)
    add_tree(ASSETS_DIR)
    add_tree(CLASSES_DIR)

    recompiled_prefixes = set()
    for dp, _, fs in os.walk(CLASSES_DIR):
        for f in fs:
            if f.endswith(".class") and "$" not in f:
                rel = os.path.relpath(os.path.join(dp, f), CLASSES_DIR).replace("\\", "/")
                recompiled_prefixes.add(rel[:-6] + "$")

    tmp_jar = out_jar + ".tmp"
    count = 0
    with zipfile.ZipFile(tmp_jar, "w", compression=zipfile.ZIP_DEFLATED) as z:
        for arcname in sorted(file_map.keys()):
            src_path = file_map[arcname]
            if src_path.startswith(extract_dir) and any(arcname.startswith(p) for p in recompiled_prefixes):
                continue
            z.write(src_path, arcname)
            count += 1
    if os.path.exists(out_jar):
        os.remove(out_jar)
    os.replace(tmp_jar, out_jar)
    print(f"Packed {count} entries -> {out_jar}")


def main():
    parser = argparse.ArgumentParser(description="Build Chinese localization JAR for Pixel Dungeon 1.9.2 (Steam)")
    parser.add_argument("--jar", help="Path to original desktop-1.9.2-steam.jar (auto-detected if omitted)")
    parser.add_argument("--out", default=DEFAULT_DIST_JAR, help="Output JAR path (default: dist/desktop-1.9.2-steam.jar)")
    parser.add_argument("--install", action="store_true", help="Backup original JAR to .bak and install the localized JAR")
    parser.add_argument("--restore", action="store_true", help="Restore original JAR from .bak backup")
    args = parser.parse_args()

    jar_path = args.jar or find_steam_jar()
    if not jar_path or not os.path.isfile(jar_path):
        raise SystemExit(
            f"Could not find {JAR_NAME}. Please pass --jar <path-to-{JAR_NAME}> or set PD_JAR environment variable."
        )

    backup_path = jar_path + ".bak"
    if args.restore:
        if not os.path.isfile(backup_path):
            raise SystemExit(f"Backup not found: {backup_path}")
        shutil.copy2(backup_path, jar_path)
        print(f"Restored original JAR from {backup_path}")
        return

    extract_game_jar(jar_path, EXTRACT_DIR)
    java_files = strip_bom_and_collect()
    compile_sources(java_files, EXTRACT_DIR)
    pack_jar(EXTRACT_DIR, os.path.abspath(args.out))

    if args.install:
        if not os.path.exists(backup_path):
            print(f"Backing up original JAR -> {backup_path}")
            shutil.copy2(jar_path, backup_path)
        shutil.copy2(os.path.abspath(args.out), jar_path)
        print(f"Installed localized JAR -> {jar_path}")


if __name__ == "__main__":
    main()
