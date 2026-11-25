/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.plants;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.potions.PotionOfMindVision;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.watabou.pixeldungeon.plants.Plant;

public class Fadeleaf
extends Plant {
    private static final String TXT_DESC = "任何触碰到消逝草的生物都会被传送到当前楼层的随机位置。";

    public Fadeleaf() {
        this.image = 6;
        this.plantName = "消逝草";
    }

    @Override
    public void activate(Char ch) {
        super.activate(ch);
        if (ch instanceof Hero) {
            ScrollOfTeleportation.teleportHero((Hero)ch);
            ((Hero)ch).curAction = null;
        } else if (ch instanceof Mob) {
            int newPos;
            int count = 10;
            do {
                newPos = Dungeon.level.randomRespawnCell();
            } while (count-- > 0 && newPos == -1);
            if (newPos != -1) {
                ch.pos = newPos;
                ch.sprite.place(ch.pos);
                ch.sprite.visible = Dungeon.visible[this.pos];
            }
        }
        if (Dungeon.visible[this.pos]) {
            CellEmitter.get(this.pos).start(Speck.factory(2), 0.2f, 3);
        }
    }

    @Override
    public String desc() {
        return TXT_DESC;
    }

    public static class Seed
    extends Plant.Seed {
        public Seed() {
            this.plantName = "消逝草";
            this.name = this.plantName + "种子";
            this.image = 94;
            this.plantClass = Fadeleaf.class;
            this.alchemyClass = PotionOfMindVision.class;
        }

        @Override
        public String desc() {
            return Fadeleaf.TXT_DESC;
        }
    }
}

