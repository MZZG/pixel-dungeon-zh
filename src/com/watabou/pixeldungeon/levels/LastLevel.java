/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.levels;

import com.watabou.noosa.Scene;
import com.watabou.pixeldungeon.items.Amulet;
import com.watabou.pixeldungeon.levels.HallsLevel;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.painters.Painter;
import com.watabou.utils.Random;
import java.util.Arrays;

public class LastLevel
extends Level {
    private static final int SIZE = 7;
    private int pedestal;

    public LastLevel() {
        this.color1 = 8393984;
        this.color2 = 10913057;
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
    protected boolean build() {
        Arrays.fill(this.map, 4);
        Painter.fill(this, 1, 1, 7, 7, 63);
        Painter.fill(this, 2, 2, 5, 5, 1);
        Painter.fill(this, 3, 3, 3, 3, 14);
        this.entrance = 228;
        this.map[this.entrance] = 7;
        this.exit = this.entrance - 224;
        this.map[this.exit] = 25;
        this.pedestal = 132;
        this.map[this.pedestal] = 11;
        this.map[this.pedestal + 1] = 36;
        this.map[this.pedestal - 1] = 36;
        this.feeling = Level.Feeling.NONE;
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
    protected void createItems() {
        this.drop(new Amulet(), this.pedestal);
    }

    @Override
    public int randomRespawnCell() {
        return -1;
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

