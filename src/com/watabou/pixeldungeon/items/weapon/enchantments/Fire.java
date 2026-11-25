/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.enchantments;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.effects.particles.FlameParticle;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Fire
extends Weapon.Enchantment {
    private static final String TXT_BLAZING = "烈焰%s";
    private static ItemSprite.Glowing ORANGE = new ItemSprite.Glowing(0xFF4400);

    @Override
    public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
        int level = Math.max(0, weapon.effectiveLevel());
        if (Random.Int(level + 3) >= 2) {
            if (Random.Int(2) == 0) {
                Buff.affect(defender, Burning.class).reignite(defender);
            }
            defender.damage(Random.Int(1, level + 2), this);
            defender.sprite.emitter().burst(FlameParticle.FACTORY, level + 1);
            return true;
        }
        return false;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return ORANGE;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_BLAZING, weaponName);
    }
}

