/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.potions;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.Fire;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.scenes.GameScene;

public class PotionOfLiquidFlame
extends Potion {
    public PotionOfLiquidFlame() {
        this.name = "液火药剂";
    }

    @Override
    public void shatter(int cell) {
        if (Dungeon.visible[cell]) {
            this.setKnown();
            this.splash(cell);
            GameScene.play("snd_shatter.mp3", cell);
        }
        GameScene.add(Blob.seed(cell, 2, Fire.class));
    }

    @Override
    public String desc() {
        return "该药瓶里装着一种不稳定的化合物，一旦暴露在空气中就会猛烈地燃烧起来。";
    }

    @Override
    public int price() {
        return this.isKnown() ? 40 * this.quantity : super.price();
    }
}

