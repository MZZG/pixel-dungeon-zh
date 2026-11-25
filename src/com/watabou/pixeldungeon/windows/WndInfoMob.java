/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ui.Component;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.ui.BuffIndicator;
import com.watabou.pixeldungeon.ui.HealthBar;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndTitledMessage;

public class WndInfoMob
extends WndTitledMessage {
    public WndInfoMob(Mob mob) {
        super(new MobTitle(mob), WndInfoMob.desc(mob));
    }

    private static String desc(Mob mob) {
        StringBuilder builder = new StringBuilder(mob.description());
        builder.append("\n\n" + mob.state.status() + "。");
        return builder.toString();
    }

    private static class MobTitle
    extends Component {
        private static final int GAP = 2;
        private CharSprite image;
        private BitmapText name;
        private HealthBar health;
        private BuffIndicator buffs;

        public MobTitle(Mob mob) {
            this.name = PixelScene.createText(Utils.capitalize(mob.name), 9.0f);
            this.name.hardlight(0xFFFF44);
            this.name.measure();
            this.add(this.name);
            this.image = mob.sprite();
            this.add(this.image);
            this.health = new HealthBar();
            this.health.level((float)mob.HP / (float)mob.HT);
            this.add(this.health);
            this.buffs = new BuffIndicator(mob, false);
            this.add(this.buffs);
        }

        @Override
        protected void layout() {
            this.image.x = 0.0f;
            this.image.y = Math.max(0.0f, this.name.height() + 2.0f + this.health.height() - this.image.height);
            this.name.x = this.image.width + 2.0f;
            this.name.y = this.image.height - this.health.height() - 2.0f - this.name.baseLine();
            float w = this.width - this.image.width - 2.0f;
            this.health.setRect(this.image.width + 2.0f, this.image.height - this.health.height(), w, this.health.height());
            this.buffs.setPos(this.name.x + this.name.width() + 2.0f, this.name.y + this.name.baseLine() - this.buffs.height());
            this.height = this.health.bottom();
        }
    }
}

