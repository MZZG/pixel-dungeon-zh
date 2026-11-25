/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.scenes;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.effects.BadgeBanner;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.scenes.TitleScene;
import com.watabou.pixeldungeon.ui.Archs;
import com.watabou.pixeldungeon.ui.Button;
import com.watabou.pixeldungeon.ui.ExitButton;
import com.watabou.pixeldungeon.windows.WndBadge;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import java.util.List;

public class BadgesScene
extends PixelScene {
    private static final String TXT_TITLE = "你的徽章";

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
        int pw = w - 32;
        int ph = h - 32;
        Badges.loadGlobal();
        int nCols = 8;
        int nRows = 4;
        float size = Math.min(pw / nCols, ph / nRows);
        float left = ((float)w - size * (float)nCols) / 2.0f;
        float top = ((float)h - size * (float)nRows) / 2.0f;
        List<Badges.Badge> badges = Badges.filtered(true);
        for (int i = 0; i < nRows; ++i) {
            for (int j = 0; j < nCols; ++j) {
                int index = i * nCols + j;
                Badges.Badge b = index < badges.size() ? badges.get(index) : null;
                BadgeButton button = new BadgeButton(b);
                button.setPos(left + (float)j * size + (size - button.width()) / 2.0f, top + (float)i * size + (size - button.height()) / 2.0f);
                this.add(button);
            }
        }
        BitmapText title = PixelScene.createText(TXT_TITLE, 9.0f);
        title.hardlight(0xFFFF44);
        title.measure();
        title.x = BadgesScene.align(((float)w - title.width()) / 2.0f);
        title.y = BadgesScene.align((top - title.baseLine()) / 2.0f);
        this.add(title);
        ExitButton btnExit = new ExitButton();
        btnExit.setPos((float)Camera.main.width - btnExit.width(), 0.0f);
        this.add(btnExit);
        this.fadeIn();
        Badges.loadingListener = new Callback(){

            @Override
            public void call() {
                if (Game.scene() == BadgesScene.this) {
                    PixelDungeon.switchNoFade(BadgesScene.class);
                }
            }
        };
    }

    @Override
    public void destroy() {
        Badges.saveGlobal();
        Badges.loadingListener = null;
        super.destroy();
    }

    @Override
    protected void onBackPressed() {
        PixelDungeon.switchNoFade(TitleScene.class);
    }

    private static class BadgeButton
    extends Button {
        private Badges.Badge badge;
        private Image icon;

        public BadgeButton(Badges.Badge badge) {
            this.badge = badge;
            this.active = badge != null;
            this.icon = BadgeBanner.image(badge != null ? badge.image : 56);
            this.icon.scale.set(2.0f);
            this.add(this.icon);
            this.setSize(this.icon.width(), this.icon.height());
        }

        @Override
        protected void layout() {
            super.layout();
            this.icon.x = PixelScene.align(this.x + (this.width - this.icon.width()) / 2.0f);
            this.icon.y = PixelScene.align(this.y + (this.height - this.icon.height()) / 2.0f);
        }

        @Override
        public void update() {
            super.update();
            if ((double)Random.Float() < (double)Game.elapsed * 0.1) {
                BadgeBanner.highlight(this.icon, this.badge.image);
            }
        }

        @Override
        protected void onClick() {
            Sample.INSTANCE.play("snd_click.mp3", 0.7f, 0.7f, 1.2f);
            Game.scene().add(new WndBadge(this.badge));
        }

        @Override
        protected String getTooltip() {
            return "(ca)" + this.badge.name;
        }
    }
}

