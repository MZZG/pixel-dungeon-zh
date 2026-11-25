/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.FlavourBuff;

public class Invisibility
extends FlavourBuff {
    public static final float DURATION = 15.0f;

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            ++target.invisible;
            return true;
        }
        return false;
    }

    @Override
    public void detach() {
        --this.target.invisible;
        super.detach();
    }

    @Override
    public int icon() {
        return 12;
    }

    public String toString() {
        return "隐形";
    }

    public static void dispel() {
        Invisibility buff = Dungeon.hero.buff(Invisibility.class);
        if (buff != null && Dungeon.hero.visibleEnemies() > 0) {
            buff.detach();
        }
    }
}

