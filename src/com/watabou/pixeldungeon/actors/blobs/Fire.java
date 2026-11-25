/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.blobs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.effects.BlobEmitter;
import com.watabou.pixeldungeon.effects.particles.FlameParticle;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;

public class Fire
extends Blob {
    @Override
    protected void evolve() {
        boolean[] flamable = Level.flamable;
        int from = 33;
        int to = 991;
        boolean observe = false;
        for (int pos = from; pos < to; ++pos) {
            int fire;
            if (this.cur[pos] > 0) {
                this.burn(pos);
                fire = this.cur[pos] - 1;
                if (fire <= 0 && flamable[pos]) {
                    int oldTile = Dungeon.level.map[pos];
                    Dungeon.level.destroy(pos);
                    observe = true;
                    GameScene.updateMap(pos);
                    if (Dungeon.visible[pos]) {
                        GameScene.discoverTile(pos, oldTile);
                    }
                }
            } else if (flamable[pos] && (this.cur[pos - 1] > 0 || this.cur[pos + 1] > 0 || this.cur[pos - 32] > 0 || this.cur[pos + 32] > 0)) {
                fire = 4;
                this.burn(pos);
            } else {
                fire = 0;
            }
            this.off[pos] = fire;
            this.volume += this.off[pos];
        }
        if (observe) {
            Dungeon.observe();
        }
    }

    private void burn(int pos) {
        Heap heap;
        Char ch = Actor.findChar(pos);
        if (ch != null) {
            Buff.affect(ch, Burning.class).reignite(ch);
        }
        if ((heap = (Heap)Dungeon.level.heaps.get(pos)) != null) {
            heap.burn();
        }
    }

    @Override
    public void seed(int cell, int amount) {
        if (this.cur[cell] == 0) {
            this.volume += amount;
            this.cur[cell] = amount;
        }
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.start(FlameParticle.FACTORY, 0.03f, 0);
    }

    @Override
    public String tileDesc() {
        return "一团火焰正在这里肆虐。";
    }
}

