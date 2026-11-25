/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.rings;

import com.watabou.pixeldungeon.items.rings.Ring;

public class RingOfHaste
extends Ring {
    public RingOfHaste() {
        this.name = "疾速之戒";
    }

    @Override
    protected Ring.RingBuff buff() {
        return new Haste();
    }

    @Override
    public String desc() {
        return this.isKnown() ? "这枚戒指能加速佩戴者周围的时间流逝，使所有行动都更快一步。" : super.desc();
    }

    public class Haste
    extends Ring.RingBuff {
    }
}

