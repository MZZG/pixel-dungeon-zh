/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.actors.mobs.npcs.Ghost;
import com.watabou.pixeldungeon.items.food.MysteryMeat;
import com.watabou.pixeldungeon.sprites.CrabSprite;
import com.watabou.utils.Random;

public class Crab
extends Mob {
    public Crab() {
        this.name = "下水道螃蟹";
        this.spriteClass = CrabSprite.class;
        this.HT = 15;
        this.HP = 15;
        this.defenseSkill = 5;
        this.baseSpeed = 2.0f;
        this.EXP = 3;
        this.maxLvl = 9;
        this.loot = new MysteryMeat();
        this.lootChance = 0.167f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(3, 6);
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public int dr() {
        return 4;
    }

    @Override
    public String defenseVerb() {
        return "招架";
    }

    @Override
    public void die(Object cause) {
        Ghost.Quest.processSewersKill(this.pos);
        super.die(cause);
    }

    @Override
    public String description() {
        return "这些巨型螃蟹处于下水道食物链的顶端。它们行动极其迅捷，坚硬的外骨骼能抵御沉重的打击。";
    }
}

