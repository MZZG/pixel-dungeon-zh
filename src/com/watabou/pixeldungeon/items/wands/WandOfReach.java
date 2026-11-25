/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.wands;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.effects.MagicMissile;
import com.watabou.pixeldungeon.effects.Swap;
import com.watabou.pixeldungeon.items.Dewdrop;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.items.potions.PotionOfMight;
import com.watabou.pixeldungeon.items.potions.PotionOfStrength;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfEnchantment;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

public class WandOfReach
extends Wand {
    private static final String TXT_YOU_NOW_HAVE = "你用魔法将%s隔空收入了背包";

    public WandOfReach() {
        this.name = "念动法杖";
        this.hitChars = false;
    }

    @Override
    protected void onZap(int cell) {
        int reach = Math.min(Ballistica.distance, this.power() + 4);
        boolean mapUpdated = false;
        for (int i = 1; i < reach; ++i) {
            int c = Ballistica.trace[i];
            int before = Dungeon.level.map[c];
            Char ch = Actor.findChar(c);
            if (ch != null) {
                Actor.addDelayed(new Swap(curUser, ch), -1.0f);
                break;
            }
            Heap heap = (Heap)Dungeon.level.heaps.get(c);
            if (heap != null) {
                switch (heap.type) {
                    case HEAP: {
                        this.transport(heap);
                        break;
                    }
                    case CHEST: 
                    case MIMIC: 
                    case TOMB: 
                    case SKELETON: {
                        heap.open(curUser);
                        break;
                    }
                }
                break;
            }
            Dungeon.level.press(c, null);
            if (before == 6) {
                Level.set(c, 5);
                GameScene.updateMap(c);
            } else if (Level.water[c]) {
                GameScene.ripple(c);
            }
            mapUpdated = mapUpdated || Dungeon.level.map[c] != before;
        }
        if (mapUpdated) {
            Dungeon.observe();
        }
    }

    private void transport(Heap heap) {
        Item item = heap.pickUp();
        if (item.doPickUp(curUser)) {
            if (!(item instanceof Dewdrop)) {
                if ((item instanceof ScrollOfUpgrade || item instanceof ScrollOfEnchantment) && ((Scroll)item).isKnown() || (item instanceof PotionOfStrength || item instanceof PotionOfMight) && ((Potion)item).isKnown()) {
                    GLog.p(TXT_YOU_NOW_HAVE, item.name());
                } else {
                    GLog.i(TXT_YOU_NOW_HAVE, item.name());
                }
            }
        } else {
            Dungeon.level.drop((Item)item, (int)WandOfReach.curUser.pos).sprite.drop();
        }
    }

    @Override
    protected void fx(int cell, Callback callback) {
        MagicMissile.force(WandOfReach.curUser.sprite.parent, WandOfReach.curUser.pos, cell, callback);
        Sample.INSTANCE.play("snd_zap.mp3");
    }

    @Override
    public String desc() {
        return "这根实用法杖可用于远距离抓取物品或与敌人互换位置。射出的魔力波动还会影响沿途的所有格子：触发陷阱、踩倒高草、开关门扉。";
    }
}

