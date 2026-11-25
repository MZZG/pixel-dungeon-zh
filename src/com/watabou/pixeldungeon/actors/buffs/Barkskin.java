/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.actors.buffs.Buff;

public class Barkskin
extends Buff {
    private int level = 0;

    @Override
    public boolean act() {
        if (this.target.isAlive()) {
            this.spend(1.0f);
            if (--this.level <= 0) {
                this.detach();
            }
        } else {
            this.detach();
        }
        return true;
    }

    public int level() {
        return this.level;
    }

    public void level(int value) {
        if (this.level < value) {
            this.level = value;
        }
    }

    @Override
    public int icon() {
        return 24;
    }

    public String toString() {
        return "树肤";
    }
}

