/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.enchantments;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.sprites.ItemSprite;

public class Luck
extends Weapon.Enchantment {
    private static final String TXT_LUCKY = "幸运%s";
    private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing(65280);

    @Override
    public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
        int level = Math.max(0, weapon.effectiveLevel());
        int dmg = damage;
        for (int i = 1; i <= level + 1; ++i) {
            dmg = Math.max(dmg, attacker.damageRoll() - i);
        }
        if (dmg > damage) {
            defender.damage(dmg - damage, this);
            return true;
        }
        return false;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_LUCKY, weaponName);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return GREEN;
    }
}

