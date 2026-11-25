/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.Pushing;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.potions.PotionOfHealing;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.features.Door;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.SwarmSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import java.util.ArrayList;

public class Swarm
extends Mob {
    private static final float SPLIT_DELAY = 1.0f;
    int generation;
    private static final String GENERATION = "generation";

    public Swarm() {
        this.name = "蝇群";
        this.spriteClass = SwarmSprite.class;
        this.HT = 80;
        this.HP = 80;
        this.defenseSkill = 5;
        this.maxLvl = 10;
        this.flying = true;
        this.generation = 0;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(GENERATION, this.generation);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.generation = bundle.getInt(GENERATION);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(1, 4);
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        if (this.HP >= damage + 2) {
            int[] neighbours;
            ArrayList<Integer> candidates = new ArrayList<Integer>();
            boolean[] passable = Level.passable;
            for (int n : neighbours = new int[]{this.pos + 1, this.pos - 1, this.pos + 32, this.pos - 32}) {
                if (!passable[n] || Actor.findChar(n) != null) continue;
                candidates.add(n);
            }
            if (candidates.size() > 0) {
                Swarm clone = this.split();
                clone.HP = (this.HP - damage) / 2;
                clone.pos = (Integer)Random.element(candidates);
                clone.state = clone.HUNTING;
                if (Dungeon.level.map[clone.pos] == 5) {
                    Door.enter(clone.pos);
                }
                GameScene.add(clone, 1.0f);
                Actor.addDelayed(new Pushing(clone, this.pos, clone.pos), -1.0f);
                this.HP -= clone.HP;
            }
        }
        return damage;
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public String defenseVerb() {
        return "闪避";
    }

    private Swarm split() {
        Swarm clone = new Swarm();
        clone.generation = this.generation + 1;
        if (this.buff(Burning.class) != null) {
            Buff.affect(clone, Burning.class).reignite(clone);
        }
        if (this.buff(Poison.class) != null) {
            Buff.affect(clone, Poison.class).set(2.0f);
        }
        return clone;
    }

    @Override
    protected void dropLoot() {
        if (Random.Int(5 * (this.generation + 1)) == 0) {
            Dungeon.level.drop((Item)new PotionOfHealing(), (int)this.pos).sprite.drop();
        }
    }

    @Override
    public String description() {
        return "致命的飞蝇群愤怒地发出嗡嗡声。每次非魔法攻击都会把它分割成两群更小但同样危险的蝇群。";
    }
}

