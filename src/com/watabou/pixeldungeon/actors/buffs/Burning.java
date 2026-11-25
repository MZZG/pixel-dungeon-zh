/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.Fire;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Light;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.Thief;
import com.watabou.pixeldungeon.effects.particles.ElmoParticle;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.food.ChargrilledMeat;
import com.watabou.pixeldungeon.items.food.MysteryMeat;
import com.watabou.pixeldungeon.items.rings.RingOfElements;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfWipeOut;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Burning
extends Buff
implements Hero.Doom {
    private static final String TXT_BURNS_UP = "%s被烧毁了！";
    private static final String TXT_BURNED_TO_DEATH = "你被燃烧至死...";
    private static final float DURATION = 8.0f;
    private float left;
    private static final String LEFT = "left";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEFT, this.left);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.left = bundle.getFloat(LEFT);
    }

    @Override
    public boolean act() {
        if (this.target.isAlive()) {
            if (this.target instanceof Hero) {
                Buff.prolong(this.target, Light.class, 1.01f);
            }
            this.target.damage(Random.Int(1, 5), this);
            if (this.target instanceof Hero) {
                Item item = ((Hero)this.target).belongings.randomUnequipped();
                if (item instanceof Scroll || item instanceof ScrollOfWipeOut && (double)Random.Float() < 0.5) {
                    item = item.detach(((Hero)this.target).belongings.backpack);
                    GLog.w(TXT_BURNS_UP, item.toString());
                    Heap.burnFX(this.target.pos);
                } else if (item instanceof MysteryMeat) {
                    item = item.detach(((Hero)this.target).belongings.backpack);
                    ChargrilledMeat steak = new ChargrilledMeat();
                    if (!steak.collect(((Hero)this.target).belongings.backpack)) {
                        Dungeon.level.drop((Item)steak, (int)this.target.pos).sprite.drop();
                    }
                    GLog.w(TXT_BURNS_UP, item.toString());
                    Heap.burnFX(this.target.pos);
                }
            } else if (this.target instanceof Thief && ((Thief)this.target).item instanceof Scroll) {
                ((Thief)this.target).item = null;
                this.target.sprite.emitter().burst(ElmoParticle.FACTORY, 6);
            }
        } else {
            this.detach();
        }
        if (Level.flamable[this.target.pos]) {
            GameScene.add(Blob.seed(this.target.pos, 4, Fire.class));
        }
        this.spend(1.0f);
        this.left -= 1.0f;
        if (this.left <= 0.0f || Random.Float() > (2.0f + (float)this.target.HP / (float)this.target.HT) / 3.0f || Level.water[this.target.pos] && !this.target.flying) {
            this.detach();
        }
        return true;
    }

    public void reignite(Char ch) {
        this.left = Burning.duration(ch);
    }

    @Override
    public int icon() {
        return 2;
    }

    public String toString() {
        return "燃烧";
    }

    public static float duration(Char ch) {
        RingOfElements.Resistance r = ch.buff(RingOfElements.Resistance.class);
        return r != null ? r.durationFactor() * 8.0f : 8.0f;
    }

    @Override
    public void onDeath() {
        Badges.validateDeathFromFire();
        Dungeon.fail(Utils.format("在第 %d 层被烧死", Dungeon.depth));
        GLog.n(TXT_BURNED_TO_DEATH, new Object[0]);
    }
}

