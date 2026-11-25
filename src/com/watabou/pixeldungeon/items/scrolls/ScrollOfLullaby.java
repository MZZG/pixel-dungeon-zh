/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.scrolls;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.buffs.Sleep;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.utils.GLog;

public class ScrollOfLullaby
extends Scroll {
    public ScrollOfLullaby() {
        this.name = "催眠卷轴";
    }

    @Override
    protected void doRead() {
        ScrollOfLullaby.curUser.sprite.centerEmitter().start(Speck.factory(9), 0.3f, 5);
        Sample.INSTANCE.play("snd_lullaby.mp3");
        Invisibility.dispel();
        int count = 0;
        Mob affected = null;
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (!Level.fieldOfView[mob.pos]) continue;
            Buff.affect(mob, Sleep.class);
            if (mob.buff(Sleep.class) == null) continue;
            affected = mob;
            ++count;
        }
        switch (count) {
            case 0: {
                GLog.i("卷轴奏响了轻柔舒缓的旋律。", new Object[0]);
                break;
            }
            case 1: {
                GLog.i("卷轴奏响了轻柔的旋律，" + affected.name + "沉沉睡去！", new Object[0]);
                break;
            }
            default: {
                GLog.i("卷轴奏响了轻柔的旋律，怪物们纷纷陷入沉睡！", new Object[0]);
            }
        }
        this.setKnown();
        this.readAnimation();
    }

    @Override
    public String desc() {
        return "一曲轻柔的催眠曲会让视野内的所有生物陷入沉睡，为你创造逃脱或偷袭的机会。";
    }

    @Override
    public int price() {
        return this.isKnown() ? 50 * this.quantity : super.price();
    }
}

