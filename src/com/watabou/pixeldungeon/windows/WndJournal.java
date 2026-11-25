/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.input.NoosaInputProcessor;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.Journal;
import com.watabou.pixeldungeon.input.GameAction;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.Icons;
import com.watabou.pixeldungeon.ui.ScrollPane;
import com.watabou.pixeldungeon.ui.Window;
import java.util.ArrayList;
import java.util.Collections;

public class WndJournal
extends Window {
    private static final int WIDTH = 112;
    private static final int HEIGHT = 144;
    private static final int ITEM_HEIGHT = 18;
    private static final String TXT_TITLE = "日志";
    private static final String TXT_NO_RECORDS = "你的日志中还没有任何记录。";

    public WndJournal() {
        this.resize(112, 144);
        BitmapText txtTitle = PixelScene.createText(TXT_TITLE, 9.0f);
        txtTitle.hardlight(0xFFFF44);
        txtTitle.measure();
        txtTitle.x = PixelScene.align(this.camera, (112.0f - txtTitle.width()) / 2.0f);
        this.add(txtTitle);
        if (!Journal.records.isEmpty()) {
            Collections.sort(Journal.records);
            Component content = new Component();
            final ArrayList<ListItem> items = new ArrayList<ListItem>();
            float pos = 0.0f;
            for (Journal.Record rec : Journal.records) {
                ListItem item = new ListItem(rec);
                item.setRect(0.0f, pos, 112.0f, 18.0f);
                items.add(item);
                content.add(item);
                pos += item.height();
            }
            content.setSize(112.0f, pos);
            ScrollPane list = new ScrollPane(content){

                @Override
                public void onClick(float x, float y) {
                    int index = (int)(y / 18.0f);
                    if (index < items.size()) {
                        ((ListItem)items.get(index)).onClick();
                    }
                }
            };
            this.add(list);
            list.setRect(0.0f, txtTitle.height(), 112.0f, (float)this.height - txtTitle.height());
        } else {
            BitmapTextMultiline txtNoRecords = PixelScene.createMultiline(TXT_NO_RECORDS, 7.0f);
            txtNoRecords.maxWidth = 74;
            txtNoRecords.hardlight(13291458);
            txtNoRecords.measure();
            float pos = (144.0f - txtNoRecords.height()) / 2.0f;
            for (BitmapText line : txtNoRecords.new LineSplitter().split()) {
                line.measure();
                line.x = PixelScene.align((112.0f - line.width()) / 2.0f);
                line.y = PixelScene.align(pos);
                this.add(line);
                pos += line.height();
            }
        }
    }

    @Override
    protected void onKeyUp(NoosaInputProcessor.Key<GameAction> key) {
        if (key.action == GameAction.JOURNAL) {
            this.hide();
        } else {
            super.onKeyUp(key);
        }
    }

    private static class ListItem
    extends Component {
        private Journal.Record rec;
        private BitmapText feature;
        private BitmapText depth;
        private Image icon;

        public ListItem(Journal.Record rec) {
            this.rec = rec;
            this.feature.text(rec.feature.desc);
            this.feature.measure();
            this.depth.text(Integer.toString(rec.depth));
            this.depth.measure();
            if (rec.depth == Dungeon.depth) {
                this.feature.hardlight(0xFFFF44);
                this.depth.hardlight(0xFFFF44);
            }
        }

        @Override
        protected void createChildren() {
            this.feature = PixelScene.createText(9.0f);
            this.add(this.feature);
            this.depth = new BitmapText(PixelScene.font1x);
            this.add(this.depth);
            this.icon = Icons.get(Icons.DEPTH);
            this.add(this.icon);
        }

        @Override
        protected void layout() {
            this.icon.x = this.width - this.icon.width;
            this.depth.x = this.icon.x - 1.0f - this.depth.width();
            this.depth.y = PixelScene.align(this.y + (this.height - this.depth.height()) / 2.0f);
            this.icon.y = this.depth.y - 1.0f;
            this.feature.y = PixelScene.align(this.depth.y + this.depth.baseLine() - this.feature.baseLine());
        }

        public void onClick() {
            if (this.rec.depth == Dungeon.depth && this.rec.pos != -1) {
                DungeonTilemap.focusOn(this.rec.pos);
            }
        }
    }
}

