/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.enchantments;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Slow;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Slowness
extends Weapon.Enchantment {
    private static final String TXT_CHILLING = "寒霜%s";
    private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing(17663);

    @Override
    public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
        int level = Math.max(0, weapon.effectiveLevel());
        if (Random.Int(level + 4) >= 3) {
            Buff.prolong(defender, Slow.class, Random.Float(1.0f, 2 + level) * Slow.durationFactor(defender));
            return true;
        }
        return false;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLUE;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_CHILLING, weaponName);
    }
}

