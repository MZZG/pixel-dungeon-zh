/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.FlavourBuff;
import com.watabou.pixeldungeon.actors.buffs.Roots;

public class Levitation
extends FlavourBuff {
    public static final float DURATION = 20.0f;

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            target.flying = true;
            Roots.detach(target, Roots.class);
            return true;
        }
        return false;
    }

    @Override
    public void detach() {
        this.target.flying = false;
        Dungeon.level.press(this.target.pos, this.target);
        super.detach();
    }

    @Override
    public int icon() {
        return 1;
    }

    public String toString() {
        return "漂浮";
    }
}

