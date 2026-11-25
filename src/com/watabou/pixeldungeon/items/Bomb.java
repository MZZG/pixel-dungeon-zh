/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.particles.BlastParticle;
import com.watabou.pixeldungeon.effects.particles.SmokeParticle;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;

public class Bomb
extends Item {
    public Bomb() {
        this.name = "炸弹";
        this.image = 124;
        this.defaultAction = "THROW";
        this.stackable = true;
    }

    @Override
    protected void onThrow(int cell) {
        if (Level.pit[cell]) {
            super.onThrow(cell);
        } else {
            GameScene.play("snd_blast.mp3", 2.0f, 1.0f, cell);
            if (Dungeon.visible[cell]) {
                CellEmitter.center(cell).burst(BlastParticle.FACTORY, 30);
            }
            boolean terrainAffected = false;
            for (int n : Level.NEIGHBOURS9) {
                int dmg;
                Char ch;
                int c = cell + n;
                if (c < 0 || c >= 1024) continue;
                if (Dungeon.visible[c]) {
                    CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
                }
                if (Level.flamable[c]) {
                    Dungeon.level.destroy(c);
                    GameScene.updateMap(c);
                    terrainAffected = true;
                }
                if ((ch = Actor.findChar(c)) == null || (dmg = Random.Int(1 + Dungeon.depth, 10 + Dungeon.depth * 2) - Random.Int(ch.dr())) <= 0) continue;
                ch.damage(dmg, this);
                if (!ch.isAlive()) continue;
                Buff.prolong(ch, Paralysis.class, 2.0f);
            }
            if (terrainAffected) {
                Dungeon.observe();
            }
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public Item random() {
        this.quantity = Random.IntRange(1, 3);
        return this;
    }

    @Override
    public int price() {
        return 10 * this.quantity;
    }

    @Override
    public String info() {
        return "这是一枚装满黑火药的小型炸弹。贴心的是，只要扔出去引信就会自动点燃。";
    }
}

