/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor.glyphs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.actors.buffs.Frost;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.particles.FlameParticle;
import com.watabou.pixeldungeon.effects.particles.SnowParticle;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class AntiEntropy
extends Armor.Glyph {
    private static final String TXT_ANTI_ENTROPY = "反熵之%s";
    private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing(255);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        int level = Math.max(0, armor.effectiveLevel());
        if (Level.adjacent(attacker.pos, defender.pos) && Random.Int(level + 6) >= 5) {
            Buff.prolong(attacker, Frost.class, Frost.duration(attacker) * Random.Float(1.0f, 1.5f));
            CellEmitter.get(attacker.pos).start(SnowParticle.FACTORY, 0.2f, 6);
            Buff.affect(defender, Burning.class).reignite(defender);
            defender.sprite.emitter().burst(FlameParticle.FACTORY, 5);
        }
        return damage;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_ANTI_ENTROPY, weaponName);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLUE;
    }
}

