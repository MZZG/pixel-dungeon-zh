/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.wands;

import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.effects.MagicMissile;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.utils.Callback;

public class WandOfBlink
extends Wand {
    public WandOfBlink() {
        this.name = "闪现法杖";
    }

    @Override
    protected void onZap(int cell) {
        int level = this.power();
        if (Ballistica.distance > level + 4) {
            cell = Ballistica.trace[level + 3];
        } else if (Actor.findChar(cell) != null && Ballistica.distance > 1) {
            cell = Ballistica.trace[Ballistica.distance - 2];
        }
        WandOfBlink.curUser.sprite.visible = true;
        WandOfBlink.appear(Dungeon.hero, cell);
        Dungeon.observe();
    }

    @Override
    protected void fx(int cell, Callback callback) {
        MagicMissile.whiteLight(WandOfBlink.curUser.sprite.parent, WandOfBlink.curUser.pos, cell, callback);
        Sample.INSTANCE.play("snd_zap.mp3");
        WandOfBlink.curUser.sprite.visible = false;
    }

    public static void appear(Char ch, int pos) {
        ch.sprite.interruptMotion();
        ch.move(pos);
        ch.sprite.place(pos);
        if (ch.invisible == 0) {
            ch.sprite.alpha(0.0f);
            ch.sprite.parent.add(new AlphaTweener(ch.sprite, 1.0f, 0.4f));
        }
        ch.sprite.emitter().start(Speck.factory(2), 0.2f, 3);
        Sample.INSTANCE.play("snd_teleport.mp3");
    }

    @Override
    public String desc() {
        return "这根法杖能让你向选定方向瞬间移动，不过生物与实体障碍物会阻挡你的传送路径。";
    }
}

