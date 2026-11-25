/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.mobs.npcs.Imp;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.quest.DwarfToken;
import com.watabou.pixeldungeon.items.rings.Ring;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.windows.WndQuest;

public class WndImp
extends WndQuest {
    private static final String TXT_MESSAGE = "哦太棒了！你简直就是我的英雄！\n关于你的奖励，我现在没带现金，但我这有个更好的东西。这个戒指是我们的传家宝，是我爷爷从一个死掉的圣骑士手上摘下来的。";
    private static final String TXT_REWARD = "接受戒指";
    private Imp imp;
    private DwarfToken tokens;

    public WndImp(Imp imp, DwarfToken tokens) {
        super(imp, TXT_MESSAGE, TXT_REWARD);
        this.imp = imp;
        this.tokens = tokens;
    }

    @Override
    protected void onSelect(int index) {
        this.tokens.detach(Dungeon.hero.belongings.backpack);
        Ring reward = Imp.Quest.reward;
        ((Item)reward).identify();
        if (reward.doPickUp(Dungeon.hero)) {
            GLog.i("你获得了%s", ((Item)reward).name());
        } else {
            Dungeon.level.drop((Item)reward, (int)this.imp.pos).sprite.drop();
        }
        this.imp.flee();
        Imp.Quest.complete();
    }
}

