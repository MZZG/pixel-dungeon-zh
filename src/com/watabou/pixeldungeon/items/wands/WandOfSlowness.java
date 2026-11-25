/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.wands;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Slow;
import com.watabou.pixeldungeon.effects.MagicMissile;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

public class WandOfSlowness
extends Wand {
    public WandOfSlowness() {
        this.name = "迟缓法杖";
    }

    @Override
    protected void onZap(int cell) {
        Char ch = Actor.findChar(cell);
        if (ch != null) {
            Buff.affect(ch, Slow.class, (float)(4 + this.power()) * Slow.durationFactor(ch));
        } else {
            GLog.i("什么都没有发生", new Object[0]);
        }
    }

    @Override
    protected void fx(int cell, Callback callback) {
        MagicMissile.slowness(WandOfSlowness.curUser.sprite.parent, WandOfSlowness.curUser.pos, cell, callback);
        Sample.INSTANCE.play("snd_zap.mp3");
    }

    @Override
    public String desc() {
        return "这根法杖会使目标在效果结束前移动与攻击速度减半。";
    }
}

