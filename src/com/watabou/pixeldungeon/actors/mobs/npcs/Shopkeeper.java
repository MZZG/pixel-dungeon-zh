/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs.npcs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.mobs.npcs.NPC;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.particles.ElmoParticle;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.ShopkeeperSprite;
import com.watabou.pixeldungeon.windows.WndBag;
import com.watabou.pixeldungeon.windows.WndTradeItem;

public class Shopkeeper
extends NPC {
    private static WndBag.Listener itemSelector = new WndBag.Listener(){

        @Override
        public void onSelect(Item item) {
            if (item != null) {
                WndBag parentWnd = Shopkeeper.sell();
                GameScene.show(new WndTradeItem(item, parentWnd));
            }
        }
    };

    public Shopkeeper() {
        this.name = "店主";
        this.spriteClass = ShopkeeperSprite.class;
    }

    @Override
    protected boolean act() {
        this.throwItem();
        this.sprite.turnTo(this.pos, Dungeon.hero.pos);
        this.spend(1.0f);
        return true;
    }

    @Override
    public void damage(int dmg, Object src) {
        this.flee();
    }

    @Override
    public void add(Buff buff) {
        this.flee();
    }

    protected void flee() {
        for (Heap heap : Dungeon.level.heaps.values()) {
            if (heap.type != Heap.Type.FOR_SALE) continue;
            CellEmitter.get(heap.pos).burst(ElmoParticle.FACTORY, 4);
            heap.destroy();
        }
        this.destroy();
        this.sprite.killAndErase();
        CellEmitter.get(this.pos).burst(ElmoParticle.FACTORY, 6);
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public String description() {
        return "这个矮胖的家伙看起来更适合待在大城市的商业区，而不是在这种地牢里。不过看他的物价就知道他为什么喜欢在这儿做生意了。";
    }

    public static WndBag sell() {
        return GameScene.selectItem(itemSelector, WndBag.Mode.FOR_SALE, "选择要出售的物品", "cursor_trade.png");
    }

    @Override
    public void interact() {
        Shopkeeper.sell();
    }
}

