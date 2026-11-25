/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor;

import com.watabou.pixeldungeon.items.armor.Armor;

public class PlateArmor
extends Armor {
    public PlateArmor() {
        super(5);
        this.name = "板甲";
        this.image = 28;
    }

    @Override
    public String desc() {
        return "厚重的金属板拼接到一起，为能承受其骇人重量的冒险家提供无与伦比的防御。";
    }
}

