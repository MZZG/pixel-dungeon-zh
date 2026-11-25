/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.quest;

import com.watabou.pixeldungeon.items.Item;

public class DwarfToken
extends Item {
    public DwarfToken() {
        this.name = "矮人徽记";
        this.image = 122;
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
        return "许多矮人和他们的大型造物都会携带这种用途不明的小块金属。也许是饰品，也许是某种身份牌——矮人真是群古怪的家伙。";
    }

    @Override
    public int price() {
        return this.quantity * 100;
    }
}

