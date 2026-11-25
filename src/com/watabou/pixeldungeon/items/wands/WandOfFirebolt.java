/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.wands;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.Fire;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.effects.MagicMissile;
import com.watabou.pixeldungeon.effects.particles.FlameParticle;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfFirebolt
extends Wand {
    public WandOfFirebolt() {
        this.name = "火弹法杖";
    }

    @Override
    protected void onZap(int cell) {
        int level = this.power();
        for (int i = 1; i < Ballistica.distance - 1; ++i) {
            int c = Ballistica.trace[i];
            if (!Level.flamable[c]) continue;
            GameScene.add(Blob.seed(c, 1, Fire.class));
        }
        GameScene.add(Blob.seed(cell, 1, Fire.class));
        Char ch = Actor.findChar(cell);
        if (ch != null) {
            ch.damage(Random.Int(1, 6 + (int)(2.0 * Math.pow(level, 1.5))), this);
            Buff.affect(ch, Burning.class).reignite(ch);
            ch.sprite.emitter().burst(FlameParticle.FACTORY, 5);
            if (ch == curUser && !ch.isAlive()) {
                Dungeon.fail(Utils.format("在第 %2$d 层被自己的%1$s烧死", this.name, Dungeon.depth));
                GLog.n("你用自己的火弹法杖烧死了自己...", new Object[0]);
            }
        }
    }

    @Override
    protected void fx(int cell, Callback callback) {
        MagicMissile.fire(WandOfFirebolt.curUser.sprite.parent, WandOfFirebolt.curUser.pos, cell, callback);
        Sample.INSTANCE.play("snd_zap.mp3");
    }

    @Override
    public String desc() {
        return "这根法杖能喷吐出炽热的魔法火焰，点燃可燃地形并灼伤命中的生物。";
    }
}

