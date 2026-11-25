/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.watabou.pixeldungeon.items.EquipableItem;
import com.watabou.pixeldungeon.items.Gold;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.rings.RingOfHaggler;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.ui.RedButton;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.IconTitle;
import com.watabou.pixeldungeon.windows.WndBag;

public class WndTradeItem
extends Window {
    private static final float GAP = 2.0f;
    private static final int WIDTH = 120;
    private static final int BTN_HEIGHT = 16;
    private static final String TXT_SALE = "出售：%s - %d 金币";
    private static final String TXT_BUY = "花 %d 金币购买";
    private static final String TXT_SELL = "卖出换取 %d 金币";
    private static final String TXT_SELL_1 = "卖出 1 个换取 %d 金币";
    private static final String TXT_SELL_ALL = "全部卖出换取 %d 金币";
    private static final String TXT_CANCEL = "算了";
    private static final String TXT_SOLD = "你以 %2$d 金币卖出了%1$s";
    private static final String TXT_BOUGHT = "你花费 %2$d 金币购买了%1$s";
    private WndBag owner;

    public WndTradeItem(final Item item, WndBag owner) {
        this.owner = owner;
        float pos = this.createDescription(item, false);
        if (item.quantity() == 1) {
            RedButton btnSell = new RedButton(Utils.format(TXT_SELL, item.price())){

                @Override
                protected void onClick() {
                    WndTradeItem.this.sell(item);
                    WndTradeItem.this.hide();
                }
            };
            btnSell.setRect(0.0f, pos + 2.0f, 120.0f, 16.0f);
            this.add(btnSell);
            pos = btnSell.bottom();
        } else {
            int priceAll = item.price();
            RedButton btnSell1 = new RedButton(Utils.format(TXT_SELL_1, priceAll / item.quantity())){

                @Override
                protected void onClick() {
                    WndTradeItem.this.sellOne(item);
                    WndTradeItem.this.hide();
                }
            };
            btnSell1.setRect(0.0f, pos + 2.0f, 120.0f, 16.0f);
            this.add(btnSell1);
            RedButton btnSellAll = new RedButton(Utils.format(TXT_SELL_ALL, priceAll)){

                @Override
                protected void onClick() {
                    WndTradeItem.this.sell(item);
                    WndTradeItem.this.hide();
                }
            };
            btnSellAll.setRect(0.0f, btnSell1.bottom() + 2.0f, 120.0f, 16.0f);
            this.add(btnSellAll);
            pos = btnSellAll.bottom();
        }
        RedButton btnCancel = new RedButton(TXT_CANCEL){

            @Override
            protected void onClick() {
                WndTradeItem.this.hide();
            }
        };
        btnCancel.setRect(0.0f, pos + 2.0f, 120.0f, 16.0f);
        this.add(btnCancel);
        this.resize(120, (int)btnCancel.bottom());
    }

    public WndTradeItem(final Heap heap, boolean canBuy) {
        Item item = heap.peek();
        float pos = this.createDescription(item, true);
        int price = this.price(item);
        if (canBuy) {
            RedButton btnBuy = new RedButton(Utils.format(TXT_BUY, price)){

                @Override
                protected void onClick() {
                    WndTradeItem.this.hide();
                    WndTradeItem.this.buy(heap);
                }
            };
            btnBuy.setRect(0.0f, pos + 2.0f, 120.0f, 16.0f);
            btnBuy.enable(price <= Dungeon.gold);
            this.add(btnBuy);
            RedButton btnCancel = new RedButton(TXT_CANCEL){

                @Override
                protected void onClick() {
                    WndTradeItem.this.hide();
                }
            };
            btnCancel.setRect(0.0f, btnBuy.bottom() + 2.0f, 120.0f, 16.0f);
            this.add(btnCancel);
            this.resize(120, (int)btnCancel.bottom());
        } else {
            this.resize(120, (int)pos);
        }
    }

    @Override
    public void hide() {
        super.hide();
        if (this.owner != null) {
            this.owner.hide();
            Shopkeeper.sell();
        }
    }

    private float createDescription(Item item, boolean forSale) {
        IconTitle titlebar = new IconTitle();
        titlebar.icon(new ItemSprite(item.image(), item.glowing()));
        titlebar.label(forSale ? Utils.format(TXT_SALE, item.toString(), this.price(item)) : Utils.capitalize(item.toString()));
        titlebar.setRect(0.0f, 0.0f, 120.0f, 0.0f);
        this.add(titlebar);
        if (item.levelKnown) {
            if (item.level() < 0) {
                titlebar.color(0xFF4444);
            } else if (item.level() > 0) {
                titlebar.color(item.isBroken() ? 0xFF8800 : 0x44FF44);
            }
        }
        BitmapTextMultiline info = PixelScene.createMultiline(item.info(), 6.0f);
        info.maxWidth = 120;
        info.measure();
        info.x = titlebar.left();
        info.y = titlebar.bottom() + 2.0f;
        this.add(info);
        return info.y + info.height();
    }

    private void sell(Item item) {
        Hero hero = Dungeon.hero;
        if (item.isEquipped(hero) && !((EquipableItem)item).doUnequip(hero, false)) {
            return;
        }
        item.detachAll(hero.belongings.backpack);
        int price = item.price();
        new Gold(price).doPickUp(hero);
        GLog.i(TXT_SOLD, item.name(), price);
    }

    private void sellOne(Item item) {
        if (item.quantity() <= 1) {
            this.sell(item);
        } else {
            Hero hero = Dungeon.hero;
            item = item.detach(hero.belongings.backpack);
            int price = item.price();
            new Gold(price).doPickUp(hero);
            GLog.i(TXT_SOLD, item.name(), price);
        }
    }

    private int price(Item item) {
        int price = item.price() * 5 * (Dungeon.depth / 5 + 1);
        if (Dungeon.hero.buff(RingOfHaggler.Haggling.class) != null && price >= 2) {
            price /= 2;
        }
        return price;
    }

    private void buy(Heap heap) {
        Hero hero = Dungeon.hero;
        Item item = heap.pickUp();
        int price = this.price(item);
        Dungeon.gold -= price;
        GLog.i(TXT_BOUGHT, item.name(), price);
        if (!item.doPickUp(hero)) {
            Dungeon.level.drop((Item)item, (int)heap.pos).sprite.drop();
        }
    }
}

