/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs.npcs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.effects.particles.ElmoParticle;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.sprites.ImpSprite;
import com.watabou.pixeldungeon.utils.Utils;

public class ImpShopkeeper
extends Shopkeeper {
    private static final String TXT_GREETINGS = "你好啊，朋友！";
    private boolean seenBefore;

    public ImpShopkeeper() {
        this.name = "野心勃勃的小恶魔";
        this.spriteClass = ImpSprite.class;
        this.seenBefore = false;
    }

    @Override
    protected boolean act() {
        if (!this.seenBefore && Dungeon.visible[this.pos]) {
            this.yell(Utils.format(TXT_GREETINGS, new Object[0]));
            this.seenBefore = true;
        }
        return super.act();
    }

    @Override
    protected void flee() {
        for (Heap heap : Dungeon.level.heaps.values()) {
            if (heap.type != Heap.Type.FOR_SALE) continue;
            CellEmitter.get(heap.pos).burst(ElmoParticle.FACTORY, 4);
            heap.destroy();
        }
        this.destroy();
        this.sprite.emitter().burst(Speck.factory(7), 15);
        this.sprite.killAndErase();
    }

    @Override
    public String description() {
        return "小恶魔是低阶恶魔。它们既不以力量见长也没有魔法天赋，但十分聪明且善于交际，许多小恶魔喜欢在非恶魔种族之间做生意。";
    }
}

