/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.melee;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Random;

public class MeleeWeapon
extends Weapon {
    private int tier;

    public MeleeWeapon(int tier, float acu, float dly) {
        this.tier = tier;
        this.ACU = acu;
        this.DLY = dly;
        this.STR = this.typicalSTR();
    }

    protected int min0() {
        return this.tier;
    }

    protected int max0() {
        return (int)((float)(this.tier * this.tier - this.tier + 10) / this.ACU * this.DLY);
    }

    @Override
    public int min() {
        return this.isBroken() ? this.min0() : this.min0() + this.level();
    }

    @Override
    public int max() {
        return this.isBroken() ? this.max0() : this.max0() + this.level() * this.tier;
    }

    @Override
    public final Item upgrade() {
        return this.upgrade(false);
    }

    @Override
    public Item upgrade(boolean enchant) {
        --this.STR;
        return super.upgrade(enchant);
    }

    public Item safeUpgrade() {
        return this.upgrade(this.enchantment != null);
    }

    @Override
    public Item degrade() {
        ++this.STR;
        return super.degrade();
    }

    public int typicalSTR() {
        return 8 + this.tier * 2;
    }

    @Override
    public String info() {
        String p = "\n\n";
        StringBuilder info = new StringBuilder(this.desc());
        int lvl = this.visiblyUpgraded();
        String quality = lvl != 0 ? (lvl > 0 ? (this.isBroken() ? "破损的强化" : "经过强化的") : "被削弱的") : "";
        info.append("\n\n");
        info.append("这把" + this.name + "是一件" + quality);
        info.append(" " + this.tier + " 阶近战武器。");
        if (this.levelKnown) {
            int min = this.min();
            int max = this.max();
            info.append("它的平均每次命中伤害为 " + (min + (max - min) / 2) + " 点。");
        } else {
            int min = this.min0();
            int max = this.max0();
            info.append("它通常的平均每次命中伤害为 " + (min + (max - min) / 2) + " 点，一般需要 " + this.typicalSTR() + " 点力量。");
            if (this.typicalSTR() > Dungeon.hero.STR()) {
                info.append("这把武器对你来说可能过重了。");
            }
        }
        if (this.DLY != 1.0f) {
            info.append("这是一把攻速较" + (this.DLY < 1.0f ? "快" : "慢"));
            if (this.ACU != 1.0f) {
                if (this.ACU > 1.0f == this.DLY < 1.0f) {
                    info.append("且");
                } else {
                    info.append("但");
                }
                info.append(this.ACU > 1.0f ? "精准" : "不够精准");
            }
            info.append("的武器。");
        } else if (this.ACU != 1.0f) {
            info.append("这是一把" + (this.ACU > 1.0f ? "较为精准" : "不太精准") + "的武器。");
        }
        switch (this.imbue) {
            case SPEED: {
                info.append("它经过配重调整，攻速更快。");
                break;
            }
            case ACCURACY: {
                info.append("它经过配重调整，更为精准。");
                break;
            }
        }
        if (this.enchantment != null) {
            info.append("它带有魔法附魔。");
        }
        if (this.levelKnown && Dungeon.hero.belongings.backpack.items.contains(this)) {
            if (this.STR > Dungeon.hero.STR()) {
                info.append("\n\n");
                info.append("由于力量不足，使用这把" + this.name + "时你的命中率与攻击速度都会下降。");
            }
            if (this.STR < Dungeon.hero.STR()) {
                info.append("\n\n");
                info.append("由于力量充沛，使用这把" + this.name + "时你的攻击伤害得到了提升。");
            }
        }
        if (this.isEquipped(Dungeon.hero)) {
            info.append("\n\n");
            info.append("你已装备这把" + this.name + (this.cursed ? "，而且因为它被诅咒了，你无法将其卸下。" : "。"));
        } else if (this.cursedKnown && this.cursed) {
            info.append("\n\n");
            info.append("你能感受到这把" + this.name + "中潜藏着一股邪恶的魔力。");
        }
        return info.toString();
    }

    @Override
    public int price() {
        int price = 20 * (1 << this.tier - 1);
        if (this.enchantment != null) {
            price = (int)((double)price * 1.5);
        }
        return this.considerState(price);
    }

    @Override
    public Item random() {
        super.random();
        if (Random.Int(10 + this.level()) == 0) {
            this.enchant();
        }
        return this;
    }
}

