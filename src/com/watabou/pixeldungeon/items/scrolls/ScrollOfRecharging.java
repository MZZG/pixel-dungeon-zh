/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.scrolls;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.SpellSprite;
import com.watabou.pixeldungeon.effects.particles.EnergyParticle;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.utils.GLog;

public class ScrollOfRecharging
extends Scroll {
    public ScrollOfRecharging() {
        this.name = "充能卷轴";
    }

    @Override
    protected void doRead() {
        int count = ScrollOfRecharging.curUser.belongings.charge(true);
        ScrollOfRecharging.charge(curUser);
        Sample.INSTANCE.play("snd_read.mp3");
        Invisibility.dispel();
        if (count > 0) {
            GLog.i("一股能量涌过你的背包，为你的法杖充满了魔力", new Object[0]);
            SpellSprite.show(curUser, 2);
        } else {
            GLog.i("一股能量涌过你的背包，但什么也没有发生", new Object[0]);
        }
        this.setKnown();
        this.readAnimation();
    }

    @Override
    public String desc() {
        return "羊皮纸中封存的原始魔力在释放后会立刻将你携带的所有法杖完全充能。";
    }

    public static void charge(Hero hero) {
        hero.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 15);
    }

    @Override
    public int price() {
        return this.isKnown() ? 40 * this.quantity : super.price();
    }
}

