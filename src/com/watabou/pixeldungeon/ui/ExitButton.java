/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.ui;

import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.scenes.TitleScene;
import com.watabou.pixeldungeon.ui.Button;
import com.watabou.pixeldungeon.ui.Icons;

public class ExitButton
extends Button {
    private Image image;

    public ExitButton() {
        this.width = this.image.width;
        this.height = this.image.height;
    }

    @Override
    protected void createChildren() {
        super.createChildren();
        this.image = Icons.EXIT.get();
        this.add(this.image);
    }

    @Override
    protected void layout() {
        super.layout();
        this.image.x = this.x;
        this.image.y = this.y;
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
    protected void onClick() {
        if (Game.scene() instanceof TitleScene) {
            Game.instance.finish();
        } else {
            PixelDungeon.switchNoFade(TitleScene.class);
        }
    }

    @Override
    protected String getTooltip() {
        return Game.scene() instanceof TitleScene ? "退出 _(Esc)_" : "返回 _(Esc)_";
    }
}

