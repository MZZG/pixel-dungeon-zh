/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.rings;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.rings.Ring;

public class RingOfThorns
extends Ring {
    public RingOfThorns() {
        this.name = "荆棘之戒";
    }

    @Override
    protected Ring.RingBuff buff() {
        return new Thorns();
    }

    @Override
    public Item random() {
        this.level(1);
        return this;
    }

    @Override
    public boolean doPickUp(Hero hero) {
        this.identify();
        Badges.validateRingOfThorns();
        Badges.validateItemLevelAquired(this);
        return super.doPickUp(hero);
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public void use() {
    }

    @Override
    public String desc() {
        return this.isKnown() ? "虽然这枚戒指本身并没有实体尖刺，但攻击你的敌人会受到其自身造成伤害一定比例的反伤。强化此戒指不会带来额外加成。" : super.desc();
    }

    public class Thorns
    extends Ring.RingBuff {
    }
}

