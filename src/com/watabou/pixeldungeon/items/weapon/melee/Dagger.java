/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.melee;

import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;

public class Dagger
extends MeleeWeapon {
    public Dagger() {
        super(1, 1.2f, 1.0f);
        this.name = "匕首";
        this.image = 19;
    }

    @Override
    public String desc() {
        return "一把朴素的铁匕首，木质手柄已经磨得光滑。";
    }
}

