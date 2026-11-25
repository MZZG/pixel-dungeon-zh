/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.missiles;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.windows.WndOptions;
import java.util.ArrayList;

public abstract class MissileWeapon
extends Weapon {
    private static final String TXT_MISSILES = "投掷武器";
    private static final String TXT_YES = "是的，我知道我在做什么";
    private static final String TXT_NO = "不，我改主意了";
    private static final String TXT_R_U_SURE = "你真的要把它当作近战武器装备吗？";

    public MissileWeapon() {
        this.stackable = true;
        this.levelKnown = true;
        this.defaultAction = "THROW";
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (hero.heroClass != HeroClass.HUNTRESS && hero.heroClass != HeroClass.ROGUE) {
            actions.remove("EQUIP");
            actions.remove("UNEQUIP");
        }
        return actions;
    }

    @Override
    protected void onThrow(int cell) {
        Char enemy = Actor.findChar(cell);
        if (enemy == null || enemy == curUser) {
            super.onThrow(cell);
        } else if (!curUser.shoot(enemy, this)) {
            this.miss(cell);
        }
    }

    protected void miss(int cell) {
        super.onThrow(cell);
    }

    @Override
    public void proc(Char attacker, Char defender, int damage) {
        super.proc(attacker, defender, damage);
        Hero hero = (Hero)attacker;
        if (hero.rangedWeapon == null && this.stackable) {
            if (this.quantity == 1) {
                this.doUnequip(hero, false, false);
            } else {
                this.detach(null);
            }
        }
    }

    @Override
    public boolean doEquip(final Hero hero) {
        GameScene.show(new WndOptions(TXT_MISSILES, TXT_R_U_SURE, new String[]{TXT_YES, TXT_NO}){

            @Override
            protected void onSelect(int index) {
                if (index == 0) {
                    MissileWeapon.super.doEquip(hero);
                }
            }
        });
        return false;
    }

    @Override
    public Item random() {
        return this;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public String info() {
        StringBuilder info = new StringBuilder(this.desc());
        int min = this.min();
        int max = this.max();
        info.append("\n\n这件武器的平均每次命中伤害为 " + (min + (max - min) / 2) + " 点。");
        if (Dungeon.hero.belongings.backpack.items.contains(this)) {
            if (this.STR > Dungeon.hero.STR()) {
                info.append("由于力量不足，使用这件" + this.name + "时你的命中率与攻击速度都会下降。");
            }
            if (this.STR < Dungeon.hero.STR()) {
                info.append("由于力量充沛，使用这件" + this.name + "时你的攻击伤害得到了提升。");
            }
        }
        if (this.isEquipped(Dungeon.hero)) {
            info.append("\n\n你已将其装备在手中。");
        }
        return info.toString();
    }
}

