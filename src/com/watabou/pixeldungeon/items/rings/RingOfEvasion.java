/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.rings;

import com.watabou.pixeldungeon.items.rings.Ring;

public class RingOfEvasion
extends Ring {
    public RingOfEvasion() {
        this.name = "闪避之戒";
    }

    @Override
    protected Ring.RingBuff buff() {
        return new Evasion();
    }

    @Override
    public String desc() {
        return this.isKnown() ? "这枚戒指能提高你闪避敌人攻击的几率。" : super.desc();
    }

    public class Evasion
    extends Ring.RingBuff {
    }
}

