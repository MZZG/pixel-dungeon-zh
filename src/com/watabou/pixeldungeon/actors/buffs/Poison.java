/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.rings.RingOfElements;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;

public class Poison
extends Buff
implements Hero.Doom {
    protected float left;
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

    public void set(float duration) {
        this.left = duration;
    }

    @Override
    public int icon() {
        return 3;
    }

    public String toString() {
        return "中毒";
    }

    @Override
    public boolean act() {
        if (this.target.isAlive()) {
            this.target.damage((int)(this.left / 3.0f) + 1, this);
            this.spend(1.0f);
            if ((this.left -= 1.0f) <= 0.0f) {
                this.detach();
            }
        } else {
            this.detach();
        }
        return true;
    }

    public static float durationFactor(Char ch) {
        RingOfElements.Resistance r = ch.buff(RingOfElements.Resistance.class);
        return r != null ? r.durationFactor() : 1.0f;
    }

    @Override
    public void onDeath() {
        Badges.validateDeathFromPoison();
        Dungeon.fail(Utils.format("在第 %d 层中毒身亡", Dungeon.depth));
        GLog.n("你被毒死了...", new Object[0]);
    }
}

