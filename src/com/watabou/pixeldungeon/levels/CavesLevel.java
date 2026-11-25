/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.levels;

import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.actors.mobs.npcs.Blacksmith;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.Patch;
import com.watabou.pixeldungeon.levels.RegularLevel;
import com.watabou.pixeldungeon.levels.Room;
import com.watabou.pixeldungeon.levels.painters.Painter;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class CavesLevel
extends RegularLevel {
    public CavesLevel() {
        this.color1 = 5459774;
        this.color2 = 12179041;
        this.viewDistance = 6;
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
    protected boolean[] water() {
        return Patch.generate(this.feeling == Level.Feeling.WATER ? 0.6f : 0.45f, 6);
    }

    @Override
    protected boolean[] grass() {
        return Patch.generate(this.feeling == Level.Feeling.GRASS ? 0.55f : 0.35f, 3);
    }

    @Override
    protected void assignRoomType() {
        super.assignRoomType();
        Blacksmith.Quest.spawn(this.rooms);
    }

    @Override
    protected void decorate() {
        int pos;
        int i;
        for (Room room : this.rooms) {
            int corner;
            if (room.type != Room.Type.STANDARD || room.width() <= 3 || room.height() <= 3) continue;
            int s = room.square();
            if (Random.Int(s) > 8 && this.map[(corner = room.left + 1 + (room.top + 1) * 32) - 1] == 4 && this.map[corner - 32] == 4) {
                this.map[corner] = 4;
            }
            if (Random.Int(s) > 8 && this.map[(corner = room.right - 1 + (room.top + 1) * 32) + 1] == 4 && this.map[corner - 32] == 4) {
                this.map[corner] = 4;
            }
            if (Random.Int(s) > 8 && this.map[(corner = room.left + 1 + (room.bottom - 1) * 32) - 1] == 4 && this.map[corner + 32] == 4) {
                this.map[corner] = 4;
            }
            if (Random.Int(s) > 8 && this.map[(corner = room.right - 1 + (room.bottom - 1) * 32) + 1] == 4 && this.map[corner + 32] == 4) {
                this.map[corner] = 4;
            }
            for (Room n : room.connected.keySet()) {
                if (n.type != Room.Type.STANDARD && n.type != Room.Type.TUNNEL || Random.Int(3) != 0) continue;
                Painter.set((Level)this, room.connected.get(n), 24);
            }
        }
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
            if (Random.Int(6) > n) continue;
            this.map[i] = 24;
        }
        for (i = 0; i < 1024; ++i) {
            if (this.map[i] != 4 || Random.Int(12) != 0) continue;
            this.map[i] = 12;
        }
        while ((pos = this.roomEntrance.random()) == this.entrance) {
        }
        this.map[pos] = 29;
        if (Dungeon.bossLevel(Dungeon.depth + 1)) {
            return;
        }
        for (Room r : this.rooms) {
            if (r.type != Room.Type.STANDARD) continue;
            for (Room n : r.neigbours) {
                if (n.type != Room.Type.STANDARD || r.connected.containsKey(n)) continue;
                Rect w = r.intersect(n);
                if (w.left == w.right && w.bottom - w.top >= 5) {
                    w.top += 2;
                    --w.bottom;
                    ++w.right;
                    Painter.fill(this, w.left, w.top, 1, w.height(), 0);
                    continue;
                }
                if (w.top != w.bottom || w.right - w.left < 5) continue;
                w.left += 2;
                --w.right;
                ++w.bottom;
                Painter.fill(this, w.left, w.top, w.width(), 1, 0);
            }
        }
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
            case 41: {
                return "到底是谁会在矿洞里摆个书架？";
            }
        }
        return super.tileDesc(tile);
    }

    @Override
    public void addVisuals(Scene scene) {
        super.addVisuals(scene);
        CavesLevel.addVisuals(this, scene);
    }

    public static void addVisuals(Level level, Scene scene) {
        for (int i = 0; i < 1024; ++i) {
            if (level.map[i] != 12) continue;
            scene.add(new Vein(i));
        }
    }

    public static final class Sparkle
    extends PixelParticle {
        public void reset(float x, float y) {
            this.revive();
            this.x = x;
            this.y = y;
            this.lifespan = 0.5f;
            this.left = 0.5f;
        }

        @Override
        public void update() {
            super.update();
            float p = this.left / this.lifespan;
            this.am = p < 0.5f ? p * 2.0f : (1.0f - p) * 2.0f;
            this.size(this.am * 2.0f);
        }
    }

    private static class Vein
    extends Group {
        private int pos;
        private float delay;

        public Vein(int pos) {
            this.pos = pos;
            this.delay = Random.Float(2.0f);
        }

        @Override
        public void update() {
            this.visible = Dungeon.visible[this.pos];
            if (this.visible) {
                super.update();
                if ((this.delay -= Game.elapsed) <= 0.0f) {
                    this.delay = Random.Float();
                    PointF p = DungeonTilemap.tileToWorld(this.pos);
                    ((Sparkle)this.recycle(Sparkle.class)).reset(p.x + Random.Float(16.0f), p.y + Random.Float(16.0f));
                }
            }
        }
    }
}

