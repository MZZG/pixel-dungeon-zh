/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.actors.buffs.FlavourBuff;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.buffs.Vertigo;
import java.util.HashSet;

public class GasesImmunity
extends FlavourBuff {
    public static final float DURATION = 5.0f;
    public static final HashSet<Class<?>> IMMUNITIES = new HashSet();

    @Override
    public int icon() {
        return 25;
    }

    public String toString() {
        return "气体免疫";
    }

    static {
        IMMUNITIES.add(Paralysis.class);
        IMMUNITIES.add(ToxicGas.class);
        IMMUNITIES.add(Vertigo.class);
    }
}

