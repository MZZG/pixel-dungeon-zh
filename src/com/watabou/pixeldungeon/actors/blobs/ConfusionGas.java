/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.blobs;

import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Vertigo;
import com.watabou.pixeldungeon.effects.BlobEmitter;
import com.watabou.pixeldungeon.effects.Speck;

public class ConfusionGas
extends Blob {
    @Override
    protected void evolve() {
        super.evolve();
        for (int i = 0; i < 1024; ++i) {
            Char ch;
            if (this.cur[i] <= 0 || (ch = Actor.findChar(i)) == null) continue;
            Buff.prolong(ch, Vertigo.class, Vertigo.duration(ch));
        }
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.pour(Speck.factory(111, true), 0.6f);
    }

    @Override
    public String tileDesc() {
        return "这里盘绕着一片致眩气体。";
    }
}

