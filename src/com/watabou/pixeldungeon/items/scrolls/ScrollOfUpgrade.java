/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.scrolls;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.scrolls.InventoryScroll;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.windows.WndBag;

public class ScrollOfUpgrade
extends InventoryScroll {
    private static final String TXT_LOOKS_BETTER = "你的%s现在看起来明显强多了";

    public ScrollOfUpgrade() {
        this.name = "升级卷轴";
        this.inventoryTitle = "选择要升级的物品";
        this.mode = WndBag.Mode.UPGRADEABLE;
        this.cursor = "cursor_upgrade.png";
    }

    @Override
    protected void onItemSelected(Item item) {
        ScrollOfRemoveCurse.uncurse(Dungeon.hero, item);
        if (item.isBroken()) {
            item.fix();
        } else {
            item.upgrade();
        }
        ScrollOfUpgrade.upgrade(Dungeon.hero);
        GLog.p(TXT_LOOKS_BETTER, item.name());
        Badges.validateItemLevelAquired(item);
    }

    public static void upgrade(Hero hero) {
        hero.sprite.emitter().start(Speck.factory(4), 0.2f, 3);
    }

    @Override
    public String desc() {
        return "这张卷轴可以强化一件物品并提升其品质：增强法杖威力与充能上限、提升武器伤害、增加护甲防御力或强化戒指效果，同时还能降低装备的力量需求并解除诅咒。";
    }
}

