/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.missiles;

import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.utils.Random;

public class Shuriken
extends MissileWeapon {
    public Shuriken() {
        this(1);
    }

    public Shuriken(int number) {
        this.name = "手里剑";
        this.image = 15;
        this.STR = 13;
        this.DLY = 0.5f;
        this.quantity = number;
    }

    @Override
    public int min() {
        return 2;
    }

    @Override
    public int max() {
        return 6;
    }

    @Override
    public String desc() {
        return "星形的锐利金属飞镖，命中目标能造成可观的伤害，而且投掷速度极快。";
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

