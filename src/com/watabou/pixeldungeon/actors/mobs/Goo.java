/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Ooze;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.LloydsBeacon;
import com.watabou.pixeldungeon.items.keys.SkeletonKey;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.watabou.pixeldungeon.items.weapon.enchantments.Death;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.SewerBossLevel;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.GooSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import java.util.HashSet;

public class Goo
extends Mob {
    private static final float PUMP_UP_DELAY = 2.0f;
    private boolean pumpedUp;
    private boolean jumped;
    private static final HashSet<Class<?>> RESISTANCES = new HashSet();

    public Goo() {
        this.name = Dungeon.depth == Statistics.getInt(Statistics.Type.DEEPEST) ? "Goo" : "spawn of Goo";
        this.HT = 80;
        this.HP = 80;
        this.EXP = 10;
        this.defenseSkill = 12;
        this.spriteClass = GooSprite.class;
        this.loot = new LloydsBeacon();
        this.lootChance = 0.333f;
        this.pumpedUp = false;
        this.jumped = false;
    }

    @Override
    public int damageRoll() {
        if (this.pumpedUp) {
            return Random.NormalIntRange(5, 30);
        }
        return Random.NormalIntRange(2, 12);
    }

    @Override
    public int attackSkill(Char target) {
        return this.pumpedUp && !this.jumped ? 30 : 15;
    }

    @Override
    public int dr() {
        return 2;
    }

    @Override
    public boolean act() {
        if (Level.water[this.pos] && this.HP < this.HT) {
            this.sprite.emitter().burst(Speck.factory(0), 1);
            ++this.HP;
        }
        return super.act();
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return this.pumpedUp ? this.distance(enemy) <= 2 : super.canAttack(enemy);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (Random.Int(3) == 0) {
            Buff.affect(enemy, Ooze.class);
            enemy.sprite.burst(0, 5);
        }
        return damage;
    }

    @Override
    protected boolean doAttack(final Char enemy) {
        if (this.pumpedUp) {
            if (Level.adjacent(this.pos, enemy.pos)) {
                this.jumped = false;
                return super.doAttack(enemy);
            }
            this.jumped = true;
            if (Ballistica.cast(this.pos, enemy.pos, false, true) == enemy.pos) {
                final int dest = Ballistica.trace[Ballistica.distance - 2];
                Callback afterJump = new Callback(){

                    @Override
                    public void call() {
                        Goo.this.move(dest);
                        Dungeon.level.mobPress(Goo.this);
                        Goo.super.doAttack(enemy);
                    }
                };
                if (Dungeon.visible[this.pos] || Dungeon.visible[dest]) {
                    this.sprite.jump(this.pos, dest, afterJump);
                    return false;
                }
                afterJump.call();
                return true;
            }
            this.sprite.idle();
            this.pumpedUp = false;
            return true;
        }
        if (Random.Int(3) > 0) {
            return super.doAttack(enemy);
        }
        this.pumpedUp = true;
        this.spend(2.0f);
        ((GooSprite)this.sprite).pumpUp();
        if (Dungeon.visible[this.pos]) {
            this.sprite.showStatus(0xFF0000, "!!!", new Object[0]);
            GLog.n("粘咕正在不断地膨胀蓄力！", new Object[0]);
        }
        return true;
    }

    @Override
    public boolean attack(Char enemy) {
        boolean result = super.attack(enemy);
        this.pumpedUp = false;
        return result;
    }

    @Override
    protected boolean getCloser(int target) {
        this.pumpedUp = false;
        return super.getCloser(target);
    }

    @Override
    public void move(int step) {
        ((SewerBossLevel)Dungeon.level).seal();
        super.move(step);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        ((SewerBossLevel)Dungeon.level).unseal();
        GameScene.bossSlain();
        Dungeon.level.drop((Item)new SkeletonKey(), (int)this.pos).sprite.drop();
        Badges.validateBossSlain();
        this.yell("咕...咕...");
    }

    @Override
    public void notice() {
        super.notice();
        this.yell("咕-咕！");
    }

    @Override
    public String description() {
        return "人们对粘咕知之甚少。它甚至可能根本不是生物，只是下水道中各种污浊物质聚合后诞生了原始自我意识的产物。";
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    static {
        RESISTANCES.add(ToxicGas.class);
        RESISTANCES.add(Death.class);
        RESISTANCES.add(ScrollOfPsionicBlast.class);
    }
}

