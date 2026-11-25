/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.wands;

import com.watabou.noosa.Camera;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Lightning;
import com.watabou.pixeldungeon.effects.particles.SparkParticle;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.traps.LightningTrap;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import java.util.ArrayList;
import java.util.HashSet;

public class WandOfLightning
extends Wand {
    private ArrayList<Char> affected;
    private int[] points;
    private int nPoints;

    public WandOfLightning() {
        this.name = "雷霆法杖";
        this.affected = new ArrayList();
        this.points = new int[20];
    }

    @Override
    protected void onZap(int cell) {
        if (!curUser.isAlive()) {
            Dungeon.fail(Utils.format("在第 %2$d 层被自己的%1$s电死", this.name, Dungeon.depth));
            GLog.n("你用自己的雷霆法杖电死了自己...", new Object[0]);
        }
    }

    private void hit(Char ch, int damage) {
        if (damage < 1) {
            return;
        }
        if (ch == Dungeon.hero) {
            Camera.main.shake(2.0f, 0.3f);
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

    @Override
    protected void fx(int cell, Callback callback) {
        this.nPoints = 0;
        this.points[this.nPoints++] = Dungeon.hero.pos;
        Char ch = Actor.findChar(cell);
        if (ch != null) {
            this.affected.clear();
            int lvl = this.power();
            this.hit(ch, Random.Int(5 + lvl / 2, 10 + lvl));
        } else {
            this.points[this.nPoints++] = cell;
            CellEmitter.center(cell).burst(SparkParticle.FACTORY, 3);
        }
        WandOfLightning.curUser.sprite.parent.add(new Lightning(this.points, this.nPoints, callback));
    }

    @Override
    public String desc() {
        return "这根法杖能召唤致命的电弧，在彼此靠近的多名生物之间跳跃并造成伤害。";
    }
}

