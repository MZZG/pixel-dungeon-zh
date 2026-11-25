/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.FlavourBuff;
import com.watabou.pixeldungeon.actors.buffs.Frost;
import com.watabou.pixeldungeon.items.rings.RingOfElements;

public class Paralysis
extends FlavourBuff {
    private static final float DURATION = 10.0f;

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            target.paralysed = true;
            return true;
        }
        return false;
    }

    @Override
    public void detach() {
        super.detach();
        Paralysis.unfreeze(this.target);
    }

    @Override
    public int icon() {
        return 4;
    }

    public String toString() {
        return "麻痹";
    }

    public static float duration(Char ch) {
        RingOfElements.Resistance r = ch.buff(RingOfElements.Resistance.class);
        return r != null ? r.durationFactor() * 10.0f : 10.0f;
    }

    public static void unfreeze(Char ch) {
        if (ch.buff(Paralysis.class) == null && ch.buff(Frost.class) == null) {
            ch.paralysed = false;
        }
    }
}

