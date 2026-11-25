/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.enchantments;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.items.weapon.Weapon;

public class Instability
extends Weapon.Enchantment {
    private static final String TXT_UNSTABLE = "紊乱%s";

    @Override
    public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
        return Instability.random().proc(weapon, attacker, defender, damage);
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_UNSTABLE, weaponName);
    }
}

