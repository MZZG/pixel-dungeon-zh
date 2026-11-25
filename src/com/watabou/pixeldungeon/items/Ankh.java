/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items;

import com.watabou.pixeldungeon.items.Item;

public class Ankh
extends Item {
    public Ankh() {
        this.stackable = true;
        this.name = "重生十字架";
        this.image = 1;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public String info() {
        return "古老的永生象征，赐予持有者死而复生的力量。复活时所有未装备的物品都会丢失。";
    }

    @Override
    public int price() {
        return 50 * this.quantity;
    }
}

