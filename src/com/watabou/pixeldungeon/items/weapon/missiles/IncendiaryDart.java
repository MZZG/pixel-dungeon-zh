/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.weapon.missiles;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.Fire;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;

public class IncendiaryDart
extends MissileWeapon {
    public IncendiaryDart() {
        this(1);
    }

    public IncendiaryDart(int number) {
        this.name = "燃烧飞镖";
        this.image = 108;
        this.STR = 12;
        this.quantity = number;
    }

    @Override
    public int min() {
        return 1;
    }

    @Override
    public int max() {
        return 2;
    }

    @Override
    protected void onThrow(int cell) {
        Char enemy = Actor.findChar(cell);
        if (enemy == null || enemy == curUser) {
            if (Level.flamable[cell]) {
                GameScene.add(Blob.seed(cell, 4, Fire.class));
            } else {
                super.onThrow(cell);
            }
        } else if (!curUser.shoot(enemy, this)) {
            Dungeon.level.drop((Item)this, (int)cell).sprite.drop();
        }
    }

    @Override
    public void proc(Char attacker, Char defender, int damage) {
        Buff.affect(defender, Burning.class).reignite(defender);
        super.proc(attacker, defender, damage);
    }

    @Override
    public String desc() {
        return "每支飞镖的尖刺都会牢牢钉入目标体内，而捆绑在镖身上的不稳定化合物则会瞬间爆发出炽烈的火焰。";
    }

    @Override
    public Item random() {
        this.quantity = Random.Int(3, 6);
        return this;
    }

    @Override
    public int price() {
        return 10 * this.quantity;
    }
}

