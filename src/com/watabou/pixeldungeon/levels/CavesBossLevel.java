/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.levels;

import com.watabou.noosa.Camera;
import com.watabou.noosa.Scene;
import com.watabou.pixeldungeon.Bones;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.mobs.Bestiary;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.keys.SkeletonKey;
import com.watabou.pixeldungeon.levels.CavesLevel;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.Patch;
import com.watabou.pixeldungeon.levels.painters.Painter;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class CavesBossLevel
extends Level {
    private static final int ROOM_LEFT = 14;
    private static final int ROOM_RIGHT = 18;
    private static final int ROOM_TOP = 14;
    private static final int ROOM_BOTTOM = 18;
    private int arenaDoor;
    private boolean enteredArena;
    private boolean keyDropped;
    private static final String DOOR = "door";
    private static final String ENTERED = "entered";
    private static final String DROPPED = "droppped";

    public CavesBossLevel() {
        this.color1 = 5459774;
        this.color2 = 12179041;
        this.viewDistance = 6;
        this.enteredArena = false;
        this.keyDropped = false;
    }

    @Override
    public String tilesTex() {
        return "tiles2.png";
    }

    @Override
    public String waterTex() {
        return "water2.png";
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DOOR, this.arenaDoor);
        bundle.put(ENTERED, this.enteredArena);
        bundle.put(DROPPED, this.keyDropped);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.arenaDoor = bundle.getInt(DOOR);
        this.enteredArena = bundle.getBoolean(ENTERED);
        this.keyDropped = bundle.getBoolean(DROPPED);
    }

    @Override
    protected boolean build() {
        int i;
        int topMost = Integer.MAX_VALUE;
        for (i = 0; i < 8; ++i) {
            int bottom;
            int top;
            int right;
            int left;
            if (Random.Int(2) == 0) {
                left = Random.Int(1, 11);
                right = 21;
            } else {
                left = 11;
                right = Random.Int(21, 31);
            }
            if (Random.Int(2) == 0) {
                top = Random.Int(2, 11);
                bottom = 21;
            } else {
                top = 11;
                bottom = Random.Int(17, 31);
            }
            Painter.fill(this, left, top, right - left + 1, bottom - top + 1, 1);
            if (top >= topMost) continue;
            topMost = top;
            this.exit = Random.Int(left, right) + (top - 1) * 32;
        }
        this.map[this.exit] = 25;
        for (i = 0; i < 1024; ++i) {
            if (this.map[i] != 1 || Random.Int(6) != 0) continue;
            this.map[i] = 23;
        }
        Painter.fill(this, 13, 13, 7, 7, 4);
        Painter.fill(this, 14, 15, 5, 4, 1);
        Painter.fill(this, 14, 14, 5, 1, 17);
        this.arenaDoor = Random.Int(14, 18) + 608;
        this.map[this.arenaDoor] = 5;
        this.entrance = Random.Int(15, 17) + Random.Int(15, 17) * 32;
        this.map[this.entrance] = 7;
        boolean[] patch = Patch.generate(0.45f, 6);
        for (int i2 = 0; i2 < 1024; ++i2) {
            if (this.map[i2] != 1 || !patch[i2]) continue;
            this.map[i2] = 63;
        }
        return true;
    }

    @Override
    protected void decorate() {
        int sign;
        int i;
        for (i = 33; i < 992; ++i) {
            if (this.map[i] != 1) continue;
            int n = 0;
            if (this.map[i + 1] == 4) {
                ++n;
            }
            if (this.map[i - 1] == 4) {
                ++n;
            }
            if (this.map[i + 32] == 4) {
                ++n;
            }
            if (this.map[i - 32] == 4) {
                ++n;
            }
            if (Random.Int(8) > n) continue;
            this.map[i] = 24;
        }
        for (i = 0; i < 1024; ++i) {
            if (this.map[i] != 4 || Random.Int(8) != 0) continue;
            this.map[i] = 12;
        }
        while ((sign = Random.Int(14, 18) + Random.Int(14, 18) * 32) == this.entrance) {
        }
        this.map[sign] = 29;
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
            while ((pos = Random.IntRange(14, 18) + Random.IntRange(15, 18) * 32) == this.entrance || this.map[pos] == 29) {
            }
            this.drop((Item)item, (int)pos).type = Heap.Type.SKELETON;
        }
    }

    @Override
    public int randomRespawnCell() {
        return -1;
    }

    @Override
    public void press(int cell, Char hero) {
        super.press(cell, hero);
        if (!this.enteredArena && this.outsideEntraceRoom(cell) && hero == Dungeon.hero) {
            this.enteredArena = true;
            Mob boss = Bestiary.mob(Dungeon.depth);
            boss.state = boss.HUNTING;
            do {
                boss.pos = Random.Int(1024);
            } while (!passable[boss.pos] || !this.outsideEntraceRoom(boss.pos) || Dungeon.visible[boss.pos]);
            GameScene.add(boss);
            CavesBossLevel.set(this.arenaDoor, 4);
            GameScene.updateMap(this.arenaDoor);
            Dungeon.observe();
            CellEmitter.get(this.arenaDoor).start(Speck.factory(8), 0.07f, 10);
            Camera.main.shake(3.0f, 0.7f);
            GameScene.play("snd_rocks.mp3", this.arenaDoor);
        }
    }

    @Override
    public Heap drop(Item item, int cell) {
        if (!this.keyDropped && item instanceof SkeletonKey) {
            this.keyDropped = true;
            CellEmitter.get(this.arenaDoor).start(Speck.factory(8), 0.07f, 10);
            CavesBossLevel.set(this.arenaDoor, 24);
            GameScene.updateMap(this.arenaDoor);
            Dungeon.observe();
        }
        return super.drop(item, cell);
    }

    private boolean outsideEntraceRoom(int cell) {
        int cx = cell % 32;
        int cy = cell / 32;
        return cx < 13 || cx > 19 || cy < 13 || cy > 19;
    }

    @Override
    public String tileName(int tile) {
        switch (tile) {
            case 2: {
                return "荧光苔藓";
            }
            case 15: {
                return "荧光蘑菇";
            }
            case 63: {
                return "冰冷刺骨的积水。";
            }
        }
        return super.tileName(tile);
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case 7: {
                return "通向上一层的梯子。";
            }
            case 8: {
                return "通向下一层的梯子。";
            }
            case 15: {
                return "高耸的蘑菇群遮挡了你的视线。";
            }
            case 12: {
                return "岩壁上隐约可见金属矿脉的纹路——难道是黄金？";
            }
        }
        return super.tileDesc(tile);
    }

    @Override
    public void addVisuals(Scene scene) {
        CavesLevel.addVisuals(this, scene);
    }
}

