/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.FlavourBuff;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.rings.RingOfElements;

public class Weakness
extends FlavourBuff {
    private static final float DURATION = 40.0f;

    @Override
    public int icon() {
        return 14;
    }

    public String toString() {
        return "虚弱";
    }

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            Hero hero = (Hero)target;
            hero.weakened = true;
            hero.belongings.discharge();
            return true;
        }
        return false;
    }

    @Override
    public void detach() {
        super.detach();
        ((Hero)this.target).weakened = false;
    }

    public static float duration(Char ch) {
        RingOfElements.Resistance r = ch.buff(RingOfElements.Resistance.class);
        return r != null ? r.durationFactor() * 40.0f : 40.0f;
    }
}

