/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor.glyphs;

import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.npcs.MirrorImage;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.wands.WandOfBlink;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;
import java.util.ArrayList;

public class Multiplicity
extends Armor.Glyph {
    private static final String TXT_MULTIPLICITY = "分身之%s";
    private static ItemSprite.Glowing PINK = new ItemSprite.Glowing(0xCCAA88);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        int level = Math.max(0, armor.effectiveLevel());
        if (Random.Int(level / 2 + 6) >= 5) {
            ArrayList<Integer> respawnPoints = new ArrayList<Integer>();
            for (int i = 0; i < Level.NEIGHBOURS8.length; ++i) {
                int p = defender.pos + Level.NEIGHBOURS8[i];
                if (Actor.findChar(p) != null || !Level.passable[p] && !Level.avoid[p]) continue;
                respawnPoints.add(p);
            }
            if (respawnPoints.size() > 0) {
                MirrorImage mob = new MirrorImage();
                mob.duplicate((Hero)defender);
                GameScene.add(mob);
                WandOfBlink.appear(mob, (Integer)Random.element(respawnPoints));
                defender.damage(Random.IntRange(1, defender.HT / 6), this);
                this.checkOwner(defender);
            }
        }
        return damage;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_MULTIPLICITY, weaponName);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return PINK;
    }
}

