/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.melee;

import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;

public class WarHammer
extends MeleeWeapon {
    public WarHammer() {
        super(5, 1.2f, 1.0f);
        this.name = "战锤";
        this.image = 23;
    }

    @Override
    public String desc() {
        return "很少有生物能扛住这柄由铅与精钢铸成的庞大重锤的粉碎一击，但也唯有最强壮的冒险者才能挥动它。";
    }
}

