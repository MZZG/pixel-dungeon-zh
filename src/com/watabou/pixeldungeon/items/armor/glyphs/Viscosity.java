/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor.glyphs;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Viscosity
extends Armor.Glyph {
    private static final String TXT_VISCOSITY = "粘稠之%s";
    private static ItemSprite.Glowing PURPLE = new ItemSprite.Glowing(0x8844CC);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        if (damage == 0) {
            return 0;
        }
        int level = Math.max(0, armor.effectiveLevel());
        if (Random.Int(level + 7) >= 6) {
            DeferedDamage debuff = defender.buff(DeferedDamage.class);
            if (debuff == null) {
                debuff = new DeferedDamage();
                debuff.attachTo(defender);
            }
            debuff.prolong(damage);
            defender.sprite.showStatus(0xFF8800, "延缓 %d", damage);
            return 0;
        }
        return damage;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_VISCOSITY, weaponName);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return PURPLE;
    }

    public static class DeferedDamage
    extends Buff {
        protected int damage = 0;
        private static final String DAMAGE = "damage";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(DAMAGE, this.damage);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            this.damage = bundle.getInt(DAMAGE);
        }

        @Override
        public boolean attachTo(Char target) {
            if (super.attachTo(target)) {
                this.postpone(1.0f);
                return true;
            }
            return false;
        }

        public void prolong(int damage) {
            this.damage += damage;
        }

        @Override
        public int icon() {
            return 28;
        }

        public String toString() {
            return Utils.format("延缓伤害 (%d)", this.damage);
        }

        @Override
        public boolean act() {
            if (this.target.isAlive()) {
                this.target.damage(1, this);
                if (this.target == Dungeon.hero && !this.target.isAlive()) {
                    Dungeon.fail(Utils.format("在第 %2$d 层死于%1$s", "粘稠刻印", Dungeon.depth));
                    GLog.n("粘稠刻印夺走了你的生命...", new Object[0]);
                    Badges.validateDeathFromGlyph();
                }
                this.spend(1.0f);
                if (--this.damage <= 0) {
                    this.detach();
                }
            } else {
                this.detach();
            }
            return true;
        }
    }
}

