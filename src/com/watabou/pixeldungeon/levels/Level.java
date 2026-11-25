/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.levels;

import com.watabou.noosa.Scene;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Alchemy;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.WellWater;
import com.watabou.pixeldungeon.actors.buffs.Awareness;
import com.watabou.pixeldungeon.actors.buffs.Blindness;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.MindVision;
import com.watabou.pixeldungeon.actors.buffs.Shadows;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.actors.mobs.Bestiary;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.particles.FlowParticle;
import com.watabou.pixeldungeon.effects.particles.WindParticle;
import com.watabou.pixeldungeon.items.Generator;
import com.watabou.pixeldungeon.items.Gold;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.bags.ScrollHolder;
import com.watabou.pixeldungeon.items.bags.SeedPouch;
import com.watabou.pixeldungeon.items.food.Food;
import com.watabou.pixeldungeon.items.potions.PotionOfHealing;
import com.watabou.pixeldungeon.items.potions.PotionOfStrength;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfEnchantment;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.watabou.pixeldungeon.levels.Terrain;
import com.watabou.pixeldungeon.levels.features.Chasm;
import com.watabou.pixeldungeon.levels.features.Door;
import com.watabou.pixeldungeon.levels.features.HighGrass;
import com.watabou.pixeldungeon.levels.painters.Painter;
import com.watabou.pixeldungeon.levels.traps.AlarmTrap;
import com.watabou.pixeldungeon.levels.traps.FireTrap;
import com.watabou.pixeldungeon.levels.traps.GrippingTrap;
import com.watabou.pixeldungeon.levels.traps.LightningTrap;
import com.watabou.pixeldungeon.levels.traps.ParalyticTrap;
import com.watabou.pixeldungeon.levels.traps.PoisonTrap;
import com.watabou.pixeldungeon.levels.traps.SummoningTrap;
import com.watabou.pixeldungeon.levels.traps.ToxicTrap;
import com.watabou.pixeldungeon.mechanics.ShadowCaster;
import com.watabou.pixeldungeon.plants.Plant;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Level
implements Bundlable {
    public static final int WIDTH = 32;
    public static final int HEIGHT = 32;
    public static final int LENGTH = 1024;
    public static final int[] NEIGHBOURS4 = new int[]{-32, 1, 32, -1};
    public static final int[] NEIGHBOURS8 = new int[]{1, -1, 32, -32, 33, -31, 31, -33};
    public static final int[] NEIGHBOURS9 = new int[]{0, 1, -1, 32, -32, 33, -31, 31, -33};
    protected static final float TIME_TO_RESPAWN = 50.0f;
    private static final String TXT_HIDDEN_PLATE_CLICKS = "隐藏的踏板发出咔哒一声！";
    public static boolean resizingNeeded;
    public static int loadedMapSize;
    public int[] map;
    public boolean[] visited;
    public boolean[] mapped;
    public int viewDistance = Dungeon.isChallenged(32) ? 3 : 8;
    public static boolean[] fieldOfView;
    public static boolean[] passable;
    public static boolean[] losBlocking;
    public static boolean[] flamable;
    public static boolean[] secret;
    public static boolean[] solid;
    public static boolean[] avoid;
    public static boolean[] water;
    public static boolean[] pit;
    public static boolean[] discoverable;
    public Feeling feeling = Feeling.NONE;
    public int entrance;
    public int exit;
    public HashSet<Mob> mobs;
    public SparseArray<Heap> heaps;
    public HashMap<Class<? extends Blob>, Blob> blobs;
    public SparseArray<Plant> plants;
    protected ArrayList<Item> itemsToSpawn = new ArrayList();
    public int color1 = 17408;
    public int color2 = 0x88CC44;
    protected static boolean pitRoomNeeded;
    protected static boolean weakFloorCreated;
    private static final String MAP = "map";
    private static final String VISITED = "visited";
    private static final String MAPPED = "mapped";
    private static final String ENTRANCE = "entrance";
    private static final String EXIT = "exit";
    private static final String HEAPS = "heaps";
    private static final String PLANTS = "plants";
    private static final String MOBS = "mobs";
    private static final String BLOBS = "blobs";

    public void create() {
        resizingNeeded = false;
        this.map = new int[1024];
        this.visited = new boolean[1024];
        Arrays.fill(this.visited, false);
        this.mapped = new boolean[1024];
        Arrays.fill(this.mapped, false);
        this.mobs = new HashSet();
        this.heaps = new SparseArray();
        this.blobs = new HashMap();
        this.plants = new SparseArray();
        if (!Dungeon.bossLevel()) {
            this.addItemToSpawn(Generator.random(Generator.Category.FOOD));
            if (Dungeon.posNeeded()) {
                this.addItemToSpawn(new PotionOfStrength());
                ++Dungeon.potionOfStrength;
            }
            if (Dungeon.souNeeded()) {
                this.addItemToSpawn(new ScrollOfUpgrade());
                ++Dungeon.scrollsOfUpgrade;
            }
            if (Dungeon.soeNeeded()) {
                this.addItemToSpawn(new ScrollOfEnchantment());
                ++Dungeon.scrollsOfEnchantment;
            }
            if (Dungeon.depth > 1) {
                switch (Random.Int(10)) {
                    case 0: {
                        if (Dungeon.bossLevel(Dungeon.depth + 1)) break;
                        this.feeling = Feeling.CHASM;
                        break;
                    }
                    case 1: {
                        this.feeling = Feeling.WATER;
                        break;
                    }
                    case 2: {
                        this.feeling = Feeling.GRASS;
                    }
                }
            }
        }
        boolean pitNeeded = Dungeon.depth > 1 && weakFloorCreated;
        do {
            Arrays.fill(this.map, this.feeling == Feeling.CHASM ? 0 : 4);
            pitRoomNeeded = pitNeeded;
            weakFloorCreated = false;
        } while (!this.build());
        this.decorate();
        this.buildFlagMaps();
        this.cleanWalls();
        this.createMobs();
        this.createItems();
    }

    public void reset() {
        for (Mob mob : this.mobs.toArray(new Mob[0])) {
            if (mob.reset()) continue;
            this.mobs.remove(mob);
        }
        this.createMobs();
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        this.mobs = new HashSet();
        this.heaps = new SparseArray();
        this.blobs = new HashMap();
        this.plants = new SparseArray();
        this.map = bundle.getIntArray(MAP);
        this.visited = bundle.getBooleanArray(VISITED);
        this.mapped = bundle.getBooleanArray(MAPPED);
        this.entrance = bundle.getInt(ENTRANCE);
        this.exit = bundle.getInt(EXIT);
        weakFloorCreated = false;
        this.adjustMapSize();
        Collection<Bundlable> collection = bundle.getCollection(HEAPS);
        for (Bundlable h : collection) {
            Heap heap = (Heap)h;
            if (resizingNeeded) {
                heap.pos = this.adjustPos(heap.pos);
            }
            this.heaps.put(heap.pos, heap);
        }
        collection = bundle.getCollection(PLANTS);
        for (Bundlable p : collection) {
            Plant plant = (Plant)p;
            if (resizingNeeded) {
                plant.pos = this.adjustPos(plant.pos);
            }
            this.plants.put(plant.pos, plant);
        }
        collection = bundle.getCollection(MOBS);
        for (Bundlable m : collection) {
            Mob mob = (Mob)m;
            if (mob == null) continue;
            if (resizingNeeded) {
                mob.pos = this.adjustPos(mob.pos);
            }
            this.mobs.add(mob);
        }
        collection = bundle.getCollection(BLOBS);
        for (Bundlable b : collection) {
            Blob blob = (Blob)b;
            this.blobs.put(blob.getClass(), blob);
        }
        this.buildFlagMaps();
        this.cleanWalls();
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(MAP, this.map);
        bundle.put(VISITED, this.visited);
        bundle.put(MAPPED, this.mapped);
        bundle.put(ENTRANCE, this.entrance);
        bundle.put(EXIT, this.exit);
        bundle.put(HEAPS, this.heaps.valuesAsList());
        bundle.put(PLANTS, this.plants.valuesAsList());
        bundle.put(MOBS, this.mobs);
        bundle.put(BLOBS, this.blobs.values());
    }

    public int tunnelTile() {
        return this.feeling == Feeling.CHASM ? 14 : 1;
    }

    private void adjustMapSize() {
        if (this.map.length < 1024) {
            resizingNeeded = true;
            loadedMapSize = (int)Math.sqrt(this.map.length);
            int[] map = new int[1024];
            Arrays.fill(map, 4);
            boolean[] visited = new boolean[1024];
            Arrays.fill(visited, false);
            boolean[] mapped = new boolean[1024];
            Arrays.fill(mapped, false);
            for (int i = 0; i < loadedMapSize; ++i) {
                System.arraycopy(this.map, i * loadedMapSize, map, i * 32, loadedMapSize);
                System.arraycopy(this.visited, i * loadedMapSize, visited, i * 32, loadedMapSize);
                System.arraycopy(this.mapped, i * loadedMapSize, mapped, i * 32, loadedMapSize);
            }
            this.map = map;
            this.visited = visited;
            this.mapped = mapped;
            this.entrance = this.adjustPos(this.entrance);
            this.exit = this.adjustPos(this.exit);
        } else {
            resizingNeeded = false;
        }
    }

    public int adjustPos(int pos) {
        return pos / loadedMapSize * 32 + pos % loadedMapSize;
    }

    public String tilesTex() {
        return null;
    }

    public String waterTex() {
        return null;
    }

    protected abstract boolean build();

    protected abstract void decorate();

    protected abstract void createMobs();

    protected abstract void createItems();

    public void addVisuals(Scene scene) {
        for (int i = 0; i < 1024; ++i) {
            if (!pit[i]) continue;
            scene.add(new WindParticle.Wind(i));
            if (i < 32 || !water[i - 32]) continue;
            scene.add(new FlowParticle.Flow(i - 32));
        }
    }

    public int nMobs() {
        return 0;
    }

    public Actor respawner() {
        return new Actor(){

            @Override
            protected boolean act() {
                if (Level.this.mobs.size() < Level.this.nMobs()) {
                    Mob mob = Bestiary.mutable(Dungeon.depth);
                    mob.state = mob.WANDERING;
                    mob.pos = Level.this.randomRespawnCell();
                    if (Dungeon.hero.isAlive() && mob.pos != -1) {
                        GameScene.add(mob);
                        if (Statistics.getBoolean(Statistics.Type.AMULET)) {
                            mob.beckon(Dungeon.hero.pos);
                        }
                    }
                }
                this.spend(Dungeon.nightMode || Statistics.getBoolean(Statistics.Type.AMULET) ? 25.0f : 50.0f);
                return true;
            }
        };
    }

    public int randomRespawnCell() {
        int cell;
        while (!passable[cell = Random.Int(1024)] || Dungeon.visible[cell] || Actor.findChar(cell) != null) {
        }
        return cell;
    }

    public int randomDestination() {
        int cell;
        while (!passable[cell = Random.Int(1024)]) {
        }
        return cell;
    }

    public void addItemToSpawn(Item item) {
        if (item != null) {
            this.itemsToSpawn.add(item);
        }
    }

    public Item itemToSpanAsPrize() {
        if (Random.Int(this.itemsToSpawn.size() + 1) > 0) {
            Item item = Random.element(this.itemsToSpawn);
            this.itemsToSpawn.remove(item);
            return item;
        }
        return null;
    }

    private void buildFlagMaps() {
        int i;
        for (int i2 = 0; i2 < 1024; ++i2) {
            int flags = Terrain.flags[this.map[i2]];
            Level.passable[i2] = (flags & 1) != 0;
            Level.losBlocking[i2] = (flags & 2) != 0;
            Level.flamable[i2] = (flags & 4) != 0;
            Level.secret[i2] = (flags & 8) != 0;
            Level.solid[i2] = (flags & 0x10) != 0;
            Level.avoid[i2] = (flags & 0x20) != 0;
            Level.water[i2] = (flags & 0x40) != 0;
            Level.pit[i2] = (flags & 0x80) != 0;
        }
        int lastRow = 992;
        for (i = 0; i < 32; ++i) {
            Level.avoid[i] = false;
            Level.passable[i] = false;
            Level.avoid[lastRow + i] = false;
            Level.passable[lastRow + i] = false;
        }
        for (i = 32; i < lastRow; i += 32) {
            Level.avoid[i] = false;
            Level.passable[i] = false;
            Level.avoid[i + 32 - 1] = false;
            Level.passable[i + 32 - 1] = false;
        }
        for (i = 32; i < 992; ++i) {
            if (water[i]) {
                this.map[i] = this.getWaterTile(i);
            }
            if (!pit[i] || pit[i - 32]) continue;
            int c = this.map[i - 32];
            this.map[i] = c == 14 || c == 36 ? 44 : (water[i - 32] ? 46 : ((Terrain.flags[c] & 0x100) != 0 ? 45 : 43));
        }
    }

    private int getWaterTile(int pos) {
        int t = 48;
        for (int j = 0; j < NEIGHBOURS4.length; ++j) {
            if ((Terrain.flags[this.map[pos + NEIGHBOURS4[j]]] & 0x100) == 0) continue;
            t += 1 << j;
        }
        return t;
    }

    public void destroy(int pos) {
        if ((Terrain.flags[this.map[pos]] & 0x100) == 0) {
            Level.set(pos, 9);
        } else {
            boolean flood = false;
            for (int j = 0; j < NEIGHBOURS4.length; ++j) {
                if (!water[pos + NEIGHBOURS4[j]]) continue;
                flood = true;
                break;
            }
            if (flood) {
                Level.set(pos, this.getWaterTile(pos));
            } else {
                Level.set(pos, 9);
            }
        }
    }

    private void cleanWalls() {
        for (int i = 0; i < 1024; ++i) {
            int n;
            int j;
            boolean d = false;
            for (j = 0; j < NEIGHBOURS9.length; ++j) {
                n = i + NEIGHBOURS9[j];
                if (n < 0 || n >= 1024 || this.map[n] == 4 || this.map[n] == 12) continue;
                d = true;
                break;
            }
            if (d) {
                d = false;
                for (j = 0; j < NEIGHBOURS9.length; ++j) {
                    n = i + NEIGHBOURS9[j];
                    if (n < 0 || n >= 1024 || pit[n]) continue;
                    d = true;
                    break;
                }
            }
            Level.discoverable[i] = d;
        }
    }

    public static void set(int cell, int terrain) {
        Painter.set(Dungeon.level, cell, terrain);
        int flags = Terrain.flags[terrain];
        Level.passable[cell] = (flags & 1) != 0;
        Level.losBlocking[cell] = (flags & 2) != 0;
        Level.flamable[cell] = (flags & 4) != 0;
        Level.secret[cell] = (flags & 8) != 0;
        Level.solid[cell] = (flags & 0x10) != 0;
        Level.avoid[cell] = (flags & 0x20) != 0;
        Level.pit[cell] = (flags & 0x80) != 0;
        Level.water[cell] = terrain == 63 || terrain >= 48;
    }

    public Heap drop(Item item, int cell) {
        Heap heap;
        if (Dungeon.isChallenged(1) && item instanceof Food) {
            item = new Gold(item.price());
        } else if (Dungeon.isChallenged(2) && item instanceof Armor) {
            item = new Gold(item.price());
        } else if (Dungeon.isChallenged(4) && item instanceof PotionOfHealing) {
            item = new Gold(item.price());
        } else if (Dungeon.isChallenged(8) && item instanceof SeedPouch) {
            item = new Gold(item.price());
        } else if (Dungeon.isChallenged(64) && (item instanceof Scroll || item instanceof ScrollHolder) && !(item instanceof ScrollOfUpgrade)) {
            item = new Gold(item.price());
        }
        if (this.map[cell] == 42 && !(item instanceof Plant.Seed)) {
            int n;
            while (this.map[n = cell + NEIGHBOURS8[Random.Int(8)]] != 14) {
            }
            cell = n;
        }
        if ((heap = (Heap)this.heaps.get(cell)) == null) {
            heap = new Heap();
            heap.pos = cell;
            if (this.map[cell] == 0 || Dungeon.level != null && pit[cell]) {
                Dungeon.dropToChasm(item);
                GameScene.discard(heap);
            } else {
                this.heaps.put(cell, heap);
                GameScene.add(heap);
            }
        } else if (heap.type == Heap.Type.LOCKED_CHEST || heap.type == Heap.Type.CRYSTAL_CHEST) {
            int n;
            while (!passable[n = cell + NEIGHBOURS8[Random.Int(8)]] && !avoid[n]) {
            }
            return this.drop(item, n);
        }
        heap.drop(item);
        if (Dungeon.level != null) {
            this.press(cell, null);
        }
        return heap;
    }

    public Plant plant(Plant.Seed seed, int pos) {
        Plant plant = (Plant)this.plants.get(pos);
        if (plant != null) {
            plant.wither();
        }
        plant = seed.couch(pos);
        this.plants.put(pos, plant);
        GameScene.add(plant);
        return plant;
    }

    public void uproot(int pos) {
        this.plants.remove(pos);
    }

    public int pitCell() {
        return this.randomRespawnCell();
    }

    public void press(int cell, Char ch) {
        Plant plant;
        if (pit[cell] && ch == Dungeon.hero) {
            Chasm.heroFall(cell);
            return;
        }
        boolean trap = false;
        switch (this.map[cell]) {
            case 18: {
                GLog.i(TXT_HIDDEN_PLATE_CLICKS, new Object[0]);
            }
            case 17: {
                trap = true;
                ToxicTrap.trigger(cell, ch);
                break;
            }
            case 20: {
                GLog.i(TXT_HIDDEN_PLATE_CLICKS, new Object[0]);
            }
            case 19: {
                trap = true;
                FireTrap.trigger(cell, ch);
                break;
            }
            case 22: {
                GLog.i(TXT_HIDDEN_PLATE_CLICKS, new Object[0]);
            }
            case 21: {
                trap = true;
                ParalyticTrap.trigger(cell, ch);
                break;
            }
            case 28: {
                GLog.i(TXT_HIDDEN_PLATE_CLICKS, new Object[0]);
            }
            case 27: {
                trap = true;
                PoisonTrap.trigger(cell, ch);
                break;
            }
            case 31: {
                GLog.i(TXT_HIDDEN_PLATE_CLICKS, new Object[0]);
            }
            case 30: {
                trap = true;
                AlarmTrap.trigger(cell, ch);
                break;
            }
            case 33: {
                GLog.i(TXT_HIDDEN_PLATE_CLICKS, new Object[0]);
            }
            case 32: {
                trap = true;
                LightningTrap.trigger(cell, ch);
                break;
            }
            case 38: {
                GLog.i(TXT_HIDDEN_PLATE_CLICKS, new Object[0]);
            }
            case 37: {
                trap = true;
                GrippingTrap.trigger(cell, ch);
                break;
            }
            case 40: {
                GLog.i(TXT_HIDDEN_PLATE_CLICKS, new Object[0]);
            }
            case 39: {
                trap = true;
                SummoningTrap.trigger(cell, ch);
                break;
            }
            case 15: {
                HighGrass.trample(this, cell, ch);
                break;
            }
            case 34: {
                WellWater.affectCell(cell);
                break;
            }
            case 42: {
                if (ch != null) break;
                Alchemy.transmute(cell);
                break;
            }
            case 5: {
                Door.enter(cell);
            }
        }
        if (trap) {
            GameScene.play("snd_trap.mp3", cell);
            if (ch == Dungeon.hero) {
                Dungeon.hero.interrupt();
            }
            Level.set(cell, 23);
            GameScene.updateMap(cell);
        }
        if ((plant = (Plant)this.plants.get(cell)) != null) {
            plant.activate(ch);
        }
    }

    public void mobPress(Mob mob) {
        Plant plant;
        int cell = mob.pos;
        if (pit[cell] && !mob.flying) {
            Chasm.mobFall(mob);
            return;
        }
        boolean trap = true;
        switch (this.map[cell]) {
            case 17: {
                ToxicTrap.trigger(cell, mob);
                break;
            }
            case 19: {
                FireTrap.trigger(cell, mob);
                break;
            }
            case 21: {
                ParalyticTrap.trigger(cell, mob);
                break;
            }
            case 27: {
                PoisonTrap.trigger(cell, mob);
                break;
            }
            case 30: {
                AlarmTrap.trigger(cell, mob);
                break;
            }
            case 32: {
                LightningTrap.trigger(cell, mob);
                break;
            }
            case 37: {
                GrippingTrap.trigger(cell, mob);
                break;
            }
            case 39: {
                SummoningTrap.trigger(cell, mob);
                break;
            }
            case 5: {
                Door.enter(cell);
            }
            default: {
                trap = false;
            }
        }
        if (trap) {
            if (Dungeon.visible[cell]) {
                GameScene.play("snd_trap.mp3", cell);
            }
            Level.set(cell, 23);
            GameScene.updateMap(cell);
        }
        if ((plant = (Plant)this.plants.get(cell)) != null) {
            plant.activate(mob);
        }
    }

    public boolean[] updateFieldOfView(Char c) {
        boolean sighted;
        int cx = c.pos % 32;
        int cy = c.pos / 32;
        boolean bl = sighted = c.buff(Blindness.class) == null && c.buff(Shadows.class) == null && c.isAlive();
        if (sighted) {
            ShadowCaster.castShadow(cx, cy, fieldOfView, c.viewDistance);
        } else {
            Arrays.fill(fieldOfView, false);
        }
        int sense = 1;
        if (c.isAlive()) {
            for (Buff buff : c.buffs(MindVision.class)) {
                sense = Math.max(((MindVision)buff).distance, sense);
            }
        }
        if (sighted && sense > 1 || !sighted) {
            int ax = Math.max(0, cx - sense);
            int n = Math.min(cx + sense, 31);
            int ay = Math.max(0, cy - sense);
            int by = Math.min(cy + sense, 31);
            int len = n - ax + 1;
            int pos = ax + ay * 32;
            int y = ay;
            while (y <= by) {
                Arrays.fill(fieldOfView, pos, pos + len, true);
                ++y;
                pos += 32;
            }
            for (int i = 0; i < 1024; ++i) {
                int n2 = i;
                fieldOfView[n2] = fieldOfView[n2] & discoverable[i];
            }
        }
        if (c.isAlive()) {
            int p;
            if (c.buff(MindVision.class) != null) {
                for (Mob mob : this.mobs) {
                    p = mob.pos;
                    Level.fieldOfView[p] = true;
                    Level.fieldOfView[p + 1] = true;
                    Level.fieldOfView[p - 1] = true;
                    Level.fieldOfView[p + 32 + 1] = true;
                    Level.fieldOfView[p + 32 - 1] = true;
                    Level.fieldOfView[p - 32 + 1] = true;
                    Level.fieldOfView[p - 32 - 1] = true;
                    Level.fieldOfView[p + 32] = true;
                    Level.fieldOfView[p - 32] = true;
                }
            } else if (c == Dungeon.hero && ((Hero)c).heroClass == HeroClass.HUNTRESS) {
                for (Mob mob : this.mobs) {
                    p = mob.pos;
                    if (Level.distance(c.pos, p) != 2) continue;
                    Level.fieldOfView[p] = true;
                    Level.fieldOfView[p + 1] = true;
                    Level.fieldOfView[p - 1] = true;
                    Level.fieldOfView[p + 32 + 1] = true;
                    Level.fieldOfView[p + 32 - 1] = true;
                    Level.fieldOfView[p - 32 + 1] = true;
                    Level.fieldOfView[p - 32 - 1] = true;
                    Level.fieldOfView[p + 32] = true;
                    Level.fieldOfView[p - 32] = true;
                }
            }
            if (c.buff(Awareness.class) != null) {
                for (Heap heap : this.heaps.values()) {
                    p = heap.pos;
                    Level.fieldOfView[p] = true;
                    Level.fieldOfView[p + 1] = true;
                    Level.fieldOfView[p - 1] = true;
                    Level.fieldOfView[p + 32 + 1] = true;
                    Level.fieldOfView[p + 32 - 1] = true;
                    Level.fieldOfView[p - 32 + 1] = true;
                    Level.fieldOfView[p - 32 - 1] = true;
                    Level.fieldOfView[p + 32] = true;
                    Level.fieldOfView[p - 32] = true;
                }
            }
        }
        return fieldOfView;
    }

    public static int distance(int a, int b) {
        int ax = a % 32;
        int ay = a / 32;
        int bx = b % 32;
        int by = b / 32;
        return Math.max(Math.abs(ax - bx), Math.abs(ay - by));
    }

    public static boolean adjacent(int a, int b) {
        int diff = Math.abs(a - b);
        return diff == 1 || diff == 32 || diff == 33 || diff == 31;
    }

    public String tileName(int tile) {
        if (tile >= 48) {
            return this.tileName(63);
        }
        if (tile != 0 && (Terrain.flags[tile] & 0x80) != 0) {
            return this.tileName(0);
        }
        switch (tile) {
            case 0: {
                return "深渊";
            }
            case 1: 
            case 14: 
            case 18: 
            case 20: 
            case 22: 
            case 24: 
            case 28: 
            case 31: 
            case 33: {
                return "地板";
            }
            case 2: {
                return "草地";
            }
            case 63: {
                return "水潭";
            }
            case 4: 
            case 12: 
            case 16: {
                return "墙壁";
            }
            case 5: {
                return "关着的门";
            }
            case 6: {
                return "开着的门";
            }
            case 7: {
                return "楼层入口";
            }
            case 8: {
                return "楼层出口";
            }
            case 9: {
                return "余烬";
            }
            case 10: {
                return "锁着的门";
            }
            case 11: {
                return "基座";
            }
            case 13: {
                return "路障";
            }
            case 15: {
                return "高草";
            }
            case 25: {
                return "封锁的楼层出口";
            }
            case 26: {
                return "开启的楼层出口";
            }
            case 29: {
                return "告示牌";
            }
            case 34: {
                return "井";
            }
            case 3: {
                return "干涸的井";
            }
            case 35: 
            case 36: {
                return "雕像";
            }
            case 17: {
                return "毒气陷阱";
            }
            case 19: {
                return "火焰陷阱";
            }
            case 21: {
                return "麻痹陷阱";
            }
            case 27: {
                return "毒镖陷阱";
            }
            case 30: {
                return "警报陷阱";
            }
            case 32: {
                return "雷电陷阱";
            }
            case 37: {
                return "捕兽夹陷阱";
            }
            case 39: {
                return "召唤陷阱";
            }
            case 23: {
                return "已触发的陷阱";
            }
            case 41: {
                return "书架";
            }
            case 42: {
                return "炼金釜";
            }
        }
        return "???";
    }

    public String tileDesc(int tile) {
        switch (tile) {
            case 0: {
                return "你看不到底。";
            }
            case 63: {
                return "着火时踩入水中可以熄灭火焰。";
            }
            case 7: {
                return "通向上一层的楼梯。";
            }
            case 8: 
            case 26: {
                return "通向下一层的楼梯。";
            }
            case 9: {
                return "地面散落着余烬。";
            }
            case 15: {
                return "茂密的植被遮挡了你的视线。";
            }
            case 10: {
                return "这扇门锁住了，你需要对应的钥匙才能打开。";
            }
            case 25: {
                return "沉重的铁栏杆封锁住了通往下一层的楼梯。";
            }
            case 13: {
                return "木制路障虽然十分坚固，但已经风干多年，或许可以点燃烧掉？";
            }
            case 29: {
                return "离得太远了，你看不清上面的字。";
            }
            case 17: 
            case 19: 
            case 21: 
            case 27: 
            case 30: 
            case 32: 
            case 37: 
            case 39: {
                return "踩到隐藏的压力踏板会触发陷阱。";
            }
            case 23: {
                return "这个陷阱已经被触发过了，现在不再危险。";
            }
            case 35: 
            case 36: {
                return "显然有人曾想装饰一下这里，可惜水平实在堪忧。";
            }
            case 42: {
                return "将几颗种子扔进去即可炼制药剂。";
            }
            case 3: {
                return "这口井已经干涸了。";
            }
        }
        if (tile >= 48) {
            return this.tileDesc(63);
        }
        if ((Terrain.flags[tile] & 0x80) != 0) {
            return this.tileDesc(0);
        }
        return "";
    }

    static {
        fieldOfView = new boolean[1024];
        passable = new boolean[1024];
        losBlocking = new boolean[1024];
        flamable = new boolean[1024];
        secret = new boolean[1024];
        solid = new boolean[1024];
        avoid = new boolean[1024];
        water = new boolean[1024];
        pit = new boolean[1024];
        discoverable = new boolean[1024];
        pitRoomNeeded = false;
        weakFloorCreated = false;
    }

    public static enum Feeling {
        NONE,
        CHASM,
        WATER,
        GRASS;

    }
}

