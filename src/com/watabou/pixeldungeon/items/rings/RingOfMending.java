/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.rings;

import com.watabou.pixeldungeon.items.rings.Ring;

public class RingOfMending
extends Ring {
    public RingOfMending() {
        this.name = "愈合之戒";
    }

    @Override
    protected Ring.RingBuff buff() {
        return new Rejuvenation();
    }

    @Override
    public String desc() {
        return this.isKnown() ? "这枚戒指能增强身体的再生能力，加快生命值的自然恢复速度。负等级的愈合之戒会减缓甚至完全阻止自然回复。" : super.desc();
    }

    public class Rejuvenation
    extends Ring.RingBuff {
    }
}

