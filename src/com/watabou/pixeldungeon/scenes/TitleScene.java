package com.watabou.pixeldungeon.scenes;

import com.badlogic.gdx.Gdx;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.effects.BannerSprites;
import com.watabou.pixeldungeon.effects.Fireball;
import com.watabou.pixeldungeon.input.GameAction;
import com.watabou.pixeldungeon.ui.Archs;
import com.watabou.pixeldungeon.ui.ExitButton;
import com.watabou.pixeldungeon.ui.PrefsButton;

public class TitleScene extends PixelScene {
    private static final String TXT_PLAY = "开始游戏";
    private static final String TXT_HIGHSCORES = "排行榜";
    private static final String TXT_BADGES = "徽章";
    private static final String TXT_ABOUT = "关于";
    private static final int MARGIN_VERSION = 3;

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
        Image title = BannerSprites.get(BannerSprites.Type.PIXEL_DUNGEON);
        this.add(title);
        float height = title.height + 48.0f;
        title.x = ((float) w - title.width()) / 2.0f;
        title.y = ((float) h - height) / 2.0f;
        this.placeTorch(title.x + 18.0f, title.y + 20.0f);
        this.placeTorch(title.x + title.width - 18.0f, title.y + 20.0f);
        Image signs = new Image(BannerSprites.get(BannerSprites.Type.PIXEL_DUNGEON_SIGNS)) {
            private float time = 0.0f;

            @Override
            public void update() {
                super.update();
                this.am = (float) Math.sin(-(this.time += Game.elapsed));
            }

            @Override
            public void draw() {
                Gdx.gl.glBlendFunc(770, 1);
                super.draw();
                Gdx.gl.glBlendFunc(770, 771);
            }
        };
        signs.x = title.x;
        signs.y = title.y;
        this.add(signs);
        DashboardItem btnBadges = new DashboardItem(TXT_BADGES, 3) {
            @Override
            protected void onClick() {
                PixelDungeon.switchNoFade(BadgesScene.class);
            }
        };
        this.add(btnBadges);
        DashboardItem btnAbout = new DashboardItem(TXT_ABOUT, 1) {
            @Override
            protected void onClick() {
                PixelDungeon.switchNoFade(AboutScene.class);
            }
        };
        this.add(btnAbout);
        DashboardItem btnPlay = new DashboardItem(TXT_PLAY, 0) {
            @Override
            protected void onClick() {
                PixelDungeon.switchNoFade(StartScene.class);
            }
        };
        this.add(btnPlay);
        DashboardItem btnHighscores = new DashboardItem(TXT_HIGHSCORES, 2) {
            @Override
            protected void onClick() {
                PixelDungeon.switchNoFade(RankingsScene.class);
            }
        };
        this.add(btnHighscores);
        float y = ((float) h + height) / 2.0f - 48.0f;
        btnHighscores.setPos((float) (w / 2) - btnHighscores.width(), y);
        btnBadges.setPos(w / 2, y);
        btnPlay.setPos(btnHighscores.left() - btnPlay.width(), y);
        btnAbout.setPos(btnBadges.right(), y);
        BitmapText version = new BitmapText("v " + Game.version + " 汉化版", font1x);
        version.measure();
        version.hardlight(0x888888);
        version.x = (float) w - version.width() - 3.0f;
        version.y = (float) h - version.height() - 3.0f;
        this.add(version);
        PrefsButton btnPrefs = new PrefsButton();
        btnPrefs.setPos(0.0f, 0.0f);
        this.add(btnPrefs);
        ExitButton btnExit = new ExitButton();
        btnExit.setPos((float) w - btnExit.width(), 0.0f);
        this.add(btnExit);
        this.fadeIn();
    }

    private void placeTorch(float x, float y) {
        Fireball fb = new Fireball();
        fb.setPos(x, y);
        this.add(fb);
    }

    private static class DashboardItem extends Button<GameAction> {
        public static final float SIZE = 48.0f;
        private static final int IMAGE_SIZE = 32;
        private Image image;
        private BitmapText label;

        public DashboardItem(String text, int index) {
            this.image.frame(this.image.texture.uvRect(index * 32, 0, (index + 1) * 32, 32));
            this.label.text(text);
            this.label.measure();
            this.setSize(48.0f, 48.0f);
        }

        @Override
        protected void createChildren() {
            super.createChildren();
            this.image = new Image("dashboard.png");
            this.add(this.image);
            this.label = PixelScene.createText(9.0f);
            this.add(this.label);
        }

        @Override
        protected void layout() {
            super.layout();
            this.image.x = PixelScene.align(this.x + (this.width - this.image.width()) / 2.0f);
            this.image.y = PixelScene.align(this.y);
            this.label.x = PixelScene.align(this.x + (this.width - this.label.width()) / 2.0f);
            this.label.y = PixelScene.align(this.image.y + this.image.height() + 2.0f);
        }

        @Override
        protected void onTouchDown() {
            this.image.brightness(1.5f);
            Sample.INSTANCE.play("snd_click.mp3", 1.0f, 0.8f, 0.0f);
        }

        @Override
        protected void onTouchUp() {
            this.image.resetColor();
        }
    }
}
