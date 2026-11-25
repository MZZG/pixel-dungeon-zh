/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor.glyphs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Charm;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

public class Affection
extends Armor.Glyph {
    private static final String TXT_AFFECTION = "魅惑之%s";
    private static ItemSprite.Glowing PINK = new ItemSprite.Glowing(0xFF4488);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        int level = (int)GameMath.gate(0.0f, armor.effectiveLevel(), 6.0f);
        if (Level.adjacent(attacker.pos, defender.pos) && Random.Int(level / 2 + 5) >= 4) {
            int duration = Random.IntRange(3, 7);
            Buff.affect((Char)attacker, Charm.class, (float)(Charm.durationFactor((Char)attacker) * (float)duration)).object = defender.id();
            attacker.sprite.centerEmitter().start(Speck.factory(11), 0.2f, 5);
            duration = (int)((float)duration * Random.Float(0.5f, 1.0f));
            Buff.affect((Char)defender, Charm.class, (float)(Charm.durationFactor((Char)defender) * (float)duration)).object = attacker.id();
            defender.sprite.centerEmitter().start(Speck.factory(11), 0.2f, 5);
        }
        return damage;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_AFFECTION, weaponName);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return PINK;
    }
}

