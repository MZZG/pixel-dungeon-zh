/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.ui;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.input.GameAction;
import com.watabou.pixeldungeon.items.keys.IronKey;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.BuffIndicator;
import com.watabou.pixeldungeon.ui.Button;
import com.watabou.pixeldungeon.ui.DangerIndicator;
import com.watabou.pixeldungeon.ui.LootIndicator;
import com.watabou.pixeldungeon.ui.ResumeButton;
import com.watabou.pixeldungeon.windows.WndCatalogus;
import com.watabou.pixeldungeon.windows.WndGame;
import com.watabou.pixeldungeon.windows.WndJournal;
import com.watabou.pixeldungeon.windows.WndSettings;

public class StatusPane
extends Component {
    private Image image;
    private int lastKeys = -1;
    private BitmapText depth;
    private BitmapText keys;
    private DangerIndicator danger;
    private LootIndicator loot;
    private ResumeButton resume;
    private BuffIndicator buffs;
    private TopRightButton btnMenu;
    private TopRightButton btnCatalogus;
    private TopRightButton btnJournal;
    private boolean tagDanger = false;
    private boolean tagLoot = false;
    private boolean tagResume = false;

    @Override
    protected void createChildren() {
        this.image = new Image("status_pane.png");
        this.add(this.image);
        this.btnMenu = new TopRightButton(48, 2, 14, 11){

            @Override
            protected void onClick() {
                GameScene.show(new WndGame());
            }

            @Override
            protected boolean onLongClick() {
                GameScene.show(new WndSettings(true));
                return true;
            }

            @Override
            protected String getTooltip() {
                return "(Lc)菜单 _(Esc)_";
            }
        };
        this.add(this.btnMenu);
        this.btnCatalogus = new TopRightButton(48, 20, 14, 11){
            {
                this.hotKey = GameAction.CATALOGUS;
            }

            @Override
            protected void onClick() {
                GameScene.show(new WndCatalogus());
            }
        };
        this.add(this.btnCatalogus);
        this.btnJournal = new TopRightButton(48, 38, 14, 11){
            {
                this.hotKey = GameAction.JOURNAL;
            }

            @Override
            protected void onClick() {
                GameScene.show(new WndJournal());
            }
        };
        this.add(this.btnJournal);
        this.depth = new BitmapText(Integer.toString(Dungeon.depth), PixelScene.font1x);
        this.depth.hardlight(13291458);
        this.depth.measure();
        this.add(this.depth);
        Dungeon.hero.belongings.countIronKeys();
        this.keys = new BitmapText(PixelScene.font1x);
        this.keys.hardlight(13291458);
        this.add(this.keys);
        this.danger = new DangerIndicator();
        this.add(this.danger);
        this.loot = new LootIndicator();
        this.add(this.loot);
        this.resume = new ResumeButton();
        this.add(this.resume);
        this.buffs = new BuffIndicator(Dungeon.hero, true);
        this.add(this.buffs);
    }

    @Override
    protected void layout() {
        this.height = 32.0f;
        this.image.x = this.width - this.image.width;
        this.depth.x = this.width - 23.0f - this.depth.width() - 20.0f;
        this.depth.y = 4.0f;
        this.keys.y = 4.0f;
        this.layoutTags();
        this.buffs.setPos(2.0f, 2.0f);
        this.btnMenu.setPos(this.width - this.btnMenu.width(), 0.0f);
        this.btnCatalogus.setPos(this.width - this.btnCatalogus.width(), this.btnMenu.bottom() + 3.0f);
        this.btnJournal.setPos(this.width - this.btnJournal.width(), this.btnCatalogus.bottom() + 3.0f);
    }

    private void layoutTags() {
        float pos = 54.0f;
        if (this.tagDanger) {
            this.danger.setPos(this.width - this.danger.width(), pos);
            pos = this.danger.bottom() + 1.0f;
        }
        if (this.tagLoot) {
            this.loot.setPos(this.width - this.loot.width(), pos);
            pos = this.loot.bottom() + 1.0f;
        }
        if (this.tagResume) {
            this.resume.setPos(this.width - this.resume.width(), pos);
        }
    }

    @Override
    public void update() {
        int k;
        super.update();
        if (this.tagDanger != this.danger.visible || this.tagLoot != this.loot.visible || this.tagResume != this.resume.visible) {
            this.tagDanger = this.danger.visible;
            this.tagLoot = this.loot.visible;
            this.tagResume = this.resume.visible;
            this.layoutTags();
        }
        if ((k = IronKey.curDepthQuantity) != this.lastKeys) {
            this.lastKeys = k;
            this.keys.text(Integer.toString(this.lastKeys));
            this.keys.measure();
            this.keys.x = this.width - 8.0f - this.keys.width() - 20.0f;
        }
    }

    private static class TopRightButton
    extends Button {
        private Image image;

        public TopRightButton(int x, int y, int w, int h) {
            this.image = new Image("status_pane.png", x, y, w, h);
            this.add(this.image);
            this.width = this.image.width + 4.0f;
            this.height = this.image.height + 4.0f;
        }

        @Override
        protected void layout() {
            super.layout();
            this.image.x = this.x + 2.0f;
            this.image.y = this.y + 2.0f;
        }

        @Override
        protected void onTouchDown() {
            this.image.brightness(1.5f);
            Sample.INSTANCE.play("snd_click.mp3");
        }

        @Override
        protected void onTouchUp() {
            this.image.resetColor();
        }

        @Override
        protected String getTooltip() {
            return "(Lc)" + super.getTooltip();
        }
    }
}

