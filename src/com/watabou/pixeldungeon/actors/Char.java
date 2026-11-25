/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.watabou.noosa.Camera;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.buffs.Amok;
import com.watabou.pixeldungeon.actors.buffs.Bleeding;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.actors.buffs.Charm;
import com.watabou.pixeldungeon.actors.buffs.Cripple;
import com.watabou.pixeldungeon.actors.buffs.Frost;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.buffs.Levitation;
import com.watabou.pixeldungeon.actors.buffs.Light;
import com.watabou.pixeldungeon.actors.buffs.MindVision;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.buffs.Roots;
import com.watabou.pixeldungeon.actors.buffs.Shadows;
import com.watabou.pixeldungeon.actors.buffs.Sleep;
import com.watabou.pixeldungeon.actors.buffs.Slow;
import com.watabou.pixeldungeon.actors.buffs.Speed;
import com.watabou.pixeldungeon.actors.buffs.Terror;
import com.watabou.pixeldungeon.actors.buffs.Vertigo;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.actors.mobs.Bestiary;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.particles.PoisonParticle;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.features.Door;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;
import java.util.HashSet;

public abstract class Char
extends Actor {
    protected static final String TXT_HIT = "%s击中了%s";
    protected static final String TXT_KILL = "%s杀害了你...";
    protected static final String TXT_DEFEAT = "%s击败了%s";
    private static final String TXT_YOU_MISSED = "%s%s了你的攻击";
    private static final String TXT_SMB_MISSED = "%s%s了%s的攻击";
    private static final String TXT_OUT_OF_PARALYSIS = "剧痛使%s从麻痹中惊醒";
    public int pos = 0;
    public CharSprite sprite;
    public String name = "mob";
    public int HT;
    public int HP;
    protected float baseSpeed = 1.0f;
    public boolean paralysed = false;
    public boolean rooted = false;
    public boolean flying = false;
    public boolean losBlocking = false;
    public int invisible = 0;
    public int viewDistance = 8;
    private HashSet<Buff> buffs = new HashSet();
    private static final String POS = "pos";
    private static final String TAG_HP = "HP";
    private static final String TAG_HT = "HT";
    private static final String BUFFS = "buffs";
    private static final HashSet<Class<?>> EMPTY = new HashSet();

    @Override
    protected boolean act() {
        Dungeon.level.updateFieldOfView(this);
        return false;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(POS, this.pos);
        bundle.put(TAG_HP, this.HP);
        bundle.put(TAG_HT, this.HT);
        bundle.put(BUFFS, this.buffs);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.pos = bundle.getInt(POS);
        this.HP = bundle.getInt(TAG_HP);
        this.HT = bundle.getInt(TAG_HT);
        for (Bundlable b : bundle.getCollection(BUFFS)) {
            if (b == null) continue;
            ((Buff)b).attachTo(this);
        }
    }

    public boolean attack(Char enemy) {
        boolean visibleFight;
        boolean bl = visibleFight = Dungeon.visible[this.pos] || Dungeon.visible[enemy.pos];
        if (Char.hit(this, enemy, false)) {
            if (visibleFight) {
                GLog.i(TXT_HIT, this.name, enemy.name);
            }
            int dr = this instanceof Hero && ((Hero)this).rangedWeapon != null && ((Hero)this).subClass == HeroSubClass.SNIPER ? 0 : Random.IntRange(0, enemy.dr());
            int dmg = this.damageRoll();
            int effectiveDamage = Math.max(dmg - dr, 0);
            effectiveDamage = this.attackProc(enemy, effectiveDamage);
            effectiveDamage = enemy.defenseProc(this, effectiveDamage);
            enemy.damage(effectiveDamage, this);
            if (visibleFight) {
                GameScene.play("snd_hit.mp3", 1.0f, Random.Float(0.8f, 1.25f), this.pos);
            }
            if (enemy == Dungeon.hero) {
                Dungeon.hero.interrupt();
                if (effectiveDamage > enemy.HT / 4) {
                    Camera.main.shake(GameMath.gate(1.0f, effectiveDamage / (enemy.HT / 4), 5.0f), 0.3f);
                }
            }
            enemy.sprite.bloodBurstA(this.sprite.center(), effectiveDamage);
            enemy.sprite.flash();
            if (!enemy.isAlive() && visibleFight) {
                if (enemy == Dungeon.hero) {
                    if (Dungeon.hero.killerGlyph == null) {
                        if (Bestiary.isBoss(this)) {
                            Dungeon.fail(Utils.format("在第 %2$d 层被%1$s杀害", this.name, Dungeon.depth));
                        } else {
                            Dungeon.fail(Utils.format("在第 %2$d 层被%1$s杀害", Utils.indefinite(this.name), Dungeon.depth));
                        }
                        GLog.n(TXT_KILL, this.name);
                    }
                } else {
                    GLog.i(TXT_DEFEAT, this.name, enemy.name);
                }
            }
            return true;
        }
        if (visibleFight) {
            String defense = enemy.defenseVerb();
            enemy.sprite.showStatus(0xFFFF00, defense, new Object[0]);
            if (this == Dungeon.hero) {
                GLog.i(TXT_YOU_MISSED, enemy.name, defense);
            } else {
                GLog.i(TXT_SMB_MISSED, enemy.name, defense, this.name);
            }
            GameScene.play("snd_miss.mp3", this.pos);
        }
        return false;
    }

    public static boolean hit(Char attacker, Char defender, boolean magic) {
        float acuRoll = Random.Float(attacker.attackSkill(defender));
        float defRoll = Random.Float(defender.defenseSkill(attacker));
        return (magic ? acuRoll * 2.0f : acuRoll) >= defRoll;
    }

    public int attackSkill(Char target) {
        return 0;
    }

    public int defenseSkill(Char enemy) {
        return 0;
    }

    public String defenseVerb() {
        return "闪避";
    }

    public int dr() {
        return 0;
    }

    public int damageRoll() {
        return 1;
    }

    public int attackProc(Char enemy, int damage) {
        return damage;
    }

    public int defenseProc(Char enemy, int damage) {
        return damage;
    }

    public float speed() {
        return this.buff(Cripple.class) == null ? this.baseSpeed : this.baseSpeed * 0.5f;
    }

    public void damage(int dmg, Object src) {
        if (this.HP <= 0) {
            return;
        }
        Buff.detach(this, Frost.class);
        Class<?> srcClass = src.getClass();
        if (this.immunities().contains(srcClass)) {
            dmg = 0;
        } else if (this.resistances().contains(srcClass)) {
            dmg = Random.IntRange(0, dmg);
        }
        if (this.buff(Paralysis.class) != null && Random.Int(dmg) >= Random.Int(this.HP)) {
            Buff.detach(this, Paralysis.class);
            if (Dungeon.visible[this.pos]) {
                GLog.i(TXT_OUT_OF_PARALYSIS, this.name);
            }
        }
        this.HP -= dmg;
        if (dmg > 0 || src instanceof Char) {
            this.sprite.showStatus(this.HP > this.HT / 2 ? 0xFF8800 : 0xFF0000, Integer.toString(dmg), new Object[0]);
        }
        if (this.HP <= 0) {
            this.die(src);
        }
    }

    public void destroy() {
        this.HP = 0;
        Actor.remove(this);
        Actor.freeCell(this.pos);
    }

    public void die(Object src) {
        this.destroy();
        this.sprite.die();
    }

    public boolean isAlive() {
        return this.HP > 0;
    }

    @Override
    protected void spend(float time) {
        float timeScale = 1.0f;
        Slow slow = this.buff(Slow.class);
        if (slow != null) {
            timeScale *= slow.timeScale();
        }
        if (this.buff(Speed.class) != null) {
            timeScale *= 2.0f;
        }
        super.spend(time / timeScale);
    }

    public HashSet<Buff> buffs() {
        return this.buffs;
    }

    public <T extends Buff> HashSet<T> buffs(Class<T> c) {
        HashSet<T> filtered = new HashSet<T>();
        for (Buff b : this.buffs) {
            if (!ClassReflection.isInstance(c, b)) continue;
            filtered.add((T)b);
        }
        return filtered;
    }

    public <T extends Buff> T buff(Class<T> c) {
        for (Buff b : this.buffs) {
            if (!ClassReflection.isInstance(c, b)) continue;
            return (T)b;
        }
        return null;
    }

    public boolean isCharmedBy(Char ch) {
        int chID = ch.id();
        for (Buff b : this.buffs) {
            if (!(b instanceof Charm) || ((Charm)b).object != chID) continue;
            return true;
        }
        return false;
    }

    public void add(Buff buff) {
        this.buffs.add(buff);
        Actor.add(buff);
        if (this.sprite != null) {
            if (buff instanceof Poison) {
                CellEmitter.center(this.pos).burst(PoisonParticle.SPLASH, 5);
                this.sprite.showStatus(0xFF0000, "中毒", new Object[0]);
            } else if (buff instanceof Amok) {
                this.sprite.showStatus(0xFF0000, "狂乱", new Object[0]);
            } else if (buff instanceof Slow) {
                this.sprite.showStatus(0xFF0000, "迟缓", new Object[0]);
            } else if (buff instanceof MindVision) {
                this.sprite.showStatus(65280, "灵视", new Object[0]);
            } else if (buff instanceof Paralysis) {
                this.sprite.add(CharSprite.State.PARALYSED);
                this.sprite.showStatus(0xFF0000, "麻痹", new Object[0]);
            } else if (buff instanceof Terror) {
                this.sprite.showStatus(0xFF0000, "恐惧", new Object[0]);
            } else if (buff instanceof Roots) {
                this.sprite.showStatus(0xFF0000, "缠绕", new Object[0]);
            } else if (buff instanceof Cripple) {
                this.sprite.showStatus(0xFF0000, "残废", new Object[0]);
            } else if (buff instanceof Bleeding) {
                this.sprite.showStatus(0xFF0000, "流血", new Object[0]);
            } else if (buff instanceof Vertigo) {
                this.sprite.showStatus(0xFF0000, "眩晕", new Object[0]);
            } else if (buff instanceof Sleep) {
                this.sprite.idle();
            } else if (buff instanceof Burning) {
                this.sprite.add(CharSprite.State.BURNING);
            } else if (buff instanceof Levitation) {
                this.sprite.add(CharSprite.State.LEVITATING);
            } else if (buff instanceof Frost) {
                this.sprite.add(CharSprite.State.FROZEN);
            } else if (buff instanceof Invisibility) {
                if (!(buff instanceof Shadows)) {
                    this.sprite.showStatus(65280, "隐形", new Object[0]);
                }
                this.sprite.add(CharSprite.State.INVISIBLE);
            }
        }
    }

    public void remove(Buff buff) {
        this.buffs.remove(buff);
        Actor.remove(buff);
        if (buff instanceof Burning) {
            this.sprite.remove(CharSprite.State.BURNING);
        } else if (buff instanceof Levitation) {
            this.sprite.remove(CharSprite.State.LEVITATING);
        } else if (buff instanceof Invisibility && this.invisible <= 0) {
            this.sprite.remove(CharSprite.State.INVISIBLE);
        } else if (buff instanceof Paralysis) {
            this.sprite.remove(CharSprite.State.PARALYSED);
        } else if (buff instanceof Frost) {
            this.sprite.remove(CharSprite.State.FROZEN);
        }
    }

    public void remove(Class<? extends Buff> buffClass) {
        for (Buff buff : this.buffs(buffClass)) {
            this.remove(buff);
        }
    }

    @Override
    protected void onRemove() {
        for (Buff buff : this.buffs.toArray(new Buff[0])) {
            buff.detach();
        }
    }

    public void updateSpriteState() {
        for (Buff buff : this.buffs) {
            if (buff instanceof Burning) {
                this.sprite.add(CharSprite.State.BURNING);
                continue;
            }
            if (buff instanceof Levitation) {
                this.sprite.add(CharSprite.State.LEVITATING);
                continue;
            }
            if (buff instanceof Invisibility) {
                this.sprite.add(CharSprite.State.INVISIBLE);
                continue;
            }
            if (buff instanceof Paralysis) {
                this.sprite.add(CharSprite.State.PARALYSED);
                continue;
            }
            if (buff instanceof Frost) {
                this.sprite.add(CharSprite.State.FROZEN);
                continue;
            }
            if (!(buff instanceof Light)) continue;
            this.sprite.add(CharSprite.State.ILLUMINATED);
        }
    }

    public int stealth() {
        return 0;
    }

    public void move(int step) {
        if (Level.adjacent(step, this.pos) && this.buff(Vertigo.class) != null && (!Level.passable[step = this.pos + Level.NEIGHBOURS8[Random.Int(8)]] && !Level.avoid[step] || Actor.findChar(step) != null)) {
            return;
        }
        if (Dungeon.level.map[this.pos] == 6) {
            Door.leave(this.pos);
        }
        this.pos = step;
        if (this.flying && Dungeon.level.map[this.pos] == 5) {
            Door.enter(this.pos);
        }
        if (this != Dungeon.hero) {
            this.sprite.visible = Dungeon.visible[this.pos];
        }
    }

    public int distance(Char other) {
        return Level.distance(this.pos, other.pos);
    }

    public void onMotionComplete() {
        this.next();
    }

    public void onAttackComplete() {
        this.next();
    }

    public void onOperateComplete() {
        this.next();
    }

    public HashSet<Class<?>> resistances() {
        return EMPTY;
    }

    public HashSet<Class<?>> immunities() {
        return EMPTY;
    }
}

