/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.Rankings;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Ankh;
import com.watabou.pixeldungeon.scenes.InterlevelScene;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.ui.RedButton;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.windows.IconTitle;

public class WndResurrect
extends Window {
    private static final String TXT_MESSAGE = "你已经阵亡，但你获得了再一次征服地牢的机会。你愿意接受吗？";
    private static final String TXT_YES = "是的，我要继续战斗！";
    private static final String TXT_NO = "不了，我选择放弃";
    private static final int WIDTH = 120;
    private static final int BTN_HEIGHT = 20;
    private static final float GAP = 2.0f;
    public static WndResurrect instance;
    public static Object causeOfDeath;

    public WndResurrect(Ankh ankh, Object causeOfDeath) {
        instance = this;
        WndResurrect.causeOfDeath = causeOfDeath;
        IconTitle titlebar = new IconTitle();
        titlebar.icon(new ItemSprite(ankh.image(), null));
        titlebar.label(ankh.name());
        titlebar.setRect(0.0f, 0.0f, 120.0f, 0.0f);
        this.add(titlebar);
        BitmapTextMultiline message = PixelScene.createMultiline(TXT_MESSAGE, 6.0f);
        message.maxWidth = 120;
        message.measure();
        message.y = titlebar.bottom() + 2.0f;
        this.add(message);
        RedButton btnYes = new RedButton(TXT_YES){

            @Override
            protected void onClick() {
                WndResurrect.this.hide();
                Statistics.add(Statistics.Type.ANKHS, 1);
                InterlevelScene.mode = InterlevelScene.Mode.RESURRECT;
                Game.switchScene(InterlevelScene.class);
            }
        };
        btnYes.setRect(0.0f, message.y + message.height() + 2.0f, 120.0f, 20.0f);
        this.add(btnYes);
        RedButton btnNo = new RedButton(TXT_NO){

            @Override
            protected void onClick() {
                WndResurrect.this.hide();
                Rankings.INSTANCE.submit(false);
                Hero.reallyDie(causeOfDeath);
            }
        };
        btnNo.setRect(0.0f, btnYes.bottom() + 2.0f, 120.0f, 20.0f);
        this.add(btnNo);
        this.resize(120, (int)btnNo.bottom());
    }

    @Override
    public void destroy() {
        super.destroy();
        instance = null;
    }

    @Override
    public void onBackPressed() {
    }
}

