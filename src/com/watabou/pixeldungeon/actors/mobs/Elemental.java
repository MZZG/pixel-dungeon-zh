/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.actors.buffs.Frost;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.potions.PotionOfLiquidFlame;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.watabou.pixeldungeon.items.wands.WandOfFirebolt;
import com.watabou.pixeldungeon.items.weapon.enchantments.Fire;
import com.watabou.pixeldungeon.sprites.ElementalSprite;
import com.watabou.utils.Random;
import java.util.HashSet;

public class Elemental
extends Mob {
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet();

    public Elemental() {
        this.name = "火焰元素";
        this.spriteClass = ElementalSprite.class;
        this.HT = 65;
        this.HP = 65;
        this.defenseSkill = 20;
        this.EXP = 10;
        this.maxLvl = 20;
        this.flying = true;
        this.loot = new PotionOfLiquidFlame();
        this.lootChance = 0.1f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(16, 20);
    }

    @Override
    public int attackSkill(Char target) {
        return 25;
    }

    @Override
    public int dr() {
        return 5;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (Random.Int(2) == 0) {
            Buff.affect(enemy, Burning.class).reignite(enemy);
        }
        return damage;
    }

    @Override
    public void add(Buff buff) {
        if (buff instanceof Burning) {
            if (this.HP < this.HT) {
                ++this.HP;
                this.sprite.emitter().burst(Speck.factory(0), 1);
            }
        } else {
            if (buff instanceof Frost) {
                this.damage(Random.NormalIntRange(1, this.HT * 2 / 3), buff);
            }
            super.add(buff);
        }
    }

    @Override
    public String description() {
        return "四处游荡的火元素是召唤强大存在时产生的副产物。它们的本性过于混沌，就连最强大的恶魔学者也无法将其完全掌控。";
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    static {
        IMMUNITIES.add(Burning.class);
        IMMUNITIES.add(Fire.class);
        IMMUNITIES.add(WandOfFirebolt.class);
        IMMUNITIES.add(ScrollOfPsionicBlast.class);
    }
}

