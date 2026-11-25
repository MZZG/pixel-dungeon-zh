/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.FlavourBuff;
import com.watabou.utils.Bundle;

public class Terror
extends FlavourBuff {
    public static final float DURATION = 10.0f;
    public int object = 0;
    private static final String OBJECT = "object";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(OBJECT, this.object);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.object = bundle.getInt(OBJECT);
    }

    @Override
    public int icon() {
        return 10;
    }

    public String toString() {
        return "恐惧";
    }

    public static void recover(Char target) {
        Terror terror = target.buff(Terror.class);
        if (terror != null && terror.cooldown() < 10.0f) {
            target.remove(terror);
        }
    }
}

