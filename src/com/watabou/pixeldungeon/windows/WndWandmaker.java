/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.mobs.npcs.Wandmaker;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndQuest;

public class WndWandmaker
extends WndQuest {
    private static final String TXT_MESSAGE = "哦，看来你成功了！希望这事没给你添太多麻烦。信守承诺，你可以从我这些上等法杖中挑选一根。";
    private static final String TXT_BATTLE = "战斗法杖";
    private static final String TXT_NON_BATTLE = "功能法杖";
    private static final String TXT_FARAWELL = "祝你在这场试炼中好运，%s！";
    private Wandmaker wandmaker;
    private Item questItem;

    public WndWandmaker(Wandmaker wandmaker, Item item) {
        super(wandmaker, TXT_MESSAGE, TXT_BATTLE, TXT_NON_BATTLE);
        this.wandmaker = wandmaker;
        this.questItem = item;
    }

    @Override
    protected void onSelect(int index) {
        this.questItem.detach(Dungeon.hero.belongings.backpack);
        Wand reward = index == 0 ? Wandmaker.Quest.wand1 : Wandmaker.Quest.wand2;
        ((Item)reward).identify();
        if (reward.doPickUp(Dungeon.hero)) {
            GLog.i("你获得了%s", ((Item)reward).name());
        } else {
            Dungeon.level.drop((Item)reward, (int)this.wandmaker.pos).sprite.drop();
        }
        this.wandmaker.yell(Utils.format(TXT_FARAWELL, Dungeon.hero.className()));
        this.wandmaker.destroy();
        this.wandmaker.sprite.die();
        Wandmaker.Quest.complete();
    }
}

