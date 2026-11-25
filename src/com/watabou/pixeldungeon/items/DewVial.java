/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.effects.particles.ShaftParticle;
import com.watabou.pixeldungeon.items.Dewdrop;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import java.util.ArrayList;

public class DewVial
extends Item {
    private static final int MAX_VOLUME = 10;
    private static final String AC_DRINK = "DRINK";
    private static final float TIME_TO_DRINK = 1.0f;
    private static final String TXT_VALUE = "%+dHP";
    private static final String TXT_STATUS = "%d/%d";
    private static final String TXT_AUTO_DRINK = "露水瓶自动清空并治愈了你的伤势。";
    private static final String TXT_COLLECTED = "你将露珠收集进了露水瓶中。";
    private static final String TXT_FULL = "你的露水瓶已经满了！";
    private static final String TXT_EMPTY = "你的露水瓶是空的！";
    private int volume;
    private static final String VOLUME = "volume";
    private static final double NUM = 20.0;
    private static final double POW = Math.log10(20.0);
    private static final ItemSprite.Glowing WHITE = new ItemSprite.Glowing(0xFFFFCC);

    public DewVial() {
        this.name = "露水瓶";
        this.image = 120;
        this.defaultAction = AC_DRINK;
        this.unique = true;
        this.volume = 0;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(VOLUME, this.volume);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.volume = bundle.getInt(VOLUME);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (this.volume > 0) {
            actions.add(AC_DRINK);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action.equals(AC_DRINK)) {
            if (this.volume > 0) {
                int value = (int)Math.ceil(Math.pow(this.volume, POW) / 20.0 * (double)hero.HT);
                int effect = Math.min(hero.HT - hero.HP, value);
                if (effect > 0) {
                    hero.HP += effect;
                    hero.sprite.emitter().burst(Speck.factory(0), this.volume > 5 ? 2 : 1);
                    hero.sprite.showStatus(65280, TXT_VALUE, effect);
                }
                this.volume = 0;
                hero.spend(1.0f);
                hero.busy();
                Sample.INSTANCE.play("snd_drink.mp3");
                hero.sprite.operate(hero.pos);
                this.updateQuickslot();
            } else {
                GLog.w(TXT_EMPTY, new Object[0]);
            }
        } else {
            super.execute(hero, action);
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    public boolean isFull() {
        return this.volume >= 10;
    }

    public void collectDew(Dewdrop dew) {
        GLog.i(TXT_COLLECTED, new Object[0]);
        this.volume += dew.quantity;
        if (this.volume >= 10) {
            this.volume = 10;
            GLog.p(TXT_FULL, new Object[0]);
        }
        this.updateQuickslot();
    }

    public void fill() {
        this.volume = 10;
        this.updateQuickslot();
    }

    public static void autoDrink(Hero hero) {
        DewVial vial = hero.belongings.getItem(DewVial.class);
        if (vial != null && vial.isFull()) {
            vial.execute(hero);
            hero.sprite.emitter().start(ShaftParticle.FACTORY, 0.2f, 3);
            GLog.w(TXT_AUTO_DRINK, new Object[0]);
        }
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return this.isFull() ? WHITE : null;
    }

    @Override
    public String status() {
        return Utils.format(TXT_STATUS, this.volume, 10);
    }

    @Override
    public String info() {
        return "你可以将多余的露珠储存在这个小瓶中以备不时之需。当露水瓶装满时，在生死攸关的瞬间会自动饮用其中的露水。";
    }

    @Override
    public String toString() {
        return super.toString() + " (" + this.status() + ")";
    }
}

