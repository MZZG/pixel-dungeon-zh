/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.mobs.Scorpio;
import com.watabou.pixeldungeon.sprites.AcidicSprite;
import com.watabou.utils.Random;

public class Acidic
extends Scorpio {
    public Acidic() {
        this.name = "酸液巨蝎";
        this.spriteClass = AcidicSprite.class;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        int dmg = Random.IntRange(0, damage);
        if (dmg > 0) {
            enemy.damage(dmg, this);
        }
        return super.defenseProc(enemy, damage);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        Badges.validateRare(this);
    }
}

