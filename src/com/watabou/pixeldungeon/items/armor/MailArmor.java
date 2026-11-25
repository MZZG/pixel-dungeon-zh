/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor;

import com.watabou.pixeldungeon.items.armor.Armor;

public class MailArmor
extends Armor {
    public MailArmor() {
        super(3);
        this.name = "链甲";
        this.image = 26;
    }

    @Override
    public String desc() {
        return "由金属链环环相扣制成的一套结实又不失灵活的护甲。";
    }
}

