/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.potions;

import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.potions.Potion;

public class PotionOfExperience
extends Potion {
    public PotionOfExperience() {
        this.name = "经验药剂";
    }

    @Override
    protected void apply(Hero hero) {
        this.setKnown();
        hero.earnExp(hero.maxExp() - hero.exp);
    }

    @Override
    public String desc() {
        return "无数战斗积累而来的经验浓缩为液态，喝下这瓶药剂能立刻提升你的等级。";
    }

    @Override
    public int price() {
        return this.isKnown() ? 80 * this.quantity : super.price();
    }
}

