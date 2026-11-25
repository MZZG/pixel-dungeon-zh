/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.mobs.Monk;
import com.watabou.pixeldungeon.sprites.SeniorSprite;
import com.watabou.utils.Random;

public class Senior
extends Monk {
    public Senior() {
        this.name = "武僧大师";
        this.spriteClass = SeniorSprite.class;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(12, 20);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (Random.Int(10) == 0) {
            Buff.prolong(enemy, Paralysis.class, 1.1f);
        }
        return super.attackProc(enemy, damage);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        Badges.validateRare(this);
    }
}

