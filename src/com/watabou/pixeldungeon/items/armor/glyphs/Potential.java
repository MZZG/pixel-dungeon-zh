/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor.glyphs;

import com.watabou.noosa.Camera;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.effects.Lightning;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.traps.LightningTrap;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Potential
extends Armor.Glyph {
    private static final String TXT_POTENTIAL = "电势之%s";
    private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing(0x66CCEE);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        int level = Math.max(0, armor.effectiveLevel());
        if (Level.adjacent(attacker.pos, defender.pos) && Random.Int(level + 7) >= 6) {
            int dmg = Random.IntRange(1, damage);
            attacker.damage(dmg, LightningTrap.LIGHTNING);
            dmg = Random.IntRange(1, dmg);
            defender.damage(dmg, LightningTrap.LIGHTNING);
            this.checkOwner(defender);
            if (defender == Dungeon.hero) {
                Camera.main.shake(2.0f, 0.3f);
            }
            int[] points = new int[]{attacker.pos, defender.pos};
            attacker.sprite.parent.add(new Lightning(points, 2, null));
        }
        return damage;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_POTENTIAL, weaponName);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLUE;
    }
}

