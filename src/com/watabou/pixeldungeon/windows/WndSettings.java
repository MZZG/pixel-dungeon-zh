/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.CheckBox;
import com.watabou.pixeldungeon.ui.RedButton;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.windows.WndKeymap;

public class WndSettings
extends Window {
    private static final String TXT_ZOOM_IN = "+";
    private static final String TXT_ZOOM_OUT = "-";
    private static final String TXT_ZOOM_DEFAULT = "默认缩放";
    private static final String TXT_SCALE_UP = "放大界面";
    private static final String TXT_MUSIC = "背景音乐";
    private static final String TXT_SOUND = "游戏音效";
    private static final String TXT_BINDINGS = "键鼠键位";
    private static final String TXT_SWITCH_FULL = "切换为全屏模式";
    private static final String TXT_SWITCH_WIN = "切换为窗口模式";
    private static final int WIDTH = 112;
    private static final int BTN_HEIGHT = 20;
    private static final int GAP = 2;
    private RedButton btnZoomOut;
    private RedButton btnZoomIn;

    public WndSettings(boolean inGame) {
        Component btnImmersive = null;
        if (inGame) {
            int w = 20;
            this.btnZoomOut = new RedButton(TXT_ZOOM_OUT){

                @Override
                protected void onClick() {
                    WndSettings.this.zoom(Camera.main.zoom - 1.0f);
                }
            };
            this.add(this.btnZoomOut.setRect(0.0f, 0.0f, w, 20.0f));
            this.btnZoomIn = new RedButton(TXT_ZOOM_IN){

                @Override
                protected void onClick() {
                    WndSettings.this.zoom(Camera.main.zoom + 1.0f);
                }
            };
            this.add(this.btnZoomIn.setRect(112 - w, 0.0f, w, 20.0f));
            this.add(new RedButton(TXT_ZOOM_DEFAULT){

                @Override
                protected void onClick() {
                    WndSettings.this.zoom(PixelScene.defaultZoom);
                }
            }.setRect(this.btnZoomOut.right(), 0.0f, 112.0f - this.btnZoomIn.width() - this.btnZoomOut.width(), 20.0f));
            this.updateEnabled();
        } else {
            CheckBox btnScaleUp = new CheckBox(TXT_SCALE_UP){

                @Override
                protected void onClick() {
                    super.onClick();
                    PixelDungeon.scaleUp(this.checked());
                }
            };
            btnScaleUp.setRect(0.0f, 0.0f, 112.0f, 20.0f);
            btnScaleUp.checked(PixelDungeon.scaleUp());
            this.add(btnScaleUp);
        }
        CheckBox btnMusic = new CheckBox(TXT_MUSIC){

            @Override
            protected void onClick() {
                super.onClick();
                PixelDungeon.music(this.checked());
            }
        };
        btnMusic.setRect(0.0f, (btnImmersive != null ? btnImmersive.bottom() : 20.0f) + 2.0f, 112.0f, 20.0f);
        btnMusic.checked(PixelDungeon.music());
        this.add(btnMusic);
        CheckBox btnSound = new CheckBox(TXT_SOUND){

            @Override
            protected void onClick() {
                super.onClick();
                PixelDungeon.soundFx(this.checked());
                Sample.INSTANCE.play("snd_click.mp3");
            }
        };
        btnSound.setRect(0.0f, btnMusic.bottom() + 2.0f, 112.0f, 20.0f);
        btnSound.checked(PixelDungeon.soundFx());
        this.add(btnSound);
        Application.ApplicationType type = Gdx.app.getType();
        RedButton lastBtn = btnSound;
        if (!inGame && type == Application.ApplicationType.Desktop) {
            RedButton btnResolution = new RedButton(this.resolutionText()){

                @Override
                protected void onClick() {
                    PixelDungeon.fullscreen(!PixelDungeon.fullscreen());
                }
            };
            btnResolution.enable(PixelDungeon.instance.getPlatformSupport().isFullscreenEnabled());
            btnResolution.setRect(0.0f, btnSound.bottom() + 2.0f, 112.0f, 20.0f);
            this.add(btnResolution);
            lastBtn = btnResolution;
        }
        if (type == Application.ApplicationType.Desktop) {
            RedButton btnKeymap = new RedButton(TXT_BINDINGS){

                @Override
                protected void onClick() {
                    this.parent.add(new WndKeymap());
                }
            };
            btnKeymap.setRect(0.0f, lastBtn.bottom() + 2.0f, 112.0f, 20.0f);
            this.add(btnKeymap);
            lastBtn = btnKeymap;
        }
        this.resize(112, (int)lastBtn.bottom());
    }

    private void zoom(float value) {
        Camera.main.zoom(value);
        PixelDungeon.zoom((int)(value - PixelScene.defaultZoom));
        this.updateEnabled();
    }

    private void updateEnabled() {
        float zoom = Camera.main.zoom;
        this.btnZoomIn.enable(zoom < PixelScene.maxZoom);
        this.btnZoomOut.enable(zoom > PixelScene.minZoom);
    }

    private String resolutionText() {
        return Gdx.graphics.isFullscreen() ? TXT_SWITCH_WIN : TXT_SWITCH_FULL;
    }
}

