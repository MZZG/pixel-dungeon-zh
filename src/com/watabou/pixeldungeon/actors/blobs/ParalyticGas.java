/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.blobs;

import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.effects.BlobEmitter;
import com.watabou.pixeldungeon.effects.Speck;

public class ParalyticGas
extends Blob {
    @Override
    protected void evolve() {
        super.evolve();
        for (int i = 0; i < 1024; ++i) {
            Char ch;
            if (this.cur[i] <= 0 || (ch = Actor.findChar(i)) == null) continue;
            Buff.prolong(ch, Paralysis.class, Paralysis.duration(ch));
        }
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.pour(Speck.factory(108), 0.6f);
    }

    @Override
    public String tileDesc() {
        return "这里盘绕着一片麻痹气体。";
    }
}

