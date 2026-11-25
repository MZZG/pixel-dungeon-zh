/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.levels;

import com.watabou.noosa.Scene;
import com.watabou.pixeldungeon.Bones;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.mobs.Bestiary;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.levels.Patch;
import com.watabou.pixeldungeon.levels.RegularLevel;
import com.watabou.pixeldungeon.levels.Room;
import com.watabou.pixeldungeon.levels.SewerLevel;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Graph;
import com.watabou.utils.Random;
import java.util.ArrayList;
import java.util.List;

public class SewerBossLevel
extends RegularLevel {
    private int stairs;
    private static final String STAIRS = "stairs";

    public SewerBossLevel() {
        this.color1 = 4748860;
        this.color2 = 5871946;
        this.stairs = 0;
    }

    @Override
    public String tilesTex() {
        return "tiles0.png";
    }

    @Override
    public String waterTex() {
        return "water0.png";
    }

    @Override
    protected boolean build() {
        int distance;
        this.initRooms();
        int retry = 0;
        int minDistance = (int)Math.sqrt(this.rooms.size());
        do {
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
            } while (this.roomExit == this.roomEntrance || this.roomExit.width() < 6 || this.roomExit.height() < 6 || this.roomExit.top == 0);
            Graph.buildDistanceMap(this.rooms, this.roomExit);
            distance = this.roomEntrance.distance();
            if (retry++ <= 10) continue;
            return false;
        } while (distance < minDistance);
        this.roomEntrance.type = Room.Type.ENTRANCE;
        this.roomExit.type = Room.Type.BOSS_EXIT;
        Graph.buildDistanceMap(this.rooms, this.roomExit);
        List<Room> path = Graph.buildPath(this.rooms, this.roomEntrance, this.roomExit);
        Graph.setPrice(path, this.roomEntrance.distance);
        Graph.buildDistanceMap(this.rooms, this.roomExit);
        path = Graph.buildPath(this.rooms, this.roomEntrance, this.roomExit);
        Room room = this.roomEntrance;
        for (Room next : path) {
            room.connect(next);
            room = next;
        }
        room = (Room)this.roomExit.connected.keySet().toArray()[0];
        if (this.roomExit.top == room.bottom) {
            return false;
        }
        for (Object r : this.rooms) {
            if (((Room)r).type != Room.Type.NULL || ((Room)r).connected.size() <= 0) continue;
            ((Room)r).type = Room.Type.TUNNEL;
        }
        ArrayList<Room> candidates = new ArrayList<Room>();
        for (Room r : this.roomExit.neigbours) {
            if (this.roomExit.connected.containsKey(r) || this.roomExit.left != r.right && this.roomExit.right != r.left && this.roomExit.bottom != r.top) continue;
            candidates.add(r);
        }
        if (candidates.size() > 0) {
            Room kingsRoom = (Room)Random.element(candidates);
            kingsRoom.connect(this.roomExit);
            kingsRoom.type = Room.Type.RAT_KING;
        }
        this.paint();
        this.paintWater();
        this.paintGrass();
        this.placeTraps();
        return true;
    }

    @Override
    protected boolean[] water() {
        return Patch.generate(0.5f, 5);
    }

    @Override
    protected boolean[] grass() {
        return Patch.generate(0.4f, 4);
    }

    @Override
    protected void decorate() {
        int pos;
        int start = this.roomExit.top * 32 + this.roomExit.left + 1;
        int end = start + this.roomExit.width() - 1;
        for (int i = start; i < end; ++i) {
            if (i != this.exit) {
                this.map[i] = 12;
                this.map[i + 32] = 63;
                continue;
            }
            this.map[i + 32] = 1;
        }
        while ((pos = this.roomEntrance.random()) == this.entrance) {
        }
        this.map[pos] = 29;
    }

    @Override
    public void addVisuals(Scene scene) {
        SewerLevel.addVisuals(this, scene);
    }

    @Override
    protected void createMobs() {
        Mob mob = Bestiary.mob(Dungeon.depth);
        mob.pos = this.roomExit.random();
        this.mobs.add(mob);
    }

    @Override
    public Actor respawner() {
        return null;
    }

    @Override
    protected void createItems() {
        Item item = Bones.get();
        if (item != null) {
            int pos;
            while ((pos = this.roomEntrance.random()) == this.entrance || this.map[pos] == 29) {
            }
            this.drop((Item)item, (int)pos).type = Heap.Type.SKELETON;
        }
    }

    public void seal() {
        if (this.entrance != 0) {
            SewerBossLevel.set(this.entrance, 48);
            GameScene.updateMap(this.entrance);
            GameScene.ripple(this.entrance);
            this.stairs = this.entrance;
            this.entrance = 0;
        }
    }

    public void unseal() {
        if (this.stairs != 0) {
            this.entrance = this.stairs;
            this.stairs = 0;
            SewerBossLevel.set(this.entrance, 7);
            GameScene.updateMap(this.entrance);
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(STAIRS, this.stairs);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.stairs = bundle.getInt(STAIRS);
    }

    @Override
    public String tileName(int tile) {
        switch (tile) {
            case 63: {
                return "浑浊水潭";
            }
        }
        return super.tileName(tile);
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case 24: {
                return "潮湿发黄的苔藓覆盖着地面。";
            }
        }
        return super.tileDesc(tile);
    }
}

