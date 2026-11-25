/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.FlavourBuff;

public class Slow
extends FlavourBuff {
    private static final float DURATION = 10.0f;

    @Override
    public int icon() {
        return 7;
    }

    public String toString() {
        return "迟缓";
    }

    public float timeScale() {
        return 1.0f / (this.cooldown() / 5.0f + 1.0f);
    }

    public static float duration(Char ch) {
        return 10.0f * Slow.durationFactor(ch);
    }
}

