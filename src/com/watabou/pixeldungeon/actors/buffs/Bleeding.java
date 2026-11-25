/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.effects.Splash;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Bleeding
extends Buff {
    protected int level;
    private static final String LEVEL = "level";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEVEL, this.level);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.level = bundle.getInt(LEVEL);
    }

    public void set(int level) {
        this.level = level;
    }

    @Override
    public int icon() {
        return 26;
    }

    public String toString() {
        return "流血";
    }

    @Override
    public boolean act() {
        if (this.target.isAlive()) {
            this.level = Random.Int(this.level / 2, this.level);
            if (this.level > 0) {
                this.target.damage(this.level, this);
                if (this.target.sprite.visible) {
                    Splash.at(this.target.sprite.center(), -1.5707964f, 0.5235988f, this.target.sprite.blood(), Math.min(10 * this.level / this.target.HT, 10));
                }
                if (this.target == Dungeon.hero && !this.target.isAlive()) {
                    Dungeon.fail(Utils.format("在第 %d 层失血过多而死", Dungeon.depth));
                    GLog.n("你因失血过多而死...", new Object[0]);
                }
                this.spend(1.0f);
            } else {
                this.detach();
            }
        } else {
            this.detach();
        }
        return true;
    }
}

