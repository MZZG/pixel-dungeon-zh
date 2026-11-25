/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.enchantments;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Leech
extends Weapon.Enchantment {
    private static final String TXT_VAMPIRIC = "血饮%s";
    private static ItemSprite.Glowing RED = new ItemSprite.Glowing(0x660022);

    @Override
    public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
        int level = Math.max(0, weapon.effectiveLevel());
        int maxValue = damage * (level + 2) / (level + 6);
        int effValue = Math.min(Random.IntRange(0, maxValue), attacker.HT - attacker.HP);
        if (effValue > 0) {
            attacker.HP += effValue;
            attacker.sprite.emitter().start(Speck.factory(0), 0.4f, 1);
            attacker.sprite.showStatus(65280, Integer.toString(effValue), new Object[0]);
            return true;
        }
        return false;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return RED;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_VAMPIRIC, weaponName);
    }
}

