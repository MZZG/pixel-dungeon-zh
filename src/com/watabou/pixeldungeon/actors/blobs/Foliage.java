/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.blobs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Journal;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Shadows;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.BlobEmitter;
import com.watabou.pixeldungeon.effects.particles.ShaftParticle;
import com.watabou.pixeldungeon.scenes.GameScene;

public class Foliage
extends Blob {
    @Override
    protected void evolve() {
        int from = 33;
        int to = 991;
        int[] map = Dungeon.level.map;
        boolean regrowth = false;
        int visiblePos = -1;
        for (int pos = from; pos < to; ++pos) {
            if (this.cur[pos] > 0) {
                this.off[pos] = this.cur[pos];
                this.volume += this.off[pos];
                if (map[pos] == 9) {
                    map[pos] = 2;
                    regrowth = true;
                }
                if (!Dungeon.visible[pos]) continue;
                visiblePos = pos;
                continue;
            }
            this.off[pos] = 0;
        }
        Hero hero = Dungeon.hero;
        if (hero.isAlive() && hero.visibleEnemies() == 0 && this.cur[hero.pos] > 0) {
            Buff.affect(hero, Shadows.class).prolong();
        }
        if (regrowth) {
            GameScene.updateMap();
        }
        if (visiblePos != -1) {
            Journal.add(Journal.Feature.GARDEN, visiblePos);
        }
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.start(ShaftParticle.FACTORY, 0.9f, 0);
    }

    @Override
    public String tileDesc() {
        return "光柱刺破了地下花园中的黑暗。";
    }
}

