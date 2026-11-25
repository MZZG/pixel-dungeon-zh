/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.rings;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.rings.Ring;

public class RingOfDetection
extends Ring {
    public RingOfDetection() {
        this.name = "探知之戒";
    }

    @Override
    public boolean doEquip(Hero hero) {
        if (super.doEquip(hero)) {
            Dungeon.hero.search(false);
            return true;
        }
        return false;
    }

    @Override
    protected Ring.RingBuff buff() {
        return new Detection();
    }

    @Override
    public String desc() {
        return this.isKnown() ? "佩戴这枚戒指可以让你无需花费时间搜索就能察觉隐藏的陷阱与暗门。但负等级的探知之戒会钝化你的感官，哪怕主动搜索也更难发现秘密。" : super.desc();
    }

    public class Detection
    extends Ring.RingBuff {
    }
}

