/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.actors.buffs.FlavourBuff;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.food.FrozenCarpaccio;
import com.watabou.pixeldungeon.items.food.MysteryMeat;
import com.watabou.pixeldungeon.items.rings.RingOfElements;

public class Frost
extends FlavourBuff {
    private static final float DURATION = 5.0f;

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            target.paralysed = true;
            Burning.detach(target, Burning.class);
            if (target instanceof Hero) {
                Hero hero = (Hero)target;
                Item item = hero.belongings.randomUnequipped();
                if (item instanceof MysteryMeat) {
                    item = item.detach(hero.belongings.backpack);
                    FrozenCarpaccio carpaccio = new FrozenCarpaccio();
                    if (!carpaccio.collect(hero.belongings.backpack)) {
                        Dungeon.level.drop((Item)carpaccio, (int)target.pos).sprite.drop();
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void detach() {
        super.detach();
        Paralysis.unfreeze(this.target);
    }

    @Override
    public int icon() {
        return 15;
    }

    public String toString() {
        return "冻结";
    }

    public static float duration(Char ch) {
        RingOfElements.Resistance r = ch.buff(RingOfElements.Resistance.class);
        return r != null ? r.durationFactor() * 5.0f : 5.0f;
    }
}

