/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.potions;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.blobs.Fire;
import com.watabou.pixeldungeon.actors.blobs.Freezing;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.BArray;
import com.watabou.utils.PathFinder;

public class PotionOfFrost
extends Potion {
    private static final int DISTANCE = 2;

    public PotionOfFrost() {
        this.name = "冰霜药剂";
    }

    @Override
    public void shatter(int cell) {
        PathFinder.buildDistanceMap(cell, BArray.not(Level.losBlocking, null), 2);
        Fire fire = (Fire)Dungeon.level.blobs.get(Fire.class);
        boolean visible = false;
        for (int i = 0; i < 1024; ++i) {
            if (PathFinder.distance[i] >= Integer.MAX_VALUE) continue;
            visible = Freezing.affect(i, fire) || visible;
        }
        if (visible) {
            this.splash(cell);
            GameScene.play("snd_shatter.mp3", cell);
            this.setKnown();
        }
    }

    @Override
    public String desc() {
        return "接触到空气后，这种药剂会迅速蒸发成一片冰冷刺骨的寒雾，使任何触及的生物被冰封在原地，无法行动与移动。";
    }

    @Override
    public int price() {
        return this.isKnown() ? 50 * this.quantity : super.price();
    }
}

