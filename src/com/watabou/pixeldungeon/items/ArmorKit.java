/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.armor.ClassArmor;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.HeroSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.windows.WndBag;
import java.util.ArrayList;

public class ArmorKit
extends Item {
    private static final String TXT_SELECT_ARMOR = "选择要升级的护甲";
    private static final String TXT_UPGRADED = "你用护甲工具包升级了你的%s";
    private static final float TIME_TO_UPGRADE = 2.0f;
    private static final String AC_APPLY = "APPLY";
    private final WndBag.Listener itemSelector;

    public ArmorKit() {
        this.name = "护甲工具包";
        this.image = 86;
        this.unique = true;
        this.itemSelector = new WndBag.Listener(){

            @Override
            public void onSelect(Item item) {
                if (item != null) {
                    ArmorKit.this.upgrade((Armor)item);
                }
            }
        };
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_APPLY);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action == AC_APPLY) {
            curUser = hero;
            GameScene.selectItem(this.itemSelector, WndBag.Mode.ARMOR, TXT_SELECT_ARMOR, "cursor_reforge.png");
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

    private void upgrade(Armor armor) {
        this.detach(ArmorKit.curUser.belongings.backpack);
        ArmorKit.curUser.sprite.centerEmitter().start(Speck.factory(104), 0.05f, 10);
        curUser.spend(2.0f);
        curUser.busy();
        GLog.w(TXT_UPGRADED, armor.name());
        ClassArmor classArmor = ClassArmor.upgrade(curUser, armor);
        if (ArmorKit.curUser.belongings.armor == armor) {
            ArmorKit.curUser.belongings.armor = classArmor;
            ((HeroSprite)ArmorKit.curUser.sprite).updateArmor();
        } else {
            armor.detach(ArmorKit.curUser.belongings.backpack);
            classArmor.collect(ArmorKit.curUser.belongings.backpack);
        }
        ArmorKit.curUser.sprite.operate(ArmorKit.curUser.pos);
        Sample.INSTANCE.play("snd_evoke.mp3");
    }

    @Override
    public String info() {
        return "任何人都能用这套工具与材料将普通护甲改造成\"史诗护甲\"——它不仅保留原护甲的全部属性，还能赋予穿戴者对应职业的专属特技。无需任何裁缝、制革或锻造手艺。";
    }
}

