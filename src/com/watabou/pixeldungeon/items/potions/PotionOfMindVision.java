/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.potions;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.MindVision;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.utils.GLog;

public class PotionOfMindVision
extends Potion {
    public PotionOfMindVision() {
        this.name = "灵视药剂";
    }

    @Override
    protected void apply(Hero hero) {
        this.setKnown();
        Buff.affect(hero, MindVision.class, 20.0f);
        Dungeon.observe();
        if (Dungeon.level.mobs.size() > 0) {
            GLog.i("你不知为何能够感知到其他生物的心智存在！", new Object[0]);
        } else {
            GLog.i("你能隐约察觉到此刻这一层只有你独自一人。", new Object[0]);
        }
    }

    @Override
    public String desc() {
        return "喝下它之后，你的心智将与远方生物的精神信号同调，隔着墙壁也能感知到它们的所在；同时还能看穿附近的墙壁与门扉。";
    }

    @Override
    public int price() {
        return this.isKnown() ? 35 * this.quantity : super.price();
    }
}

