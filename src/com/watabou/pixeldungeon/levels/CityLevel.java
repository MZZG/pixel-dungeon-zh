/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.levels;

import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.actors.mobs.npcs.Imp;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.Patch;
import com.watabou.pixeldungeon.levels.RegularLevel;
import com.watabou.pixeldungeon.levels.Room;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class CityLevel
extends RegularLevel {
    public CityLevel() {
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
    protected boolean[] water() {
        return Patch.generate(this.feeling == Level.Feeling.WATER ? 0.65f : 0.45f, 4);
    }

    @Override
    protected boolean[] grass() {
        return Patch.generate(this.feeling == Level.Feeling.GRASS ? 0.6f : 0.4f, 3);
    }

    @Override
    protected void assignRoomType() {
        super.assignRoomType();
        for (Room r : this.rooms) {
            if (r.type != Room.Type.TUNNEL) continue;
            r.type = Room.Type.PASSAGE;
        }
    }

    @Override
    protected void decorate() {
        int pos;
        for (int i = 0; i < 1024; ++i) {
            if (this.map[i] == 1 && Random.Int(10) == 0) {
                this.map[i] = 24;
                continue;
            }
            if (this.map[i] != 4 || Random.Int(8) != 0) continue;
            this.map[i] = 12;
        }
        while ((pos = this.roomEntrance.random()) == this.entrance) {
        }
        this.map[pos] = 29;
    }

    @Override
    protected void createItems() {
        super.createItems();
        Imp.Quest.spawn(this, this.roomEntrance);
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
        super.addVisuals(scene);
        CityLevel.addVisuals(this, scene);
    }

    public static void addVisuals(Level level, Scene scene) {
        for (int i = 0; i < 1024; ++i) {
            if (level.map[i] != 12) continue;
            scene.add(new Smoke(i));
        }
    }

    public static final class SmokeParticle
    extends PixelParticle {
        public SmokeParticle() {
            this.color(0);
            this.speed.set(Random.Float(8.0f), -Random.Float(8.0f));
        }

        public void reset(float x, float y) {
            this.revive();
            this.x = x;
            this.y = y;
            this.lifespan = 2.0f;
            this.left = 2.0f;
        }

        @Override
        public void update() {
            super.update();
            float p = this.left / this.lifespan;
            this.am = p > 0.8f ? 1.0f - p : p * 0.25f;
            this.size(8.0f - p * 4.0f);
        }
    }

    private static class Smoke
    extends Emitter {
        private int pos;
        private static final Emitter.Factory factory = new Emitter.Factory(){

            @Override
            public void emit(Emitter emitter, int index, float x, float y) {
                SmokeParticle p = (SmokeParticle)emitter.recycle(SmokeParticle.class);
                p.reset(x, y);
            }
        };

        public Smoke(int pos) {
            this.pos = pos;
            PointF p = DungeonTilemap.tileCenterToWorld(pos);
            this.pos(p.x - 4.0f, p.y - 2.0f, 4.0f, 0.0f);
            this.pour(factory, 0.2f);
        }

        @Override
        public void update() {
            this.visible = Dungeon.visible[this.pos];
            if (this.visible) {
                super.update();
            }
        }
    }
}

