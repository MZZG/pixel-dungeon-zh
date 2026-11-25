/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.melee;

import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;

public class Sword
extends MeleeWeapon {
    public Sword() {
        super(3, 1.0f, 1.0f);
        this.name = "单手剑";
        this.image = 20;
    }

    @Override
    public String desc() {
        return "剃刀般锋利的钢制剑身闪烁着令人安心的寒光。";
    }
}

