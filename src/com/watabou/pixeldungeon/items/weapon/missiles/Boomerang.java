/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.missiles;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.pixeldungeon.sprites.MissileSprite;

public class Boomerang
extends MissileWeapon {
    private boolean throwEquiped;

    public Boomerang() {
        this.name = "回旋镖";
        this.image = 106;
        this.STR = 10;
        this.stackable = false;
    }

    @Override
    public int min() {
        return this.isBroken() ? 1 : 1 + this.level();
    }

    @Override
    public int max() {
        return this.isBroken() ? 4 : 4 + 2 * this.level();
    }

    @Override
    public boolean isUpgradable() {
        return true;
    }

    @Override
    public Item upgrade() {
        return this.upgrade(false);
    }

    @Override
    public Item upgrade(boolean enchant) {
        super.upgrade(enchant);
        this.updateQuickslot();
        return this;
    }

    @Override
    public int maxDurability(int lvl) {
        return 8 * (lvl < 16 ? 16 - lvl : 1);
    }

    @Override
    public void proc(Char attacker, Char defender, int damage) {
        super.proc(attacker, defender, damage);
        if (attacker instanceof Hero && ((Hero)attacker).rangedWeapon == this) {
            this.circleBack(defender.pos, (Hero)attacker);
        }
    }

    @Override
    protected void miss(int cell) {
        this.circleBack(cell, curUser);
    }

    private void circleBack(int from, Hero owner) {
        ((MissileSprite)Boomerang.curUser.sprite.parent.recycle(MissileSprite.class)).reset(from, Boomerang.curUser.pos, curItem, null);
        if (this.throwEquiped) {
            owner.belongings.weapon = this;
            owner.spend(-1.0f);
        } else if (!this.collect(Boomerang.curUser.belongings.backpack)) {
            Dungeon.level.drop((Item)this, (int)owner.pos).sprite.drop();
        }
    }

    @Override
    public void cast(Hero user, int dst) {
        this.throwEquiped = this.isEquipped(user);
        super.cast(user, dst);
    }

    @Override
    public String desc() {
        return "投向敌人后，这把扁平弯曲的木制飞镖会自动飞回投掷者手中。";
    }
}

