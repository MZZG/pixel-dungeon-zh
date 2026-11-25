/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.potions.PotionOfHealing;
import com.watabou.pixeldungeon.items.weapon.enchantments.Leech;
import com.watabou.pixeldungeon.sprites.BatSprite;
import com.watabou.utils.Random;
import java.util.HashSet;

public class Bat
extends Mob {
    private static final HashSet<Class<?>> RESISTANCES = new HashSet();

    public Bat() {
        this.name = "吸血蝙蝠";
        this.spriteClass = BatSprite.class;
        this.HT = 30;
        this.HP = 30;
        this.defenseSkill = 15;
        this.baseSpeed = 2.0f;
        this.EXP = 7;
        this.maxLvl = 15;
        this.flying = true;
        this.loot = new PotionOfHealing();
        this.lootChance = 0.125f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(6, 12);
    }

    @Override
    public int attackSkill(Char target) {
        return 16;
    }

    @Override
    public int dr() {
        return 4;
    }

    @Override
    public String defenseVerb() {
        return "闪避";
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        int reg = Math.min(damage, this.HT - this.HP);
        if (reg > 0) {
            this.HP += reg;
            this.sprite.emitter().burst(Speck.factory(0), 1);
        }
        return damage;
    }

    @Override
    public String description() {
        return "这些栖息在洞窟穹顶中的敏捷生物十分顽强，每次成功攻击都能吸取生命来击败比自己庞大得多的对手。";
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    static {
        RESISTANCES.add(Leech.class);
    }
}

