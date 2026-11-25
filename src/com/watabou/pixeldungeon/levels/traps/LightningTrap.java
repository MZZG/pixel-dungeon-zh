/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.levels.traps;

import com.watabou.noosa.Camera;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Lightning;
import com.watabou.pixeldungeon.effects.particles.SparkParticle;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Random;

public class LightningTrap {
    private static final String name = "雷电陷阱";
    public static final Electricity LIGHTNING = new Electricity();

    public static void trigger(int pos, Char ch) {
        if (ch != null) {
            ch.damage(Math.max(1, Random.Int(ch.HP / 3, 2 * ch.HP / 3)), LIGHTNING);
            if (ch == Dungeon.hero) {
                Camera.main.shake(2.0f, 0.3f);
                if (!ch.isAlive()) {
                    Dungeon.fail(Utils.format("在第 %2$d 层死于%1$s触发", name, Dungeon.depth));
                    GLog.n("你死于雷电陷阱的电击...", new Object[0]);
                } else {
                    ((Hero)ch).belongings.charge(false);
                }
            }
            int[] points = new int[]{pos - 32, pos + 32};
            ch.sprite.parent.add(new Lightning(points, 2, null));
            points[0] = pos - 1;
            points[1] = pos + 1;
            ch.sprite.parent.add(new Lightning(points, 2, null));
        }
        CellEmitter.center(pos).burst(SparkParticle.FACTORY, Random.IntRange(3, 4));
    }

    public static class Electricity {
    }
}

