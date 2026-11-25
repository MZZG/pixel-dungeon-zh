/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.IconTitle;

public class WndInfoItem
extends Window {
    private static final String TXT_CHEST = "宝箱";
    private static final String TXT_LOCKED_CHEST = "上锁的宝箱";
    private static final String TXT_CRYSTAL_CHEST = "水晶宝箱";
    private static final String TXT_TOMB = "坟墓";
    private static final String TXT_SKELETON = "遗骸";
    private static final String TXT_WONT_KNOW = "打开前你是看不见里面有什么的！";
    private static final String TXT_NEED_KEY = "打开前你是看不见里面有什么的！你需要一枚金钥匙才能打开它。";
    private static final String TXT_INSIDE = "你能看到里面装着%s，但需要金钥匙才能打开。";
    private static final String TXT_OWNER = "这座古墓里或许埋葬着有用的东西，但墓主显然不会同意你去翻动它。";
    private static final String TXT_REMAINS = "这是你某位前辈留下的全部遗骸，或许能从里面找到些值钱的东西。";
    private static final float GAP = 2.0f;
    private static final int WIDTH = 120;

    public WndInfoItem(Heap heap) {
        if (heap.type == Heap.Type.HEAP || heap.type == Heap.Type.FOR_SALE) {
            Item item = heap.peek();
            int color = 0xFFFF44;
            if (item.levelKnown) {
                if (item.level() < 0) {
                    color = 0xFF4444;
                } else if (item.level() > 0) {
                    color = item.isBroken() ? 0xFF8800 : 0x44FF44;
                }
            }
            this.fillFields(item.image(), item.glowing(), color, item.toString(), item.info());
        } else {
            String info;
            String title;
            if (heap.type == Heap.Type.CHEST || heap.type == Heap.Type.MIMIC) {
                title = TXT_CHEST;
                info = TXT_WONT_KNOW;
            } else if (heap.type == Heap.Type.TOMB) {
                title = TXT_TOMB;
                info = TXT_OWNER;
            } else if (heap.type == Heap.Type.SKELETON) {
                title = TXT_SKELETON;
                info = TXT_REMAINS;
            } else if (heap.type == Heap.Type.CRYSTAL_CHEST) {
                title = TXT_CRYSTAL_CHEST;
                info = Utils.format(TXT_INSIDE, Utils.indefinite(heap.peek().name()));
            } else {
                title = TXT_LOCKED_CHEST;
                info = TXT_NEED_KEY;
            }
            this.fillFields(heap.image(), heap.glowing(), 0xFFFF44, title, info);
        }
    }

    public WndInfoItem(Item item) {
        int color = 0xFFFF44;
        if (item.levelKnown) {
            if (item.level() < 0 || item.isBroken()) {
                color = 0xFF4444;
            } else if (item.level() > 0) {
                color = 0x44FF44;
            }
        }
        this.fillFields(item.image(), item.glowing(), color, item.toString(), item.info());
    }

    private void fillFields(int image, ItemSprite.Glowing glowing, int titleColor, String title, String info) {
        IconTitle titlebar = new IconTitle();
        titlebar.icon(new ItemSprite(image, glowing));
        titlebar.label(Utils.capitalize(title), titleColor);
        titlebar.setRect(0.0f, 0.0f, 120.0f, 0.0f);
        this.add(titlebar);
        BitmapTextMultiline txtInfo = PixelScene.createMultiline(info, 6.0f);
        txtInfo.maxWidth = 120;
        txtInfo.measure();
        txtInfo.x = titlebar.left();
        txtInfo.y = titlebar.bottom() + 2.0f;
        this.add(txtInfo);
        this.resize(120, (int)(txtInfo.y + txtInfo.height()));
    }
}

