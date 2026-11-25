/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor;

import com.watabou.pixeldungeon.items.armor.Armor;

public class LeatherArmor
extends Armor {
    public LeatherArmor() {
        super(2);
        this.name = "皮甲";
        this.image = 25;
    }

    @Override
    public String desc() {
        return "用鞣制的兽皮制成的护甲。没有布甲轻，但提供更好的防御。";
    }
}

