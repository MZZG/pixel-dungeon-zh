/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.DewVial;
import com.watabou.pixeldungeon.items.Item;

public class Dewdrop
extends Item {
    private static final String TXT_VALUE = "%+dHP";

    public Dewdrop() {
        this.name = "露珠";
        this.image = 81;
        this.stackable = true;
    }

    @Override
    public boolean doPickUp(Hero hero) {
        DewVial vial = hero.belongings.getItem(DewVial.class);
        if (hero.HP < hero.HT || vial == null || vial.isFull()) {
            int effect;
            int value = 1 + (Dungeon.depth - 1) / 5;
            if (hero.heroClass == HeroClass.HUNTRESS) {
                ++value;
            }
            if ((effect = Math.min(hero.HT - hero.HP, value * this.quantity)) > 0) {
                hero.HP += effect;
                hero.sprite.emitter().burst(Speck.factory(0), 1);
                hero.sprite.showStatus(65280, TXT_VALUE, effect);
            }
        } else {
            vial.collectDew(this);
        }
        Sample.INSTANCE.play("snd_dewdrop.mp3");
        hero.spendAndNext(1.0f);
        return true;
    }

    @Override
    public String info() {
        return "一滴晶莹剔透的露珠。";
    }
}

