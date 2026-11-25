/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.blobs;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.ParalyticGas;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.BlobEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Random;

public class ToxicGas
extends Blob
implements Hero.Doom {
    @Override
    protected void evolve() {
        super.evolve();
        int levelDamage = 5 + Dungeon.depth * 5;
        for (int i = 0; i < 1024; ++i) {
            Char ch;
            if (this.cur[i] <= 0 || (ch = Actor.findChar(i)) == null) continue;
            int damage = (ch.HT + levelDamage) / 40;
            if (Random.Int(40) < (ch.HT + levelDamage) % 40) {
                ++damage;
            }
            ch.damage(damage, this);
        }
        Blob blob = Dungeon.level.blobs.get(ParalyticGas.class);
        if (blob != null) {
            int[] par = blob.cur;
            for (int i = 0; i < 1024; ++i) {
                int p = par[i];
                int t = this.cur[i];
                if (p >= t) {
                    this.volume -= t;
                    this.cur[i] = 0;
                    continue;
                }
                blob.volume -= p;
                par[i] = 0;
            }
        }
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.pour(Speck.factory(107), 0.6f);
    }

    @Override
    public String tileDesc() {
        return "这里盘绕着一片发绿的毒气。";
    }

    @Override
    public void onDeath() {
        Badges.validateDeathFromGas();
        Dungeon.fail(Utils.format("在第 %d 层因毒气窒息而死", Dungeon.depth));
        GLog.n("你死于毒气窒息……", new Object[0]);
    }
}

