/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.wands;

import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.MagicMissile;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.BArray;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WandOfAvalanche
extends Wand {
    public WandOfAvalanche() {
        this.name = "崩落法杖";
        this.hitChars = false;
    }

    @Override
    protected void onZap(int cell) {
        GameScene.play("snd_rocks.mp3", cell);
        int level = this.power();
        Ballistica.distance = Math.min(Ballistica.distance, 8 + level);
        int size = 1 + level / 3;
        PathFinder.buildDistanceMap(cell, BArray.not(Level.solid, null), size);
        int shake = 0;
        for (int i = 0; i < 1024; ++i) {
            int d = PathFinder.distance[i];
            if (d < Integer.MAX_VALUE) {
                Char ch = Actor.findChar(i);
                if (ch != null) {
                    ch.sprite.flash();
                    ch.damage(Random.Int(2, (4 + size - d) * (level + 1)), this);
                    int a = level + 2 + d;
                    int b = level + 1;
                    if (ch.isAlive() && Random.Int(a) < b) {
                        Buff.prolong(ch, Paralysis.class, Random.IntRange(3, 5));
                    }
                }
                if (ch != null && ch.isAlive()) {
                    if (ch instanceof Mob) {
                        Dungeon.level.mobPress((Mob)ch);
                    } else {
                        Dungeon.level.press(i, ch);
                    }
                } else {
                    Dungeon.level.press(i, null);
                }
                if (Dungeon.visible[i]) {
                    CellEmitter.get(i).start(Speck.factory(8), 0.07f, 3 + (size - d));
                    if (Level.water[i]) {
                        GameScene.ripple(i);
                    }
                    if (shake < size - d) {
                        shake = size - d;
                    }
                }
            }
            Camera.main.shake(3.0f, 0.07f * (float)(3 + shake));
        }
        if (!curUser.isAlive()) {
            Dungeon.fail(Utils.format("在第 %2$d 层被自己的%1$s砸死", this.name, Dungeon.depth));
            GLog.n("你用自己的崩落法杖害死了自己...", new Object[0]);
        }
    }

    @Override
    protected void fx(int cell, Callback callback) {
        MagicMissile.earth(WandOfAvalanche.curUser.sprite.parent, WandOfAvalanche.curUser.pos, cell, callback);
        Sample.INSTANCE.play("snd_zap.mp3");
    }

    @Override
    public String desc() {
        return "当这根法杖的魔弹击中墙壁或固体障碍物时，会引发落石雪崩，震晕并伤害范围内的所有生物。";
    }
}

