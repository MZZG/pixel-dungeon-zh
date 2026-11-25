/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.scrolls;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.items.wands.WandOfBlink;
import com.watabou.pixeldungeon.utils.GLog;

public class ScrollOfTeleportation
extends Scroll {
    public static final String TXT_TELEPORTED = "眨眼之间你就被传送到了本层的另一处位置。";
    public static final String TXT_NO_TELEPORT = "这里强大的魔力场阻止了你的传送！";

    public ScrollOfTeleportation() {
        this.name = "传送卷轴";
    }

    @Override
    protected void doRead() {
        Sample.INSTANCE.play("snd_read.mp3");
        Invisibility.dispel();
        ScrollOfTeleportation.teleportHero(curUser);
        this.setKnown();
        this.readAnimation();
    }

    public static void teleportHero(Hero hero) {
        int pos;
        int count = 10;
        do {
            pos = Dungeon.level.randomRespawnCell();
        } while (count-- > 0 && pos == -1);
        if (pos == -1) {
            GLog.w(TXT_NO_TELEPORT, new Object[0]);
        } else {
            WandOfBlink.appear(hero, pos);
            Dungeon.level.press(pos, hero);
            Dungeon.observe();
            GLog.i(TXT_TELEPORTED, new Object[0]);
        }
    }

    @Override
    public String desc() {
        return "羊皮纸上的法术能瞬间将你传送到当前楼层的任意随机地点。虽然可以用来脱离险境，但倒霉的话也可能会落入更危险的境地。";
    }

    @Override
    public int price() {
        return this.isKnown() ? 40 * this.quantity : super.price();
    }
}

