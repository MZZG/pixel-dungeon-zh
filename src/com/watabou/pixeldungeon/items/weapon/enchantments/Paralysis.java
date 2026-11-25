/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.enchantments;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Paralysis
extends Weapon.Enchantment {
    private static final String TXT_STUNNING = "震慑%s";
    private static ItemSprite.Glowing YELLOW = new ItemSprite.Glowing(0xCCAA44);

    @Override
    public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
        int level = Math.max(0, weapon.effectiveLevel());
        if (Random.Int(level + 8) >= 7) {
            Buff.prolong(defender, com.watabou.pixeldungeon.actors.buffs.Paralysis.class, Random.Float(1.0f, 1.5f + (float)level));
            return true;
        }
        return false;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return YELLOW;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_STUNNING, weaponName);
    }
}

