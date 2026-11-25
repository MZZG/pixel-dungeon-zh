/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.scrolls;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.actors.buffs.Blindness;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.ItemStatusHandler;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfChallenge;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfEnchantment;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfIdentify;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfLullaby;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfRecharging;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfTerror;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.watabou.pixeldungeon.sprites.HeroSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import java.util.ArrayList;
import java.util.HashSet;

public abstract class Scroll
extends Item {
    private static final String TXT_BLINDED = "失明时无法阅读卷轴";
    public static final String AC_READ = "READ";
    protected static final float TIME_TO_READ = 1.0f;
    private static final Class<?>[] scrolls = new Class[]{ScrollOfIdentify.class, ScrollOfMagicMapping.class, ScrollOfRecharging.class, ScrollOfRemoveCurse.class, ScrollOfTeleportation.class, ScrollOfChallenge.class, ScrollOfTerror.class, ScrollOfLullaby.class, ScrollOfPsionicBlast.class, ScrollOfMirrorImage.class, ScrollOfUpgrade.class, ScrollOfEnchantment.class};
    private static final String[] runes = new String[]{"KAUNAN", "SOWILO", "LAGUZ", "YNGVI", "GYFU", "RAIDO", "ISAZ", "MANNAZ", "NAUDIZ", "BERKANAN", "ODAL", "TIWAZ"};
    private static final Integer[] images = new Integer[]{40, 41, 42, 43, 44, 45, 46, 47, 76, 77, 78, 79};
    private static ItemStatusHandler<Scroll> handler;
    private String rune;

    public static void initLabels() {
        handler = new ItemStatusHandler(scrolls, runes, images);
    }

    public static void save(Bundle bundle) {
        handler.save(bundle);
    }

    public static void restore(Bundle bundle) {
        handler = new ItemStatusHandler(scrolls, runes, images, bundle);
    }

    public Scroll() {
        this.stackable = true;
        this.defaultAction = AC_READ;
        this.image = handler.image(this);
        this.rune = handler.label(this);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_READ);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action.equals(AC_READ)) {
            if (hero.buff(Blindness.class) != null) {
                GLog.w(TXT_BLINDED, new Object[0]);
            } else {
                curUser = hero;
                curItem = this.detach(hero.belongings.backpack);
                this.doRead();
            }
        } else {
            super.execute(hero, action);
        }
    }

    protected abstract void doRead();

    protected void readAnimation() {
        curUser.spend(1.0f);
        curUser.busy();
        ((HeroSprite)Scroll.curUser.sprite).read();
    }

    public boolean isKnown() {
        return handler.isKnown(this);
    }

    public void setKnown() {
        if (!this.isKnown()) {
            handler.know(this);
        }
        Badges.validateAllScrollsIdentified();
    }

    @Override
    public Item identify() {
        this.setKnown();
        return super.identify();
    }

    @Override
    public String name() {
        return this.isKnown() ? this.name : "符文卷轴 \"" + this.rune + "\"";
    }

    @Override
    public String info() {
        return this.isKnown() ? this.desc() : "这张羊皮纸上写满了难以辨认的文字，顶端印着符文 " + this.rune + "。谁知道大声念出它会发生什么？";
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return this.isKnown();
    }

    public static HashSet<Class<? extends Scroll>> getKnown() {
        return handler.known();
    }

    public static HashSet<Class<? extends Scroll>> getUnknown() {
        return handler.unknown();
    }

    public static boolean allKnown() {
        return handler.known().size() == scrolls.length;
    }

    @Override
    public int price() {
        return 15 * this.quantity;
    }
}

