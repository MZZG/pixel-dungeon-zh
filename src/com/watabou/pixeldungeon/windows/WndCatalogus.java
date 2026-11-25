/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.watabou.input.NoosaInputProcessor;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ui.Component;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.input.GameAction;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.ui.ScrollPane;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndInfoItem;
import com.watabou.pixeldungeon.windows.WndItem;
import com.watabou.pixeldungeon.windows.WndTabbed;
import java.util.ArrayList;

public class WndCatalogus
extends WndTabbed {
    private static final int WIDTH = 128;
    private static final int HEIGHT = 128;
    private static final int ITEM_HEIGHT = 18;
    private static final int TAB_WIDTH = 50;
    private static final String TXT_POTIONS = "药剂";
    private static final String TXT_SCROLLS = "卷轴";
    private static final String TXT_TITLE = "物品图鉴";
    private BitmapText txtTitle;
    private ScrollPane list;
    private ArrayList<ListItem> items = new ArrayList();
    private static boolean showPotions = true;

    public WndCatalogus() {
        WndTabbed.Tab[] tabs;
        this.resize(128, 128);
        this.txtTitle = PixelScene.createText(TXT_TITLE, 9.0f);
        this.txtTitle.hardlight(0xFFFF44);
        this.txtTitle.measure();
        this.add(this.txtTitle);
        this.list = new ScrollPane(new Component()){

            @Override
            public void onClick(float x, float y) {
                int size = WndCatalogus.this.items.size();
                for (int i = 0; i < size && !((ListItem)WndCatalogus.this.items.get(i)).onClick(x, y); ++i) {
                }
            }
        };
        this.add(this.list);
        this.list.setRect(0.0f, this.txtTitle.height(), this.width, (float)this.height - this.txtTitle.height());
        for (WndTabbed.Tab tab : tabs = new WndTabbed.Tab[]{new WndTabbed.LabeledTab(TXT_POTIONS){

            @Override
            protected void select(boolean value) {
                super.select(value);
                showPotions = value;
                WndCatalogus.this.updateList();
            }
        }, new WndTabbed.LabeledTab(TXT_SCROLLS){

            @Override
            protected void select(boolean value) {
                super.select(value);
                showPotions = !value;
                WndCatalogus.this.updateList();
            }
        }}) {
            tab.setSize(50.0f, this.tabHeight());
            this.add(tab);
        }
        this.select(showPotions ? 0 : 1);
    }

    private void updateList() {
        ListItem item;
        this.txtTitle.text(Utils.format(TXT_TITLE, showPotions ? TXT_POTIONS : TXT_SCROLLS));
        this.txtTitle.measure();
        this.txtTitle.x = PixelScene.align(PixelScene.uiCamera, ((float)this.width - this.txtTitle.width()) / 2.0f);
        this.items.clear();
        Component content = this.list.content();
        content.clear();
        this.list.scrollTo(0.0f, 0.0f);
        float pos = 0.0f;
        for (Class<? extends Item> itemClass : showPotions ? Potion.getKnown() : Scroll.getKnown()) {
            item = new ListItem(itemClass);
            item.setRect(0.0f, pos, this.width, 18.0f);
            content.add(item);
            this.items.add(item);
            pos += item.height();
        }
        for (Class<? extends Item> itemClass : showPotions ? Potion.getUnknown() : Scroll.getUnknown()) {
            item = new ListItem(itemClass);
            item.setRect(0.0f, pos, this.width, 18.0f);
            content.add(item);
            this.items.add(item);
            pos += item.height();
        }
        content.setSize(this.width, pos);
        this.list.setSize(this.list.width(), this.list.height());
    }

    @Override
    protected void onKeyUp(NoosaInputProcessor.Key<GameAction> key) {
        if (key.action == GameAction.CATALOGUS) {
            this.hide();
        } else {
            super.onKeyUp(key);
        }
    }

    private class ListItem
    extends Component {
        private Item item;
        private boolean identified;
        private ItemSprite sprite;
        private BitmapText topLeft;
        private BitmapText label;

        public ListItem(Class<? extends Item> cl) {
            try {
                this.item = ClassReflection.newInstance(cl);
                this.identified = this.item.isIdentified();
                if (this.identified) {
                    this.sprite.view(this.item.image(), null);
                    this.label.text(this.item.name());
                } else {
                    this.sprite.view(127, null);
                    this.label.text(this.item.trueName());
                    this.label.hardlight(0xCCCCCC);
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            Item instance = Dungeon.hero.belongings.getItem(cl);
            this.topLeft.text(instance != null && this.identified ? Integer.toString(instance.quantity()) : "");
        }

        @Override
        protected void createChildren() {
            this.sprite = new ItemSprite();
            this.add(this.sprite);
            this.topLeft = new BitmapText(PixelScene.font1x);
            this.topLeft.hardlight(0xFFFF44);
            this.add(this.topLeft);
            this.label = PixelScene.createText(8.0f);
            this.add(this.label);
        }

        @Override
        protected void layout() {
            this.sprite.y = PixelScene.align(this.y + (this.height - this.sprite.height) / 2.0f);
            this.topLeft.x = this.sprite.x;
            this.topLeft.y = this.sprite.y;
            this.label.x = this.sprite.x + this.sprite.width;
            this.label.y = PixelScene.align(this.y + (this.height - this.label.baseLine()) / 2.0f);
        }

        public boolean onClick(float x, float y) {
            if (this.identified && this.inside(x, y)) {
                Object instance = Dungeon.hero.belongings.getItem(this.item.getClass());
                if (instance == null) {
                    GameScene.show(new WndInfoItem(this.item));
                } else {
                    GameScene.show(new WndItem(WndCatalogus.this, (Item)instance));
                }
                return true;
            }
            return false;
        }
    }
}

