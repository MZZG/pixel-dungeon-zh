/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.melee;

import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;

public class Glaive
extends MeleeWeapon {
    public Glaive() {
        super(5, 1.0f, 1.0f);
        this.name = "关刀";
        this.image = 30;
    }

    @Override
    public String desc() {
        return "一种长柄武器，长杆顶端装有宽阔锋利的大刀刃。";
    }
}

