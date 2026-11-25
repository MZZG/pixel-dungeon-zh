/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Amok;
import com.watabou.pixeldungeon.actors.buffs.Sleep;
import com.watabou.pixeldungeon.actors.buffs.Terror;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.actors.mobs.npcs.Imp;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.watabou.pixeldungeon.sprites.GolemSprite;
import com.watabou.utils.Random;
import java.util.HashSet;

public class Golem
extends Mob {
    private static final HashSet<Class<?>> RESISTANCES = new HashSet();
    private static final HashSet<Class<?>> IMMUNITIES;

    public Golem() {
        this.name = "魔像";
        this.spriteClass = GolemSprite.class;
        this.HT = 85;
        this.HP = 85;
        this.defenseSkill = 18;
        this.EXP = 12;
        this.maxLvl = 22;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(20, 40);
    }

    @Override
    public int attackSkill(Char target) {
        return 28;
    }

    @Override
    protected float attackDelay() {
        return 1.5f;
    }

    @Override
    public int dr() {
        return 12;
    }

    @Override
    public String defenseVerb() {
        return "格挡";
    }

    @Override
    public void die(Object cause) {
        Imp.Quest.process(this);
        super.die(cause);
    }

    @Override
    public String description() {
        return "矮人试图将机械工艺与新掌握的元素束缚之力结合起来，用大地之灵作为魔像机械躯体的\"灵魂\"——原本以为这样最容易控制，但仪式中哪怕最微小的失误也可能引发暴走。";
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    static {
        RESISTANCES.add(ScrollOfPsionicBlast.class);
        IMMUNITIES = new HashSet();
        IMMUNITIES.add(Amok.class);
        IMMUNITIES.add(Terror.class);
        IMMUNITIES.add(Sleep.class);
    }
}

