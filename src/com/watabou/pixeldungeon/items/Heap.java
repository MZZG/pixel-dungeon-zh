/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.actors.buffs.Frost;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.Mimic;
import com.watabou.pixeldungeon.actors.mobs.Wraith;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.effects.Splash;
import com.watabou.pixeldungeon.effects.particles.ElmoParticle;
import com.watabou.pixeldungeon.effects.particles.FlameParticle;
import com.watabou.pixeldungeon.effects.particles.ShadowParticle;
import com.watabou.pixeldungeon.items.Dewdrop;
import com.watabou.pixeldungeon.items.Generator;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.food.ChargrilledMeat;
import com.watabou.pixeldungeon.items.food.FrozenCarpaccio;
import com.watabou.pixeldungeon.items.food.MysteryMeat;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.plants.Plant;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import java.util.LinkedList;

public class Heap
implements Bundlable {
    private static final String TXT_MIMIC = "这是个宝箱怪！";
    private static final int SEEDS_TO_POTION = 3;
    private static final float FADE_TIME = 0.6f;
    public Type type = Type.HEAP;
    public int pos = 0;
    public ItemSprite sprite;
    public LinkedList<Item> items = new LinkedList();
    private static final String POS = "pos";
    private static final String TYPE = "type";
    private static final String ITEMS = "items";

    public int image() {
        switch (this.type) {
            case HEAP: 
            case FOR_SALE: {
                return this.size() > 0 ? this.items.peek().image() : 0;
            }
            case CHEST: 
            case MIMIC: {
                return 11;
            }
            case LOCKED_CHEST: {
                return 12;
            }
            case CRYSTAL_CHEST: {
                return 105;
            }
            case TOMB: {
                return 13;
            }
            case SKELETON: {
                return 0;
            }
            case HIDDEN: {
                return 119;
            }
        }
        return 0;
    }

    public ItemSprite.Glowing glowing() {
        return (this.type == Type.HEAP || this.type == Type.FOR_SALE) && this.items.size() > 0 ? this.items.peek().glowing() : null;
    }

    public void open(Hero hero) {
        block0 : switch (this.type) {
            case MIMIC: {
                if (Mimic.spawnAt(this.pos, this.items) != null) {
                    GLog.n(TXT_MIMIC, new Object[0]);
                    this.destroy();
                    break;
                }
                this.type = Type.CHEST;
                break;
            }
            case TOMB: {
                Wraith.spawnAround(hero.pos);
                break;
            }
            case SKELETON: {
                CellEmitter.center(this.pos).start(Speck.factory(105), 0.1f, 3);
                for (Item item : this.items) {
                    if (!item.cursed) continue;
                    if (Wraith.spawnAt(this.pos) == null) {
                        hero.sprite.emitter().burst(ShadowParticle.CURSE, 6);
                        hero.damage(hero.HP / 2, this);
                    }
                    GameScene.play("snd_cursed.mp3", this.pos);
                    break block0;
                }
                break;
            }
            case HIDDEN: {
                this.sprite.alpha(0.0f);
                this.sprite.parent.add(new AlphaTweener(this.sprite, 1.0f, 0.6f));
                break;
            }
        }
        if (this.type != Type.MIMIC) {
            this.type = Type.HEAP;
            this.sprite.link();
            this.sprite.drop();
        }
    }

    public int size() {
        return this.items.size();
    }

    public Item pickUp() {
        Item item = this.items.removeFirst();
        if (this.items.isEmpty()) {
            this.destroy();
        } else if (this.sprite != null) {
            this.sprite.view(this.image(), this.glowing());
        }
        return item;
    }

    public Item peek() {
        return this.items.peek();
    }

    public void drop(Item item) {
        if (item.stackable) {
            Class<?> c = item.getClass();
            for (Item i : this.items) {
                if (i.getClass() != c) continue;
                i.quantity += item.quantity;
                item = i;
                break;
            }
            this.items.remove(item);
        }
        if (item instanceof Dewdrop) {
            this.items.add(item);
        } else {
            this.items.addFirst(item);
        }
        if (this.sprite != null) {
            this.sprite.view(this.image(), this.glowing());
        }
    }

    public void replace(Item a, Item b) {
        int index = this.items.indexOf(a);
        if (index != -1) {
            this.items.remove(index);
            this.items.add(index, b);
        }
    }

    public void burn() {
        Mimic m;
        if (this.type == Type.MIMIC && (m = Mimic.spawnAt(this.pos, this.items)) != null) {
            Buff.affect(m, Burning.class).reignite(m);
            m.sprite.emitter().burst(FlameParticle.FACTORY, 5);
            this.destroy();
        }
        if (this.type != Type.HEAP) {
            return;
        }
        boolean burnt = false;
        boolean evaporated = false;
        for (Item item : this.items.toArray(new Item[0])) {
            if (item instanceof Scroll) {
                this.items.remove(item);
                burnt = true;
                continue;
            }
            if (item instanceof Dewdrop) {
                this.items.remove(item);
                evaporated = true;
                continue;
            }
            if (!(item instanceof MysteryMeat)) continue;
            this.replace(item, ChargrilledMeat.cook((MysteryMeat)item));
            burnt = true;
        }
        if (burnt || evaporated) {
            if (Dungeon.visible[this.pos]) {
                if (burnt) {
                    Heap.burnFX(this.pos);
                } else {
                    Heap.evaporateFX(this.pos);
                }
            }
            if (this.isEmpty()) {
                this.destroy();
            } else if (this.sprite != null) {
                this.sprite.view(this.image(), this.glowing());
            }
        }
    }

    public void freeze() {
        Mimic m;
        if (this.type == Type.MIMIC && (m = Mimic.spawnAt(this.pos, this.items)) != null) {
            Buff.prolong(m, Frost.class, Frost.duration(m) * Random.Float(1.0f, 1.5f));
            this.destroy();
        }
        if (this.type != Type.HEAP) {
            return;
        }
        boolean frozen = false;
        for (Item item : this.items.toArray(new Item[0])) {
            if (!(item instanceof MysteryMeat)) continue;
            this.replace(item, FrozenCarpaccio.cook((MysteryMeat)item));
            frozen = true;
        }
        if (frozen) {
            if (this.isEmpty()) {
                this.destroy();
            } else if (this.sprite != null) {
                this.sprite.view(this.image(), this.glowing());
            }
        }
    }

    public Item transmute() {
        CellEmitter.get(this.pos).burst(Speck.factory(12), 3);
        Splash.at(this.pos, 0xFFFFFF, 3);
        float[] chances = new float[this.items.size()];
        int count = 0;
        int index = 0;
        for (Item item : this.items) {
            if (item instanceof Plant.Seed) {
                count += item.quantity;
                chances[index++] = item.quantity;
                continue;
            }
            count = 0;
            break;
        }
        if (count >= 3) {
            CellEmitter.get(this.pos).burst(Speck.factory(7), 6);
            GameScene.play("snd_puff.mp3", this.pos);
            if (Random.Int(count) == 0) {
                CellEmitter.center(this.pos).burst(Speck.factory(102), 3);
                this.destroy();
                Statistics.add(Statistics.Type.ALCHEMY, 1);
                Badges.validatePotionsCooked();
                return Generator.random(Generator.Category.POTION);
            }
            Plant.Seed proto = (Plant.Seed)this.items.get(Random.chances(chances));
            Class<? extends Item> itemClass = proto.alchemyClass;
            this.destroy();
            Statistics.add(Statistics.Type.ALCHEMY, 1);
            Badges.validatePotionsCooked();
            if (itemClass == null) {
                return Generator.random(Generator.Category.POTION);
            }
            try {
                return ClassReflection.newInstance(itemClass);
            }
            catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static void burnFX(int pos) {
        CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
        GameScene.play("snd_burning.mp3", pos);
    }

    public static void evaporateFX(int pos) {
        CellEmitter.get(pos).burst(Speck.factory(13), 5);
    }

    public boolean isEmpty() {
        return this.items == null || this.items.size() == 0;
    }

    public void destroy() {
        Dungeon.level.heaps.remove(this.pos);
        if (this.sprite != null) {
            this.sprite.kill();
        }
        this.items.clear();
        this.items = null;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        this.pos = bundle.getInt(POS);
        this.type = Type.valueOf(bundle.getString(TYPE));
        this.items = new LinkedList<Item>();
        for (com.watabou.utils.Bundlable b : bundle.getCollection(ITEMS)) {
            this.items.add((Item)b);
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(POS, this.pos);
        bundle.put(TYPE, this.type.toString());
        bundle.put(ITEMS, this.items);
    }

    public static enum Type {
        HEAP,
        FOR_SALE,
        CHEST,
        LOCKED_CHEST,
        CRYSTAL_CHEST,
        TOMB,
        SKELETON,
        MIMIC,
        HIDDEN;

    }
}

