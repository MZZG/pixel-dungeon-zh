/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.quest;

import com.watabou.pixeldungeon.items.Item;

public class RatSkull
extends Item {
    public RatSkull() {
        this.name = "巨鼠头骨";
        this.image = 103;
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
        return "这本可以当成不错的狩猎战利品，但味道实在是太冲了，根本没法挂在墙上。";
    }

    @Override
    public int price() {
        return 100;
    }
}

