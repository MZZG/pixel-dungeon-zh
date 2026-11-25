/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.enchantments;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Terror;
import com.watabou.pixeldungeon.actors.buffs.Vertigo;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Horror
extends Weapon.Enchantment {
    private static final String TXT_ELDRITCH = "异质%s";
    private static ItemSprite.Glowing GREY = new ItemSprite.Glowing(0x222222);

    @Override
    public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
        int level = Math.max(0, weapon.effectiveLevel());
        if (Random.Int(level + 5) >= 4) {
            if (defender == Dungeon.hero) {
                Buff.affect(defender, Vertigo.class, Vertigo.duration(defender));
            } else {
                Buff.affect((Char)defender, Terror.class, (float)10.0f).object = attacker.id();
            }
            return true;
        }
        return false;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return GREY;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_ELDRITCH, weaponName);
    }
}

