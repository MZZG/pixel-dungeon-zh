/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor;

import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.armor.HuntressArmor;
import com.watabou.pixeldungeon.items.armor.MageArmor;
import com.watabou.pixeldungeon.items.armor.RogueArmor;
import com.watabou.pixeldungeon.items.armor.WarriorArmor;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import java.util.ArrayList;

public abstract class ClassArmor
extends Armor {
    private static final String TXT_LOW_HEALTH = "你的生命值过低！";
    private static final String TXT_NOT_EQUIPPED = "你需要穿戴这套护甲才能使用它的特技！";
    private int DR;
    private static final String ARMOR_STR = "STR";
    private static final String ARMOR_DR = "DR";

    public ClassArmor() {
        super(6);
        this.levelKnown = true;
        this.cursedKnown = true;
        this.defaultAction = this.special();
    }

    public static ClassArmor upgrade(Hero owner, Armor armor) {
        ClassArmor classArmor = null;
        switch (owner.heroClass) {
            case WARRIOR: {
                classArmor = new WarriorArmor();
                break;
            }
            case ROGUE: {
                classArmor = new RogueArmor();
                break;
            }
            case MAGE: {
                classArmor = new MageArmor();
                break;
            }
            case HUNTRESS: {
                classArmor = new HuntressArmor();
            }
        }
        classArmor.STR = armor.STR;
        classArmor.DR = armor.DR();
        classArmor.inscribe(armor.glyph);
        return classArmor;
    }

    @Override
    public int DR() {
        return this.DR;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ARMOR_STR, this.STR);
        bundle.put(ARMOR_DR, this.DR);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.STR = bundle.getInt(ARMOR_STR);
        this.DR = bundle.getInt(ARMOR_DR);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (hero.HP >= 3 && this.isEquipped(hero)) {
            actions.add(this.special());
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action == this.special()) {
            if (hero.HP < 3) {
                GLog.w(TXT_LOW_HEALTH, new Object[0]);
            } else if (!this.isEquipped(hero)) {
                GLog.w(TXT_NOT_EQUIPPED, new Object[0]);
            } else {
                curUser = hero;
                this.doSpecial();
            }
        } else {
            super.execute(hero, action);
        }
    }

    public abstract String special();

    public abstract void doSpecial();

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int price() {
        return 0;
    }

    @Override
    public String desc() {
        return "这件护甲看上去帅呆了！";
    }
}

