/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.buffs.Vertigo;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.Flare;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.ArmorKit;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.keys.SkeletonKey;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.watabou.pixeldungeon.items.wands.WandOfBlink;
import com.watabou.pixeldungeon.items.wands.WandOfDisintegration;
import com.watabou.pixeldungeon.items.weapon.enchantments.Death;
import com.watabou.pixeldungeon.levels.CityBossLevel;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.KingSprite;
import com.watabou.pixeldungeon.sprites.UndeadSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import java.util.HashSet;

public class King
extends Mob {
    private static final int MAX_ARMY_SIZE = 5;
    private boolean nextPedestal;
    private static final String PEDESTAL = "pedestal";
    private static final HashSet<Class<?>> RESISTANCES = new HashSet();
    private static final HashSet<Class<?>> IMMUNITIES;

    public King() {
        this.name = Dungeon.depth == Statistics.getInt(Statistics.Type.DEEPEST) ? "King of Dwarves" : "undead King of Dwarves";
        this.spriteClass = KingSprite.class;
        this.HT = 300;
        this.HP = 300;
        this.EXP = 40;
        this.defenseSkill = 25;
        Undead.count = 0;
        this.nextPedestal = true;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PEDESTAL, this.nextPedestal);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.nextPedestal = bundle.getBoolean(PEDESTAL);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(20, 38);
    }

    @Override
    public int attackSkill(Char target) {
        return 32;
    }

    @Override
    public int dr() {
        return 14;
    }

    @Override
    public String defenseVerb() {
        return "招架";
    }

    @Override
    protected boolean getCloser(int target) {
        return this.canTryToSummon() ? super.getCloser(CityBossLevel.pedestal(this.nextPedestal)) : super.getCloser(target);
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return this.canTryToSummon() ? this.pos == CityBossLevel.pedestal(this.nextPedestal) : Level.adjacent(this.pos, enemy.pos);
    }

    private boolean canTryToSummon() {
        if (Undead.count < this.maxArmySize()) {
            Char ch = Actor.findChar(CityBossLevel.pedestal(this.nextPedestal));
            return ch == this || ch == null;
        }
        return false;
    }

    @Override
    public boolean attack(Char enemy) {
        if (this.canTryToSummon() && this.pos == CityBossLevel.pedestal(this.nextPedestal)) {
            this.summon();
            return true;
        }
        if (Actor.findChar(CityBossLevel.pedestal(this.nextPedestal)) == enemy) {
            this.nextPedestal = !this.nextPedestal;
        }
        return super.attack(enemy);
    }

    @Override
    public void die(Object cause) {
        GameScene.bossSlain();
        Dungeon.level.drop((Item)new ArmorKit(), (int)this.pos).sprite.drop();
        Dungeon.level.drop((Item)new SkeletonKey(), (int)this.pos).sprite.drop();
        super.die(cause);
        Badges.validateBossSlain();
        this.yell("你杀不死我的，" + Dungeon.hero.heroClass.title() + "...我是...永生的...");
    }

    private int maxArmySize() {
        return 1 + 5 * (this.HT - this.HP) / this.HT;
    }

    private void summon() {
        this.nextPedestal = !this.nextPedestal;
        this.sprite.centerEmitter().start(Speck.factory(5), 0.4f, 2);
        GameScene.play("snd_challenge.mp3", this.pos);
        boolean[] passable = (boolean[])Level.passable.clone();
        for (Actor actor : Actor.all()) {
            if (!(actor instanceof Char)) continue;
            passable[((Char)actor).pos] = false;
        }
        int undeadsToSummon = this.maxArmySize() - Undead.count;
        PathFinder.buildDistanceMap(this.pos, passable, undeadsToSummon);
        PathFinder.distance[this.pos] = Integer.MAX_VALUE;
        int dist = 1;
        block1: for (int i = 0; i < undeadsToSummon; ++i) {
            do {
                for (int j = 0; j < 1024; ++j) {
                    if (PathFinder.distance[j] != dist) continue;
                    Undead undead = new Undead();
                    undead.pos = j;
                    GameScene.add(undead);
                    WandOfBlink.appear(undead, j);
                    new Flare(3, 32.0f).color(0, false).show(undead.sprite, 2.0f);
                    PathFinder.distance[j] = Integer.MAX_VALUE;
                    continue block1;
                }
            } while (++dist < undeadsToSummon);
        }
        this.yell("起来吧，奴仆们！");
    }

    @Override
    public void notice() {
        super.notice();
        this.yell("你好大的胆子！");
    }

    @Override
    public String description() {
        return "矮人的末代国王以深谙生死奥秘而闻名。他说服王庭成员参与了一场号称能赐予永恒青春的仪式。最终只有他一人得到了永生——以及一支作为附赠品的不死大军。";
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
        RESISTANCES.add(WandOfDisintegration.class);
        IMMUNITIES = new HashSet();
        IMMUNITIES.add(Paralysis.class);
        IMMUNITIES.add(Vertigo.class);
    }

    public static class Undead
    extends Mob {
        public static int count = 0;
        private static final HashSet<Class<?>> IMMUNITIES = new HashSet();

        public Undead() {
            this.name = "不死矮人";
            this.spriteClass = UndeadSprite.class;
            this.HT = 28;
            this.HP = 28;
            this.defenseSkill = 15;
            this.EXP = 0;
            this.state = this.WANDERING;
        }

        @Override
        protected void onAdd() {
            ++count;
            super.onAdd();
        }

        @Override
        protected void onRemove() {
            --count;
            super.onRemove();
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange(12, 16);
        }

        @Override
        public int attackSkill(Char target) {
            return 16;
        }

        @Override
        public int attackProc(Char enemy, int damage) {
            if (Random.Int(5) == 0) {
                Buff.prolong(enemy, Paralysis.class, 1.0f);
            }
            return damage;
        }

        @Override
        public void damage(int dmg, Object src) {
            super.damage(dmg, src);
            if (src instanceof ToxicGas) {
                ((ToxicGas)src).clear(this.pos);
            }
        }

        @Override
        public void die(Object cause) {
            super.die(cause);
            if (Dungeon.visible[this.pos]) {
                GameScene.play("snd_bones.mp3", this.pos);
            }
        }

        @Override
        public int dr() {
            return 5;
        }

        @Override
        public String defenseVerb() {
            return "格挡";
        }

        @Override
        public String description() {
            return "这些由矮人国王意志唤醒的不死矮人曾是他的朝臣。如今只剩骸骨，却依然留着惊人浓密的胡须。";
        }

        @Override
        public HashSet<Class<?>> immunities() {
            return IMMUNITIES;
        }

        static {
            IMMUNITIES.add(Death.class);
            IMMUNITIES.add(Paralysis.class);
        }
    }
}

