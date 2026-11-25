/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.levels.traps;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;

public class AlarmTrap {
    public static void trigger(int pos, Char ch) {
        for (Mob mob : Dungeon.level.mobs) {
            if (mob == ch) continue;
            mob.beckon(pos);
        }
        if (Dungeon.visible[pos]) {
            GLog.w("陷阱发出刺耳的警报声，回荡在整个地牢之中！", new Object[0]);
            CellEmitter.center(pos).start(Speck.factory(5), 0.3f, 3);
        }
        GameScene.play("snd_alert.mp3", pos);
    }
}

