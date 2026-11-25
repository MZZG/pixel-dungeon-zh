/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.levels;

import com.watabou.noosa.Scene;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.pixeldungeon.Bones;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.mobs.Bestiary;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.keys.SkeletonKey;
import com.watabou.pixeldungeon.levels.CityLevel;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.painters.Painter;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class CityBossLevel
extends Level {
    private static final int TOP = 2;
    private static final int HALL_WIDTH = 7;
    private static final int HALL_HEIGHT = 15;
    private static final int CHAMBER_HEIGHT = 3;
    private static final int LEFT = 12;
    private static final int CENTER = 15;
    private int arenaDoor;
    private boolean enteredArena;
    private boolean keyDropped;
    private static final String DOOR = "door";
    private static final String ENTERED = "entered";
    private static final String DROPPED = "droppped";

    public CityBossLevel() {
        this.color1 = 4941366;
        this.color2 = 0xF2F2F2;
        this.enteredArena = false;
        this.keyDropped = false;
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
        Painter.fill(this, 12, 2, 7, 15, 1);
        Painter.fill(this, 15, 2, 1, 15, 14);
        for (int y = 3; y < 17; y += 2) {
            this.map[y * 32 + 15 - 2] = 36;
            this.map[y * 32 + 15 + 2] = 36;
        }
        int left = CityBossLevel.pedestal(true);
        int right = CityBossLevel.pedestal(false);
        this.map[right] = 11;
        this.map[left] = 11;
        for (int i = left + 1; i < right; ++i) {
            this.map[i] = 14;
        }
        this.exit = 47;
        this.map[this.exit] = 25;
        this.arenaDoor = 559;
        this.map[this.arenaDoor] = 5;
        Painter.fill(this, 12, 18, 7, 3, 1);
        Painter.fill(this, 12, 18, 1, 3, 41);
        Painter.fill(this, 18, 18, 1, 3, 41);
        this.entrance = (19 + Random.Int(2)) * 32 + 12 + Random.Int(5);
        this.map[this.entrance] = 7;
        return true;
    }

    @Override
    protected void decorate() {
        for (int i = 0; i < 1024; ++i) {
            if (this.map[i] == 1 && Random.Int(10) == 0) {
                this.map[i] = 24;
                continue;
            }
            if (this.map[i] != 4 || Random.Int(8) != 0) continue;
            this.map[i] = 12;
        }
        int sign = this.arenaDoor + 32 + 1;
        this.map[sign] = 29;
    }

    public static int pedestal(boolean left) {
        if (left) {
            return 301;
        }
        return 305;
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
            while ((pos = Random.IntRange(13, 17) + Random.IntRange(18, 20) * 32) == this.entrance || this.map[pos] == 29) {
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
            int count = 0;
            do {
                boss.pos = Random.Int(1024);
            } while (!passable[boss.pos] || !this.outsideEntraceRoom(boss.pos) || Dungeon.visible[boss.pos] && count++ < 20);
            GameScene.add(boss);
            if (Dungeon.visible[boss.pos]) {
                boss.notice();
                boss.sprite.alpha(0.0f);
                boss.sprite.parent.add(new AlphaTweener(boss.sprite, 1.0f, 0.1f));
            }
            CityBossLevel.set(this.arenaDoor, 10);
            GameScene.updateMap(this.arenaDoor);
            Dungeon.observe();
        }
    }

    @Override
    public Heap drop(Item item, int cell) {
        if (!this.keyDropped && item instanceof SkeletonKey) {
            this.keyDropped = true;
            CityBossLevel.set(this.arenaDoor, 5);
            GameScene.updateMap(this.arenaDoor);
            Dungeon.observe();
        }
        return super.drop(item, cell);
    }

    private boolean outsideEntraceRoom(int cell) {
        return cell / 32 < this.arenaDoor / 32;
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
                return "通向下一层的斜坡。";
            }
            case 12: 
            case 24: {
                return "这里缺了几块地砖。";
            }
            case 14: {
                return "地面铺着厚实的地毯。";
            }
            case 35: 
            case 36: {
                return "这尊雕像刻画了一位摆出英勇姿态的矮人。";
            }
            case 41: {
                return "各个学科的藏书整齐地排满了书架。";
            }
        }
        return super.tileDesc(tile);
    }

    @Override
    public void addVisuals(Scene scene) {
        CityLevel.addVisuals(this, scene);
    }
}

