/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Cripple;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.food.MysteryMeat;
import com.watabou.pixeldungeon.items.potions.PotionOfHealing;
import com.watabou.pixeldungeon.items.weapon.enchantments.Leech;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.sprites.ScorpioSprite;
import com.watabou.utils.Random;
import java.util.HashSet;

public class Scorpio
extends Mob {
    private static final HashSet<Class<?>> RESISTANCES = new HashSet();

    public Scorpio() {
        this.name = "巨蝎";
        this.spriteClass = ScorpioSprite.class;
        this.HT = 95;
        this.HP = 95;
        this.defenseSkill = 24;
        this.viewDistance = 4;
        this.EXP = 14;
        this.maxLvl = 25;
        this.loot = new PotionOfHealing();
        this.lootChance = 0.125f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(20, 32);
    }

    @Override
    public int attackSkill(Char target) {
        return 36;
    }

    @Override
    public int dr() {
        return 16;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return !Level.adjacent(this.pos, enemy.pos) && Ballistica.cast(this.pos, enemy.pos, false, true) == enemy.pos;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (Random.Int(2) == 0) {
            Buff.prolong(enemy, Cripple.class, 10.0f);
        }
        return damage;
    }

    @Override
    protected boolean getCloser(int target) {
        if (this.state == this.HUNTING) {
            return this.enemySeen && this.getFurther(target);
        }
        return super.getCloser(target);
    }

    @Override
    protected void dropLoot() {
        if (Random.Int(8) == 0) {
            Dungeon.level.drop((Item)new PotionOfHealing(), (int)this.pos).sprite.drop();
        } else if (Random.Int(6) == 0) {
            Dungeon.level.drop((Item)new MysteryMeat(), (int)this.pos).sprite.drop();
        }
    }

    @Override
    public String description() {
        return "这种形似巨型蛛形纲动物的恶魔会极力避免近身肉搏，转而从远处发射令人致残的锯齿毒刺。";
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    static {
        RESISTANCES.add(Leech.class);
        RESISTANCES.add(Poison.class);
    }
}

