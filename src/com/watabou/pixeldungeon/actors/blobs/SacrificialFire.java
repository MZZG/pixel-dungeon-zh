/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.blobs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.Journal;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.FlavourBuff;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.BlobEmitter;
import com.watabou.pixeldungeon.effects.Flare;
import com.watabou.pixeldungeon.effects.Wound;
import com.watabou.pixeldungeon.effects.particles.SacrificialParticle;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfWipeOut;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class SacrificialFire
extends Blob {
    private static final String TXT_WORTHY = "“你的祭品甚是诚心……”";
    private static final String TXT_UNWORTHY = "“你的祭品毫无价值……”";
    private static final String TXT_REWARD = "“你的祭品与你自己都值得赞赏！”";
    protected int pos;

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        for (int i = 0; i < 1024; ++i) {
            if (this.cur[i] <= 0) continue;
            this.pos = i;
            break;
        }
    }

    @Override
    protected void evolve() {
        this.volume = this.off[this.pos] = this.cur[this.pos];
        Char ch = Actor.findChar(this.pos);
        if (ch != null) {
            if (Dungeon.visible[this.pos] && ch.buff(Marked.class) == null) {
                ch.sprite.emitter().burst(SacrificialParticle.FACTORY, 20);
                GameScene.play("snd_burning.mp3", this.pos);
            }
            Buff.prolong(ch, Marked.class, 5.0f);
            for (Mob mob : Dungeon.level.mobs) {
                if (mob == ch) continue;
                mob.beckon(this.pos);
            }
        }
        if (Dungeon.visible[this.pos]) {
            Journal.add(Journal.Feature.SACRIFICIAL_CHAMBER);
        }
    }

    @Override
    public void seed(int cell, int amount) {
        this.cur[this.pos] = 0;
        this.pos = cell;
        this.volume = this.cur[this.pos] = amount;
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.pour(SacrificialParticle.FACTORY, 0.04f);
    }

    public static void sacrifice(Char ch) {
        Wound.hit(ch);
        SacrificialFire fire = (SacrificialFire)Dungeon.level.blobs.get(SacrificialFire.class);
        if (fire != null) {
            int exp = 0;
            if (ch instanceof Mob) {
                exp = ((Mob)ch).exp() * Random.IntRange(1, 3);
            } else if (ch instanceof Hero) {
                exp = ((Hero)ch).maxExp();
            }
            if (exp > 0) {
                int volume = fire.volume - exp;
                if (volume > 0) {
                    fire.seed(fire.pos, volume);
                    GLog.w(TXT_WORTHY, new Object[0]);
                } else {
                    fire.seed(fire.pos, 0);
                    Journal.remove(Journal.Feature.SACRIFICIAL_CHAMBER);
                    GLog.w(TXT_REWARD, new Object[0]);
                    GameScene.effect(new Flare(7, 32.0f).color(0x66FFFF, true).show(ch.sprite.parent, DungeonTilemap.tileCenterToWorld(fire.pos), 2.0f));
                    Dungeon.level.drop((Item)new ScrollOfWipeOut(), (int)fire.pos).sprite.drop();
                }
            } else {
                GLog.w(TXT_UNWORTHY, new Object[0]);
            }
        }
    }

    @Override
    public String tileDesc() {
        return "这里燃烧着献祭之火。任何被这火焰触及的生物都会被标记为献给地牢幽魂的祭品。";
    }

    public static class Marked
    extends FlavourBuff {
        public static final float DURATION = 5.0f;

        @Override
        public int icon() {
            return 31;
        }

        public String toString() {
            return "献祭印记";
        }

        @Override
        public void detach() {
            if (!this.target.isAlive()) {
                SacrificialFire.sacrifice(this.target);
            }
            super.detach();
        }
    }
}

