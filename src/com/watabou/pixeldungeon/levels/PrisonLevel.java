/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.levels;

import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.Emitter;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.actors.mobs.npcs.Wandmaker;
import com.watabou.pixeldungeon.effects.Halo;
import com.watabou.pixeldungeon.effects.particles.FlameParticle;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.Patch;
import com.watabou.pixeldungeon.levels.RegularLevel;
import com.watabou.pixeldungeon.levels.Room;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class PrisonLevel
extends RegularLevel {
    public PrisonLevel() {
        this.color1 = 6976061;
        this.color2 = 8950348;
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
    protected void createMobs() {
        super.createMobs();
        Wandmaker.Quest.spawn(this, this.roomEntrance);
    }

    @Override
    protected void decorate() {
        int pos;
        int i;
        for (i = 33; i < 991; ++i) {
            if (this.map[i] != 1) continue;
            float c = 0.05f;
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
            if (this.map[i] != 4 || this.map[i + 32] != 1 && this.map[i + 32] != 14 || Random.Int(6) != 0) continue;
            this.map[i] = 12;
        }
        for (i = 32; i < 992; ++i) {
            if (this.map[i] != 4 || this.map[i - 32] != 4 || this.map[i + 32] != 1 && this.map[i + 32] != 14 || Random.Int(3) != 0) continue;
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
            case 41: {
                return "这大概是监狱图书馆残留下来的书架，或许能点着？";
            }
        }
        return super.tileDesc(tile);
    }

    @Override
    public void addVisuals(Scene scene) {
        super.addVisuals(scene);
        PrisonLevel.addVisuals(this, scene);
    }

    public static void addVisuals(Level level, Scene scene) {
        for (int i = 0; i < 1024; ++i) {
            if (level.map[i] != 12) continue;
            scene.add(new Torch(i));
        }
    }

    private static class Torch
    extends Emitter {
        private int pos;

        public Torch(int pos) {
            this.pos = pos;
            PointF p = DungeonTilemap.tileCenterToWorld(pos);
            this.pos(p.x - 1.0f, p.y + 3.0f, 2.0f, 0.0f);
            this.pour(FlameParticle.FACTORY, 0.15f);
            this.add(new Halo(16.0f, 0xFFFFCC, 0.2f).point(p.x, p.y));
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

