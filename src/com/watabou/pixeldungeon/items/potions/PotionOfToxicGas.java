/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.potions;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.scenes.GameScene;

public class PotionOfToxicGas
extends Potion {
    public PotionOfToxicGas() {
        this.name = "毒气药剂";
    }

    @Override
    public void shatter(int cell) {
        if (Dungeon.visible[cell]) {
            this.setKnown();
            this.splash(cell);
            GameScene.play("snd_shatter.mp3", cell);
        }
        GameScene.add(Blob.seed(cell, 1000, ToxicGas.class));
    }

    @Override
    public String desc() {
        return "打开或摔碎这个密封玻璃瓶会导致内容物爆裂成一团致命的绿色毒雾。你应该选择从远处将这瓶药剂扔向敌人，而不是亲自打开它。";
    }

    @Override
    public int price() {
        return this.isKnown() ? 40 * this.quantity : super.price();
    }
}

