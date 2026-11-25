/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.buffs.Roots;
import com.watabou.pixeldungeon.actors.buffs.Terror;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.actors.mobs.npcs.Ghost;
import com.watabou.pixeldungeon.effects.Pushing;
import com.watabou.pixeldungeon.items.weapon.enchantments.Death;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.sprites.CursePersonificationSprite;
import com.watabou.utils.Random;
import java.util.HashSet;

public class CursePersonification
extends Mob {
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet();

    public CursePersonification() {
        this.name = "诅咒化身";
        this.spriteClass = CursePersonificationSprite.class;
        this.HP = this.HT = 10 + Dungeon.depth * 3;
        this.defenseSkill = 10 + Dungeon.depth;
        this.EXP = 3;
        this.maxLvl = 5;
        this.state = this.HUNTING;
        this.baseSpeed = 0.5f;
        this.flying = true;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(3, 5);
    }

    @Override
    public int attackSkill(Char target) {
        return 10 + Dungeon.depth;
    }

    @Override
    public int dr() {
        return 1;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        for (int i = 0; i < Level.NEIGHBOURS8.length; ++i) {
            int ofs = Level.NEIGHBOURS8[i];
            if (enemy.pos - this.pos != ofs) continue;
            int newPos = enemy.pos + ofs;
            if (!Level.passable[newPos] && !Level.avoid[newPos] || Actor.findChar(newPos) != null) break;
            Actor.addDelayed(new Pushing(enemy, enemy.pos, newPos), -1.0f);
            enemy.pos = newPos;
            if (enemy instanceof Mob) {
                Dungeon.level.mobPress((Mob)enemy);
                break;
            }
            Dungeon.level.press(newPos, enemy);
            break;
        }
        return super.attackProc(enemy, damage);
    }

    @Override
    protected boolean act() {
        if (this.HP > 0 && this.HP < this.HT) {
            ++this.HP;
        }
        return super.act();
    }

    @Override
    public void die(Object cause) {
        Ghost ghost = new Ghost();
        ghost.state = ghost.PASSIVE;
        Ghost.replace(this, ghost);
    }

    @Override
    public String description() {
        return "这个生物的外形酷似悲伤幽灵，但周身缠绕着漆黑的怨气，脸上满是绝望的神情。";
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    static {
        IMMUNITIES.add(Death.class);
        IMMUNITIES.add(Terror.class);
        IMMUNITIES.add(Paralysis.class);
        IMMUNITIES.add(Roots.class);
    }
}

