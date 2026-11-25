/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.scrolls;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Blindness;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;

public class ScrollOfPsionicBlast
extends Scroll {
    public ScrollOfPsionicBlast() {
        this.name = "灵爆秘卷";
    }

    @Override
    protected void doRead() {
        GameScene.flash(0xFFFFFF);
        Sample.INSTANCE.play("snd_blast.mp3");
        Invisibility.dispel();
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (!Level.fieldOfView[mob.pos]) continue;
            Buff.prolong(mob, Blindness.class, Random.Int(3, 6));
            mob.damage(Random.IntRange(1, mob.HT * 2 / 3), this);
        }
        Buff.prolong(curUser, Blindness.class, Random.Int(3, 6));
        Dungeon.observe();
        this.setKnown();
        this.readAnimation();
    }

    @Override
    public String desc() {
        return "这张卷轴蕴含着毁灭性的灵能冲击，可对视野内的所有生物造成巨额伤害；伴随而来的强烈闪光还会暂时致盲范围内的所有生物（包括你自己）。";
    }

    @Override
    public int price() {
        return this.isKnown() ? 80 * this.quantity : super.price();
    }
}

