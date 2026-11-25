/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.EquipableItem;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.armor.glyphs.Affection;
import com.watabou.pixeldungeon.items.armor.glyphs.AntiEntropy;
import com.watabou.pixeldungeon.items.armor.glyphs.AutoRepair;
import com.watabou.pixeldungeon.items.armor.glyphs.Bounce;
import com.watabou.pixeldungeon.items.armor.glyphs.Displacement;
import com.watabou.pixeldungeon.items.armor.glyphs.Entanglement;
import com.watabou.pixeldungeon.items.armor.glyphs.Metabolism;
import com.watabou.pixeldungeon.items.armor.glyphs.Multiplicity;
import com.watabou.pixeldungeon.items.armor.glyphs.Potential;
import com.watabou.pixeldungeon.items.armor.glyphs.Stench;
import com.watabou.pixeldungeon.items.armor.glyphs.Viscosity;
import com.watabou.pixeldungeon.sprites.HeroSprite;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import java.util.ArrayList;

public class Armor
extends EquipableItem {
    private static final int HITS_TO_KNOW = 10;
    private static final String TXT_EQUIP_CURSED = "你的%s痛苦地勒紧了你的身体";
    private static final String TXT_IDENTIFY = "你对这件%s已经足够熟悉并完成了鉴定，它是%s。";
    private static final String TXT_TO_STRING = "%s :%d";
    private static final String TXT_BROKEN = "损坏的%s :%d";
    private static final String TXT_INCOMPATIBLE = "不同属性的魔法相冲突，消除了护甲上的刻印！";
    public int tier;
    public int STR;
    private int hitsToKnow = 10;
    public Glyph glyph;
    private static final String UNFAMILIRIARITY = "unfamiliarity";
    private static final String GLYPH = "glyph";

    public Armor(int tier) {
        this.tier = tier;
        this.STR = this.typicalSTR();
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(UNFAMILIRIARITY, this.hitsToKnow);
        bundle.put(GLYPH, this.glyph);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.hitsToKnow = bundle.getInt(UNFAMILIRIARITY);
        if (this.hitsToKnow == 0) {
            this.hitsToKnow = 10;
        }
        this.inscribe((Glyph)bundle.get(GLYPH));
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(this.isEquipped(hero) ? "UNEQUIP" : "EQUIP");
        return actions;
    }

    @Override
    public boolean doEquip(Hero hero) {
        this.detach(hero.belongings.backpack);
        if (hero.belongings.armor == null || hero.belongings.armor.doUnequip(hero, true, false)) {
            hero.belongings.armor = this;
            this.cursedKnown = true;
            if (this.cursed) {
                Armor.equipCursed(hero);
                GLog.n(TXT_EQUIP_CURSED, this.toString());
            }
            ((HeroSprite)hero.sprite).updateArmor();
            hero.spendAndNext(this.time2equip(hero));
            return true;
        }
        this.collect(hero.belongings.backpack);
        return false;
    }

    @Override
    protected float time2equip(Hero hero) {
        return 2.0f / hero.speed();
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        if (super.doUnequip(hero, collect, single)) {
            hero.belongings.armor = null;
            ((HeroSprite)hero.sprite).updateArmor();
            return true;
        }
        return false;
    }

    @Override
    public boolean isEquipped(Hero hero) {
        return hero.belongings.armor == this;
    }

    public int DR() {
        return this.tier * (2 + this.effectiveLevel() + (this.glyph == null ? 0 : 1));
    }

    @Override
    public Item upgrade() {
        return this.upgrade(false);
    }

    public Item upgrade(boolean inscribe) {
        if (this.glyph != null) {
            if (!inscribe && Random.Int(this.level()) > 0) {
                GLog.w(TXT_INCOMPATIBLE, new Object[0]);
                this.inscribe(null);
            }
        } else if (inscribe) {
            this.inscribe();
        }
        --this.STR;
        return super.upgrade();
    }

    @Override
    public Item degrade() {
        ++this.STR;
        return super.degrade();
    }

    @Override
    public int maxDurability(int lvl) {
        return 6 * (lvl < 16 ? 16 - lvl : 1);
    }

    public int proc(Char attacker, Char defender, int damage) {
        if (this.glyph != null) {
            damage = this.glyph.proc(this, attacker, defender, damage);
        }
        if (!this.levelKnown && --this.hitsToKnow <= 0) {
            this.levelKnown = true;
            GLog.w(TXT_IDENTIFY, this.name(), this.toString());
            Badges.validateItemLevelAquired(this);
        }
        this.use();
        return damage;
    }

    @Override
    public String toString() {
        return this.levelKnown ? Utils.format(this.isBroken() ? TXT_BROKEN : TXT_TO_STRING, super.toString(), this.STR) : super.toString();
    }

    @Override
    public String name() {
        return this.glyph == null ? super.name() : this.glyph.name(super.name());
    }

    @Override
    public String info() {
        String name = this.name();
        StringBuilder info = new StringBuilder(this.desc());
        if (this.levelKnown) {
            info.append("\n\n这件" + name + "每次受击最多可吸收 " + Math.max(this.DR(), 0) + " 点伤害。");
            if (this.STR > Dungeon.hero.STR()) {
                if (this.isEquipped(Dungeon.hero)) {
                    info.append("\n\n由于力量不足，你的移动速度与闪避能力降低了。");
                } else {
                    info.append("\n\n由于力量不足，穿戴这件护甲会降低你的移动速度与闪避能力。");
                }
            }
        } else {
            info.append("\n\n普通的" + name + "每次受击最多可吸收 " + this.typicalDR() + " 点伤害，通常需要 " + this.typicalSTR() + " 点力量。");
            if (this.typicalSTR() > Dungeon.hero.STR()) {
                info.append("这件护甲对你来说可能太重了。");
            }
        }
        if (this.glyph != null) {
            info.append("它带有魔法刻印。");
        }
        if (this.isEquipped(Dungeon.hero)) {
            info.append("\n\n你正穿着这件" + name + (this.cursed ? "，而且因为它被诅咒了，你无法将其脱下。" : "。"));
        } else if (this.cursedKnown && this.cursed) {
            info.append("\n\n你能感受到这件" + name + "中潜藏着一股邪恶的魔力。");
        }
        return info.toString();
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
        if (Random.Int(10) == 0) {
            this.inscribe();
        }
        return this;
    }

    public int typicalSTR() {
        return 7 + this.tier * 2;
    }

    public int typicalDR() {
        return this.tier * 2;
    }

    @Override
    public int price() {
        int price = 10 * (1 << this.tier - 1);
        if (this.glyph != null) {
            price = (int)((double)price * 1.5);
        }
        return this.considerState(price);
    }

    public Armor inscribe(Glyph glyph) {
        this.glyph = glyph;
        return this;
    }

    public Armor inscribe() {
        Class<?> oldGlyphClass = this.glyph != null ? this.glyph.getClass() : null;
        Glyph gl = Glyph.random();
        while (gl.getClass() == oldGlyphClass) {
            gl = Glyph.random();
        }
        return this.inscribe(gl);
    }

    public boolean isInscribed() {
        return this.glyph != null;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return this.glyph != null ? this.glyph.glowing() : null;
    }

    public static abstract class Glyph
    implements Bundlable {
        private static final Class<?>[] glyphs = new Class[]{Bounce.class, Affection.class, AntiEntropy.class, Multiplicity.class, Potential.class, Metabolism.class, Stench.class, Viscosity.class, Displacement.class, Entanglement.class, AutoRepair.class};
        private static final float[] chances = new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};

        public abstract int proc(Armor var1, Char var2, Char var3, int var4);

        public String name(String armorName) {
            return armorName;
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

        public boolean checkOwner(Char owner) {
            if (!owner.isAlive() && owner instanceof Hero) {
                ((Hero)owner).killerGlyph = this;
                Badges.validateDeathFromGlyph();
                return true;
            }
            return false;
        }

        public static Glyph random() {
            try {
                Class<?> glyph;
                do {
                    glyph = glyphs[Random.chances(chances)];
                } while (Dungeon.legacy && glyph == AutoRepair.class);
                return (Glyph)glyph.newInstance();
            }
            catch (Exception e) {
                return null;
            }
        }
    }
}

