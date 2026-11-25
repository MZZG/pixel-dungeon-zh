/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.FlavourBuff;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.sprites.HeroSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Disguise
extends FlavourBuff {
    public static final float DURATION = 100.0f;
    private static final String COSTUME = "costume";
    public HeroClass costume;

    public void choose(HeroClass notThis, HeroClass notThat) {
        HeroClass[] costumes = new HeroClass[]{HeroClass.WARRIOR, HeroClass.MAGE, HeroClass.ROGUE, HeroClass.HUNTRESS};
        do {
            this.costume = Random.element(costumes);
        } while (this.costume == notThis || this.costume == notThat);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COSTUME, this.costume);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.costume = bundle.getEnum(COSTUME, HeroClass.class);
    }

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            if (target.sprite != null) {
                CellEmitter.get(target.pos).burst(Speck.factory(7), 6);
            }
            return true;
        }
        return false;
    }

    @Override
    public void detach() {
        CellEmitter.get(this.target.pos).burst(Speck.factory(7), 6);
        super.detach();
        if (this.target.sprite != null) {
            ((HeroSprite)this.target.sprite).updateTexture();
        }
    }

    @Override
    public int icon() {
        return 32;
    }

    public String toString() {
        return "伪装";
    }
}

