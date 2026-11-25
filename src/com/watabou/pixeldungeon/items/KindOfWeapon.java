/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.EquipableItem;
import com.watabou.pixeldungeon.ui.QuickSlot;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Random;
import java.util.ArrayList;

public abstract class KindOfWeapon
extends EquipableItem {
    private static final String TXT_EQUIP_CURSED = "你的手不由自主地紧握住%s，疼得抽搐了一下";
    protected static final float TIME_TO_EQUIP = 1.0f;

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(this.isEquipped(hero) ? "UNEQUIP" : "EQUIP");
        return actions;
    }

    @Override
    public boolean isEquipped(Hero hero) {
        return hero.belongings.weapon == this;
    }

    @Override
    public boolean doEquip(Hero hero) {
        this.detachAll(hero.belongings.backpack);
        if (hero.belongings.weapon == null || hero.belongings.weapon.doUnequip(hero, true)) {
            hero.belongings.weapon = this;
            this.activate(hero);
            QuickSlot.refresh();
            this.cursedKnown = true;
            if (this.cursed) {
                KindOfWeapon.equipCursed(hero);
                GLog.n(TXT_EQUIP_CURSED, this.name());
            }
            hero.spendAndNext(1.0f);
            return true;
        }
        this.collect(hero.belongings.backpack);
        return false;
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        if (super.doUnequip(hero, collect, single)) {
            hero.belongings.weapon = null;
            return true;
        }
        return false;
    }

    public void activate(Hero hero) {
    }

    public abstract int min();

    public abstract int max();

    public int damageRoll(Hero owner) {
        return Random.NormalIntRange(this.min(), this.max());
    }

    public float acuracyFactor(Hero hero) {
        return 1.0f;
    }

    public float speedFactor(Hero hero) {
        return 1.0f;
    }

    public void proc(Char attacker, Char defender, int damage) {
    }
}

