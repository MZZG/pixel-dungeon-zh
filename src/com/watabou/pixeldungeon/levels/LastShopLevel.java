/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.levels;

import com.watabou.noosa.Scene;
import com.watabou.pixeldungeon.Bones;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.mobs.npcs.Imp;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.levels.CityLevel;
import com.watabou.pixeldungeon.levels.Patch;
import com.watabou.pixeldungeon.levels.RegularLevel;
import com.watabou.pixeldungeon.levels.Room;
import com.watabou.utils.Graph;
import com.watabou.utils.Random;
import java.util.List;

public class LastShopLevel
extends RegularLevel {
    public LastShopLevel() {
        this.color1 = 4941366;
        this.color2 = 0xF2F2F2;
    }

    @Override
    public String tilesTex() {
        return "tiles3.png";
    }

    @Override
    public String waterTex() {
        return "water3.png";
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
            distance = Graph.buildPath(this.rooms, this.roomEntrance, this.roomExit).size();
            if (retry++ <= 10) continue;
            return false;
        } while (distance < minDistance);
        this.roomEntrance.type = Room.Type.ENTRANCE;
        this.roomExit.type = Room.Type.EXIT;
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
        Room roomShop = null;
        int shopSquare = 0;
        for (Room r : this.rooms) {
            if (r.type != Room.Type.NULL || r.connected.size() <= 0) continue;
            r.type = Room.Type.PASSAGE;
            if (r.square() <= shopSquare) continue;
            roomShop = r;
            shopSquare = r.square();
        }
        if (roomShop == null || shopSquare < 30) {
            return false;
        }
        roomShop.type = Imp.Quest.isCompleted() ? Room.Type.SHOP : Room.Type.STANDARD;
        this.paint();
        this.paintWater();
        this.paintGrass();
        return true;
    }

    @Override
    protected void decorate() {
        block4: {
            int pos;
            for (int i = 0; i < 1024; ++i) {
                if (this.map[i] == 1 && Random.Int(10) == 0) {
                    this.map[i] = 24;
                    continue;
                }
                if (this.map[i] == 4 && Random.Int(8) == 0) {
                    this.map[i] = 12;
                    continue;
                }
                if (this.map[i] != 16) continue;
                this.map[i] = 5;
            }
            if (!Imp.Quest.isCompleted()) break block4;
            while ((pos = this.roomEntrance.random()) == this.entrance) {
            }
            this.map[pos] = 29;
        }
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
        Item item = Bones.get();
        if (item != null) {
            int pos;
            while ((pos = this.roomEntrance.random()) == this.entrance || this.map[pos] == 29) {
            }
            this.drop((Item)item, (int)pos).type = Heap.Type.SKELETON;
        }
    }

    @Override
    public int randomRespawnCell() {
        return -1;
    }

    @Override
    public String tileName(int tile) {
        switch (tile) {
            case 63: {
                return "异色水潭";
            }
            case 15: {
                return "盛开的高大花丛";
            }
        }
        return super.tileName(tile);
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case 7: {
                return "通向上一层的斜坡。";
            }
            case 8: {
                return "通往炼狱深处的斜坡。";
            }
            case 12: 
            case 24: {
                return "这里缺了几块地砖。";
            }
            case 14: {
                return "地面铺着厚实的地毯。";
            }
        }
        return super.tileDesc(tile);
    }

    @Override
    protected boolean[] water() {
        return Patch.generate(0.35f, 4);
    }

    @Override
    protected boolean[] grass() {
        return Patch.generate(0.3f, 3);
    }

    @Override
    public void addVisuals(Scene scene) {
        CityLevel.addVisuals(this, scene);
    }
}

