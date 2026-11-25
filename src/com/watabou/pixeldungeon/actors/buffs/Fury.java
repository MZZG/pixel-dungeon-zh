/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.actors.buffs.Buff;

public class Fury
extends Buff {
    public static float LEVEL = 0.4f;

    @Override
    public boolean act() {
        if ((float)this.target.HP > (float)this.target.HT * LEVEL) {
            this.detach();
        }
        this.spend(1.0f);
        return true;
    }

    @Override
    public int icon() {
        return 18;
    }

    public String toString() {
        return "暴怒";
    }
}

