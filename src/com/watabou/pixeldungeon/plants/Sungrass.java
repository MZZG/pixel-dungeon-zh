/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.plants;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.effects.particles.ShaftParticle;
import com.watabou.pixeldungeon.items.potions.PotionOfHealing;
import com.watabou.pixeldungeon.plants.Plant;
import com.watabou.utils.Bundle;

public class Sungrass
extends Plant {
    private static final String TXT_DESC = "阳春草以其汁液卓越的疗伤功效而闻名。";

    public Sungrass() {
        this.image = 4;
        this.plantName = "阳春草";
    }

    @Override
    public void activate(Char ch) {
        super.activate(ch);
        if (ch != null) {
            Buff.affect(ch, Health.class);
        }
        if (Dungeon.visible[this.pos]) {
            CellEmitter.get(this.pos).start(ShaftParticle.FACTORY, 0.2f, 3);
        }
    }

    @Override
    public String desc() {
        return TXT_DESC;
    }

    public static class Health
    extends Buff {
        private static final float STEP = 5.0f;
        private int pos;
        private static final String POS = "pos";

        @Override
        public boolean attachTo(Char target) {
            this.pos = target.pos;
            return super.attachTo(target);
        }

        @Override
        public boolean act() {
            if (this.target.pos != this.pos || this.target.HP >= this.target.HT) {
                this.detach();
            } else {
                this.target.HP = Math.min(this.target.HT, this.target.HP + this.target.HT / 10);
                this.target.sprite.emitter().burst(Speck.factory(0), 1);
            }
            this.spend(5.0f);
            return true;
        }

        @Override
        public int icon() {
            return 19;
        }

        public String toString() {
            return "草药疗养";
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(POS, this.pos);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            this.pos = bundle.getInt(POS);
        }
    }

    public static class Seed
    extends Plant.Seed {
        public Seed() {
            this.plantName = "阳春草";
            this.name = this.plantName + "种子";
            this.image = 92;
            this.plantClass = Sungrass.class;
            this.alchemyClass = PotionOfHealing.class;
        }

        @Override
        public String desc() {
            return Sungrass.TXT_DESC;
        }
    }
}

