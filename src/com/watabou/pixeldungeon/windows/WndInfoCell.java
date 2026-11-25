/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Image;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.windows.IconTitle;

public class WndInfoCell
extends Window {
    private static final float GAP = 2.0f;
    private static final int WIDTH = 120;
    private static final String TXT_NOTHING = "这里什么也没有。";

    public WndInfoCell(int cell) {
        int tile = Dungeon.level.map[cell];
        if (Level.water[cell]) {
            tile = 63;
        } else if (Level.pit[cell]) {
            tile = 0;
        }
        IconTitle titlebar = new IconTitle();
        if (tile == 63) {
            Image water = new Image(Dungeon.level.waterTex());
            water.frame(0, 0, 16, 16);
            titlebar.icon(water);
        } else {
            titlebar.icon(DungeonTilemap.tile(tile));
        }
        titlebar.label(Dungeon.level.tileName(tile));
        titlebar.setRect(0.0f, 0.0f, 120.0f, 0.0f);
        this.add(titlebar);
        BitmapTextMultiline info = PixelScene.createMultiline(6.0f);
        this.add(info);
        StringBuilder desc = new StringBuilder(Dungeon.level.tileDesc(tile));
        int newLine = 10;
        for (Blob blob : Dungeon.level.blobs.values()) {
            if (blob.cur[cell] <= 0 || blob.tileDesc() == null) continue;
            if (desc.length() > 0) {
                desc.append('\n');
            }
            desc.append(blob.tileDesc());
        }
        info.text(desc.length() > 0 ? desc.toString() : TXT_NOTHING);
        info.maxWidth = 120;
        info.measure();
        info.x = titlebar.left();
        info.y = titlebar.bottom() + 2.0f;
        this.resize(120, (int)(info.y + info.height()));
    }
}

