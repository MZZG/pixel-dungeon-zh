/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.wands;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Amok;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Vertigo;
import com.watabou.pixeldungeon.effects.MagicMissile;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

public class WandOfAmok
extends Wand {
    public WandOfAmok() {
        this.name = "狂乱法杖";
    }

    @Override
    protected void onZap(int cell) {
        Char ch = Actor.findChar(cell);
        if (ch != null) {
            if (ch == Dungeon.hero) {
                Buff.affect(ch, Vertigo.class, Vertigo.duration(ch));
            } else {
                Buff.affect(ch, Amok.class, 3.0f + (float)this.power());
            }
        } else {
            GLog.i("什么都没有发生", new Object[0]);
        }
    }

    @Override
    protected void fx(int cell, Callback callback) {
        MagicMissile.purpleLight(WandOfAmok.curUser.sprite.parent, WandOfAmok.curUser.pos, cell, callback);
        Sample.INSTANCE.play("snd_zap.mp3");
    }

    @Override
    public String desc() {
        return "这根法杖射出的紫色光线会让目标陷入狂乱，无差别地攻击周围的生物。";
    }
}

