/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.potions;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.ParalyticGas;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.scenes.GameScene;

public class PotionOfParalyticGas
extends Potion {
    public PotionOfParalyticGas() {
        this.name = "麻痹药剂";
    }

    @Override
    public void shatter(int cell) {
        if (Dungeon.visible[cell]) {
            this.setKnown();
            this.splash(cell);
            GameScene.play("snd_shatter.mp3", cell);
        }
        GameScene.add(Blob.seed(cell, 1000, ParalyticGas.class));
    }

    @Override
    public String desc() {
        return "暴露于空气时，烧瓶里的液体会气化成一片令人失去知觉的黄色云雾。任何吸入气体的生物都会立刻麻痹，直到气体消散后一段时间才能恢复行动。适合扔向远处的敌群。";
    }

    @Override
    public int price() {
        return this.isKnown() ? 40 * this.quantity : super.price();
    }
}

