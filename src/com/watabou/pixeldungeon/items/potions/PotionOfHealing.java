/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.potions;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Bleeding;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Cripple;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.buffs.Weakness;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.utils.GLog;

public class PotionOfHealing
extends Potion {
    public PotionOfHealing() {
        this.name = "治疗药剂";
    }

    @Override
    protected void apply(Hero hero) {
        this.setKnown();
        PotionOfHealing.heal(Dungeon.hero);
        GLog.p("你的伤势完全痊愈了。", new Object[0]);
    }

    public static void heal(Hero hero) {
        hero.HP = hero.HT;
        Buff.detach(hero, Poison.class);
        Buff.detach(hero, Cripple.class);
        Buff.detach(hero, Weakness.class);
        Buff.detach(hero, Bleeding.class);
        hero.sprite.emitter().start(Speck.factory(0), 0.4f, 4);
    }

    @Override
    public String desc() {
        return "一瓶能瞬间将你恢复至满血并治愈中毒的灵药。";
    }

    @Override
    public int price() {
        return this.isKnown() ? 30 * this.quantity : super.price();
    }
}

