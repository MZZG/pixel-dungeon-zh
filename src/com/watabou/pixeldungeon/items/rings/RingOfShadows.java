/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.rings;

import com.watabou.pixeldungeon.items.rings.Ring;

public class RingOfShadows
extends Ring {
    public RingOfShadows() {
        this.name = "暗影之戒";
    }

    @Override
    protected Ring.RingBuff buff() {
        return new Shadows();
    }

    @Override
    public String desc() {
        return this.isKnown() ? "佩戴这枚戒指时敌人更难察觉你的存在。负等级的暗影之戒反而会惊动原本未发现你的敌人。" : super.desc();
    }

    public class Shadows
    extends Ring.RingBuff {
    }
}

