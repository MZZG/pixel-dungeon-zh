/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor;

import com.watabou.pixeldungeon.items.armor.Armor;

public class ScaleArmor
extends Armor {
    public ScaleArmor() {
        super(4);
        this.name = "鳞甲";
        this.image = 27;
    }

    @Override
    public String desc() {
        return "在厚实的皮背心中缝入金属鳞片，形成了灵活而高防御的护甲。";
    }
}

