/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.scrolls;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.windows.WndBag;
import com.watabou.pixeldungeon.windows.WndOptions;

public abstract class InventoryScroll
extends Scroll {
    protected String inventoryTitle = "选择一件物品";
    protected WndBag.Mode mode = WndBag.Mode.ALL;
    protected String cursor = "cursor_default.png";
    private static final String TXT_WARNING = "你真的要取消使用这张卷轴吗？无论如何它都会被消耗掉。";
    private static final String TXT_YES = "是的，我确定";
    private static final String TXT_NO = "不，我改主意了";
    protected static boolean identifiedByUse = false;
    protected static WndBag.Listener itemSelector = new WndBag.Listener(){

        @Override
        public void onSelect(Item item) {
            if (item != null) {
                ((InventoryScroll)curItem).onItemSelected(item);
                ((InventoryScroll)curItem).readAnimation();
                Sample.INSTANCE.play("snd_read.mp3");
                Invisibility.dispel();
            } else if (identifiedByUse) {
                ((InventoryScroll)curItem).confirmCancelation();
            } else {
                curItem.collect(curUser.belongings.backpack);
            }
        }
    };

    @Override
    protected void doRead() {
        if (!this.isKnown()) {
            this.setKnown();
            identifiedByUse = true;
        } else {
            identifiedByUse = false;
        }
        GameScene.selectItem(itemSelector, this.mode, this.inventoryTitle, this.cursor);
    }

    private void confirmCancelation() {
        GameScene.show(new WndOptions(this.name(), TXT_WARNING, new String[]{TXT_YES, TXT_NO}){

            @Override
            protected void onSelect(int index) {
                switch (index) {
                    case 0: {
                        curUser.spendAndNext(1.0f);
                        identifiedByUse = false;
                        break;
                    }
                    case 1: {
                        GameScene.selectItem(itemSelector, InventoryScroll.this.mode, InventoryScroll.this.inventoryTitle);
                    }
                }
            }

            @Override
            public void onBackPressed() {
            }
        });
    }

    protected abstract void onItemSelected(Item var1);
}

