/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.FlavourBuff;
import com.watabou.pixeldungeon.items.rings.RingOfElements;

public class Vertigo
extends FlavourBuff {
    public static final float DURATION = 10.0f;

    @Override
    public int icon() {
        return 29;
    }

    public String toString() {
        return "眩晕";
    }

    public static float duration(Char ch) {
        RingOfElements.Resistance r = ch.buff(RingOfElements.Resistance.class);
        return r != null ? r.durationFactor() * 10.0f : 10.0f;
    }
}

