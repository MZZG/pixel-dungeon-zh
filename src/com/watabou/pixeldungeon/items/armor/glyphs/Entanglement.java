/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor.glyphs;

import com.watabou.noosa.Camera;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Roots;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.particles.EarthParticle;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.plants.Earthroot;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Entanglement
extends Armor.Glyph {
    private static final String TXT_ENTANGLEMENT = "缠绕之%s";
    private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing(0x448822);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        int level = Math.max(0, armor.effectiveLevel());
        if (Random.Int(4) == 0) {
            Buff.prolong(defender, Roots.class, 5 - level / 5);
            Buff.affect(defender, Earthroot.Armor.class).level(5 * (level + 1));
            CellEmitter.bottom(defender.pos).start(EarthParticle.FACTORY, 0.05f, 8);
            Camera.main.shake(1.0f, 0.4f);
        }
        return damage;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_ENTANGLEMENT, weaponName);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return GREEN;
    }
}

