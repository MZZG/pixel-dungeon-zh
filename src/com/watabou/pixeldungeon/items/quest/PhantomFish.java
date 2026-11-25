/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.quest;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.utils.GLog;
import java.util.ArrayList;

public class PhantomFish
extends Item {
    private static final String AC_EAT = "EAT";
    private static final float TIME_TO_EAT = 2.0f;

    public PhantomFish() {
        this.name = "幻影鱼";
        this.image = 118;
        this.unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_EAT);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action.equals(AC_EAT)) {
            this.detach(hero.belongings.backpack);
            hero.sprite.operate(hero.pos);
            hero.busy();
            Sample.INSTANCE.play("snd_eat.mp3");
            Sample.INSTANCE.play("snd_meld.mp3");
            GLog.i("你看到自己的双手变得透明了！", new Object[0]);
            Buff.affect(hero, Invisibility.class, 15.0f);
            hero.spend(2.0f);
        } else {
            super.execute(hero, action);
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public String info() {
        return "这条半透明的小鱼在空气中都几乎看不清，一旦回到水里就会完全隐形。";
    }
}

