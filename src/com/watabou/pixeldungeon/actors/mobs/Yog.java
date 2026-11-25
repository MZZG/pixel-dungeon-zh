/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.Fire;
import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.actors.buffs.Amok;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.actors.buffs.Charm;
import com.watabou.pixeldungeon.actors.buffs.Ooze;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.buffs.Sleep;
import com.watabou.pixeldungeon.actors.buffs.Terror;
import com.watabou.pixeldungeon.actors.buffs.Vertigo;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.Pushing;
import com.watabou.pixeldungeon.effects.particles.ShadowParticle;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.keys.SkeletonKey;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.watabou.pixeldungeon.items.weapon.enchantments.Death;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.BurningFistSprite;
import com.watabou.pixeldungeon.sprites.LarvaSprite;
import com.watabou.pixeldungeon.sprites.RottingFistSprite;
import com.watabou.pixeldungeon.sprites.YogSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Random;
import java.util.ArrayList;
import java.util.HashSet;

public class Yog
extends Mob {
    private static final String TXT_DESC = "Yog-Dzewa 是一位来自混沌领域的强大古神。一个世纪前，古代矮人勉强赢得了对抗其恶魔军团的战争，却无法杀死这位古神本身，于是将它封印在都城下方的大厅之中，以为它再也无力东山再起。";
    private static int fistsCount = 0;
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet();

    public Yog() {
        this.name = Dungeon.depth == Statistics.getInt(Statistics.Type.DEEPEST) ? "Yog-Dzewa" : "echo of Yog-Dzewa";
        this.spriteClass = YogSprite.class;
        this.HT = 300;
        this.HP = 300;
        this.EXP = 50;
        this.state = this.PASSIVE;
    }

    public void spawnFists() {
        RottingFist fist1 = new RottingFist();
        BurningFist fist2 = new BurningFist();
        do {
            fist1.pos = this.pos + Level.NEIGHBOURS8[Random.Int(8)];
            fist2.pos = this.pos + Level.NEIGHBOURS8[Random.Int(8)];
        } while (!Level.passable[fist1.pos] || !Level.passable[fist2.pos] || fist1.pos == fist2.pos);
        GameScene.add(fist1);
        GameScene.add(fist2);
    }

    @Override
    public void damage(int dmg, Object src) {
        if (fistsCount > 0) {
            for (Mob mob : Dungeon.level.mobs) {
                if (!(mob instanceof BurningFist) && !(mob instanceof RottingFist)) continue;
                mob.beckon(this.pos);
            }
            dmg >>= fistsCount;
        }
        super.damage(dmg, src);
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        ArrayList<Integer> spawnPoints = new ArrayList<Integer>();
        for (int i = 0; i < Level.NEIGHBOURS8.length; ++i) {
            int p = this.pos + Level.NEIGHBOURS8[i];
            if (Actor.findChar(p) != null || !Level.passable[p] && !Level.avoid[p]) continue;
            spawnPoints.add(p);
        }
        if (spawnPoints.size() > 0) {
            Larva larva = new Larva();
            larva.pos = (Integer)Random.element(spawnPoints);
            GameScene.add(larva);
            Actor.addDelayed(new Pushing(larva, this.pos, larva.pos), -1.0f);
        }
        return super.defenseProc(enemy, damage);
    }

    @Override
    public void beckon(int cell) {
    }

    @Override
    public void die(Object cause) {
        for (Object obj : (Iterable)Dungeon.level.mobs.clone()) {
            Mob mob = (Mob)obj;
            if (!(mob instanceof BurningFist) && !(mob instanceof RottingFist)) continue;
            mob.die(cause);
        }
        GameScene.bossSlain();
        Dungeon.level.drop((Item)new SkeletonKey(), (int)this.pos).sprite.drop();
        super.die(cause);
        this.yell("...");
    }

    @Override
    public void notice() {
        super.notice();
        this.yell("希望不过是幻影...");
    }

    @Override
    public String description() {
        return TXT_DESC;
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    static {
        IMMUNITIES.add(Death.class);
        IMMUNITIES.add(Terror.class);
        IMMUNITIES.add(Amok.class);
        IMMUNITIES.add(Charm.class);
        IMMUNITIES.add(Sleep.class);
        IMMUNITIES.add(Burning.class);
        IMMUNITIES.add(ToxicGas.class);
        IMMUNITIES.add(ScrollOfPsionicBlast.class);
    }

    public static class Larva
    extends Mob {
        public Larva() {
            this.name = "古神幼虫";
            this.spriteClass = LarvaSprite.class;
            this.HT = 25;
            this.HP = 25;
            this.defenseSkill = 20;
            this.EXP = 0;
            this.state = this.HUNTING;
        }

        @Override
        public int attackSkill(Char target) {
            return 30;
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange(15, 20);
        }

        @Override
        public int dr() {
            return 8;
        }

        @Override
        public String description() {
            return Yog.TXT_DESC;
        }
    }

    public static class BurningFist
    extends Mob {
        private static final HashSet<Class<?>> RESISTANCES = new HashSet();
        private static final HashSet<Class<?>> IMMUNITIES;

        public BurningFist() {
            this.name = "燃烧之拳";
            this.spriteClass = BurningFistSprite.class;
            this.HT = 200;
            this.HP = 200;
            this.defenseSkill = 25;
            this.EXP = 0;
            this.state = this.WANDERING;
            fistsCount++;
        }

        @Override
        public void die(Object cause) {
            super.die(cause);
            fistsCount--;
        }

        @Override
        public int attackSkill(Char target) {
            return 36;
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange(20, 32);
        }

        @Override
        public int dr() {
            return 15;
        }

        @Override
        protected boolean canAttack(Char enemy) {
            return Ballistica.cast(this.pos, enemy.pos, false, true) == enemy.pos;
        }

        @Override
        public boolean attack(Char enemy) {
            if (!Level.adjacent(this.pos, enemy.pos)) {
                this.spend(this.attackDelay());
                if (BurningFist.hit(this, enemy, true)) {
                    int dmg = this.damageRoll();
                    enemy.damage(dmg, this);
                    enemy.sprite.bloodBurstA(this.sprite.center(), dmg);
                    enemy.sprite.flash();
                    if (!enemy.isAlive() && enemy == Dungeon.hero) {
                        Dungeon.fail(Utils.format("在第 %2$d 层被%1$s杀害", this.name, Dungeon.depth));
                        GLog.n("%s杀害了你...", this.name);
                    }
                    return true;
                }
                enemy.sprite.showStatus(0xFFFF00, enemy.defenseVerb(), new Object[0]);
                return false;
            }
            return super.attack(enemy);
        }

        @Override
        public boolean act() {
            for (int i = 0; i < Level.NEIGHBOURS9.length; ++i) {
                GameScene.add(Blob.seed(this.pos + Level.NEIGHBOURS9[i], 2, Fire.class));
            }
            return super.act();
        }

        @Override
        public String description() {
            return Yog.TXT_DESC;
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
            RESISTANCES.add(ToxicGas.class);
            RESISTANCES.add(Death.class);
            RESISTANCES.add(ScrollOfPsionicBlast.class);
            IMMUNITIES = new HashSet();
            IMMUNITIES.add(Amok.class);
            IMMUNITIES.add(Sleep.class);
            IMMUNITIES.add(Terror.class);
            IMMUNITIES.add(Burning.class);
        }
    }

    public static class RottingFist
    extends Mob {
        private static final int REGENERATION = 4;
        private static final HashSet<Class<?>> RESISTANCES = new HashSet();
        private static final HashSet<Class<?>> IMMUNITIES;

        public RottingFist() {
            this.name = "腐烂之拳";
            this.spriteClass = RottingFistSprite.class;
            this.HT = 300;
            this.HP = 300;
            this.defenseSkill = 25;
            this.EXP = 0;
            this.state = this.WANDERING;
            fistsCount++;
        }

        @Override
        public void die(Object cause) {
            super.die(cause);
            fistsCount--;
        }

        @Override
        public int attackSkill(Char target) {
            return 36;
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange(24, 36);
        }

        @Override
        public int dr() {
            return 15;
        }

        @Override
        public int attackProc(Char enemy, int damage) {
            if (Random.Int(3) == 0) {
                Buff.affect(enemy, Ooze.class);
                enemy.sprite.burst(-16777216, 5);
            }
            return damage;
        }

        @Override
        public boolean act() {
            if (Level.water[this.pos] && this.HP < this.HT) {
                this.sprite.emitter().burst(ShadowParticle.UP, 2);
                this.HP += 4;
            }
            return super.act();
        }

        @Override
        public String description() {
            return Yog.TXT_DESC;
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
            RESISTANCES.add(ToxicGas.class);
            RESISTANCES.add(Death.class);
            RESISTANCES.add(ScrollOfPsionicBlast.class);
            IMMUNITIES = new HashSet();
            IMMUNITIES.add(Amok.class);
            IMMUNITIES.add(Sleep.class);
            IMMUNITIES.add(Terror.class);
            IMMUNITIES.add(Poison.class);
            IMMUNITIES.add(Vertigo.class);
        }
    }
}

