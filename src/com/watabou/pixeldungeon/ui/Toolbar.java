/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.ui;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Component;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.effects.BitmaskEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.effects.particles.BloodParticle;
import com.watabou.pixeldungeon.input.GameAction;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.scenes.CellSelector;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.ui.Button;
import com.watabou.pixeldungeon.ui.GoldIndicator;
import com.watabou.pixeldungeon.ui.QuickSlot;
import com.watabou.pixeldungeon.windows.WndBag;
import com.watabou.pixeldungeon.windows.WndCatalogus;
import com.watabou.pixeldungeon.windows.WndHero;

public class Toolbar
extends Component {
    private static final int SKIN_WIDTH = 150;
    private Image skin;
    private Tool btnWait;
    private Tool btnSearch;
    private Tool btnInfo;
    private Tool btnInventory;
    private Tool btnQuick1;
    private Tool btnQuick2;
    private Tool[] tools;
    private CharacterShield avatar;
    private Image exp;
    private PickedUpItem pickedUp;
    private boolean lastEnabled = true;
    private static CellSelector.Listener informer = new CellSelector.Listener(){

        @Override
        public void onSelect(Integer cell) {
            GameScene.examineCell(cell);
        }

        @Override
        public String prompt() {
            return "选择要查看的格子";
        }

        @Override
        public String cursor() {
            return "cursor_examine.png";
        }
    };

    public Toolbar() {
        this.width = 150.0f;
        this.height = 26.0f;
    }

    @Override
    protected void createChildren() {
        this.skin = new Image("toolbar.png");
        this.add(this.skin);
        this.btnWait = new Tool(0, 38, 20, 24, GameAction.REST){

            @Override
            protected void onClick() {
                this.restOneTurn();
            }

            @Override
            protected boolean onLongClick() {
                this.restFull();
                return true;
            }

            private void restOneTurn() {
                Dungeon.hero.rest(false);
            }

            private void restFull() {
                Dungeon.hero.rest(true);
            }
        };
        this.add(this.btnWait);
        this.btnSearch = new Tool(20, 38, 20, 24, GameAction.SEARCH){

            @Override
            protected void onClick() {
                Dungeon.hero.search(true);
            }
        };
        this.add(this.btnSearch);
        this.btnInfo = new Tool(40, 38, 20, 24, GameAction.CELL_INFO){

            @Override
            protected void onClick() {
                GameScene.selectCell(informer);
            }
        };
        this.add(this.btnInfo);
        this.avatar = new CharacterShield();
        this.add(this.avatar);
        this.btnInventory = new Tool(90, 38, 20, 24, GameAction.BACKPACK){
            private GoldIndicator gold;

            @Override
            protected void onClick() {
                this.showBackpack();
            }

            @Override
            protected boolean onLongClick() {
                this.showCatalogus();
                return true;
            }

            private void showBackpack() {
                GameScene.show(new WndBag(Dungeon.hero.belongings.backpack, null, WndBag.Mode.ALL, null, null));
            }

            private void showCatalogus() {
                GameScene.show(new WndCatalogus());
            }

            @Override
            protected void createChildren() {
                super.createChildren();
                this.gold = new GoldIndicator();
                this.add(this.gold);
            }

            @Override
            protected void layout() {
                super.layout();
                this.gold.fill(this);
            }
        };
        this.add(this.btnInventory);
        this.btnQuick1 = new QuickSlotTool(110, 38, 20, 24, true);
        this.add(this.btnQuick1);
        this.btnQuick2 = new QuickSlotTool(130, 38, 20, 24, false);
        this.add(this.btnQuick2);
        this.tools = new Tool[]{this.btnWait, this.btnSearch, this.btnInfo, this.btnInventory, this.btnQuick1, this.btnQuick2};
        this.pickedUp = new PickedUpItem();
        this.add(this.pickedUp);
        this.exp = new Image("exp_bar.png");
        this.exp.scale.y = 2.0f;
        this.add(this.exp);
    }

    @Override
    protected void layout() {
        this.skin.x = this.x;
        this.skin.y = this.y - this.skin.height();
        for (Tool tool : this.tools) {
            tool.place(this.skin.x, this.skin.y);
        }
        this.avatar.setPos(this.btnInfo.right(), this.y - this.avatar.height() - 2.0f);
        this.exp.x = this.x + 1.0f;
        this.exp.y = this.y - 2.0f;
    }

    @Override
    public void update() {
        super.update();
        this.exp.scale.x = (this.width - 2.0f) / this.exp.width * (float)Dungeon.hero.exp / (float)Dungeon.hero.maxExp();
        if (this.lastEnabled != Dungeon.hero.ready) {
            this.lastEnabled = Dungeon.hero.ready;
            for (Tool tool : this.tools) {
                tool.enable(this.lastEnabled);
            }
        }
        if (!Dungeon.hero.isAlive()) {
            this.btnInventory.enable(true);
        }
    }

    public void pickup(Item item) {
        this.pickedUp.reset(item, this.btnInventory.centerX(), this.btnInventory.centerY());
    }

    private static class PickedUpItem
    extends ItemSprite {
        private static final float DISTANCE = 16.0f;
        private static final float DURATION = 0.2f;
        private float dstX;
        private float dstY;
        private float left;

        public PickedUpItem() {
            this.originToCenter();
            this.visible = false;
            this.active = false;
        }

        public void reset(Item item, float dstX, float dstY) {
            this.view(item.image(), item.glowing());
            this.visible = true;
            this.active = true;
            this.dstX = dstX - 8.0f;
            this.dstY = dstY - 8.0f;
            this.left = 0.2f;
            this.x = this.dstX - 16.0f;
            this.y = this.dstY - 16.0f;
            this.alpha(1.0f);
        }

        @Override
        public void update() {
            super.update();
            if ((this.left -= Game.elapsed) <= 0.0f) {
                this.active = false;
                this.visible = false;
            } else {
                float p = this.left / 0.2f;
                this.scale.set((float)Math.sqrt(p));
                float offset = 16.0f * p;
                this.x = this.dstX - offset;
                this.y = this.dstY - offset;
            }
        }
    }

    private static class CharacterShield
    extends Button {
        private static final int BUST_WIDTH = 26;
        private static final int BUST_HEIGHT = 26;
        private BitmapText level;
        private Image bust;
        private Image hp;
        private BitmaskEmitter blood;
        private int lastLvl = -1;
        private int lastArmor = 0;

        public CharacterShield() {
            this.hotKey = GameAction.HERO_INFO;
            this.setSize(30.0f, 38.0f);
        }

        @Override
        protected void createChildren() {
            super.createChildren();
            this.bust = new Image("busts.png");
            this.updateBust();
            this.add(this.bust);
            this.blood = new BitmaskEmitter(this.bust);
            this.blood.pour(BloodParticle.FACTORY, 0.3f);
            this.blood.autoKill = false;
            this.blood.on = false;
            this.add(this.blood);
            this.level = new BitmapText(PixelScene.font1x);
            this.level.hardlight(16772004);
            this.add(this.level);
            this.hp = new Image("hp_bar.png");
            this.add(this.hp);
        }

        @Override
        protected void layout() {
            super.layout();
            this.hp.x = this.x + 2.0f;
            this.hp.y = this.y + 30.0f;
            this.bust.x = this.x + 2.0f;
            this.bust.y = this.y + 2.0f;
        }

        @Override
        public void update() {
            super.update();
            float health = (float)Dungeon.hero.HP / (float)Dungeon.hero.HT;
            this.hp.scale.x = 26.0f * health;
            if (health == 0.0f) {
                this.bust.tint(0, 0.6f);
                this.blood.on = false;
            } else if (health < 0.25f) {
                this.bust.tint(0xCC0000, 0.4f);
                this.blood.on = true;
            } else {
                this.bust.resetColor();
                this.blood.on = false;
            }
            if (Dungeon.hero.lvl != this.lastLvl) {
                if (this.lastLvl != -1) {
                    Emitter emitter = (Emitter)this.recycle(Emitter.class);
                    emitter.revive();
                    emitter.pos(this.x + 27.5f, this.y + 2.5f);
                    emitter.burst(Speck.factory(1), 20);
                }
                this.lastLvl = Dungeon.hero.lvl;
                this.level.text(Integer.toString(this.lastLvl));
                this.level.measure();
                this.level.x = PixelScene.align(this.x + 27.5f - this.level.width() / 2.0f);
                this.level.y = PixelScene.align(this.y - 1.0f);
            }
            if (Dungeon.hero.tier() != this.lastArmor) {
                this.lastArmor = Dungeon.hero.tier();
                this.blood.target(this.bust);
                this.updateBust();
            }
        }

        private void updateBust() {
            this.bust.frame(Dungeon.hero.heroClass.ordinal() * 26, this.lastArmor * 26, 26, 26);
        }

        @Override
        protected void onClick() {
            CharSprite sprite = Dungeon.hero.sprite;
            if (!sprite.isVisible()) {
                Camera.main.focusOn(sprite);
            } else {
                GameScene.show(new WndHero());
            }
        }

        @Override
        protected String getTooltip() {
            return "(Lt)" + super.getTooltip();
        }
    }

    private class QuickSlotTool
    extends Tool {
        private QuickSlot slot;

        public QuickSlotTool(int x, int y, int width, int height, boolean primary) {
            super(x, y, width, height, primary ? GameAction.QUICKSLOT_PRIMARY : GameAction.QUICKSLOT_SECONDARY);
            if (primary) {
                this.slot.primary();
            } else {
                this.slot.secondary();
            }
        }

        @Override
        protected void createChildren() {
            super.createChildren();
            this.slot = new QuickSlot();
            this.add(this.slot);
        }

        @Override
        protected void layout() {
            super.layout();
            this.slot.setRect(this.x, this.y, this.width, this.height);
        }

        @Override
        public void enable(boolean value) {
            this.slot.enable(value);
            super.enable(value);
        }
    }

    private class Tool
    extends Button {
        private static final int BGCOLOR = 8093811;
        private int txX;
        private int txY;
        private Image base;

        public Tool(int x, int y, int width, int height, GameAction hotKey) {
            this.txX = x;
            this.txY = y;
            this.base.frame(x, y, width, height);
            this.width = width;
            this.height = height;
            this.hotKey = hotKey;
        }

        @Override
        protected void createChildren() {
            super.createChildren();
            this.base = new Image(Toolbar.this.skin);
            this.add(this.base);
        }

        @Override
        protected void layout() {
            super.layout();
            this.base.x = this.x;
            this.base.y = this.y;
        }

        public void place(float x, float y) {
            this.setPos(x + (float)this.txX, y + (float)this.txY);
        }

        @Override
        protected void onTouchDown() {
            this.base.brightness(1.4f);
        }

        @Override
        protected void onTouchUp() {
            if (this.active) {
                this.base.resetColor();
            } else {
                this.base.tint(8093811, 0.7f);
            }
        }

        public void enable(boolean value) {
            if (value != this.active) {
                if (value) {
                    this.base.resetColor();
                } else {
                    this.base.tint(8093811, 0.7f);
                }
                this.active = value;
            }
        }
    }
}

