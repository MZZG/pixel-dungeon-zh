/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.actors.mobs.npcs.Ghost;
import com.watabou.pixeldungeon.sprites.RatSprite;
import com.watabou.utils.Random;

public class Rat
extends Mob {
    public Rat() {
        this.name = "啮齿小鼠";
        this.spriteClass = RatSprite.class;
        this.HT = 8;
        this.HP = 8;
        this.defenseSkill = 3;
        this.maxLvl = 5;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(1, 5);
    }

    @Override
    public int attackSkill(Char target) {
        return 8;
    }

    @Override
    public int dr() {
        return 1;
    }

    @Override
    public void die(Object cause) {
        Ghost.Quest.processSewersKill(this.pos);
        super.die(cause);
    }

    @Override
    public String description() {
        return "啮齿小鼠是下水道中颇具攻击性但较为弱小的生物，唯有成群结队时才具有威胁。";
    }
}

