/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.scrolls;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.mobs.npcs.MirrorImage;
import com.watabou.pixeldungeon.effects.Pushing;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;
import java.util.ArrayList;

public class ScrollOfMirrorImage
extends Scroll {
    private static final int NIMAGES = 3;

    public ScrollOfMirrorImage() {
        this.name = "镜像卷轴";
    }

    @Override
    protected void doRead() {
        int nImages;
        ArrayList<Integer> respawnPoints = new ArrayList<Integer>();
        for (int i = 0; i < Level.NEIGHBOURS8.length; ++i) {
            int p = ScrollOfMirrorImage.curUser.pos + Level.NEIGHBOURS8[i];
            if (Actor.findChar(p) != null || !Level.passable[p] && !Level.avoid[p]) continue;
            respawnPoints.add(p);
        }
        for (nImages = 3; nImages > 0 && respawnPoints.size() > 0; --nImages) {
            int index = Random.index(respawnPoints);
            MirrorImage mob = new MirrorImage();
            mob.duplicate(curUser);
            GameScene.add(mob);
            int pos = (Integer)respawnPoints.get(index);
            mob.sprite.place(pos);
            mob.move(pos);
            mob.sprite.flipHorizontal = ScrollOfMirrorImage.curUser.sprite.flipHorizontal;
            Actor.addDelayed(new Mirroring(mob, ScrollOfMirrorImage.curUser.pos, pos), -1.0f);
            respawnPoints.remove(index);
        }
        if (nImages < 3) {
            this.setKnown();
        }
        Sample.INSTANCE.play("snd_read.mp3");
        Invisibility.dispel();
        this.readAnimation();
    }

    @Override
    public String desc() {
        return "卷轴上的咒语会召唤出读者的幻象分身，自动追击你的敌人。";
    }

    protected static class Mirroring
    extends Pushing {
        public Mirroring(Char ch, int from, int to) {
            super(ch, from, to);
            ch.sprite.am = 0.0f;
        }

        @Override
        protected Pushing.Effect effect() {
            this.sprite.emitter().start(Speck.factory(2), 0.2f, 3);
            Sample.INSTANCE.play("snd_teleport.mp3");
            return new Effect();
        }

        protected class Effect
        extends Pushing.Effect {
            protected Effect() {
            }

            @Override
            public void update() {
                super.update();
                ((Mirroring)Mirroring.this).sprite.am = this.delay / 0.15f;
            }
        }
    }
}

