/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Amok;
import com.watabou.pixeldungeon.actors.buffs.Terror;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.actors.mobs.npcs.Imp;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.KindOfWeapon;
import com.watabou.pixeldungeon.items.food.Food;
import com.watabou.pixeldungeon.items.weapon.melee.Knuckles;
import com.watabou.pixeldungeon.sprites.MonkSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Random;
import java.util.HashSet;

public class Monk
extends Mob {
    public static final String TXT_DISARM = "%s把你手中的%s击飞了！";
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet();

    public Monk() {
        this.name = "矮人武僧";
        this.spriteClass = MonkSprite.class;
        this.HT = 70;
        this.HP = 70;
        this.defenseSkill = 30;
        this.EXP = 11;
        this.maxLvl = 21;
        this.loot = new Food();
        this.lootChance = 0.083f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(12, 16);
    }

    @Override
    public int attackSkill(Char target) {
        return 30;
    }

    @Override
    protected float attackDelay() {
        return 0.5f;
    }

    @Override
    public int dr() {
        return 2;
    }

    @Override
    public String defenseVerb() {
        return "招架";
    }

    @Override
    public void die(Object cause) {
        Imp.Quest.process(this);
        super.die(cause);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (Random.Int(6) == 0 && enemy == Dungeon.hero) {
            Hero hero = Dungeon.hero;
            KindOfWeapon weapon = hero.belongings.weapon;
            if (weapon != null && !(weapon instanceof Knuckles) && !weapon.cursed) {
                hero.belongings.weapon = null;
                Dungeon.level.drop((Item)weapon, (int)hero.pos).sprite.drop();
                GLog.w(TXT_DISARM, this.name, weapon.name());
            }
        }
        return damage;
    }

    @Override
    public String description() {
        return "这些武僧是狂热的卫道者，誓死守护都城的秘密不被外来者窥探。他们不穿护甲也不用兵器，全凭徒手格斗之术作战。";
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    static {
        IMMUNITIES.add(Amok.class);
        IMMUNITIES.add(Terror.class);
    }
}

