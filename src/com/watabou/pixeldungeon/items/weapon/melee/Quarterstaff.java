/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.melee;

import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;

public class Quarterstaff
extends MeleeWeapon {
    public Quarterstaff() {
        super(2, 1.0f, 1.0f);
        this.name = "铁头棍";
        this.image = 17;
    }

    @Override
    public String desc() {
        return "两端包裹着铁箍的硬木长棍。";
    }
}

