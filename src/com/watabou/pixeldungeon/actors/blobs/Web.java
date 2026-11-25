/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.blobs;

import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Roots;
import com.watabou.pixeldungeon.effects.BlobEmitter;
import com.watabou.pixeldungeon.effects.particles.WebParticle;

public class Web
extends Blob {
    @Override
    protected void evolve() {
        for (int i = 0; i < 1024; ++i) {
            int offv;
            this.off[i] = offv = this.cur[i] > 0 ? this.cur[i] - 1 : 0;
            if (offv <= 0) continue;
            this.volume += offv;
            Char ch = Actor.findChar(i);
            if (ch == null) continue;
            Buff.prolong(ch, Roots.class, 1.0f);
        }
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.pour(WebParticle.FACTORY, 0.4f);
    }

    @Override
    public void seed(int cell, int amount) {
        int diff = amount - this.cur[cell];
        if (diff > 0) {
            this.cur[cell] = amount;
            this.volume += diff;
        }
    }

    @Override
    public String tileDesc() {
        return "这里的一切都被厚厚的蛛网覆盖着。";
    }
}

