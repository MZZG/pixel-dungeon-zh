using System;
using System.Diagnostics;
using System.IO;
using System.Windows.Forms;

namespace PixelDungeonZhLauncher
{
    internal static class Program
    {
        [STAThread]
        private static void Main()
        {
            string baseDir = AppDomain.CurrentDomain.BaseDirectory;
            string jarPath = FindJar(baseDir);

            if (jarPath == null)
            {
                MessageBox.Show(
                    "未在当前目录找到汉化版游戏主程序 (desktop-1.9.2-steam-zh.jar 或 desktop-1.9.2-steam.jar)。\n\n" +
                    "请将本启动器与汉化版 JAR 文件放在同一目录中后再试，或手动选择 JAR 文件。",
                    "Pixel Dungeon 独立启动器",
                    MessageBoxButtons.OK,
                    MessageBoxIcon.Information
                );
                using (OpenFileDialog dlg = new OpenFileDialog())
                {
                    dlg.Title = "选择 Pixel Dungeon 汉化版 JAR 文件";
                    dlg.Filter = "JAR 文件 (*.jar)|*.jar|所有文件 (*.*)|*.*";
                    dlg.InitialDirectory = baseDir;
                    if (dlg.ShowDialog() != DialogResult.OK)
                    {
                        return;
                    }
                    jarPath = dlg.FileName;
                }
            }

            string workDir = Path.GetDirectoryName(jarPath);
            string bundledJava = Path.Combine(workDir, "jre", "bin", "javaw.exe");
            if (!File.Exists(bundledJava))
            {
                bundledJava = Path.Combine(workDir, "jre", "bin", "java.exe");
            }

            string javaExe;
            string extraJvmArgs = "";
            if (File.Exists(bundledJava))
            {
                javaExe = bundledJava;
                extraJvmArgs = "-server ";
            }
            else
            {
                javaExe = FindSystemJava();
                if (javaExe == null)
                {
                    MessageBox.Show(
                        "未检测到 Java 运行环境 (JRE/JDK 8+)。\n\n" +
                        "请安装 Java 8 或更高版本，或将原游戏目录中的 jre 文件夹复制到当前目录。",
                        "缺少 Java 运行环境",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Error
                    );
                    return;
                }
            }

            ProcessStartInfo psi = new ProcessStartInfo();
            psi.FileName = javaExe;
            psi.Arguments = extraJvmArgs + "-Xmx1G -cp \"" + jarPath + "\" com.watabou.pd.desktop.StandaloneLauncher";
            psi.WorkingDirectory = workDir;
            psi.UseShellExecute = false;
            psi.CreateNoWindow = true;

            try
            {
                Process.Start(psi);
            }
            catch (Exception ex)
            {
                MessageBox.Show(
                    "启动游戏失败：\n" + ex.Message,
                    "Pixel Dungeon 独立启动器",
                    MessageBoxButtons.OK,
                    MessageBoxIcon.Error
                );
            }
        }

        private static string FindJar(string dir)
        {
            string[] names = { "desktop-1.9.2-steam-zh.jar", "desktop-1.9.2-steam.jar" };
            foreach (string name in names)
            {
                string candidate = Path.Combine(dir, name);
                if (File.Exists(candidate))
                {
                    return candidate;
                }
            }
            return null;
        }

        private static string FindSystemJava()
        {
            string javaHome = Environment.GetEnvironmentVariable("JAVA_HOME");
            if (!string.IsNullOrEmpty(javaHome))
            {
                string jw = Path.Combine(javaHome, "bin", "javaw.exe");
                if (File.Exists(jw)) return jw;
                string j = Path.Combine(javaHome, "bin", "java.exe");
                if (File.Exists(j)) return j;
            }

            string pathEnv = Environment.GetEnvironmentVariable("PATH") ?? "";
            foreach (string entry in pathEnv.Split(Path.PathSeparator))
            {
                if (string.IsNullOrWhiteSpace(entry)) continue;
                try
                {
                    string jw = Path.Combine(entry.Trim(), "javaw.exe");
                    if (File.Exists(jw)) return jw;
                }
                catch { }
            }
            foreach (string entry in pathEnv.Split(Path.PathSeparator))
            {
                if (string.IsNullOrWhiteSpace(entry)) continue;
                try
                {
                    string j = Path.Combine(entry.Trim(), "java.exe");
                    if (File.Exists(j)) return j;
                }
                catch { }
            }
            return null;
        }
    }
}
