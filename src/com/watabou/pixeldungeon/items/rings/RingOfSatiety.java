/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.rings;

import com.watabou.pixeldungeon.items.rings.Ring;

public class RingOfSatiety
extends Ring {
    public RingOfSatiety() {
        this.name = "饱食之戒";
    }

    @Override
    protected Ring.RingBuff buff() {
        return new Satiety();
    }

    @Override
    public String desc() {
        return this.isKnown() ? "佩戴这枚戒指能让你更长时间不感到饥饿。负等级的饱食之戒则会加速饥饿。" : super.desc();
    }

    public class Satiety
    extends Ring.RingBuff {
    }
}

