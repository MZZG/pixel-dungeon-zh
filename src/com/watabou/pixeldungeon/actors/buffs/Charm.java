/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.FlavourBuff;
import com.watabou.pixeldungeon.items.rings.RingOfElements;
import com.watabou.utils.Bundle;

public class Charm
extends FlavourBuff {
    public int object = 0;
    private static final String OBJECT = "object";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(OBJECT, this.object);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.object = bundle.getInt(OBJECT);
    }

    @Override
    public int icon() {
        return 21;
    }

    public String toString() {
        return "魅惑";
    }

    public static float durationFactor(Char ch) {
        RingOfElements.Resistance r = ch.buff(RingOfElements.Resistance.class);
        return r != null ? r.durationFactor() : 1.0f;
    }
}

