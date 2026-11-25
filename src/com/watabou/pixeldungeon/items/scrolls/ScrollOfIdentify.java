/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.scrolls;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.effects.Identification;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.scrolls.InventoryScroll;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.windows.WndBag;

public class ScrollOfIdentify
extends InventoryScroll {
    public ScrollOfIdentify() {
        this.name = "鉴定卷轴";
        this.inventoryTitle = "选择要鉴定的物品";
        this.mode = WndBag.Mode.UNIDENTIFED;
        this.cursor = "cursor_examine.png";
    }

    @Override
    protected void onItemSelected(Item item) {
        ScrollOfIdentify.curUser.sprite.parent.add(new Identification(ScrollOfIdentify.curUser.sprite.center().offset(0.0f, -16.0f)));
        item.identify();
        GLog.i("它是" + item, new Object[0]);
        Badges.validateItemLevelAquired(item);
    }

    @Override
    public String desc() {
        return "永久揭示一件物品的所有隐藏属性。";
    }

    @Override
    public int price() {
        return this.isKnown() ? 30 * this.quantity : super.price();
    }
}

