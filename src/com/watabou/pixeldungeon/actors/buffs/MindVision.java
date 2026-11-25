/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.FlavourBuff;

public class MindVision
extends FlavourBuff {
    public static final float DURATION = 20.0f;
    public int distance = 2;

    @Override
    public int icon() {
        return 0;
    }

    public String toString() {
        return "灵视";
    }

    @Override
    public void detach() {
        super.detach();
        Dungeon.observe();
    }
}

