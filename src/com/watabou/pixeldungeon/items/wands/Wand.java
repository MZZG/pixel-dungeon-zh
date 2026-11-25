/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.wands;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.effects.MagicMissile;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.ItemStatusHandler;
import com.watabou.pixeldungeon.items.KindOfWeapon;
import com.watabou.pixeldungeon.items.bags.Bag;
import com.watabou.pixeldungeon.items.rings.RingOfPower;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.watabou.pixeldungeon.items.wands.WandOfAmok;
import com.watabou.pixeldungeon.items.wands.WandOfAvalanche;
import com.watabou.pixeldungeon.items.wands.WandOfBlink;
import com.watabou.pixeldungeon.items.wands.WandOfDisintegration;
import com.watabou.pixeldungeon.items.wands.WandOfFirebolt;
import com.watabou.pixeldungeon.items.wands.WandOfFlock;
import com.watabou.pixeldungeon.items.wands.WandOfLightning;
import com.watabou.pixeldungeon.items.wands.WandOfPoison;
import com.watabou.pixeldungeon.items.wands.WandOfReach;
import com.watabou.pixeldungeon.items.wands.WandOfRegrowth;
import com.watabou.pixeldungeon.items.wands.WandOfSlowness;
import com.watabou.pixeldungeon.items.wands.WandOfTeleportation;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.scenes.CellSelector;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.ui.QuickSlot;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.windows.WndBag;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import java.util.ArrayList;

