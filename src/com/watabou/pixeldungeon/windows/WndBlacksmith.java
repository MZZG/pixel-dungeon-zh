/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;
import com.watabou.pixeldungeon.Chrome;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.npcs.Blacksmith;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.ItemSlot;
import com.watabou.pixeldungeon.ui.RedButton;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.IconTitle;
import com.watabou.pixeldungeon.windows.WndBag;
import com.watabou.pixeldungeon.windows.WndMessage;

public class WndBlacksmith
extends Window {
    private static final int BTN_SIZE = 36;
    private static final float GAP = 2.0f;
    private static final float BTN_GAP = 10.0f;
    private static final int WIDTH = 116;
    private ItemButton btnPressed;
    private ItemButton btnItem1;
    private ItemButton btnItem2;
    private RedButton btnReforge;
    private static final String TXT_PROMPT = "行，一言为定！我能为你做的是：把两件相同的物品重铸成一件品质更好的物品。";
    private static final String TXT_SELECT = "选择要重铸的物品";
    private static final String TXT_REFORGE = "重铸物品";
    protected WndBag.Listener itemSelector = new WndBag.Listener(){

        @Override
        public void onSelect(Item item) {
            if (item != null) {
                WndBlacksmith.this.btnPressed.item(item);
                if (((WndBlacksmith)WndBlacksmith.this).btnItem1.item != null && ((WndBlacksmith)WndBlacksmith.this).btnItem2.item != null) {
                    String result = Blacksmith.verify(((WndBlacksmith)WndBlacksmith.this).btnItem1.item, ((WndBlacksmith)WndBlacksmith.this).btnItem2.item);
                    if (result != null) {
                        GameScene.show(new WndMessage(result));
                        WndBlacksmith.this.btnReforge.enable(false);
                    } else {
                        WndBlacksmith.this.btnReforge.enable(true);
                    }
                }
            }
        }
    };

    public WndBlacksmith(Blacksmith troll, Hero hero) {
        IconTitle titlebar = new IconTitle();
        titlebar.icon(troll.sprite());
        titlebar.label(Utils.capitalize(troll.name));
        titlebar.setRect(0.0f, 0.0f, 116.0f, 0.0f);
        this.add(titlebar);
        BitmapTextMultiline message = PixelScene.createMultiline(TXT_PROMPT, 6.0f);
        message.maxWidth = 116;
        message.measure();
        message.y = titlebar.bottom() + 2.0f;
        this.add(message);
        this.btnItem1 = new ItemButton(){

            @Override
            protected void onClick() {
                WndBlacksmith.this.btnPressed = WndBlacksmith.this.btnItem1;
                GameScene.selectItem(WndBlacksmith.this.itemSelector, WndBag.Mode.UPGRADEABLE, WndBlacksmith.TXT_SELECT, "cursor_reforge.png");
            }
        };
        this.btnItem1.setRect(17.0f, message.y + message.height() + 10.0f, 36.0f, 36.0f);
        this.add(this.btnItem1);
        this.btnItem2 = new ItemButton(){

            @Override
            protected void onClick() {
                WndBlacksmith.this.btnPressed = WndBlacksmith.this.btnItem2;
                GameScene.selectItem(WndBlacksmith.this.itemSelector, WndBag.Mode.UPGRADEABLE, WndBlacksmith.TXT_SELECT, "cursor_reforge.png");
            }
        };
        this.btnItem2.setRect(this.btnItem1.right() + 10.0f, this.btnItem1.top(), 36.0f, 36.0f);
        this.add(this.btnItem2);
        this.btnReforge = new RedButton(TXT_REFORGE){

            @Override
            protected void onClick() {
                Blacksmith.upgrade(((WndBlacksmith)WndBlacksmith.this).btnItem1.item, ((WndBlacksmith)WndBlacksmith.this).btnItem2.item);
                WndBlacksmith.this.hide();
            }
        };
        this.btnReforge.enable(false);
        this.btnReforge.setRect(0.0f, this.btnItem1.bottom() + 10.0f, 116.0f, 20.0f);
        this.add(this.btnReforge);
        this.resize(116, (int)this.btnReforge.bottom());
    }

    public static class ItemButton
    extends Component {
        protected NinePatch bg;
        protected ItemSlot slot;
        public Item item = null;

        @Override
        protected void createChildren() {
            super.createChildren();
            this.bg = Chrome.get(Chrome.Type.BUTTON);
            this.add(this.bg);
            this.slot = new ItemSlot(){

                @Override
                protected void onTouchDown() {
                    ItemButton.this.bg.brightness(1.2f);
                    Sample.INSTANCE.play("snd_click.mp3");
                }

                @Override
                protected void onTouchUp() {
                    ItemButton.this.bg.resetColor();
                }

                @Override
                protected void onClick() {
                    ItemButton.this.onClick();
                }
            };
            this.add(this.slot);
        }

        protected void onClick() {
        }

        @Override
        protected void layout() {
            super.layout();
            this.bg.x = this.x;
            this.bg.y = this.y;
            this.bg.size(this.width, this.height);
            this.slot.setRect(this.x + 2.0f, this.y + 2.0f, this.width - 4.0f, this.height - 4.0f);
        }

        public void item(Item item) {
            this.item = item;
            this.slot.item(this.item);
        }
    }
}

