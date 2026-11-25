/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.food;

import com.watabou.pixeldungeon.items.food.Food;
import com.watabou.pixeldungeon.items.food.MysteryMeat;

public class ChargrilledMeat
extends Food {
    public ChargrilledMeat() {
        this.name = "烤肉";
        this.image = 114;
        this.energy = 100.0f;
    }

    @Override
    public String info() {
        return "看起来像块不错的牛排。";
    }

    @Override
    public int price() {
        return 5 * this.quantity;
    }

    public static Food cook(MysteryMeat ingredient) {
        ChargrilledMeat result = new ChargrilledMeat();
        result.quantity = ingredient.quantity();
        return result;
    }
}

