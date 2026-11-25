from patch_engine import apply_file_patches

PATCHES_PART3 = {
    "pixeldungeon/items/Amulet.java": [
        ('this.name = "Amulet of Yendor";', 'this.name = "Yendor护符";'),
        ('return "The Amulet of Yendor is the most powerful known artifact of unknown origin. It is said that the amulet is able to fulfil any wish if its owner\'s will-power is strong enough to \\"persuade\\" it to do it.";', 'return "Yendor护符是已知最强大而神秘的神器，来历不明。据说只要持有者的意志足够坚定，能够\\"说服\\"护符，它就能实现任何愿望。";'),
    ],
    "pixeldungeon/items/Ankh.java": [
        ('this.name = "Ankh";', 'this.name = "重生十字架";'),
        ('return "The ancient symbol of immortality grants an ability to return to life after death. Upon resurrection all non-equipped items are lost.";', 'return "古老的永生象征，赐予持有者死而复生的力量。复活时所有未装备的物品都会丢失。";'),
    ],
    "pixeldungeon/items/ArmorKit.java": [
        ('private static final String TXT_SELECT_ARMOR = "Select an armor to upgrade";', 'private static final String TXT_SELECT_ARMOR = "选择要升级的护甲";'),
        ('private static final String TXT_UPGRADED = "you applied the armor kit to upgrade your %s";', 'private static final String TXT_UPGRADED = "你用护甲工具包升级了你的%s";'),
        ('this.name = "armor kit";', 'this.name = "护甲工具包";'),
        ('return "Using this kit of small tools and materials anybody can transform any armor into an \\"epic armor\\", which will keep all properties of the original armor, but will also provide its wearer a special ability depending on his class. No skills in tailoring, leatherworking or blacksmithing are required.";', 'return "任何人都能用这套工具与材料将普通护甲改造成\\"史诗护甲\\"——它不仅保留原护甲的全部属性，还能赋予穿戴者对应职业的专属特技。无需任何裁缝、制革或锻造手艺。";'),
    ],
    "pixeldungeon/items/Bomb.java": [
        ('this.name = "bomb";', 'this.name = "炸弹";'),
        ('return "This is a relatively small bomb, filled with black powder. Conveniently, its fuse is lit automatically when the bomb is thrown.";', 'return "这是一枚装满黑火药的小型炸弹。贴心的是，只要扔出去引信就会自动点燃。";'),
    ],
    "pixeldungeon/items/DewVial.java": [
        ('private static final String TXT_AUTO_DRINK = "The dew vial was emptied to heal your wounds.";', 'private static final String TXT_AUTO_DRINK = "露水瓶自动清空并治愈了你的伤势。";'),
        ('private static final String TXT_COLLECTED = "You collected a dewdrop into your dew vial.";', 'private static final String TXT_COLLECTED = "你将露珠收集进了露水瓶中。";'),
        ('private static final String TXT_FULL = "Your dew vial is full!";', 'private static final String TXT_FULL = "你的露水瓶已经满了！";'),
        ('private static final String TXT_EMPTY = "Your dew vial is empty!";', 'private static final String TXT_EMPTY = "你的露水瓶是空的！";'),
        ('this.name = "dew vial";', 'this.name = "露水瓶";'),
        ('return "You can store excess dew in this tiny vessel for drinking it later. If the vial is full, in a moment of deadly peril the dew will be consumed automatically.";', 'return "你可以将多余的露珠储存在这个小瓶中以备不时之需。当露水瓶装满时，在生死攸关的瞬间会自动饮用其中的露水。";'),
    ],
    "pixeldungeon/items/Dewdrop.java": [
        ('this.name = "dewdrop";', 'this.name = "露珠";'),
        ('return "A crystal clear dewdrop.";', 'return "一滴晶莹剔透的露珠。";'),
    ],
    "pixeldungeon/items/EquipableItem.java": [
        ('private static final String TXT_UNEQUIP_CURSED = "You can\'t remove cursed %s!";', 'private static final String TXT_UNEQUIP_CURSED = "你无法卸下被诅咒的%s！";'),
    ],
    "pixeldungeon/items/Gold.java": [
        ('private static final String TXT_COLLECT = "Collect gold coins to spend them later in a shop.";', 'private static final String TXT_COLLECT = "收集金币以便之后在商店消费。";'),
        ('private static final String TXT_INFO = "A pile of %d gold coins. Collect gold coins to spend them later in a shop.";', 'private static final String TXT_INFO = "一堆共计 %d 枚的金币。收集金币以便之后在商店消费。";'),
        ('private static final String TXT_INFO_1 = "One gold coin. Collect gold coins to spend them later in a shop.";', 'private static final String TXT_INFO_1 = "一枚金币。收集金币以便之后在商店消费。";'),
        ('this.name = "gold";', 'this.name = "金币";'),
    ],
    "pixeldungeon/items/Heap.java": [
        ('private static final String TXT_MIMIC = "This is a mimic!";', 'private static final String TXT_MIMIC = "这是个宝箱怪！";'),
        ('this.items = new LinkedList<Bundlable>(bundle.getCollection(ITEMS));', 'this.items = new LinkedList<Item>();\n        for (com.watabou.utils.Bundlable b : bundle.getCollection(ITEMS)) {\n            this.items.add((Item)b);\n        }'),
    ],
    "pixeldungeon/items/Honeypot.java": [
        ('this.name = "honeypot";', 'this.name = "蜂蜜罐";'),
        ('return "There is not much honey in this small honeypot, but there is a golden bee there and it doesn\'t want to leave it.";', 'return "这个小蜂蜜罐里没剩多少蜂蜜，但里面住着一只不肯离开的金色蜜蜂。";'),
    ],
    "pixeldungeon/items/Item.java": [
        ('private static final String TXT_PACK_FULL = "Your pack is too full for the %s";', 'private static final String TXT_PACK_FULL = "你的背包太满了，装不下%s";'),
        ('private static final String TXT_BROKEN = "Because of frequent use, your %s has broken.";', 'private static final String TXT_BROKEN = "由于频繁使用，你的%s已经损坏了。";'),
        ('private static final String TXT_GONNA_BREAK = "Because of frequent use, your %s is going to break soon.";', 'private static final String TXT_GONNA_BREAK = "由于频繁使用，你的%s快要损坏了。";'),
        ('protected String name = "smth";', 'protected String name = "某物";'),
        ('return "Choose direction of throw";', 'return "选择投掷方向";'),
    ],
    "pixeldungeon/items/KindOfWeapon.java": [
        ('private static final String TXT_EQUIP_CURSED = "you wince as your grip involuntarily tightens around your %s";', 'private static final String TXT_EQUIP_CURSED = "你的手不由自主地紧握住%s，疼得抽搐了一下";'),
    ],
    "pixeldungeon/items/LloydsBeacon.java": [
        ('private static final String TXT_PREVENTING = "Strong magic aura of this place prevents you from using the lloyd\'s beacon!";', 'private static final String TXT_PREVENTING = "这里强大的魔力场使你无法使用时空道标！";'),
        ('private static final String TXT_CREATURES = "Psychic aura of neighbouring creatures doesn\'t allow you to use the lloyd\'s beacon at this moment.";', 'private static final String TXT_CREATURES = "附近生物的精神波动正在干扰你的时空道标，此刻无法使用。";'),
        ('private static final String TXT_RETURN = "The lloyd\'s beacon is successfully set at your current location, now you can return here anytime.";', 'private static final String TXT_RETURN = "时空道标已成功设置在你当前的位置，现在你可以随时返回这里。";'),
        ('private static final String TXT_INFO = "Lloyd\'s beacon is an intricate magic device, that allows you to return to a place you have already been.";', 'private static final String TXT_INFO = "时空道标是一件精密的魔法装置，能让你瞬间返回曾经去过的地方。";'),
        ('private static final String TXT_SET = "\\n\\nThis beacon was set somewhere on the level %d of Pixel Dungeon.";', 'private static final String TXT_SET = "\\n\\n此道标已被设定在像素地牢第 %d 层的某处。";'),
        ('this.name = "lloyd\'s beacon";', 'this.name = "时空道标";'),
    ],
    "pixeldungeon/items/TomeOfMastery.java": [
        ('private static final String TXT_BLINDED = "You can\'t read while blinded";', 'private static final String TXT_BLINDED = "失明时无法阅读";'),
        ('this.name = Dungeon.hero != null && Dungeon.hero.subClass != HeroSubClass.NONE ? "Tome of Remastery" : "Tome of Mastery";', 'this.name = Dungeon.hero != null && Dungeon.hero.subClass != HeroSubClass.NONE ? "重修之书" : "精通之书";'),
        ('return "This worn leather book is not that thick, but you feel somehow, that you can gather a lot from it. Remember though that reading this tome may require some time.";', 'return "这本破旧的皮面古书并不算厚，但你隐隐感到能从中领悟到许多奥秘。不过阅读它需要花费一些时间。";'),
        ('GLog.w("You have chosen the way of the %s!", Utils.capitalize(way.title()));', 'GLog.w("你选择了%s之道！", Utils.capitalize(way.title()));'),
    ],
    "pixeldungeon/items/Torch.java": [
        ('this.name = "torch";', 'this.name = "火把";'),
        ('return "It\'s an indispensable item in The Demon Halls, which are notorious for their poor ambient lighting.";', 'return "在以光线昏暗而闻名的恶魔大厅中，这是一件不可或缺的照明工具。";'),
    ],
    "pixeldungeon/items/Weightstone.java": [
        ('private static final String TXT_SELECT_WEAPON = "Select a weapon to balance";', 'private static final String TXT_SELECT_WEAPON = "选择要配重的武器";'),
        ('private static final String TXT_FAST = "you balanced your %s to make it faster";', 'private static final String TXT_FAST = "你调整了%s的配重，使其攻速更快";'),
        ('private static final String TXT_ACCURATE = "you balanced your %s to make it more accurate";', 'private static final String TXT_ACCURATE = "你调整了%s的配重，使其更加精准";'),
        ('this.name = "weightstone";', 'this.name = "平衡石";'),
        ('return "Using a weightstone, you can balance your melee weapon to increase its speed or accuracy.";', 'return "使用平衡石可以调整近战武器的配重，从而提升其攻击速度或命中精准度。";'),
        ('private static final String TXT_CHOICE = "How would you like to balance your %s?";', 'private static final String TXT_CHOICE = "你想如何为%s配重？";'),
        ('private static final String TXT_SPEED = "For speed";', 'private static final String TXT_SPEED = "追求攻速";'),
        ('private static final String TXT_ACCURACY = "For accuracy";', 'private static final String TXT_ACCURACY = "追求精准";'),
        ('private static final String TXT_CANCEL = "Never mind";', 'private static final String TXT_CANCEL = "算了";'),
    ],

    # Armor
    "pixeldungeon/items/armor/Armor.java": [
        ('private static final String TXT_EQUIP_CURSED = "your %s constricts around you painfully";', 'private static final String TXT_EQUIP_CURSED = "你的%s痛苦地勒紧了你的身体";'),
        ('private static final String TXT_IDENTIFY = "you are now familiar enough with your %s to identify it. It is %s.";', 'private static final String TXT_IDENTIFY = "你对这件%s已经足够熟悉并完成了鉴定，它是%s。";'),
        ('private static final String TXT_BROKEN = "broken %s :%d";', 'private static final String TXT_BROKEN = "损坏的%s :%d";'),
        ('private static final String TXT_INCOMPATIBLE = "Interaction of different types of magic has erased the glyph on this armor!";', 'private static final String TXT_INCOMPATIBLE = "不同属性的魔法相冲突，消除了护甲上的刻印！";'),
        ('info.append("\\n\\nThis " + name + " provides damage absorption up to " + "" + Math.max(this.DR(), 0) + " points per attack. ");', 'info.append("\\n\\n这件" + name + "每次受击最多可吸收 " + Math.max(this.DR(), 0) + " 点伤害。");'),
        ('info.append("\\n\\nBecause of your inadequate strength your movement speed and defense skill is decreased. ");', 'info.append("\\n\\n由于力量不足，你的移动速度与闪避能力降低了。");'),
        ('info.append("\\n\\nBecause of your inadequate strength wearing this armor will decrease your movement speed and defense skill. ");', 'info.append("\\n\\n由于力量不足，穿戴这件护甲会降低你的移动速度与闪避能力。");'),
        ('info.append("\\n\\nTypical " + name + " provides damage absorption up to " + this.typicalDR() + " points per attack " + " and requires " + this.typicalSTR() + " points of strength. ");', 'info.append("\\n\\n普通的" + name + "每次受击最多可吸收 " + this.typicalDR() + " 点伤害，通常需要 " + this.typicalSTR() + " 点力量。");'),
        ('info.append("Probably this armor is too heavy for you. ");', 'info.append("这件护甲对你来说可能太重了。");'),
        ('info.append("It is enchanted.");', 'info.append("它带有魔法刻印。");'),
        ('info.append("\\n\\nYou are wearing the " + name + (this.cursed ? ", and because it is cursed, you are powerless to remove it." : "."));', 'info.append("\\n\\n你正穿着这件" + name + (this.cursed ? "，而且因为它被诅咒了，你无法将其脱下。" : "。"));'),
        ('info.append("\\n\\nYou can feel a malevolent magic lurking within the " + name + ".");', 'info.append("\\n\\n你能感受到这件" + name + "中潜藏着一股邪恶的魔力。");'),
    ],
    "pixeldungeon/items/armor/ClassArmor.java": [
        ('private static final String TXT_LOW_HEALTH = "Your health is too low!";', 'private static final String TXT_LOW_HEALTH = "你的生命值过低！";'),
        ('private static final String TXT_NOT_EQUIPPED = "You need to be wearing this armor to use its special power!";', 'private static final String TXT_NOT_EQUIPPED = "你需要穿戴这套护甲才能使用它的特技！";'),
        ('return "The thing looks awesome!";', 'return "这件护甲看上去帅呆了！";'),
    ],
    "pixeldungeon/items/armor/ClothArmor.java": [
        ('this.name = "cloth armor";', 'this.name = "布甲";'),
        ('return "This lightweight armor offers basic protection.";', 'return "这件轻便的护甲能提供最基本的防御。";'),
    ],
    "pixeldungeon/items/armor/HuntressArmor.java": [
        ('private static final String TXT_NO_ENEMIES = "No enemies in sight";', 'private static final String TXT_NO_ENEMIES = "视野内没有敌人";'),
        ('private static final String TXT_NOT_HUNTRESS = "Only huntresses can use this armor!";', 'private static final String TXT_NOT_HUNTRESS = "只有女猎手才能使用这件护甲！";'),
        ('this.name = "huntress cloak";', 'this.name = "女猎手披风";'),
        ('return "A huntress in such cloak can create a fan of spectral blades. Each of these blades will target a single enemy in the huntress\'s field of view, inflicting damage depending on her currently equipped melee weapon.";', 'return "身穿这件披风的女猎手可以扇形射出一排灵魂飞刃，每把飞刃都会追踪视野内的一名敌人，并依据当前装备的近战武器造成伤害。";'),
    ],
    "pixeldungeon/items/armor/LeatherArmor.java": [
        ('this.name = "leather armor";', 'this.name = "皮甲";'),
        ('return "Armor made from tanned monster hide. Not as light as cloth armor but provides better protection.";', 'return "用鞣制的兽皮制成的护甲。没有布甲轻，但提供更好的防御。";'),
    ],
    "pixeldungeon/items/armor/MageArmor.java": [
        ('private static final String TXT_NOT_MAGE = "Only mages can use this armor!";', 'private static final String TXT_NOT_MAGE = "只有法师才能使用这件护甲！";'),
        ('this.name = "mage robe";', 'this.name = "法师长袍";'),
        ('return "Wearing this gorgeous robe, a mage can cast a spell of molten earth: all the enemies in his field of view will be set on fire and unable to move at the same time.";', 'return "穿着这件华丽法袍的法师可以施放熔岩大地法术：视野内的所有敌人都会被点燃并同时被定身在原地。";'),
    ],
    "pixeldungeon/items/armor/MailArmor.java": [
        ('this.name = "mail armor";', 'this.name = "链甲";'),
        ('return "Interlocking metal links make for a tough but flexible suit of armor.";', 'return "由金属链环环相扣制成的一套结实又不失灵活的护甲。";'),
    ],
    "pixeldungeon/items/armor/PlateArmor.java": [
        ('this.name = "plate armor";', 'this.name = "板甲";'),
        ('return "Enormous plates of metal are joined together into a suit that provides unmatched protection to any adventurer strong enough to bear its staggering weight.";', 'return "厚重的金属板拼接到一起，为能承受其骇人重量的冒险家提供无与伦比的防御。";'),
    ],
    "pixeldungeon/items/armor/RogueArmor.java": [
        ('private static final String TXT_FOV = "You can only jump to an empty location in your field of view";', 'private static final String TXT_FOV = "你只能跳跃到视野内的空地上";'),
        ('private static final String TXT_NOT_ROGUE = "Only rogues can use this armor!";', 'private static final String TXT_NOT_ROGUE = "只有盗贼才能使用这件护甲！";'),
        ('return "Choose a location to jump to";', 'return "选择要跳跃的位置";'),
        ('this.name = "rogue garb";', 'this.name = "盗贼夜行衣";'),
        ('return "Wearing this dark garb, a rogue can perform a trick, that is called \\"smoke bomb\\" (though no real explosives are used): he blinds enemies who could see him and jumps aside.";', 'return "穿着这套深色夜行衣的盗贼可以施展名为\\"烟雾弹\\"的戏法（虽然并没有用到真炸药）：致盲视野内能看到他的敌人并借机闪身脱逃。";'),
    ],
    "pixeldungeon/items/armor/ScaleArmor.java": [
        ('this.name = "scale armor";', 'this.name = "鳞甲";'),
        ('return "The metal scales sewn onto a leather vest create a flexible, yet protective armor.";', 'return "在厚实的皮背心中缝入金属鳞片，形成了灵活而高防御的护甲。";'),
    ],
    "pixeldungeon/items/armor/WarriorArmor.java": [
        ('private static final String TXT_NOT_WARRIOR = "Only warriors can use this armor!";', 'private static final String TXT_NOT_WARRIOR = "只有战士才能使用这件护甲！";'),
        ('return "Choose direction to leap";', 'return "选择飞跃的目标位置";'),
        ('this.name = "warrior suit of armor";', 'this.name = "战士重铠";'),
        ('return "While this armor looks heavy, it allows a warrior to perform heroic leap towards a targeted location, slamming down to stun all neighbouring enemies.";', 'return "这副铠甲看似笨重，却能让战士向目标位置发动英勇之跃，重重落地并震晕周围所有敌人。";'),
    ],

    # Glyphs
    "pixeldungeon/items/armor/glyphs/Affection.java": [('private static final String TXT_AFFECTION = "%s of affection";', 'private static final String TXT_AFFECTION = "魅惑之%s";')],
    "pixeldungeon/items/armor/glyphs/AntiEntropy.java": [('private static final String TXT_ANTI_ENTROPY = "%s of anti-entropy";', 'private static final String TXT_ANTI_ENTROPY = "反熵之%s";')],
    "pixeldungeon/items/armor/glyphs/AutoRepair.java": [('private static final String TXT_AUTO_REPAIR = "%s of auto-repair";', 'private static final String TXT_AUTO_REPAIR = "自修之%s";')],
    "pixeldungeon/items/armor/glyphs/Bounce.java": [('private static final String TXT_BOUNCE = "%s of bounce";', 'private static final String TXT_BOUNCE = "斥力之%s";')],
    "pixeldungeon/items/armor/glyphs/Displacement.java": [('private static final String TXT_DISPLACEMENT = "%s of displacement";', 'private static final String TXT_DISPLACEMENT = "定相之%s";')],
    "pixeldungeon/items/armor/glyphs/Entanglement.java": [('private static final String TXT_ENTANGLEMENT = "%s of entanglement";', 'private static final String TXT_ENTANGLEMENT = "缠绕之%s";')],
    "pixeldungeon/items/armor/glyphs/Metabolism.java": [('private static final String TXT_METABOLISM = "%s of metabolism";', 'private static final String TXT_METABOLISM = "代谢之%s";')],
    "pixeldungeon/items/armor/glyphs/Multiplicity.java": [('private static final String TXT_MULTIPLICITY = "%s of multiplicity";', 'private static final String TXT_MULTIPLICITY = "分身之%s";')],
    "pixeldungeon/items/armor/glyphs/Potential.java": [('private static final String TXT_POTENTIAL = "%s of potential";', 'private static final String TXT_POTENTIAL = "电势之%s";')],
    "pixeldungeon/items/armor/glyphs/Stench.java": [('private static final String TXT_STENCH = "%s of stench";', 'private static final String TXT_STENCH = "恶臭之%s";')],
    "pixeldungeon/items/armor/glyphs/Viscosity.java": [
        ('private static final String TXT_VISCOSITY = "%s of viscosity";', 'private static final String TXT_VISCOSITY = "粘稠之%s";'),
        ('defender.sprite.showStatus(0xFF8800, "deferred %d", damage);', 'defender.sprite.showStatus(0xFF8800, "延缓 %d", damage);'),
        ('return Utils.format("Defered damage (%d)", this.damage);', 'return Utils.format("延缓伤害 (%d)", this.damage);'),
        ('Dungeon.fail(Utils.format("Killed by the %s on level %d", "enchantment of viscosity", Dungeon.depth));', 'Dungeon.fail(Utils.format("在第 %2$d 层死于%1$s", "粘稠刻印", Dungeon.depth));'),
        ('GLog.n("The enchantment of viscosity killed you...", new Object[0]);', 'GLog.n("粘稠刻印夺走了你的生命...", new Object[0]);'),
    ],

    # Bags
    "pixeldungeon/items/bags/Keyring.java": [
        ('this.name = "key ring";', 'this.name = "钥匙环";'),
        ('return "This is a copper key ring, that lets you keep all your keys separately from the rest of your belongings.";', 'return "这是一个黄铜钥匙环，可以让你把所有钥匙单独收纳在一起，不占用背包空间。";'),
    ],
    "pixeldungeon/items/bags/ScrollHolder.java": [
        ('this.name = "scroll holder";', 'this.name = "卷轴筒";'),
        ('return "You can place any number of scrolls into this tubular container. It saves room in your backpack and protects scrolls from fire.";', 'return "你可以把任意数量的卷轴放入这个圆筒容器中，既节省背包空间，又能保护卷轴免受火焰烧毁。";'),
    ],
    "pixeldungeon/items/bags/SeedPouch.java": [
        ('this.name = "seed pouch";', 'this.name = "种子袋";'),
        ('return "This small velvet pouch allows you to store any number of seeds in it. Very convenient.";', 'return "这个天鹅绒小袋子可以让你收纳任意数量的植物种子，非常方便。";'),
    ],
    "pixeldungeon/items/bags/WandHolster.java": [
        ('this.name = "wand holster";', 'this.name = "法杖套";'),
        ('return "This slim holder is made of leather of some exotic animal. It allows to compactly carry up to " + this.size + " wands.";', 'return "这个纤细的护套由某种奇异野兽的皮革制成，最多可紧凑地收纳 " + this.size + " 根法杖。";'),
    ],

    # Food
    "pixeldungeon/items/food/ChargrilledMeat.java": [
        ('this.name = "chargrilled meat";', 'this.name = "烤肉";'),
        ('return "It looks like a decent steak.";', 'return "看起来像块不错的牛排。";'),
    ],
    "pixeldungeon/items/food/Food.java": [
        ('public String message = "That food tasted delicious!";', 'public String message = "吃起来真美味！";'),
        ('this.name = "ration of food";', 'this.name = "口粮";'),
        ('return "Nothing fancy here: dried meat, some biscuits - things like that.";', 'return "里面都是些寻常干粮：一片肉干，几块饼干——诸如此类。";'),
    ],
    "pixeldungeon/items/food/FrozenCarpaccio.java": [
        ('this.name = "frozen carpaccio";', 'this.name = "冷冻生肉片";'),
        ('GLog.i("You see your hands turn invisible!", new Object[0]);', 'GLog.i("你看到自己的双手变得透明了！", new Object[0]);'),
        ('GLog.i("You feel your skin hardens!", new Object[0]);', 'GLog.i("你感到自己的皮肤变硬了！", new Object[0]);'),
        ('GLog.i("Refreshing!", new Object[0]);', 'GLog.i("神清气爽！", new Object[0]);'),
        ('GLog.i("You feel better!", new Object[0]);', 'GLog.i("你感觉好多了！", new Object[0]);'),
        ('return "It\'s a piece of frozen raw meat. The only way to eat it is by cutting thin slices of it. And this way it\'s suprisingly good.";', 'return "这是一块冻硬的生肉。唯一吃下去的办法就是把它切成薄片——而且这么吃味道意外地不错。";'),
    ],
    "pixeldungeon/items/food/MysteryMeat.java": [
        ('this.name = "mystery meat";', 'this.name = "神秘的肉";'),
        ('this.message = "That food tasted... strange.";', 'this.message = "这玩意儿吃起来很...奇怪。";'),
        ('GLog.w("Oh it\'s hot!", new Object[0]);', 'GLog.w("嗷！好烫！", new Object[0]);'),
        ('GLog.w("You can\'t feel your legs!", new Object[0]);', 'GLog.w("你的双腿失去了知觉！", new Object[0]);'),
        ('GLog.w("You are not feeling well.", new Object[0]);', 'GLog.w("你感觉不太对劲。", new Object[0]);'),
        ('GLog.w("You are stuffed.", new Object[0]);', 'GLog.w("你吃撑了。", new Object[0]);'),
        ('return "Eat at your own risk!";', 'return "想吃可以，后果自负！";'),
    ],
    "pixeldungeon/items/food/OverpricedRation.java": [
        ('this.name = "overpriced food ration";', 'this.name = "高价口粮";'),
        ('this.message = "That food tasted ok.";', 'this.message = "味道还算凑合。";'),
        ('return "It looks exactly like a standard ration of food but smaller.";', 'return "它看起来和普通口粮一模一样，只是份量小了一圈。";'),
    ],
    "pixeldungeon/items/food/Pasty.java": [
        ('this.name = "pasty";', 'this.name = "馅饼";'),
        ('return "This is authentic Cornish pasty with traditional filling of beef and potato.";', 'return "这是一个正宗的康沃尔馅饼，里面包着传统的牛肉与土豆馅料。";'),
    ],

    # Keys
    "pixeldungeon/items/keys/GoldenKey.java": [
        ('this.name = "golden key";', 'this.name = "金钥匙";'),
        ('return "The notches on this golden key are tiny and intricate. Maybe it can open some chest lock?";', 'return "这把金钥匙的匙齿细密而精致，或许能打开某只宝箱上的锁？";'),
    ],
    "pixeldungeon/items/keys/IronKey.java": [
        ('private static final String TXT_FROM_DEPTH = "iron key from depth %d";', 'private static final String TXT_FROM_DEPTH = "第 %d 层的铁钥匙";'),
        ('this.name = "iron key";', 'this.name = "铁钥匙";'),
        ('return "The notches on this ancient iron key are well worn; its leather lanyard is battered by age. What door might it open?";', 'return "这把古老铁钥匙的匙齿已严重磨损，皮质挂绳也饱经岁月摧残。它对应的究竟是哪扇门呢？";'),
    ],
    "pixeldungeon/items/keys/SkeletonKey.java": [
        ('this.name = "skeleton key";', 'this.name = "骷髅钥匙";'),
        ('return "This key looks serious: its head is shaped like a skull. Probably it can open some serious door.";', 'return "这把钥匙看上去非同寻常，钥匙柄雕成了骷髅的形状，想必能打开某扇重要的门。";'),
    ],

    # Quest items
    "pixeldungeon/items/quest/CorpseDust.java": [
        ('this.name = "corpse dust";', 'this.name = "尸尘";'),
        ('return "The ball of corpse dust doesn\'t differ outwardly from a regular dust ball. However, you know somehow that it\'s better to get rid of it as soon as possible.";', 'return "这团尸尘外表看上去与普通的灰尘没什么两样，但你本能地觉得最好尽快把它处理掉。";'),
    ],
    "pixeldungeon/items/quest/DarkGold.java": [
        ('this.name = "dark gold ore";', 'this.name = "暗金矿";'),
        ('return "This metal is called dark not because of its color (it doesn\'t differ from the normal gold), but because it melts under the daylight, making it useless on the surface.";', 'return "这种金属之所以叫暗金，并非因为颜色发黑（它和普通黄金一样），而是因为它一见日光就会熔化，在地表毫无用处。";'),
    ],
    "pixeldungeon/items/quest/DriedRose.java": [
        ('this.name = "dried rose";', 'this.name = "干枯玫瑰";'),
        ('return "The rose has dried long ago, but it has kept all its petals somehow.";', 'return "这朵玫瑰早已干枯，却不知为何完好地保留了全部花瓣。";'),
    ],
    "pixeldungeon/items/quest/DwarfToken.java": [
        ('this.name = "dwarf token";', 'this.name = "矮人徽记";'),
        ('return "Many dwarves and some of their larger creations carry these small pieces of metal of unknown purpose. Maybe they are jewelry or maybe some kind of ID. Dwarves are strange folk.";', 'return "许多矮人和他们的大型造物都会携带这种用途不明的小块金属。也许是饰品，也许是某种身份牌——矮人真是群古怪的家伙。";'),
    ],
    "pixeldungeon/items/quest/PhantomFish.java": [
        ('this.name = "phantom fish";', 'this.name = "幻影鱼";'),
        ('GLog.i("You see your hands turn invisible!", new Object[0]);', 'GLog.i("你看到自己的双手变得透明了！", new Object[0]);'),
        ('return "You can barely see this tiny translucent fish in the air. In the water it becomes effectively invisible.";', 'return "这条半透明的小鱼在空气中都几乎看不清，一旦回到水里就会完全隐形。";'),
    ],
    "pixeldungeon/items/quest/Pickaxe.java": [
        ('private static final String TXT_NO_VEIN = "There is no dark gold vein near you to mine";', 'private static final String TXT_NO_VEIN = "你附近没有可挖掘的暗金矿脉";'),
        ('this.name = "pickaxe";', 'this.name = "镐子";'),
        ('GLog.i("You now have %s", gold.name());', 'GLog.i("你获得了%s", gold.name());'),
        ('return "This is a large and sturdy tool for breaking rocks. Probably it can be used as a weapon.";', 'return "这是一把结实沉重的碎石工具，或许也能拿来当武器使。";'),
    ],
    "pixeldungeon/items/quest/RatSkull.java": [
        ('this.name = "giant rat skull";', 'this.name = "巨鼠头骨";'),
        ('return "It could be a nice hunting trophy, but it smells too bad to place it on a wall.";', 'return "这本可以当成不错的狩猎战利品，但味道实在是太冲了，根本没法挂在墙上。";'),
    ],

    # Weapons & Enchantments
    "pixeldungeon/items/weapon/Weapon.java": [
        ('private static final String TXT_IDENTIFY = "You are now familiar enough with your %s to identify it. It is %s.";', 'private static final String TXT_IDENTIFY = "你对这件%s已经足够熟悉并完成了鉴定，它是%s。";'),
        ('private static final String TXT_INCOMPATIBLE = "Interaction of different types of magic has negated the enchantment on this weapon!";', 'private static final String TXT_INCOMPATIBLE = "不同属性的魔法相冲突，消除了武器上的附魔！";'),
        ('private static final String TXT_BROKEN = "broken %s :%d";', 'private static final String TXT_BROKEN = "损坏的%s :%d";'),
    ],
    "pixeldungeon/items/weapon/enchantments/Death.java": [('private static final String TXT_GRIM = "grim %s";', 'private static final String TXT_GRIM = "死神%s";')],
    "pixeldungeon/items/weapon/enchantments/Fire.java": [('private static final String TXT_BLAZING = "blazing %s";', 'private static final String TXT_BLAZING = "烈焰%s";')],
    "pixeldungeon/items/weapon/enchantments/Horror.java": [('private static final String TXT_ELDRITCH = "eldritch %s";', 'private static final String TXT_ELDRITCH = "异质%s";')],
    "pixeldungeon/items/weapon/enchantments/Instability.java": [('private static final String TXT_UNSTABLE = "Unstable %s";', 'private static final String TXT_UNSTABLE = "紊乱%s";')],
    "pixeldungeon/items/weapon/enchantments/Leech.java": [('private static final String TXT_VAMPIRIC = "vampiric %s";', 'private static final String TXT_VAMPIRIC = "血饮%s";')],
    "pixeldungeon/items/weapon/enchantments/Luck.java": [('private static final String TXT_LUCKY = "lucky %s";', 'private static final String TXT_LUCKY = "幸运%s";')],
    "pixeldungeon/items/weapon/enchantments/Paralysis.java": [('private static final String TXT_STUNNING = "stunning %s";', 'private static final String TXT_STUNNING = "震慑%s";')],
    "pixeldungeon/items/weapon/enchantments/Poison.java": [('private static final String TXT_VENOMOUS = "venomous %s";', 'private static final String TXT_VENOMOUS = "猛毒%s";')],
    "pixeldungeon/items/weapon/enchantments/Shock.java": [('private static final String TXT_SHOCKING = "shocking %s";', 'private static final String TXT_SHOCKING = "电击%s";')],
    "pixeldungeon/items/weapon/enchantments/Slow.java": [('private static final String TXT_CHILLING = "chilling %s";', 'private static final String TXT_CHILLING = "寒霜%s";')],
    "pixeldungeon/items/weapon/enchantments/Slowness.java": [('private static final String TXT_CHILLING = "chilling %s";', 'private static final String TXT_CHILLING = "寒霜%s";')],
    "pixeldungeon/items/weapon/enchantments/Tempering.java": [('private static final String TXT_TEMPERED = "tempered %s";', 'private static final String TXT_TEMPERED = "淬炼%s";')],

    # Melee weapons
    "pixeldungeon/items/weapon/melee/BattleAxe.java": [
        ('this.name = "battle axe";', 'this.name = "战斧";'),
        ('return "The enormous steel head of this battle axe puts considerable heft behind each stroke.";', 'return "这把战斧巨大的精钢斧刃让每一次挥砍都带着沉重无比的力道。";'),
    ],
    "pixeldungeon/items/weapon/melee/Dagger.java": [
        ('this.name = "dagger";', 'this.name = "匕首";'),
        ('return "A simple iron dagger with a well worn wooden handle.";', 'return "一把朴素的铁匕首，木质手柄已经磨得光滑。";'),
    ],
    "pixeldungeon/items/weapon/melee/Glaive.java": [
        ('this.name = "glaive";', 'this.name = "关刀";'),
        ('return "A polearm consisting of a sword blade on the end of a pole.";', 'return "一种长柄武器，长杆顶端装有宽阔锋利的大刀刃。";'),
    ],
    "pixeldungeon/items/weapon/melee/Knuckles.java": [
        ('this.name = "knuckleduster";', 'this.name = "指虎";'),
        ('return "A piece of iron shaped to fit around the knuckles.";', 'return "一块打造成贴合指关节形状的铁块，轻巧而难以被缴械。";'),
    ],
    "pixeldungeon/items/weapon/melee/Longsword.java": [
        ('this.name = "longsword";', 'this.name = "长剑";'),
        ('return "This towering blade inflicts heavy damage by investing its heft into every cut.";', 'return "这柄修长沉重的剑刃将全身重量倾注于每一次斩击之中，威力惊人。";'),
    ],
    "pixeldungeon/items/weapon/melee/Mace.java": [
        ('this.name = "mace";', 'this.name = "硬头锤";'),
        ('return "The iron head of this weapon inflicts substantial damage.";', 'return "沉重的铁质锤头能造成极为可观的钝击伤害。";'),
    ],
    "pixeldungeon/items/weapon/melee/MeleeWeapon.java": [
        ('String quality = lvl != 0 ? (lvl > 0 ? (this.isBroken() ? "broken upgraded" : "upgraded") : "degraded") : "";', 'String quality = lvl != 0 ? (lvl > 0 ? (this.isBroken() ? "破损的强化" : "经过强化的") : "被削弱的") : "";'),
        ('info.append("This " + this.name + " is " + Utils.indefinite(quality));', 'info.append("这把" + this.name + "是一件" + quality);'),
        ('info.append(" tier-" + this.tier + " melee weapon. ");', 'info.append(" " + this.tier + " 阶近战武器。");'),
        ('info.append("Its average damage is " + (min + (max - min) / 2) + " points per hit. ");', 'info.append("它的平均每次命中伤害为 " + (min + (max - min) / 2) + " 点。");'),
        ('info.append("Its typical average damage is " + (min + (max - min) / 2) + " points per hit " + "and usually it requires " + this.typicalSTR() + " points of strength. ");', 'info.append("它通常的平均每次命中伤害为 " + (min + (max - min) / 2) + " 点，一般需要 " + this.typicalSTR() + " 点力量。");'),
        ('info.append("Probably this weapon is too heavy for you. ");', 'info.append("这把武器对你来说可能过重了。");'),
        ('info.append("This is a rather " + (this.DLY < 1.0f ? "fast" : "slow"));', 'info.append("这是一把攻速较" + (this.DLY < 1.0f ? "快" : "慢"));'),
        ('info.append(" and ");', 'info.append("且");'),
        ('info.append(" but ");', 'info.append("但");'),
        ('info.append(this.ACU > 1.0f ? "accurate" : "inaccurate");', 'info.append(this.ACU > 1.0f ? "精准" : "不够精准");'),
        ('info.append(" weapon. ");', 'info.append("的武器。");'),
        ('info.append("This is a rather " + (this.ACU > 1.0f ? "accurate" : "inaccurate") + " weapon. ");', 'info.append("这是一把" + (this.ACU > 1.0f ? "较为精准" : "不太精准") + "的武器。");'),
        ('info.append("It was balanced to make it faster. ");', 'info.append("它经过配重调整，攻速更快。");'),
        ('info.append("It was balanced to make it more accurate. ");', 'info.append("它经过配重调整，更为精准。");'),
        ('info.append("It is enchanted.");', 'info.append("它带有魔法附魔。");'),
        ('info.append("Because of your inadequate strength the accuracy and speed of your attack with this " + this.name + " is decreased.");', 'info.append("由于力量不足，使用这把" + this.name + "时你的命中率与攻击速度都会下降。");'),
        ('info.append("Because of your excess strength the damage of your attack with this " + this.name + " is increased.");', 'info.append("由于力量充沛，使用这把" + this.name + "时你的攻击伤害得到了提升。");'),
        ('info.append("You hold the " + this.name + " at the ready" + (this.cursed ? ", and because it is cursed, you are powerless to let go." : "."));', 'info.append("你已装备这把" + this.name + (this.cursed ? "，而且因为它被诅咒了，你无法将其卸下。" : "。"));'),
        ('info.append("You can feel a malevolent magic lurking within " + this.name + ".");', 'info.append("你能感受到这把" + this.name + "中潜藏着一股邪恶的魔力。");'),
    ],
    "pixeldungeon/items/weapon/melee/Quarterstaff.java": [
        ('this.name = "quarterstaff";', 'this.name = "铁头棍";'),
        ('return "A staff of hardwood, its ends are shod with iron.";', 'return "两端包裹着铁箍的硬木长棍。";'),
    ],
    "pixeldungeon/items/weapon/melee/ShortSword.java": [
        ('private static final String TXT_SELECT_WEAPON = "Select a weapon to upgrade";', 'private static final String TXT_SELECT_WEAPON = "选择要强化的武器";'),
        ('private static final String TXT_REFORGED = "you reforged the short sword to upgrade your %s";', 'private static final String TXT_REFORGED = "你重铸了短剑，使%s得到了强化";'),
        ('private static final String TXT_NOT_BOOMERANG = "you can\'t upgrade a boomerang this way";', 'private static final String TXT_NOT_BOOMERANG = "无法用这种方式强化回旋镖";'),
        ('this.name = "short sword";', 'this.name = "短剑";'),
        ('return "It is indeed quite short, just a few inches longer, than a dagger.";', 'return "它确实相当短小，只比匕首长出几寸。";'),
    ],
    "pixeldungeon/items/weapon/melee/Spear.java": [
        ('this.name = "spear";', 'this.name = "长矛";'),
        ('return "A slender wooden rod tipped with sharpened iron.";', 'return "一根装有锋利铁枪头的细长木杆。";'),
    ],
    "pixeldungeon/items/weapon/melee/Sword.java": [
        ('this.name = "sword";', 'this.name = "单手剑";'),
        ('return "The razor-sharp length of steel blade shines reassuringly.";', 'return "剃刀般锋利的钢制剑身闪烁着令人安心的寒光。";'),
    ],
    "pixeldungeon/items/weapon/melee/WarHammer.java": [
        ('this.name = "war hammer";', 'this.name = "战锤";'),
        ('return "Few creatures can withstand the crushing blow of this towering mass of lead and steel, but only the strongest of adventurers can use it effectively.";', 'return "很少有生物能扛住这柄由铅与精钢铸成的庞大重锤的粉碎一击，但也唯有最强壮的冒险者才能挥动它。";'),
    ],

    # Missile weapons
    "pixeldungeon/items/weapon/missiles/Boomerang.java": [
        ('this.name = "boomerang";', 'this.name = "回旋镖";'),
        ('return "Thrown to the enemy this flat curved wooden missile will return to the hands of its thrower.";', 'return "投向敌人后，这把扁平弯曲的木制飞镖会自动飞回投掷者手中。";'),
    ],
    "pixeldungeon/items/weapon/missiles/CurareDart.java": [
        ('this.name = "curare dart";', 'this.name = "麻痹吹箭";'),
        ('return "These little evil darts don\'t do much damage but they can paralyze the target leaving it helpless and motionless for some time.";', 'return "这种阴险的小飞镖伤害不高，但能麻痹目标，使其在一段时间内动弹不得、任人宰割。";'),
    ],
    "pixeldungeon/items/weapon/missiles/Dart.java": [
        ('this.name = "dart";', 'this.name = "飞镖";'),
        ('return "These simple metal spikes are weighted to fly true and sting their prey with a flick of the wrist.";', 'return "这些结构简单的金属飞镖配重均匀，只需轻轻一甩便能笔直飞出并刺中猎物。";'),
    ],
    "pixeldungeon/items/weapon/missiles/IncendiaryDart.java": [
        ('this.name = "incendiary dart";', 'this.name = "燃烧飞镖";'),
        ('return "The spike on each of these darts is designed to pin it to its target while the unstable compounds strapped to its length burst into brilliant flames.";', 'return "每支飞镖的尖刺都会牢牢钉入目标体内，而捆绑在镖身上的不稳定化合物则会瞬间爆发出炽烈的火焰。";'),
    ],
    "pixeldungeon/items/weapon/missiles/Javelin.java": [
        ('this.name = "javelin";', 'this.name = "标枪";'),
        ('return "This length of metal is weighted to keep the spike at its tip foremost as it sails through the air.";', 'return "这根金属投枪经过配重设计，飞向空中时能始终保持尖端朝前穿刺目标。";'),
    ],
    "pixeldungeon/items/weapon/missiles/MissileWeapon.java": [
        ('private static final String TXT_MISSILES = "Missile weapon";', 'private static final String TXT_MISSILES = "投掷武器";'),
        ('private static final String TXT_YES = "Yes, I know what I\'m doing";', 'private static final String TXT_YES = "是的，我知道我在做什么";'),
        ('private static final String TXT_NO = "No, I changed my mind";', 'private static final String TXT_NO = "不，我改主意了";'),
        ('private static final String TXT_R_U_SURE = "Do you really want to equip it as a melee weapon?";', 'private static final String TXT_R_U_SURE = "你真的要把它当作近战武器装备吗？";'),
        ('info.append("\\n\\nAverage damage of this weapon equals to " + (min + (max - min) / 2) + " points per hit. ");', 'info.append("\\n\\n这件武器的平均每次命中伤害为 " + (min + (max - min) / 2) + " 点。");'),
        ('info.append("Because of your inadequate strength the accuracy and speed of your attack with this " + this.name + " is decreased.");', 'info.append("由于力量不足，使用这件" + this.name + "时你的命中率与攻击速度都会下降。");'),
        ('info.append("Because of your excess strength the damage of your attack with this " + this.name + " is increased.");', 'info.append("由于力量充沛，使用这件" + this.name + "时你的攻击伤害得到了提升。");'),
        ('info.append("\\n\\nYou hold the " + this.name + " at the ready.");', 'info.append("\\n\\n你已将其装备在手中。");'),
    ],
    "pixeldungeon/items/weapon/missiles/Shuriken.java": [
        ('this.name = "shuriken";', 'this.name = "手里剑";'),
        ('return "Star-shaped pieces of metal with razor-sharp blades do significant damage when they hit a target. They can be thrown at very high rate.";', 'return "星形的锐利金属飞镖，命中目标能造成可观的伤害，而且投掷速度极快。";'),
    ],
    "pixeldungeon/items/weapon/missiles/Tamahawk.java": [
        ('this.name = "tomahawk";', 'this.name = "飞斧";'),
        ('return "This throwing axe is not that heavy, but it still requires significant strength to be used effectively.";', 'return "这把投掷用的手斧并不算特别重，但仍需要相当的力量才能有效挥投。";'),
    ],
}

if __name__ == "__main__":
    apply_file_patches(PATCHES_PART3)
