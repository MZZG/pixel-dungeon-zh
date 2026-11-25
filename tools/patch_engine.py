import os
import re

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DECOMP = os.path.join(ROOT, "src_decompiled", "com", "watabou")
SRC = os.path.join(ROOT, "src", "com", "watabou")


def auto_fix_cfr(txt, rel):
    pattern = re.compile(
        r"([ \t]*)float ([a-zA-Z0-9_]+);\r?\n"
        r"(?:([ \t]*super\.update\(\);)\r?\n)?"
        r"([ \t]*)(this\.[a-zA-Z0-9_]+) -= ([^\n;]+);\r?\n"
        r"([ \t]*)if \(\2 (<=|<|>=|>) ([^)]+)\)"
    )

    def repl(m):
        mid = (m.group(3) + "\n") if m.group(3) else ""
        indent = m.group(4)
        lhs = m.group(5)
        rhs = m.group(6)
        op = m.group(8)
        val = m.group(9)
        return f"{mid}{indent}if (({lhs} -= {rhs}) {op} {val})"

    return pattern.sub(repl, txt)


def apply_file_patches(patches, decomp_dir=DECOMP, src_dir=SRC):
    """Apply exact (old, new) replacement rules from decompiled sources into src/."""
    applied = 0
    for rel, rules in patches.items():
        src_in = os.path.join(decomp_dir, rel.replace("/", os.sep))
        src_out = os.path.join(src_dir, rel.replace("/", os.sep))
        with open(src_in, "r", encoding="utf-8") as f:
            txt = f.read()
        txt = auto_fix_cfr(txt, rel)
        for old, new in rules:
            if old not in txt:
                raise RuntimeError(f"[{rel}] Could not find target snippet:\n{old}")
            txt = txt.replace(old, new, 1)
        os.makedirs(os.path.dirname(src_out), exist_ok=True)
        with open(src_out, "w", encoding="utf-8") as f:
            f.write(txt)
        applied += 1
    print(f"Applied patches to {applied} files.")
