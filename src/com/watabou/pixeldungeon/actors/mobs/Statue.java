/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Journal;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.items.Generator;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.items.weapon.enchantments.Death;
import com.watabou.pixeldungeon.items.weapon.enchantments.Leech;
import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;
import com.watabou.pixeldungeon.sprites.StatueSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import java.util.HashSet;

public class Statue
extends Mob {
    private Weapon weapon;
    private static final String WEAPON = "weapon";
    private static final HashSet<Class<?>> RESISTANCES = new HashSet();
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet();

    public Statue() {
        this.name = "活化石像";
        this.spriteClass = StatueSprite.class;
        this.EXP = 0;
        this.state = this.PASSIVE;
        do {
            this.weapon = (Weapon)Generator.random(Generator.Category.WEAPON);
        } while (!(this.weapon instanceof MeleeWeapon) || this.weapon.level() < 0);
        this.weapon.identify();
        this.weapon.enchant();
        this.HP = this.HT = 15 + Dungeon.depth * 5;
        this.defenseSkill = 4 + Dungeon.depth;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(WEAPON, this.weapon);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.weapon = (Weapon)bundle.get(WEAPON);
    }

    @Override
    protected boolean act() {
        if (Dungeon.visible[this.pos]) {
            Journal.add(Journal.Feature.STATUE);
        }
        return super.act();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(this.weapon.min(), this.weapon.max());
    }

    @Override
    public int attackSkill(Char target) {
        return (int)((float)(9 + Dungeon.depth) * this.weapon.ACU);
    }

    @Override
    protected float attackDelay() {
        return this.weapon.DLY;
    }

    @Override
    public int dr() {
        return Dungeon.depth;
    }

    @Override
    public void damage(int dmg, Object src) {
        if (this.state == this.PASSIVE) {
            this.state = this.HUNTING;
        }
        super.damage(dmg, src);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        this.weapon.proc(this, enemy, damage);
        return damage;
    }

    @Override
    public void beckon(int cell) {
    }

    @Override
    public void die(Object cause) {
        Dungeon.level.drop((Item)this.weapon, (int)this.pos).sprite.drop();
        super.die(cause);
    }

    @Override
    public void destroy() {
        Journal.remove(Journal.Feature.STATUE);
        super.destroy();
    }

    @Override
    public boolean reset() {
        this.state = this.PASSIVE;
        return true;
    }

    @Override
    public String description() {
        return "你本以为它只是地牢里又一座丑陋的石像，但它闪烁着红光的双眼暴露了一切。虽然石像本身是石头雕成的，但它手中紧握的_" + this.weapon.name() + "_却货真价实。";
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
        RESISTANCES.add(Poison.class);
        RESISTANCES.add(Death.class);
        RESISTANCES.add(ScrollOfPsionicBlast.class);
        IMMUNITIES.add(Leech.class);
    }
}

