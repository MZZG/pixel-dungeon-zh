/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.food;

import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.buffs.Roots;
import com.watabou.pixeldungeon.actors.buffs.Slow;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.food.Food;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class MysteryMeat
extends Food {
    public MysteryMeat() {
        this.name = "神秘的肉";
        this.image = 113;
        this.energy = 100.0f;
        this.message = "这玩意儿吃起来很...奇怪。";
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals("EAT")) {
            switch (Random.Int(5)) {
                case 0: {
                    GLog.w("嗷！好烫！", new Object[0]);
                    Buff.affect(hero, Burning.class).reignite(hero);
                    break;
                }
                case 1: {
                    GLog.w("你的双腿失去了知觉！", new Object[0]);
                    Buff.prolong(hero, Roots.class, Paralysis.duration(hero));
                    break;
                }
                case 2: {
                    GLog.w("你感觉不太对劲。", new Object[0]);
                    Buff.affect(hero, Poison.class).set(Poison.durationFactor(hero) * (float)hero.HT / 5.0f);
                    break;
                }
                case 3: {
                    GLog.w("你吃撑了。", new Object[0]);
                    Buff.prolong(hero, Slow.class, Slow.duration(hero));
                }
            }
        }
    }

    @Override
    public String info() {
        return "想吃可以，后果自负！";
    }

    @Override
    public int price() {
        return 5 * this.quantity;
    }
}

