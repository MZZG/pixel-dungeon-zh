/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.enchantments;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Poison
extends Weapon.Enchantment {
    private static final String TXT_VENOMOUS = "猛毒%s";
    private static ItemSprite.Glowing PURPLE = new ItemSprite.Glowing(0x4400AA);

    @Override
    public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
        int level = Math.max(0, weapon.effectiveLevel());
        if (Random.Int(level + 3) >= 2) {
            Buff.affect(defender, com.watabou.pixeldungeon.actors.buffs.Poison.class).set(com.watabou.pixeldungeon.actors.buffs.Poison.durationFactor(defender) * (float)(level + 1));
            return true;
        }
        return false;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return PURPLE;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_VENOMOUS, weaponName);
    }
}

