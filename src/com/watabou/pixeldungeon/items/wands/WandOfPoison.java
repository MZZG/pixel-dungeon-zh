/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.wands;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.effects.MagicMissile;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

public class WandOfPoison
extends Wand {
    public WandOfPoison() {
        this.name = "剧毒法杖";
    }

    @Override
    protected void onZap(int cell) {
        Char ch = Actor.findChar(cell);
        if (ch != null) {
            Buff.affect(ch, Poison.class).set(Poison.durationFactor(ch) * (float)(5 + this.power()));
        } else {
            GLog.i("什么都没有发生", new Object[0]);
        }
    }

    @Override
    protected void fx(int cell, Callback callback) {
        MagicMissile.poison(WandOfPoison.curUser.sprite.parent, WandOfPoison.curUser.pos, cell, callback);
        Sample.INSTANCE.play("snd_zap.mp3");
    }

    @Override
    public String desc() {
        return "这根扭曲木杖射出的剧毒魔弹会让目标身中剧毒并持续掉血，法杖等级越高，中毒持续时间越长。";
    }
}

