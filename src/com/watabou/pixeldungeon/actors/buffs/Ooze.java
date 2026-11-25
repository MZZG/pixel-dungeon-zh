/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;

public class Ooze
extends Buff {
    private static final String TXT_HERO_KILLED = "%s杀害了你...";
    public int damage = 1;

    @Override
    public int icon() {
        return 8;
    }

    public String toString() {
        return "腐蚀淤泥";
    }

    @Override
    public boolean act() {
        if (this.target.isAlive()) {
            this.target.damage(this.damage, this);
            if (!this.target.isAlive() && this.target == Dungeon.hero) {
                Dungeon.fail(Utils.format("在第 %d 层被腐蚀淤泥吞噬", Dungeon.depth));
                GLog.n(TXT_HERO_KILLED, this.toString());
            }
            this.spend(1.0f);
        }
        if (Level.water[this.target.pos]) {
            this.detach();
        }
        return true;
    }
}

