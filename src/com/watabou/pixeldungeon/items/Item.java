/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.SnipersMark;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.Degradation;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.Generator;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.bags.Bag;
import com.watabou.pixeldungeon.items.rings.Ring;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.scenes.CellSelector;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.sprites.MissileSprite;
import com.watabou.pixeldungeon.ui.QuickSlot;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Item
implements Bundlable {
    private static final String TXT_PACK_FULL = "你的背包太满了，装不下%s";
    private static final String TXT_BROKEN = "由于频繁使用，你的%s已经损坏了。";
    private static final String TXT_GONNA_BREAK = "由于频繁使用，你的%s快要损坏了。";
    private static final String TXT_TO_STRING = "%s";
    private static final String TXT_TO_STRING_X = "%s x%d";
    private static final String TXT_TO_STRING_LVL = "%s%+d";
    private static final String TXT_TO_STRING_LVL_X = "%s%+d x%d";
    private static final float DURABILITY_WARNING_LEVEL = 0.16666667f;
    protected static final float TIME_TO_THROW = 1.0f;
    protected static final float TIME_TO_PICK_UP = 1.0f;
    protected static final float TIME_TO_DROP = 0.5f;
    public static final String AC_DROP = "DROP";
    public static final String AC_THROW = "THROW";
    public String defaultAction;
    protected String name = "某物";
    protected int image = 0;
    public boolean stackable = false;
    protected int quantity = 1;
    private int level = 0;
    private int durability = this.maxDurability();
    public boolean levelKnown = false;
    public boolean cursed;
    public boolean cursedKnown;
    public boolean unique = false;
    private static Comparator<Item> itemComparator = new Comparator<Item>(){

        @Override
        public int compare(Item lhs, Item rhs) {
            return Generator.Category.order(lhs) - Generator.Category.order(rhs);
        }
    };
    private static final String QUANTITY = "quantity";
    private static final String LEVEL = "level";
    private static final String LEVEL_KNOWN = "levelKnown";
    private static final String CURSED = "cursed";
    private static final String CURSED_KNOWN = "cursedKnown";
    private static final String DURABILITY = "durability";
    protected static Hero curUser = null;
    protected static Item curItem = null;
    protected static CellSelector.Listener thrower = new CellSelector.Listener(){

        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                curItem.cast(curUser, target);
            }
        }

        @Override
        public String prompt() {
            return "选择投掷方向";
        }

        @Override
        public String cursor() {
            return "cursor_target.png";
        }
    };

    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = new ArrayList<String>();
        actions.add(AC_DROP);
        actions.add(AC_THROW);
        return actions;
    }

    public boolean doPickUp(Hero hero) {
        if (this.collect(hero.belongings.backpack)) {
            GameScene.pickUp(this);
            Sample.INSTANCE.play("snd_item.mp3");
            hero.spendAndNext(1.0f);
            return true;
        }
        return false;
    }

    public void doDrop(Hero hero) {
        hero.spendAndNext(0.5f);
        Dungeon.level.drop((Item)this.detachAll((Bag)hero.belongings.backpack), (int)hero.pos).sprite.drop(hero.pos);
    }

    public void doThrow(Hero hero) {
        GameScene.selectCell(thrower);
    }

    public void execute(Hero hero, String action) {
        curUser = hero;
        curItem = this;
        if (action.equals(AC_DROP)) {
            this.doDrop(hero);
        } else if (action.equals(AC_THROW)) {
            this.doThrow(hero);
        }
    }

    public void execute(Hero hero) {
        this.execute(hero, this.defaultAction);
    }

    protected void onThrow(int cell) {
        Heap heap = Dungeon.level.drop(this, cell);
        if (!heap.isEmpty()) {
            heap.sprite.drop(cell);
        }
    }

    public boolean collect(Bag container) {
        ArrayList<Item> items = container.items;
        if (items.contains(this)) {
            return true;
        }
        for (Item item : items) {
            if (!(item instanceof Bag) || !((Bag)item).grab(this)) continue;
            return this.collect((Bag)item);
        }
        if (this.stackable) {
            Class<?> c = this.getClass();
            for (Item item : items) {
                if (item.getClass() != c) continue;
                item.quantity += this.quantity;
                item.updateQuickslot();
                return true;
            }
        }
        if (items.size() < container.size) {
            if (Dungeon.hero != null && Dungeon.hero.isAlive()) {
                Badges.validateItemLevelAquired(this);
            }
            items.add(this);
            QuickSlot.refresh();
            Collections.sort(items, itemComparator);
            return true;
        }
        GLog.n(TXT_PACK_FULL, this.name());
        return false;
    }

    public boolean collect() {
        return this.collect(Dungeon.hero.belongings.backpack);
    }

    public final Item detach(Bag container) {
        if (this.quantity <= 0) {
            return null;
        }
        if (this.quantity == 1) {
            return this.detachAll(container);
        }
        --this.quantity;
        this.updateQuickslot();
        try {
            Item detached = (Item)ClassReflection.newInstance(this.getClass());
            detached.onDetach();
            return detached;
        }
        catch (Exception e) {
            return null;
        }
    }

    public final Item detachAll(Bag container) {
        for (Item item : container.items) {
            Bag bag;
            if (item == this) {
                container.items.remove(this);
                item.onDetach();
                QuickSlot.refresh();
                return this;
            }
            if (!(item instanceof Bag) || !(bag = (Bag)item).contains(this)) continue;
            return this.detachAll(bag);
        }
        return this;
    }

    protected void onDetach() {
    }

    public int level() {
        return this.level;
    }

    public void level(int value) {
        this.level = value;
    }

    public int effectiveLevel() {
        return this.isBroken() ? 0 : this.level;
    }

    public Item upgrade() {
        this.cursed = false;
        this.cursedKnown = true;
        ++this.level;
        this.fix();
        return this;
    }

    public final Item upgrade(int n) {
        for (int i = 0; i < n; ++i) {
            this.upgrade();
        }
        return this;
    }

    public Item degrade() {
        --this.level;
        this.fix();
        return this;
    }

    public final Item degrade(int n) {
        for (int i = 0; i < n; ++i) {
            this.degrade();
        }
        return this;
    }

    public void use() {
        if (!Dungeon.legacy && this.level > 0 && !this.isBroken()) {
            int threshold = (int)((float)this.maxDurability() * 0.16666667f);
            if (this.durability-- >= threshold && threshold > this.durability && this.levelKnown) {
                GLog.w(TXT_GONNA_BREAK, this.name());
            }
            if (this.isBroken()) {
                this.getBroken();
                if (this.levelKnown) {
                    GLog.n(TXT_BROKEN, this.name());
                    Dungeon.hero.interrupt();
                    CharSprite sprite = Dungeon.hero.sprite;
                    PointF point = sprite.center().offset(0.0f, -16.0f);
                    if (this instanceof Weapon) {
                        sprite.parent.add(Degradation.weapon(point));
                    } else if (this instanceof Armor) {
                        sprite.parent.add(Degradation.armor(point));
                    } else if (this instanceof Ring) {
                        sprite.parent.add(Degradation.ring(point));
                    } else if (this instanceof Wand) {
                        sprite.parent.add(Degradation.wand(point));
                    }
                    Sample.INSTANCE.play("snd_degrade.mp3");
                }
            }
        }
    }

    public boolean isBroken() {
        return this.durability <= 0;
    }

    public void getBroken() {
    }

    public void fix() {
        this.durability = this.maxDurability();
    }

    public void polish() {
        if (this.durability < this.maxDurability()) {
            ++this.durability;
        }
    }

    public int durability() {
        return this.durability;
    }

    public int maxDurability(int lvl) {
        return 1;
    }

    public final int maxDurability() {
        return this.maxDurability(this.level);
    }

    public int visiblyUpgraded() {
        return this.levelKnown ? this.level : 0;
    }

    public boolean visiblyCursed() {
        return this.cursed && this.cursedKnown;
    }

    public boolean visiblyBroken() {
        return this.levelKnown && this.isBroken();
    }

    public boolean isUpgradable() {
        return true;
    }

    public boolean isIdentified() {
        return this.levelKnown && this.cursedKnown;
    }

    public boolean isEquipped(Hero hero) {
        return false;
    }

    public Item identify() {
        this.levelKnown = true;
        this.cursedKnown = true;
        return this;
    }

    public static void evoke(Hero hero) {
        hero.sprite.emitter().burst(Speck.factory(102), 5);
    }

    public String toString() {
        if (this.levelKnown && this.level != 0) {
            if (this.quantity > 1) {
                return Utils.format(TXT_TO_STRING_LVL_X, this.name(), this.level, this.quantity);
            }
            return Utils.format(TXT_TO_STRING_LVL, this.name(), this.level);
        }
        if (this.quantity > 1) {
            return Utils.format(TXT_TO_STRING_X, this.name(), this.quantity);
        }
        return Utils.format(TXT_TO_STRING, this.name());
    }

    public String name() {
        return this.name;
    }

    public final String trueName() {
        return this.name;
    }

    public int image() {
        return this.image;
    }

    public ItemSprite.Glowing glowing() {
        return null;
    }

    public String info() {
        return this.desc();
    }

    public String desc() {
        return "";
    }

    public int quantity() {
        return this.quantity;
    }

    public void quantity(int value) {
        this.quantity = value;
    }

    public int price() {
        return 0;
    }

    public int considerState(int price) {
        if (this.cursed && this.cursedKnown) {
            price /= 2;
        }
        if (this.levelKnown) {
            if (this.level > 0) {
                price *= this.level + 1;
                if (this.isBroken()) {
                    price /= 2;
                }
            } else if (this.level < 0) {
                price /= 1 - this.level;
            }
        }
        if (price < 1) {
            price = 1;
        }
        return price;
    }

    public static Item virtual(Class<? extends Item> cl) {
        try {
            Item item = ClassReflection.newInstance(cl);
            item.quantity = 0;
            return item;
        }
        catch (Exception e) {
            return null;
        }
    }

    public Item random() {
        return this;
    }

    public String status() {
        return this.quantity != 1 ? Integer.toString(this.quantity) : null;
    }

    public void updateQuickslot() {
        if (this.stackable) {
            Class<?> cl = this.getClass();
            if (QuickSlot.primaryValue == cl || QuickSlot.secondaryValue == cl) {
                QuickSlot.refresh();
            }
        } else if (QuickSlot.primaryValue == this || QuickSlot.secondaryValue == this) {
            QuickSlot.refresh();
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(QUANTITY, this.quantity);
        bundle.put(LEVEL, this.level);
        bundle.put(LEVEL_KNOWN, this.levelKnown);
        bundle.put(CURSED, this.cursed);
        bundle.put(CURSED_KNOWN, this.cursedKnown);
        if (this.isUpgradable()) {
            bundle.put(DURABILITY, this.durability);
        }
        QuickSlot.save(bundle, this);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        this.quantity = bundle.getInt(QUANTITY);
        this.levelKnown = bundle.getBoolean(LEVEL_KNOWN);
        this.cursedKnown = bundle.getBoolean(CURSED_KNOWN);
        int level = bundle.getInt(LEVEL);
        if (level > 0) {
            this.upgrade(level);
        } else if (level < 0) {
            this.degrade(-level);
        }
        this.cursed = bundle.getBoolean(CURSED);
        if (this.isUpgradable()) {
            this.durability = bundle.getInt(DURABILITY);
        }
        if (this.durability <= 0) {
            this.durability = this.maxDurability(level);
        }
        QuickSlot.restore(bundle, this);
    }

    public void cast(final Hero user, int dst) {
        final int cell = Ballistica.cast(user.pos, dst, false, true);
        user.sprite.zap(cell);
        user.busy();
        GameScene.play("snd_miss.mp3", 0.6f, 1.5f, user.pos);
        Char enemy = Actor.findChar(cell);
        QuickSlot.target(this, enemy);
        float delay = 1.0f;
        if (this instanceof MissileWeapon) {
            SnipersMark mark;
            delay *= ((MissileWeapon)this).speedFactor(user);
            if (enemy != null && (mark = user.buff(SnipersMark.class)) != null) {
                if (mark.object == enemy.id()) {
                    delay *= 0.5f;
                }
                user.remove(mark);
            }
        }
        final float finalDelay = delay;
        ((MissileSprite)user.sprite.parent.recycle(MissileSprite.class)).reset(user.pos, cell, this, new Callback(){

            @Override
            public void call() {
                Item.this.detach(user.belongings.backpack).onThrow(cell);
                user.spendAndNext(finalDelay);
            }
        });
    }
}

