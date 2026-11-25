/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Blindness;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.Thief;
import com.watabou.pixeldungeon.sprites.BanditSprite;
import com.watabou.utils.Random;

public class Bandit
extends Thief {
    public Bandit() {
        this.name = "疯狂强盗";
        this.spriteClass = BanditSprite.class;
    }

    @Override
    protected boolean steal(Hero hero) {
        if (super.steal(hero)) {
            Buff.prolong(hero, Blindness.class, Random.Int(5, 12));
            Dungeon.observe();
            return true;
        }
        return false;
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        Badges.validateRare(this);
    }
}

