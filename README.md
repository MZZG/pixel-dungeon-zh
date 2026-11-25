# Pixel Dungeon (Steam 1.9.2) 简体中文汉化版

基于 Watabou 开源原版 [Pixel Dungeon](https://github.com/watabou/pixel-dungeon)（Steam 1.9.2，`desktop-1.9.2-steam.jar`）修改的简体中文汉化项目。

本项目的中文术语表与字体渲染思路参考了开源衍生版 [Shattered Pixel Dungeon](https://github.com/00-Evan/shattered-pixel-dungeon)，并针对原版硬编码文本与 ASCII 位图字体的架构进行了引擎级改造。

---

## 直接下载使用（Releases 发行版）

如果你不想自己配置 JDK 和 Python 编译，可以直接前往本仓库的 [Releases 页面](https://github.com/MZZG/pixel-dungeon-zh/releases) 下载编译好的文件：

1. 下载发行版中的 `desktop-1.9.2-steam-zh.jar`（已编译完成的简体中文版游戏包）；发行版中同时附带了原版二进制 `desktop-1.9.2-steam-original.jar` 供备份/对照。
2. 打开你本地的游戏安装目录（例如 `.../SteamLibrary/steamapps/common/Pixel Dungeon/`）。
3. 将原目录下的 `desktop-1.9.2-steam.jar` 重命名备份（例如改为 `desktop-1.9.2-steam.jar.bak`）。
4. 将下载的 `desktop-1.9.2-steam-zh.jar` 改名为 `desktop-1.9.2-steam.jar` 并放入该目录。
5. 通过 Steam 或双击 `PD.exe` 启动游戏即可（请确保 Steam 处于运行状态，且不要直接用 64 位系统 Java 双击 `.jar`，`PD.exe` 会自动调用自带的 32 位 `jre`）。

---

## 主要特性

1. **混合动态中文字体图集（`BitmapText.java`）**
   * **ASCII 字符与数字**：完整保留原版 `font1x.png` ~ `font3x.png` 像素字形，维持原汁原味的像素风格。
   * **中文及全角符号**：按需通过 Java2D 光栅化 `assets_extra/fonts/droid_sans.ttf` 并自动生成 1px 黑色投影，增量写入 `1024x1024` / `2048x2048` 纹理图集，兼容原版卷轴反色着色器（`WndStory`）。
   * 无需额外 native 动态链接库（如 `gdx-freetype.dll`），跨平台兼容原版自带的 Java 8 运行环境。

2. **中文自动换行与排版（`BitmapTextMultiline.java` / `GameLog.java`）**
   * 实现 CJK 逐字换行、英文/数字连词不断行、开/闭标点禁则（避免句号、逗号出现在行首，或左括号出现在行尾）。
   * 保持 `Highlighter`（`_高亮_` 标记）与 `LineSplitter` 的字符索引完全对齐。
   * 在 `GameLog` 中适配中文句末标点（`。！？…` 等），避免末尾附加多余的英文句号。

3. **游戏内文本与语法适配（`src/`）**
   * 移除英文冠词前缀拼接（`Utils.indefinite`），重整战斗日志、武器/护甲详情、死因统计的语序与占位符。
   * 在 `WndItem` 中通过 `actionLabel()` 将物品操作按钮显示为中文，同时保持内部 `AC_*` 动作常量不变，确保快捷栏、背包筛选与存档 100% 兼容。
   * 覆盖主界面、菜单设置、键位说明、英雄职业/转职、怪物/NPC 对话、道具/药剂/卷轴/法杖/戒指、地形、植物、徽章、章节剧情等全部文本。

---

## 目录结构

```text
pixel-dungeon-zh/
├── assets_extra/
│   └── fonts/droid_sans.ttf   # CJK 字体文件
├── src/                       # 修改与汉化后的 Java 源码（310 个文件）
└── tools/
    ├── build.py               # 跨平台自动构建与打包脚本
    ├── patch_engine.py        # 源码补丁辅助工具
    └── patches_part1~4.py     # 各模块汉化补丁记录
```

---

## 自行从源码编译

> 注：本代码仓库仅保存修改后的源码与字体资源，已编译的汉化包与原版游戏二进制文件请从 [Releases 页面](https://github.com/MZZG/pixel-dungeon-zh/releases) 下载。如需自行编译，你需要在本地准备原版 `desktop-1.9.2-steam.jar`。

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

2. 运行构建脚本（脚本会自动尝试查找本地 Steam 安装目录中的 `desktop-1.9.2-steam.jar`）：
   ```bash
   python tools/build.py
   ```
   如果游戏安装在自定义路径，可通过 `--jar` 手动指定（或设置环境变量 `PD_JAR`）：
   ```bash
   python tools/build.py --jar "/path/to/Pixel Dungeon/desktop-1.9.2-steam.jar"
   ```
   构建完成后会在 `dist/desktop-1.9.2-steam.jar` 生成汉化版 JAR。

3. **一键安装到游戏目录（自动备份原文件为 `.bak`）**：
   ```bash
   python tools/build.py --install
   # 如需指定游戏 JAR 路径：
   python tools/build.py --jar "/path/to/Pixel Dungeon/desktop-1.9.2-steam.jar" --install
   ```

4. **恢复英文原版**：
   ```bash
   python tools/build.py --restore
   # 或指定路径：
   python tools/build.py --jar "/path/to/Pixel Dungeon/desktop-1.9.2-steam.jar" --restore
   ```

---

## 致谢与许可

* 原作 **Pixel Dungeon** by [Watabou](https://github.com/watabou/pixel-dungeon)
* 中文术语与字体参考 **Shattered Pixel Dungeon** by [00-Evan](https://github.com/00-Evan/shattered-pixel-dungeon)
* 本项目遵循 **GPL-3.0** 开源协议。
