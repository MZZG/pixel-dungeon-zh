/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.rings;

import com.watabou.pixeldungeon.items.rings.Ring;

public class RingOfAccuracy
extends Ring {
    public RingOfAccuracy() {
        this.name = "精准之戒";
    }

    @Override
    protected Ring.RingBuff buff() {
        return new Accuracy();
    }

    @Override
    public String desc() {
        return this.isKnown() ? "这枚戒指能提高你攻击命中敌人的几率。" : super.desc();
    }

    public class Accuracy
    extends Ring.RingBuff {
    }
}

