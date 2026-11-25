/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.wands;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.DeathRay;
import com.watabou.pixeldungeon.effects.particles.PurpleParticle;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import java.util.ArrayList;

public class WandOfDisintegration
extends Wand {
    public WandOfDisintegration() {
        this.name = "解离法杖";
        this.hitChars = false;
    }

    @Override
    protected void onZap(int cell) {
        int lvl;
        boolean terrainAffected = false;
        int level = this.power();
        int maxDistance = this.distance();
        Ballistica.distance = Math.min(Ballistica.distance, maxDistance);
        ArrayList<Char> chars = new ArrayList<Char>();
        for (int i = 1; i < Ballistica.distance; ++i) {
            int terr;
            int c = Ballistica.trace[i];
            Char ch = Actor.findChar(c);
            if (ch != null) {
                chars.add(ch);
            }
            if ((terr = Dungeon.level.map[c]) == 5 || terr == 29) {
                Dungeon.level.destroy(c);
                GameScene.updateMap(c);
                terrainAffected = true;
            } else if (terr == 15) {
                Level.set(c, 2);
                GameScene.updateMap(c);
                terrainAffected = true;
            }
            CellEmitter.center(c).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
        }
        if (terrainAffected) {
            Dungeon.observe();
        }
        int dmgMin = lvl = level + chars.size();
        int dmgMax = 8 + lvl * lvl / 3;
        for (Char ch : chars) {
            ch.damage(Random.NormalIntRange(dmgMin, dmgMax), this);
            ch.sprite.centerEmitter().burst(PurpleParticle.BURST, Random.IntRange(1, 2));
            ch.sprite.flash();
        }
    }

    private int distance() {
        return this.level() + 4;
    }

    @Override
    protected void fx(int cell, Callback callback) {
        cell = Ballistica.trace[Math.min(Ballistica.distance, this.distance()) - 1];
        WandOfDisintegration.curUser.sprite.parent.add(new DeathRay(WandOfDisintegration.curUser.sprite.center(), DungeonTilemap.tileCenterToWorld(cell)));
        callback.call();
    }

    @Override
    public String desc() {
        return "这根法杖会射出一道毁灭性能量射线，穿透沿途所有生物。穿过的目标越多，对每个目标造成的伤害也越高。";
    }
}

