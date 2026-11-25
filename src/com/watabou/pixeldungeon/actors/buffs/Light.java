/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.FlavourBuff;

public class Light
extends FlavourBuff {
    public static final float DURATION = 250.0f;
    public static final int DISTANCE = 4;

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            if (Dungeon.level != null) {
                target.viewDistance = Math.max(Dungeon.level.viewDistance, 4);
                Dungeon.observe();
            }
            return true;
        }
        return false;
    }

    @Override
    public void detach() {
        this.target.viewDistance = Dungeon.level.viewDistance;
        Dungeon.observe();
        super.detach();
    }

    @Override
    public int icon() {
        return 22;
    }

    public String toString() {
        return "光耀";
    }
}

