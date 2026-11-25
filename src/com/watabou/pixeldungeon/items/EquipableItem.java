/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.particles.ShadowParticle;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.utils.GLog;

public abstract class EquipableItem
extends Item {
    private static final String TXT_UNEQUIP_CURSED = "你无法卸下被诅咒的%s！";
    public static final String AC_EQUIP = "EQUIP";
    public static final String AC_UNEQUIP = "UNEQUIP";

    @Override
    public void execute(Hero hero, String action) {
        if (action.equals(AC_EQUIP)) {
            this.doEquip(hero);
        } else if (action.equals(AC_UNEQUIP)) {
            this.doUnequip(hero, true);
        } else {
            super.execute(hero, action);
        }
    }

    @Override
    public void doDrop(Hero hero) {
        if (!this.isEquipped(hero) || this.doUnequip(hero, false, false)) {
            super.doDrop(hero);
        }
    }

    @Override
    public void cast(Hero user, int dst) {
        if (this.isEquipped(user) && this.quantity == 1 && !this.doUnequip(user, false, false)) {
            return;
        }
        super.cast(user, dst);
    }

    protected static void equipCursed(Hero hero) {
        hero.sprite.emitter().burst(ShadowParticle.CURSE, 6);
        Sample.INSTANCE.play("snd_cursed.mp3");
    }

    protected float time2equip(Hero hero) {
        return 1.0f;
    }

    public abstract boolean doEquip(Hero var1);

    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        if (this.cursed) {
            GLog.w(TXT_UNEQUIP_CURSED, this.name());
            return false;
        }
        if (single) {
            hero.spendAndNext(this.time2equip(hero));
        } else {
            hero.spend(this.time2equip(hero));
        }
        if (collect && !this.collect(hero.belongings.backpack)) {
            Dungeon.level.drop(this, hero.pos);
        }
        return true;
    }

    public final boolean doUnequip(Hero hero, boolean collect) {
        return this.doUnequip(hero, collect, true);
    }
}

