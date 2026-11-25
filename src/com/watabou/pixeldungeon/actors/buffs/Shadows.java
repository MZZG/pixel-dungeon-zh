/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.utils.Bundle;

public class Shadows
extends Invisibility {
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

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            Sample.INSTANCE.play("snd_meld.mp3");
            Dungeon.observe();
            return true;
        }
        return false;
    }

    @Override
    public void detach() {
        super.detach();
        Dungeon.observe();
    }

    @Override
    public boolean act() {
        if (this.target.isAlive()) {
            this.spend(2.0f);
            if ((this.left -= 1.0f) <= 0.0f || Dungeon.hero.visibleEnemies() > 0) {
                this.detach();
            }
        } else {
            this.detach();
        }
        return true;
    }

    public void prolong() {
        this.left = 2.0f;
    }

    @Override
    public int icon() {
        return 13;
    }

    @Override
    public String toString() {
        return "暗影融合";
    }
}

