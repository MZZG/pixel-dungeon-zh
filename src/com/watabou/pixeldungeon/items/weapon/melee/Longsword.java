/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.melee;

import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;

public class Longsword
extends MeleeWeapon {
    public Longsword() {
        super(4, 1.0f, 1.0f);
        this.name = "长剑";
        this.image = 21;
    }

    @Override
    public String desc() {
        return "这柄修长沉重的剑刃将全身重量倾注于每一次斩击之中，威力惊人。";
    }
}

