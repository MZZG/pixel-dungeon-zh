/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.melee;

import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;

public class Mace
extends MeleeWeapon {
    public Mace() {
        super(3, 1.0f, 0.8f);
        this.name = "硬头锤";
        this.image = 18;
    }

    @Override
    public String desc() {
        return "沉重的铁质锤头能造成极为可观的钝击伤害。";
    }
}

