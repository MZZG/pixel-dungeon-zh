/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.rings;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.items.EquipableItem;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.ItemStatusHandler;
import com.watabou.pixeldungeon.items.rings.RingOfAccuracy;
import com.watabou.pixeldungeon.items.rings.RingOfDetection;
import com.watabou.pixeldungeon.items.rings.RingOfElements;
import com.watabou.pixeldungeon.items.rings.RingOfEvasion;
import com.watabou.pixeldungeon.items.rings.RingOfHaggler;
import com.watabou.pixeldungeon.items.rings.RingOfHaste;
import com.watabou.pixeldungeon.items.rings.RingOfHerbalism;
import com.watabou.pixeldungeon.items.rings.RingOfMending;
import com.watabou.pixeldungeon.items.rings.RingOfPower;
import com.watabou.pixeldungeon.items.rings.RingOfSatiety;
import com.watabou.pixeldungeon.items.rings.RingOfShadows;
import com.watabou.pixeldungeon.items.rings.RingOfThorns;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndOptions;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import java.util.ArrayList;

public class Ring
extends EquipableItem {
    private static final int TICKS_TO_KNOW = 200;
    private static final float TIME_TO_EQUIP = 1.0f;
    private static final String TXT_IDENTIFY = "你对这枚%s已经足够熟悉并完成了鉴定，它是%s。";
    private static final String TXT_UNEQUIP_TITLE = "取下一枚戒指";
    private static final String TXT_UNEQUIP_MESSAGE = "你同时只能佩戴两枚戒指，请先取下一枚已装备的戒指。";
    protected Buff buff;
    private static final Class<?>[] rings = new Class[]{RingOfMending.class, RingOfDetection.class, RingOfShadows.class, RingOfPower.class, RingOfHerbalism.class, RingOfAccuracy.class, RingOfEvasion.class, RingOfSatiety.class, RingOfHaste.class, RingOfHaggler.class, RingOfElements.class, RingOfThorns.class};
    private static final String[] gems = new String[]{"钻石", "猫眼石", "石榴石", "红宝石", "紫水晶", "黄玉", "缟玛瑙", "碧玺", "祖母绿", "蓝宝石", "石英", "玛瑙"};
    private static final Integer[] images = new Integer[]{32, 33, 34, 35, 36, 37, 38, 39, 72, 73, 74, 75};
    private static ItemStatusHandler<Ring> handler;
    private String gem;
    private int ticksToKnow = 200;
    private static final String UNFAMILIRIARITY = "unfamiliarity";

    public static void initGems() {
        handler = new ItemStatusHandler(rings, gems, images);
    }

    public static void save(Bundle bundle) {
        handler.save(bundle);
    }

    public static void restore(Bundle bundle) {
        handler = new ItemStatusHandler(rings, gems, images, bundle);
    }

    public Ring() {
        this.syncGem();
    }

    public void syncGem() {
        this.image = handler.image(this);
        this.gem = handler.label(this);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(this.isEquipped(hero) ? "UNEQUIP" : "EQUIP");
        return actions;
    }

    @Override
    public boolean doEquip(final Hero hero) {
        if (hero.belongings.ring1 != null && hero.belongings.ring2 != null) {
            final Ring r1 = hero.belongings.ring1;
            final Ring r2 = hero.belongings.ring2;
            PixelDungeon.scene().add(new WndOptions(TXT_UNEQUIP_TITLE, TXT_UNEQUIP_MESSAGE, new String[]{Utils.capitalize(r1.toString()), Utils.capitalize(r2.toString())}){

                @Override
                protected void onSelect(int index) {
                    Ring equipped;
                    Ring.this.detach(hero.belongings.backpack);
                    Ring ring = equipped = index == 0 ? r1 : r2;
                    if (equipped.doUnequip(hero, true, false)) {
                        Ring.this.doEquip(hero);
                    } else {
                        Ring.this.collect(hero.belongings.backpack);
                    }
                }
            });
            return false;
        }
        if (hero.belongings.ring1 == null) {
            hero.belongings.ring1 = this;
        } else {
            hero.belongings.ring2 = this;
        }
        this.detach(hero.belongings.backpack);
        this.activate(hero);
        this.cursedKnown = true;
        if (this.cursed) {
            Ring.equipCursed(hero);
            GLog.n("你的" + this + "痛苦地勒紧了你的手指", new Object[0]);
        }
        hero.spendAndNext(1.0f);
        return true;
    }

    public void activate(Char ch) {
        this.buff = this.buff();
        this.buff.attachTo(ch);
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        if (super.doUnequip(hero, collect, single)) {
            if (hero.belongings.ring1 == this) {
                hero.belongings.ring1 = null;
            } else {
                hero.belongings.ring2 = null;
            }
            hero.remove(this.buff);
            this.buff = null;
            return true;
        }
        return false;
    }

    @Override
    public boolean isEquipped(Hero hero) {
        return hero.belongings.ring1 == this || hero.belongings.ring2 == this;
    }

    @Override
    public int effectiveLevel() {
        return this.isBroken() ? 1 : this.level();
    }

    private void renewBuff() {
        if (this.buff != null) {
            Char owner = this.buff.target;
            this.buff.detach();
            this.buff = this.buff();
            if (this.buff != null) {
                this.buff.attachTo(owner);
            }
        }
    }

    @Override
    public void getBroken() {
        this.renewBuff();
        super.getBroken();
    }

    @Override
    public void fix() {
        super.fix();
        this.renewBuff();
    }

    @Override
    public int maxDurability(int lvl) {
        if (lvl <= 1) {
            return Integer.MAX_VALUE;
        }
        return 100 * (lvl < 16 ? 16 - lvl : 1);
    }

    public boolean isKnown() {
        return handler.isKnown(this);
    }

    protected void setKnown() {
        if (!this.isKnown()) {
            handler.know(this);
        }
        Badges.validateAllRingsIdentified();
    }

    @Override
    public String toString() {
        return this.levelKnown && this.isBroken() ? "损坏的" + super.toString() : super.toString();
    }

    @Override
    public String name() {
        return this.isKnown() ? this.name : this.gem + "戒指";
    }

    @Override
    public String desc() {
        return "这枚金属指环上镶嵌着一颗巨大的" + this.gem + "，在黑暗中闪烁着微光。谁知道戴上它会有什么效果？";
    }

    @Override
    public String info() {
        if (this.isEquipped(Dungeon.hero)) {
            return this.desc() + "\n\n这枚" + this.name() + "正戴在你的手指上" + (this.cursed ? "，而且因为它被诅咒了，你无法将其取下。" : "。");
        }
        if (this.cursed && this.cursedKnown) {
            return this.desc() + "\n\n你能感受到这枚" + this.name() + "中潜藏着一股邪恶的魔力。";
        }
        return this.desc();
    }

    @Override
    public boolean isIdentified() {
        return super.isIdentified() && this.isKnown();
    }

    @Override
    public Item identify() {
        this.setKnown();
        return super.identify();
    }

    @Override
    public Item random() {
        int lvl = Random.Int(1, 3);
        if (Random.Float() < 0.3f) {
            this.degrade(lvl);
            this.cursed = true;
        } else {
            this.upgrade(lvl);
        }
        return this;
    }

    public static boolean allKnown() {
        return handler.known().size() == rings.length - 2;
    }

    @Override
    public int price() {
        return this.considerState(80);
    }

    protected RingBuff buff() {
        return null;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(UNFAMILIRIARITY, this.ticksToKnow);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.ticksToKnow = bundle.getInt(UNFAMILIRIARITY);
        if (this.ticksToKnow == 0) {
            this.ticksToKnow = 200;
        }
    }

    public class RingBuff
    extends Buff {
        private static final String TXT_KNOWN = "这是一枚%s";
        public int level;

        public RingBuff() {
            this.level = Ring.this.effectiveLevel();
        }

        @Override
        public boolean attachTo(Char target) {
            if (target instanceof Hero && ((Hero)target).heroClass == HeroClass.ROGUE && !Ring.this.isKnown()) {
                Ring.this.setKnown();
                GLog.i(TXT_KNOWN, Ring.this.name());
                Badges.validateItemLevelAquired(Ring.this);
            }
            return super.attachTo(target);
        }

        @Override
        public boolean act() {
            if (!Ring.this.isIdentified() && --Ring.this.ticksToKnow <= 0) {
                String gemName = Ring.this.name();
                Ring.this.identify();
                GLog.w(Ring.TXT_IDENTIFY, gemName, Ring.this.toString());
                Badges.validateItemLevelAquired(Ring.this);
            }
            Ring.this.use();
            this.spend(1.0f);
            return true;
        }
    }
}

