/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.plants;

import com.watabou.noosa.Camera;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.particles.EarthParticle;
import com.watabou.pixeldungeon.items.potions.PotionOfParalyticGas;
import com.watabou.pixeldungeon.plants.Plant;
import com.watabou.utils.Bundle;

public class Earthroot
extends Plant {
    private static final String TXT_DESC = "当生物触碰地缚根时，其根须会在其周身缠绕形成一层天然护甲。";

    public Earthroot() {
        this.image = 5;
        this.plantName = "地缚根";
    }

    @Override
    public void activate(Char ch) {
        super.activate(ch);
        if (ch != null) {
            Buff.affect(ch, Armor.class).level = ch.HT;
        }
        if (Dungeon.visible[this.pos]) {
            CellEmitter.bottom(this.pos).start(EarthParticle.FACTORY, 0.05f, 8);
            Camera.main.shake(1.0f, 0.4f);
        }
    }

    @Override
    public String desc() {
        return TXT_DESC;
    }

    public static class Armor
    extends Buff {
        private static final float STEP = 1.0f;
        private int pos;
        private int level;
        private static final String POS = "pos";
        private static final String LEVEL = "level";

        @Override
        public boolean attachTo(Char target) {
            this.pos = target.pos;
            return super.attachTo(target);
        }

        @Override
        public boolean act() {
            if (this.target.pos != this.pos) {
                this.detach();
            }
            this.spend(1.0f);
            return true;
        }

        public int absorb(int damage) {
            if (damage >= this.level) {
                this.detach();
                return damage - this.level;
            }
            this.level -= damage;
            return 0;
        }

        public void level(int value) {
            if (this.level < value) {
                this.level = value;
            }
        }

        @Override
        public int icon() {
            return 20;
        }

        public String toString() {
            return "植被护甲";
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(POS, this.pos);
            bundle.put(LEVEL, this.level);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            this.pos = bundle.getInt(POS);
            this.level = bundle.getInt(LEVEL);
        }
    }

    public static class Seed
    extends Plant.Seed {
        public Seed() {
            this.plantName = "地缚根";
            this.name = this.plantName + "种子";
            this.image = 93;
            this.plantClass = Earthroot.class;
            this.alchemyClass = PotionOfParalyticGas.class;
        }

        @Override
        public String desc() {
            return Earthroot.TXT_DESC;
        }
    }
}

