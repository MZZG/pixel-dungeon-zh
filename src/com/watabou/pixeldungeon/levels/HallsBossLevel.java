/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.levels;

import com.watabou.noosa.Scene;
import com.watabou.pixeldungeon.Bones;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.mobs.Yog;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.particles.FlameParticle;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.keys.SkeletonKey;
import com.watabou.pixeldungeon.levels.HallsLevel;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.Patch;
import com.watabou.pixeldungeon.levels.painters.Painter;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class HallsBossLevel
extends Level {
    private static final int ROOM_LEFT = 15;
    private static final int ROOM_RIGHT = 17;
    private static final int ROOM_TOP = 15;
    private static final int ROOM_BOTTOM = 17;
    private int stairs;
    private boolean enteredArena;
    private boolean keyDropped;
    private static final String STAIRS = "stairs";
    private static final String ENTERED = "entered";
    private static final String DROPPED = "droppped";

    public HallsBossLevel() {
        this.color1 = 8393984;
        this.color2 = 10913057;
        this.viewDistance = 3;
        this.stairs = -1;
        this.enteredArena = false;
        this.keyDropped = false;
    }

    @Override
    public String tilesTex() {
        return "tiles4.png";
    }

    @Override
    public String waterTex() {
        return "water4.png";
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(STAIRS, this.stairs);
        bundle.put(ENTERED, this.enteredArena);
        bundle.put(DROPPED, this.keyDropped);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.stairs = bundle.getInt(STAIRS);
        this.enteredArena = bundle.getBoolean(ENTERED);
        this.keyDropped = bundle.getBoolean(DROPPED);
    }

    @Override
    protected boolean build() {
        for (int i = 0; i < 5; ++i) {
            int top = Random.IntRange(2, 14);
            int bottom = Random.IntRange(18, 22);
            Painter.fill(this, 2 + i * 4, top, 4, bottom - top + 1, 1);
            if (i == 2) {
                this.exit = i * 4 + 3 + (top - 1) * 32;
            }
            for (int j = 0; j < 4; ++j) {
                if (Random.Int(2) != 0) continue;
                int y = Random.IntRange(top + 1, bottom - 1);
                this.map[i * 4 + j + y * 32] = 12;
            }
        }
        this.map[this.exit] = 25;
        Painter.fill(this, 14, 14, 5, 5, 4);
        Painter.fill(this, 15, 15, 3, 3, 1);
        this.entrance = Random.Int(16, 16) + Random.Int(16, 16) * 32;
        this.map[this.entrance] = 7;
        boolean[] patch = Patch.generate(0.45f, 6);
        for (int i = 0; i < 1024; ++i) {
            if (this.map[i] != 1 || !patch[i]) continue;
            this.map[i] = 63;
        }
        return true;
    }

    @Override
    protected void decorate() {
        for (int i = 0; i < 1024; ++i) {
            if (this.map[i] != 1 || Random.Int(10) != 0) continue;
            this.map[i] = 24;
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
            while ((pos = Random.IntRange(15, 17) + Random.IntRange(16, 17) * 32) == this.entrance || this.map[pos] == 29) {
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
        if (!this.enteredArena && hero == Dungeon.hero && cell != this.entrance) {
            int i;
            this.enteredArena = true;
            for (i = 14; i <= 18; ++i) {
                this.doMagic(448 + i);
                this.doMagic(576 + i);
            }
            for (i = 15; i < 18; ++i) {
                this.doMagic(i * 32 + 15 - 1);
                this.doMagic(i * 32 + 17 + 1);
            }
            this.doMagic(this.entrance);
            GameScene.updateMap();
            Dungeon.observe();
            Yog boss = new Yog();
            do {
                boss.pos = Random.Int(1024);
            } while (!passable[boss.pos] || Dungeon.visible[boss.pos]);
            GameScene.add(boss);
            boss.spawnFists();
            this.stairs = this.entrance;
            this.entrance = -1;
        }
    }

    private void doMagic(int cell) {
        HallsBossLevel.set(cell, 14);
        CellEmitter.get(cell).start(FlameParticle.FACTORY, 0.1f, 3);
    }

    @Override
    public Heap drop(Item item, int cell) {
        if (!this.keyDropped && item instanceof SkeletonKey) {
            this.keyDropped = true;
            this.entrance = this.stairs;
            HallsBossLevel.set(this.entrance, 7);
            GameScene.updateMap(this.entrance);
        }
        return super.drop(item, cell);
    }

    @Override
    public String tileName(int tile) {
        switch (tile) {
            case 63: {
                return "低温岩浆";
            }
            case 2: {
                return "余烬苔藓";
            }
            case 15: {
                return "余烬蘑菇";
            }
            case 35: 
            case 36: {
                return "台柱";
            }
        }
        return super.tileName(tile);
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case 63: {
                return "看起来如同岩浆一般，但其实冰冷无比，大概可以安全触碰。";
            }
            case 35: 
            case 36: {
                return "这根石柱竟然是由货真价实的人形头骨垒成的，酷毙了。";
            }
        }
        return super.tileDesc(tile);
    }

    @Override
    public void addVisuals(Scene scene) {
        HallsLevel.addVisuals(this, scene);
    }
}

