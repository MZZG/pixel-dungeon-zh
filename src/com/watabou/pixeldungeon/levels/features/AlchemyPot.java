/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.levels.features;

import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.windows.WndBag;

public class AlchemyPot {
    private static final String TXT_SELECT_SEED = "选择要投入炼金釜的种子";
    private static Hero hero;
    private static int pos;
    private static final WndBag.Listener itemSelector;

    public static void operate(Hero hero, int pos) {
        AlchemyPot.hero = hero;
        AlchemyPot.pos = pos;
        GameScene.selectItem(itemSelector, WndBag.Mode.SEED, TXT_SELECT_SEED);
    }

    static {
        itemSelector = new WndBag.Listener(){

            @Override
            public void onSelect(Item item) {
                if (item != null) {
                    item.cast(hero, pos);
                }
            }
        };
    }
}

