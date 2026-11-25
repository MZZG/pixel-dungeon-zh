/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.missiles;

import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.utils.Random;

public class Dart
extends MissileWeapon {
    public Dart() {
        this(1);
    }

    public Dart(int number) {
        this.name = "飞镖";
        this.image = 31;
        this.quantity = number;
    }

    @Override
    public int min() {
        return 1;
    }

    @Override
    public int max() {
        return 4;
    }

    @Override
    public String desc() {
        return "这些结构简单的金属飞镖配重均匀，只需轻轻一甩便能笔直飞出并刺中猎物。";
    }

    @Override
    public Item random() {
        this.quantity = Random.Int(5, 15);
        return this;
    }

    @Override
    public int price() {
        return this.quantity * 2;
    }
}

