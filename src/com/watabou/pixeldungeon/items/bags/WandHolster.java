/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.bags;

import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.bags.Bag;
import com.watabou.pixeldungeon.items.wands.Wand;

public class WandHolster
extends Bag {
    public WandHolster() {
        this.name = "法杖套";
        this.image = 111;
        this.size = 12;
    }

    @Override
    public boolean grab(Item item) {
        return item instanceof Wand;
    }

    @Override
    public boolean collect(Bag container) {
        if (super.collect(container)) {
            if (this.owner != null) {
                for (Item item : this.items) {
                    ((Wand)item).charge(this.owner);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDetach() {
        for (Item item : this.items) {
            ((Wand)item).stopCharging();
        }
    }

    @Override
    public int price() {
        return 50;
    }

    @Override
    public String info() {
        return "这个纤细的护套由某种奇异野兽的皮革制成，最多可紧凑地收纳 " + this.size + " 根法杖。";
    }
}

