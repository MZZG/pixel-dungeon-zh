/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.levels;

import com.watabou.noosa.Scene;
import com.watabou.pixeldungeon.Bones;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.mobs.Bestiary;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.keys.IronKey;
import com.watabou.pixeldungeon.items.keys.SkeletonKey;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.Patch;
import com.watabou.pixeldungeon.levels.PrisonLevel;
import com.watabou.pixeldungeon.levels.RegularLevel;
import com.watabou.pixeldungeon.levels.Room;
import com.watabou.pixeldungeon.levels.painters.Painter;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Graph;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import java.util.List;

public class PrisonBossLevel
extends RegularLevel {
    private Room anteroom;
    private int arenaDoor;
    private boolean enteredArena;
    private boolean keyDropped;
    private static final String ARENA = "arena";
    private static final String DOOR = "door";
    private static final String ENTERED = "entered";
    private static final String DROPPED = "droppped";

    public PrisonBossLevel() {
        this.color1 = 6976061;
        this.color2 = 8950348;
        this.enteredArena = false;
        this.keyDropped = false;
    }

    @Override
    public String tilesTex() {
        return "tiles1.png";
    }

    @Override
    public String waterTex() {
        return "water1.png";
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ARENA, this.roomExit);
        bundle.put(DOOR, this.arenaDoor);
        bundle.put(ENTERED, this.enteredArena);
        bundle.put(DROPPED, this.keyDropped);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.roomExit = (Room)bundle.get(ARENA);
        this.arenaDoor = bundle.getInt(DOOR);
        this.enteredArena = bundle.getBoolean(ENTERED);
        this.keyDropped = bundle.getBoolean(DROPPED);
    }

    @Override
    protected boolean build() {
        int distance;
        this.initRooms();
        int retry = 0;
        do {
            if (retry++ > 10) {
                return false;
            }
            int innerRetry = 0;
            do {
                if (innerRetry++ > 10) {
                    return false;
                }
                this.roomEntrance = (Room)Random.element(this.rooms);
            } while (this.roomEntrance.width() < 4 || this.roomEntrance.height() < 4);
            innerRetry = 0;
            do {
                if (innerRetry++ > 10) {
                    return false;
                }
                this.roomExit = (Room)Random.element(this.rooms);
            } while (this.roomExit == this.roomEntrance || this.roomExit.width() < 7 || this.roomExit.height() < 7 || this.roomExit.top == 0);
            Graph.buildDistanceMap(this.rooms, this.roomExit);
        } while ((distance = Graph.buildPath(this.rooms, this.roomEntrance, this.roomExit).size()) < 3);
        this.roomEntrance.type = Room.Type.ENTRANCE;
        this.roomExit.type = Room.Type.BOSS_EXIT;
        List<Room> path = Graph.buildPath(this.rooms, this.roomEntrance, this.roomExit);
        Graph.setPrice(path, this.roomEntrance.distance);
        Graph.buildDistanceMap(this.rooms, this.roomExit);
        path = Graph.buildPath(this.rooms, this.roomEntrance, this.roomExit);
        this.anteroom = path.get(path.size() - 2);
        this.anteroom.type = Room.Type.STANDARD;
        Room room = this.roomEntrance;
        for (Room next : path) {
            room.connect(next);
            room = next;
        }
        for (Room r : this.rooms) {
            if (r.type != Room.Type.NULL || r.connected.size() <= 0) continue;
            r.type = Room.Type.PASSAGE;
        }
        this.paint();
        Room r = (Room)this.roomExit.connected.keySet().toArray()[0];
        if (this.roomExit.connected.get((Object)r).y == this.roomExit.top) {
            return false;
        }
        this.paintWater();
        this.paintGrass();
        this.placeTraps();
        return true;
    }

    @Override
    protected boolean[] water() {
        return Patch.generate(0.45f, 5);
    }

    @Override
    protected boolean[] grass() {
        return Patch.generate(0.3f, 4);
    }

    @Override
    protected void paintDoors(Room r) {
        for (Room n : r.connected.keySet()) {
            if (r.type == Room.Type.NULL) continue;
            Point door = r.connected.get(n);
            if (r.type == Room.Type.PASSAGE && n.type == Room.Type.PASSAGE) {
                Painter.set((Level)this, door, 1);
                continue;
            }
            Painter.set((Level)this, door, 5);
        }
    }

    @Override
    protected void placeTraps() {
        int nTraps = this.nTraps();
        for (int i = 0; i < nTraps; ++i) {
            int trapPos = Random.Int(1024);
            if (this.map[trapPos] != 1) continue;
            this.map[trapPos] = 27;
        }
    }

    @Override
    protected void decorate() {
        int pos;
        int i;
        for (i = 33; i < 991; ++i) {
            if (this.map[i] != 1) continue;
            float c = 0.15f;
            if (this.map[i + 1] == 4 && this.map[i + 32] == 4) {
                c += 0.2f;
            }
            if (this.map[i - 1] == 4 && this.map[i + 32] == 4) {
                c += 0.2f;
            }
            if (this.map[i + 1] == 4 && this.map[i - 32] == 4) {
                c += 0.2f;
            }
            if (this.map[i - 1] == 4 && this.map[i - 32] == 4) {
                c += 0.2f;
            }
            if (!(Random.Float() < c)) continue;
            this.map[i] = 24;
        }
        for (i = 0; i < 32; ++i) {
            if (this.map[i] != 4 || this.map[i + 32] != 1 && this.map[i + 32] != 14 || Random.Int(4) != 0) continue;
            this.map[i] = 12;
        }
        for (i = 32; i < 992; ++i) {
            if (this.map[i] != 4 || this.map[i - 32] != 4 || this.map[i + 32] != 1 && this.map[i + 32] != 14 || Random.Int(2) != 0) continue;
            this.map[i] = 12;
        }
        while ((pos = this.roomEntrance.random()) == this.entrance) {
        }
        this.map[pos] = 29;
        Room.Door door = this.roomExit.entrance();
        this.arenaDoor = door.x + door.y * 32;
        Painter.set((Level)this, this.arenaDoor, 10);
        Painter.fill(this, this.roomExit.left + 2, this.roomExit.top + 2, this.roomExit.width() - 3, this.roomExit.height() - 3, 23);
    }

    @Override
    protected void createMobs() {
    }

    @Override
    public Actor respawner() {
        return null;
    }

    @Override
    protected void createItems() {
        int keyPos = this.anteroom.random();
        while (!passable[keyPos]) {
            keyPos = this.anteroom.random();
        }
        this.drop((Item)new IronKey(), (int)keyPos).type = Heap.Type.CHEST;
        Item item = Bones.get();
        if (item != null) {
            int pos;
            while ((pos = this.roomEntrance.random()) == this.entrance || this.map[pos] == 29) {
            }
            this.drop((Item)item, (int)pos).type = Heap.Type.SKELETON;
        }
    }

    @Override
    public void press(int cell, Char ch) {
        super.press(cell, ch);
        if (ch == Dungeon.hero && !this.enteredArena && this.roomExit.inside(cell)) {
            int pos;
            this.enteredArena = true;
            while ((pos = this.roomExit.random()) == cell || Actor.findChar(pos) != null) {
            }
            Mob boss = Bestiary.mob(Dungeon.depth);
            boss.state = boss.HUNTING;
            boss.pos = pos;
            GameScene.add(boss);
            boss.notice();
            this.mobPress(boss);
            PrisonBossLevel.set(this.arenaDoor, 10);
            GameScene.updateMap(this.arenaDoor);
            Dungeon.observe();
        }
    }

    @Override
    public Heap drop(Item item, int cell) {
        if (!this.keyDropped && item instanceof SkeletonKey) {
            this.keyDropped = true;
            PrisonBossLevel.set(this.arenaDoor, 5);
            GameScene.updateMap(this.arenaDoor);
            Dungeon.observe();
        }
        return super.drop(item, cell);
    }

    @Override
    public int randomRespawnCell() {
        return -1;
    }

    @Override
    public String tileName(int tile) {
        switch (tile) {
            case 63: {
                return "幽暗冰冷的积水。";
            }
        }
        return super.tileName(tile);
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case 24: {
                return "地面上仍残留着陈旧的血迹。";
            }
        }
        return super.tileDesc(tile);
    }

    @Override
    public void addVisuals(Scene scene) {
        PrisonLevel.addVisuals(this, scene);
    }
}

