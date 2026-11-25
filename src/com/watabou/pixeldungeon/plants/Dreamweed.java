/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.plants;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.ConfusionGas;
import com.watabou.pixeldungeon.items.potions.PotionOfInvisibility;
import com.watabou.pixeldungeon.plants.Plant;
import com.watabou.pixeldungeon.scenes.GameScene;

public class Dreamweed
extends Plant {
    private static final String TXT_DESC = "触碰迷梦草时，它会喷出一片闪烁微光的致眩气体。";

    public Dreamweed() {
        this.image = 3;
        this.plantName = "迷梦草";
    }

    @Override
    public void activate(Char ch) {
        super.activate(ch);
        if (ch != null) {
            GameScene.add(Blob.seed(this.pos, 400, ConfusionGas.class));
        }
    }

    @Override
    public String desc() {
        return TXT_DESC;
    }

    public static class Seed
    extends Plant.Seed {
        public Seed() {
            this.plantName = "迷梦草";
            this.name = this.plantName + "种子";
            this.image = 91;
            this.plantClass = Dreamweed.class;
            this.alchemyClass = PotionOfInvisibility.class;
        }

        @Override
        public String desc() {
            return Dreamweed.TXT_DESC;
        }
    }
}

