/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.food;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.buffs.Hunger;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.effects.SpellSprite;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfRecharging;
import com.watabou.pixeldungeon.utils.GLog;
import java.util.ArrayList;

public class Food
extends Item {
    private static final float TIME_TO_EAT = 3.0f;
    public static final String AC_EAT = "EAT";
    public float energy = 260.0f;
    public String message = "吃起来真美味！";

    public Food() {
        this.stackable = true;
        this.name = "口粮";
        this.image = 4;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_EAT);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action.equals(AC_EAT)) {
            this.detach(hero.belongings.backpack);
            hero.buff(Hunger.class).satisfy(this.energy);
            GLog.i(this.message, new Object[0]);
            switch (hero.heroClass) {
                case WARRIOR: {
                    if (hero.HP >= hero.HT) break;
                    hero.HP = Math.min(hero.HP + 5, hero.HT);
                    hero.sprite.emitter().burst(Speck.factory(0), 1);
                    break;
                }
                case MAGE: {
                    hero.belongings.charge(false);
                    ScrollOfRecharging.charge(hero);
                    break;
                }
            }
            hero.sprite.operate(hero.pos);
            hero.busy();
            SpellSprite.show(hero, 0);
            Sample.INSTANCE.play("snd_eat.mp3");
            hero.spend(3.0f);
            Statistics.add(Statistics.Type.FOOD, 1);
            Badges.validateFoodEaten();
        } else {
            super.execute(hero, action);
        }
    }

    @Override
    public String info() {
        return "里面都是些寻常干粮：一片肉干，几块饼干——诸如此类。";
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
    public int price() {
        return 10 * this.quantity;
    }
}

