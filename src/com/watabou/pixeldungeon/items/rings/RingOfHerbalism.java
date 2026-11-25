/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.rings;

import com.watabou.pixeldungeon.items.rings.Ring;

public class RingOfHerbalism
extends Ring {
    public RingOfHerbalism() {
        this.name = "草药之戒";
    }

    @Override
    protected Ring.RingBuff buff() {
        return new Herbalism();
    }

    @Override
    public String desc() {
        return this.isKnown() ? "这枚戒指能提高你踩踏高草时获得露珠与植物种子的几率。" : super.desc();
    }

    public class Herbalism
    extends Ring.RingBuff {
    }
}

