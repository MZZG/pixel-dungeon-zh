/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.plants;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Roots;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.bags.Bag;
import com.watabou.pixeldungeon.items.potions.PotionOfStrength;
import com.watabou.pixeldungeon.plants.Plant;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;

public class Rotberry
extends Plant {
    private static final String TXT_DESC = "这种灌木结出的浆果尝起来有着甜美而又致命的滋味。";

    public Rotberry() {
        this.image = 7;
        this.plantName = "腐莓";
    }

    @Override
    public void activate(Char ch) {
        super.activate(ch);
        GameScene.add(Blob.seed(this.pos, 100, ToxicGas.class));
        Dungeon.level.drop((Item)new Seed(), (int)this.pos).sprite.drop();
        if (ch != null) {
            Buff.prolong(ch, Roots.class, 3.0f);
        }
    }

    @Override
    public String desc() {
        return TXT_DESC;
    }

    public static class Seed
    extends Plant.Seed {
        public Seed() {
            this.plantName = "腐莓";
            this.name = this.plantName + "种子";
            this.image = 95;
            this.plantClass = Rotberry.class;
            this.alchemyClass = PotionOfStrength.class;
        }

        @Override
        public boolean collect(Bag container) {
            if (super.collect(container)) {
                if (Dungeon.level != null) {
                    for (Mob mob : Dungeon.level.mobs) {
                        mob.beckon(Dungeon.hero.pos);
                    }
                    GLog.w("这颗种子发出一声响彻地牢的咆哮！", new Object[0]);
                    CellEmitter.center(Dungeon.hero.pos).start(Speck.factory(5), 0.3f, 3);
                    Sample.INSTANCE.play("snd_challenge.mp3");
                }
                return true;
            }
            return false;
        }

        @Override
        public String desc() {
            return Rotberry.TXT_DESC;
        }
    }
}

