/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor.glyphs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.wands.WandOfBlink;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Displacement
extends Armor.Glyph {
    private static final String TXT_DISPLACEMENT = "定相之%s";
    private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing(0x66AAFF);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        if (Dungeon.bossLevel()) {
            return damage;
        }
        int level = armor.effectiveLevel();
        int nTries = (level < 0 ? 1 : level + 1) * 5;
        for (int i = 0; i < nTries; ++i) {
            int pos = Random.Int(1024);
            if (!Dungeon.visible[pos] || !Level.passable[pos] || Actor.findChar(pos) != null) continue;
            WandOfBlink.appear(defender, pos);
            Dungeon.level.press(pos, defender);
            Dungeon.observe();
            break;
        }
        return damage;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_DISPLACEMENT, weaponName);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLUE;
    }
}

