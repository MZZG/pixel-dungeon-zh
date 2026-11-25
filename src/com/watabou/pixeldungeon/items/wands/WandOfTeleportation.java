/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.wands;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.effects.MagicMissile;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

public class WandOfTeleportation
extends Wand {
    public WandOfTeleportation() {
        this.name = "传送法杖";
    }

    @Override
    protected void onZap(int cell) {
        Char ch = Actor.findChar(cell);
        if (ch == curUser) {
            this.setKnown();
            ScrollOfTeleportation.teleportHero(curUser);
        } else if (ch != null) {
            int pos;
            int count = 10;
            do {
                pos = Dungeon.level.randomRespawnCell();
            } while (count-- > 0 && pos == -1);
            if (pos == -1) {
                GLog.w("这里强大的魔力场阻止了传送！", new Object[0]);
            } else {
                ch.pos = pos;
                ch.sprite.place(ch.pos);
                ch.sprite.visible = Dungeon.visible[pos];
                GLog.i(WandOfTeleportation.curUser.name + "将" + ch.name + "传送到了某处", new Object[0]);
            }
        } else {
            GLog.i("什么都没有发生", new Object[0]);
        }
    }

    @Override
    protected void fx(int cell, Callback callback) {
        MagicMissile.coldLight(WandOfTeleportation.curUser.sprite.parent, WandOfTeleportation.curUser.pos, cell, callback);
        Sample.INSTANCE.play("snd_zap.mp3");
    }

    @Override
    public String desc() {
        return "这根法杖射出的魔弹会强行将目标生物传送到当前楼层的随机位置。";
    }
}

