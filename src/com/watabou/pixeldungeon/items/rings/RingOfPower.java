/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.rings;

import com.watabou.pixeldungeon.items.rings.Ring;

public class RingOfPower
extends Ring {
    public RingOfPower() {
        this.name = "威能之戒";
    }

    @Override
    protected Ring.RingBuff buff() {
        return new Power();
    }

    @Override
    public String desc() {
        return this.isKnown() ? "在戒指散发的能量场中，你的法杖威力会得到增强。负等级的威能之戒则会削弱法杖威力。" : super.desc();
    }

    public class Power
    extends Ring.RingBuff {
    }
}

