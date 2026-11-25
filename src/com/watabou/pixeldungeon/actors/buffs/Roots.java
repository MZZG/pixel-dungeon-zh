/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.FlavourBuff;

public class Roots
extends FlavourBuff {
    @Override
    public boolean attachTo(Char target) {
        if (!target.flying && super.attachTo(target)) {
            target.rooted = true;
            return true;
        }
        return false;
    }

    @Override
    public void detach() {
        this.target.rooted = false;
        super.detach();
    }

    @Override
    public int icon() {
        return 11;
    }

    public String toString() {
        return "缠绕";
    }
}

