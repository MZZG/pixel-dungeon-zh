/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.gltextures.TextureCache;
import com.watabou.input.Cursor;
import com.watabou.input.NoosaInputProcessor;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Tooltip;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.hero.Belongings;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.input.GameAction;
import com.watabou.pixeldungeon.items.Gold;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.bags.Bag;
import com.watabou.pixeldungeon.items.bags.Keyring;
import com.watabou.pixeldungeon.items.bags.ScrollHolder;
import com.watabou.pixeldungeon.items.bags.SeedPouch;
import com.watabou.pixeldungeon.items.bags.WandHolster;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;
import com.watabou.pixeldungeon.items.weapon.missiles.Boomerang;
import com.watabou.pixeldungeon.plants.Plant;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.Icons;
import com.watabou.pixeldungeon.ui.ItemSlot;
import com.watabou.pixeldungeon.ui.QuickSlot;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndItem;
import com.watabou.pixeldungeon.windows.WndTabbed;
import com.watabou.utils.GameMath;
import com.watabou.utils.RectF;

public class WndBag
extends WndTabbed {
    protected static final int COLS = 4;
    protected static final int SLOT_SIZE = 28;
    protected static final int SLOT_MARGIN = 1;
    protected static final int TAB_WIDTH = 25;
    protected static final int TITLE_HEIGHT = 12;
    private static final String TXT_THROW = "选择要投掷的物品";
    private static final String TXT_DRINK = "选择要饮用的药剂";
    private static final String TXT_READ = "选择要阅读的卷轴";
    private static final String TXT_ZAP = "选择要释放的法杖";
    private static final String TXT_PLANT = "选择要种植的种子";
    private Listener listener;
    private Mode mode;
    private String title;
    private String cursor;
    public static Class<? extends Wand> wandClass;
    private int nCols;
    private int nRows;
    protected int count;
    protected int col;
    protected int row;
    private static Mode lastMode;
    private static Bag lastBag;

    public WndBag(Bag bag, Listener listener, Mode mode, String title, String cursor) {
        Bag[] bags;
        this.listener = listener;
        this.mode = mode;
        this.title = title;
        this.cursor = cursor != null ? cursor : "cursor_default.png";
        lastMode = mode;
        lastBag = bag;
        this.nCols = 4;
        this.nRows = 24 / this.nCols + (24 % this.nCols > 0 ? 1 : 0);
        int slotsWidth = 28 * this.nCols + 1 * (this.nCols - 1);
        int slotsHeight = 28 * this.nRows + 1 * (this.nRows - 1);
        if (title == null) {
            switch (mode) {
                case THROW: {
                    title = TXT_THROW;
                    break;
                }
                case DRINK: {
                    title = TXT_DRINK;
                    break;
                }
                case READ: {
                    title = TXT_READ;
                    break;
                }
                case ZAP: {
                    title = TXT_ZAP;
                    break;
                }
                case PLANT: {
                    title = TXT_PLANT;
                    break;
                }
                default: {
                    title = Utils.capitalize(bag.name());
                }
            }
        }
        BitmapText txtTitle = PixelScene.createText(title, 9.0f);
        txtTitle.hardlight(0xFFFF44);
        txtTitle.measure();
        txtTitle.x = (int)((float)slotsWidth - txtTitle.width()) / 2;
        txtTitle.y = (int)(12.0f - txtTitle.height()) / 2;
        this.add(txtTitle);
        this.placeItems(bag);
        this.resize(slotsWidth, slotsHeight + 12);
        Belongings stuff = Dungeon.hero.belongings;
        for (Bag b : bags = new Bag[]{stuff.backpack, stuff.getItem(SeedPouch.class), stuff.getItem(ScrollHolder.class), stuff.getItem(WandHolster.class), stuff.getItem(Keyring.class)}) {
            if (b == null) continue;
            BagTab tab = new BagTab(b);
            tab.setSize(25.0f, this.tabHeight());
            this.add(tab);
            if (b == bag) {
                tab.select(true);
                this.selected = tab;
                continue;
            }
            tab.select(false);
        }
    }

    @Override
    protected void onKeyUp(NoosaInputProcessor.Key<GameAction> key) {
        if (key.action == GameAction.BACKPACK) {
            this.hide();
        } else {
            super.onKeyUp(key);
        }
    }

    public static WndBag lastBag(Listener listener, Mode mode, String title, String cursor) {
        if (mode == lastMode && lastBag != null && Dungeon.hero.belongings.backpack.contains(lastBag)) {
            return new WndBag(lastBag, listener, mode, title, cursor);
        }
        return new WndBag(Dungeon.hero.belongings.backpack, listener, mode, title, cursor);
    }

    public static WndBag scrollHolder(Listener listener, Mode mode, String title, String cursor) {
        ScrollHolder holder = Dungeon.hero.belongings.getItem(ScrollHolder.class);
        return holder != null ? new WndBag(holder, listener, mode, title, cursor) : new WndBag(Dungeon.hero.belongings.backpack, listener, mode, title, cursor);
    }

    public static WndBag wandHolster(Listener listener, Mode mode, String title, String cursor) {
        WandHolster holster = Dungeon.hero.belongings.getItem(WandHolster.class);
        return holster != null ? new WndBag(holster, listener, mode, title, cursor) : new WndBag(Dungeon.hero.belongings.backpack, listener, mode, title, cursor);
    }

    public static WndBag seedPouch(Listener listener, Mode mode, String title, String cursor) {
        SeedPouch pouch = Dungeon.hero.belongings.getItem(SeedPouch.class);
        return pouch != null ? new WndBag(pouch, listener, mode, title, cursor) : new WndBag(Dungeon.hero.belongings.backpack, listener, mode, title, cursor);
    }

    protected void placeItems(Bag container) {
        boolean backpack;
        Belongings stuff = Dungeon.hero.belongings;
        this.placeItem(stuff.weapon != null ? stuff.weapon : new Placeholder(5));
        this.placeItem(stuff.armor != null ? stuff.armor : new Placeholder(6));
        this.placeItem(stuff.ring1 != null ? stuff.ring1 : new Placeholder(7));
        this.placeItem(stuff.ring2 != null ? stuff.ring2 : new Placeholder(7));
        boolean bl = backpack = container == Dungeon.hero.belongings.backpack;
        if (!backpack) {
            this.count = this.nCols;
            this.col = 0;
            this.row = 1;
        }
        for (Item item : container.items) {
            this.placeItem(item);
        }
        while (this.count - (backpack ? 4 : this.nCols) < container.size) {
            this.placeItem(null);
        }
        if (container == Dungeon.hero.belongings.backpack) {
            this.row = this.nRows - 1;
            this.col = this.nCols - 1;
            this.placeItem(new Gold(Dungeon.gold));
        }
    }

    protected void placeItem(Item item) {
        int x = this.col * 29;
        int y = 12 + this.row * 29;
        this.add(new ItemButton(item).setPos(x, y));
        if (++this.col >= this.nCols) {
            this.col = 0;
            ++this.row;
        }
        ++this.count;
    }

    @Override
    public void onMenuPressed() {
        if (this.listener == null) {
            this.hide();
        }
    }

    @Override
    public void onBackPressed() {
        if (this.listener != null) {
            this.listener.onSelect(null);
        }
        super.onBackPressed();
    }

    @Override
    public void select(WndTabbed.Tab tab) {
        this.hide();
        GameScene.show(new WndBag(((BagTab)tab).bag, this.listener, this.mode, this.title, this.cursor));
    }

    @Override
    protected int tabHeight() {
        return 20;
    }

    public static interface Listener {
        public void onSelect(Item var1);
    }

    private class ItemButton
    extends ItemSlot {
        private static final int NORMAL = -11907772;
        private static final int EQUIPPED = -10262949;
        private static final int NBARS = 3;
        private Item item;
        private ColorBlock bg;
        private ColorBlock[] durability;

        public ItemButton(Item item) {
            super(item);
            this.item = item;
            if (item instanceof Gold) {
                this.bg.visible = false;
            }
            this.height = 28.0f;
            this.width = 28.0f;
        }

        @Override
        protected void createChildren() {
            this.bg = new ColorBlock(28.0f, 28.0f, -11907772);
            this.add(this.bg);
            super.createChildren();
        }

        @Override
        protected void layout() {
            this.bg.x = this.x;
            this.bg.y = this.y;
            if (this.durability != null) {
                for (int i = 0; i < 3; ++i) {
                    this.durability[i].x = this.x + 1.0f + (float)(i * 3);
                    this.durability[i].y = this.y + this.height - 3.0f;
                }
            }
            super.layout();
        }

        /*
         * Unable to fully structure code
         */
        @Override
        public void item(Item item) {
            block10: {
                block8: {
                    block9: {
                        super.item(item);
                        if (item == null) break block8;
                        this.bg.texture(TextureCache.createSolid(item.isEquipped(Dungeon.hero) != false ? -10262949 : -11907772));
                        if (item.cursed && item.cursedKnown) {
                            this.bg.ra = 0.2f;
                            this.bg.ga = -0.1f;
                        } else if (!item.isIdentified()) {
                            this.bg.ra = 0.1f;
                            this.bg.ba = 0.1f;
                        }
                        if (!Dungeon.legacy && WndBag.lastBag.owner.isAlive() && item.isUpgradable() && item.levelKnown) {
                            this.durability = new ColorBlock[3];
                            int nBars = (int)GameMath.gate(0.0f, Math.round(3.0f * (float)item.durability() / (float)item.maxDurability()), 3.0f);
                            int i;
                            for (i = 0; i < nBars; ++i) {
                                this.durability[i] = new ColorBlock(2.0f, 2.0f, -16716288);
                                this.add(this.durability[i]);
                            }
                            for (i = nBars; i < 3; ++i) {
                                this.durability[i] = new ColorBlock(2.0f, 2.0f, -3407872);
                                this.add(this.durability[i]);
                            }
                        }
                        if (item.name() != null) break block9;
                        this.enable(false);
                        break block10;
                    }
                    boolean enabled = WndBag.this.mode == Mode.QUICKSLOT && item.defaultAction != null || WndBag.this.mode == Mode.FOR_SALE && item.price() > 0 && (!item.isEquipped(Dungeon.hero) || !item.cursed) || WndBag.this.mode == Mode.UPGRADEABLE && item.isUpgradable() || WndBag.this.mode == Mode.UNIDENTIFED && !item.isIdentified() || WndBag.this.mode == Mode.WEAPON && (item instanceof MeleeWeapon || item instanceof Boomerang) || WndBag.this.mode == Mode.ARMOR && item instanceof Armor || WndBag.this.mode == Mode.ENCHANTABLE && (item instanceof MeleeWeapon || item instanceof Boomerang || item instanceof Armor) || WndBag.this.mode == Mode.DRINK && item instanceof Potion || WndBag.this.mode == Mode.READ && item instanceof Scroll || (WndBag.this.mode == Mode.WAND || WndBag.this.mode == Mode.ZAP) && (WndBag.wandClass != null ? WndBag.wandClass.isInstance(item) : item instanceof Wand) || (WndBag.this.mode == Mode.SEED || WndBag.this.mode == Mode.PLANT) && item instanceof Plant.Seed || WndBag.this.mode == Mode.ALL || WndBag.this.mode == Mode.THROW;
                    this.enable(enabled);
                    break block10;
                }
                this.bg.color(-11907772);
            }
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
            if (WndBag.this.listener != null) {
                WndBag.this.hide();
                WndBag.this.listener.onSelect(this.item);
            } else {
                switch (WndBag.this.mode) {
                    case THROW: {
                        WndBag.this.hide();
                        this.item.execute(Dungeon.hero, "THROW");
                        break;
                    }
                    case DRINK: {
                        WndBag.this.hide();
                        this.item.execute(Dungeon.hero, "DRINK");
                        break;
                    }
                    case READ: {
                        WndBag.this.hide();
                        this.item.execute(Dungeon.hero, "READ");
                        break;
                    }
                    case ZAP: {
                        WndBag.this.hide();
                        this.item.execute(Dungeon.hero, "ZAP");
                        break;
                    }
                    case PLANT: {
                        WndBag.this.hide();
                        this.item.execute(Dungeon.hero, "PLANT");
                        break;
                    }
                    default: {
                        if (NoosaInputProcessor.modifier) {
                            this.onLongClick();
                            break;
                        }
                        Game.scene().add(new WndItem(WndBag.this, this.item));
                    }
                }
            }
        }

        @Override
        protected void onMouseMove() {
            Cursor.set(WndBag.this.cursor);
            Tooltip.show(this, this.getTooltip());
        }

        @Override
        protected boolean onLongClick() {
            if (WndBag.this.listener == null && this.item.defaultAction != null) {
                WndBag.this.hide();
                QuickSlot.primaryValue = this.item.stackable ? this.item.getClass() : this.item;
                QuickSlot.refresh();
                return true;
            }
            return false;
        }
    }

    private static class Placeholder
    extends Item {
        public Placeholder(int image) {
            this.name = null;
            this.image = image;
        }

        @Override
        public boolean isIdentified() {
            return true;
        }

        @Override
        public boolean isEquipped(Hero hero) {
            return true;
        }
    }

    private class BagTab
    extends WndTabbed.Tab {
        private Image icon;
        private Bag bag;

        public BagTab(Bag bag) {
            super();
            this.bag = bag;
            this.icon = this.icon();
            this.add(this.icon);
        }

        @Override
        protected void select(boolean value) {
            super.select(value);
            this.icon.am = this.selected ? 1.0f : 0.6f;
        }

        @Override
        protected void layout() {
            super.layout();
            this.icon.copy(this.icon());
            this.icon.x = PixelScene.align(this.x + (this.width - this.icon.width) / 2.0f);
            this.icon.y = PixelScene.align(this.y + (this.height - this.icon.height) / 2.0f - 2.0f - (float)(!this.selected ? 1 : 0));
            if (!this.selected && this.icon.y < this.y + 5.0f) {
                RectF frame = this.icon.frame();
                this.icon.frame(new RectF(frame.left, frame.top + (this.y + 5.0f - this.icon.y) / (float)this.icon.texture.height, frame.right, frame.bottom));
                this.icon.y = this.y + 5.0f;
            }
        }

        private Image icon() {
            if (this.bag instanceof SeedPouch) {
                return Icons.get(Icons.SEED_POUCH);
            }
            if (this.bag instanceof ScrollHolder) {
                return Icons.get(Icons.SCROLL_HOLDER);
            }
            if (this.bag instanceof WandHolster) {
                return Icons.get(Icons.WAND_HOLSTER);
            }
            if (this.bag instanceof Keyring) {
                return Icons.get(Icons.KEYRING);
            }
            return Icons.get(Icons.BACKPACK);
        }

        @Override
        protected String getTooltip() {
            return Utils.capitalize(this.bag.name());
        }
    }

    public static enum Mode {
        ALL,
        UNIDENTIFED,
        UPGRADEABLE,
        QUICKSLOT,
        FOR_SALE,
        WEAPON,
        ARMOR,
        ENCHANTABLE,
        WAND,
        SEED,
        THROW,
        DRINK,
        READ,
        ZAP,
        PLANT;

    }
}

