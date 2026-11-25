/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.ui;

import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.ui.Button;
import com.watabou.pixeldungeon.ui.Icons;
import com.watabou.pixeldungeon.windows.WndSettings;

public class PrefsButton
extends Button {
    private Image image;

    public PrefsButton() {
        this.width = this.image.width;
        this.height = this.image.height;
    }

    @Override
    protected void createChildren() {
        super.createChildren();
        this.image = Icons.PREFS.get();
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
        this.parent.add(new WndSettings(false));
    }

    @Override
    protected String getTooltip() {
        return "设置";
    }
}

