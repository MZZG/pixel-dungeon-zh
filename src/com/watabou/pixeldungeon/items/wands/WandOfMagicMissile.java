/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.wands;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndBag;
import com.watabou.utils.Random;
import java.util.ArrayList;

public class WandOfMagicMissile
extends Wand {
    public static final String AC_DISENCHANT = "DISENCHANT";
    private static final String TXT_SELECT_WAND = "选择要升级的法杖";
    private static final String TXT_DISENCHANTED = "你将魔弹法杖祛魅，并用其魔力精华升级了%s";
    private static final float TIME_TO_DISENCHANT = 2.0f;
    private final WndBag.Listener itemSelector;

    public WandOfMagicMissile() {
        this.name = "魔弹法杖";
        this.image = 3;
        this.itemSelector = new WndBag.Listener(){

            @Override
            public void onSelect(Item item) {
                if (item != null) {
                    Sample.INSTANCE.play("snd_evoke.mp3");
                    ScrollOfUpgrade.upgrade(curUser);
                    Item.evoke(curUser);
                    GLog.w(WandOfMagicMissile.TXT_DISENCHANTED, item.name());
                    item.upgrade();
                    curUser.spendAndNext(2.0f);
                    Badges.validateItemLevelAquired(item);
                } else if (WandOfMagicMissile.this.disenchantEquipped) {
                    curUser.belongings.weapon = WandOfMagicMissile.this;
                    WandOfMagicMissile.this.updateQuickslot();
                } else {
                    WandOfMagicMissile.this.collect(curUser.belongings.backpack);
                }
            }
        };
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (this.level() > 0) {
            actions.add(AC_DISENCHANT);
        }
        return actions;
    }

    @Override
    protected void onZap(int cell) {
        Char ch = Actor.findChar(cell);
        if (ch != null) {
            int level = this.power();
            ch.damage(Random.Int(1, 6 + level * 2), this);
            ch.sprite.burst(-6697729, level / 2 + 2);
            if (ch == curUser && !ch.isAlive()) {
                Dungeon.fail(Utils.format("在第 %2$d 层被自己的%1$s炸死", this.name, Dungeon.depth));
                GLog.n("你用自己的魔弹法杖害死了自己...", new Object[0]);
            }
        }
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action.equals(AC_DISENCHANT)) {
            if (hero.belongings.weapon == this) {
                this.disenchantEquipped = true;
                hero.belongings.weapon = null;
                this.updateQuickslot();
            } else {
                this.disenchantEquipped = false;
                this.detach(hero.belongings.backpack);
            }
            curUser = hero;
            WndBag.wandClass = null;
            GameScene.selectItem(this.itemSelector, WndBag.Mode.WAND, TXT_SELECT_WAND, "cursor_upgrade.png");
        } else {
            super.execute(hero, action);
        }
    }

    @Override
    protected boolean isKnown() {
        return true;
    }

    @Override
    public void setKnown() {
    }

    @Override
    protected int initialCharges() {
        return 3;
    }

    @Override
    public String desc() {
        return "这根法杖会发射纯粹的魔法能量飞弹，对目标生物造成中等伤害。";
    }
}

