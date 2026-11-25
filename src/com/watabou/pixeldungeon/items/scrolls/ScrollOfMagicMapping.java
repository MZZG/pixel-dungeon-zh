/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.scrolls;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.effects.SpellSprite;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.Terrain;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;

public class ScrollOfMagicMapping
extends Scroll {
    private static final String TXT_LAYOUT = "你熟悉了这一层的布局。";

    public ScrollOfMagicMapping() {
        this.name = "探地卷轴";
    }

    @Override
    protected void doRead() {
        int length = 1024;
        int[] map = Dungeon.level.map;
        boolean[] mapped = Dungeon.level.mapped;
        boolean[] discoverable = Level.discoverable;
        boolean noticed = false;
        for (int i = 0; i < length; ++i) {
            int terr = map[i];
            if (!discoverable[i]) continue;
            mapped[i] = true;
            if ((Terrain.flags[terr] & 8) == 0) continue;
            Level.set(i, Terrain.discover(terr));
            GameScene.updateMap(i);
            if (!Dungeon.visible[i]) continue;
            GameScene.discoverTile(i, terr);
            ScrollOfMagicMapping.discover(i);
            noticed = true;
        }
        Dungeon.observe();
        GLog.i(TXT_LAYOUT, new Object[0]);
        if (noticed) {
            Sample.INSTANCE.play("snd_secret.mp3");
        }
        SpellSprite.show(curUser, 1);
        Sample.INSTANCE.play("snd_read.mp3");
        Invisibility.dispel();
        this.setKnown();
        this.readAnimation();
    }

    @Override
    public String desc() {
        return "阅读这张卷轴时，一幅清晰无比的地图会直接印入你的脑海，揭示整层的精确结构与所有隐藏的秘密。不过物品与生物的位置仍然未知。";
    }

    @Override
    public int price() {
        return this.isKnown() ? 25 * this.quantity : super.price();
    }

    public static void discover(int cell) {
        CellEmitter.get(cell).start(Speck.factory(101), 0.1f, 4);
    }
}

