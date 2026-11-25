/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.actors.mobs.npcs.Ghost;
import com.watabou.pixeldungeon.items.Gold;
import com.watabou.pixeldungeon.sprites.GnollSprite;
import com.watabou.utils.Random;

public class Gnoll
extends Mob {
    public Gnoll() {
        this.name = "豺狼巡查";
        this.spriteClass = GnollSprite.class;
        this.HT = 12;
        this.HP = 12;
        this.defenseSkill = 4;
        this.EXP = 2;
        this.maxLvl = 8;
        this.loot = Gold.class;
        this.lootChance = 0.5f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(2, 5);
    }

    @Override
    public int attackSkill(Char target) {
        return 11;
    }

    @Override
    public int dr() {
        return 2;
    }

    @Override
    public void die(Object cause) {
        Ghost.Quest.processSewersKill(this.pos);
        super.die(cause);
    }

    @Override
    public String description() {
        return "豺狼人是鬣狗状的类人生物。它们在下水道和地牢里游荡，不时向上行动试图向地表发起进攻。豺狼巡查是它们种群里最普通的一员，既没有暴徒强壮，也赶不上萨满聪明。";
    }
}

