/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.scrolls;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.effects.Enchanting;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.scrolls.InventoryScroll;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.windows.WndBag;

public class ScrollOfEnchantment
extends InventoryScroll {
    private static final String TXT_GLOWS = "你的%s在黑暗中闪烁着微光";

    public ScrollOfEnchantment() {
        this.name = "注魔秘卷";
        this.inventoryTitle = "选择要注魔的武器或护甲";
        this.mode = WndBag.Mode.ENCHANTABLE;
        this.cursor = "cursor_upgrade.png";
    }

    @Override
    protected void onItemSelected(Item item) {
        ScrollOfRemoveCurse.uncurse(Dungeon.hero, item);
        if (item instanceof Weapon) {
            ((Weapon)item).enchant();
        } else {
            ((Armor)item).inscribe();
        }
        item.fix();
        ScrollOfEnchantment.curUser.sprite.emitter().start(Speck.factory(101), 0.2f, 6);
        Enchanting.show(curUser, item);
        GLog.w(TXT_GLOWS, item.name());
    }

    @Override
    public String desc() {
        return "这张卷轴能够为一把武器或一件护甲赋予随机附魔或刻印，赐予其特殊力量。";
    }
}

