/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Shadows;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.items.rings.RingOfSatiety;
import com.watabou.pixeldungeon.ui.BuffIndicator;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Hunger
extends Buff
implements Hero.Doom {
    private static final float STEP = 10.0f;
    public static final float HUNGRY = 260.0f;
    public static final float STARVING = 360.0f;
    private static final String TXT_HUNGRY = "你有点饿了。";
    private static final String TXT_STARVING = "你已经饥肠辘辘！";
    private static final String TXT_DEATH = "你活活饿死了...";
    private float level;
    private static final String LEVEL = "level";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEVEL, this.level);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.level = bundle.getFloat(LEVEL);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean act() {
        if (this.target.isAlive()) {
            Hero hero = (Hero)this.target;
            if (this.isStarving()) {
                if (Random.Float() < 0.3f && (this.target.HP > 1 || !this.target.paralysed)) {
                    GLog.n(TXT_STARVING, new Object[0]);
                    hero.damage(1, this);
                    hero.interrupt();
                }
            } else {
                int bonus = 0;
                for (Buff buff : this.target.buffs(RingOfSatiety.Satiety.class)) {
                    bonus += ((RingOfSatiety.Satiety)buff).level;
                }
                float newLevel = this.level + 10.0f - (float)bonus;
                boolean statusUpdated = false;
                if (newLevel >= 360.0f) {
                    GLog.n(TXT_STARVING, new Object[0]);
                    statusUpdated = true;
                    hero.interrupt();
                } else if (newLevel >= 260.0f && this.level < 260.0f) {
                    GLog.w(TXT_HUNGRY, new Object[0]);
                    statusUpdated = true;
                }
                this.level = newLevel;
                if (statusUpdated) {
                    BuffIndicator.refreshHero();
                }
            }
            float step = ((Hero)this.target).heroClass == HeroClass.ROGUE ? 12.0f : 10.0f;
            this.spend(this.target.buff(Shadows.class) == null ? step : step * 1.5f);
        } else {
            this.diactivate();
        }
        return true;
    }

    public void satisfy(float energy) {
        this.level -= energy;
        if (this.level < 0.0f) {
            this.level = 0.0f;
        } else if (this.level > 360.0f) {
            this.level = 360.0f;
        }
        BuffIndicator.refreshHero();
    }

    public boolean isStarving() {
        return this.level >= 360.0f;
    }

    @Override
    public int icon() {
        if (this.level < 260.0f) {
            return -1;
        }
        if (this.level < 360.0f) {
            return 5;
        }
        return 6;
    }

    public String toString() {
        if (this.level < 360.0f) {
            return "饥饿";
        }
        return "极度饥饿";
    }

    @Override
    public void onDeath() {
        Badges.validateDeathFromHunger();
        Dungeon.fail(Utils.format("在第 %d 层饿死", Dungeon.depth));
        GLog.n(TXT_DEATH, new Object[0]);
    }
}

