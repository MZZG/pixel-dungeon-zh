from patch_engine import apply_file_patches

PATCHES_PART2 = {
    "pixeldungeon/actors/Char.java": [
        ('protected static final String TXT_HIT = "%s hit %s";', 'protected static final String TXT_HIT = "%s击中了%s";'),
        ('protected static final String TXT_KILL = "%s killed you...";', 'protected static final String TXT_KILL = "%s杀害了你...";'),
        ('protected static final String TXT_DEFEAT = "%s defeated %s";', 'protected static final String TXT_DEFEAT = "%s击败了%s";'),
        ('private static final String TXT_YOU_MISSED = "%s %s your attack";', 'private static final String TXT_YOU_MISSED = "%s%s了你的攻击";'),
        ('private static final String TXT_SMB_MISSED = "%s %s %s\'s attack";', 'private static final String TXT_SMB_MISSED = "%s%s了%s的攻击";'),
        ('private static final String TXT_OUT_OF_PARALYSIS = "The pain snapped %s out of paralysis";', 'private static final String TXT_OUT_OF_PARALYSIS = "剧痛使%s从麻痹中惊醒";'),
        ('Dungeon.fail(Utils.format("Killed by the %s on level %d", this.name, Dungeon.depth));', 'Dungeon.fail(Utils.format("在第 %2$d 层被%1$s杀害", this.name, Dungeon.depth));'),
        ('Dungeon.fail(Utils.format("Killed by %s on level %d", Utils.indefinite(this.name), Dungeon.depth));', 'Dungeon.fail(Utils.format("在第 %2$d 层被%1$s杀害", Utils.indefinite(this.name), Dungeon.depth));'),
        ('return "dodged";', 'return "闪避";'),
        ('this.sprite.showStatus(0xFF0000, "poisoned", new Object[0]);', 'this.sprite.showStatus(0xFF0000, "中毒", new Object[0]);'),
        ('this.sprite.showStatus(0xFF0000, "amok", new Object[0]);', 'this.sprite.showStatus(0xFF0000, "狂乱", new Object[0]);'),
        ('this.sprite.showStatus(0xFF0000, "slowed", new Object[0]);', 'this.sprite.showStatus(0xFF0000, "迟缓", new Object[0]);'),
        ('this.sprite.showStatus(65280, "mind", new Object[0]);\n                this.sprite.showStatus(65280, "vision", new Object[0]);', 'this.sprite.showStatus(65280, "灵视", new Object[0]);'),
        ('this.sprite.showStatus(0xFF0000, "paralysed", new Object[0]);', 'this.sprite.showStatus(0xFF0000, "麻痹", new Object[0]);'),
        ('this.sprite.showStatus(0xFF0000, "frightened", new Object[0]);', 'this.sprite.showStatus(0xFF0000, "恐惧", new Object[0]);'),
        ('this.sprite.showStatus(0xFF0000, "rooted", new Object[0]);', 'this.sprite.showStatus(0xFF0000, "缠绕", new Object[0]);'),
        ('this.sprite.showStatus(0xFF0000, "crippled", new Object[0]);', 'this.sprite.showStatus(0xFF0000, "残废", new Object[0]);'),
        ('this.sprite.showStatus(0xFF0000, "bleeding", new Object[0]);', 'this.sprite.showStatus(0xFF0000, "流血", new Object[0]);'),
        ('this.sprite.showStatus(0xFF0000, "dizzy", new Object[0]);', 'this.sprite.showStatus(0xFF0000, "眩晕", new Object[0]);'),
        ('this.sprite.showStatus(65280, "invisible", new Object[0]);', 'this.sprite.showStatus(65280, "隐形", new Object[0]);'),
        (
            '        HashSet<Buff> filtered = new HashSet<Buff>();\n        for (Buff b : this.buffs) {\n            if (!ClassReflection.isInstance(c, b)) continue;\n            filtered.add(b);\n        }',
            '        HashSet<T> filtered = new HashSet<T>();\n        for (Buff b : this.buffs) {\n            if (!ClassReflection.isInstance(c, b)) continue;\n            filtered.add((T)b);\n        }'
        ),
    ],

    # Blobs
    "pixeldungeon/actors/blobs/ConfusionGas.java": [
        ('return "A cloud of confusion gas is swirling here.";', 'return "这里盘绕着一片致眩气体。";'),
    ],
    "pixeldungeon/actors/blobs/Fire.java": [
        ('return "A fire is raging here.";', 'return "一团火焰正在这里肆虐。";'),
    ],
    "pixeldungeon/actors/blobs/Foliage.java": [
        ('return "Shafts of light pierce the gloom of the underground garden.";', 'return "光柱刺破了地下花园中的黑暗。";'),
    ],
    "pixeldungeon/actors/blobs/ParalyticGas.java": [
        ('return "A cloud of paralytic gas is swirling here.";', 'return "这里盘绕着一片麻痹气体。";'),
    ],
    "pixeldungeon/actors/blobs/SacrificialFire.java": [
        ('private static final String TXT_WORTHY = "\\"Your sacrifice is worthy...\\" ";', 'private static final String TXT_WORTHY = "“你的祭品甚是诚心……”";'),
        ('private static final String TXT_UNWORTHY = "\\"Your sacrifice is unworthy...\\" ";', 'private static final String TXT_UNWORTHY = "“你的祭品毫无价值……”";'),
        ('private static final String TXT_REWARD = "\\"Your sacrifice is worthy and so you are!\\" ";', 'private static final String TXT_REWARD = "“你的祭品与你自己都值得赞赏！”";'),
        ('return "Sacrificial fire burns here. Every creature touched by this fire is marked as an offering for the spirits of the dungeon.";', 'return "这里燃烧着献祭之火。任何被这火焰触及的生物都会被标记为献给地牢幽魂的祭品。";'),
        ('return "Marked for sacrifice";', 'return "献祭印记";'),
    ],
    "pixeldungeon/actors/blobs/ToxicGas.java": [
        ('return "A greenish cloud of toxic gas is swirling here.";', 'return "这里盘绕着一片发绿的毒气。";'),
        ('Dungeon.fail(Utils.format("Died from toxic gas on level %d", Dungeon.depth));', 'Dungeon.fail(Utils.format("在第 %d 层因毒气窒息而死", Dungeon.depth));'),
        ('GLog.n("You died from a toxic gas..", new Object[0]);', 'GLog.n("你死于毒气窒息……", new Object[0]);'),
    ],
    "pixeldungeon/actors/blobs/WaterOfAwareness.java": [
        ('private static final String TXT_PROCCED = "As you take a sip, you feel the knowledge pours into your mind. Now you know everything about your equipped items. Also you sense all items on the level and know all its secrets.";', 'private static final String TXT_PROCCED = "你小酌一口后，无数知识瞬间涌入脑海。你已完全知晓身上装备的一切，同时也感知到了本层的所有物品与秘密。";'),
        ('return "Power of knowledge radiates from the water of this well. Take a sip from it to reveal all secrets of equipped items.";', 'return "这口井中散发着知识之力。饮下一口可以揭晓当前穿戴装备的全部秘密。";'),
    ],
    "pixeldungeon/actors/blobs/WaterOfHealth.java": [
        ('private static final String TXT_PROCCED = "As you take a sip, you feel your wounds heal completely.";', 'private static final String TXT_PROCCED = "你小酌一口后，立刻感到所有伤口都痊愈了。";'),
        ('return "Power of health radiates from the water of this well. Take a sip from it to heal your wounds and satisfy hunger.";', 'return "这口井中散发着生命之力。饮下一口可以治愈伤势并缓解饥饿。";'),
    ],
    "pixeldungeon/actors/blobs/WaterOfTransmutation.java": [
        ('private static final String TXT_PROCCED = "As you take a sip, you feel something changes in you.";', 'private static final String TXT_PROCCED = "你小酌一口后，隐约感到体内有什么发生了变化。";'),
        ('return "Power of change radiates from the water of this well. Throw an item into the well to turn it into something else.";', 'return "这口井中散发着嬗变之力。将一件物品投入井中，就能将其转化为另一件物品。";'),
    ],
    "pixeldungeon/actors/blobs/Web.java": [
        ('return "Everything is covered with a thick web here.";', 'return "这里的一切都被厚厚的蛛网覆盖着。";'),
    ],

    # Buffs
    "pixeldungeon/actors/buffs/Amok.java": [('return "Amok";', 'return "狂乱";')],
    "pixeldungeon/actors/buffs/Barkskin.java": [('return "Barkskin";', 'return "树肤";')],
    "pixeldungeon/actors/buffs/Bleeding.java": [
        ('return "Bleeding";', 'return "流血";'),
        ('Dungeon.fail(Utils.format("Bled to death on level %d", Dungeon.depth));', 'Dungeon.fail(Utils.format("在第 %d 层失血过多而死", Dungeon.depth));'),
        ('GLog.n("You bled to death...", new Object[0]);', 'GLog.n("你因失血过多而死...", new Object[0]);'),
    ],
    "pixeldungeon/actors/buffs/Blindness.java": [('return "Blinded";', 'return "失明";')],
    "pixeldungeon/actors/buffs/Burning.java": [
        ('private static final String TXT_BURNS_UP = "%s burns up!";', 'private static final String TXT_BURNS_UP = "%s被烧毁了！";'),
        ('private static final String TXT_BURNED_TO_DEATH = "You burned to death...";', 'private static final String TXT_BURNED_TO_DEATH = "你被燃烧至死...";'),
        ('return "Burning";', 'return "燃烧";'),
        ('Dungeon.fail(Utils.format("Burned to death on level %d", Dungeon.depth));', 'Dungeon.fail(Utils.format("在第 %d 层被烧死", Dungeon.depth));'),
    ],
    "pixeldungeon/actors/buffs/Charm.java": [('return "Charmed";', 'return "魅惑";')],
    "pixeldungeon/actors/buffs/Combo.java": [
        ('private static String TXT_COMBO = "%d hit combo!";', 'private static String TXT_COMBO = "%d 连击！";'),
        ('return "Combo";', 'return "连击";'),
    ],
    "pixeldungeon/actors/buffs/Cripple.java": [('return "Crippled";', 'return "残废";')],
    "pixeldungeon/actors/buffs/Disguise.java": [('return "Disguised";', 'return "伪装";')],
    "pixeldungeon/actors/buffs/Frost.java": [('return "Frozen";', 'return "冻结";')],
    "pixeldungeon/actors/buffs/Fury.java": [('return "Fury";', 'return "暴怒";')],
    "pixeldungeon/actors/buffs/GasesImmunity.java": [('return "Immune to gases";', 'return "气体免疫";')],
    "pixeldungeon/actors/buffs/Hunger.java": [
        ('private static final String TXT_HUNGRY = "You are hungry.";', 'private static final String TXT_HUNGRY = "你有点饿了。";'),
        ('private static final String TXT_STARVING = "You are starving!";', 'private static final String TXT_STARVING = "你已经饥肠辘辘！";'),
        ('private static final String TXT_DEATH = "You starved to death...";', 'private static final String TXT_DEATH = "你活活饿死了...";'),
        ('return "Hungry";', 'return "饥饿";'),
        ('return "Starving";', 'return "极度饥饿";'),
        ('Dungeon.fail(Utils.format("Starved to death on level %d", Dungeon.depth));', 'Dungeon.fail(Utils.format("在第 %d 层饿死", Dungeon.depth));'),
        (
            '                void var4_10;\n                int bonus = 0;\n                for (Buff buff : this.target.buffs(RingOfSatiety.Satiety.class)) {\n                    bonus += ((RingOfSatiety.Satiety)buff).level;\n                }\n                float newLevel = this.level + 10.0f - (float)bonus;\n                boolean bl = false;\n                if (newLevel >= 360.0f) {\n                    GLog.n(TXT_STARVING, new Object[0]);\n                    boolean bl2 = true;\n                    hero.interrupt();\n                } else if (newLevel >= 260.0f && this.level < 260.0f) {\n                    GLog.w(TXT_HUNGRY, new Object[0]);\n                    boolean bl3 = true;\n                }\n                this.level = newLevel;\n                if (var4_10 != false) {\n                    BuffIndicator.refreshHero();\n                }',
            '''                int bonus = 0;
                for (Buff buff : this.target.buffs(RingOfSatiety.Satiety.class)) {
                    bonus += ((RingOfSatiety.Satiety)buff).level;
                }
                float newLevel = this.level + 10.0f - (float)bonus;
                boolean statusUpdated = false;
                if (newLevel >= 360.0f) {
                    GLog.n(TXT_STARVING, new Object[0]);
                    statusUpdated = true;
                    hero.interrupt();
                } else if (newLevel >= 260.0f && this.level < 260.0f) {
                    GLog.w(TXT_HUNGRY, new Object[0]);
                    statusUpdated = true;
                }
                this.level = newLevel;
                if (statusUpdated) {
                    BuffIndicator.refreshHero();
                }'''
        ),
    ],
    "pixeldungeon/actors/buffs/Invisibility.java": [('return "Invisible";', 'return "隐形";')],
    "pixeldungeon/actors/buffs/Levitation.java": [('return "Levitating";', 'return "漂浮";')],
    "pixeldungeon/actors/buffs/Light.java": [('return "Illuminated";', 'return "光耀";')],
    "pixeldungeon/actors/buffs/MindVision.java": [('return "Mind vision";', 'return "灵视";')],
    "pixeldungeon/actors/buffs/Ooze.java": [
        ('private static final String TXT_HERO_KILLED = "%s killed you...";', 'private static final String TXT_HERO_KILLED = "%s杀害了你...";'),
        ('return "Caustic ooze";', 'return "腐蚀淤泥";'),
        ('Dungeon.fail(Utils.format("Killed by a caustic ooze on level %d", Dungeon.depth));', 'Dungeon.fail(Utils.format("在第 %d 层被腐蚀淤泥吞噬", Dungeon.depth));'),
    ],
    "pixeldungeon/actors/buffs/Paralysis.java": [('return "Paralysed";', 'return "麻痹";')],
    "pixeldungeon/actors/buffs/Poison.java": [
        ('return "Poisoned";', 'return "中毒";'),
        ('Dungeon.fail(Utils.format("Died from poison on level %d", Dungeon.depth));', 'Dungeon.fail(Utils.format("在第 %d 层中毒身亡", Dungeon.depth));'),
        ('GLog.n("You died from poison...", new Object[0]);', 'GLog.n("你被毒死了...", new Object[0]);'),
        ('            float f;\n            this.target.damage((int)(this.left / 3.0f) + 1, this);\n            this.spend(1.0f);\n            this.left -= 1.0f;\n            if (f <= 0.0f) {', '            this.target.damage((int)(this.left / 3.0f) + 1, this);\n            this.spend(1.0f);\n            if ((this.left -= 1.0f) <= 0.0f) {'),
    ],
    "pixeldungeon/actors/buffs/Rage.java": [('return "Blinded with rage";', 'return "怒火攻心";')],
    "pixeldungeon/actors/buffs/Roots.java": [('return "Rooted";', 'return "缠绕";')],
    "pixeldungeon/actors/buffs/Shadows.java": [('return "Shadowmelded";', 'return "暗影融合";')],
    "pixeldungeon/actors/buffs/Slow.java": [('return "Slowed";', 'return "迟缓";')],
    "pixeldungeon/actors/buffs/SnipersMark.java": [('return "Zeroed in";', 'return "狙击锁定";')],
    "pixeldungeon/actors/buffs/Terror.java": [('return "Terror";', 'return "恐惧";')],
    "pixeldungeon/actors/buffs/Vertigo.java": [('return "Vertigo";', 'return "眩晕";')],
    "pixeldungeon/actors/buffs/Weakness.java": [('return "Weakened";', 'return "虚弱";')],

    # Hero
    "pixeldungeon/actors/hero/Belongings.java": [
        ('this.name = "backpack";', 'this.name = "背包";'),
        (
            '        ArrayList<Item> result = new ArrayList<Item>();\n        for (Item item : this) {\n            if (!itemClass.isInstance(item)) continue;\n            result.add(item);\n        }',
            '        ArrayList<T> result = new ArrayList<T>();\n        for (Item item : this) {\n            if (!itemClass.isInstance(item)) continue;\n            result.add((T)item);\n        }'
        ),
    ],
    "pixeldungeon/actors/hero/Hero.java": [
        ('private static final String TXT_LEAVE = "One does not simply leave Pixel Dungeon.";', 'private static final String TXT_LEAVE = "没有人能轻轻松松离开像素地牢。";'),
        ('private static final String TXT_LEVEL_UP = "level up!";', 'private static final String TXT_LEVEL_UP = "升级！";'),
        ('private static final String TXT_NEW_LEVEL = "Welcome to level %d! Now you are healthier and more focused. It\'s easier for you to hit enemies and dodge their attacks.";', 'private static final String TXT_NEW_LEVEL = "欢迎来到 %d 级！现在你更加强壮也更加专注，更容易击中敌人并闪避攻击了。";'),
        ('public static final String TXT_YOU_NOW_HAVE = "You now have %s";', 'public static final String TXT_YOU_NOW_HAVE = "你获得了%s";'),
        ('private static final String TXT_SOMETHING_ELSE = "There is something else here";', 'private static final String TXT_SOMETHING_ELSE = "这里还有别的东西";'),
        ('private static final String TXT_LOCKED_CHEST = "This chest is locked and you don\'t have matching key";', 'private static final String TXT_LOCKED_CHEST = "这个宝箱锁着，你没有对应的钥匙";'),
        ('private static final String TXT_LOCKED_DOOR = "You don\'t have a matching key";', 'private static final String TXT_LOCKED_DOOR = "你没有对应的钥匙";'),
        ('private static final String TXT_NOTICED_SMTH = "You noticed something";', 'private static final String TXT_NOTICED_SMTH = "你注意到了什么东西";'),
        ('private static final String TXT_SEARCH = "search";', 'private static final String TXT_SEARCH = "搜索";'),
        ('this.name = "you";', 'this.name = "你";'),
        ('Dungeon.win("Obtained the Amulet of Yendor");', 'Dungeon.win("获得Yendor护符");'),
        ('GLog.w("You catch fire!", new Object[0]);', 'GLog.w("你着火了！", new Object[0]);'),
        ('GLog.w("You are paralysed!", new Object[0]);', 'GLog.w("你被麻痹了！", new Object[0]);'),
        ('GLog.w("You are poisoned!", new Object[0]);', 'GLog.w("你中毒了！", new Object[0]);'),
        ('GLog.w("Caustic ooze eats your flesh. Wash away it!", new Object[0]);', 'GLog.w("腐蚀淤泥正在侵蚀你的血肉，快找水冲掉！", new Object[0]);'),
        ('GLog.w("You can\'t move!", new Object[0]);', 'GLog.w("你不能移动了！", new Object[0]);'),
        ('GLog.w("You feel weakened!", new Object[0]);', 'GLog.w("你感到一阵虚弱！", new Object[0]);'),
        ('GLog.w("You are blinded!", new Object[0]);', 'GLog.w("你失明了！", new Object[0]);'),
        ('GLog.w("You become furious!", new Object[0]);', 'GLog.w("你陷入了狂怒！", new Object[0]);'),
        ('this.sprite.showStatus(65280, "furious", new Object[0]);', 'this.sprite.showStatus(65280, "狂怒", new Object[0]);'),
        ('GLog.w("You are charmed!", new Object[0]);', 'GLog.w("你被魅惑了！", new Object[0]);'),
        ('GLog.w("You are crippled!", new Object[0]);', 'GLog.w("你残废了！", new Object[0]);'),
        ('GLog.w("You are bleeding!", new Object[0]);', 'GLog.w("你正在流血！", new Object[0]);'),
        ('GLog.w("Everything is spinning around you!", new Object[0]);', 'GLog.w("四周的天地都在旋转！", new Object[0]);'),
        ('if (count <= true) break;', 'if (count <= 1) break;'),
        (
            '            Level.set(doorCell, (door = Dungeon.level.map[doorCell = ((HeroAction.Unlock)this.curAction).dst]) == 10 ? 5 : 26);',
            '            doorCell = ((HeroAction.Unlock)this.curAction).dst;\n            door = Dungeon.level.map[doorCell];\n            Level.set(doorCell, door == 10 ? 5 : 26);'
        ),
    ],
    "pixeldungeon/actors/hero/HeroClass.java": [
        ('WARRIOR("warrior"),', 'WARRIOR("战士"),'),
        ('MAGE("mage"),', 'MAGE("法师"),'),
        ('ROGUE("rogue"),', 'ROGUE("盗贼"),'),
        ('HUNTRESS("huntress");', 'HUNTRESS("女猎手");'),
        (
            'WAR_PERKS = new String[]{"Warriors start with 11 points of Strength.", "Warriors start with a unique short sword. This sword can be later \\"reforged\\" to upgrade another melee weapon.", "Warriors are less proficient with missile weapons.", "Any piece of food restores some health when eaten.", "Potions of Strength are identified from the beginning."};',
            'WAR_PERKS = new String[]{"战士初始拥有 11 点力量。", "战士开局持有一把独特的短剑，日后可用于\\"重铸\\"并强化另一把近战武器。", "战士不擅长使用投掷武器。", "食用任意食物都能恢复少量生命。", "力量药剂在开局时便已鉴定。"};'
        ),
        (
            'MAG_PERKS = new String[]{"Mages start with a unique Wand of Magic Missile. This wand can be later \\"disenchanted\\" to upgrade another wand.", "Mages recharge their wands faster.", "When eaten, any piece of food restores 1 charge for all wands in the inventory.", "Mages can use wands as a melee weapon.", "Scrolls of Identify are identified from the beginning."};',
            'MAG_PERKS = new String[]{"法师开局持有一根独特的魔弹法杖，日后可用于\\"祛魅\\"并强化另一根法杖。", "法师的法杖充能速度更快。", "食用任意食物时，会为背包中的所有法杖恢复 1 点充能。", "法师可以将法杖当作近战武器使用。", "鉴定卷轴在开局时便已鉴定。"};'
        ),
        (
            'ROG_PERKS = new String[]{"Rogues start with a Ring of Shadows+1.", "Rogues identify a type of a ring on equipping it.", "Rogues are proficient with light armor, dodging better while wearing one.", "Rogues are proficient in detecting hidden doors and traps.", "Rogues can go without food longer.", "Scrolls of Magic Mapping are identified from the beginning."};',
            'ROG_PERKS = new String[]{"盗贼开局携带一枚 +1 暗影之戒。", "盗贼装备戒指时能立刻识别其种类。", "盗贼擅长穿戴轻甲，穿着时闪避能力更高。", "盗贼更擅长察觉隐藏门与陷阱。", "盗贼更耐饥饿。", "探地卷轴在开局时便已鉴定。"};'
        ),
        (
            'HUN_PERKS = new String[]{"Huntresses start with 15 points of Health.", "Huntresses start with a unique upgradeable boomerang.", "Huntresses are proficient with missile weapons and get a damage bonus for excessive strength when using them.", "Huntresses gain more health from dewdrops.", "Huntresses sense neighbouring monsters even if they are hidden behind obstacles."};',
            'HUN_PERKS = new String[]{"女猎手初始拥有 15 点生命值。", "女猎手开局持有一把可强化的独特回旋镖。", "女猎手精通投掷武器，使用超出力量需求的投掷武器时能获得额外伤害加成。", "女猎手通过露珠可恢复更多生命。", "即使怪物藏在障碍物后，女猎手也能感知到邻近的敌人。"};'
        ),
    ],
    "pixeldungeon/actors/hero/HeroSubClass.java": [
        ('GLADIATOR("gladiator", "A successful attack with a melee weapon allows the _Gladiator_ to start a combo, in which every next successful hit inflicts more damage."),', 'GLADIATOR("角斗士", "使用近战武器成功命中敌人后，_角斗士_会进入连击状态，每一次连续命中都将造成更高的伤害。"),'),
        ('BERSERKER("berserker", "The more enemies surround the _Berserker_, the faster he performs his attacks. When severely wounded, the _Berserker_ enters a state of wild fury significantly increasing his damage output."),', 'BERSERKER("狂战士", "周围包围的敌人越多，_狂战士_的攻击速度就越快。身负重伤时，_狂战士_会进入狂怒状态，大幅提升自身伤害。"),'),
        ('WARLOCK("warlock", "After killing an enemy the _Warlock_ consumes its soul. It heals his wounds and satisfies his hunger."),', 'WARLOCK("术士", "击杀敌人后，_术士_会吞噬对方的灵魂，以此治愈伤势并缓解饥饿。"),'),
        ('BATTLEMAGE("battlemage", "When fighting with a wand in his hands, the _Battlemage_ inflicts additional damage depending on the current number of charges. Every successful hit restores 1 charge to this wand."),', 'BATTLEMAGE("战斗法师", "手持法杖作战时，_战斗法师_会根据法杖当前剩余充能造成额外伤害，且每次成功命中都会为该法杖恢复 1 点充能。"),'),
        ('ASSASSIN("assassin", "When performing a surprise attack, the _Assassin_ inflicts additional damage to his target."),', 'ASSASSIN("刺客", "发动偷袭时，_刺客_会对目标造成额外的致命伤害。"),'),
        ('FREERUNNER("freerunner", "The _Freerunner_ can move almost twice faster, than most of the monsters. When he is running, the Freerunner is much harder to hit. For that he must be unencumbered and not starving."),', 'FREERUNNER("疾行者", "_疾行者_的移动速度几乎是大多数怪物的两倍，奔跑时也更难被击中。前提是不能超重且未处于饥饿掉血状态。"),'),
        ('SNIPER("sniper", "_Snipers_ are able to detect weak points in an enemy\'s armor, effectively ignoring it when using a missile weapon."),', 'SNIPER("狙击手", "_狙击手_能够看穿敌人护甲的弱点，使用投掷武器攻击时可完全无视目标防御。"),'),
        ('WARDEN("warden", "Having a strong connection with forces of nature gives _Wardens_ an ability to gather dewdrops and seeds from plants. Also trampling a high grass grants them a temporary armor buff.");', 'WARDEN("守望者", "与自然之力的紧密联系让_守望者_能从草丛中采集更多露珠和种子，踩踏高草还能获得临时的树肤护甲增益。");'),
    ],

    # Mobs & NPCs
    "pixeldungeon/actors/mobs/Acidic.java": [('this.name = "acidic scorpio";', 'this.name = "酸液巨蝎";')],
    "pixeldungeon/actors/mobs/Albino.java": [('this.name = "albino rat";', 'this.name = "白化老鼠";')],
    "pixeldungeon/actors/mobs/Bandit.java": [('this.name = "crazy bandit";', 'this.name = "疯狂强盗";')],
    "pixeldungeon/actors/mobs/Bat.java": [
        ('this.name = "vampire bat";', 'this.name = "吸血蝙蝠";'),
        ('return "evaded";', 'return "闪避";'),
        ('return "These brisk and tenacious inhabitants of cave domes may defeat much larger opponents by replenishing their health with each successful attack.";', 'return "这些栖息在洞窟穹顶中的敏捷生物十分顽强，每次成功攻击都能吸取生命来击败比自己庞大得多的对手。";'),
    ],
    "pixeldungeon/actors/mobs/Brute.java": [
        ('private static final String TXT_ENRAGED = "%s becomes enraged!";', 'private static final String TXT_ENRAGED = "%s被激怒了！";'),
        ('this.name = "gnoll brute";', 'this.name = "豺狼暴徒";'),
        ('this.sprite.showStatus(0xFF0000, "enraged", new Object[0]);', 'this.sprite.showStatus(0xFF0000, "激怒", new Object[0]);'),
        ('return "Brutes are the largest, strongest and toughest of all gnolls. When severely wounded, they go berserk, inflicting even more damage to their enemies.";', 'return "暴徒是豺狼人中体型最大、力量最强、也最耐打的一族。身受重伤时会陷入狂暴，对敌人造成更加恐怖的伤害。";'),
    ],
    "pixeldungeon/actors/mobs/Crab.java": [
        ('this.name = "sewer crab";', 'this.name = "下水道螃蟹";'),
        ('return "parried";', 'return "招架";'),
        ('return "These huge crabs are at the top of the food chain in the sewers. They are extremely fast and their thick exoskeleton can withstand heavy blows.";', 'return "这些巨型螃蟹处于下水道食物链的顶端。它们行动极其迅捷，坚硬的外骨骼能抵御沉重的打击。";'),
    ],
    "pixeldungeon/actors/mobs/CursePersonification.java": [
        ('this.name = "curse personification";', 'this.name = "诅咒化身";'),
        ('return "This creature resembles the sad ghost, but it swirls with darkness. Its face bears an expression of despair.";', 'return "这个生物的外形酷似悲伤幽灵，但周身缠绕着漆黑的怨气，脸上满是绝望的神情。";'),
    ],
    "pixeldungeon/actors/mobs/DM300.java": [
        ('GLog.n("DM-300 repairs itself!", new Object[0]);', 'GLog.n("DM-300 正在自我修复！", new Object[0]);'),
        ('this.yell("Mission failed. Shutting down.");', 'this.yell("任务失败。正在关机。");'),
        ('this.yell("Unauthorised personnel detected.");', 'this.yell("检测到未授权人员。");'),
        ('return "This machine was created by the Dwarves several centuries ago. Later, Dwarves started to replace machines with golems, elementals and even demons. Eventually it led their civilization to the decline. The DM-300 and similar machines were typically used for construction and mining, and in some cases, for city defense.";', 'return "这台机械由矮人在几个世纪前制造。后来矮人开始用魔像、元素甚至恶魔替代机械，最终导致了文明的衰落。DM-300 及同类机械原本常用于建造与采矿，有时也用于城市防卫。";'),
    ],
    "pixeldungeon/actors/mobs/Elemental.java": [
        ('this.name = "fire elemental";', 'this.name = "火焰元素";'),
        ('return "Wandering fire elementals are a byproduct of summoning greater entities. They are too chaotic in their nature to be controlled by even the most powerful demonologist.";', 'return "四处游荡的火元素是召唤强大存在时产生的副产物。它们的本性过于混沌，就连最强大的恶魔学者也无法将其完全掌控。";'),
    ],
    "pixeldungeon/actors/mobs/Eye.java": [
        ('private static final String TXT_DEATHGAZE_KILLED = "%s\'s deathgaze killed you...";', 'private static final String TXT_DEATHGAZE_KILLED = "%s的死亡凝视杀害了你...";'),
        ('this.name = "evil eye";', 'this.name = "邪眼";'),
        ('Dungeon.fail(Utils.format("Killed by %s on level %d", Utils.indefinite(this.name), Dungeon.depth));', 'Dungeon.fail(Utils.format("在第 %2$d 层被%1$s杀害", Utils.indefinite(this.name), Dungeon.depth));'),
        ('return "One of this demon\'s other names is \\"orb of hatred\\", because when it sees an enemy, it uses its deathgaze recklessly, often ignoring its allies and wounding them.";', 'return "这种恶魔又被称为\\"憎恨之球\\"——一旦发现敌人，它便会不顾一切地释放死亡凝视，甚至连身边的同伴也会一并误伤。";'),
    ],
    "pixeldungeon/actors/mobs/FetidRat.java": [
        ('this.name = "fetid rat";', 'this.name = "腐臭老鼠";'),
        ('return "evaded";', 'return "闪避";'),
        ('return "This marsupial rat is much larger than a regular one. It is surrounded by a foul cloud.";', 'return "这只啮齿巨鼠比普通老鼠大得多，周身还环绕着令人作呕的恶臭毒云。";'),
    ],
    "pixeldungeon/actors/mobs/Gnoll.java": [
        ('this.name = "gnoll scout";', 'this.name = "豺狼巡查";'),
        ('return "Gnolls are hyena-like humanoids. They dwell in sewers and dungeons, venturing up to raid the surface from time to time. Gnoll scouts are regular members of their pack, they are not as strong as brutes and not as intelligent as shamans.";', 'return "豺狼人是鬣狗状的类人生物。它们在下水道和地牢里游荡，不时向上行动试图向地表发起进攻。豺狼巡查是它们种群里最普通的一员，既没有暴徒强壮，也赶不上萨满聪明。";'),
    ],
    "pixeldungeon/actors/mobs/Golem.java": [
        ('this.name = "golem";', 'this.name = "魔像";'),
        ('return "blocked";', 'return "格挡";'),
        ('return "The Dwarves tried to combine their knowledge of mechanisms with their newfound power of elemental binding. They used spirits of earth as the \\"soul\\" for the mechanical bodies of golems, which were believed to be most controllable of all. Despite this, the tiniest mistake in the ritual could cause an outbreak.";', 'return "矮人试图将机械工艺与新掌握的元素束缚之力结合起来，用大地之灵作为魔像机械躯体的\\"灵魂\\"——原本以为这样最容易控制，但仪式中哪怕最微小的失误也可能引发暴走。";'),
    ],
    "pixeldungeon/actors/mobs/Goo.java": [
        ('GLog.n("Goo is pumping itself up!", new Object[0]);', 'GLog.n("粘咕正在不断地膨胀蓄力！", new Object[0]);'),
        ('this.yell("glurp... glurp...");', 'this.yell("咕...咕...");'),
        ('this.yell("GLURP-GLURP!");', 'this.yell("咕-咕！");'),
        ('return "Little known about The Goo. It\'s quite possible that it is not even a creature, but rather a conglomerate of substances from the sewers that gained rudiments of free will.";', 'return "人们对粘咕知之甚少。它甚至可能根本不是生物，只是下水道中各种污浊物质聚合后诞生了原始自我意识的产物。";'),
    ],
    "pixeldungeon/actors/mobs/King.java": [
        ('return "parried";', 'return "招架";'),
        ('this.yell("You cannot kill me, " + Dungeon.hero.heroClass.title() + "... I am... immortal...");', 'this.yell("你杀不死我的，" + Dungeon.hero.heroClass.title() + "...我是...永生的...");'),
        ('this.yell("Arise, slaves!");', 'this.yell("起来吧，奴仆们！");'),
        ('this.yell("How dare you!");', 'this.yell("你好大的胆子！");'),
        ('return "The last king of dwarves was known for his deep understanding of processes of life and death. He has persuaded members of his court to participate in a ritual, that should have granted them eternal youthfulness. In the end he was the only one, who got it - and an army of undead as a bonus.";', 'return "矮人的末代国王以深谙生死奥秘而闻名。他说服王庭成员参与了一场号称能赐予永恒青春的仪式。最终只有他一人得到了永生——以及一支作为附赠品的不死大军。";'),
        ('this.name = "undead dwarf";', 'this.name = "不死矮人";'),
        ('return "blocked";', 'return "格挡";'),
        ('return "These undead dwarves, risen by the will of the King of Dwarves, were members of his court. They appear as skeletons with a stunning amount of facial hair.";', 'return "这些由矮人国王意志唤醒的不死矮人曾是他的朝臣。如今只剩骸骨，却依然留着惊人浓密的胡须。";'),
    ],
    "pixeldungeon/actors/mobs/Mimic.java": [
        ('this.name = "mimic";', 'this.name = "宝箱怪";'),
        ('return "Mimics are magical creatures which can take any shape they wish. In dungeons they almost always choose a shape of a treasure chest, because they know how to beckon an adventurer.";', 'return "宝箱怪是可以任意变换形态的魔法生物。在地牢中它们几乎总是伪装成宝箱，因为它们深谙如何引诱冒险者上钩。";'),
        ('this.items = new ArrayList<Bundlable>(bundle.getCollection(ITEMS));', 'this.items = new ArrayList<Item>();\n        for (com.watabou.utils.Bundlable b : bundle.getCollection(ITEMS)) {\n            this.items.add((Item)b);\n        }'),
    ],
    "pixeldungeon/actors/mobs/Mob.java": [
        ('private static final String TXT_DIED = "You hear something died in the distance";', 'private static final String TXT_DIED = "你听到远处有什么东西死去了";'),
        ('return "Real description is coming soon!";', 'return "暂无详细描述。";'),
        ('GLog.n("%s: \\"%s\\" ", this.name, str);', 'GLog.n("%s：\\"%s\\" ", this.name, str);'),
        ('return Utils.format("This %s is passive", Mob.this.name);', 'return Utils.format("这只%s正处于被动状态", Mob.this.name);'),
        ('return Utils.format("This %s is fleeing", Mob.this.name);', 'return Utils.format("这只%s正在逃跑", Mob.this.name);'),
        ('return Utils.format("This %s is hunting", Mob.this.name);', 'return Utils.format("这只%s正在猎杀你", Mob.this.name);'),
        ('return Utils.format("This %s is wandering", Mob.this.name);', 'return Utils.format("这只%s正在游荡", Mob.this.name);'),
        ('return Utils.format("This %s is sleeping", Mob.this.name);', 'return Utils.format("这只%s正在沉睡", Mob.this.name);'),
    ],
    "pixeldungeon/actors/mobs/Monk.java": [
        ('public static final String TXT_DISARM = "%s has knocked the %s from your hands!";', 'public static final String TXT_DISARM = "%s把你手中的%s击飞了！";'),
        ('this.name = "dwarf monk";', 'this.name = "矮人武僧";'),
        ('return "parried";', 'return "招架";'),
        ('return "These monks are fanatics, who devoted themselves to protecting their city\'s secrets from all aliens. They don\'t use any armor or weapons, relying solely on the art of hand-to-hand combat.";', 'return "这些武僧是狂热的卫道者，誓死守护都城的秘密不被外来者窥探。他们不穿护甲也不用兵器，全凭徒手格斗之术作战。";'),
    ],
    "pixeldungeon/actors/mobs/Piranha.java": [
        ('this.name = "giant piranha";', 'this.name = "巨型食人鱼";'),
        ('return "These carnivorous fish are not natural inhabitants of underground pools. They were bred specifically to protect flooded treasure vaults.";', 'return "这些凶猛的食肉鱼并非地下水池的原生生物，它们是专门被培育用来看守水淹宝库的守卫。";'),
    ],
    "pixeldungeon/actors/mobs/Rat.java": [
        ('this.name = "marsupial rat";', 'this.name = "啮齿小鼠";'),
        ('return "Marsupial rats are aggressive, but rather weak denizens of the sewers. They can be dangerous only in big numbers.";', 'return "啮齿小鼠是下水道中颇具攻击性但较为弱小的生物，唯有成群结队时才具有威胁。";'),
    ],
    "pixeldungeon/actors/mobs/Scorpio.java": [
        ('this.name = "scorpio";', 'this.name = "巨蝎";'),
        ('return "These huge arachnid-like demonic creatures avoid close combat by all means, firing crippling serrated spikes from long distances.";', 'return "这种形似巨型蛛形纲动物的恶魔会极力避免近身肉搏，转而从远处发射令人致残的锯齿毒刺。";'),
    ],
    "pixeldungeon/actors/mobs/Senior.java": [('this.name = "senior monk";', 'this.name = "武僧大师";')],
    "pixeldungeon/actors/mobs/Shaman.java": [
        ('private static final String TXT_LIGHTNING_KILLED = "%s\'s lightning bolt killed you...";', 'private static final String TXT_LIGHTNING_KILLED = "%s的闪电箭杀害了你...";'),
        ('this.name = "gnoll shaman";', 'this.name = "豺狼萨满";'),
        ('Dungeon.fail(Utils.format("Killed by %s on level %d", Utils.indefinite(this.name), Dungeon.depth));', 'Dungeon.fail(Utils.format("在第 %2$d 层被%1$s杀害", Utils.indefinite(this.name), Dungeon.depth));'),
        ('return "The most intelligent gnolls can master shamanistic magic. Gnoll shamans prefer battle spells to compensate for lack of might, not hesitating to use them on those who question their status in a tribe.";', 'return "最聪慧的豺狼人能够掌握萨满法术。豺狼萨满依靠战斗法术来弥补体格的不足，并会毫不犹豫地用它们惩罚敢于质疑自己部族地位的家伙。";'),
    ],
    "pixeldungeon/actors/mobs/Shielded.java": [
        ('this.name = "shielded brute";', 'this.name = "持盾暴徒";'),
        ('return "blocked";', 'return "格挡";'),
    ],
    "pixeldungeon/actors/mobs/Skeleton.java": [
        ('private static final String TXT_HERO_KILLED = "You were killed by the explosion of bones...";', 'private static final String TXT_HERO_KILLED = "你死于迸溅的碎骨...";'),
        ('this.name = "skeleton";', 'this.name = "骷髅";'),
        ('Dungeon.fail(Utils.format("Killed by %s on level %d", Utils.indefinite(this.name), Dungeon.depth));', 'Dungeon.fail(Utils.format("在第 %2$d 层被%1$s杀害", Utils.indefinite(this.name), Dungeon.depth));'),
        ('return "blocked";', 'return "格挡";'),
        ('return "Skeletons are composed of corpses bones from unlucky adventurers and inhabitants of the dungeon, animated by emanations of evil magic from the depths below. After they have been damaged enough, they disintegrate in an explosion of bones.";', 'return "骷髅由地牢中倒霉冒险者与原住民的尸骨构成，被深处涌出的邪恶魔力所驱动。遭受足够伤害后，它们会在碎骨爆裂中解体。";'),
    ],
    "pixeldungeon/actors/mobs/Spinner.java": [
        ('this.name = "cave spinner";', 'this.name = "矿洞蜘蛛";'),
        ('return "These greenish furry cave spiders try to avoid direct combat, preferring to wait in the distance while their victim, entangled in the spinner\'s excreted cobweb, slowly dies from their poisonous bite.";', 'return "这些浑身覆有绿色绒毛的洞穴蜘蛛会极力避免正面交锋，它们更喜欢在远处静待被蛛网困住的猎物在剧毒啃咬下慢慢死去。";'),
    ],
    "pixeldungeon/actors/mobs/Statue.java": [
        ('this.name = "animated statue";', 'this.name = "活化石像";'),
        (
            'return "You would think that it\'s just another ugly statue of this dungeon, but its red glowing eyes give itself away. While the statue itself is made of stone, the _" + this.weapon.name() + "_, it\'s wielding, looks real.";',
            'return "你本以为它只是地牢里又一座丑陋的石像，但它闪烁着红光的双眼暴露了一切。虽然石像本身是石头雕成的，但它手中紧握的_" + this.weapon.name() + "_却货真价实。";'
        ),
    ],
    "pixeldungeon/actors/mobs/Succubus.java": [
        ('this.name = "succubus";', 'this.name = "魅魔";'),
        ('return "The succubi are demons that look like seductive (in a slightly gothic way) girls. Using its magic, the succus can charm a hero, who will become unable to attack anything until the charm wears off.";', 'return "魅魔是一种外貌迷人（略带哥特风）的女妖恶魔。";') if False else
        ('return "The succubi are demons that look like seductive (in a slightly gothic way) girls. Using its magic, the succubus can charm a hero, who will become unable to attack anything until the charm wears off.";', 'return "魅魔是一种外貌妖娆（略带哥特风格）的恶魔。她们能施展魔法魅惑英雄，使其在效果消退前无法发起任何攻击。";'),
    ],
    "pixeldungeon/actors/mobs/Swarm.java": [
        ('this.name = "swarm of flies";', 'this.name = "蝇群";'),
        ('return "evaded";', 'return "闪避";'),
        ('return "The deadly swarm of flies buzzes angrily. Every non-magical attack will split it into two smaller but equally dangerous swarms.";', 'return "致命的飞蝇群愤怒地发出嗡嗡声。每次非魔法攻击都会把它分割成两群更小但同样危险的蝇群。";'),
    ],
    "pixeldungeon/actors/mobs/Tengu.java": [
        ('this.yell("Free at last...");', 'this.yell("终于...解脱了...");'),
        ('this.yell("Gotcha, " + Dungeon.hero.heroClass.title() + "!");', 'this.yell("抓到你了，" + Dungeon.hero.heroClass.title() + "！");'),
        ('return "Tengu are members of the ancient assassins clan, which is also called Tengu. These assassins are noted for extensive use of shuriken and traps.";', 'return "天狗是古老刺客宗族\\"天狗一族\\"的成员。这些刺客以熟练运用手里剑与各种陷阱而闻名。";'),
    ],
    "pixeldungeon/actors/mobs/Thief.java": [
        ('protected static final String TXT_STOLE = "%s stole %s from you!";', 'protected static final String TXT_STOLE = "%s偷走了你的%s！";'),
        ('protected static final String TXT_CARRIES = "\\n\\n%s is carrying a _%s_. Stolen obviously.";', 'protected static final String TXT_CARRIES = "\\n\\n%s正拿着一件_%s_，显然是偷来的。";'),
        ('this.name = "crazy thief";', 'this.name = "疯狂小偷";'),
        ('String desc = "Deeper levels of the dungeon have always been a hiding place for all kinds of criminals. Not all of them could keep a clear mind during their extended periods so far from daylight. Long ago, these crazy thieves and bandits have forgotten who they are and why they steal.";', 'String desc = "地牢深处向来是各路罪犯的藏身之所。长期远离天日，并非所有人都能保持神智清醒。早在很久以前，这些疯癫的窃贼与强盗就已忘记自己是谁，也忘了为何而偷窃。";'),
    ],
    "pixeldungeon/actors/mobs/Warlock.java": [
        ('private static final String TXT_SHADOWBOLT_KILLED = "%s\'s shadow bolt killed you...";', 'private static final String TXT_SHADOWBOLT_KILLED = "%s的暗影箭杀害了你...";'),
        ('this.name = "dwarf warlock";', 'this.name = "矮人术士";'),
        ('Dungeon.fail(Utils.format("Killed by %s on level %d", Utils.indefinite(this.name), Dungeon.depth));', 'Dungeon.fail(Utils.format("在第 %2$d 层被%1$s杀害", Utils.indefinite(this.name), Dungeon.depth));'),
        ('return "When dwarves\' interests have shifted from engineering to arcane arts, warlocks have come to power in the city. They started with elemental magic, but soon switched to demonology and necromancy.";', 'return "当矮人的兴趣从工程机械转向奥术秘法后，术士便在城中掌权。他们最初研习元素魔法，但很快就转投了恶魔学与死灵法术。";'),
    ],
    "pixeldungeon/actors/mobs/Wraith.java": [
        ('this.name = "wraith";', 'this.name = "怨灵";'),
        ('return "evaded";', 'return "闪避";'),
        ('return "A wraith is a vengeful spirit of a sinner, whose grave or tomb was disturbed. Being an ethereal entity, it is very hard to hit with a regular weapon.";', 'return "怨灵是墓穴被惊扰后前来复仇的罪人之魂。作为虚无缥缈的灵体，它很难被普通武器击中。";'),
    ],
    "pixeldungeon/actors/mobs/Yog.java": [
        ('private static final String TXT_DESC = "Yog-Dzewa is an Old God, a powerful entity from the realms of chaos. A century ago, the ancient dwarves barely won the war against its army of demons, but were unable to kill the god itself. Instead, they then imprisoned it in the halls below their city, believing it to be too weak to rise ever again.";', 'private static final String TXT_DESC = "Yog-Dzewa 是一位来自混沌领域的强大古神。一个世纪前，古代矮人勉强赢得了对抗其恶魔军团的战争，却无法杀死这位古神本身，于是将它封印在都城下方的大厅之中，以为它再也无力东山再起。";'),
        ('this.yell("Hope is an illusion...");', 'this.yell("希望不过是幻影...");'),
        ('this.name = "god\'s larva";', 'this.name = "古神幼虫";'),
        ('this.name = "burning fist";', 'this.name = "燃烧之拳";'),
        ('Dungeon.fail(Utils.format("Killed by the %s on level %d", this.name, Dungeon.depth));', 'Dungeon.fail(Utils.format("在第 %2$d 层被%1$s杀害", this.name, Dungeon.depth));'),
        ('GLog.n("%s killed you...", this.name);', 'GLog.n("%s杀害了你...", this.name);'),
        ('this.name = "rotting fist";', 'this.name = "腐烂之拳";'),
        ('        for (Mob mob : (Iterable)Dungeon.level.mobs.clone()) {', '        for (Object obj : (Iterable)Dungeon.level.mobs.clone()) {\n            Mob mob = (Mob)obj;'),
    ],
    "pixeldungeon/actors/mobs/npcs/Bee.java": [
        ('this.name = "golden bee";', 'this.name = "金色蜜蜂";'),
        ('return "Despite their small size, golden bees tend to protect their master fiercely. They don\'t live long though.";', 'return "尽管体型娇小，金色蜜蜂仍会竭尽全力保护主人，只是它们的寿命并不长。";'),
        ('return Utils.format("This %s is wandering", Bee.this.name);', 'return Utils.format("这只%s正在游荡", Bee.this.name);'),
    ],
    "pixeldungeon/actors/mobs/npcs/Blacksmith.java": [
        ('private static final String TXT_GOLD_1 = "Hey human! Wanna be useful, eh? Take dis pickaxe and mine me some _dark gold ore_, _15 pieces_ should be enough. What do you mean, how am I gonna pay? You greedy...\\nOk, ok, I don\'t have money to pay, but I can do some smithin\' for you. Consider yourself lucky, I\'m the only blacksmith around.";', 'private static final String TXT_GOLD_1 = "喂，人类！想帮点忙吗？拿着这把镐子去给我挖些_暗金矿_，_15 块_就够了。什么，问我怎么付报酬？你这贪心的家伙……\\n行吧行吧，我手头没钱付给你，但我可以帮你免费打铁。算你走运，这附近可就我这么一个铁匠。";'),
        ('private static final String TXT_BLOOD_1 = "Hey human! Wanna be useful, eh? Take dis pickaxe and _kill a bat_ wit\' it, I need its blood on the head. What do you mean, how am I gonna pay? You greedy...\\nOk, ok, I don\'t have money to pay, but I can do some smithin\' for you. Consider yourself lucky, I\'m the only blacksmith around.";', 'private static final String TXT_BLOOD_1 = "喂，人类！想帮点忙吗？拿着这把镐子去_砸死一只蝙蝠_，我需要镐头沾上它的血。什么，问我怎么付报酬？你这贪心的家伙……\\n行吧行吧，我手头没钱付给你，但我可以帮你免费打铁。算你走运，这附近可就我这么一个铁匠。";'),
        ('private static final String TXT2 = "Are you kiddin\' me? Where is my pickaxe?!";', 'private static final String TXT2 = "你在逗我吗？我的镐子呢？！";'),
        ('private static final String TXT3 = "Dark gold ore. 15 pieces. Seriously, is it dat hard?";', 'private static final String TXT3 = "暗金矿，15 块。说真的，这有那么难吗？";'),
        ('private static final String TXT4 = "I said I need bat blood on the pickaxe. Chop chop!";', 'private static final String TXT4 = "我说过镐头上要沾上蝙蝠血！动作快点！";'),
        ('private static final String TXT_COMPLETED = "Oh, you have returned... Better late dan never.";', 'private static final String TXT_COMPLETED = "哦，你终于回来了……总比不回来强。";'),
        ('private static final String TXT_GET_LOST = "I\'m busy. Get lost!";', 'private static final String TXT_GET_LOST = "我正忙着呢，滚开！";'),
        ('private static final String TXT_LOOKS_BETTER = "your %s certainly looks better now";', 'private static final String TXT_LOOKS_BETTER = "你的%s现在看起来好多了";'),
        ('this.name = "troll blacksmith";', 'this.name = "巨魔铁匠";'),
        ('GLog.i("You now have %s", pick.name());', 'GLog.i("你获得了%s", pick.name());'),
        ('return "Select 2 different items, not the same item twice!";', 'return "请选择两件不同的物品，别选同一件两次！";'),
        ('return "Select 2 items of the same type!";', 'return "请选择两件同类型的物品！";'),
        ('return "I need to know what I\'m working with, identify them first!";', 'return "我得先知道我在锻造什么，先把它们鉴定了吧！";'),
        ('return "I don\'t work with cursed items!";', 'return "我可不碰被诅咒的玩意儿！";'),
        ('return "It\'s a junk, the quality is too poor!";', 'return "这东西就是废铁，品质太差了！";'),
        ('return "I can\'t reforge these items!";', 'return "我没法重铸这些物品！";'),
        ('return "This troll blacksmith looks like all trolls look: he is tall and lean, and his skin resembles stone in both color and texture. The troll blacksmith is tinkering with unproportionally small tools.";', 'return "这位巨魔铁匠长得和所有巨魔一样：高大精瘦，皮肤的颜色与质感犹如岩石。他正用小得不成比例的工具敲敲打打。";'),
    ],
    "pixeldungeon/actors/mobs/npcs/Ghost.java": [
        ('this.name = "sad ghost";', 'this.name = "悲伤幽灵";'),
        ('return "evaded";', 'return "闪避";'),
        ('return "The ghost is barely visible. It looks like a shapeless spot of faint light with a sorrowful face.";', 'return "这个幽灵几乎不可见，看起来像是一团带有悲伤面孔的朦胧微光。";'),
        ('private static final String TXT_ROSE1 = "Hello adventurer... Once I was like you - strong and confident... And now I\'m dead... But I can\'t leave this place... Not until I have my _dried rose_... It\'s very important to me... Some monster stole it from my body...";', 'private static final String TXT_ROSE1 = "你好，冒险者……曾经我也像你一样强大而自信……可如今我已死去……但在拿回我的_干枯玫瑰_之前，我无法离开这里……它对我非常重要……有只怪物从我的尸体上偷走了它……";'),
        ('private static final String TXT_ROSE2 = "Please... Help me... _Find the rose_...";', 'private static final String TXT_ROSE2 = "求求你……帮帮我……_找到那朵玫瑰_……";'),
        ('private static final String TXT_ROSE3 = "Yes! Yes!!! This is it! Please give it to me! And you can take one of these items, maybe they will be useful to you in your journey...";', 'private static final String TXT_ROSE3 = "对！就是它！！！快交给我吧！作为报答，你可以从这些物品中任选一件，或许能在旅途中帮到你……";'),
        ('private static final String TXT_RAT1 = "Hello adventurer... Once I was like you - strong and confident... And now I\'m dead... But I can\'t leave this place... Not until I have my revenge... Slay the _fetid rat_, that has taken my life...";', 'private static final String TXT_RAT1 = "你好，冒险者……曾经我也像你一样强大而自信……可如今我已死去……但在大仇得报之前，我无法离开这里……请帮我消灭那只夺走我性命的_腐臭老鼠_……";'),
        ('private static final String TXT_RAT2 = "Please... Help me... _Slay the abomination_...";', 'private static final String TXT_RAT2 = "求求你……帮帮我……_消灭那个孽障_……";'),
        ('private static final String TXT_RAT3 = "Yes! The ugly creature is slain and I can finally rest... Please take one of these items, maybe they will be useful to you in your journey...";', 'private static final String TXT_RAT3 = "太好了！那只丑陋的怪物终于伏诛，我也可以安息了……请挑一件我的遗物吧，或许能在你的旅途中派上用场……";'),
        ('private static final String TXT_CURSE1 = "Hello adventurer... Once I was like you - strong and confident... And now I\'m dead... But I can\'t leave this place, as I am bound by a horrid curse... Please... Help me... _Destroy the curse_...";', 'private static final String TXT_CURSE1 = "你好，冒险者……曾经我也像你一样强大而自信……可如今我已死去……可怕的诅咒将我束缚于此无法安息……求求你……帮帮我……_破除这个诅咒_……";'),
        ('private static final String TXT_CURSE2 = "Thank you, %s! The curse is broken and I can finally rest... Please take one of these items, maybe they will be useful to you in your journey...";', 'private static final String TXT_CURSE2 = "谢谢你，%s！诅咒已经破除，我终于可以安息了……请收下其中一件物品吧，或许能在旅途中助你一臂之力……";'),
        ('private static final String TXT_YES = "Yes, I will do it for you";', 'private static final String TXT_YES = "好的，我这就去帮你";'),
        ('private static final String TXT_NO = "No, I can\'t help you";', 'private static final String TXT_NO = "抱歉，我帮不了你";'),
        ('                            } else {\n                                this.relocate(ghost);\n                            }', '                            } else {\n                                relocate(ghost);\n                            }'),
    ],
    "pixeldungeon/actors/mobs/npcs/Imp.java": [
        ('private static final String TXT_GOLEMS1 = "Are you an adventurer? I love adventurers! You can always rely on them if something needs to be killed. Am I right? For a bounty, of course ;)\\nIn my case this is _golems_ who need to be killed. You see, I\'m going to start a little business here, but these stupid golems are bad for business! It\'s very hard to negotiate with wandering lumps of granite, damn them! So please, kill... let\'s say _6 of them_ and a reward is yours.";', 'private static final String TXT_GOLEMS1 = "你是冒险者吗？我最喜欢冒险者了！只要有东西需要干掉，找他们准没错，对吧？当然是有赏金的 ;)\\n我这边需要清理的是那些_魔像_。你看，我打算在这儿做点小买卖，但那些蠢魔像太妨碍生意了！跟四处晃悠的花岗岩疙瘩根本没法讲道理！所以请帮我干掉……就算 _6 只_吧，报酬绝对少不了你。";'),
        ('private static final String TXT_MONKS1 = "Are you an adventurer? I love adventurers! You can always rely on them if something needs to be killed. Am I right? For a bounty, of course ;)\\nIn my case this is _monks_ who need to be killed. You see, I\'m going to start a little business here, but these lunatics don\'t buy anything themselves and will scare away other customers. So please, kill... let\'s say _8 of them_ and a reward is yours.";', 'private static final String TXT_MONKS1 = "你是冒险者吗？我最喜欢冒险者了！只要有东西需要干掉，找他们准没错，对吧？当然是有赏金的 ;)\\n我这边需要清理的是那些_武僧_。你看，我打算在这儿做点小买卖，但这些狂热分子自己啥也不买，还会吓跑其他客人。所以请帮我干掉……就算 _8 个_吧，报酬绝对少不了你。";'),
        ('private static final String TXT_GOLEMS2 = "How is your golem safari going?";', 'private static final String TXT_GOLEMS2 = "你的魔像狩猎进展如何啦？";'),
        ('private static final String TXT_MONKS2 = "Oh, you are still alive! I knew that your kung-fu is stronger ;) Just don\'t forget to grab these monks\' tokens.";', 'private static final String TXT_MONKS2 = "哦，你还活着呢！我就知道你的功夫更胜一筹 ;) 别忘了把那些武僧的徽记带回来。";'),
        ('private static final String TXT_CYA = "See you, %s!";', 'private static final String TXT_CYA = "我们会再见的，%s！";'),
        ('private static final String TXT_HEY = "Psst, %s!";', 'private static final String TXT_HEY = "喂喂喂，%s！";'),
        ('this.name = "ambitious imp";', 'this.name = "野心勃勃的小恶魔";'),
        ('return "evaded";', 'return "闪避";'),
        ('return "Imps are lesser demons. They are notable for neither their strength nor their magic talent, but they are quite smart and sociable. Many imps prefer to live among non-demons.";', 'return "小恶魔是低阶恶魔。它们既不以力量见长也没有魔法天赋，但十分聪明且善于交际，许多小恶魔更愿意混迹于非恶魔种族之中。";'),
    ],
    "pixeldungeon/actors/mobs/npcs/ImpShopkeeper.java": [
        ('private static final String TXT_GREETINGS = "Hello, friend!";', 'private static final String TXT_GREETINGS = "你好啊，朋友！";'),
        ('this.name = "ambitious imp";', 'this.name = "野心勃勃的小恶魔";'),
        ('return "Imps are lesser demons. They are notable for neither their strength nor their magic talent. But they are quite smart and sociable, and many of imps prefer to live and do business among non-demons.";', 'return "小恶魔是低阶恶魔。它们既不以力量见长也没有魔法天赋，但十分聪明且善于交际，许多小恶魔喜欢在非恶魔种族之间做生意。";'),
    ],
    "pixeldungeon/actors/mobs/npcs/MirrorImage.java": [
        ('this.name = "mirror image";', 'this.name = "镜像";'),
        ('return "This illusion bears a close resemblance to you, but it\'s paler and twitches a little.";', 'return "这个幻象与你十分相像，只是面色更加苍白，还微微有些抽搐。";'),
    ],
    "pixeldungeon/actors/mobs/npcs/RatKing.java": [
        ('this.name = "rat king";', 'this.name = "鼠王";'),
        ('this.yell("I\'m not sleeping!");', 'this.yell("我可不是在睡觉！");'),
        ('this.yell("What is it? I have no time for this nonsense. My kingdom won\'t rule itself!");', 'this.yell("你这是想干什么？我可没时间管这些破事。我的王国可不会自己运转下去！");'),
        ('return "This rat is a little bigger than a regular marsupial rat and it\'s wearing a tiny crown on its head.";', 'return "这只老鼠比普通啮齿小鼠略大一些，头上还戴着一顶小小的皇冠。";'),
    ],
    "pixeldungeon/actors/mobs/npcs/Shopkeeper.java": [
        ('this.name = "shopkeeper";', 'this.name = "店主";'),
        ('return "This stout guy looks more appropriate for a trade district in some large city than for a dungeon. His prices explain why he prefers to do business here.";', 'return "这个矮胖的家伙看起来更适合待在大城市的商业区，而不是在这种地牢里。不过看他的物价就知道他为什么喜欢在这儿做生意了。";'),
        ('return GameScene.selectItem(itemSelector, WndBag.Mode.FOR_SALE, "Select an item to sell", "cursor_trade.png");', 'return GameScene.selectItem(itemSelector, WndBag.Mode.FOR_SALE, "选择要出售的物品", "cursor_trade.png");'),
    ],
    "pixeldungeon/actors/mobs/npcs/Wandmaker.java": [
        ('this.txtQuest1 = "Oh, what a pleasant surprise to meet a decent person in such place! I came here for a rare ingredient - a _Rotberry seed_. Being a magic user, I\'m quite able to defend myself against local monsters, but I\'m getting lost in no time, it\'s very embarrassing. Probably you could help me? I would be happy to pay for your service with one of my best wands.";', 'this.txtQuest1 = "哎呀，能在这种地方遇见正派人真是意外之喜！我来此是为了寻找一种稀有素材——_腐莓种子_。作为施法者，对付这里的怪物倒不在话下，但我老是一下子就迷路，实在有些尴尬。你能帮帮我吗？我非常乐意用我最好的一根法杖作为酬谢。";'),
        ('this.txtQuest2 = "Any luck with a _Rotberry seed_, %s? No? Don\'t worry, I\'m not in a hurry.";', 'this.txtQuest2 = "找到_腐莓种子_了吗，%s？还没？没关系，我不着急。";'),
        ('this.txtQuest1 = "Oh, what a pleasant surprise to meet a decent person in such place! I came here for a rare ingredient - _corpse dust_. It can be gathered from skeletal remains and there is an ample number of them in the dungeon. Being a magic user, I\'m quite able to defend myself against local monsters, but I\'m getting lost in no time, it\'s very embarrassing. Probably you could help me? I would be happy to pay for your service with one of my best wands.";', 'this.txtQuest1 = "哎呀，能在这种地方遇见正派人真是意外之喜！我来此是为了寻找一种稀有素材——_尸尘_。地牢里遗骸众多，正可从中收集。作为施法者，对付这里的怪物倒不在话下，但我老是一下子就迷路，实在有些尴尬。你能帮帮我吗？我非常乐意用我最好的一根法杖作为酬谢。";'),
        ('this.txtQuest2 = "Any luck with _corpse dust_, %s? Bone piles are the most obvious places to look.";', 'this.txtQuest2 = "找到_尸尘_了吗，%s？白骨堆是最有可能找到的地方。";'),
        ('this.txtQuest1 = "Oh, what a pleasant surprise to meet a decent person in such place! I came here for a rare ingredient: a _phantom fish_. You can catch it with your bare hands, but it\'s very hard to notice in the water. Being a magic user, I\'m quite able to defend myself against local monsters, but I\'m getting lost in no time, it\'s very embarrassing. Probably you could help me? I would be happy to pay for your service with one of my best wands.";', 'this.txtQuest1 = "哎呀，能在这种地方遇见正派人真是意外之喜！我来此是为了寻找一种稀有素材——_幻影鱼_。徒手就能捉到它，只是它在水里很难被发现。作为施法者，对付这里的怪物倒不在话下，但我老是一下子就迷路，实在有些尴尬。你能帮帮我吗？我非常乐意用我最好的一根法杖作为酬谢。";'),
        ('this.txtQuest2 = "Any luck with a _phantom fish_, %s? You may want to try searching for it in one of the local pools.";', 'this.txtQuest2 = "找到_幻影鱼_了吗，%s？你可以试着在附近的水池里搜寻一下。";'),
        ('this.name = "old wandmaker";', 'this.name = "老杖匠";'),
        ('return "absorbed";', 'return "吸收";'),
        ('return "This old but hale gentleman wears a slightly confused expression. He is protected by a magic shield.";', 'return "这位年迈却硬朗的老先生神情略显迷茫，周身笼罩着一层魔法护盾。";'),
    ],
}

if __name__ == "__main__":
    apply_file_patches(PATCHES_PART2)
