/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.levels;

import com.watabou.noosa.Game;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.actors.mobs.npcs.Ghost;
import com.watabou.pixeldungeon.items.DewVial;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.Patch;
import com.watabou.pixeldungeon.levels.RegularLevel;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class SewerLevel
extends RegularLevel {
    public SewerLevel() {
        this.color1 = 4748860;
        this.color2 = 5871946;
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
    protected boolean[] water() {
        return Patch.generate(this.feeling == Level.Feeling.WATER ? 0.6f : 0.45f, 5);
    }

    @Override
    protected boolean[] grass() {
        return Patch.generate(this.feeling == Level.Feeling.GRASS ? 0.6f : 0.4f, 4);
    }

    @Override
    protected void decorate() {
        int pos;
        int i;
        for (i = 0; i < 32; ++i) {
            if (this.map[i] != 4 || this.map[i + 32] != 63 || Random.Int(4) != 0) continue;
            this.map[i] = 12;
        }
        for (i = 32; i < 992; ++i) {
            if (this.map[i] != 4 || this.map[i - 32] != 4 || this.map[i + 32] != 63 || Random.Int(2) != 0) continue;
            this.map[i] = 12;
        }
        for (i = 33; i < 991; ++i) {
            if (this.map[i] != 1) continue;
            int count = (this.map[i + 1] == 4 ? 1 : 0) + (this.map[i - 1] == 4 ? 1 : 0) + (this.map[i + 32] == 4 ? 1 : 0) + (this.map[i - 32] == 4 ? 1 : 0);
            if (Random.Int(16) >= count * count) continue;
            this.map[i] = 24;
        }
        while ((pos = this.roomEntrance.random()) == this.entrance) {
        }
        this.map[pos] = 29;
    }

    @Override
    protected void createMobs() {
        super.createMobs();
        Ghost.Quest.spawn(this);
    }

    @Override
    protected void createItems() {
        if (Dungeon.dewVial && Random.Int(4 - Dungeon.depth) == 0) {
            this.addItemToSpawn(new DewVial());
            Dungeon.dewVial = false;
        }
        super.createItems();
    }

    @Override
    public void addVisuals(Scene scene) {
        super.addVisuals(scene);
        SewerLevel.addVisuals(this, scene);
    }

    public static void addVisuals(Level level, Scene scene) {
        for (int i = 0; i < 1024; ++i) {
            if (level.map[i] != 12) continue;
            scene.add(new Sink(i));
        }
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
            case 41: {
                return "这个书架塞满了廉价无用的破书，烧掉怎么样？";
            }
        }
        return super.tileDesc(tile);
    }

    public static final class WaterParticle
    extends PixelParticle {
        public WaterParticle() {
            this.acc.y = 50.0f;
            this.am = 0.5f;
            this.color(ColorMath.random(11979970, 3892819));
            this.size(2.0f);
        }

        public void reset(float x, float y) {
            this.revive();
            this.x = x;
            this.y = y;
            this.speed.set(Random.Float(-2.0f, 2.0f), 0.0f);
            this.lifespan = 0.5f;
            this.left = 0.5f;
        }
    }

    private static class Sink
    extends Emitter {
        private int pos;
        private float rippleDelay = 0.0f;
        private static final Emitter.Factory factory = new Emitter.Factory(){

            @Override
            public void emit(Emitter emitter, int index, float x, float y) {
                WaterParticle p = (WaterParticle)emitter.recycle(WaterParticle.class);
                p.reset(x, y);
            }
        };

        public Sink(int pos) {
            this.pos = pos;
            PointF p = DungeonTilemap.tileCenterToWorld(pos);
            this.pos(p.x - 2.0f, p.y + 1.0f, 4.0f, 0.0f);
            this.pour(factory, 0.05f);
        }

        @Override
        public void update() {
            this.visible = Dungeon.visible[this.pos];
            if (this.visible) {
                super.update();
                if ((this.rippleDelay -= Game.elapsed) <= 0.0f) {
                    GameScene.ripple((int)(this.pos + 32)).y -= 8.0f;
                    this.rippleDelay = Random.Float(0.2f, 0.3f);
                }
            }
        }
    }
}

