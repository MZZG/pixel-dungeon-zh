/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.enchantments;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.particles.ShadowParticle;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Death
extends Weapon.Enchantment {
    private static final String TXT_GRIM = "死神%s";
    private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing(0);

    @Override
    public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
        int level = Math.max(0, weapon.effectiveLevel());
        if (Random.Int(level + 100) >= 92) {
            defender.damage(defender.HP, this);
            defender.sprite.emitter().burst(ShadowParticle.UP, 5);
            if (!defender.isAlive() && attacker instanceof Hero) {
                Badges.validateGrimWeapon();
            }
            return true;
        }
        return false;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_GRIM, weaponName);
    }
}

