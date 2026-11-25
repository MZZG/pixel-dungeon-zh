/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.rings;

import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.mobs.Eye;
import com.watabou.pixeldungeon.actors.mobs.Warlock;
import com.watabou.pixeldungeon.actors.mobs.Yog;
import com.watabou.pixeldungeon.items.rings.Ring;
import com.watabou.pixeldungeon.levels.traps.LightningTrap;
import com.watabou.utils.Random;
import java.util.HashSet;

public class RingOfElements
extends Ring {
    private static final HashSet<Class<?>> EMPTY = new HashSet();
    private static final HashSet<Class<?>> FULL = new HashSet();

    public RingOfElements() {
        this.name = "元素之戒";
    }

    @Override
    protected Ring.RingBuff buff() {
        return new Resistance();
    }

    @Override
    public String desc() {
        return this.isKnown() ? "这枚戒指能提供对火焰、雷电、毒气等各类元素的抗性，并缩短负面状态的持续时间。" : super.desc();
    }

    static {
        FULL.add(Burning.class);
        FULL.add(ToxicGas.class);
        FULL.add(Poison.class);
        FULL.add(LightningTrap.Electricity.class);
        FULL.add(Warlock.class);
        FULL.add(Eye.class);
        FULL.add(Yog.BurningFist.class);
    }

    public class Resistance
    extends Ring.RingBuff {
        public HashSet<Class<?>> resistances() {
            if (Random.Int(this.level + 3) >= 3) {
                return FULL;
            }
            return EMPTY;
        }

        public float durationFactor() {
            return this.level < 0 ? 1.0f : (2.0f + 0.5f * (float)this.level) / (float)(2 + this.level);
        }
    }
}

