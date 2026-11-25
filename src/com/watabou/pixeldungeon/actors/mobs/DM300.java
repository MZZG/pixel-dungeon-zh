/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.noosa.Camera;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.effects.particles.ElmoParticle;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.keys.SkeletonKey;
import com.watabou.pixeldungeon.items.rings.RingOfThorns;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.watabou.pixeldungeon.items.weapon.enchantments.Death;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.DM300Sprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Random;
import java.util.HashSet;

public class DM300
extends Mob {
    private static final HashSet<Class<?>> RESISTANCES = new HashSet();
    private static final HashSet<Class<?>> IMMUNITIES;

    public DM300() {
        this.name = Dungeon.depth == Statistics.getInt(Statistics.Type.DEEPEST) ? "DM-300" : "DM-350";
        this.spriteClass = DM300Sprite.class;
        this.HT = 200;
        this.HP = 200;
        this.EXP = 30;
        this.defenseSkill = 18;
        this.loot = new RingOfThorns().random();
        this.lootChance = 0.333f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(18, 24);
    }

    @Override
    public int attackSkill(Char target) {
        return 28;
    }

    @Override
    public int dr() {
        return 10;
    }

    @Override
    public boolean act() {
        GameScene.add(Blob.seed(this.pos, 30, ToxicGas.class));
        return super.act();
    }

    @Override
    public void move(int step) {
        Char ch;
        int[] cells;
        int cell;
        super.move(step);
        if (Dungeon.level.map[step] == 23 && this.HP < this.HT) {
            this.HP += Random.Int(1, this.HT - this.HP);
            this.sprite.emitter().burst(ElmoParticle.FACTORY, 5);
            if (Dungeon.visible[step] && Dungeon.hero.isAlive()) {
                GLog.n("DM-300 正在自我修复！", new Object[0]);
            }
        }
        if (Dungeon.visible[cell = (cells = new int[]{step - 1, step + 1, step - 32, step + 32, step - 1 - 32, step - 1 + 32, step + 1 - 32, step + 1 + 32})[Random.Int(cells.length)]]) {
            CellEmitter.get(cell).start(Speck.factory(8), 0.07f, 10);
            Camera.main.shake(3.0f, 0.7f);
            GameScene.play("snd_rocks.mp3", this.pos);
            if (Level.water[cell]) {
                GameScene.ripple(cell);
            } else if (Dungeon.level.map[cell] == 1) {
                Level.set(cell, 24);
                GameScene.updateMap(cell);
            }
        }
        if ((ch = Actor.findChar(cell)) != null && ch != this) {
            Buff.prolong(ch, Paralysis.class, 2.0f);
        }
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        GameScene.bossSlain();
        Dungeon.level.drop((Item)new SkeletonKey(), (int)this.pos).sprite.drop();
        Badges.validateBossSlain();
        this.yell("任务失败。正在关机。");
    }

    @Override
    public void notice() {
        super.notice();
        this.yell("检测到未授权人员。");
    }

    @Override
    public String description() {
        return "这台机械由矮人在几个世纪前制造。后来矮人开始用魔像、元素甚至恶魔替代机械，最终导致了文明的衰落。DM-300 及同类机械原本常用于建造与采矿，有时也用于城市防卫。";
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
        RESISTANCES.add(Death.class);
        RESISTANCES.add(ScrollOfPsionicBlast.class);
        IMMUNITIES = new HashSet();
        IMMUNITIES.add(ToxicGas.class);
    }
}

