/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.scenes;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Tooltip;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.Rankings;
import com.watabou.pixeldungeon.effects.Flare;
import com.watabou.pixeldungeon.input.GameAction;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.scenes.TitleScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.ui.Archs;
import com.watabou.pixeldungeon.ui.ExitButton;
import com.watabou.pixeldungeon.ui.Icons;
import com.watabou.pixeldungeon.windows.WndError;
import com.watabou.pixeldungeon.windows.WndRanking;

public class RankingsScene
extends PixelScene {
    private static final int DEFAULT_COLOR = 0xCCCCCC;
    private static final String TXT_TITLE = "英雄榜";
    private static final String TXT_TOTAL = "总游戏次数：";
    private static final String TXT_NO_GAMES = "目前为止还没有已经结束的游戏。";
    private static final String TXT_TOTAL_TOOLTIP = "(cb)_通关次数_ / 总游戏次数";
    private static final String TXT_NO_INFO = "没有额外信息";
    private static final float ROW_HEIGHT_L = 22.0f;
    private static final float MAX_ROW_WIDTH = 180.0f;
    private static final float GAP = 4.0f;

    @Override
    public void create() {
        super.create();
        Music.INSTANCE.play("theme.mp3", true);
        Music.INSTANCE.volume(1.0f);
        int w = Camera.main.width;
        int h = Camera.main.height;
        Archs archs = new Archs();
        archs.setSize(w, h);
        this.add(archs);
        Rankings.INSTANCE.load();
        if (Rankings.INSTANCE.records.size() > 0) {
            float rowHeight = 22.0f;
            float left = ((float)w - Math.min(180.0f, (float)w)) / 2.0f + 4.0f;
            float top = RankingsScene.align(((float)h - rowHeight * (float)Rankings.INSTANCE.records.size()) / 2.0f);
            BitmapText title = PixelScene.createText(TXT_TITLE, 9.0f);
            title.hardlight(0xFFFF44);
            title.measure();
            title.x = RankingsScene.align(((float)w - title.width()) / 2.0f);
            title.y = RankingsScene.align(top - title.height() - 4.0f);
            this.add(title);
            int pos = 0;
            for (Rankings.Record rec : Rankings.INSTANCE.records) {
                Record row = new Record(pos, pos == Rankings.INSTANCE.lastRecord, rec);
                row.setRect(left, top + (float)pos * rowHeight, (float)w - left * 2.0f, rowHeight);
                this.add(row);
                ++pos;
            }
            if (Rankings.INSTANCE.totalNumber >= 6) {
                BitmapText label = PixelScene.createText(TXT_TOTAL, 8.0f);
                label.hardlight(0xCCCCCC);
                label.measure();
                this.add(label);
                BitmapText won = PixelScene.createText(Integer.toString(Rankings.INSTANCE.wonNumber), 8.0f);
                won.hardlight(0xFFFF44);
                won.measure();
                this.add(won);
                BitmapText total = PixelScene.createText("/" + Rankings.INSTANCE.totalNumber, 8.0f);
                total.hardlight(0xCCCCCC);
                total.measure();
                total.x = RankingsScene.align(((float)w - total.width()) / 2.0f);
                total.y = RankingsScene.align(top + (float)pos * rowHeight + 4.0f);
                this.add(total);
                float tw = label.width() + won.width() + total.width();
                label.x = RankingsScene.align(((float)w - tw) / 2.0f);
                won.x = label.x + label.width();
                total.x = won.x + won.width();
                won.y = total.y = RankingsScene.align(top + (float)pos * rowHeight + 4.0f);
                label.y = total.y;
                TouchArea tooltip = new TouchArea(label.x, label.y, total.x + total.width() - label.x, label.height()){

                    @Override
                    public boolean onMouseMove(int x, int y) {
                        Tooltip.show(this, RankingsScene.TXT_TOTAL_TOOLTIP);
                        return true;
                    }
                };
                this.add(tooltip);
            }
        } else {
            BitmapText title = PixelScene.createText(TXT_NO_GAMES, 8.0f);
            title.hardlight(0xCCCCCC);
            title.measure();
            title.x = RankingsScene.align(((float)w - title.width()) / 2.0f);
            title.y = RankingsScene.align(((float)h - title.height()) / 2.0f);
            this.add(title);
        }
        ExitButton btnExit = new ExitButton();
        btnExit.setPos((float)Camera.main.width - btnExit.width(), 0.0f);
        this.add(btnExit);
        this.fadeIn();
    }

    @Override
    protected void onBackPressed() {
        PixelDungeon.switchNoFade(TitleScene.class);
    }

    public static class Record
    extends Button<GameAction> {
        private static final float GAP = 4.0f;
        private static final int TEXT_WIN = 0xFFFF88;
        private static final int TEXT_LOSE = 0xCCCCCC;
        private static final int FLARE_WIN = 0x888866;
        private static final int FLARE_LOSE = 0x666666;
        private Rankings.Record rec;
        private ItemSprite shield;
        private Flare flare;
        private BitmapText position;
        private BitmapTextMultiline desc;
        private Image classIcon;

        public Record(int pos, boolean latest, Rankings.Record rec) {
            this.rec = rec;
            if (latest) {
                this.flare = new Flare(6, 24.0f);
                this.flare.angularSpeed = 90.0f;
                this.flare.color(rec.win ? 0x888866 : 0x666666);
                this.addToBack(this.flare);
            }
            this.position.text(Integer.toString(pos + 1));
            this.position.measure();
            this.desc.text(rec.info);
            this.desc.measure();
            if (rec.win) {
                this.shield.view(87, null);
                this.position.hardlight(0xFFFF88);
                this.desc.hardlight(0xFFFF88);
            } else {
                this.position.hardlight(0xCCCCCC);
                this.desc.hardlight(0xCCCCCC);
            }
            this.classIcon.copy(Icons.get(rec.heroClass));
        }

        @Override
        protected void createChildren() {
            super.createChildren();
            this.shield = new ItemSprite(13, null);
            this.add(this.shield);
            this.position = new BitmapText(PixelScene.font1x);
            this.add(this.position);
            this.desc = PixelScene.createMultiline(9.0f);
            this.add(this.desc);
            this.classIcon = new Image();
            this.add(this.classIcon);
        }

        @Override
        protected void layout() {
            super.layout();
            this.shield.x = this.x;
            this.shield.y = this.y + (this.height - this.shield.height) / 2.0f;
            this.position.x = PixelScene.align(this.shield.x + (this.shield.width - this.position.width()) / 2.0f);
            this.position.y = PixelScene.align(this.shield.y + (this.shield.height - this.position.height()) / 2.0f + 1.0f);
            if (this.flare != null) {
                this.flare.point(this.shield.center());
            }
            this.classIcon.x = PixelScene.align(this.x + this.width - this.classIcon.width);
            this.classIcon.y = this.shield.y;
            this.desc.x = this.shield.x + this.shield.width + 4.0f;
            this.desc.maxWidth = (int)(this.classIcon.x - this.desc.x);
            this.desc.measure();
            this.desc.y = this.position.y + this.position.baseLine() - this.desc.baseLine();
        }

        @Override
        protected void onClick() {
            if (this.rec.gameFile.length() > 0) {
                this.parent.add(new WndRanking(this.rec.gameFile));
            } else {
                this.parent.add(new WndError(RankingsScene.TXT_NO_INFO));
            }
        }
    }
}

