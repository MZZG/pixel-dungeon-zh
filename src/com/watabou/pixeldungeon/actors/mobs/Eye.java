/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Terror;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.particles.PurpleParticle;
import com.watabou.pixeldungeon.items.Dewdrop;
import com.watabou.pixeldungeon.items.wands.WandOfDisintegration;
import com.watabou.pixeldungeon.items.weapon.enchantments.Death;
import com.watabou.pixeldungeon.items.weapon.enchantments.Leech;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.sprites.EyeSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Random;
import java.util.HashSet;

public class Eye
extends Mob {
    private static final String TXT_DEATHGAZE_KILLED = "%s的死亡凝视杀害了你...";
    private int hitCell;
    private static final HashSet<Class<?>> RESISTANCES = new HashSet();
    private static final HashSet<Class<?>> IMMUNITIES;

    public Eye() {
        this.name = "邪眼";
        this.spriteClass = EyeSprite.class;
        this.HT = 100;
        this.HP = 100;
        this.defenseSkill = 20;
        this.viewDistance = 4;
        this.EXP = 13;
        this.maxLvl = 25;
        this.flying = true;
        this.loot = new Dewdrop();
        this.lootChance = 0.5f;
    }

    @Override
    public int dr() {
        return 10;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        this.hitCell = Ballistica.cast(this.pos, enemy.pos, true, false);
        for (int i = 1; i < Ballistica.distance; ++i) {
            if (Ballistica.trace[i] != enemy.pos) continue;
            return true;
        }
        return false;
    }

    @Override
    public int attackSkill(Char target) {
        return 30;
    }

    @Override
    protected float attackDelay() {
        return 1.6f;
    }

    @Override
    protected boolean doAttack(Char enemy) {
        this.spend(this.attackDelay());
        boolean rayVisible = false;
        for (int i = 0; i < Ballistica.distance; ++i) {
            if (!Dungeon.visible[Ballistica.trace[i]]) continue;
            rayVisible = true;
        }
        if (rayVisible) {
            this.sprite.attack(this.hitCell);
            return false;
        }
        this.attack(enemy);
        return true;
    }

    @Override
    public boolean attack(Char enemy) {
        for (int i = 1; i < Ballistica.distance; ++i) {
            int pos = Ballistica.trace[i];
            Char ch = Actor.findChar(pos);
            if (ch == null) continue;
            if (Eye.hit(this, ch, true)) {
                ch.damage(Random.NormalIntRange(14, 20), this);
                if (Dungeon.visible[pos]) {
                    ch.sprite.flash();
                    CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
                }
                if (ch.isAlive() || ch != Dungeon.hero) continue;
                Dungeon.fail(Utils.format("在第 %2$d 层被%1$s杀害", Utils.indefinite(this.name), Dungeon.depth));
                GLog.n(TXT_DEATHGAZE_KILLED, this.name);
                continue;
            }
            ch.sprite.showStatus(0xFFFF00, ch.defenseVerb(), new Object[0]);
        }
        return true;
    }

    @Override
    public String description() {
        return "这种恶魔又被称为\"憎恨之球\"——一旦发现敌人，它便会不顾一切地释放死亡凝视，甚至连身边的同伴也会一并误伤。";
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
        RESISTANCES.add(WandOfDisintegration.class);
        RESISTANCES.add(Death.class);
        RESISTANCES.add(Leech.class);
        IMMUNITIES = new HashSet();
        IMMUNITIES.add(Terror.class);
    }
}

