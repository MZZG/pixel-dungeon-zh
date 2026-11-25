/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.hero;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.KindOfWeapon;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.bags.Bag;
import com.watabou.pixeldungeon.items.keys.IronKey;
import com.watabou.pixeldungeon.items.keys.Key;
import com.watabou.pixeldungeon.items.rings.Ring;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import java.util.ArrayList;
import java.util.Iterator;

public class Belongings
implements Iterable<Item> {
    public static final int BACKPACK_SIZE = 19;
    private Hero owner;
    public Bag backpack;
    public KindOfWeapon weapon = null;
    public Armor armor = null;
    public Ring ring1 = null;
    public Ring ring2 = null;
    private static final String WEAPON = "weapon";
    private static final String ARMOR = "armor";
    private static final String RING1 = "ring1";
    private static final String RING2 = "ring2";

    public Belongings(Hero owner) {
        this.owner = owner;
        this.backpack = new Bag(){
            {
                this.name = "背包";
                this.size = 19;
            }
        };
        this.backpack.owner = owner;
    }

    public void storeInBundle(Bundle bundle) {
        this.backpack.storeInBundle(bundle);
        bundle.put(WEAPON, this.weapon);
        bundle.put(ARMOR, this.armor);
        bundle.put(RING1, this.ring1);
        bundle.put(RING2, this.ring2);
    }

    public void restoreFromBundle(Bundle bundle) {
        this.backpack.clear();
        this.backpack.restoreFromBundle(bundle);
        this.weapon = (KindOfWeapon)bundle.get(WEAPON);
        if (this.weapon != null) {
            this.weapon.activate(this.owner);
        }
        this.armor = (Armor)bundle.get(ARMOR);
        this.ring1 = (Ring)bundle.get(RING1);
        if (this.ring1 != null) {
            this.ring1.activate(this.owner);
        }
        this.ring2 = (Ring)bundle.get(RING2);
        if (this.ring2 != null) {
            this.ring2.activate(this.owner);
        }
    }

    public <T extends Item> T getItem(Class<T> itemClass) {
        for (Item item : this) {
            if (!itemClass.isInstance(item)) continue;
            return (T)item;
        }
        return null;
    }

    public <T extends Item> ArrayList<T> getItems(Class<T> itemClass) {
        ArrayList<T> result = new ArrayList<T>();
        for (Item item : this) {
            if (!itemClass.isInstance(item)) continue;
            result.add((T)item);
        }
        return result;
    }

    public <T extends Key> T getKey(Class<T> kind, int depth) {
        for (Item item : this.backpack) {
            if (item.getClass() != kind || ((Key)item).depth != depth) continue;
            return (T)((Key)item);
        }
        return null;
    }

    public void countIronKeys() {
        IronKey.curDepthQuantity = 0;
        for (Item item : this.backpack) {
            if (!(item instanceof IronKey) || ((IronKey)item).depth != Dungeon.depth) continue;
            ++IronKey.curDepthQuantity;
        }
    }

    public void identify() {
        for (Item item : this) {
            item.identify();
        }
    }

    public void observe() {
        if (this.weapon != null) {
            this.weapon.identify();
            Badges.validateItemLevelAquired(this.weapon);
        }
        if (this.armor != null) {
            this.armor.identify();
            Badges.validateItemLevelAquired(this.armor);
        }
        if (this.ring1 != null) {
            this.ring1.identify();
            Badges.validateItemLevelAquired(this.ring1);
        }
        if (this.ring2 != null) {
            this.ring2.identify();
            Badges.validateItemLevelAquired(this.ring2);
        }
        for (Item item : this.backpack) {
            item.cursedKnown = true;
        }
    }

    public void uncurseEquipped() {
        ScrollOfRemoveCurse.uncurse(this.owner, this.armor, this.weapon, this.ring1, this.ring2);
    }

    public Item randomUnequipped() {
        return Random.element(this.backpack.items);
    }

    public void resurrect(int depth) {
        for (Item item : this.backpack.items.toArray(new Item[0])) {
            if (item instanceof Key) {
                if (((Key)item).depth != depth) continue;
                item.detachAll(this.backpack);
                continue;
            }
            if (item.unique || item.isEquipped(this.owner)) continue;
            item.detachAll(this.backpack);
        }
        if (this.weapon != null) {
            this.weapon.cursed = false;
            this.weapon.activate(this.owner);
        }
        if (this.armor != null) {
            this.armor.cursed = false;
        }
        if (this.ring1 != null) {
            this.ring1.cursed = false;
            this.ring1.activate(this.owner);
        }
        if (this.ring2 != null) {
            this.ring2.cursed = false;
            this.ring2.activate(this.owner);
        }
    }

    public int charge(boolean full) {
        int count = 0;
        for (Item item : this) {
            if (!(item instanceof Wand)) continue;
            Wand wand = (Wand)item;
            if (wand.curCharges >= wand.maxCharges) continue;
            wand.curCharges = full ? wand.maxCharges : wand.curCharges + 1;
            ++count;
            wand.updateQuickslot();
        }
        return count;
    }

    public int discharge() {
        int count = 0;
        for (Item item : this) {
            if (!(item instanceof Wand)) continue;
            Wand wand = (Wand)item;
            if (wand.curCharges <= 0) continue;
            --wand.curCharges;
            ++count;
            wand.updateQuickslot();
        }
        return count;
    }

    @Override
    public Iterator<Item> iterator() {
        return new ItemIterator();
    }

    private class ItemIterator
    implements Iterator<Item> {
        private int index = 0;
        private Iterator<Item> backpackIterator;
        private Item[] equipped;
        private int backpackIndex;

        private ItemIterator() {
            this.backpackIterator = Belongings.this.backpack.iterator();
            this.equipped = new Item[]{Belongings.this.weapon, Belongings.this.armor, Belongings.this.ring1, Belongings.this.ring2};
            this.backpackIndex = this.equipped.length;
        }

        @Override
        public boolean hasNext() {
            for (int i = this.index; i < this.backpackIndex; ++i) {
                if (this.equipped[i] == null) continue;
                return true;
            }
            return this.backpackIterator.hasNext();
        }

        @Override
        public Item next() {
            while (this.index < this.backpackIndex) {
                Item item;
                if ((item = this.equipped[this.index++]) == null) continue;
                return item;
            }
            return this.backpackIterator.next();
        }

        @Override
        public void remove() {
            switch (this.index) {
                case 0: {
                    Belongings.this.weapon = null;
                    this.equipped[0] = null;
                    break;
                }
                case 1: {
                    Belongings.this.armor = null;
                    this.equipped[1] = null;
                    break;
                }
                case 2: {
                    Belongings.this.ring1 = null;
                    this.equipped[2] = null;
                    break;
                }
                case 3: {
                    Belongings.this.ring2 = null;
                    this.equipped[3] = null;
                    break;
                }
                default: {
                    this.backpackIterator.remove();
                }
            }
        }
    }
}

