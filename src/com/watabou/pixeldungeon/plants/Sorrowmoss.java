/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.plants;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.particles.PoisonParticle;
import com.watabou.pixeldungeon.items.potions.PotionOfToxicGas;
import com.watabou.pixeldungeon.plants.Plant;

public class Sorrowmoss
extends Plant {
    private static final String TXT_DESC = "断肠苔（其实是一种花而非苔藓）长着剃刀般锋利的花瓣，表面涂满致命毒液。";

    public Sorrowmoss() {
        this.image = 2;
        this.plantName = "断肠苔";
    }

    @Override
    public void activate(Char ch) {
        super.activate(ch);
        if (ch != null) {
            Buff.affect(ch, Poison.class).set(Poison.durationFactor(ch) * (float)(4 + Dungeon.depth / 2));
        }
        if (Dungeon.visible[this.pos]) {
            CellEmitter.center(this.pos).burst(PoisonParticle.SPLASH, 3);
        }
    }

    @Override
    public String desc() {
        return TXT_DESC;
    }

    public static class Seed
    extends Plant.Seed {
        public Seed() {
            this.plantName = "断肠苔";
            this.name = this.plantName + "种子";
            this.image = 90;
            this.plantClass = Sorrowmoss.class;
            this.alchemyClass = PotionOfToxicGas.class;
        }

        @Override
        public String desc() {
            return Sorrowmoss.TXT_DESC;
        }
    }
}

