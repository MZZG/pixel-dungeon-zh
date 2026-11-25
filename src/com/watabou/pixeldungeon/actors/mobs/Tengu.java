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
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.TomeOfMastery;
import com.watabou.pixeldungeon.items.keys.SkeletonKey;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.watabou.pixeldungeon.items.weapon.enchantments.Death;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.TenguSprite;
import com.watabou.utils.Random;
import java.util.HashSet;

public class Tengu
extends Mob {
    private static final int JUMP_DELAY = 5;
    private int timeToJump;
    private static final HashSet<Class<?>> RESISTANCES = new HashSet();

    public Tengu() {
        this.name = Dungeon.depth == Statistics.getInt(Statistics.Type.DEEPEST) ? "Tengu" : "memory of Tengu";
        this.spriteClass = TenguSprite.class;
        this.HT = 120;
        this.HP = 120;
        this.EXP = 20;
        this.defenseSkill = 20;
        this.timeToJump = 5;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(8, 15);
    }

    @Override
    public int attackSkill(Char target) {
        return 20;
    }

    @Override
    public int dr() {
        return 5;
    }

    @Override
    public void die(Object cause) {
        Badges.Badge badgeToCheck = null;
        switch (Dungeon.hero.heroClass) {
            case WARRIOR: {
                badgeToCheck = Badges.Badge.MASTERY_WARRIOR;
                break;
            }
            case MAGE: {
                badgeToCheck = Badges.Badge.MASTERY_MAGE;
                break;
            }
            case ROGUE: {
                badgeToCheck = Badges.Badge.MASTERY_ROGUE;
                break;
            }
            case HUNTRESS: {
                badgeToCheck = Badges.Badge.MASTERY_HUNTRESS;
            }
        }
        if (!Badges.isUnlocked(badgeToCheck) || Dungeon.hero.subClass != HeroSubClass.NONE) {
            Dungeon.level.drop((Item)new TomeOfMastery(), (int)this.pos).sprite.drop();
        }
        GameScene.bossSlain();
        Dungeon.level.drop((Item)new SkeletonKey(), (int)this.pos).sprite.drop();
        super.die(cause);
        Badges.validateBossSlain();
        this.yell("终于...解脱了...");
    }

    @Override
    protected boolean getCloser(int target) {
        if (Level.fieldOfView[target]) {
            this.jump();
            return true;
        }
        return super.getCloser(target);
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return Ballistica.cast(this.pos, enemy.pos, false, true) == enemy.pos;
    }

    @Override
    protected boolean doAttack(Char enemy) {
        --this.timeToJump;
        if (this.timeToJump <= 0 && Level.adjacent(this.pos, enemy.pos)) {
            this.jump();
            return true;
        }
        return super.doAttack(enemy);
    }

    private void jump() {
        int newPos;
        this.timeToJump = 5;
        for (int i = 0; i < 4; ++i) {
            int trapPos;
            while (!Level.fieldOfView[trapPos = Random.Int(1024)] || !Level.passable[trapPos]) {
            }
            if (Dungeon.level.map[trapPos] != 23) continue;
            Level.set(trapPos, 27);
            GameScene.updateMap(trapPos);
            ScrollOfMagicMapping.discover(trapPos);
        }
        while (!Level.fieldOfView[newPos = Random.Int(1024)] || !Level.passable[newPos] || this.enemy != null && Level.adjacent(newPos, this.enemy.pos) || Actor.findChar(newPos) != null) {
        }
        this.sprite.move(this.pos, newPos);
        this.move(newPos);
        if (Dungeon.visible[newPos]) {
            CellEmitter.get(newPos).burst(Speck.factory(7), 6);
            GameScene.play("snd_puff.mp3", newPos);
        }
        this.spend(1.0f / this.speed());
    }

    @Override
    public void notice() {
        super.notice();
        this.yell("抓到你了，" + Dungeon.hero.heroClass.title() + "！");
    }

    @Override
    public String description() {
        return "天狗是古老刺客宗族\"天狗一族\"的成员。这些刺客以熟练运用手里剑与各种陷阱而闻名。";
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    static {
        RESISTANCES.add(ToxicGas.class);
        RESISTANCES.add(Poison.class);
        RESISTANCES.add(Death.class);
        RESISTANCES.add(ScrollOfPsionicBlast.class);
    }
}

