/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Amok;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Sleep;
import com.watabou.pixeldungeon.actors.buffs.Terror;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.effects.Flare;
import com.watabou.pixeldungeon.effects.Wound;
import com.watabou.pixeldungeon.items.Generator;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import java.util.HashSet;

public abstract class Mob
extends Char {
    private static final String TXT_DIED = "你听到远处有什么东西死去了";
    protected static final String TXT_RAGE = "#$%^";
    protected static final String TXT_EXP = "%+dEXP";
    public AiState SLEEPEING = new Sleeping();
    public AiState HUNTING = new Hunting();
    public AiState WANDERING = new Wandering();
    public AiState FLEEING = new Fleeing();
    public AiState PASSIVE = new Passive();
    public AiState state = this.SLEEPEING;
    public Class<? extends CharSprite> spriteClass;
    protected int target = -1;
    protected int defenseSkill = 0;
    protected int EXP = 1;
    protected int maxLvl = 30;
    protected Char enemy;
    protected boolean enemySeen;
    protected boolean alerted = false;
    protected static final float TIME_TO_WAKE_UP = 1.0f;
    public boolean hostile = true;
    private static final String STATE = "state";
    private static final String TARGET = "target";
    protected Object loot = null;
    protected float lootChance = 0.0f;

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        if (this.state == this.SLEEPEING) {
            bundle.put(STATE, "SLEEPING");
        } else if (this.state == this.WANDERING) {
            bundle.put(STATE, "WANDERING");
        } else if (this.state == this.HUNTING) {
            bundle.put(STATE, "HUNTING");
        } else if (this.state == this.FLEEING) {
            bundle.put(STATE, "FLEEING");
        } else if (this.state == this.PASSIVE) {
            bundle.put(STATE, "PASSIVE");
        }
        bundle.put(TARGET, this.target);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        String state = bundle.getString(STATE);
        if (state.equals("SLEEPING")) {
            this.state = this.SLEEPEING;
        } else if (state.equals("WANDERING")) {
            this.state = this.WANDERING;
        } else if (state.equals("HUNTING")) {
            this.state = this.HUNTING;
        } else if (state.equals("FLEEING")) {
            this.state = this.FLEEING;
        } else if (state.equals("PASSIVE")) {
            this.state = this.PASSIVE;
        }
        this.target = bundle.getInt(TARGET);
    }

    public CharSprite sprite() {
        CharSprite sprite = null;
        try {
            sprite = ClassReflection.newInstance(this.spriteClass);
        }
        catch (Exception exception) {
            // empty catch block
        }
        return sprite;
    }

    @Override
    protected boolean act() {
        super.act();
        boolean justAlerted = this.alerted;
        this.alerted = false;
        this.sprite.hideAlert();
        if (this.paralysed) {
            this.enemySeen = false;
            this.spend(1.0f);
            return true;
        }
        this.enemy = this.chooseEnemy();
        boolean enemyInFOV = this.enemy != null && this.enemy.isAlive() && Level.fieldOfView[this.enemy.pos] && this.enemy.invisible <= 0;
        return this.state.act(enemyInFOV, justAlerted);
    }

    protected Char chooseEnemy() {
        Char source;
        Terror terror;
        if (this.buff(Amok.class) != null && (this.enemy == Dungeon.hero || this.enemy == null)) {
            HashSet<Mob> enemies = new HashSet<Mob>();
            for (Mob mob : Dungeon.level.mobs) {
                if (mob == this || !Level.fieldOfView[mob.pos]) continue;
                enemies.add(mob);
            }
            if (enemies.size() > 0) {
                return (Char)Random.element(enemies);
            }
        }
        if ((terror = this.buff(Terror.class)) != null && (source = (Char)Actor.findById(terror.object)) != null) {
            return source;
        }
        return this.enemy != null && this.enemy.isAlive() ? this.enemy : Dungeon.hero;
    }

    protected boolean moveSprite(int from, int to) {
        if (this.sprite.isVisible() && (Dungeon.visible[from] || Dungeon.visible[to])) {
            this.sprite.move(from, to);
            return true;
        }
        this.sprite.place(to);
        return true;
    }

    @Override
    public void add(Buff buff) {
        super.add(buff);
        if (buff instanceof Amok) {
            if (this.sprite != null) {
                this.sprite.showStatus(0xFF0000, TXT_RAGE, new Object[0]);
            }
            this.state = this.HUNTING;
        } else if (buff instanceof Terror) {
            this.state = this.FLEEING;
        } else if (buff instanceof Sleep) {
            if (this.sprite != null) {
                new Flare(4, 32.0f).color(0x44FFFF, true).show(this.sprite, 2.0f);
            }
            this.state = this.SLEEPEING;
            this.postpone(1.5f);
        }
    }

    @Override
    public void remove(Buff buff) {
        super.remove(buff);
        if (buff instanceof Terror) {
            this.sprite.showStatus(0xFF0000, TXT_RAGE, new Object[0]);
            this.state = this.HUNTING;
        }
    }

    protected boolean canAttack(Char enemy) {
        return Level.adjacent(this.pos, enemy.pos) && !this.isCharmedBy(enemy);
    }

    protected boolean getCloser(int target) {
        if (this.rooted) {
            return false;
        }
        int step = Dungeon.findPath(this, this.pos, target, Level.passable, Level.fieldOfView);
        if (step != -1) {
            this.move(step);
            return true;
        }
        return false;
    }

    protected boolean getFurther(int target) {
        int step = Dungeon.flee(this, this.pos, target, Level.passable, Level.fieldOfView);
        if (step != -1) {
            this.move(step);
            return true;
        }
        return false;
    }

    @Override
    public void move(int step) {
        super.move(step);
        if (!this.flying) {
            Dungeon.level.mobPress(this);
        }
    }

    protected float attackDelay() {
        return 1.0f;
    }

    protected boolean doAttack(Char enemy) {
        boolean visible = Dungeon.visible[this.pos];
        if (visible) {
            this.sprite.attack(enemy.pos);
        } else {
            this.attack(enemy);
        }
        this.spend(this.attackDelay());
        return !visible;
    }

    @Override
    public void onAttackComplete() {
        this.attack(this.enemy);
        super.onAttackComplete();
    }

    @Override
    public int defenseSkill(Char enemy) {
        return this.enemySeen && !this.paralysed ? this.defenseSkill : 0;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        if (!this.enemySeen && enemy == Dungeon.hero && ((Hero)enemy).subClass == HeroSubClass.ASSASSIN) {
            damage += Random.Int(1, damage);
            Wound.hit(this);
        }
        return damage;
    }

    public void aggro(Char ch) {
        this.enemy = ch;
    }

    @Override
    public void damage(int dmg, Object src) {
        Terror.recover(this);
        if (this.state == this.SLEEPEING) {
            this.state = this.WANDERING;
        }
        this.alerted = true;
        super.damage(dmg, src);
    }

    @Override
    public void destroy() {
        super.destroy();
        Dungeon.level.mobs.remove(this);
        if (Dungeon.hero.isAlive()) {
            if (this.hostile) {
                Statistics.add(Statistics.Type.SLAIN, 1);
                Badges.validateMonstersSlain();
                Statistics.qualifiedForNoKilling = false;
                if (Dungeon.nightMode) {
                    Statistics.add(Statistics.Type.NIGHT, 1);
                } else {
                    Statistics.set(Statistics.Type.NIGHT, 0);
                }
                Badges.validateNightHunter();
            }
            int exp = this.exp();
            if (Dungeon.hero.lvl <= this.maxLvl && exp > 0) {
                Dungeon.hero.sprite.showStatus(65280, TXT_EXP, this.EXP);
                Dungeon.hero.earnExp(this.EXP);
            }
        }
    }

    public int exp() {
        return Dungeon.hero.lvl <= this.maxLvl ? this.EXP : 0;
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        if (Dungeon.hero.lvl <= this.maxLvl + 2) {
            this.dropLoot();
        }
        if (Dungeon.hero.isAlive() && !Dungeon.visible[this.pos]) {
            GLog.i(TXT_DIED, new Object[0]);
        }
    }

    protected void dropLoot() {
        if (this.loot != null && Random.Float() < this.lootChance) {
            Item item = this.loot instanceof Generator.Category ? Generator.random((Generator.Category)((Object)this.loot)) : (this.loot instanceof Class ? Generator.random((Class)this.loot) : (Item)this.loot);
            Dungeon.level.drop((Item)item, (int)this.pos).sprite.drop();
        }
    }

    public boolean reset() {
        return false;
    }

    public void beckon(int cell) {
        this.notice();
        if (this.state != this.HUNTING) {
            this.state = this.WANDERING;
        }
        this.target = cell;
    }

    public String description() {
        return "暂无详细描述。";
    }

    public void notice() {
        this.sprite.showAlert();
    }

    public void yell(String str) {
        GLog.n("%s：\"%s\" ", this.name, str);
    }

    private class Passive
    implements AiState {
        public static final String TAG = "PASSIVE";

        private Passive() {
        }

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            Mob.this.enemySeen = false;
            Mob.this.spend(1.0f);
            return true;
        }

        @Override
        public String status() {
            return Utils.format("这只%s正处于被动状态", Mob.this.name);
        }
    }

    protected class Fleeing
    implements AiState {
        public static final String TAG = "FLEEING";

        protected Fleeing() {
        }

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            Mob.this.enemySeen = enemyInFOV;
            if (enemyInFOV) {
                Mob.this.target = Mob.this.enemy.pos;
            }
            int oldPos = Mob.this.pos;
            if (Mob.this.target != -1 && Mob.this.getFurther(Mob.this.target)) {
                Mob.this.spend(1.0f / Mob.this.speed());
                return Mob.this.moveSprite(oldPos, Mob.this.pos);
            }
            Mob.this.spend(1.0f);
            this.nowhereToRun();
            return true;
        }

        protected void nowhereToRun() {
        }

        @Override
        public String status() {
            return Utils.format("这只%s正在逃跑", Mob.this.name);
        }
    }

    private class Hunting
    implements AiState {
        public static final String TAG = "HUNTING";

        private Hunting() {
        }

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            Mob.this.enemySeen = enemyInFOV;
            if (enemyInFOV && Mob.this.canAttack(Mob.this.enemy)) {
                return Mob.this.doAttack(Mob.this.enemy);
            }
            if (enemyInFOV) {
                Mob.this.target = Mob.this.enemy.pos;
            }
            int oldPos = Mob.this.pos;
            if (Mob.this.target != -1 && Mob.this.getCloser(Mob.this.target)) {
                Mob.this.spend(1.0f / Mob.this.speed());
                return Mob.this.moveSprite(oldPos, Mob.this.pos);
            }
            Mob.this.spend(1.0f);
            Mob.this.state = Mob.this.WANDERING;
            Mob.this.target = Dungeon.level.randomDestination();
            return true;
        }

        @Override
        public String status() {
            return Utils.format("这只%s正在猎杀你", Mob.this.name);
        }
    }

    private class Wandering
    implements AiState {
        public static final String TAG = "WANDERING";

        private Wandering() {
        }

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            if (enemyInFOV && (justAlerted || Random.Int(Mob.this.distance(Mob.this.enemy) / 2 + Mob.this.enemy.stealth()) == 0)) {
                Mob.this.enemySeen = true;
                Mob.this.notice();
                Mob.this.state = Mob.this.HUNTING;
                Mob.this.target = Mob.this.enemy.pos;
            } else {
                Mob.this.enemySeen = false;
                int oldPos = Mob.this.pos;
                if (Mob.this.target != -1 && Mob.this.getCloser(Mob.this.target)) {
                    Mob.this.spend(1.0f / Mob.this.speed());
                    return Mob.this.moveSprite(oldPos, Mob.this.pos);
                }
                Mob.this.target = Dungeon.level.randomDestination();
                Mob.this.spend(1.0f);
            }
            return true;
        }

        @Override
        public String status() {
            return Utils.format("这只%s正在游荡", Mob.this.name);
        }
    }

    private class Sleeping
    implements AiState {
        public static final String TAG = "SLEEPING";

        private Sleeping() {
        }

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            if (enemyInFOV && Random.Int(Mob.this.distance(Mob.this.enemy) + Mob.this.enemy.stealth() + (Mob.this.enemy.flying ? 2 : 0)) == 0) {
                Mob.this.enemySeen = true;
                Mob.this.notice();
                Mob.this.state = Mob.this.HUNTING;
                Mob.this.target = Mob.this.enemy.pos;
                if (Dungeon.isChallenged(16)) {
                    for (Mob mob : Dungeon.level.mobs) {
                        if (mob == Mob.this) continue;
                        mob.beckon(Mob.this.target);
                    }
                }
                Mob.this.spend(1.0f);
            } else {
                Mob.this.enemySeen = false;
                Mob.this.spend(1.0f);
            }
            return true;
        }

        @Override
        public String status() {
            return Utils.format("这只%s正在沉睡", Mob.this.name);
        }
    }

    public static interface AiState {
        public boolean act(boolean var1, boolean var2);

        public String status();
    }
}

