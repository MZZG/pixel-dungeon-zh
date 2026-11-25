/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.enchantments;

import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.effects.Lightning;
import com.watabou.pixeldungeon.effects.particles.SparkParticle;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.traps.LightningTrap;
import com.watabou.utils.Random;
import java.util.ArrayList;
import java.util.HashSet;

public class Shock
extends Weapon.Enchantment {
    private static final String TXT_SHOCKING = "电击%s";
    private ArrayList<Char> affected = new ArrayList();
    private int[] points = new int[20];
    private int nPoints;

    @Override
    public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
        int level = Math.max(0, weapon.effectiveLevel());
        if (Random.Int(level + 4) >= 3) {
            this.points[0] = attacker.pos;
            this.nPoints = 1;
            this.affected.clear();
            this.affected.add(attacker);
            this.hit(defender, Random.Int(1, damage / 2));
            attacker.sprite.parent.add(new Lightning(this.points, this.nPoints, null));
            return true;
        }
        return false;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_SHOCKING, weaponName);
    }

    private void hit(Char ch, int damage) {
        if (damage < 1) {
            return;
        }
        this.affected.add(ch);
        ch.damage(Level.water[ch.pos] && !ch.flying ? damage * 2 : damage, LightningTrap.LIGHTNING);
        ch.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
        ch.sprite.flash();
        this.points[this.nPoints++] = ch.pos;
        HashSet<Char> ns = new HashSet<Char>();
        for (int i = 0; i < Level.NEIGHBOURS8.length; ++i) {
            Char n = Actor.findChar(ch.pos + Level.NEIGHBOURS8[i]);
            if (n == null || this.affected.contains(n)) continue;
            ns.add(n);
        }
        if (ns.size() > 0) {
            this.hit((Char)Random.element(ns), Random.Int(damage / 2, damage));
        }
    }
}

