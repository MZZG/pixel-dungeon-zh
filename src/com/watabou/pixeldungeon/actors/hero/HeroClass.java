/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.hero;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.TomeOfMastery;
import com.watabou.pixeldungeon.items.armor.ClothArmor;
import com.watabou.pixeldungeon.items.bags.Keyring;
import com.watabou.pixeldungeon.items.food.Food;
import com.watabou.pixeldungeon.items.potions.PotionOfStrength;
import com.watabou.pixeldungeon.items.rings.RingOfShadows;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfIdentify;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.watabou.pixeldungeon.items.wands.WandOfMagicMissile;
import com.watabou.pixeldungeon.items.weapon.melee.Dagger;
import com.watabou.pixeldungeon.items.weapon.melee.Knuckles;
import com.watabou.pixeldungeon.items.weapon.melee.ShortSword;
import com.watabou.pixeldungeon.items.weapon.missiles.Boomerang;
import com.watabou.pixeldungeon.items.weapon.missiles.Dart;
import com.watabou.pixeldungeon.ui.QuickSlot;
import com.watabou.utils.Bundle;

public enum HeroClass {
    WARRIOR("战士"),
    MAGE("法师"),
    ROGUE("盗贼"),
    HUNTRESS("女猎手");

    private String title;
    public static final String[] WAR_PERKS;
    public static final String[] MAG_PERKS;
    public static final String[] ROG_PERKS;
    public static final String[] HUN_PERKS;
    private static final String CLASS = "class";

    private HeroClass(String title) {
        this.title = title;
    }

    public void initHero(Hero hero) {
        hero.heroClass = this;
        HeroClass.initCommon(hero);
        switch (this) {
            case WARRIOR: {
                HeroClass.initWarrior(hero);
                break;
            }
            case MAGE: {
                HeroClass.initMage(hero);
                break;
            }
            case ROGUE: {
                HeroClass.initRogue(hero);
                break;
            }
            case HUNTRESS: {
                HeroClass.initHuntress(hero);
            }
        }
        if (Badges.isUnlocked(this.masteryBadge())) {
            new TomeOfMastery().collect();
        }
        hero.updateAwareness();
    }

    private static void initCommon(Hero hero) {
        hero.belongings.armor = new ClothArmor();
        hero.belongings.armor.identify();
        new Food().identify().collect();
        new Keyring().collect();
    }

    public Badges.Badge masteryBadge() {
        switch (this) {
            case WARRIOR: {
                return Badges.Badge.MASTERY_WARRIOR;
            }
            case MAGE: {
                return Badges.Badge.MASTERY_MAGE;
            }
            case ROGUE: {
                return Badges.Badge.MASTERY_ROGUE;
            }
            case HUNTRESS: {
                return Badges.Badge.MASTERY_HUNTRESS;
            }
        }
        return null;
    }

    private static void initWarrior(Hero hero) {
        ++hero.STR;
        hero.belongings.weapon = new ShortSword();
        hero.belongings.weapon.identify();
        new Dart(8).identify().collect();
        QuickSlot.primaryValue = Dart.class;
        new PotionOfStrength().setKnown();
    }

    private static void initMage(Hero hero) {
        hero.belongings.weapon = new Knuckles();
        hero.belongings.weapon.identify();
        WandOfMagicMissile wand = new WandOfMagicMissile();
        wand.identify().collect();
        QuickSlot.primaryValue = wand;
        new ScrollOfIdentify().setKnown();
    }

    private static void initRogue(Hero hero) {
        hero.belongings.weapon = new Dagger();
        hero.belongings.weapon.identify();
        hero.belongings.ring1 = new RingOfShadows();
        hero.belongings.ring1.upgrade().identify();
        new Dart(8).identify().collect();
        hero.belongings.ring1.activate(hero);
        QuickSlot.primaryValue = Dart.class;
        new ScrollOfMagicMapping().setKnown();
    }

    private static void initHuntress(Hero hero) {
        hero.HP = hero.HT -= 5;
        hero.belongings.weapon = new Dagger();
        hero.belongings.weapon.identify();
        Boomerang boomerang = new Boomerang();
        boomerang.identify().collect();
        QuickSlot.primaryValue = boomerang;
    }

    public String title() {
        return this.title;
    }

    public String spritesheet() {
        switch (this) {
            case WARRIOR: {
                return "warrior.png";
            }
            case MAGE: {
                return "mage.png";
            }
            case ROGUE: {
                return "rogue.png";
            }
            case HUNTRESS: {
                return "ranger.png";
            }
        }
        return null;
    }

    public String[] perks() {
        switch (this) {
            case WARRIOR: {
                return WAR_PERKS;
            }
            case MAGE: {
                return MAG_PERKS;
            }
            case ROGUE: {
                return ROG_PERKS;
            }
            case HUNTRESS: {
                return HUN_PERKS;
            }
        }
        return null;
    }

    public void storeInBundle(Bundle bundle) {
        bundle.put(CLASS, this.toString());
    }

    public static HeroClass restoreInBundle(Bundle bundle) {
        String value = bundle.getString(CLASS);
        return value.length() > 0 ? HeroClass.valueOf(value) : ROGUE;
    }

    static {
        WAR_PERKS = new String[]{"战士初始拥有 11 点力量。", "战士开局持有一把独特的短剑，日后可用于\"重铸\"并强化另一把近战武器。", "战士不擅长使用投掷武器。", "食用任意食物都能恢复少量生命。", "力量药剂在开局时便已鉴定。"};
        MAG_PERKS = new String[]{"法师开局持有一根独特的魔弹法杖，日后可用于\"祛魅\"并强化另一根法杖。", "法师的法杖充能速度更快。", "食用任意食物时，会为背包中的所有法杖恢复 1 点充能。", "法师可以将法杖当作近战武器使用。", "鉴定卷轴在开局时便已鉴定。"};
        ROG_PERKS = new String[]{"盗贼开局携带一枚 +1 暗影之戒。", "盗贼装备戒指时能立刻识别其种类。", "盗贼擅长穿戴轻甲，穿着时闪避能力更高。", "盗贼更擅长察觉隐藏门与陷阱。", "盗贼更耐饥饿。", "探地卷轴在开局时便已鉴定。"};
        HUN_PERKS = new String[]{"女猎手初始拥有 15 点生命值。", "女猎手开局持有一把可强化的独特回旋镖。", "女猎手精通投掷武器，使用超出力量需求的投掷武器时能获得额外伤害加成。", "女猎手通过露珠可恢复更多生命。", "即使怪物藏在障碍物后，女猎手也能感知到邻近的敌人。"};
    }
}

