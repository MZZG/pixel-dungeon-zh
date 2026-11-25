/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.scrolls;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Blindness;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.Bestiary;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.effects.particles.WrathParticle;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.HeroSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Random;
import java.util.ArrayList;

public class ScrollOfWipeOut
extends Item {
    private static final String TXT_BLINDED = "失明时无法阅读卷轴";
    public static final String AC_READ = "READ";
    protected static final float TIME_TO_READ = 1.0f;

    public ScrollOfWipeOut() {
        this.name = "灭绝卷轴";
        this.image = 117;
        this.stackable = true;
        this.defaultAction = AC_READ;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_READ);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action.equals(AC_READ)) {
            if (hero.buff(Blindness.class) != null) {
                GLog.w(TXT_BLINDED, new Object[0]);
            } else {
                curUser = hero;
                curItem = this.detach(hero.belongings.backpack);
                this.doRead();
            }
        } else {
            super.execute(hero, action);
        }
    }

    private void doRead() {
        GameScene.flash(0x224477);
        ScrollOfWipeOut.curUser.sprite.centerEmitter().start(WrathParticle.FACTORY, 0.01f, 30);
        Invisibility.dispel();
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (Bestiary.isBoss(mob)) continue;
            GameScene.play("snd_cursed.mp3", 1.0f, Random.Float(0.6f, 0.9f), mob.pos);
            mob.die(this);
        }
        for (Heap heap : Dungeon.level.heaps.values()) {
            switch (heap.type) {
                case FOR_SALE: {
                    heap.type = Heap.Type.HEAP;
                    if (!Dungeon.visible[heap.pos]) break;
                    CellEmitter.center(heap.pos).burst(Speck.factory(14), 2);
                    break;
                }
                case MIMIC: {
                    heap.type = Heap.Type.HEAP;
                    heap.sprite.link();
                    GameScene.play("snd_cursed.mp3", 1.0f, Random.Float(0.6f, 0.9f), heap.pos);
                    break;
                }
            }
        }
        curUser.spend(1.0f);
        curUser.busy();
        ((HeroSprite)ScrollOfWipeOut.curUser.sprite).read();
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public String desc() {
        return "阅读这张卷轴会释放地牢幽魂的盛怒，瞬间消灭当前楼层的一切生物——好吧，几乎是一切，部分极为强大的存在或许能幸免。";
    }

    @Override
    public int price() {
        return 100 * this.quantity;
    }
}

