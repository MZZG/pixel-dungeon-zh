/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.quest;

import com.watabou.pixeldungeon.items.Item;

public class DriedRose
extends Item {
    public DriedRose() {
        this.name = "干枯玫瑰";
        this.image = 100;
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
        return "这朵玫瑰早已干枯，却不知为何完好地保留了全部花瓣。";
    }
}

