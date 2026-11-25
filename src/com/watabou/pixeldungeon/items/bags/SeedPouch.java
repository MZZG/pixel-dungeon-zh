/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.bags;

import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.bags.Bag;
import com.watabou.pixeldungeon.plants.Plant;

public class SeedPouch
extends Bag {
    public SeedPouch() {
        this.name = "种子袋";
        this.image = 83;
        this.size = 8;
    }

    @Override
    public boolean grab(Item item) {
        return item instanceof Plant.Seed;
    }

    @Override
    public int price() {
        return 50;
    }

    @Override
    public String info() {
        return "这个天鹅绒小袋子可以让你收纳任意数量的植物种子，非常方便。";
    }
}

