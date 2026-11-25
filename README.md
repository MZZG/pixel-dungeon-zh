# Pixel Dungeon (Steam 1.9.2) 简体中文汉化版

基于 Watabou 开源原版 [Pixel Dungeon](https://github.com/watabou/pixel-dungeon)（Steam 1.9.2，`desktop-1.9.2-steam.jar`）修改的简体中文汉化项目。

本项目的中文术语表与字体渲染思路参考了开源衍生版 [Shattered Pixel Dungeon](https://github.com/00-Evan/shattered-pixel-dungeon)，并针对原版硬编码文本与 ASCII 位图字体的架构进行了引擎级改造。

> **构建声明**：本项目由 **Gemini 4H（Gemini 4 Hydrogen High）** 协助构建。

---

## 直接下载使用（Releases 发行版）

前往本仓库的 [Releases 页面](https://github.com/MZZG/pixel-dungeon-zh/releases) 下载编译好的文件即可直接游玩：

### 方式一：脱离 Steam 独立运行（无需启动 Steam）

1. 在 Releases 中同时下载 `desktop-1.9.2-steam-zh.jar` 与 `PixelDungeon-ZH-Launcher.exe`，放在同一文件夹中。
2. Windows 用户直接双击 `PixelDungeon-ZH-Launcher.exe` 即可离线启动游戏（无需 Steam 客户端，也支持使用系统已安装的 Java 8+ 或目录内自带的 `jre`）。
3. macOS / Linux 用户或命令行启动可使用：
   ```bash
   java -Xmx1G -cp desktop-1.9.2-steam-zh.jar com.watabou.pd.desktop.StandaloneLauncher
   ```
   （若从原版入口 `DesktopLauncher` 启动，传入 `--no-steam` 参数同样可脱离 Steam 运行。）

### 方式二：替换进 Steam 游戏目录（保留 Steam 成就与统计）

1. 打开本地的 Steam 游戏目录（例如 `.../SteamLibrary/steamapps/common/Pixel Dungeon/`）。
2. 备份原目录下的 `desktop-1.9.2-steam.jar`（例如重命名为 `desktop-1.9.2-steam.jar.bak`）。
3. 下载 `desktop-1.9.2-steam-zh.jar`，将其重命名为 `desktop-1.9.2-steam.jar` 并放入游戏目录。
4. 通过 Steam 或运行目录内的 `PD.exe` 启动游戏（此方式需保持 Steam 运行）。

---

## 主要特性

1. **混合动态中文字体图集（`BitmapText.java`）**
   * **ASCII 字符与数字**：完整保留原版 `font1x.png` ~ `font3x.png` 像素字形，维持原汁原味的像素风格。
   * **中文及全角符号**：按需通过 Java2D 光栅化 `assets_extra/fonts/droid_sans.ttf` 并自动生成 1px 黑色投影，增量写入 `1024x1024` 纹理图集，兼容原版卷轴反色着色器（`WndStory`）。
   * 无需额外 native 动态链接库，跨平台兼容 Java 8 及更高版本。

2. **中文自动换行与排版（`BitmapTextMultiline.java` / `GameLog.java`）**
   * 实现 CJK 逐字换行、英文/数字连词不断行、开/闭标点禁则（避免句号、逗号出现在行首，或左括号出现在行尾）。
   * 保持 `Highlighter`（`_高亮_` 标记）与 `LineSplitter` 的字符索引完全对齐。
   * 在 `GameLog` 中适配中文句末标点（`。！？…` 等），避免末尾附加多余的英文句号。

3. **游戏内文本与语法适配（`src/`）**
   * 移除英文冠词前缀拼接（`Utils.indefinite`），重整战斗日志、武器/护甲详情、死因统计的语序与占位符。
   * 在 `WndItem` 中通过 `actionLabel()` 将物品操作按钮显示为中文，同时保持内部 `AC_*` 动作常量不变，确保快捷栏、背包筛选与存档 100% 兼容。
   * 覆盖主界面、菜单设置、键位说明、英雄职业/转职、怪物/NPC 对话、道具/药剂/卷轴/法杖/戒指、地形、植物、徽章、章节剧情等全部文本。

4. **独立离线启动支持（`StandaloneLauncher.java` / `OfflineDesktopSupport.java`）**
   * 新增不依赖 `steamworks4j` 的独立启动入口与 Windows 图形化启动器源码（`tools/launcher/StandaloneLauncher.cs`）。

---

## 目录结构

```text
pixel-dungeon-zh/
├── assets_extra/
│   └── fonts/droid_sans.ttf              # CJK 字体文件
├── src/                                  # 修改与汉化后的 Java 源码（312 个文件）
└── tools/
    ├── build.py                          # 跨平台自动构建与打包脚本
    ├── patch_engine.py                   # 源码补丁辅助工具
    ├── patches_part1~4.py                # 各模块汉化补丁记录
    └── launcher/StandaloneLauncher.cs    # 脱离 Steam 的 Windows 启动器源码
```

---

## 自行从源码编译

> 注：本代码仓库仅保存修改后的源码、字体资源与构建工具；如需自行编译，需要在本地准备原版 `desktop-1.9.2-steam.jar`。

### 依赖环境

* **JDK 8 或更高版本**（需包含 `javac` 编译器，脚本会自动以 Java 8 字节码目标编译）
* **Python 3.7+**
* **Steam 版 Pixel Dungeon**（含 `desktop-1.9.2-steam.jar`）

### 构建步骤

1. 克隆本仓库并进入目录：
   ```bash
   git clone https://github.com/MZZG/pixel-dungeon-zh.git
   cd pixel-dungeon-zh
   ```

2. 运行构建脚本（会自动尝试查找本地 Steam 目录中的 `desktop-1.9.2-steam.jar`）：
   ```bash
   python tools/build.py
   ```
   若游戏安装在自定义路径，可通过 `--jar` 指定（或设置环境变量 `PD_JAR`）：
   ```bash
   python tools/build.py --jar "/path/to/Pixel Dungeon/desktop-1.9.2-steam.jar"
   ```
   构建完成后会在 `dist/desktop-1.9.2-steam.jar` 生成汉化版 JAR。

3. **一键安装到游戏目录（自动备份原文件为 `.bak`）**：
   ```bash
   python tools/build.py --install
   ```

4. **恢复英文原版**：
   ```bash
   python tools/build.py --restore
   ```

5. **编译 Windows 独立启动器（可选）**：
   使用 Windows 自带的 .NET `csc.exe` 即可编译 `tools/launcher/StandaloneLauncher.cs`：
   ```powershell
   C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe /optimize+ /target:winexe /r:System.Windows.Forms.dll /r:System.Drawing.dll /out:dist\PixelDungeon-ZH-Launcher.exe tools\launcher\StandaloneLauncher.cs
   ```

---

## 致谢与许可

* 原作 **Pixel Dungeon** by [Watabou](https://github.com/watabou/pixel-dungeon)
* 中文术语与字体参考 **Shattered Pixel Dungeon** by [00-Evan](https://github.com/00-Evan/shattered-pixel-dungeon)
* 本项目遵循 **GPL-3.0** 开源协议，并由 **Gemini 4H** 协助构建。
