/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.potions;

import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Levitation;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.utils.GLog;

public class PotionOfLevitation
extends Potion {
    public PotionOfLevitation() {
        this.name = "浮空药剂";
    }

    @Override
    protected void apply(Hero hero) {
        this.setKnown();
        Buff.affect(hero, Levitation.class, 20.0f);
        GLog.i("你漂浮到了空中！", new Object[0]);
    }

    @Override
    public String desc() {
        return "喝下这种奇特的液体能让你悬浮在半空，轻松飘过陷阱。不过火焰和毒气同样会弥漫在空中，浮空时也无法躲过。";
    }

    @Override
    public int price() {
        return this.isKnown() ? 35 * this.quantity : super.price();
    }
}

