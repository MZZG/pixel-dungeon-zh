/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.actors.buffs.FlavourBuff;

public class Cripple
extends FlavourBuff {
    public static final float DURATION = 10.0f;

    @Override
    public int icon() {
        return 23;
    }

    public String toString() {
        return "残废";
    }
}

