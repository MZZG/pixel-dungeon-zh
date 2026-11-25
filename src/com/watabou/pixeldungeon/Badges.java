/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon;

import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.Rankings;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.actors.mobs.Acidic;
import com.watabou.pixeldungeon.actors.mobs.Albino;
import com.watabou.pixeldungeon.actors.mobs.Bandit;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.actors.mobs.Senior;
import com.watabou.pixeldungeon.actors.mobs.Shielded;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.bags.ScrollHolder;
import com.watabou.pixeldungeon.items.bags.SeedPouch;
import com.watabou.pixeldungeon.items.bags.WandHolster;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.items.rings.Ring;
import com.watabou.pixeldungeon.items.rings.RingOfHaggler;
import com.watabou.pixeldungeon.items.rings.RingOfThorns;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PDPlatformSupport;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Badges {
    private static final Badge[] MONSTERS_SLAIN = new Badge[]{Badge.MONSTERS_SLAIN_1, Badge.MONSTERS_SLAIN_2, Badge.MONSTERS_SLAIN_3, Badge.MONSTERS_SLAIN_4};
    private static final Badge[] GOLD_COLLECTED = new Badge[]{Badge.GOLD_COLLECTED_1, Badge.GOLD_COLLECTED_2, Badge.GOLD_COLLECTED_3, Badge.GOLD_COLLECTED_4};
    private static final Badge[] LEVEL_REACHED = new Badge[]{Badge.LEVEL_REACHED_1, Badge.LEVEL_REACHED_2, Badge.LEVEL_REACHED_3, Badge.LEVEL_REACHED_4};
    private static final Badge[] STRENGTH_ATTAINED = new Badge[]{Badge.STRENGTH_ATTAINED_1, Badge.STRENGTH_ATTAINED_2, Badge.STRENGTH_ATTAINED_3, Badge.STRENGTH_ATTAINED_4};
    private static final Badge[] FOOD_EATEN = new Badge[]{Badge.FOOD_EATEN_1, Badge.FOOD_EATEN_2, Badge.FOOD_EATEN_3, Badge.FOOD_EATEN_4};
    private static final Badge[] POTIONS_COOKED = new Badge[]{Badge.POTIONS_COOKED_1, Badge.POTIONS_COOKED_2, Badge.POTIONS_COOKED_3, Badge.POTIONS_COOKED_4};
    private static final Badge[] PIRANHAS_KILLED = new Badge[]{Badge.PIRANHAS};
    private static final Badge[] ITEM_LEVEL = new Badge[]{Badge.ITEM_LEVEL_1, Badge.ITEM_LEVEL_2, Badge.ITEM_LEVEL_3, Badge.ITEM_LEVEL_4};
    private static final Badge[] COMBOS = new Badge[]{Badge.MASTERY_COMBO};
    private static final Badge[] NIGHT_HUNTERS = new Badge[]{Badge.NIGHT_HUNTER};
    private static final Badge[] GAMES_PLAYED = new Badge[]{Badge.GAMES_PLAYED_1, Badge.GAMES_PLAYED_2, Badge.GAMES_PLAYED_3, Badge.GAMES_PLAYED_4};
    private static final Badge[] BOSS_SLAIN = new Badge[]{Badge.BOSS_SLAIN_1, Badge.BOSS_SLAIN_2, Badge.BOSS_SLAIN_3, Badge.BOSS_SLAIN_4};
    private static final Badge[] ALL_BAGS = new Badge[]{Badge.BAG_BOUGHT_SEED_POUCH, Badge.BAG_BOUGHT_SCROLL_HOLDER, Badge.BAG_BOUGHT_WAND_HOLSTER};
    private static final Map<Class<?>, Badge> MAP_BAGS = new HashMap<Class<?>, Badge>(){
        {
            this.put(SeedPouch.class, Badge.BAG_BOUGHT_SEED_POUCH);
            this.put(ScrollHolder.class, Badge.BAG_BOUGHT_SCROLL_HOLDER);
            this.put(WandHolster.class, Badge.BAG_BOUGHT_WAND_HOLSTER);
        }
    };
    private static final Badge[] ALL_YASD = new Badge[]{Badge.DEATH_FROM_FIRE, Badge.DEATH_FROM_GAS, Badge.DEATH_FROM_HUNGER, Badge.DEATH_FROM_POISON};
    private static final Badge[] ALL_IDENTIFIED = new Badge[]{Badge.ALL_POTIONS_IDENTIFIED, Badge.ALL_SCROLLS_IDENTIFIED, Badge.ALL_RINGS_IDENTIFIED, Badge.ALL_WANDS_IDENTIFIED};
    private static final Badge[] ALL_RARE = new Badge[]{Badge.RARE_ALBINO, Badge.RARE_BANDIT, Badge.RARE_SHIELDED, Badge.RARE_SENIOR, Badge.RARE_ACIDIC};
    private static final Map<Class<?>, Badge> MAP_RARE = new HashMap<Class<?>, Badge>(){
        {
            this.put(Albino.class, Badge.RARE_ALBINO);
            this.put(Bandit.class, Badge.RARE_BANDIT);
            this.put(Shielded.class, Badge.RARE_SHIELDED);
            this.put(Senior.class, Badge.RARE_SENIOR);
            this.put(Acidic.class, Badge.RARE_ACIDIC);
        }
    };
    private static final Badge[] ALL_VICTORY = new Badge[]{Badge.VICTORY_WARRIOR, Badge.VICTORY_MAGE, Badge.VICTORY_ROGUE, Badge.VICTORY_HUNTRESS};
    private static final Map<HeroClass, Badge> MAP_VICTORY = new HashMap<HeroClass, Badge>(){
        {
            this.put(HeroClass.WARRIOR, Badge.VICTORY_WARRIOR);
            this.put(HeroClass.MAGE, Badge.VICTORY_MAGE);
            this.put(HeroClass.ROGUE, Badge.VICTORY_ROGUE);
            this.put(HeroClass.HUNTRESS, Badge.VICTORY_HUNTRESS);
        }
    };
    private static final Map<HeroClass, Badge> MAP_MASTERY = new HashMap<HeroClass, Badge>(){
        {
            this.put(HeroClass.WARRIOR, Badge.MASTERY_WARRIOR);
            this.put(HeroClass.MAGE, Badge.MASTERY_MAGE);
            this.put(HeroClass.ROGUE, Badge.MASTERY_ROGUE);
            this.put(HeroClass.HUNTRESS, Badge.MASTERY_HUNTRESS);
        }
    };
    private static final Badge[] ALL_BOSS_SLAIN_1 = new Badge[]{Badge.BOSS_SLAIN_1_WARRIOR, Badge.BOSS_SLAIN_1_MAGE, Badge.BOSS_SLAIN_1_ROGUE, Badge.BOSS_SLAIN_1_HUNTRESS};
    private static final Map<HeroClass, Badge> MAP_BOSS_SLAIN_1 = new HashMap<HeroClass, Badge>(){
        {
            this.put(HeroClass.WARRIOR, Badge.BOSS_SLAIN_1_WARRIOR);
            this.put(HeroClass.MAGE, Badge.BOSS_SLAIN_1_MAGE);
            this.put(HeroClass.ROGUE, Badge.BOSS_SLAIN_1_ROGUE);
            this.put(HeroClass.HUNTRESS, Badge.BOSS_SLAIN_1_HUNTRESS);
        }
    };
    private static final Badge[] ALL_BOSS_SLAIN_3 = new Badge[]{Badge.BOSS_SLAIN_3_GLADIATOR, Badge.BOSS_SLAIN_3_BERSERKER, Badge.BOSS_SLAIN_3_WARLOCK, Badge.BOSS_SLAIN_3_BATTLEMAGE, Badge.BOSS_SLAIN_3_FREERUNNER, Badge.BOSS_SLAIN_3_ASSASSIN, Badge.BOSS_SLAIN_3_SNIPER, Badge.BOSS_SLAIN_3_WARDEN};
    private static final Map<HeroSubClass, Badge> MAP_BOSS_SLAIN_3 = new HashMap<HeroSubClass, Badge>(){
        {
            this.put(HeroSubClass.GLADIATOR, Badge.BOSS_SLAIN_3_GLADIATOR);
            this.put(HeroSubClass.BERSERKER, Badge.BOSS_SLAIN_3_BERSERKER);
            this.put(HeroSubClass.WARLOCK, Badge.BOSS_SLAIN_3_WARLOCK);
            this.put(HeroSubClass.BATTLEMAGE, Badge.BOSS_SLAIN_3_BATTLEMAGE);
            this.put(HeroSubClass.FREERUNNER, Badge.BOSS_SLAIN_3_FREERUNNER);
            this.put(HeroSubClass.ASSASSIN, Badge.BOSS_SLAIN_3_ASSASSIN);
            this.put(HeroSubClass.SNIPER, Badge.BOSS_SLAIN_3_SNIPER);
            this.put(HeroSubClass.WARDEN, Badge.BOSS_SLAIN_3_WARDEN);
        }
    };
    private static HashSet<Badge> global;
    private static HashSet<Badge> local;
    private static boolean saveNeeded;
    public static Callback loadingListener;
    private static final String BADGES_FILE = "badges.dat";
    private static final String BADGES = "badges";

    public static void reset() {
        local.clear();
        Badges.loadGlobal();
    }

    public static HashSet<Badge> restore(Bundle bundle) {
        String[] names;
        HashSet<Badge> badges = new HashSet<Badge>();
        for (String name : names = bundle.getStringArray(BADGES)) {
            try {
                badges.add(Badge.valueOf(name));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return badges;
    }

    private static void store(Bundle bundle, HashSet<Badge> badges) {
        int count = 0;
        String[] names = new String[badges.size()];
        for (Badge badge : badges) {
            names[count++] = badge.toString();
        }
        bundle.put(BADGES, names);
    }

    public static void loadLocal(Bundle bundle) {
        local = Badges.restore(bundle);
    }

    public static void saveLocal(Bundle bundle) {
        Badges.store(bundle, local);
    }

    public static void loadGlobal() {
        if (global == null) {
            try {
                InputStream input = Game.instance.openFileInput(BADGES_FILE);
                Bundle bundle = Bundle.read(input);
                input.close();
                global = Badges.restore(bundle);
            }
            catch (IOException e) {
                global = new HashSet();
            }
        }
    }

    public static void saveGlobal() {
        if (saveNeeded) {
            PixelDungeon.instance.getPlatformSupport().storeStats();
            Bundle bundle = new Bundle();
            Badges.store(bundle, global);
            try {
                OutputStream output = Game.instance.openFileOutput(BADGES_FILE);
                Bundle.write(bundle, output);
                output.close();
                saveNeeded = false;
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    private static boolean validateAndDisplayLocal(Badge badge) {
        return Badges.validateAndDisplayLocal(true, badge);
    }

    private static boolean validateAndDisplayLocal(boolean statValue, Badge badge) {
        if (Dungeon.legacy) {
            return false;
        }
        if (!local.contains((Object)badge) && statValue) {
            local.add(badge);
            Badges.displayBadge(badge);
            return true;
        }
        return false;
    }

    private static void validateAndDisplayLocal(Statistics.Type stat, Badge[] badges) {
        Badges.validateAndDisplayLocal(Statistics.getInt(stat), badges);
    }

    private static boolean validateAndDisplayLocal(int statValue, Badge[] badges) {
        if (Dungeon.legacy) {
            return false;
        }
        Badge lastBadge = null;
        for (Badge badge : badges) {
            if (local.contains((Object)badge) || statValue < badge.statValue) continue;
            lastBadge = badge;
            local.add(badge);
        }
        if (lastBadge != null) {
            Badges.displayBadge(lastBadge);
            return true;
        }
        return false;
    }

    private static boolean validateAndDisplayGlobal(Badge badge) {
        if (Dungeon.legacy) {
            return false;
        }
        Badges.displayBadge(badge);
        return true;
    }

    private static void validateAndDisplayGlobal(int statValue, Badge[] badges) {
        if (Dungeon.legacy) {
            return;
        }
        Badge lastBadge = null;
        for (Badge badge : badges) {
            if (statValue < badge.statValue) continue;
            lastBadge = badge;
        }
        Badges.displayBadge(lastBadge);
    }

    private static boolean validateAndDisplayAllGlobal(Badge award, Badge[] badges, boolean addToLocal) {
        if (Dungeon.legacy) {
            return false;
        }
        if (global.contains((Object)award)) {
            return false;
        }
        for (Badge badge : badges) {
            if (global.contains((Object)badge)) continue;
            return false;
        }
        if (addToLocal) {
            local.add(award);
        }
        Badges.displayBadge(award);
        return true;
    }

    private static <T> boolean validateLocal(T keyType, Map<T, Badge> badges) {
        if (Dungeon.legacy) {
            return false;
        }
        for (Map.Entry<T, Badge> entry : badges.entrySet()) {
            Badge badge;
            if (entry.getKey() != keyType || local.contains((Object)(badge = entry.getValue()))) continue;
            local.add(badge);
            return true;
        }
        return false;
    }

    private static <T> boolean validateGlobal(T keyType, Map<T, Badge> badges, boolean addToLocal) {
        if (Dungeon.legacy) {
            return false;
        }
        for (Map.Entry<T, Badge> entry : badges.entrySet()) {
            if (entry.getKey() != keyType) continue;
            Badge badge = entry.getValue();
            if (addToLocal) {
                local.add(badge);
            }
            if (global.contains((Object)badge)) continue;
            global.add(badge);
            saveNeeded = true;
            return true;
        }
        return false;
    }

    public static void validateMonstersSlain() {
        Badges.validateAndDisplayLocal(Statistics.Type.SLAIN, MONSTERS_SLAIN);
    }

    public static void validateGoldCollected() {
        Badges.validateAndDisplayLocal(Statistics.Type.GOLD, GOLD_COLLECTED);
    }

    public static void validateLevelReached() {
        Badges.validateAndDisplayLocal(Dungeon.hero.lvl, LEVEL_REACHED);
    }

    public static void validateStrengthAttained() {
        Badges.validateAndDisplayLocal(Dungeon.hero.STR, STRENGTH_ATTAINED);
    }

    public static void validateFoodEaten() {
        Badges.validateAndDisplayLocal(Statistics.Type.FOOD, FOOD_EATEN);
    }

    public static void validatePotionsCooked() {
        Badges.validateAndDisplayLocal(Statistics.Type.ALCHEMY, POTIONS_COOKED);
    }

    public static void validatePiranhasKilled() {
        Badges.validateAndDisplayLocal(Statistics.Type.PIRANHAS, PIRANHAS_KILLED);
    }

    public static void validateItemLevelAquired(Item item) {
        if (!item.levelKnown) {
            return;
        }
        Badges.validateAndDisplayLocal(item.level(), ITEM_LEVEL);
    }

    private static void validateAllIdentified(Badge badge, boolean isAllKnown) {
        if (Dungeon.legacy) {
            return;
        }
        if (Dungeon.hero != null && Dungeon.hero.isAlive() && !local.contains((Object)badge) && isAllKnown) {
            local.add(badge);
            Badges.displayBadge(badge);
            Badges.validateAllItemsIdentified();
        }
    }

    public static void validateAllPotionsIdentified() {
        Badges.validateAllIdentified(Badge.ALL_POTIONS_IDENTIFIED, Potion.allKnown());
    }

    public static void validateAllScrollsIdentified() {
        Badges.validateAllIdentified(Badge.ALL_SCROLLS_IDENTIFIED, Scroll.allKnown());
    }

    public static void validateAllRingsIdentified() {
        Badges.validateAllIdentified(Badge.ALL_RINGS_IDENTIFIED, Ring.allKnown());
    }

    public static void validateAllWandsIdentified() {
        Badges.validateAllIdentified(Badge.ALL_WANDS_IDENTIFIED, Wand.allKnown());
    }

    public static void validateAllBagsBought(Item bag) {
        if (Badges.validateLocal(bag.getClass(), MAP_BAGS)) {
            Badges.validateAndDisplayAllGlobal(Badge.ALL_BAGS_BOUGHT, ALL_BAGS, true);
        }
    }

    public static void validateAllItemsIdentified() {
        Badges.validateAndDisplayAllGlobal(Badge.ALL_ITEMS_IDENTIFIED, ALL_IDENTIFIED, false);
    }

    public static void validateDeathFromFire() {
        if (Badges.validateAndDisplayLocal(Badge.DEATH_FROM_FIRE)) {
            Badges.validateYASD();
        }
    }

    public static void validateDeathFromPoison() {
        if (Badges.validateAndDisplayLocal(Badge.DEATH_FROM_POISON)) {
            Badges.validateYASD();
        }
    }

    public static void validateDeathFromGas() {
        if (Badges.validateAndDisplayLocal(Badge.DEATH_FROM_GAS)) {
            Badges.validateYASD();
        }
    }

    public static void validateDeathFromHunger() {
        if (Badges.validateAndDisplayLocal(Badge.DEATH_FROM_HUNGER)) {
            Badges.validateYASD();
        }
    }

    public static void validateDeathFromGlyph() {
        Badges.validateAndDisplayLocal(Badge.DEATH_FROM_GLYPH);
    }

    public static void validateDeathFromFalling() {
        Badges.validateAndDisplayLocal(Badge.DEATH_FROM_FALLING);
    }

    private static void validateYASD() {
        Badges.validateAndDisplayAllGlobal(Badge.YASD, ALL_YASD, true);
    }

    public static void validateBossSlain() {
        if (Badges.validateAndDisplayLocal(Dungeon.depth, BOSS_SLAIN)) {
            switch (Dungeon.depth) {
                case 5: {
                    if (!Badges.validateGlobal(Dungeon.hero.heroClass, MAP_BOSS_SLAIN_1, true)) break;
                    Badges.validateAndDisplayAllGlobal(Badge.BOSS_SLAIN_1_ALL_CLASSES, ALL_BOSS_SLAIN_1, false);
                    break;
                }
                case 15: {
                    if (!Badges.validateGlobal(Dungeon.hero.subClass, MAP_BOSS_SLAIN_3, true)) break;
                    Badges.validateAndDisplayAllGlobal(Badge.BOSS_SLAIN_3_ALL_SUBCLASSES, ALL_BOSS_SLAIN_3, false);
                }
            }
        }
    }

    public static void validateMastery() {
        Badges.validateGlobal(Dungeon.hero.heroClass, MAP_MASTERY, false);
    }

    public static void validateMasteryCombo(int n) {
        Badges.validateAndDisplayLocal(n, COMBOS);
    }

    public static void validateRingOfHaggler() {
        Badges.validateAndDisplayLocal(new RingOfHaggler().isKnown(), Badge.RING_OF_HAGGLER);
    }

    public static void validateRingOfThorns() {
        Badges.validateAndDisplayLocal(new RingOfThorns().isKnown(), Badge.RING_OF_THORNS);
    }

    public static void validateRare(Mob mob) {
        if (Badges.validateGlobal(mob.getClass(), MAP_RARE, false)) {
            Badges.validateAndDisplayAllGlobal(Badge.RARE, ALL_RARE, false);
        }
    }

    public static void validateVictory() {
        if (Badges.validateAndDisplayGlobal(Badge.VICTORY)) {
            Badges.validateGlobal(Dungeon.hero.heroClass, MAP_VICTORY, true);
            Badges.validateAndDisplayAllGlobal(Badge.VICTORY_ALL_CLASSES, ALL_VICTORY, false);
        }
    }

    public static void validateNoKilling() {
        Badges.validateAndDisplayLocal(Statistics.completedWithNoKilling, Badge.NO_MONSTERS_SLAIN);
    }

    public static void validateGrimWeapon() {
        Badges.validateAndDisplayLocal(Badge.GRIM_WEAPON);
    }

    public static void validateNightHunter() {
        Badges.validateAndDisplayLocal(Statistics.Type.NIGHT, NIGHT_HUNTERS);
    }

    public static void validateGamesPlayed() {
        Badges.validateAndDisplayGlobal(Rankings.INSTANCE.totalNumber, GAMES_PLAYED);
    }

    public static void validateHappyEnd() {
        Badges.validateAndDisplayGlobal(Badge.HAPPY_END);
    }

    public static void validateChampion() {
        Badges.validateAndDisplayGlobal(Badge.CHAMPION);
    }

    private static void displayBadge(Badge badge) {
        if (badge == null) {
            return;
        }
        if (global.contains((Object)badge)) {
            if (!badge.meta) {
                GLog.h("徽章提升：%s", badge.description);
            }
        } else {
            global.add(badge);
            saveNeeded = true;
            PDPlatformSupport platformSupport = PixelDungeon.instance.getPlatformSupport();
            platformSupport.unlockAchivement(badge.name());
            platformSupport.storeStats();
            if (badge.meta) {
                GLog.h("获得高级徽章：%s", badge.description);
            } else {
                GLog.h("获得新徽章：%s", badge.description);
            }
            PixelScene.showBadge(badge);
        }
    }

    public static boolean isUnlocked(Badge badge) {
        return global.contains((Object)badge);
    }

    public static void disown(Badge badge) {
        Badges.loadGlobal();
        global.remove((Object)badge);
        saveNeeded = true;
    }

    public static List<Badge> filtered(boolean global) {
        return global ? Badges.filtered(Badges.global, true) : Badges.filtered(local, false);
    }

    public static List<Badge> filtered(HashSet<Badge> set, boolean incMeta) {
        HashSet<Badge> filtered = new HashSet<Badge>(set);
        Iterator<Badge> iterator = filtered.iterator();
        while (iterator.hasNext()) {
            Badge badge = iterator.next();
            if ((incMeta || !badge.meta) && badge.image != -1) continue;
            iterator.remove();
        }
        Badges.leaveBest(filtered, MONSTERS_SLAIN);
        Badges.leaveBest(filtered, GOLD_COLLECTED);
        Badges.leaveBest(filtered, BOSS_SLAIN);
        Badges.leaveBest(filtered, LEVEL_REACHED);
        Badges.leaveBest(filtered, STRENGTH_ATTAINED);
        Badges.leaveBest(filtered, FOOD_EATEN);
        Badges.leaveBest(filtered, ITEM_LEVEL);
        Badges.leaveBest(filtered, POTIONS_COOKED);
        Badges.leaveBest(filtered, Badge.BOSS_SLAIN_1_ALL_CLASSES, Badge.BOSS_SLAIN_3_ALL_SUBCLASSES);
        for (Badge badge : ALL_YASD) {
            Badges.leaveBest(filtered, badge, Badge.YASD);
        }
        for (Badge badge : ALL_IDENTIFIED) {
            Badges.leaveBest(filtered, badge, Badge.ALL_ITEMS_IDENTIFIED);
        }
        Badges.leaveBest(filtered, Badge.VICTORY, Badge.VICTORY_ALL_CLASSES);
        Badges.leaveBest(filtered, Badge.VICTORY, Badge.HAPPY_END);
        Badges.leaveBest(filtered, Badge.VICTORY, Badge.CHAMPION);
        Badges.leaveBest(filtered, GAMES_PLAYED);
        ArrayList<Badge> list = new ArrayList<Badge>(filtered);
        Collections.sort(list);
        return list;
    }

    private static void leaveBest(HashSet<Badge> list, Badge ... badges) {
        for (int i = badges.length - 1; i > 0; --i) {
            if (!list.contains((Object)badges[i])) continue;
            for (int j = 0; j < i; ++j) {
                list.remove((Object)badges[j]);
            }
            break;
        }
    }

    static {
        local = new HashSet();
        saveNeeded = false;
        loadingListener = null;
    }

    public static enum Badge {
        MONSTERS_SLAIN_1("初露锋芒", "击杀 10 名敌人", 0, 10),
        MONSTERS_SLAIN_2("酣战淋漓", "击杀 50 名敌人", 1, 50),
        MONSTERS_SLAIN_3("杀戮地带", "击杀 150 名敌人", 2, 150),
        MONSTERS_SLAIN_4("横扫千军", "击杀 250 名敌人", 3, 250),
        GOLD_COLLECTED_1("零钱收集者", "收集 100 金币", 4, 100),
        GOLD_COLLECTED_2("淘金热", "收集 500 金币", 5, 500),
        GOLD_COLLECTED_3("满载而归", "收集 2500 金币", 6, 2500),
        GOLD_COLLECTED_4("腰缠万贯", "收集 7500 金币", 7, 7500),
        LEVEL_REACHED_1("成就", "达到 6 级", 8, 6),
        LEVEL_REACHED_2("成就", "达到 12 级", 9, 12),
        LEVEL_REACHED_3("成就", "达到 18 级", 10, 18),
        LEVEL_REACHED_4("成就", "达到 24 级", 11, 24),
        ALL_POTIONS_IDENTIFIED("药剂鉴赏家", "鉴定所有药剂", 16),
        ALL_SCROLLS_IDENTIFIED("卷轴学者", "鉴定所有卷轴", 17),
        ALL_RINGS_IDENTIFIED("戒指收藏家", "鉴定所有戒指", 18),
        ALL_WANDS_IDENTIFIED("法杖大师", "鉴定所有法杖", 19),
        ALL_ITEMS_IDENTIFIED("全知全识", "鉴定所有药剂、卷轴、戒指与法杖", 35, true),
        BAG_BOUGHT_SEED_POUCH,
        BAG_BOUGHT_SCROLL_HOLDER,
        BAG_BOUGHT_WAND_HOLSTER,
        ALL_BAGS_BOUGHT("购物狂", "购买所有容器袋", 23),
        DEATH_FROM_FIRE("烈焰焚身", "死于火焰", 24),
        DEATH_FROM_POISON("剧毒攻心", "死于中毒", 25),
        DEATH_FROM_GAS("毒气窒息", "死于毒气", 26),
        DEATH_FROM_HUNGER("饥肠辘辘", "死于饥饿", 27),
        DEATH_FROM_GLYPH("附魔反噬", "死于护甲刻印", 57),
        DEATH_FROM_FALLING("失足坠落", "死于坠落", 59),
        YASD("又一次愚蠢的死法", "分别死于火焰、中毒、毒气与饥饿", 34, true),
        BOSS_SLAIN_1_WARRIOR,
        BOSS_SLAIN_1_MAGE,
        BOSS_SLAIN_1_ROGUE,
        BOSS_SLAIN_1_HUNTRESS,
        BOSS_SLAIN_1("咕-咕！", "击败第 1 位头目", 12, 5),
        BOSS_SLAIN_2("抓到你了！", "击败第 2 位头目", 13, 10),
        BOSS_SLAIN_3("发现未授权人员", "击败第 3 位头目", 14, 15),
        BOSS_SLAIN_4("你好大的胆子！", "击败第 4 位头目", 15, 20),
        BOSS_SLAIN_1_ALL_CLASSES("万事通", "分别用战士、法师、盗贼和女猎手击败第 1 位头目", 32, true),
        BOSS_SLAIN_3_GLADIATOR,
        BOSS_SLAIN_3_BERSERKER,
        BOSS_SLAIN_3_WARLOCK,
        BOSS_SLAIN_3_BATTLEMAGE,
        BOSS_SLAIN_3_FREERUNNER,
        BOSS_SLAIN_3_ASSASSIN,
        BOSS_SLAIN_3_SNIPER,
        BOSS_SLAIN_3_WARDEN,
        BOSS_SLAIN_3_ALL_SUBCLASSES("全能大师", "用全部 8 个转职职业击败第 3 位头目", 33, true),
        RING_OF_HAGGLER("讨价还价", "获得商贾之戒", 20),
        RING_OF_THORNS("荆棘", "获得荆棘之戒", 21),
        STRENGTH_ATTAINED_1("成就", "力量达到 13 点", 40, 13),
        STRENGTH_ATTAINED_2("成就", "力量达到 15 点", 41, 15),
        STRENGTH_ATTAINED_3("成就", "力量达到 17 点", 42, 17),
        STRENGTH_ATTAINED_4("成就", "力量达到 19 点", 43, 19),
        FOOD_EATEN_1("成就", "食用 10 份食物", 44, 10),
        FOOD_EATEN_2("成就", "食用 20 份食物", 45, 20),
        FOOD_EATEN_3("成就", "食用 30 份食物", 46, 30),
        FOOD_EATEN_4("成就", "食用 40 份食物", 47, 40),
        MASTERY_WARRIOR,
        MASTERY_MAGE,
        MASTERY_ROGUE,
        MASTERY_HUNTRESS,
        ITEM_LEVEL_1("稀有", "获得强化 +3 物品", 48, 3),
        ITEM_LEVEL_2("珍稀", "获得强化 +6 物品", 49, 6),
        ITEM_LEVEL_3("史诗", "获得强化 +9 物品", 50, 9),
        ITEM_LEVEL_4("传说", "获得强化 +12 物品", 51, 12),
        RARE_ALBINO,
        RARE_BANDIT,
        RARE_SHIELDED,
        RARE_SENIOR,
        RARE_ACIDIC,
        RARE("珍奇狩猎", "击杀所有稀有怪物", 37, true),
        VICTORY_WARRIOR,
        VICTORY_MAGE,
        VICTORY_ROGUE,
        VICTORY_HUNTRESS,
        VICTORY("罗德尼", "获得Yendor护符", 22),
        VICTORY_ALL_CLASSES("全职业制霸", "分别用战士、法师、盗贼和女猎手获得Yendor护符", 36, true),
        MASTERY_COMBO("剑术大师", "打出 7 连击", 31, 7),
        POTIONS_COOKED_1("炼金学徒", "炼制 3 瓶药剂", 52, 3),
        POTIONS_COOKED_2("炼金熟练工", "炼制 6 瓶药剂", 53, 6),
        POTIONS_COOKED_3("炼金专家", "炼制 9 瓶药剂", 54, 9),
        POTIONS_COOKED_4("炼金宗师", "炼制 12 瓶药剂", 55, 12),
        NO_MONSTERS_SLAIN("和平主义者", "未击杀任何怪物通过一层", 28),
        GRIM_WEAPON("死神来了", "用死神附魔武器击杀怪物", 29),
        PIRANHAS("最后一口！", "击杀 6 条食人鱼", 30, 6),
        NIGHT_HUNTER("狼人", "在夜间击杀 15 只怪物", 58, 15),
        GAMES_PLAYED_1("成就", "游玩 10 局游戏", 60, true, 10),
        GAMES_PLAYED_2("成就", "游玩 100 局游戏", 61, true, 100),
        GAMES_PLAYED_3("成就", "游玩 500 局游戏", 62, true, 500),
        GAMES_PLAYED_4("成就", "游玩 2000 局游戏", 63, true, 2000),
        HAPPY_END("圆满结局", "携护符离开像素地牢", 38),
        CHAMPION("勇士", "开启挑战模式通关", 39, true);

        public static final int LOCKED = 56;
        public final boolean meta;
        public final String name;
        public final String description;
        public final int image;
        public final int statValue;

        private Badge(String name, String description, int image, int statValue) {
            this(name, description, image, false, statValue);
        }

        private Badge(String name, String description, int image) {
            this(name, description, image, false, 0);
        }

        private Badge(String name, String description, int image, boolean meta) {
            this(name, description, image, meta, 0);
        }

        private Badge(String name, String description, int image, boolean meta, int statValue) {
            this.name = name;
            this.description = description;
            this.image = image;
            this.meta = meta;
            this.statValue = statValue;
        }

        private Badge() {
            this("成就", "", -1);
        }
    }
}

