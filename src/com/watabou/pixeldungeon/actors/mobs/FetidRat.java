/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.ParalyticGas;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.quest.RatSkull;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.FetidRatSprite;
import com.watabou.utils.Random;
import java.util.HashSet;

public class FetidRat
extends Mob {
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet();

    public FetidRat() {
        this.name = "腐臭老鼠";
        this.spriteClass = FetidRatSprite.class;
        this.HT = 15;
        this.HP = 15;
        this.defenseSkill = 5;
        this.EXP = 3;
        this.maxLvl = 5;
        this.state = this.WANDERING;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(2, 6);
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public int dr() {
        return 2;
    }

    @Override
    public String defenseVerb() {
        return "闪避";
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        GameScene.add(Blob.seed(this.pos, 20, ParalyticGas.class));
        return super.defenseProc(enemy, damage);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        Dungeon.level.drop((Item)new RatSkull(), (int)this.pos).sprite.drop();
    }

    @Override
    public String description() {
        return "这只啮齿巨鼠比普通老鼠大得多，周身还环绕着令人作呕的恶臭毒云。";
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    static {
        IMMUNITIES.add(Paralysis.class);
    }
}

