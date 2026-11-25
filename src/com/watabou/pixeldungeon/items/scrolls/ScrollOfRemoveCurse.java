/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.scrolls;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.buffs.Weakness;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.Flare;
import com.watabou.pixeldungeon.effects.particles.ShadowParticle;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.utils.GLog;

public class ScrollOfRemoveCurse
extends Scroll {
    private static final String TXT_PROCCED = "你的背包散发出净化的光芒，一股邪恶能量消散了。";
    private static final String TXT_NOT_PROCCED = "你的背包散发出净化的光芒，但什么也没有发生。";

    public ScrollOfRemoveCurse() {
        this.name = "祛邪卷轴";
    }

    @Override
    protected void doRead() {
        new Flare(6, 32.0f).show(ScrollOfRemoveCurse.curUser.sprite, 2.0f);
        Sample.INSTANCE.play("snd_read.mp3");
        Invisibility.dispel();
        boolean procced = ScrollOfRemoveCurse.uncurse(curUser, ScrollOfRemoveCurse.curUser.belongings.backpack.items.toArray(new Item[0]));
        procced = ScrollOfRemoveCurse.uncurse(curUser, ScrollOfRemoveCurse.curUser.belongings.weapon, ScrollOfRemoveCurse.curUser.belongings.armor, ScrollOfRemoveCurse.curUser.belongings.ring1, ScrollOfRemoveCurse.curUser.belongings.ring2) || procced;
        Weakness.detach(curUser, Weakness.class);
        if (procced) {
            GLog.p(TXT_PROCCED, new Object[0]);
        } else {
            GLog.i(TXT_NOT_PROCCED, new Object[0]);
        }
        this.setKnown();
        this.readAnimation();
    }

    @Override
    public String desc() {
        return "卷轴上的净化咒语会立刻驱散武器、护甲、戒指与随身物品上一切让你无法取下的邪恶诅咒。";
    }

    public static boolean uncurse(Hero hero, Item ... items) {
        boolean procced = false;
        for (Item item : items) {
            if (item == null || !item.cursed) continue;
            item.cursed = false;
            procced = true;
        }
        if (procced) {
            hero.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
        }
        return procced;
    }

    @Override
    public int price() {
        return this.isKnown() ? 30 * this.quantity : super.price();
    }
}

