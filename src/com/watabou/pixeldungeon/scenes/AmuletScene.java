/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.scenes;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.effects.Flare;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.scenes.InterlevelScene;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.scenes.RankingsScene;
import com.watabou.pixeldungeon.scenes.TitleScene;
import com.watabou.pixeldungeon.ui.RedButton;
import com.watabou.utils.Random;

public class AmuletScene
extends PixelScene {
    private static final String TXT_EXIT = "现在就到此为止吧";
    private static final String TXT_STAY = "这一切还不该结束";
    private static final int WIDTH = 120;
    private static final int BTN_HEIGHT = 18;
    private static final float SMALL_GAP = 2.0f;
    private static final float LARGE_GAP = 8.0f;
    private static final String TXT = "你终于将Yendor护符握在了手中。借助它的力量，你可以征服世界，或为世人带来和平与繁荣，随心所欲。无论如何，你的人生都将彻底改变，而这场冒险也将就此落幕。又或者，你也可以选择作为一介凡人再停留片刻。";
    public static boolean noText = false;
    private Image amulet;
    private float timer = 0.0f;

    @Override
    public void create() {
        super.create();
        BitmapTextMultiline text = null;
        if (!noText) {
            text = AmuletScene.createMultiline(TXT, 8.0f);
            text.maxWidth = 120;
            text.measure();
            this.add(text);
        }
        this.amulet = new Image("amulet.png");
        this.add(this.amulet);
        RedButton btnExit = new RedButton(TXT_EXIT){

            @Override
            protected void onClick() {
                Dungeon.win("获得Yendor护符");
                Dungeon.deleteGame(Dungeon.hero.heroClass, true);
                Game.switchScene(noText ? TitleScene.class : RankingsScene.class);
            }
        };
        btnExit.setSize(120.0f, 18.0f);
        this.add(btnExit);
        RedButton btnStay = new RedButton(TXT_STAY){

            @Override
            protected void onClick() {
                AmuletScene.this.onBackPressed();
            }
        };
        btnStay.setSize(120.0f, 18.0f);
        this.add(btnStay);
        if (noText) {
            float height = this.amulet.height + 8.0f + btnExit.height() + 2.0f + btnStay.height();
            this.amulet.x = AmuletScene.align(((float)Camera.main.width - this.amulet.width) / 2.0f);
            this.amulet.y = AmuletScene.align(((float)Camera.main.height - height) / 2.0f);
            btnExit.setPos(((float)Camera.main.width - btnExit.width()) / 2.0f, this.amulet.y + this.amulet.height + 8.0f);
            btnStay.setPos(btnExit.left(), btnExit.bottom() + 2.0f);
        } else {
            float height = this.amulet.height + 8.0f + text.height() + 8.0f + btnExit.height() + 2.0f + btnStay.height();
            this.amulet.x = AmuletScene.align(((float)Camera.main.width - this.amulet.width) / 2.0f);
            this.amulet.y = AmuletScene.align(((float)Camera.main.height - height) / 2.0f);
            text.x = AmuletScene.align(((float)Camera.main.width - text.width()) / 2.0f);
            text.y = this.amulet.y + this.amulet.height + 8.0f;
            btnExit.setPos(((float)Camera.main.width - btnExit.width()) / 2.0f, text.y + text.height() + 8.0f);
            btnStay.setPos(btnExit.left(), btnExit.bottom() + 2.0f);
        }
        new Flare((int)8, (float)48.0f).color((int)0xFFDDBB, (boolean)true).show((Visual)this.amulet, (float)0.0f).angularSpeed = 30.0f;
        this.fadeIn();
    }

    @Override
    protected void onBackPressed() {
        InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
        Game.switchScene(InterlevelScene.class);
    }

    @Override
    public void update() {
        super.update();
        if ((this.timer -= Game.elapsed) < 0.0f) {
            this.timer = Random.Float(0.5f, 5.0f);
            Speck star = (Speck)this.recycle(Speck.class);
            star.reset(0, this.amulet.x + 10.5f, this.amulet.y + 5.5f, 101);
            this.add(star);
        }
    }
}

