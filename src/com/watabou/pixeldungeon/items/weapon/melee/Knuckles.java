/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.melee;

import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;

public class Knuckles
extends MeleeWeapon {
    public Knuckles() {
        super(1, 1.0f, 0.5f);
        this.name = "指虎";
        this.image = 16;
    }

    @Override
    public String desc() {
        return "一块打造成贴合指关节形状的铁块，轻巧而难以被缴械。";
    }
}

