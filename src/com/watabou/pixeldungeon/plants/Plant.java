/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.plants;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Barkskin;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.particles.LeafParticle;
import com.watabou.pixeldungeon.items.Dewdrop;
import com.watabou.pixeldungeon.items.Generator;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.PlantSprite;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import java.util.ArrayList;

public class Plant
implements Bundlable {
    public String plantName;
    public int image;
    public int pos;
    public PlantSprite sprite;
    private static final String POS = "pos";

    public void activate(Char ch) {
        if (ch instanceof Hero && ((Hero)ch).subClass == HeroSubClass.WARDEN) {
            Buff.affect(ch, Barkskin.class).level(ch.HT / 3);
        }
        this.wither();
    }

    public void wither() {
        Dungeon.level.uproot(this.pos);
        this.sprite.kill();
        if (Dungeon.visible[this.pos]) {
            CellEmitter.get(this.pos).burst(LeafParticle.GENERAL, 6);
        }
        if (Dungeon.hero.subClass == HeroSubClass.WARDEN) {
            if (Random.Int(5) == 0) {
                Dungeon.level.drop((Item)Generator.random((Generator.Category)Generator.Category.SEED), (int)this.pos).sprite.drop();
            }
            if (Random.Int(5) == 0) {
                Dungeon.level.drop((Item)new Dewdrop(), (int)this.pos).sprite.drop();
            }
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        this.pos = bundle.getInt(POS);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(POS, this.pos);
    }

    public String desc() {
        return null;
    }

    public static class Seed
    extends Item {
        public static final String AC_PLANT = "PLANT";
        private static final String TXT_INFO = "将这颗种子投掷到你想种植%s的地方。\n\n%s";
        private static final float TIME_TO_PLANT = 1.0f;
        protected Class<? extends Plant> plantClass;
        protected String plantName;
        public Class<? extends Item> alchemyClass;

        public Seed() {
            this.stackable = true;
            this.defaultAction = "THROW";
        }

        @Override
        public ArrayList<String> actions(Hero hero) {
            ArrayList<String> actions = super.actions(hero);
            actions.add(AC_PLANT);
            return actions;
        }

        @Override
        protected void onThrow(int cell) {
            if (Dungeon.level.map[cell] == 42 || Level.pit[cell]) {
                super.onThrow(cell);
            } else {
                Dungeon.level.plant(this, cell);
            }
        }

        @Override
        public void execute(Hero hero, String action) {
            if (action.equals(AC_PLANT)) {
                hero.spend(1.0f);
                hero.busy();
                ((Seed)this.detach(hero.belongings.backpack)).onThrow(hero.pos);
                hero.sprite.operate(hero.pos);
            } else {
                super.execute(hero, action);
            }
        }

        public Plant couch(int pos) {
            try {
                if (Dungeon.visible[pos]) {
                    GameScene.play("snd_plant.mp3", pos);
                }
                Plant plant = ClassReflection.newInstance(this.plantClass);
                plant.pos = pos;
                return plant;
            }
            catch (Exception e) {
                return null;
            }
        }

        @Override
        public boolean isUpgradable() {
            return false;
        }

        @Override
        public boolean isIdentified() {
            return true;
        }

        @Override
        public int price() {
            return 10 * this.quantity;
        }

        @Override
        public String info() {
            return String.format(TXT_INFO, Utils.indefinite(this.plantName), this.desc());
        }
    }
}

