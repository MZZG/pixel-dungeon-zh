from patch_engine import apply_file_patches

PATCHES_PART1 = {
    "pixeldungeon/utils/Utils.java": [
        (
            '        return Character.toUpperCase(str.charAt(0)) + str.substring(1);',
            '        if (str == null || str.length() == 0) return "";\n        return Character.toUpperCase(str.charAt(0)) + str.substring(1);'
        ),
        (
            '    public static String indefinite(String noun) {\n        if (noun.length() == 0) {\n            return "a";\n        }\n        return (VOWELS.indexOf(Character.toLowerCase(noun.charAt(0))) != -1 ? "an " : "a ") + noun;\n    }',
            '    public static String indefinite(String noun) {\n        return noun == null ? "" : noun;\n    }'
        ),
    ],

    "pixeldungeon/Badges.java": [
        ('GLog.h("Badge endorsed: %s", badge.description);', 'GLog.h("徽章提升：%s", badge.description);'),
        ('GLog.h("New super badge: %s", badge.description);', 'GLog.h("获得高级徽章：%s", badge.description);'),
        ('GLog.h("New badge: %s", badge.description);', 'GLog.h("获得新徽章：%s", badge.description);'),
        ('MONSTERS_SLAIN_1("Slowly dying", "10 enemies slain", 0, 10),', 'MONSTERS_SLAIN_1("初露锋芒", "击杀 10 名敌人", 0, 10),'),
        ('MONSTERS_SLAIN_2("Slugfest", "50 enemies slain", 1, 50),', 'MONSTERS_SLAIN_2("酣战淋漓", "击杀 50 名敌人", 1, 50),'),
        ('MONSTERS_SLAIN_3("Killing zone", "150 enemies slain", 2, 150),', 'MONSTERS_SLAIN_3("杀戮地带", "击杀 150 名敌人", 2, 150),'),
        ('MONSTERS_SLAIN_4("Massacre", "250 enemies slain", 3, 250),', 'MONSTERS_SLAIN_4("横扫千军", "击杀 250 名敌人", 3, 250),'),
        ('GOLD_COLLECTED_1("Coin collector", "100 gold collected", 4, 100),', 'GOLD_COLLECTED_1("零钱收集者", "收集 100 金币", 4, 100),'),
        ('GOLD_COLLECTED_2("Gold rush", "500 gold collected", 5, 500),', 'GOLD_COLLECTED_2("淘金热", "收集 500 金币", 5, 500),'),
        ('GOLD_COLLECTED_3("Big Whoop", "2500 gold collected", 6, 2500),', 'GOLD_COLLECTED_3("满载而归", "收集 2500 金币", 6, 2500),'),
        ('GOLD_COLLECTED_4("Scrooge McDuck", "7500 gold collected", 7, 7500),', 'GOLD_COLLECTED_4("腰缠万贯", "收集 7500 金币", 7, 7500),'),
        ('LEVEL_REACHED_1("Achievement", "Level 6 reached", 8, 6),', 'LEVEL_REACHED_1("成就", "达到 6 级", 8, 6),'),
        ('LEVEL_REACHED_2("Achievement", "Level 12 reached", 9, 12),', 'LEVEL_REACHED_2("成就", "达到 12 级", 9, 12),'),
        ('LEVEL_REACHED_3("Achievement", "Level 18 reached", 10, 18),', 'LEVEL_REACHED_3("成就", "达到 18 级", 10, 18),'),
        ('LEVEL_REACHED_4("Achievement", "Level 24 reached", 11, 24),', 'LEVEL_REACHED_4("成就", "达到 24 级", 11, 24),'),
        ('ALL_POTIONS_IDENTIFIED("Potion Connoisseur", "All potions identified", 16),', 'ALL_POTIONS_IDENTIFIED("药剂鉴赏家", "鉴定所有药剂", 16),'),
        ('ALL_SCROLLS_IDENTIFIED("Scroll Scholar", "All scrolls identified", 17),', 'ALL_SCROLLS_IDENTIFIED("卷轴学者", "鉴定所有卷轴", 17),'),
        ('ALL_RINGS_IDENTIFIED("Ring Collector", "All rings identified", 18),', 'ALL_RINGS_IDENTIFIED("戒指收藏家", "鉴定所有戒指", 18),'),
        ('ALL_WANDS_IDENTIFIED("Wand Master", "All wands identified", 19),', 'ALL_WANDS_IDENTIFIED("法杖大师", "鉴定所有法杖", 19),'),
        ('ALL_ITEMS_IDENTIFIED("Achievement", "All potions, scrolls, rings & wands identified", 35, true),', 'ALL_ITEMS_IDENTIFIED("全知全识", "鉴定所有药剂、卷轴、戒指与法杖", 35, true),'),
        ('ALL_BAGS_BOUGHT("Shopaholic", "All bags bought", 23),', 'ALL_BAGS_BOUGHT("购物狂", "购买所有容器袋", 23),'),
        ('DEATH_FROM_FIRE("Achievement", "Death from fire", 24),', 'DEATH_FROM_FIRE("烈焰焚身", "死于火焰", 24),'),
        ('DEATH_FROM_POISON("Achievement", "Death from poison", 25),', 'DEATH_FROM_POISON("剧毒攻心", "死于中毒", 25),'),
        ('DEATH_FROM_GAS("Achievement", "Death from toxic gas", 26),', 'DEATH_FROM_GAS("毒气窒息", "死于毒气", 26),'),
        ('DEATH_FROM_HUNGER("Achievement", "Death from hunger", 27),', 'DEATH_FROM_HUNGER("饥肠辘辘", "死于饥饿", 27),'),
        ('DEATH_FROM_GLYPH("Achievement", "Death from an enchantment", 57),', 'DEATH_FROM_GLYPH("附魔反噬", "死于护甲刻印", 57),'),
        ('DEATH_FROM_FALLING("Achievement", "Death from falling down", 59),', 'DEATH_FROM_FALLING("失足坠落", "死于坠落", 59),'),
        ('YASD("YASD", "Death from fire, poison, toxic gas & hunger", 34, true),', 'YASD("又一次愚蠢的死法", "分别死于火焰、中毒、毒气与饥饿", 34, true),'),
        ('BOSS_SLAIN_1("GLURP-GLURP!", "1st boss slain", 12, 5),', 'BOSS_SLAIN_1("咕-咕！", "击败第 1 位头目", 12, 5),'),
        ('BOSS_SLAIN_2("Gotcha!", "2nd boss slain", 13, 10),', 'BOSS_SLAIN_2("抓到你了！", "击败第 2 位头目", 13, 10),'),
        ('BOSS_SLAIN_3("Unauthorised personnel detected", "3rd boss slain", 14, 15),', 'BOSS_SLAIN_3("发现未授权人员", "击败第 3 位头目", 14, 15),'),
        ('BOSS_SLAIN_4("How dare you!", "4th boss slain", 15, 20),', 'BOSS_SLAIN_4("你好大的胆子！", "击败第 4 位头目", 15, 20),'),
        ('BOSS_SLAIN_1_ALL_CLASSES("Jack of all trades", "1st boss slain by Warrior, Mage, Rogue & Huntress", 32, true),', 'BOSS_SLAIN_1_ALL_CLASSES("万事通", "分别用战士、法师、盗贼和女猎手击败第 1 位头目", 32, true),'),
        ('BOSS_SLAIN_3_ALL_SUBCLASSES("Master of all trades", "3rd boss slain by Gladiator, Berserker, Warlock, Battlemage, Freerunner, Assassin, Sniper & Warden", 33, true),', 'BOSS_SLAIN_3_ALL_SUBCLASSES("全能大师", "用全部 8 个转职职业击败第 3 位头目", 33, true),'),
        ('RING_OF_HAGGLER("Haggler", "Ring of Haggler obtained", 20),', 'RING_OF_HAGGLER("讨价还价", "获得商贾之戒", 20),'),
        ('RING_OF_THORNS("Thorns", "Ring of Thorns obtained", 21),', 'RING_OF_THORNS("荆棘", "获得荆棘之戒", 21),'),
        ('STRENGTH_ATTAINED_1("Achievement", "13 points of Strength attained", 40, 13),', 'STRENGTH_ATTAINED_1("成就", "力量达到 13 点", 40, 13),'),
        ('STRENGTH_ATTAINED_2("Achievement", "15 points of Strength attained", 41, 15),', 'STRENGTH_ATTAINED_2("成就", "力量达到 15 点", 41, 15),'),
        ('STRENGTH_ATTAINED_3("Achievement", "17 points of Strength attained", 42, 17),', 'STRENGTH_ATTAINED_3("成就", "力量达到 17 点", 42, 17),'),
        ('STRENGTH_ATTAINED_4("Achievement", "19 points of Strength attained", 43, 19),', 'STRENGTH_ATTAINED_4("成就", "力量达到 19 点", 43, 19),'),
        ('FOOD_EATEN_1("Achievement", "10 pieces of food eaten", 44, 10),', 'FOOD_EATEN_1("成就", "食用 10 份食物", 44, 10),'),
        ('FOOD_EATEN_2("Achievement", "20 pieces of food eaten", 45, 20),', 'FOOD_EATEN_2("成就", "食用 20 份食物", 45, 20),'),
        ('FOOD_EATEN_3("Achievement", "30 pieces of food eaten", 46, 30),', 'FOOD_EATEN_3("成就", "食用 30 份食物", 46, 30),'),
        ('FOOD_EATEN_4("Achievement", "40 pieces of food eaten", 47, 40),', 'FOOD_EATEN_4("成就", "食用 40 份食物", 47, 40),'),
        ('ITEM_LEVEL_1("Uncommon", "Item of level 3 acquired", 48, 3),', 'ITEM_LEVEL_1("稀有", "获得强化 +3 物品", 48, 3),'),
        ('ITEM_LEVEL_2("Rare", "Item of level 6 acquired", 49, 6),', 'ITEM_LEVEL_2("珍稀", "获得强化 +6 物品", 49, 6),'),
        ('ITEM_LEVEL_3("Epic", "Item of level 9 acquired", 50, 9),', 'ITEM_LEVEL_3("史诗", "获得强化 +9 物品", 50, 9),'),
        ('ITEM_LEVEL_4("Legendary", "Item of level 12 acquired", 51, 12),', 'ITEM_LEVEL_4("传说", "获得强化 +12 物品", 51, 12),'),
        ('RARE("Great Safari", "All rare monsters slain", 37, true),', 'RARE("珍奇狩猎", "击杀所有稀有怪物", 37, true),'),
        ('VICTORY("Rodney", "Amulet of Yendor obtained", 22),', 'VICTORY("罗德尼", "获得Yendor护符", 22),'),
        ('VICTORY_ALL_CLASSES("Achievement", "Amulet of Yendor obtained by Warrior, Mage, Rogue & Huntress", 36, true),', 'VICTORY_ALL_CLASSES("全职业制霸", "分别用战士、法师、盗贼和女猎手获得Yendor护符", 36, true),'),
        ('MASTERY_COMBO("Fencer", "7-hit combo", 31, 7),', 'MASTERY_COMBO("剑术大师", "打出 7 连击", 31, 7),'),
        ('POTIONS_COOKED_1("Apprentice alchemist", "3 potions cooked", 52, 3),', 'POTIONS_COOKED_1("炼金学徒", "炼制 3 瓶药剂", 52, 3),'),
        ('POTIONS_COOKED_2("Journeyman alchemist", "6 potions cooked", 53, 6),', 'POTIONS_COOKED_2("炼金熟练工", "炼制 6 瓶药剂", 53, 6),'),
        ('POTIONS_COOKED_3("Expert alchemist", "9 potions cooked", 54, 9),', 'POTIONS_COOKED_3("炼金专家", "炼制 9 瓶药剂", 54, 9),'),
        ('POTIONS_COOKED_4("Artisan alchemist", "12 potions cooked", 55, 12),', 'POTIONS_COOKED_4("炼金宗师", "炼制 12 瓶药剂", 55, 12),'),
        ('NO_MONSTERS_SLAIN("Pacifist", "Level completed without killing any monsters", 28),', 'NO_MONSTERS_SLAIN("和平主义者", "未击杀任何怪物通过一层", 28),'),
        ('GRIM_WEAPON("Grim Reaper", "Monster killed by a Grim weapon", 29),', 'GRIM_WEAPON("死神来了", "用死神附魔武器击杀怪物", 29),'),
        ('PIRANHAS("Your last bite!", "6 piranhas killed", 30, 6),', 'PIRANHAS("最后一口！", "击杀 6 条食人鱼", 30, 6),'),
        ('NIGHT_HUNTER("Werewolf", "15 monsters killed at nighttime", 58, 15),', 'NIGHT_HUNTER("狼人", "在夜间击杀 15 只怪物", 58, 15),'),
        ('GAMES_PLAYED_1("Achievement", "10 games played", 60, true, 10),', 'GAMES_PLAYED_1("成就", "游玩 10 局游戏", 60, true, 10),'),
        ('GAMES_PLAYED_2("Achievement", "100 games played", 61, true, 100),', 'GAMES_PLAYED_2("成就", "游玩 100 局游戏", 61, true, 100),'),
        ('GAMES_PLAYED_3("Achievement", "500 games played", 62, true, 500),', 'GAMES_PLAYED_3("成就", "游玩 500 局游戏", 62, true, 500),'),
        ('GAMES_PLAYED_4("Achievement", "2000 games played", 63, true, 2000),', 'GAMES_PLAYED_4("成就", "游玩 2000 局游戏", 63, true, 2000),'),
        ('HAPPY_END("Happy End", "Pixel Dungeon left", 38),', 'HAPPY_END("圆满结局", "携护符离开像素地牢", 38),'),
        ('CHAMPION("Champion", "Challenge won", 39, true);', 'CHAMPION("勇士", "开启挑战模式通关", 39, true);'),
        ('this("Achievement", "", -1);', 'this("成就", "", -1);'),
        ('Badge[] badge = iterator.next();', 'Badge badge = iterator.next();'),
    ],

    "pixeldungeon/Challenges.java": [
        (
            'public static final String[] NAMES = new String[]{"On diet", "Faith is my armor", "Pharmacophobia", "Barren land", "Swarm intelligence", "Into darkness", "Forbidden runes"};',
            'public static final String[] NAMES = new String[]{"缩餐节食", "信念护体", "恐药异症", "荒芜之地", "集群智能", "没入黑暗", "禁忌咒文"};'
        ),
    ],

    "pixeldungeon/Dungeon.java": [
        ('return "sewers";', 'return "下水道";'),
        ('return "prison";', 'return "监狱";'),
        ('return "caves";', 'return "洞穴";'),
        ('return "Dwarven Metropolis";', 'return "矮人都城";'),
        ('return "Demon Halls";', 'return "恶魔大厅";'),
        ('return "Sanctuary";', 'return "圣所";'),
        ('return "unknown place";', 'return "未知区域";'),
    ],

    "pixeldungeon/Journal.java": [
        ('WELL_OF_HEALTH("Well of Health"),', 'WELL_OF_HEALTH("生命之泉"),'),
        ('WELL_OF_AWARENESS("Well of Awareness"),', 'WELL_OF_AWARENESS("觉察之泉"),'),
        ('WELL_OF_TRANSMUTATION("Well of Transmutation"),', 'WELL_OF_TRANSMUTATION("嬗变之泉"),'),
        ('ALCHEMY("Alchemy pot"),', 'ALCHEMY("炼金釜"),'),
        ('GARDEN("Garden"),', 'GARDEN("静谧花圃"),'),
        ('STATUE("Animated statue"),', 'STATUE("活化石像"),'),
        ('SACRIFICIAL_CHAMBER("Sacrificial chamber"),', 'SACRIFICIAL_CHAMBER("献祭密室"),'),
        ('GHOST("Sad ghost"),', 'GHOST("悲伤幽灵"),'),
        ('WANDMAKER("Old wandmaker"),', 'WANDMAKER("老杖匠"),'),
        ('TROLL("Troll blacksmith"),', 'TROLL("巨魔铁匠"),'),
        ('IMP("Ambitious imp");', 'IMP("野心勃勃的小恶魔");'),
    ],

    "pixeldungeon/ResultDescriptions.java": [
        ('public static final String MOB = "Killed by %s on level %d";', 'public static final String MOB = "在第 %2$d 层被%1$s杀害";'),
        ('public static final String BOSS = "Killed by the %s on level %d";', 'public static final String BOSS = "在第 %2$d 层被%1$s杀害";'),
        ('public static final String WAND = "Killed by your own %s on level %d";', 'public static final String WAND = "在第 %2$d 层被自己的%1$s炸死";'),
        ('public static final String GLYPH = "Killed by the %s on level %d";', 'public static final String GLYPH = "在第 %2$d 层死于%1$s";'),
        ('public static final String TRAP = "Killed by discharge of %s on level %d";', 'public static final String TRAP = "在第 %2$d 层死于%1$s触发";'),
        ('public static final String BURNING = "Burned to death on level %d";', 'public static final String BURNING = "在第 %d 层被烧死";'),
        ('public static final String HUNGER = "Starved to death on level %d";', 'public static final String HUNGER = "在第 %d 层饿死";'),
        ('public static final String POISON = "Died from poison on level %d";', 'public static final String POISON = "在第 %d 层中毒身亡";'),
        ('public static final String GAS = "Died from toxic gas on level %d";', 'public static final String GAS = "在第 %d 层因毒气窒息而死";'),
        ('public static final String BLEEDING = "Bled to death on level %d";', 'public static final String BLEEDING = "在第 %d 层失血过多而死";'),
        ('public static final String OOZE = "Killed by a caustic ooze on level %d";', 'public static final String OOZE = "在第 %d 层被腐蚀淤泥吞噬";'),
        ('public static final String FALL = "Fell to death on level %d";', 'public static final String FALL = "在第 %d 层坠落摔死";'),
        ('public static final String WIN = "Obtained the Amulet of Yendor";', 'public static final String WIN = "获得Yendor护符";'),
    ],

    "pixeldungeon/input/GameAction.java": [
        ('HERO_INFO("Hero Info"),', 'HERO_INFO("英雄信息"),'),
        ('CATALOGUS("Catalogus"),', 'CATALOGUS("图鉴"),'),
        ('JOURNAL("Journal"),', 'JOURNAL("日志"),'),
        ('REST("Wait"),', 'REST("等待"),'),
        ('SEARCH("Search"),', 'SEARCH("搜索"),'),
        ('CELL_INFO("Examine Cell"),', 'CELL_INFO("查看格子"),'),
        ('RESUME("Resume Motion"),', 'RESUME("恢复行进"),'),
        ('BACKPACK("Inventory"),', 'BACKPACK("打开背包"),'),
        ('QUICKSLOT_PRIMARY("Quickslot (primary)"),', 'QUICKSLOT_PRIMARY("主快捷栏"),'),
        ('QUICKSLOT_SECONDARY("Quickslot (secondary)"),', 'QUICKSLOT_SECONDARY("副快捷栏"),'),
        ('TAG_ATTACK("Attack"),', 'TAG_ATTACK("攻击"),'),
        ('TAG_DANGER("Visible Enemies"),', 'TAG_DANGER("视野内敌人"),'),
        ('ZOOM_IN("Zoom In"),', 'ZOOM_IN("放大视野"),'),
        ('ZOOM_OUT("Zoom Out"),', 'ZOOM_OUT("缩小视野"),'),
        ('ZOOM_DEFAULT("Default Zoom"),', 'ZOOM_DEFAULT("默认缩放"),'),
        ('MOVE_UP("Move North"),', 'MOVE_UP("向上移动"),'),
        ('MOVE_DOWN("Move South"),', 'MOVE_DOWN("向下移动"),'),
        ('MOVE_LEFT("Move West"),', 'MOVE_LEFT("向左移动"),'),
        ('MOVE_RIGHT("Move East"),', 'MOVE_RIGHT("向右移动"),'),
        ('MOVE_TOP_LEFT("Move NW"),', 'MOVE_TOP_LEFT("向左上移动"),'),
        ('MOVE_TOP_RIGHT("Move NE"),', 'MOVE_TOP_RIGHT("向右上移动"),'),
        ('MOVE_BOTTOM_LEFT("Move SW"),', 'MOVE_BOTTOM_LEFT("向左下移动"),'),
        ('MOVE_BOTTOM_RIGHT("Move SE"),', 'MOVE_BOTTOM_RIGHT("向右下移动"),'),
        ('OPERATE("Current Cell"),', 'OPERATE("调查脚下"),'),
        ('THROW("Throw an item"),', 'THROW("投掷物品"),'),
        ('DRINK("Drink a potion"),', 'DRINK("饮用药剂"),'),
        ('READ("Read a scroll"),', 'READ("阅读卷轴"),'),
        ('ZAP("Zap a wand"),', 'ZAP("释放法杖"),'),
        ('PLANT("Plant a seed"),', 'PLANT("种植种子"),'),
    ],

    "pixeldungeon/input/PDInputProcessor.java": [
        ('return "Mouse left";', 'return "鼠标左键";'),
        ('return "Mouse right";', 'return "鼠标右键";'),
        ('return "Mouse middle";', 'return "鼠标中键";'),
        ('return "Mouse " + button;', 'return "鼠标按键 " + button;'),
        ('return "Controller " + button;', 'return "手柄按键 " + button;'),
    ],

    "pixeldungeon/scenes/AboutScene.java": [
        (
            'private static final String TXT = "Original code & graphics: Watabou\\nLibGDX port: Arcnor\\nSteam version: Watabou & Arcnor\\nMusic: Cube_Code\\n\\nThis game is inspired by Brian Walker\'s Brogue. Try it on Windows, Mac OS or Linux - it\'s awesome! ;)\\n\\nCreated using libGDX and LWJGL.\\n\\nPlease visit official website for additional info:";',
            'private static final String TXT = "原作程序与美术：Watabou\\nLibGDX 移植：Arcnor\\nSteam 版：Watabou & Arcnor\\n音乐：Cube_Code\\n\\n本作灵感源自 Brian Walker 的 Brogue。强烈推荐在 Windows、Mac OS 或 Linux 上试玩！;)\\n\\n基于 libGDX 与 LWJGL 开发。\\n\\n更多信息请访问官方网站：";'
        ),
    ],

    "pixeldungeon/scenes/AmuletScene.java": [
        ('private static final String TXT_EXIT = "Let\'s call it a day";', 'private static final String TXT_EXIT = "现在就到此为止吧";'),
        ('private static final String TXT_STAY = "I\'m not done yet";', 'private static final String TXT_STAY = "这一切还不该结束";'),
        (
            'private static final String TXT = "You finally hold it in your hands, the Amulet of Yendor. Using its power you can take over the world or bring peace and prosperity to people or whatever. Anyway, your life will change forever and this game will end here. Or you can stay a mere mortal a little longer.";',
            'private static final String TXT = "你终于将Yendor护符握在了手中。借助它的力量，你可以征服世界，或为世人带来和平与繁荣，随心所欲。无论如何，你的人生都将彻底改变，而这场冒险也将就此落幕。又或者，你也可以选择作为一介凡人再停留片刻。";'
        ),
        ('Dungeon.win("Obtained the Amulet of Yendor");', 'Dungeon.win("获得Yendor护符");'),
    ],

    "pixeldungeon/scenes/BadgesScene.java": [
        ('private static final String TXT_TITLE = "Your Badges";', 'private static final String TXT_TITLE = "你的徽章";'),
    ],

    "pixeldungeon/scenes/GameScene.java": [
        ('private static final String TXT_WELCOME = "Welcome to the level %d of Pixel Dungeon!";', 'private static final String TXT_WELCOME = "欢迎来到像素地牢第 %d 层！";'),
        ('private static final String TXT_WELCOME_BACK = "Welcome back to the level %d of Pixel Dungeon!";', 'private static final String TXT_WELCOME_BACK = "欢迎回到像素地牢第 %d 层！";'),
        ('private static final String TXT_NIGHT_MODE = "Be cautious, since the dungeon is even more dangerous at night!";', 'private static final String TXT_NIGHT_MODE = "请务必小心，夜晚的地牢会变得更加危险！";'),
        ('private static final String TXT_CHASM = "Your steps echo across the dungeon.";', 'private static final String TXT_CHASM = "你的脚步声在地牢中回荡不绝。";'),
        ('private static final String TXT_WATER = "You hear the water splashing around you.";', 'private static final String TXT_WATER = "你听到四周传来水流飞溅的声响。";'),
        ('private static final String TXT_GRASS = "The smell of vegetation is thick in the air.";', 'private static final String TXT_GRASS = "浓郁的草木气息弥漫在空气中。";'),
        ('private static final String TXT_SECRETS = "The atmosphere hints that this floor hides many secrets.";', 'private static final String TXT_SECRETS = "这层地牢的气氛暗示着潜藏其中的秘密。";'),
        ('GameScene.show(new WndMessage("You don\'t know what is there."));', 'GameScene.show(new WndMessage("你不清楚那里有什么。"));'),
        ('            for (Item item : dropped) {', '            for (Object obj : dropped) {\n                Item item = (Item)obj;'),
    ],

    "pixeldungeon/scenes/InterlevelScene.java": [
        ('private static final String TXT_DESCENDING = "Descending...";', 'private static final String TXT_DESCENDING = "正在下楼...";'),
        ('private static final String TXT_ASCENDING = "Ascending...";', 'private static final String TXT_ASCENDING = "正在上楼...";'),
        ('private static final String TXT_LOADING = "Loading...";', 'private static final String TXT_LOADING = "正在载入...";'),
        ('private static final String TXT_RESURRECTING = "Resurrecting...";', 'private static final String TXT_RESURRECTING = "正在复活...";'),
        ('private static final String TXT_RETURNING = "Returning...";', 'private static final String TXT_RETURNING = "正在返回...";'),
        ('private static final String TXT_FALLING = "Falling...";', 'private static final String TXT_FALLING = "正在坠落...";'),
        ('private static final String ERR_FILE_NOT_FOUND = "File not found. For some reason.";', 'private static final String ERR_FILE_NOT_FOUND = "出于某些原因，存档文件未找到。";'),
        ('private static final String ERR_GENERIC = "Something went wrong...";', 'private static final String ERR_GENERIC = "似乎出了些问题...";'),
        (
            '                float f;\n                this.message.alpha(1.0f - p);\n                this.timeLeft -= Game.elapsed;\n                if (!(f <= 0.0f)) break;',
            '                this.message.alpha(1.0f - p);\n                if ((this.timeLeft -= Game.elapsed) > 0.0f) break;'
        ),
        (
            '                float f;\n                this.message.alpha(p);\n                if (mode == Mode.CONTINUE || mode == Mode.DESCEND && Dungeon.depth == 1) {\n                    Music.INSTANCE.volume(p);\n                }\n                this.timeLeft -= Game.elapsed;\n                if (!(f <= 0.0f)) break;',
            '                this.message.alpha(p);\n                if (mode == Mode.CONTINUE || mode == Mode.DESCEND && Dungeon.depth == 1) {\n                    Music.INSTANCE.volume(p);\n                }\n                if ((this.timeLeft -= Game.elapsed) > 0.0f) break;'
        ),
    ],

    "pixeldungeon/scenes/IntroScene.java": [
        (
            'private static final String TEXT = "Many heroes of all kinds ventured into the Dungeon before you. Some of them have returned with treasures and magical artifacts, most have never been heard of since. But none have succeeded in retrieving the Amulet of Yendor, which is told to be hidden in the depths of the Dungeon.\\n\\nYou consider yourself ready for the challenge, but most importantly, you feel that fortune smiles on you. It\'s time to start your own adventure!";',
            'private static final String TEXT = "在你之前，已有形形色色的英雄踏入过这座地牢。其中有些人带着财宝与魔法神器满载而归，但更多的人从此音讯全无。然而，从未有人成功取回传说藏在地牢最深处的Yendor护符。\\n\\n你觉得自己已经做好了迎接挑战的准备，更重要的是，你感到幸运女神正对你微笑。是时候开启属于你自己的冒险了！";'
        ),
    ],

    "pixeldungeon/scenes/RankingsScene.java": [
        ('private static final String TXT_TITLE = "Top Rankings";', 'private static final String TXT_TITLE = "英雄榜";'),
        ('private static final String TXT_TOTAL = "Games played: ";', 'private static final String TXT_TOTAL = "总游戏次数：";'),
        ('private static final String TXT_NO_GAMES = "No games have been played yet.";', 'private static final String TXT_NO_GAMES = "目前为止还没有已经结束的游戏。";'),
        ('private static final String TXT_TOTAL_TOOLTIP = "(cb)_Games won_ / Total games played";', 'private static final String TXT_TOTAL_TOOLTIP = "(cb)_通关次数_ / 总游戏次数";'),
        ('private static final String TXT_NO_INFO = "No additional information";', 'private static final String TXT_NO_INFO = "没有额外信息";'),
    ],

    "pixeldungeon/scenes/StartScene.java": [
        ('private static final String TXT_TITLE = "SELECT YOUR HERO";', 'private static final String TXT_TITLE = "选择你的英雄";'),
        ('private static final String TXT_TITLE = "LEVEL %d %s";', 'private static final String TXT_TITLE = "%d级 %s";'),
        ('private static final String TXT_DEPTH = "%s, depth %d";', 'private static final String TXT_DEPTH = "%s，第 %d 层";'),
        ('private static final String TXT_BADGES = "Badges:";', 'private static final String TXT_BADGES = "徽章：";'),
        ('private static final String TXT_LOAD_GAME = "Load Game";', 'private static final String TXT_LOAD_GAME = "载入游戏";'),
        ('private static final String TXT_ABANDON_GAME = "Abandon Game";', 'private static final String TXT_ABANDON_GAME = "放弃存档";'),
        ('private static final String TXT_REALLY = "Do you really want to abandon this game?";', 'private static final String TXT_REALLY = "你真的要放弃当前游戏吗？";'),
        ('private static final String TXT_WARNING = "Your current game progress will be erased.";', 'private static final String TXT_WARNING = "你当前的冒险进度将被永久清除。";'),
        ('private static final String TXT_YES = "Yes, abandon this game";', 'private static final String TXT_YES = "是的，放弃这局游戏";'),
        ('private static final String TXT_NO = "No, I changed my mind";', 'private static final String TXT_NO = "不，我改主意了";'),
        ('private static final String TXT_DESC_TOOLTIP = "Click to see this class perks";', 'private static final String TXT_DESC_TOOLTIP = "点击查看该职业特性";'),
        (
            'String modeInfo = (info.legacy ? "Legacy mode, " : "Standard mode, ") + (info.challenges > 0 ? "challenges:" : "no challenges");',
            'String modeInfo = (info.legacy ? "传统模式，" : "标准模式，") + (info.challenges > 0 ? "已开启挑战：" : "无挑战");'
        ),
        ('list.append(list.length() > 0 ? ", " : "");', 'list.append(list.length() > 0 ? "、" : "");'),
        ('private static final String TXT_NEW_GAME = "Start New Game";', 'private static final String TXT_NEW_GAME = "踏上征途";'),
        ('private static final String TXT_LEGACY = "Legacy Mode";', 'private static final String TXT_LEGACY = "传统模式";'),
        (
            'private static final String TXT_LEGACY_TOOLTIP = "In this mode items won\'t degrade over time, but no badges\\nwill be granted and no results will get into the rankings.";',
            'private static final String TXT_LEGACY_TOOLTIP = "在此模式下装备不会因使用而磨损损坏，\\n但无法获得徽章，成绩也不会计入排行榜。";'
        ),
        ('private static final String TXT_CHALLENGES = "Challenges";', 'private static final String TXT_CHALLENGES = "挑战";'),
        (
            'private static final String TXT_CHALLENGES_TOOLTIP = "To unlock \\"Challenges\\", win the game\\nwith any character class.";',
            'private static final String TXT_CHALLENGES_TOOLTIP = "使用任意职业通关一次游戏即可解锁\\n\\"挑战\\"模式。";'
        ),
        ('private static final String TXT_NO_MASTERY = "You have not yet obtained the Tome of Mastery with this class.";', 'private static final String TXT_NO_MASTERY = "你尚未用该职业获得过精通之书。";'),
        ('private static final String TXT_UNLOCK = "To unlock this character class, slay the 3rd boss with any other class";', 'private static final String TXT_UNLOCK = "使用任意其他职业击败第 3 位头目即可解锁该职业";'),
        ('this.checked(PixelDungeon.challenges() > 0);', 'NewGamePane.this.btnChallenges.checked(PixelDungeon.challenges() > 0);'),
        (
            '                text = PixelScene.createMultiline(TXT_UNLOCK, 6.0f);\n                ((BitmapTextMultiline)text).maxWidth = (int)(this.width - 4.0f);\n                ((BitmapTextMultiline)text).measure();\n                pos = ((Visual)text).height() < this.btnStart.height() ? this.btnStart.top() + (this.btnStart.height() - ((Visual)text).height()) / 2.0f : this.btnStart.bottom() - ((Visual)text).height();\n                for (BitmapText line : new BitmapTextMultiline.LineSplitter((BitmapTextMultiline)text).split()) {',
            '                BitmapTextMultiline unlockText = PixelScene.createMultiline(TXT_UNLOCK, 6.0f);\n                unlockText.maxWidth = (int)(this.width - 4.0f);\n                unlockText.measure();\n                pos = unlockText.height() < this.btnStart.height() ? this.btnStart.top() + (this.btnStart.height() - unlockText.height()) / 2.0f : this.btnStart.bottom() - unlockText.height();\n                for (BitmapText line : unlockText.new LineSplitter().split()) {'
        ),
    ],

    "pixeldungeon/scenes/SurfaceScene.java": [
        ('RedButton gameOver = new RedButton("Game Over"){', 'RedButton gameOver = new RedButton("游戏结束"){'),
    ],

    "pixeldungeon/ui/ExitButton.java": [
        ('return Game.scene() instanceof TitleScene ? "Exit _(Esc)_" : "Back _(Esc)_";', 'return Game.scene() instanceof TitleScene ? "退出 _(Esc)_" : "返回 _(Esc)_";'),
    ],

    "pixeldungeon/ui/ItemSlot.java": [
        ('return "Chest";', 'return "宝箱";'),
        ('return "Locked chest";', 'return "上锁的宝箱";'),
        ('return "Tomb";', 'return "坟墓";'),
        ('return "Skeletal remains";', 'return "遗骸";'),
    ],

    "pixeldungeon/ui/PrefsButton.java": [
        ('return "Settings";', 'return "设置";'),
    ],

    "pixeldungeon/ui/QuickSlot.java": [
        ('private static final String TXT_SELECT_ITEM = "Select an item for the quickslot";', 'private static final String TXT_SELECT_ITEM = "选择要放入快捷栏的物品";'),
        ('private static final String TXT_TOOLTIP = "Click to assign an item\\nto this quickslot";', 'private static final String TXT_TOOLTIP = "点击为此快捷栏\\n指定一件物品";'),
        ('return item != null ? item : Item.virtual((Class)content);', 'return item != null ? (Item)item : Item.virtual((Class)content);'),
    ],

    "pixeldungeon/ui/StatusPane.java": [
        ('return "(Lc)Menu _(Esc)_";', 'return "(Lc)菜单 _(Esc)_";'),
    ],

    "pixeldungeon/ui/Toolbar.java": [
        ('return "Select a cell to examine";', 'return "选择要查看的格子";'),
    ],

    # Batch 2: Windows
    "pixeldungeon/windows/WndBag.java": [
        ('private static final String TXT_THROW = "Select an item to throw";', 'private static final String TXT_THROW = "选择要投掷的物品";'),
        ('private static final String TXT_DRINK = "Select a potion to drink";', 'private static final String TXT_DRINK = "选择要饮用的药剂";'),
        ('private static final String TXT_READ = "Select a scroll to read";', 'private static final String TXT_READ = "选择要阅读的卷轴";'),
        ('private static final String TXT_ZAP = "Select a wand to zap";', 'private static final String TXT_ZAP = "选择要释放的法杖";'),
        ('private static final String TXT_PLANT = "Select a seed to plant";', 'private static final String TXT_PLANT = "选择要种植的种子";'),
        (
            '                        if (!Dungeon.legacy && WndBag.access$100().owner.isAlive() && item.isUpgradable() && item.levelKnown) {\n                            this.durability = new ColorBlock[3];\n                            nBars = (int)GameMath.gate(0.0f, Math.round(3.0f * (float)item.durability() / (float)item.maxDurability()), 3.0f);\n                            for (i = 0; i < nBars; ++i) {',
            '                        if (!Dungeon.legacy && WndBag.lastBag.owner.isAlive() && item.isUpgradable() && item.levelKnown) {\n                            this.durability = new ColorBlock[3];\n                            int nBars = (int)GameMath.gate(0.0f, Math.round(3.0f * (float)item.durability() / (float)item.maxDurability()), 3.0f);\n                            int i;\n                            for (i = 0; i < nBars; ++i) {'
        ),
        (
            '                    if (WndBag.access$200(WndBag.this) == Mode.QUICKSLOT && item.defaultAction != null || WndBag.access$200(WndBag.this) == Mode.FOR_SALE && item.price() > 0 && (!item.isEquipped(Dungeon.hero) || !item.cursed) || WndBag.access$200(WndBag.this) == Mode.UPGRADEABLE && item.isUpgradable() || WndBag.access$200(WndBag.this) == Mode.UNIDENTIFED && !item.isIdentified() || WndBag.access$200(WndBag.this) == Mode.WEAPON && (item instanceof MeleeWeapon || item instanceof Boomerang) || WndBag.access$200(WndBag.this) == Mode.ARMOR && item instanceof Armor || WndBag.access$200(WndBag.this) == Mode.ENCHANTABLE && (item instanceof MeleeWeapon || item instanceof Boomerang || item instanceof Armor) || WndBag.access$200(WndBag.this) == Mode.DRINK && item instanceof Potion || WndBag.access$200(WndBag.this) == Mode.READ && item instanceof Scroll || (WndBag.access$200(WndBag.this) == Mode.WAND || WndBag.access$200(WndBag.this) == Mode.ZAP) && (WndBag.wandClass != null ? WndBag.wandClass.isInstance(item) != false : item instanceof Wand != false)) ** GOTO lbl-1000\n                    if ((WndBag.access$200(WndBag.this) == Mode.SEED || WndBag.access$200(WndBag.this) == Mode.PLANT) && item instanceof Plant.Seed || WndBag.access$200(WndBag.this) == Mode.ALL || WndBag.access$200(WndBag.this) == Mode.THROW) lbl-1000:\n                    // 2 sources\n\n                    {\n                        v0 = true;\n                    } else {\n                        v0 = false;\n                    }\n                    this.enable(v0);',
            '                    boolean enabled = WndBag.this.mode == Mode.QUICKSLOT && item.defaultAction != null || WndBag.this.mode == Mode.FOR_SALE && item.price() > 0 && (!item.isEquipped(Dungeon.hero) || !item.cursed) || WndBag.this.mode == Mode.UPGRADEABLE && item.isUpgradable() || WndBag.this.mode == Mode.UNIDENTIFED && !item.isIdentified() || WndBag.this.mode == Mode.WEAPON && (item instanceof MeleeWeapon || item instanceof Boomerang) || WndBag.this.mode == Mode.ARMOR && item instanceof Armor || WndBag.this.mode == Mode.ENCHANTABLE && (item instanceof MeleeWeapon || item instanceof Boomerang || item instanceof Armor) || WndBag.this.mode == Mode.DRINK && item instanceof Potion || WndBag.this.mode == Mode.READ && item instanceof Scroll || (WndBag.this.mode == Mode.WAND || WndBag.this.mode == Mode.ZAP) && (WndBag.wandClass != null ? WndBag.wandClass.isInstance(item) : item instanceof Wand) || (WndBag.this.mode == Mode.SEED || WndBag.this.mode == Mode.PLANT) && item instanceof Plant.Seed || WndBag.this.mode == Mode.ALL || WndBag.this.mode == Mode.THROW;\n                    this.enable(enabled);'
        ),
        ('super(WndBag.this);', 'super();'),
        ('    static /* synthetic */ Bag access$100() {\n        return lastBag;\n    }\n\n', ''),
    ],

    "pixeldungeon/windows/WndBlacksmith.java": [
        ('private static final String TXT_PROMPT = "Ok, a deal is a deal, dat\'s what I can do for you: I can reforge 2 items and turn them into one of a better quality.";', 'private static final String TXT_PROMPT = "行，一言为定！我能为你做的是：把两件相同的物品重铸成一件品质更好的物品。";'),
        ('private static final String TXT_SELECT = "Select an item to reforge";', 'private static final String TXT_SELECT = "选择要重铸的物品";'),
        ('private static final String TXT_REFORGE = "Reforge them";', 'private static final String TXT_REFORGE = "重铸物品";'),
    ],

    "pixeldungeon/windows/WndCatalogus.java": [
        ('private static final String TXT_POTIONS = "Potions";', 'private static final String TXT_POTIONS = "药剂";'),
        ('private static final String TXT_SCROLLS = "Scrolls";', 'private static final String TXT_SCROLLS = "卷轴";'),
        ('private static final String TXT_TITLE = "Catalogus";', 'private static final String TXT_TITLE = "物品图鉴";'),
        ('        boolean showPotions = WndCatalogus.showPotions;\n', ''),
        ('for (Class<Item> itemClass : showPotions ? Potion.getKnown() : Scroll.getKnown()) {', 'for (Class<? extends Item> itemClass : showPotions ? Potion.getKnown() : Scroll.getKnown()) {'),
        ('for (Class<Item> itemClass : showPotions ? Potion.getUnknown() : Scroll.getUnknown()) {', 'for (Class<? extends Item> itemClass : showPotions ? Potion.getUnknown() : Scroll.getUnknown()) {'),
    ],

    "pixeldungeon/windows/WndChallenges.java": [
        ('private static final String TITLE = "Challenges";', 'private static final String TITLE = "挑战";'),
    ],

    "pixeldungeon/windows/WndChooseWay.java": [
        ('String TXT_MASTERY = "Which way will you follow?";', 'String TXT_MASTERY = "你将追随哪一条道路？";'),
        ('String TXT_CANCEL = "I\'ll decide later";', 'String TXT_CANCEL = "我稍后再做决定";'),
        ('"Which way will you follow?"', '"你将追随哪一条道路？"'),
        ('new RedButton("I\'ll decide later")', 'new RedButton("我稍后再做决定")'),
        ('String TXT_REMASTERY = "Do you want to respec into %s?";', 'String TXT_REMASTERY = "你确定要转修为%s吗？";'),
        ('String TXT_OK = "Yes, I want to respec";', 'String TXT_OK = "是的，我要转修";'),
        ('String TXT_CANCEL = "Maybe later";', 'String TXT_CANCEL = "以后再说";'),
        ('"Do you want to respec into %s?"', '"你确定要转修为%s吗？"'),
        ('new RedButton("Yes, I want to respec")', 'new RedButton("是的，我要转修")'),
        ('new RedButton("Maybe later")', 'new RedButton("以后再说")'),
    ],

    "pixeldungeon/windows/WndClass.java": [
        ('private static final String TXT_MASTERY = "Mastery";', 'private static final String TXT_MASTERY = "专精";'),
        ('super(WndClass.this, label);', 'super(label);'),
    ],

    "pixeldungeon/windows/WndError.java": [
        ('private static final String TXT_TITLE = "ERROR";', 'private static final String TXT_TITLE = "错误";'),
    ],

    "pixeldungeon/windows/WndGame.java": [
        ('private static final String TXT_SETTINGS = "Settings";', 'private static final String TXT_SETTINGS = "设置";'),
        ('private static final String TXT_CHALLEGES = "Challenges";', 'private static final String TXT_CHALLEGES = "挑战";'),
        ('private static final String TXT_RANKINGS = "Rankings";', 'private static final String TXT_RANKINGS = "排行榜";'),
        ('private static final String TXT_START = "Start New Game";', 'private static final String TXT_START = "踏上征途";'),
        ('private static final String TXT_MENU = "Main Menu";', 'private static final String TXT_MENU = "主菜单";'),
        ('private static final String TXT_EXIT = "Exit Game";', 'private static final String TXT_EXIT = "退出游戏";'),
        ('private static final String TXT_RETURN = "Return to Game";', 'private static final String TXT_RETURN = "继续冒险";'),
    ],

    "pixeldungeon/windows/WndHero.java": [
        ('private static final String TXT_STATS = "Stats";', 'private static final String TXT_STATS = "属性";'),
        ('private static final String TXT_BADGES = "Badges";', 'private static final String TXT_BADGES = "徽章";'),
        ('private static final String TXT_EXP = "Experience";', 'private static final String TXT_EXP = "经验";'),
        ('private static final String TXT_STR = "Strength";', 'private static final String TXT_STR = "力量";'),
        ('private static final String TXT_HEALTH = "Health";', 'private static final String TXT_HEALTH = "生命";'),
        ('private static final String TXT_DURATION = "Game Duration";', 'private static final String TXT_DURATION = "游戏时长";'),
        ('private static final String TXT_SLAIN = "Enemies Slain";', 'private static final String TXT_SLAIN = "击杀敌人数";'),
        ('private static final String TXT_GOLD = "Gold Collected";', 'private static final String TXT_GOLD = "金币收集数";'),
        ('private static final String TXT_ANKHS = "Ankhs Used";', 'private static final String TXT_ANKHS = "十字架使用";'),
        ('private static final String TXT_TITLE = "Level %d %s";', 'private static final String TXT_TITLE = "%d级 %s";'),
    ],

    "pixeldungeon/windows/WndImp.java": [
        (
            'private static final String TXT_MESSAGE = "Oh yes! You are my hero!\\nRegarding your reward, I don\'t have cash with me right now, but I have something better for you. This is my family heirloom ring: my granddad took it off a dead paladin\'s finger.";',
            'private static final String TXT_MESSAGE = "哦太棒了！你简直就是我的英雄！\\n关于你的奖励，我现在没带现金，但我这有个更好的东西。这个戒指是我们的传家宝，是我爷爷从一个死掉的圣骑士手上摘下来的。";'
        ),
        ('private static final String TXT_REWARD = "Take the ring";', 'private static final String TXT_REWARD = "接受戒指";'),
        ('GLog.i("You now have %s", ((Item)reward).name());', 'GLog.i("你获得了%s", ((Item)reward).name());'),
    ],

    "pixeldungeon/windows/WndInfoCell.java": [
        ('private static final String TXT_NOTHING = "There is nothing here.";', 'private static final String TXT_NOTHING = "这里什么也没有。";'),
    ],

    "pixeldungeon/windows/WndInfoItem.java": [
        ('private static final String TXT_CHEST = "Chest";', 'private static final String TXT_CHEST = "宝箱";'),
        ('private static final String TXT_LOCKED_CHEST = "Locked chest";', 'private static final String TXT_LOCKED_CHEST = "上锁的宝箱";'),
        ('private static final String TXT_CRYSTAL_CHEST = "Crystal chest";', 'private static final String TXT_CRYSTAL_CHEST = "水晶宝箱";'),
        ('private static final String TXT_TOMB = "Tomb";', 'private static final String TXT_TOMB = "坟墓";'),
        ('private static final String TXT_SKELETON = "Skeletal remains";', 'private static final String TXT_SKELETON = "遗骸";'),
        ('private static final String TXT_WONT_KNOW = "You won\'t know what\'s inside until you open it!";', 'private static final String TXT_WONT_KNOW = "打开前你是看不见里面有什么的！";'),
        ('private static final String TXT_NEED_KEY = "You won\'t know what\'s inside until you open it! But to open it you need a golden key.";', 'private static final String TXT_NEED_KEY = "打开前你是看不见里面有什么的！你需要一枚金钥匙才能打开它。";'),
        ('private static final String TXT_INSIDE = "You can see %s inside, but to open the chest you need a golden key.";', 'private static final String TXT_INSIDE = "你能看到里面装着%s，但需要金钥匙才能打开。";'),
        ('private static final String TXT_OWNER = "This ancient tomb may contain something useful, but its owner will most certainly object to checking.";', 'private static final String TXT_OWNER = "这座古墓里或许埋葬着有用的东西，但墓主显然不会同意你去翻动它。";'),
        ('private static final String TXT_REMAINS = "This is all that\'s left from one of your predecessors. Maybe it\'s worth checking for any valuables.";', 'private static final String TXT_REMAINS = "这是你某位前辈留下的全部遗骸，或许能从里面找到些值钱的东西。";'),
    ],

    "pixeldungeon/windows/WndInfoMob.java": [
        ('builder.append("\\n\\n" + mob.state.status() + ".");', 'builder.append("\\n\\n" + mob.state.status() + "。");'),
    ],

    "pixeldungeon/windows/WndItem.java": [
        (
            'RedButton btn = new RedButton(action){',
            'RedButton btn = new RedButton(actionLabel(action)){'
        ),
        (
            '        this.resize(120, (int)(y + (x > 0.0f ? 16.0f : 0.0f)));\n    }',
            '''        this.resize(120, (int)(y + (x > 0.0f ? 16.0f : 0.0f)));
    }

    private static String actionLabel(String action) {
        if ("DROP".equals(action)) return "丢弃";
        if ("THROW".equals(action)) return "投掷";
        if ("EQUIP".equals(action)) return "装备";
        if ("UNEQUIP".equals(action)) return "卸下";
        if ("DRINK".equals(action)) return "饮用";
        if ("READ".equals(action)) return "阅读";
        if ("ZAP".equals(action)) return "释放";
        if ("PLANT".equals(action)) return "种植";
        if ("EAT".equals(action)) return "食用";
        if ("APPLY".equals(action)) return "使用";
        if ("OPEN".equals(action)) return "打开";
        if ("LIGHT".equals(action)) return "点燃";
        if ("SHATTER".equals(action)) return "摔碎";
        if ("MERGE".equals(action)) return "合并";
        if ("MINE".equals(action)) return "采矿";
        if ("REFORGE".equals(action)) return "重铸";
        if ("DISENCHANT".equals(action)) return "祛魅";
        if ("SET".equals(action)) return "设置";
        if ("RETURN".equals(action)) return "返回";
        if ("END THE GAME".equals(action)) return "结束游戏";
        if ("HEROIC LEAP".equals(action)) return "英勇之跃";
        if ("MOLTEN EARTH".equals(action)) return "熔岩大地";
        if ("SMOKE BOMB".equals(action)) return "烟雾弹";
        if ("SPECTRAL BLADES".equals(action)) return "灵魂飞刃";
        return action;
    }'''
        ),
    ],

    "pixeldungeon/windows/WndJournal.java": [
        ('private static final String TXT_TITLE = "Journal";', 'private static final String TXT_TITLE = "日志";'),
        ('private static final String TXT_NO_RECORDS = "There are no records in your journal yet.";', 'private static final String TXT_NO_RECORDS = "你的日志中还没有任何记录。";'),
        ('for (BitmapText line : new BitmapTextMultiline.LineSplitter(txtNoRecords).split()) {', 'for (BitmapText line : txtNoRecords.new LineSplitter().split()) {'),
    ],

    "pixeldungeon/windows/WndKeymap.java": [
        ('public static final String TXT_UNASSIGNED = "<None>";', 'public static final String TXT_UNASSIGNED = "<未绑定>";'),
        ('RedButton btnReset = new RedButton("Reset To Defaults"){', 'RedButton btnReset = new RedButton("恢复默认设置"){'),
        (
            'String message = "Press a key for \\"" + this.gameAction.getDescription() + "\\", or press " + ((PDInputProcessor)Game.instance.getInputProcessor()).keycodeToName(129) + " to remove the binding.";',
            'String message = "请按下用于\\"" + this.gameAction.getDescription() + "\\"的按键，或按 " + ((PDInputProcessor)Game.instance.getInputProcessor()).keycodeToName(129) + " 清除绑定。";'
        ),
    ],

    "pixeldungeon/windows/WndRanking.java": [
        ('private static final String TXT_ERROR = "Unable to load additional information";', 'private static final String TXT_ERROR = "无法载入更多信息。";'),
        ('private static final String TXT_STATS = "Stats";', 'private static final String TXT_STATS = "属性";'),
        ('private static final String TXT_ITEMS = "Items";', 'private static final String TXT_ITEMS = "物品";'),
        ('private static final String TXT_BADGES = "Badges";', 'private static final String TXT_BADGES = "徽章";'),
        ('private static final String TXT_TITLE = "Level %d %s";', 'private static final String TXT_TITLE = "%d级 %s";'),
        ('private static final String TXT_CHALLENGES = "Challenges";', 'private static final String TXT_CHALLENGES = "挑战";'),
        ('private static final String TXT_HEALTH = "Health";', 'private static final String TXT_HEALTH = "生命";'),
        ('private static final String TXT_STR = "Strength";', 'private static final String TXT_STR = "力量";'),
        ('private static final String TXT_DURATION = "Game Duration";', 'private static final String TXT_DURATION = "游戏时长";'),
        ('private static final String TXT_DEPTH = "Maximum Depth";', 'private static final String TXT_DEPTH = "最高层数";'),
        ('private static final String TXT_ENEMIES = "Enemies Slain";', 'private static final String TXT_ENEMIES = "击杀敌人数";'),
        ('private static final String TXT_GOLD = "Gold Collected";', 'private static final String TXT_GOLD = "金币收集数";'),
        ('private static final String TXT_ANKHS = "Ankhs Used";', 'private static final String TXT_ANKHS = "十字架使用";'),
        ('private static final String TXT_FOOD = "Food Eaten";', 'private static final String TXT_FOOD = "食物消耗量";'),
        ('private static final String TXT_ALCHEMY = "Potions Cooked";', 'private static final String TXT_ALCHEMY = "炼制药剂数";'),
        ('            if (value instanceof Class && (item = Dungeon.hero.belongings.getItem((Class)value)) != null) {\n                return item;\n            }', '            if (value instanceof Class && (item = Dungeon.hero.belongings.getItem((Class)value)) != null) {\n                return (Item)item;\n            }'),
        ('super(WndRanking.this, label);', 'super(label);'),
    ],

    "pixeldungeon/windows/WndResurrect.java": [
        ('private static final String TXT_MESSAGE = "You died, but you were given another chance to win this dungeon. Will you take it?";', 'private static final String TXT_MESSAGE = "你已经阵亡，但你获得了再一次征服地牢的机会。你愿意接受吗？";'),
        ('private static final String TXT_YES = "Yes, I will fight!";', 'private static final String TXT_YES = "是的，我要继续战斗！";'),
        ('private static final String TXT_NO = "No, I give up";', 'private static final String TXT_NO = "不了，我选择放弃";'),
    ],

    "pixeldungeon/windows/WndSadGhost.java": [
        ('private static final String TXT_WEAPON = "Ghost\'s weapon";', 'private static final String TXT_WEAPON = "幽灵的武器";'),
        ('private static final String TXT_ARMOR = "Ghost\'s armor";', 'private static final String TXT_ARMOR = "幽灵的护甲";'),
        ('GLog.i("You now have %s", reward.name());', 'GLog.i("你获得了%s", reward.name());'),
        ('this.ghost.yell("Farewell, adventurer!");', 'this.ghost.yell("一路顺风，冒险家！");'),
    ],

    "pixeldungeon/windows/WndSettings.java": [
        ('private static final String TXT_ZOOM_DEFAULT = "Default Zoom";', 'private static final String TXT_ZOOM_DEFAULT = "默认缩放";'),
        ('private static final String TXT_SCALE_UP = "Scale up UI";', 'private static final String TXT_SCALE_UP = "放大界面";'),
        ('private static final String TXT_MUSIC = "Music";', 'private static final String TXT_MUSIC = "背景音乐";'),
        ('private static final String TXT_SOUND = "Sound FX";', 'private static final String TXT_SOUND = "游戏音效";'),
        ('private static final String TXT_BINDINGS = "Key bindings";', 'private static final String TXT_BINDINGS = "键鼠键位";'),
        ('private static final String TXT_SWITCH_FULL = "Switch to fullscreen";', 'private static final String TXT_SWITCH_FULL = "切换为全屏模式";'),
        ('private static final String TXT_SWITCH_WIN = "Switch to windowed";', 'private static final String TXT_SWITCH_WIN = "切换为窗口模式";'),
    ],

    "pixeldungeon/windows/WndStory.java": [
        (
            'CHAPTERS.put(0, "The Dungeon lies right beneath the City, its upper levels actually constitute the City\'s sewer system. Being nominally a part of the City, these levels are not that dangerous. No one will call it a safe place, but at least you won\'t need to deal with evil magic here.");',
            'CHAPTERS.put(0, "这座地牢正位于城市正下方，最上几层实际上就是城市的下水道系统。名义上它们仍属于城市的一部分，因此并不算十分致命。虽然没人会称这里为安全之地，但至少你暂时还不用面对邪恶的魔法。");'
        ),
        (
            'CHAPTERS.put(1, "Many years ago an underground prison was built here for the most dangerous criminals. At the time it seemed like a very clever idea, because this place indeed was very hard to escape. But soon dark miasma started to permeate from below, driving prisoners and guards insane. In the end the prison was abandoned, though some convicts were left locked up here.");',
            'CHAPTERS.put(1, "多年以前，这里曾修建了一座地下监狱，专门关押最穷凶极恶的罪犯。当时这看似是个绝妙的主意，因为此地确实极难逃脱。但很快，地底深处开始渗透出黑暗的瘴气，将囚犯与狱卒统统逼向疯狂。最终监狱被彻底废弃，只有一部分重刑犯仍被遗留并锁死在这里。");'
        ),
        (
            'CHAPTERS.put(2, "The caves, which stretch down under the abandoned prison, are sparcely populated. They lie too deep to be exploited by the City and they are too poor in minerals to interest the dwarves. In the past there was a trade outpost somewhere here on the route between these two states, but it has perished since the decline of Dwarven Metropolis. Only omnipresent gnolls and subterranean animals dwell here now.");',
            'CHAPTERS.put(2, "在废弃监狱下方延伸开的洞窟人迹罕至。它们太深，城市无力开发；矿藏又太过贫瘠，无法吸引矮人的兴趣。过去在连接两座城邦的商路上曾设有贸易前哨，但在矮人都城衰落之后也随之消亡。如今这里只剩下无处不在的豺狼人与地下野兽。");'
        ),
        (
            'CHAPTERS.put(3, "Dwarven Metropolis was once the greatest of dwarven city-states. In its heyday the mechanized army of dwarves has successfully repelled the invasion of the old god and his demon army. But it is said, that the returning warriors have brought seeds of corruption with them, and that victory was the beginning of the end for the underground kingdom.");',
            'CHAPTERS.put(3, "矮人都城曾是矮人诸邦中最辉煌的城邦。在鼎盛时期，矮人的机械大军曾成功击退古神及其恶魔军团的入侵。但据说凯旋的战士们带回了腐化的种子，而那场胜利，正是这座地下王国走向覆灭的开端。");'
        ),
        (
            'CHAPTERS.put(4, "In the past these levels were the outskirts of Metropolis. After the costly victory in the war with the old god dwarves were too weakened to clear them of remaining demons. Gradually demons have tightened their grip on this place and now it\'s called Demon Halls.\\n\\nVery few adventurers have ever descended this far...");',
            'CHAPTERS.put(4, "过去，这些楼层曾是矮人都城的外围区域。在与古神的惨烈战争后，精疲力竭的矮人再也无力清剿残余的恶魔。久而久之，恶魔彻底掌控了这里，如今人们称之为恶魔大厅。\\n\\n极少有冒险者能够深入至此……");'
        ),
    ],

    "pixeldungeon/windows/WndTradeItem.java": [
        ('private static final String TXT_SALE = "FOR SALE: %s - %dg";', 'private static final String TXT_SALE = "出售：%s - %d 金币";'),
        ('private static final String TXT_BUY = "Buy for %dg";', 'private static final String TXT_BUY = "花 %d 金币购买";'),
        ('private static final String TXT_SELL = "Sell for %dg";', 'private static final String TXT_SELL = "卖出换取 %d 金币";'),
        ('private static final String TXT_SELL_1 = "Sell 1 for %dg";', 'private static final String TXT_SELL_1 = "卖出 1 个换取 %d 金币";'),
        ('private static final String TXT_SELL_ALL = "Sell all for %dg";', 'private static final String TXT_SELL_ALL = "全部卖出换取 %d 金币";'),
        ('private static final String TXT_CANCEL = "Never mind";', 'private static final String TXT_CANCEL = "算了";'),
        ('private static final String TXT_SOLD = "You\'ve sold your %s for %dg";', 'private static final String TXT_SOLD = "你以 %2$d 金币卖出了%1$s";'),
        ('private static final String TXT_BOUGHT = "You\'ve bought %s for %dg";', 'private static final String TXT_BOUGHT = "你花费 %2$d 金币购买了%1$s";'),
    ],

    "pixeldungeon/windows/WndWandmaker.java": [
        ('private static final String TXT_MESSAGE = "Oh, I see you have succeeded! I do hope it hasn\'t troubled you too much. As I promised, you can choose one of my high quality wands.";', 'private static final String TXT_MESSAGE = "哦，看来你成功了！希望这事没给你添太多麻烦。信守承诺，你可以从我这些上等法杖中挑选一根。";'),
        ('private static final String TXT_BATTLE = "Battle wand";', 'private static final String TXT_BATTLE = "战斗法杖";'),
        ('private static final String TXT_NON_BATTLE = "Non-battle wand";', 'private static final String TXT_NON_BATTLE = "功能法杖";'),
        ('private static final String TXT_FARAWELL = "Good luck in your quest, %s!";', 'private static final String TXT_FARAWELL = "祝你在这场试炼中好运，%s！";'),
        ('GLog.i("You now have %s", ((Item)reward).name());', 'GLog.i("你获得了%s", ((Item)reward).name());'),
    ],
}

if __name__ == "__main__":
    apply_file_patches(PATCHES_PART1)
