/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items;

import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.scenes.AmuletScene;
import java.io.IOException;
import java.util.ArrayList;

public class Amulet
extends Item {
    private static final String AC_END = "END THE GAME";

    public Amulet() {
        this.name = "Yendor护符";
        this.image = 87;
        this.unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_END);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action == AC_END) {
            this.showAmuletScene(false);
        } else {
            super.execute(hero, action);
        }
    }

    @Override
    public boolean doPickUp(Hero hero) {
        if (super.doPickUp(hero)) {
            if (!Statistics.getBoolean(Statistics.Type.AMULET)) {
                Statistics.set(Statistics.Type.AMULET, true);
                Badges.validateVictory();
                this.showAmuletScene(true);
            }
            return true;
        }
        return false;
    }

    private void showAmuletScene(boolean showText) {
        try {
            Dungeon.saveAll();
            AmuletScene.noText = !showText;
            Game.switchScene(AmuletScene.class);
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public String info() {
        return "Yendor护符是已知最强大而神秘的神器，来历不明。据说只要持有者的意志足够坚定，能够\"说服\"护符，它就能实现任何愿望。";
    }
}

