/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.food;

import com.watabou.pixeldungeon.items.food.Food;

public class Pasty
extends Food {
    public Pasty() {
        this.name = "馅饼";
        this.image = 112;
        this.energy = 360.0f;
    }

    @Override
    public String info() {
        return "这是一个正宗的康沃尔馅饼，里面包着传统的牛肉与土豆馅料。";
    }

    @Override
    public int price() {
        return 20 * this.quantity;
    }
}