public abstract class Wand
extends KindOfWeapon {
    private static final int USAGES_TO_KNOW = 40;
    public static final String AC_ZAP = "ZAP";
    public static final String AC_MERGE = "MERGE";
    private static final String TXT_WOOD = "这根纤细的%s法杖摸起来温温热热的，谁知道挥动它会触发什么效果？";
    private static final String TXT_DAMAGE = "将这根法杖当作近战武器使用时，每次命中的平均伤害为 %d 点。";
    private static final String TXT_WEAPON = "你可以将这根法杖当作近战武器使用。";
    private static final String TXT_FIZZLES = "你的法杖哑火了，看来充能暂时用光了";
    private static final String TXT_SELF_TARGET = "你不能以自己为目标";
    private static final String TXT_IDENTIFY = "你对这根%s已经足够熟悉，完成了鉴定。";
    private static final String TXT_SELECT_WAND = "选择要合并的法杖";
    private static final String TXT_MERGED = "你将两根法杖合并成了一根%s";
    private static final float TIME_TO_ZAP = 1.0f;
    private static final float TIME_TO_MERGE = 2.0f;
    public int maxCharges;
    public int curCharges;
    protected Charger charger;
    private boolean curChargeKnown;
    private int usagesToKnow;
    protected boolean hitChars;
    protected boolean disenchantEquipped;
    private static final Class<?>[] wands = new Class[]{WandOfTeleportation.class, WandOfSlowness.class, WandOfFirebolt.class, WandOfPoison.class, WandOfRegrowth.class, WandOfBlink.class, WandOfLightning.class, WandOfAmok.class, WandOfReach.class, WandOfFlock.class, WandOfDisintegration.class, WandOfAvalanche.class};
    private static final String[] woods = new String[]{"冬青木", "紫杉木", "乌木", "樱桃木", "柚木", "花楸木", "柳木", "桃花心木", "竹制", "紫心木", "橡木", "桦木"};
    private static final Integer[] images = new Integer[]{48, 49, 50, 51, 52, 53, 54, 55, 68, 69, 70, 71};
    private static ItemStatusHandler<Wand> handler;
    private String wood;
    private static final String UNFAMILIRIARITY = "unfamiliarity";
    private static final String MAX_CHARGES = "maxCharges";
    private static final String CUR_CHARGES = "curCharges";
    private static final String CUR_CHARGE_KNOWN = "curChargeKnown";
    protected static CellSelector.Listener zapper;
    private final WndBag.Listener mergeItemSelector;

    public static void initWoods() {
        handler = new ItemStatusHandler(wands, woods, images);
    }

    public static void save(Bundle bundle) {
        handler.save(bundle);
    }

    public static void restore(Bundle bundle) {
        handler = new ItemStatusHandler(wands, woods, images, bundle);
    }

    public Wand() {
        this.curCharges = this.maxCharges = this.initialCharges();
        this.curChargeKnown = false;
        this.usagesToKnow = 40;
        this.hitChars = true;
        this.defaultAction = AC_ZAP;
        this.mergeItemSelector = new WndBag.Listener(){

            @Override
            public void onSelect(Item item) {
                if (item != null) {
                    Sample.INSTANCE.play("snd_evoke.mp3");
                    ScrollOfUpgrade.upgrade(curUser);
                    Item.evoke(curUser);
                    Wand wand = (Wand)item;
                    wand.level(Math.max(wand.level(), Wand.this.level()));
                    wand.levelKnown = wand.levelKnown && Wand.this.levelKnown;
                    wand.curChargeKnown = wand.curChargeKnown && Wand.this.curChargeKnown;
                    wand.upgrade();
                    GLog.w(Wand.TXT_MERGED, wand.toString());
                    curUser.spendAndNext(2.0f);
                    Badges.validateItemLevelAquired(item);
                } else if (Wand.this.disenchantEquipped) {
                    curUser.belongings.weapon = Wand.this;
                    Wand.this.updateQuickslot();
                } else {
                    Wand.this.collect(curUser.belongings.backpack);
                }
            }
        };
        try {
            this.image = handler.image(this);
            this.wood = handler.label(this);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (this.curCharges > 0 || !this.curChargeKnown) {
            actions.add(AC_ZAP);
        }
        if (hero.heroClass != HeroClass.MAGE) {
            actions.remove("EQUIP");
            actions.remove("UNEQUIP");
        }
        if (hero.belongings.getItems(this.getClass()).size() > 1) {
            actions.add(AC_MERGE);
        }
        return actions;
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        this.onDetach();
        return super.doUnequip(hero, collect, single);
    }

    @Override
    public void activate(Hero hero) {
        this.charge(hero);
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action.equals(AC_ZAP)) {
            curUser = hero;
            curItem = this;
            GameScene.selectCell(zapper);
        } else if (action.equals(AC_MERGE)) {
            this.doMerge(hero);
        } else {
            super.execute(hero, action);
        }
    }

    protected abstract void onZap(int var1);

    protected void doMerge(Hero hero) {
        if (hero.belongings.weapon == this) {
            this.disenchantEquipped = true;
            hero.belongings.weapon = null;
            this.updateQuickslot();
        } else {
            this.disenchantEquipped = false;
            this.detach(hero.belongings.backpack);
        }
        curUser = hero;
        WndBag.wandClass = this.getClass();
        GameScene.selectItem(this.mergeItemSelector, WndBag.Mode.WAND, TXT_SELECT_WAND);
    }

    @Override
    public boolean collect(Bag container) {
        if (super.collect(container)) {
            if (container.owner != null) {
                this.charge(container.owner);
            }
            return true;
        }
        return false;
    }

    public void charge(Char owner) {
        if (this.charger == null) {
            this.charger = new Charger();
            this.charger.attachTo(owner);
        }
    }

    @Override
    public void onDetach() {
        this.stopCharging();
    }

    public void stopCharging() {
        if (this.charger != null) {
            this.charger.detach();
            this.charger = null;
        }
    }

    public int power() {
        int eLevel = this.effectiveLevel();
        if (this.charger != null) {
            RingOfPower.Power power = this.charger.target.buff(RingOfPower.Power.class);
            return power == null ? eLevel : Math.max(eLevel + power.level, 0);
        }
        return eLevel;
    }

    protected boolean isKnown() {
        return handler.isKnown(this);
    }

    public void setKnown() {
        if (!this.isKnown()) {
            handler.know(this);
        }
        Badges.validateAllWandsIdentified();
    }

    @Override
    public Item identify() {
        this.setKnown();
        this.curChargeKnown = true;
        super.identify();
        this.updateQuickslot();
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        String status = this.status();
        if (status != null) {
            sb.append(" (" + status + ")");
        }
        if (this.isBroken()) {
            sb.insert(0, "损坏的");
        }
        return sb.toString();
    }

    @Override
    public String name() {
        return this.isKnown() ? this.name : this.wood + "法杖";
    }

    @Override
    public String info() {
        StringBuilder info = new StringBuilder(this.isKnown() ? this.desc() : String.format(TXT_WOOD, this.wood));
        if (Dungeon.hero.heroClass == HeroClass.MAGE) {
            info.append("\n\n");
            if (this.levelKnown) {
                int min = this.min();
                info.append(String.format(TXT_DAMAGE, min + (this.max() - min) / 2));
            } else {
                info.append(TXT_WEAPON);
            }
        }
        return info.toString();
    }

    @Override
    public boolean isIdentified() {
        return super.isIdentified() && this.isKnown() && this.curChargeKnown;
    }

    @Override
    public String status() {
        if (this.levelKnown) {
            return (this.curChargeKnown ? Integer.valueOf(this.curCharges) : "?") + "/" + this.maxCharges;
        }
        return null;
    }

    @Override
    public Item upgrade() {
        super.upgrade();
        this.updateLevel();
        this.curCharges = Math.min(this.curCharges + 1, this.maxCharges);
        this.updateQuickslot();
        return this;
    }

    @Override
    public Item degrade() {
        super.degrade();
        this.updateLevel();
        this.updateQuickslot();
        return this;
    }

    @Override
    public int maxDurability(int lvl) {
        return 6 * (lvl < 16 ? 16 - lvl : 1);
    }

    protected void updateLevel() {
        this.maxCharges = Math.min(this.initialCharges() + this.level(), 9);
        this.curCharges = Math.min(this.curCharges, this.maxCharges);
    }

    protected int initialCharges() {
        return 2;
    }

    @Override
    public int min() {
        int tier = 1 + this.effectiveLevel() / 3;
        return tier;
    }

    @Override
    public int max() {
        int level = this.effectiveLevel();
        int tier = 1 + level / 3;
        return (tier * tier - tier + 10) / 2 + level;
    }

    protected void fx(int cell, Callback callback) {
        MagicMissile.blueLight(Wand.curUser.sprite.parent, Wand.curUser.pos, cell, callback);
        Sample.INSTANCE.play("snd_zap.mp3");
    }

    protected void wandUsed() {
        --this.curCharges;
        if (!this.isIdentified() && --this.usagesToKnow <= 0) {
            this.identify();
            GLog.w(TXT_IDENTIFY, this.name());
        } else {
            this.updateQuickslot();
        }
        this.use();
        curUser.spendAndNext(1.0f);
    }

    @Override
    public Item random() {
        if (Random.Float() < 0.5f) {
            this.upgrade();
            if (Random.Float() < 0.15f) {
                this.upgrade();
            }
        }
        return this;
    }

    public static boolean allKnown() {
        return handler.known().size() == wands.length;
    }

    @Override
    public int price() {
        return this.considerState(50);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(UNFAMILIRIARITY, this.usagesToKnow);
        bundle.put(MAX_CHARGES, this.maxCharges);
        bundle.put(CUR_CHARGES, this.curCharges);
        bundle.put(CUR_CHARGE_KNOWN, this.curChargeKnown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.usagesToKnow = bundle.getInt(UNFAMILIRIARITY);
        if (this.usagesToKnow == 0) {
            this.usagesToKnow = 40;
        }
        this.maxCharges = bundle.getInt(MAX_CHARGES);
        this.curCharges = bundle.getInt(CUR_CHARGES);
        this.curChargeKnown = bundle.getBoolean(CUR_CHARGE_KNOWN);
    }

    static {
        zapper = new CellSelector.Listener(){

            @Override
            public void onSelect(Integer target) {
                if (target != null) {
                    if (target == curUser.pos) {
                        GLog.i(Wand.TXT_SELF_TARGET, new Object[0]);
                        return;
                    }
                    final Wand curWand = (Wand)curItem;
                    curWand.setKnown();
                    final int cell = Ballistica.cast(curUser.pos, target, true, curWand.hitChars);
                    curUser.sprite.zap(cell);
                    QuickSlot.target(curItem, Actor.findChar(cell));
                    if (curWand.curCharges > 0) {
                        curUser.busy();
                        curWand.fx(cell, new Callback(){

                            @Override
                            public void call() {
                                curWand.onZap(cell);
                                curWand.wandUsed();
                            }
                        });
                        Invisibility.dispel();
                    } else {
                        curUser.spendAndNext(1.0f);
                        GLog.w(Wand.TXT_FIZZLES, new Object[0]);
                        curWand.levelKnown = true;
                        curWand.updateQuickslot();
                    }
                }
            }

            @Override
            public String prompt() {
                return "选择释放方向";
            }

            @Override
            public String cursor() {
                return "cursor_wand.png";
            }
        };
    }

    protected class Charger
    extends Buff {
        private static final float TIME_TO_CHARGE = 40.0f;

        protected Charger() {
        }

        @Override
        public boolean attachTo(Char target) {
            super.attachTo(target);
            this.delay();
            return true;
        }

        @Override
        public boolean act() {
            if (Wand.this.curCharges < Wand.this.maxCharges) {
                ++Wand.this.curCharges;
                Wand.this.updateQuickslot();
            }
            this.delay();
            return true;
        }

        protected void delay() {
            float time2charge = ((Hero)this.target).heroClass == HeroClass.MAGE ? 40.0f / (float)Math.sqrt(1 + Wand.this.effectiveLevel()) : 40.0f;
            this.spend(time2charge);
        }
    }
}

