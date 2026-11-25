/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.quest;

import com.watabou.pixeldungeon.items.Item;

public class DarkGold
extends Item {
    public DarkGold() {
        this.name = "暗金矿";
        this.image = 102;
        this.stackable = true;
        this.unique = true;
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
        return "这种金属之所以叫暗金，并非因为颜色发黑（它和普通黄金一样），而是因为它一见日光就会熔化，在地表毫无用处。";
    }

    @Override
    public int price() {
        return this.quantity;
    }
}

