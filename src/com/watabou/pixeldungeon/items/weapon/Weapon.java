/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.KindOfWeapon;
import com.watabou.pixeldungeon.items.weapon.enchantments.Death;
import com.watabou.pixeldungeon.items.weapon.enchantments.Fire;
import com.watabou.pixeldungeon.items.weapon.enchantments.Horror;
import com.watabou.pixeldungeon.items.weapon.enchantments.Instability;
import com.watabou.pixeldungeon.items.weapon.enchantments.Leech;
import com.watabou.pixeldungeon.items.weapon.enchantments.Luck;
import com.watabou.pixeldungeon.items.weapon.enchantments.Paralysis;
import com.watabou.pixeldungeon.items.weapon.enchantments.Poison;
import com.watabou.pixeldungeon.items.weapon.enchantments.Shock;
import com.watabou.pixeldungeon.items.weapon.enchantments.Slowness;
import com.watabou.pixeldungeon.items.weapon.enchantments.Tempering;
import com.watabou.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class Weapon
extends KindOfWeapon {
    private static final int HITS_TO_KNOW = 20;
    private static final String TXT_IDENTIFY = "你对这件%s已经足够熟悉并完成了鉴定，它是%s。";
    private static final String TXT_INCOMPATIBLE = "不同属性的魔法相冲突，消除了武器上的附魔！";
    private static final String TXT_TO_STRING = "%s :%d";
    private static final String TXT_BROKEN = "损坏的%s :%d";
    public int STR = 10;
    public float ACU = 1.0f;
    public float DLY = 1.0f;
    public Imbue imbue = Imbue.NONE;
    private int hitsToKnow = 20;
    protected Enchantment enchantment;
    private static final String UNFAMILIRIARITY = "unfamiliarity";
    private static final String ENCHANTMENT = "enchantment";
    private static final String IMBUE = "imbue";

    @Override
    public void proc(Char attacker, Char defender, int damage) {
        if (this.enchantment != null) {
            this.enchantment.proc(this, attacker, defender, damage);
        }
        if (!this.levelKnown && --this.hitsToKnow <= 0) {
            this.levelKnown = true;
            GLog.i(TXT_IDENTIFY, this.name(), this.toString());
            Badges.validateItemLevelAquired(this);
        }
        this.use();
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(UNFAMILIRIARITY, this.hitsToKnow);
        bundle.put(ENCHANTMENT, this.enchantment);
        bundle.put(IMBUE, this.imbue);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.hitsToKnow = bundle.getInt(UNFAMILIRIARITY);
        if (this.hitsToKnow == 0) {
            this.hitsToKnow = 20;
        }
        this.enchantment = (Enchantment)bundle.get(ENCHANTMENT);
        this.imbue = bundle.getEnum(IMBUE, Imbue.class);
    }

    @Override
    public float acuracyFactor(Hero hero) {
        int encumbrance = this.STR - hero.STR();
        if (this instanceof MissileWeapon) {
            switch (hero.heroClass) {
                case WARRIOR: {
                    encumbrance += 3;
                    break;
                }
                case HUNTRESS: {
                    encumbrance -= 2;
                    break;
                }
            }
        }
        return (encumbrance > 0 ? (float)((double)this.ACU / Math.pow(1.5, encumbrance)) : this.ACU) * (this.imbue == Imbue.ACCURACY ? 1.5f : 1.0f);
    }

    @Override
    public float speedFactor(Hero hero) {
        int encumrance = this.STR - hero.STR();
        if (this instanceof MissileWeapon && hero.heroClass == HeroClass.HUNTRESS) {
            encumrance -= 2;
        }
        return (encumrance > 0 ? (float)((double)this.DLY * Math.pow(1.2, encumrance)) : this.DLY) * (this.imbue == Imbue.SPEED ? 0.6f : 1.0f);
    }

    @Override
    public int damageRoll(Hero hero) {
        int exStr;
        int damage = super.damageRoll(hero);
        if (hero.rangedWeapon != null == (hero.heroClass == HeroClass.HUNTRESS) && (exStr = hero.STR() - this.STR) > 0) {
            damage += Random.IntRange(0, exStr);
        }
        return damage;
    }

    public Item upgrade(boolean enchant) {
        if (this.enchantment != null) {
            if (!enchant && Random.Int(this.level()) > 0) {
                GLog.w(TXT_INCOMPATIBLE, new Object[0]);
                this.enchant(null);
            }
        } else if (enchant) {
            this.enchant();
        }
        return super.upgrade();
    }

    @Override
    public int maxDurability(int lvl) {
        return 5 * (lvl < 16 ? 16 - lvl : 1);
    }

    @Override
    public String toString() {
        return this.levelKnown ? Utils.format(this.isBroken() ? TXT_BROKEN : TXT_TO_STRING, super.toString(), this.STR) : super.toString();
    }

    @Override
    public String name() {
        return this.enchantment == null ? super.name() : this.enchantment.name(super.name());
    }

    @Override
    public Item random() {
        if ((double)Random.Float() < 0.4) {
            int n = 1;
            if (Random.Int(3) == 0) {
                ++n;
                if (Random.Int(3) == 0) {
                    ++n;
                }
            }
            if (Random.Int(2) == 0) {
                this.upgrade(n);
            } else {
                this.degrade(n);
                this.cursed = true;
            }
        }
        return this;
    }

    public Weapon enchant(Enchantment ench) {
        this.enchantment = ench;
        return this;
    }

    public Weapon enchant() {
        Class<?> oldEnchantment = this.enchantment != null ? this.enchantment.getClass() : null;
        Enchantment ench = Enchantment.random();
        while (ench.getClass() == oldEnchantment) {
            ench = Enchantment.random();
        }
        return this.enchant(ench);
    }

    public boolean isEnchanted() {
        return this.enchantment != null;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return this.enchantment != null ? this.enchantment.glowing() : null;
    }

    public static abstract class Enchantment
    implements Bundlable {
        private static final Class<?>[] enchants = new Class[]{Fire.class, Poison.class, Death.class, Paralysis.class, Leech.class, Slowness.class, Shock.class, Instability.class, Horror.class, Luck.class, Tempering.class};
        private static final float[] chances = new float[]{10.0f, 10.0f, 1.0f, 2.0f, 1.0f, 2.0f, 6.0f, 3.0f, 2.0f, 2.0f, 3.0f};

        public abstract boolean proc(Weapon var1, Char var2, Char var3, int var4);

        public String name(String weaponName) {
            return weaponName;
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
        }

        @Override
        public void storeInBundle(Bundle bundle) {
        }

        public ItemSprite.Glowing glowing() {
            return ItemSprite.Glowing.WHITE;
        }

        public static Enchantment random() {
            try {
                Class<?> ench;
                do {
                    ench = enchants[Random.chances(chances)];
                } while (Dungeon.legacy && ench == Tempering.class);
                return (Enchantment)ench.newInstance();
            }
            catch (Exception e) {
                return null;
            }
        }
    }

    public static enum Imbue {
        NONE,
        SPEED,
        ACCURACY;

    }
}

