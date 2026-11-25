/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.missiles;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Cripple;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.utils.Random;

public class Javelin
extends MissileWeapon {
    public Javelin() {
        this(1);
    }

    public Javelin(int number) {
        this.name = "标枪";
        this.image = 110;
        this.STR = 15;
        this.quantity = number;
    }

    @Override
    public int min() {
        return 2;
    }

    @Override
    public int max() {
        return 15;
    }

    @Override
    public void proc(Char attacker, Char defender, int damage) {
        super.proc(attacker, defender, damage);
        Buff.prolong(defender, Cripple.class, 10.0f);
    }

    @Override
    public String desc() {
        return "这根金属投枪经过配重设计，飞向空中时能始终保持尖端朝前穿刺目标。";
    }

    @Override
    public Item random() {
        this.quantity = Random.Int(5, 15);
        return this;
    }

    @Override
    public int price() {
        return 15 * this.quantity;
    }
}

