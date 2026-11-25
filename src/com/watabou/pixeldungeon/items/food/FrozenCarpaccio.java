/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.food;

import com.watabou.pixeldungeon.actors.buffs.Barkskin;
import com.watabou.pixeldungeon.actors.buffs.Bleeding;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Cripple;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.buffs.Weakness;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.food.Food;
import com.watabou.pixeldungeon.items.food.MysteryMeat;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class FrozenCarpaccio
extends Food {
    public FrozenCarpaccio() {
        this.name = "冷冻生肉片";
        this.image = 116;
        this.energy = 100.0f;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals("EAT")) {
            switch (Random.Int(5)) {
                case 0: {
                    GLog.i("你看到自己的双手变得透明了！", new Object[0]);
                    Buff.affect(hero, Invisibility.class, 15.0f);
                    break;
                }
                case 1: {
                    GLog.i("你感到自己的皮肤变硬了！", new Object[0]);
                    Buff.affect(hero, Barkskin.class).level(hero.HT / 4);
                    break;
                }
                case 2: {
                    GLog.i("神清气爽！", new Object[0]);
                    Buff.detach(hero, Poison.class);
                    Buff.detach(hero, Cripple.class);
                    Buff.detach(hero, Weakness.class);
                    Buff.detach(hero, Bleeding.class);
                    break;
                }
                case 3: {
                    GLog.i("你感觉好多了！", new Object[0]);
                    if (hero.HP >= hero.HT) break;
                    hero.HP = Math.min(hero.HP + hero.HT / 4, hero.HT);
                    hero.sprite.emitter().burst(Speck.factory(0), 1);
                }
            }
        }
    }

    @Override
    public String info() {
        return "这是一块冻硬的生肉。唯一吃下去的办法就是把它切成薄片——而且这么吃味道意外地不错。";
    }

    @Override
    public int price() {
        return 10 * this.quantity;
    }

    public static Food cook(MysteryMeat ingredient) {
        FrozenCarpaccio result = new FrozenCarpaccio();
        result.quantity = ingredient.quantity();
        return result;
    }
}

