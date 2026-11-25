/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.scrolls;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.buffs.Rage;
import com.watabou.pixeldungeon.actors.mobs.Mimic;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.utils.GLog;

public class ScrollOfChallenge
extends Scroll {
    public ScrollOfChallenge() {
        this.name = "决斗秘卷";
    }

    @Override
    protected void doRead() {
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            mob.beckon(ScrollOfChallenge.curUser.pos);
            if (!Dungeon.visible[mob.pos]) continue;
            Buff.affect(mob, Rage.class, Level.distance(ScrollOfChallenge.curUser.pos, mob.pos));
        }
        for (Heap heap : Dungeon.level.heaps.values()) {
            Mimic m;
            if (heap.type != Heap.Type.MIMIC || (m = Mimic.spawnAt(heap.pos, heap.items)) == null) continue;
            m.beckon(ScrollOfChallenge.curUser.pos);
            heap.destroy();
        }
        GLog.w("卷轴发出一声响彻整个地牢的挑衅咆哮！", new Object[0]);
        this.setKnown();
        ScrollOfChallenge.curUser.sprite.centerEmitter().start(Speck.factory(5), 0.3f, 3);
        Sample.INSTANCE.play("snd_challenge.mp3");
        Invisibility.dispel();
        this.readAnimation();
    }

    @Override
    public String desc() {
        return "大声诵读后，这张卷轴会释放出震耳欲聋的战吼，唤醒整层的所有怪物并暴露你的位置。";
    }
}

