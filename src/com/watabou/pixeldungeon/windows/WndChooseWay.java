/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.items.TomeOfMastery;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.ui.HighlightedText;
import com.watabou.pixeldungeon.ui.RedButton;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.IconTitle;

public class WndChooseWay
extends Window {
    private static final int WIDTH = 120;
    private static final int BTN_HEIGHT = 18;
    private static final float GAP = 2.0f;

    public WndChooseWay(final TomeOfMastery tome, final HeroSubClass way1, final HeroSubClass way2) {
        String TXT_MASTERY = "你将追随哪一条道路？";
        String TXT_CANCEL = "我稍后再做决定";
        float bottom = this.createCommonStuff(tome, way1.desc() + "\n\n" + way2.desc() + "\n\n" + "你将追随哪一条道路？");
        RedButton btnWay1 = new RedButton(Utils.capitalize(way1.title())){

            @Override
            protected void onClick() {
                WndChooseWay.this.hide();
                tome.choose(way1);
            }
        };
        btnWay1.setRect(0.0f, bottom + 2.0f, 59.0f, 18.0f);
        this.add(btnWay1);
        RedButton btnWay2 = new RedButton(Utils.capitalize(way2.title())){

            @Override
            protected void onClick() {
                WndChooseWay.this.hide();
                tome.choose(way2);
            }
        };
        btnWay2.setRect(btnWay1.right() + 2.0f, btnWay1.top(), btnWay1.width(), 18.0f);
        this.add(btnWay2);
        RedButton btnCancel = new RedButton("我稍后再做决定"){

            @Override
            protected void onClick() {
                WndChooseWay.this.hide();
            }
        };
        btnCancel.setRect(0.0f, btnWay2.bottom() + 2.0f, 120.0f, 18.0f);
        this.add(btnCancel);
        this.resize(120, (int)btnCancel.bottom());
    }

    public WndChooseWay(final TomeOfMastery tome, final HeroSubClass way) {
        String TXT_REMASTERY = "你确定要转修为%s吗？";
        String TXT_OK = "是的，我要转修";
        String TXT_CANCEL = "以后再说";
        float bottom = this.createCommonStuff(tome, way.desc() + "\n\n" + Utils.format("你确定要转修为%s吗？", Utils.indefinite(way.title())));
        RedButton btnWay = new RedButton("是的，我要转修"){

            @Override
            protected void onClick() {
                WndChooseWay.this.hide();
                tome.choose(way);
            }
        };
        btnWay.setRect(0.0f, bottom + 2.0f, 120.0f, 18.0f);
        this.add(btnWay);
        RedButton btnCancel = new RedButton("以后再说"){

            @Override
            protected void onClick() {
                WndChooseWay.this.hide();
            }
        };
        btnCancel.setRect(0.0f, btnWay.bottom() + 2.0f, 120.0f, 18.0f);
        this.add(btnCancel);
        this.resize(120, (int)btnCancel.bottom());
    }

    private float createCommonStuff(TomeOfMastery tome, String text) {
        IconTitle titlebar = new IconTitle();
        titlebar.icon(new ItemSprite(tome.image(), null));
        titlebar.label(tome.name());
        titlebar.setRect(0.0f, 0.0f, 120.0f, 0.0f);
        this.add(titlebar);
        HighlightedText hl = new HighlightedText(6.0f);
        hl.text(text, 120);
        hl.setPos(titlebar.left(), titlebar.bottom() + 2.0f);
        this.add(hl);
        return hl.bottom();
    }
}

