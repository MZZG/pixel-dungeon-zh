/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.actors.mobs.Brute;
import com.watabou.pixeldungeon.sprites.ShieldedSprite;

public class Shielded
extends Brute {
    public Shielded() {
        this.name = "持盾暴徒";
        this.spriteClass = ShieldedSprite.class;
        this.defenseSkill = 20;
    }

    @Override
    public int dr() {
        return 10;
    }

    @Override
    public String defenseVerb() {
        return "格挡";
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        Badges.validateRare(this);
    }
}

