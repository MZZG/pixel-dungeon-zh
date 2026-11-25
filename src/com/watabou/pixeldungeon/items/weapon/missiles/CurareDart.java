/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.missiles;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.utils.Random;

public class CurareDart
extends MissileWeapon {
    public static final float DURATION = 3.0f;

    public CurareDart() {
        this(1);
    }

    public CurareDart(int number) {
        this.name = "麻痹吹箭";
        this.image = 109;
        this.STR = 14;
        this.quantity = number;
    }

    @Override
    public int min() {
        return 1;
    }

    @Override
    public int max() {
        return 3;
    }

    @Override
    public void proc(Char attacker, Char defender, int damage) {
        Buff.prolong(defender, Paralysis.class, 3.0f);
        super.proc(attacker, defender, damage);
    }

    @Override
    public String desc() {
        return "这种阴险的小飞镖伤害不高，但能麻痹目标，使其在一段时间内动弹不得、任人宰割。";
    }

    @Override
    public Item random() {
        this.quantity = Random.Int(2, 5);
        return this;
    }

    @Override
    public int price() {
        return 12 * this.quantity;
    }
}

