/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Bleeding;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.mobs.Rat;
import com.watabou.pixeldungeon.sprites.AlbinoSprite;
import com.watabou.utils.Random;

public class Albino
extends Rat {
    public Albino() {
        this.name = "白化老鼠";
        this.spriteClass = AlbinoSprite.class;
        this.HT = 15;
        this.HP = 15;
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        Badges.validateRare(this);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (Random.Int(2) == 0) {
            Buff.affect(enemy, Bleeding.class).set(damage);
        }
        return damage;
    }
}

