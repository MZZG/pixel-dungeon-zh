/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.food;

import com.watabou.pixeldungeon.items.food.Food;

public class OverpricedRation
extends Food {
    public OverpricedRation() {
        this.name = "高价口粮";
        this.image = 115;
        this.energy = 100.0f;
        this.message = "味道还算凑合。";
    }

    @Override
    public String info() {
        return "它看起来和普通口粮一模一样，只是份量小了一圈。";
    }

    @Override
    public int price() {
        return 20 * this.quantity;
    }
}

