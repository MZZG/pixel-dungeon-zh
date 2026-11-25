/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor.glyphs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Stench
extends Armor.Glyph {
    private static final String TXT_STENCH = "恶臭之%s";
    private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing(0x22CC44);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        int level = Math.max(0, armor.effectiveLevel());
        if (Level.adjacent(attacker.pos, defender.pos) && Random.Int(level + 5) >= 4) {
            GameScene.add(Blob.seed(attacker.pos, 20, ToxicGas.class));
        }
        return damage;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_STENCH, weaponName);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return GREEN;
    }
}

