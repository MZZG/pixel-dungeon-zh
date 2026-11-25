/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.bags;

import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.bags.Bag;
import com.watabou.pixeldungeon.items.keys.Key;

public class Keyring
extends Bag {
    public Keyring() {
        this.name = "钥匙环";
        this.image = 126;
        this.size = 12;
    }

    @Override
    public boolean grab(Item item) {
        return item instanceof Key;
    }

    @Override
    public int price() {
        return 50;
    }

    @Override
    public String info() {
        return "这是一个黄铜钥匙环，可以让你把所有钥匙单独收纳在一起，不占用背包空间。";
    }
}

