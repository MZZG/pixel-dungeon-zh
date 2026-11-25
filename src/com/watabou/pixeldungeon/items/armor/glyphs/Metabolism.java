/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor.glyphs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Hunger;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Random;

public class Metabolism
extends Armor.Glyph {
    private static final String TXT_METABOLISM = "代谢之%s";
    private static ItemSprite.Glowing RED = new ItemSprite.Glowing(0xCC0000);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        Hunger hunger;
        int healing;
        int level = Math.max(0, armor.effectiveLevel());
        if (Random.Int(level / 2 + 5) >= 4 && (healing = Math.min(defender.HT - defender.HP, Random.Int(1, defender.HT / 5))) > 0 && (hunger = defender.buff(Hunger.class)) != null && !hunger.isStarving()) {
            hunger.satisfy(-36.0f);
            BuffIndicator.refreshHero();
            defender.HP += healing;
            defender.sprite.emitter().burst(Speck.factory(0), 1);
            defender.sprite.showStatus(65280, Integer.toString(healing), new Object[0]);
        }
        return damage;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_METABOLISM, weaponName);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return RED;
    }
}

