/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.plants;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Fire;
import com.watabou.pixeldungeon.actors.blobs.Freezing;
import com.watabou.pixeldungeon.items.potions.PotionOfFrost;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.plants.Plant;
import com.watabou.pixeldungeon.utils.BArray;
import com.watabou.utils.PathFinder;

public class Icecap
extends Plant {
    private static final String TXT_DESC = "触碰冰冠花时，它会散发出冰冷的花粉，冻结周围的一切。";

    public Icecap() {
        this.image = 1;
        this.plantName = "冰冠花";
    }

    @Override
    public void activate(Char ch) {
        super.activate(ch);
        PathFinder.buildDistanceMap(this.pos, BArray.not(Level.losBlocking, null), 1);
        Fire fire = (Fire)Dungeon.level.blobs.get(Fire.class);
        for (int i = 0; i < 1024; ++i) {
            if (PathFinder.distance[i] >= Integer.MAX_VALUE) continue;
            Freezing.affect(i, fire);
        }
    }

    @Override
    public String desc() {
        return TXT_DESC;
    }

    public static class Seed
    extends Plant.Seed {
        public Seed() {
            this.plantName = "冰冠花";
            this.name = this.plantName + "种子";
            this.image = 89;
            this.plantClass = Icecap.class;
            this.alchemyClass = PotionOfFrost.class;
        }

        @Override
        public String desc() {
            return Icecap.TXT_DESC;
        }
    }
}

