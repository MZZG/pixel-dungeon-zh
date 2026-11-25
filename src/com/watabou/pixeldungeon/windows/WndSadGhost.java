/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.mobs.npcs.Ghost;
import com.watabou.pixeldungeon.items.EquipableItem;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.windows.WndQuest;

public class WndSadGhost
extends WndQuest {
    private static final String TXT_WEAPON = "幽灵的武器";
    private static final String TXT_ARMOR = "幽灵的护甲";
    private Ghost ghost;
    private Item questItem;

    public WndSadGhost(Ghost ghost, Item item, String text) {
        super(ghost, text, TXT_WEAPON, TXT_ARMOR);
        this.ghost = ghost;
        this.questItem = item;
    }

    @Override
    protected void onSelect(int index) {
        EquipableItem reward;
        if (this.questItem != null) {
            this.questItem.detach(Dungeon.hero.belongings.backpack);
        }
        EquipableItem equipableItem = reward = index == 0 ? Ghost.Quest.weapon : Ghost.Quest.armor;
        if (reward.doPickUp(Dungeon.hero)) {
            GLog.i("你获得了%s", reward.name());
        } else {
            Dungeon.level.drop((Item)reward, (int)this.ghost.pos).sprite.drop();
        }
        this.ghost.yell("一路顺风，冒险家！");
        this.ghost.die(null);
        Ghost.Quest.complete();
    }
}

