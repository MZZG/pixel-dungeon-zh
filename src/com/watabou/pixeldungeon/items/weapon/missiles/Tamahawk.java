/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.missiles;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Bleeding;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.utils.Random;

public class Tamahawk
extends MissileWeapon {
    public Tamahawk() {
        this(1);
    }

    public Tamahawk(int number) {
        this.name = "飞斧";
        this.image = 107;
        this.STR = 17;
        this.quantity = number;
    }

    @Override
    public int min() {
        return 4;
    }

    @Override
    public int max() {
        return 20;
    }

    @Override
    public void proc(Char attacker, Char defender, int damage) {
        super.proc(attacker, defender, damage);
        Buff.affect(defender, Bleeding.class).set(damage);
    }

    @Override
    public String desc() {
        return "这把投掷用的手斧并不算特别重，但仍需要相当的力量才能有效挥投。";
    }

    @Override
    public Item random() {
        this.quantity = Random.Int(5, 12);
        return this;
    }

    @Override
    public int price() {
        return 20 * this.quantity;
    }
}

