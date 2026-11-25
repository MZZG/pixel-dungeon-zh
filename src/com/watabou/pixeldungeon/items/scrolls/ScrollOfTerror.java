/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.scrolls;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.buffs.Terror;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.Flare;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.utils.GLog;

public class ScrollOfTerror
extends Scroll {
    public ScrollOfTerror() {
        this.name = "恐惧卷轴";
    }

    @Override
    protected void doRead() {
        new Flare(5, 32.0f).color(0xFF0000, true).show(ScrollOfTerror.curUser.sprite, 2.0f);
        Sample.INSTANCE.play("snd_read.mp3");
        Invisibility.dispel();
        int count = 0;
        Mob affected = null;
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (!Level.fieldOfView[mob.pos]) continue;
            Buff.affect((Char)mob, Terror.class, (float)10.0f).object = curUser.id();
            ++count;
            affected = mob;
        }
        switch (count) {
            case 0: {
                GLog.i("卷轴迸发出一阵刺眼的红光", new Object[0]);
                break;
            }
            case 1: {
                GLog.i("卷轴迸发出一阵刺眼的红光，" + affected.name + "惊慌逃窜！", new Object[0]);
                break;
            }
            default: {
                GLog.i("卷轴迸发出一阵刺眼的红光，怪物们纷纷四散逃窜！", new Object[0]);
            }
        }
        this.setKnown();
        this.readAnimation();
    }

    @Override
    public String desc() {
        return "一阵炽烈的红光会让视野内的所有生物陷入恐惧并转身逃跑。攻击逃跑中的敌人会解除效果。";
    }

    @Override
    public int price() {
        return this.isKnown() ? 50 * this.quantity : super.price();
    }
}

