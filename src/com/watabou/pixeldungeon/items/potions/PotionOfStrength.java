/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.potions;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.utils.GLog;

public class PotionOfStrength
extends Potion {
    public PotionOfStrength() {
        this.name = "力量药剂";
    }

    @Override
    protected void apply(Hero hero) {
        this.setKnown();
        ++hero.STR;
        hero.sprite.showStatus(65280, "+1 力量", new Object[0]);
        GLog.p("一股崭新的力量涌遍全身。", new Object[0]);
        Badges.validateStrengthAttained();
    }

    @Override
    public String desc() {
        return "这种强力的液体会洗刷你的肌肉，永久性增加 1 点力量值。";
    }

    @Override
    public int price() {
        return this.isKnown() ? 100 * this.quantity : super.price();
    }
}

