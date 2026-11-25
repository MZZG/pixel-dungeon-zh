/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor;

import com.watabou.pixeldungeon.items.armor.Armor;

public class ClothArmor
extends Armor {
    public ClothArmor() {
        super(1);
        this.name = "布甲";
        this.image = 24;
    }

    @Override
    public String desc() {
        return "这件轻便的护甲能提供最基本的防御。";
    }
}

