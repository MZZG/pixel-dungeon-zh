/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.actors.buffs.Frost;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.buffs.Roots;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.food.MysteryMeat;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.sprites.PiranhaSprite;
import com.watabou.utils.Random;
import java.util.HashSet;

public class Piranha
extends Mob {
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet();

    public Piranha() {
        this.name = "巨型食人鱼";
        this.spriteClass = PiranhaSprite.class;
        this.baseSpeed = 2.0f;
        this.EXP = 0;
        this.HP = this.HT = 10 + Dungeon.depth * 5;
        this.defenseSkill = 10 + Dungeon.depth * 2;
    }

    @Override
    protected boolean act() {
        if (!Level.water[this.pos]) {
            this.die(null);
            return true;
        }
        return super.act();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(Dungeon.depth, 4 + Dungeon.depth * 2);
    }

    @Override
    public int attackSkill(Char target) {
        return 20 + Dungeon.depth * 2;
    }

    @Override
    public int dr() {
        return Dungeon.depth;
    }

    @Override
    public void die(Object cause) {
        Dungeon.level.drop((Item)new MysteryMeat(), (int)this.pos).sprite.drop();
        super.die(cause);
        Statistics.add(Statistics.Type.PIRANHAS, 1);
        Badges.validatePiranhasKilled();
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    protected boolean getCloser(int target) {
        if (this.rooted) {
            return false;
        }
        int step = Dungeon.findPath(this, this.pos, target, Level.water, Level.fieldOfView);
        if (step != -1) {
            this.move(step);
            return true;
        }
        return false;
    }

    @Override
    protected boolean getFurther(int target) {
        int step = Dungeon.flee(this, this.pos, target, Level.water, Level.fieldOfView);
        if (step != -1) {
            this.move(step);
            return true;
        }
        return false;
    }

    @Override
    public String description() {
        return "这些凶猛的食肉鱼并非地下水池的原生生物，它们是专门被培育用来看守水淹宝库的守卫。";
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    static {
        IMMUNITIES.add(Burning.class);
        IMMUNITIES.add(Paralysis.class);
        IMMUNITIES.add(ToxicGas.class);
        IMMUNITIES.add(Roots.class);
        IMMUNITIES.add(Frost.class);
    }
}

