/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.levels;

import com.badlogic.gdx.Gdx;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.items.Torch;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.Patch;
import com.watabou.pixeldungeon.levels.RegularLevel;
import com.watabou.pixeldungeon.levels.Terrain;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class HallsLevel
extends RegularLevel {
    public HallsLevel() {
        this.minRoomSize = 6;
        this.viewDistance = Math.max(25 - Dungeon.depth, 1);
        this.color1 = 8393984;
        this.color2 = 10913057;
    }

    @Override
    public void create() {
        this.addItemToSpawn(new Torch());
        super.create();
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
    protected boolean[] water() {
        return Patch.generate(this.feeling == Level.Feeling.WATER ? 0.55f : 0.4f, 6);
    }

    @Override
    protected boolean[] grass() {
        return Patch.generate(this.feeling == Level.Feeling.GRASS ? 0.55f : 0.3f, 3);
    }

    @Override
    protected void decorate() {
        int pos;
        for (int i = 33; i < 991; ++i) {
            if (this.map[i] == 1) {
                int count = 0;
                for (int j = 0; j < NEIGHBOURS8.length; ++j) {
                    if ((Terrain.flags[this.map[i + NEIGHBOURS8[j]]] & 1) <= 0) continue;
                    ++count;
                }
                if (Random.Int(80) >= count) continue;
                this.map[i] = 24;
                continue;
            }
            if (this.map[i] != 4 || this.map[i - 1] == 12 || this.map[i - 32] == 12 || Random.Int(4) != 0) continue;
            this.map[i] = 12;
        }
        while ((pos = this.roomEntrance.random()) == this.entrance) {
        }
        this.map[pos] = 29;
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
            case 41: {
                return "书架上用远古语言写就的典籍正冒着阴燃的青烟。";
            }
        }
        return super.tileDesc(tile);
    }

    @Override
    public void addVisuals(Scene scene) {
        super.addVisuals(scene);
        HallsLevel.addVisuals(this, scene);
    }

    public static void addVisuals(Level level, Scene scene) {
        for (int i = 0; i < 1024; ++i) {
            if (level.map[i] != 63) continue;
            scene.add(new Stream(i));
        }
    }

    public static class FireParticle
    extends PixelParticle.Shrinking {
        public FireParticle() {
            this.color(0xEE7722);
            this.lifespan = 1.0f;
            this.acc.set(0.0f, 80.0f);
        }

        public void reset(float x, float y) {
            this.revive();
            this.x = x;
            this.y = y;
            this.left = this.lifespan;
            this.speed.set(0.0f, -40.0f);
            this.size = 4.0f;
        }

        @Override
        public void update() {
            super.update();
            float p = this.left / this.lifespan;
            this.am = p > 0.8f ? (1.0f - p) * 5.0f : 1.0f;
        }
    }

    private static class Stream
    extends Group {
        private int pos;
        private float delay;

        public Stream(int pos) {
            this.pos = pos;
            this.delay = Random.Float(2.0f);
        }

        @Override
        public void update() {
            this.visible = Dungeon.visible[this.pos];
            if (this.visible) {
                super.update();
                if ((this.delay -= Game.elapsed) <= 0.0f) {
                    this.delay = Random.Float(2.0f);
                    PointF p = DungeonTilemap.tileToWorld(this.pos);
                    ((FireParticle)this.recycle(FireParticle.class)).reset(p.x + Random.Float(16.0f), p.y + Random.Float(16.0f));
                }
            }
        }

        @Override
        public void draw() {
            Gdx.gl.glBlendFunc(770, 1);
            super.draw();
            Gdx.gl.glBlendFunc(770, 771);
        }
    }
}

