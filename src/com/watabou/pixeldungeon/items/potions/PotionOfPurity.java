/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.potions;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.ParalyticGas;
import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.GasesImmunity;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.BArray;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;

public class PotionOfPurity
extends Potion {
    private static final String TXT_FRESHNESS = "你感到空气中弥漫着异乎寻常的清新气息。";
    private static final String TXT_NO_SMELL = "你不再闻到任何气味了！";
    private static final int DISTANCE = 2;

    public PotionOfPurity() {
        this.name = "净化药剂";
    }

    @Override
    public void shatter(int cell) {
        boolean heroAffected;
        PathFinder.buildDistanceMap(cell, BArray.not(Level.losBlocking, null), 2);
        boolean procd = false;
        Blob[] blobs = new Blob[]{Dungeon.level.blobs.get(ToxicGas.class), Dungeon.level.blobs.get(ParalyticGas.class)};
        for (int j = 0; j < blobs.length; ++j) {
            Blob blob = blobs[j];
            if (blob == null) continue;
            for (int i = 0; i < 1024; ++i) {
                int value;
                if (PathFinder.distance[i] >= Integer.MAX_VALUE || (value = blob.cur[i]) <= 0) continue;
                blob.cur[i] = 0;
                blob.volume -= value;
                procd = true;
                if (!Dungeon.visible[i]) continue;
                CellEmitter.get(i).burst(Speck.factory(101), 1);
            }
        }
        boolean bl = heroAffected = PathFinder.distance[Dungeon.hero.pos] < Integer.MAX_VALUE;
        if (procd) {
            if (Dungeon.visible[cell]) {
                this.splash(cell);
                GameScene.play("snd_shatter.mp3", cell);
            }
            this.setKnown();
            if (heroAffected) {
                GLog.p(TXT_FRESHNESS, new Object[0]);
            }
        } else {
            super.shatter(cell);
            if (heroAffected) {
                GLog.i(TXT_FRESHNESS, new Object[0]);
                this.setKnown();
            }
        }
    }

    @Override
    protected void apply(Hero hero) {
        GLog.w(TXT_NO_SMELL, new Object[0]);
        Buff.prolong(hero, GasesImmunity.class, 5.0f);
        this.setKnown();
    }

    @Override
    public String desc() {
        return "这种试剂能迅速中和作用范围内的所有有害气体，直接饮用还能让你在短时间内免疫毒气。";
    }

    @Override
    public int price() {
        return this.isKnown() ? 50 * this.quantity : super.price();
    }
}

