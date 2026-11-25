/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.plants;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.Fire;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.particles.FlameParticle;
import com.watabou.pixeldungeon.items.potions.PotionOfLiquidFlame;
import com.watabou.pixeldungeon.plants.Plant;
import com.watabou.pixeldungeon.scenes.GameScene;

public class Firebloom
extends Plant {
    private static final String TXT_DESC = "烈焰花被任何东西触碰时，都会轰然化作一团火焰。";

    public Firebloom() {
        this.image = 0;
        this.plantName = "烈焰花";
    }

    @Override
    public void activate(Char ch) {
        super.activate(ch);
        GameScene.add(Blob.seed(this.pos, 2, Fire.class));
        if (Dungeon.visible[this.pos]) {
            CellEmitter.get(this.pos).burst(FlameParticle.FACTORY, 5);
        }
    }

    @Override
    public String desc() {
        return TXT_DESC;
    }

    public static class Seed
    extends Plant.Seed {
        public Seed() {
            this.plantName = "烈焰花";
            this.name = this.plantName + "种子";
            this.image = 88;
            this.plantClass = Firebloom.class;
            this.alchemyClass = PotionOfLiquidFlame.class;
        }

        @Override
        public String desc() {
            return Firebloom.TXT_DESC;
        }
    }
}

