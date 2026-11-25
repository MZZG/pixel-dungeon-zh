/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.melee;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;
import com.watabou.pixeldungeon.items.weapon.missiles.Boomerang;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.windows.WndBag;
import java.util.ArrayList;

public class ShortSword
extends MeleeWeapon {
    public static final String AC_REFORGE = "REFORGE";
    private static final String TXT_SELECT_WEAPON = "选择要强化的武器";
    private static final String TXT_REFORGED = "你重铸了短剑，使%s得到了强化";
    private static final String TXT_NOT_BOOMERANG = "无法用这种方式强化回旋镖";
    private static final float TIME_TO_REFORGE = 2.0f;
    private boolean equipped;
    private final WndBag.Listener itemSelector;

    public ShortSword() {
        super(1, 1.0f, 1.0f);
        this.name = "短剑";
        this.image = 2;
        this.itemSelector = new WndBag.Listener(){

            @Override
            public void onSelect(Item item) {
                if (item != null && !(item instanceof Boomerang)) {
                    Sample.INSTANCE.play("snd_evoke.mp3");
                    ScrollOfUpgrade.upgrade(curUser);
                    Item.evoke(curUser);
                    GLog.w(ShortSword.TXT_REFORGED, item.name());
                    ((MeleeWeapon)item).safeUpgrade();
                    curUser.spendAndNext(2.0f);
                    Badges.validateItemLevelAquired(item);
                } else {
                    if (item instanceof Boomerang) {
                        GLog.w(ShortSword.TXT_NOT_BOOMERANG, new Object[0]);
                    }
                    if (ShortSword.this.equipped) {
                        curUser.belongings.weapon = ShortSword.this;
                    } else {
                        ShortSword.this.collect(curUser.belongings.backpack);
                    }
                }
            }
        };
        this.STR = 11;
    }

    @Override
    protected int max0() {
        return 12;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (this.level() > 0) {
            actions.add(AC_REFORGE);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action == AC_REFORGE) {
            if (hero.belongings.weapon == this) {
                this.equipped = true;
                hero.belongings.weapon = null;
            } else {
                this.equipped = false;
                this.detach(hero.belongings.backpack);
            }
            curUser = hero;
            GameScene.selectItem(this.itemSelector, WndBag.Mode.WEAPON, TXT_SELECT_WEAPON);
        } else {
            super.execute(hero, action);
        }
    }

    @Override
    public String desc() {
        return "它确实相当短小，只比匕首长出几寸。";
    }
}

