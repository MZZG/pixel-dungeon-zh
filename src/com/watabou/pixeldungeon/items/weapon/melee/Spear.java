/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.melee;

import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;

public class Spear
extends MeleeWeapon {
    public Spear() {
        super(2, 1.0f, 1.5f);
        this.name = "长矛";
        this.image = 29;
    }

    @Override
    public String desc() {
        return "一根装有锋利铁枪头的细长木杆。";
    }
}

