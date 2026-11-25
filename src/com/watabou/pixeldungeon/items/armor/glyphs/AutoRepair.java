/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor.glyphs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.sprites.ItemSprite;

public class AutoRepair
extends Armor.Glyph {
    private static final String TXT_AUTO_REPAIR = "自修之%s";
    private static ItemSprite.Glowing GRAY = new ItemSprite.Glowing(0xCC8888);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        if (defender instanceof Hero && Dungeon.gold >= armor.tier) {
            Dungeon.gold -= armor.tier;
            armor.polish();
        }
        return damage;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_AUTO_REPAIR, weaponName);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return GRAY;
    }
}

