/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.potions;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.potions.PotionOfStrength;
import com.watabou.pixeldungeon.utils.GLog;

public class PotionOfMight
extends PotionOfStrength {
    public PotionOfMight() {
        this.name = "强韧药剂";
    }

    @Override
    protected void apply(Hero hero) {
        this.setKnown();
        ++hero.STR;
        hero.HT += 5;
        hero.HP += 5;
        hero.sprite.showStatus(65280, "+1 力量, +5 生命", new Object[0]);
        GLog.p("一股崭新的力量涌遍全身。", new Object[0]);
        Badges.validateStrengthAttained();
    }

    @Override
    public String desc() {
        return "这种强效药液会流遍你的肌肉，永久提升 1 点力量与 5 点生命上限。";
    }

    @Override
    public int price() {
        return this.isKnown() ? 200 * this.quantity : super.price();
    }
}

