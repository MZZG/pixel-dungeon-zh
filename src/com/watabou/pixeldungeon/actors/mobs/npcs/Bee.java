/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs.npcs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.actors.mobs.npcs.NPC;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.sprites.BeeSprite;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import java.util.HashSet;

public class Bee
extends NPC {
    private int level;
    private static final String LEVEL = "level";
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet();

    public Bee() {
        this.name = "金色蜜蜂";
        this.spriteClass = BeeSprite.class;
        this.viewDistance = 4;
        this.WANDERING = new Wandering();
        this.flying = true;
        this.state = this.WANDERING;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEVEL, this.level);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.spawn(bundle.getInt(LEVEL));
    }

    public void spawn(int level) {
        this.level = level;
        this.HT = (3 + level) * 5;
        this.defenseSkill = 9 + level;
    }

    @Override
    public int attackSkill(Char target) {
        return this.defenseSkill;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(this.HT / 10, this.HT / 4);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (enemy instanceof Mob) {
            ((Mob)enemy).aggro(this);
        }
        return damage;
    }

    @Override
    protected boolean act() {
        --this.HP;
        if (this.HP <= 0) {
            this.die(null);
            return true;
        }
        return super.act();
    }

    @Override
    protected Char chooseEnemy() {
        if (this.enemy == null || !this.enemy.isAlive()) {
            HashSet<Mob> enemies = new HashSet<Mob>();
            for (Mob mob : Dungeon.level.mobs) {
                if (!mob.hostile || !Level.fieldOfView[mob.pos]) continue;
                enemies.add(mob);
            }
            return enemies.size() > 0 ? (Mob)Random.element(enemies) : null;
        }
        return this.enemy;
    }

    @Override
    public String description() {
        return "尽管体型娇小，金色蜜蜂仍会竭尽全力保护主人，只是它们的寿命并不长。";
    }

    @Override
    public void interact() {
        int curPos = this.pos;
        this.moveSprite(this.pos, Dungeon.hero.pos);
        this.move(Dungeon.hero.pos);
        Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
        Dungeon.hero.move(curPos);
        Dungeon.hero.spend(1.0f / Dungeon.hero.speed());
        Dungeon.hero.busy();
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    static {
        IMMUNITIES.add(Poison.class);
    }

    private class Wandering
    implements Mob.AiState {
        private Wandering() {
        }

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            if (enemyInFOV) {
                Bee.this.enemySeen = true;
                Bee.this.notice();
                Bee.this.state = Bee.this.HUNTING;
                Bee.this.target = ((Bee)Bee.this).enemy.pos;
            } else {
                Bee.this.enemySeen = false;
                int oldPos = Bee.this.pos;
                if (Bee.this.getCloser(Dungeon.hero.pos)) {
                    Bee.this.spend(1.0f / Bee.this.speed());
                    return Bee.this.moveSprite(oldPos, Bee.this.pos);
                }
                Bee.this.spend(1.0f);
            }
            return true;
        }

        @Override
        public String status() {
            return Utils.format("这只%s正在游荡", Bee.this.name);
        }
    }
}

