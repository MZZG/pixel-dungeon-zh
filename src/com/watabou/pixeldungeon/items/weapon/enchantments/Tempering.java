/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.enchantments;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.sprites.ItemSprite;

public class Tempering
extends Weapon.Enchantment {
    private static final String TXT_TEMPERED = "淬炼%s";
    private static ItemSprite.Glowing GRAY = new ItemSprite.Glowing(0xCC8888);

    @Override
    public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
        weapon.polish();
        return true;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return GRAY;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_TEMPERED, weaponName);
    }
}

