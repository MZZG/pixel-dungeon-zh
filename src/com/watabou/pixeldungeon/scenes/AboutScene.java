/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.scenes;

import com.badlogic.gdx.Gdx;
import com.watabou.input.NoosaInputProcessor;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.Visual;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.effects.Flare;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.scenes.TitleScene;
import com.watabou.pixeldungeon.ui.Archs;
import com.watabou.pixeldungeon.ui.ExitButton;
import com.watabou.pixeldungeon.ui.Icons;

public class AboutScene
extends PixelScene {
    private static final String TXT = "原作程序与美术：Watabou\nLibGDX 移植：Arcnor\nSteam 版：Watabou & Arcnor\n音乐：Cube_Code\n\n本作灵感源自 Brian Walker 的 Brogue。强烈推荐在 Windows、Mac OS 或 Linux 上试玩！;)\n\n基于 libGDX 与 LWJGL 开发。\n\n更多信息请访问官方网站：";
    private static final String LNK = "pixeldungeon.watabou.ru";

    @Override
    public void create() {
        super.create();
        BitmapTextMultiline text = AboutScene.createMultiline(TXT, 8.0f);
        text.maxWidth = Math.min(Camera.main.width, 120);
        text.measure();
        this.add(text);
        text.x = AboutScene.align(((float)Camera.main.width - text.width()) / 2.0f);
        text.y = AboutScene.align(((float)Camera.main.height - text.height()) / 2.0f);
        BitmapTextMultiline link = AboutScene.createMultiline(LNK, 8.0f);
        link.maxWidth = Math.min(Camera.main.width, 120);
        link.measure();
        link.hardlight(0xFFFF44);
        this.add(link);
        link.x = text.x;
        link.y = text.y + text.height();
        TouchArea hotArea = new TouchArea(link){

            @Override
            protected void onClick(NoosaInputProcessor.Touch touch) {
                Gdx.net.openURI("http://pixeldungeon.watabou.ru");
            }
        };
        this.add(hotArea);
        Image wata = Icons.WATA.get();
        wata.x = AboutScene.align(((float)Camera.main.width - wata.width) / 2.0f);
        wata.y = text.y - wata.height - 8.0f;
        this.add(wata);
        new Flare((int)7, (float)64.0f).color((int)0x112233, (boolean)true).show((Visual)wata, (float)0.0f).angularSpeed = 20.0f;
        Archs archs = new Archs();
        archs.setSize(Camera.main.width, Camera.main.height);
        this.addToBack(archs);
        ExitButton btnExit = new ExitButton();
        btnExit.setPos((float)Camera.main.width - btnExit.width(), 0.0f);
        this.add(btnExit);
        this.fadeIn();
    }

    @Override
    protected void onBackPressed() {
        PixelDungeon.switchNoFade(TitleScene.class);
    }
}

