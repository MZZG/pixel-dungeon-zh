/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.Web;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.buffs.Roots;
import com.watabou.pixeldungeon.actors.buffs.Terror;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.items.food.MysteryMeat;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.SpinnerSprite;
import com.watabou.utils.Random;
import java.util.HashSet;

public class Spinner
extends Mob {
    private static final HashSet<Class<?>> RESISTANCES = new HashSet();
    private static final HashSet<Class<?>> IMMUNITIES;

    public Spinner() {
        this.name = "矿洞蜘蛛";
        this.spriteClass = SpinnerSprite.class;
        this.HT = 50;
        this.HP = 50;
        this.defenseSkill = 14;
        this.EXP = 9;
        this.maxLvl = 16;
        this.loot = new MysteryMeat();
        this.lootChance = 0.125f;
        this.FLEEING = new Fleeing();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(12, 16);
    }

    @Override
    public int attackSkill(Char target) {
        return 20;
    }

    @Override
    public int dr() {
        return 6;
    }

    @Override
    protected boolean act() {
        boolean result = super.act();
        if (this.state == this.FLEEING && this.buff(Terror.class) == null && this.enemy != null && this.enemySeen && this.enemy.buff(Poison.class) == null) {
            this.state = this.HUNTING;
        }
        return result;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (Random.Int(2) == 0) {
            Buff.affect(enemy, Poison.class).set((float)Random.Int(7, 9) * Poison.durationFactor(enemy));
            this.state = this.FLEEING;
        }
        return damage;
    }

    @Override
    public void move(int step) {
        if (this.state == this.FLEEING) {
            GameScene.add(Blob.seed(this.pos, Random.Int(5, 7), Web.class));
        }
        super.move(step);
    }

    @Override
    public String description() {
        return "这些浑身覆有绿色绒毛的洞穴蜘蛛会极力避免正面交锋，它们更喜欢在远处静待被蛛网困住的猎物在剧毒啃咬下慢慢死去。";
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    static {
        RESISTANCES.add(Poison.class);
        IMMUNITIES = new HashSet();
        IMMUNITIES.add(Roots.class);
    }

    private class Fleeing
    extends Mob.Fleeing {
        private Fleeing() {
        }

        @Override
        protected void nowhereToRun() {
            if (Spinner.this.buff(Terror.class) == null) {
                Spinner.this.state = Spinner.this.HUNTING;
            } else {
                super.nowhereToRun();
            }
        }
    }
}

