/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Terror;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.items.Gold;
import com.watabou.pixeldungeon.sprites.BruteSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import java.util.HashSet;

public class Brute
extends Mob {
    private static final String TXT_ENRAGED = "%s被激怒了！";
    private boolean enraged;
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet();

    public Brute() {
        this.name = "豺狼暴徒";
        this.spriteClass = BruteSprite.class;
        this.HT = 40;
        this.HP = 40;
        this.defenseSkill = 15;
        this.EXP = 8;
        this.maxLvl = 15;
        this.loot = Gold.class;
        this.lootChance = 0.5f;
        this.enraged = false;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.enraged = this.HP < this.HT / 4;
    }

    @Override
    public int damageRoll() {
        return this.enraged ? Random.NormalIntRange(10, 40) : Random.NormalIntRange(8, 18);
    }

    @Override
    public int attackSkill(Char target) {
        return 20;
    }

    @Override
    public int dr() {
        return 8;
    }

    @Override
    public void damage(int dmg, Object src) {
        super.damage(dmg, src);
        if (this.isAlive() && !this.enraged && this.HP < this.HT / 4) {
            this.enraged = true;
            this.spend(1.0f);
            if (Dungeon.visible[this.pos]) {
                GLog.w(TXT_ENRAGED, this.name);
                this.sprite.showStatus(0xFF0000, "激怒", new Object[0]);
            }
        }
    }

    @Override
    public String description() {
        return "暴徒是豺狼人中体型最大、力量最强、也最耐打的一族。身受重伤时会陷入狂暴，对敌人造成更加恐怖的伤害。";
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    static {
        IMMUNITIES.add(Terror.class);
    }
}

