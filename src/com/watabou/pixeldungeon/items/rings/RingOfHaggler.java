/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.rings;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.rings.Ring;

public class RingOfHaggler
extends Ring {
    public RingOfHaggler() {
        this.name = "商贾之戒";
    }

    @Override
    protected Ring.RingBuff buff() {
        return new Haggling();
    }

    @Override
    public Item random() {
        this.level(1);
        return this;
    }

    @Override
    public boolean doPickUp(Hero hero) {
        this.identify();
        Badges.validateRingOfHaggler();
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
        return this.isKnown() ? "实际上这枚戒指并没有什么魔法效果，但它能向商人们证明佩戴者是盗贼公会的成员。通常他们会乐意给个折扣以换取免遭偷窃的保证。强化此戒指不会带来额外加成。" : super.desc();
    }

    public class Haggling
    extends Ring.RingBuff {
    }
}

