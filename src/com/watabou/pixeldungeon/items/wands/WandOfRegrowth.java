/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.wands;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.Regrowth;
import com.watabou.pixeldungeon.effects.MagicMissile;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

public class WandOfRegrowth
extends Wand {
    public WandOfRegrowth() {
        this.name = "再生法杖";
    }

    @Override
    protected void onZap(int cell) {
        for (int i = 1; i < Ballistica.distance - 1; ++i) {
            int p = Ballistica.trace[i];
            int c = Dungeon.level.map[p];
            if (c != 1 && c != 9 && c != 24) continue;
            Level.set(p, 2);
            GameScene.updateMap(p);
            if (!Dungeon.visible[p]) continue;
            GameScene.discoverTile(p, c);
        }
        int c = Dungeon.level.map[cell];
        if (c == 1 || c == 9 || c == 24 || c == 2 || c == 15) {
            GameScene.add(Blob.seed(cell, (this.power() + 2) * 20, Regrowth.class));
        } else {
            GLog.i("什么都没有发生", new Object[0]);
        }
    }

    @Override
    protected void fx(int cell, Callback callback) {
        MagicMissile.foliage(WandOfRegrowth.curUser.sprite.parent, WandOfRegrowth.curUser.pos, cell, callback);
        Sample.INSTANCE.play("snd_zap.mp3");
    }

    @Override
    public String desc() {
        return "\"当生命逝去，新的生机总会萌发……永恒的轮回生生不息！\"";
    }
}

