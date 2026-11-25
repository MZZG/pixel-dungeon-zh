/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.potions;

import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.utils.GLog;

public class PotionOfInvisibility
extends Potion {
    private static final float ALPHA = 0.4f;

    public PotionOfInvisibility() {
        this.name = "隐形药剂";
    }

    @Override
    protected void apply(Hero hero) {
        this.setKnown();
        Buff.affect(hero, Invisibility.class, 15.0f);
        GLog.i("你看到自己的双手变得透明了！", new Object[0]);
        Sample.INSTANCE.play("snd_meld.mp3");
    }

    @Override
    public String desc() {
        return "饮用此药剂可使你暂时隐形。隐形期间敌人将无法察觉你，但在敌人眼前发动攻击、使用法杖或卷轴都会解除隐形效果。";
    }

    @Override
    public int price() {
        return this.isKnown() ? 40 * this.quantity : super.price();
    }

    public static void melt(Char ch) {
        if (ch.sprite.parent != null) {
            ch.sprite.parent.add(new AlphaTweener(ch.sprite, 0.4f, 0.4f));
        } else {
            ch.sprite.alpha(0.4f);
        }
    }
}

