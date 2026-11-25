/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.input.Cursor;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.hero.Belongings;
import com.watabou.pixeldungeon.input.GameAction;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.HeroSprite;
import com.watabou.pixeldungeon.ui.BadgesList;
import com.watabou.pixeldungeon.ui.ItemSlot;
import com.watabou.pixeldungeon.ui.QuickSlot;
import com.watabou.pixeldungeon.ui.RedButton;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.IconTitle;
import com.watabou.pixeldungeon.windows.WndChallenges;
import com.watabou.pixeldungeon.windows.WndError;
import com.watabou.pixeldungeon.windows.WndItem;
import com.watabou.pixeldungeon.windows.WndTabbed;
import java.util.Locale;

public class WndRanking
extends WndTabbed {
    private static final String TXT_ERROR = "无法载入更多信息。";
    private static final String TXT_STATS = "属性";
    private static final String TXT_ITEMS = "物品";
    private static final String TXT_BADGES = "徽章";
    private static final int WIDTH = 112;
    private static final int HEIGHT = 134;
    private static final int TAB_WIDTH = 40;
    private Thread thread;
    private String error = null;

    public WndRanking(final String gameFile) {
        this.resize(112, 134);
        Cursor.set("cursor_busy.png");
        this.thread = new Thread(){

            @Override
            public void run() {
                try {
                    Badges.loadGlobal();
                    Dungeon.loadGame(gameFile);
                }
                catch (Exception e) {
                    WndRanking.this.error = WndRanking.TXT_ERROR;
                }
            }
        };
        this.thread.start();
    }

    @Override
    public void update() {
        super.update();
        if (this.thread != null && !this.thread.isAlive()) {
            this.thread = null;
            Cursor.set("cursor_default.png");
            if (this.error == null) {
                this.createControls();
            } else {
                this.hide();
                Game.scene().add(new WndError(TXT_ERROR));
            }
        }
    }

    private void createControls() {
        String[] labels = new String[]{TXT_STATS, TXT_ITEMS, TXT_BADGES};
        Group[] pages = new Group[]{new StatsTab(), new ItemsTab(), new BadgesTab()};
        for (int i = 0; i < pages.length; ++i) {
            this.add(pages[i]);
            RankingTab tab = new RankingTab(labels[i], pages[i]);
            tab.setSize(40.0f, this.tabHeight());
            this.add(tab);
        }
        this.select(0);
    }

    private class LabelledItemButton
    extends ItemButton {
        private BitmapText name;

        public LabelledItemButton(Item item) {
            super(item);
        }

        @Override
        protected void createChildren() {
            super.createChildren();
            this.name = PixelScene.createText("?", 7.0f);
            this.add(this.name);
        }

        @Override
        protected void layout() {
            super.layout();
            this.name.x = this.slot.right() + 2.0f;
            this.name.y = this.y + (this.height - this.name.baseLine()) / 2.0f;
            String str = Utils.capitalize(this.item.name());
            this.name.text(str);
            this.name.measure();
            if (this.name.width() > this.width - this.name.x) {
                do {
                    str = str.substring(0, str.length() - 1);
                    this.name.text(str + "...");
                    this.name.measure();
                } while (this.name.width() > this.width - this.name.x);
            }
        }
    }

    private class ItemButton
    extends Button<GameAction> {
        public static final int SIZE = 26;
        protected Item item;
        protected ItemSlot slot;
        private ColorBlock bg;

        public ItemButton(Item item) {
            this.item = item;
            this.slot.item(item);
            if (item.cursed && item.cursedKnown) {
                this.bg.ra = 0.2f;
                this.bg.ga = -0.1f;
            } else if (!item.isIdentified()) {
                this.bg.ra = 0.1f;
                this.bg.ba = 0.1f;
            }
        }

        @Override
        protected void createChildren() {
            this.bg = new ColorBlock(26.0f, 26.0f, -11907772);
            this.add(this.bg);
            this.slot = new ItemSlot();
            this.add(this.slot);
            super.createChildren();
        }

        @Override
        protected void layout() {
            this.bg.x = this.x;
            this.bg.y = this.y;
            this.slot.setRect(this.x, this.y, 26.0f, 26.0f);
            super.layout();
        }

        @Override
        protected void onTouchDown() {
            this.bg.brightness(1.5f);
            Sample.INSTANCE.play("snd_click.mp3", 0.7f, 1.2f, 0.0f);
        }

        @Override
        protected void onTouchUp() {
            this.bg.brightness(1.0f);
        }

        @Override
        protected void onClick() {
            Game.scene().add(new WndItem(null, this.item));
        }
    }

    private class BadgesTab
    extends Group {
        public BadgesTab() {
            this.camera = WndRanking.this.camera;
            BadgesList list = new BadgesList(false);
            this.add(list);
            list.setSize(112.0f, 134.0f);
        }
    }

    private class ItemsTab
    extends Group {
        private int count;
        private float pos;

        public ItemsTab() {
            Belongings stuff = Dungeon.hero.belongings;
            if (stuff.weapon != null) {
                this.addItem(stuff.weapon);
            }
            if (stuff.armor != null) {
                this.addItem(stuff.armor);
            }
            if (stuff.ring1 != null) {
                this.addItem(stuff.ring1);
            }
            if (stuff.ring2 != null) {
                this.addItem(stuff.ring2);
            }
            Item primary = this.getQuickslot(QuickSlot.primaryValue);
            Item secondary = this.getQuickslot(QuickSlot.secondaryValue);
            if (this.count >= 4 && primary != null && secondary != null) {
                float size = 26.0f;
                ItemButton slot = new ItemButton(primary);
                slot.setRect(0.0f, this.pos, size, size);
                this.add(slot);
                slot = new ItemButton(secondary);
                slot.setRect(size + 1.0f, this.pos, size, size);
                this.add(slot);
            } else {
                if (primary != null) {
                    this.addItem(primary);
                }
                if (secondary != null) {
                    this.addItem(secondary);
                }
            }
        }

        private void addItem(Item item) {
            LabelledItemButton slot = new LabelledItemButton(item);
            slot.setRect(0.0f, this.pos, WndRanking.this.width, 26.0f);
            this.add(slot);
            this.pos += slot.height() + 1.0f;
            ++this.count;
        }

        private Item getQuickslot(Object value) {
            Object item;
            if (value instanceof Item && Dungeon.hero.belongings.backpack.contains((Item)value)) {
                return (Item)value;
            }
            if (value instanceof Class && (item = Dungeon.hero.belongings.getItem((Class)value)) != null) {
                return (Item)item;
            }
            return null;
        }
    }

    private class StatsTab
    extends Group {
        private static final int GAP = 2;
        private static final String TXT_TITLE = "%d级 %s";
        private static final String TXT_CHALLENGES = "挑战";
        private static final String TXT_HEALTH = "生命";
        private static final String TXT_STR = "力量";
        private static final String TXT_DURATION = "游戏时长";
        private static final String TXT_DEPTH = "最高层数";
        private static final String TXT_ENEMIES = "击杀敌人数";
        private static final String TXT_GOLD = "金币收集数";
        private static final String TXT_ANKHS = "十字架使用";
        private static final String TXT_FOOD = "食物消耗量";
        private static final String TXT_ALCHEMY = "炼制药剂数";

        public StatsTab() {
            String heroClass = Dungeon.hero.className();
            IconTitle title = new IconTitle();
            title.icon(HeroSprite.avatar(Dungeon.hero.heroClass, Dungeon.hero.tier()));
            title.label(Utils.format(TXT_TITLE, Dungeon.hero.lvl, heroClass).toUpperCase(Locale.ENGLISH));
            title.setRect(0.0f, 0.0f, 112.0f, 0.0f);
            this.add(title);
            float pos = title.bottom();
            if (Dungeon.challenges > 0) {
                RedButton btnCatalogus = new RedButton(TXT_CHALLENGES){

                    @Override
                    protected void onClick() {
                        Game.scene().add(new WndChallenges(Dungeon.challenges, false));
                    }
                };
                btnCatalogus.setRect(0.0f, pos + 2.0f, btnCatalogus.reqWidth() + 2.0f, btnCatalogus.reqHeight() + 2.0f);
                this.add(btnCatalogus);
                pos = btnCatalogus.bottom();
            }
            pos += 6.0f;
            pos = this.statSlot(this, TXT_STR, Integer.toString(Dungeon.hero.STR), pos);
            pos = this.statSlot(this, TXT_HEALTH, Integer.toString(Dungeon.hero.HT), pos);
            pos += 6.0f;
            pos = this.statSlot(this, TXT_DURATION, Integer.toString((int)Statistics.getFloat(Statistics.Type.DURATION)), pos);
            pos += 6.0f;
            pos = this.statSlot(this, TXT_DEPTH, Integer.toString(Statistics.getInt(Statistics.Type.DEEPEST)), pos);
            pos = this.statSlot(this, TXT_ENEMIES, Integer.toString(Statistics.getInt(Statistics.Type.SLAIN)), pos);
            pos = this.statSlot(this, TXT_GOLD, Integer.toString(Statistics.getInt(Statistics.Type.GOLD)), pos);
            pos = this.statSlot(this, TXT_ANKHS, Integer.toString(Statistics.getInt(Statistics.Type.ANKHS)), pos);
            pos += 6.0f;
            pos = this.statSlot(this, TXT_FOOD, Integer.toString(Statistics.getInt(Statistics.Type.FOOD)), pos);
            pos = this.statSlot(this, TXT_ALCHEMY, Integer.toString(Statistics.getInt(Statistics.Type.ALCHEMY)), pos);
        }

        private float statSlot(Group parent, String label, String value, float pos) {
            BitmapText txt = PixelScene.createText(label, 7.0f);
            txt.y = pos;
            parent.add(txt);
            txt = PixelScene.createText(value, 7.0f);
            txt.measure();
            txt.x = PixelScene.align(72.799995f);
            txt.y = pos;
            parent.add(txt);
            return pos + 2.0f + txt.baseLine();
        }
    }

    private class RankingTab
    extends WndTabbed.LabeledTab {
        private Group page;

        public RankingTab(String label, Group page) {
            super(label);
            this.page = page;
        }

        @Override
        protected void select(boolean value) {
            super.select(value);
            if (this.page != null) {
                this.page.visible = this.page.active = this.selected;
            }
        }
    }
}

