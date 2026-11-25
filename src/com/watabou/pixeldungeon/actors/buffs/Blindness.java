/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.FlavourBuff;

public class Blindness
extends FlavourBuff {
    @Override
    public void detach() {
        super.detach();
        Dungeon.observe();
    }

    @Override
    public int icon() {
        return 16;
    }

    public String toString() {
        return "失明";
    }
}

