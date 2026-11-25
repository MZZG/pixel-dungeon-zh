/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.bags;

import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.bags.Bag;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfWipeOut;

public class ScrollHolder
extends Bag {
    public ScrollHolder() {
        this.name = "卷轴筒";
        this.image = 104;
        this.size = 12;
    }

    @Override
    public boolean grab(Item item) {
        return item instanceof Scroll || item instanceof ScrollOfWipeOut;
    }

    @Override
    public int price() {
        return 50;
    }

    @Override
    public String info() {
        return "你可以把任意数量的卷轴放入这个圆筒容器中，既节省背包空间，又能保护卷轴免受火焰烧毁。";
    }
}

