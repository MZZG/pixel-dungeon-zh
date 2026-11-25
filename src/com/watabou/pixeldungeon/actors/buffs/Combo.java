/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.utils.GLog;

public class Combo
extends Buff {
    private static String TXT_COMBO = "%d 连击！";
    public int count = 0;

    @Override
    public int icon() {
        return 17;
    }

    public String toString() {
        return "连击";
    }

    public int hit(Char enemy, int damage) {
        ++this.count;
        if (this.count >= 3) {
            Badges.validateMasteryCombo(this.count);
            GLog.p(TXT_COMBO, this.count);
            this.postpone(1.41f - (float)this.count / 10.0f);
            return (int)((float)(damage * (this.count - 2)) / 5.0f);
        }
        this.postpone(1.1f);
        return 0;
    }

    @Override
    public boolean act() {
        this.detach();
        return true;
    }
}

