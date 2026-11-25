/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.melee;

import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;

public class BattleAxe
extends MeleeWeapon {
    public BattleAxe() {
        super(4, 1.2f, 1.0f);
        this.name = "战斧";
        this.image = 22;
    }

    @Override
    public String desc() {
        return "这把战斧巨大的精钢斧刃让每一次挥砍都带着沉重无比的力道。";
    }
}

