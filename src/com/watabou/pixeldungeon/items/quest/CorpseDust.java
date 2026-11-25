/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.quest;

import com.watabou.pixeldungeon.items.Item;

public class CorpseDust
extends Item {
    public CorpseDust() {
        this.name = "尸尘";
        this.image = 121;
        this.cursed = true;
        this.cursedKnown = true;
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
        return "这团尸尘外表看上去与普通的灰尘没什么两样，但你本能地觉得最好尽快把它处理掉。";
    }
}

