/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Weakness;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.items.Generator;
import com.watabou.pixeldungeon.items.weapon.enchantments.Death;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.sprites.WarlockSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import java.util.HashSet;

public class Warlock
extends Mob
implements Callback {
    private static final float TIME_TO_ZAP = 1.0f;
    private static final String TXT_SHADOWBOLT_KILLED = "%s的暗影箭杀害了你...";
    private static final HashSet<Class<?>> RESISTANCES = new HashSet();

    public Warlock() {
        this.name = "矮人术士";
        this.spriteClass = WarlockSprite.class;
        this.HT = 70;
        this.HP = 70;
        this.defenseSkill = 18;
        this.EXP = 11;
        this.maxLvl = 21;
        this.loot = Generator.Category.POTION;
        this.lootChance = 0.83f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(12, 20);
    }

    @Override
    public int attackSkill(Char target) {
        return 25;
    }

    @Override
    public int dr() {
        return 8;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return Ballistica.cast(this.pos, enemy.pos, false, true) == enemy.pos;
    }

    @Override
    protected boolean doAttack(Char enemy) {
        boolean visible;
        if (Level.adjacent(this.pos, enemy.pos)) {
            return super.doAttack(enemy);
        }
        boolean bl = visible = Level.fieldOfView[this.pos] || Level.fieldOfView[enemy.pos];
        if (visible) {
            ((WarlockSprite)this.sprite).zap(enemy.pos);
        } else {
            this.zap();
        }
        return !visible;
    }

    private void zap() {
        this.spend(1.0f);
        if (Warlock.hit(this, this.enemy, true)) {
            if (this.enemy == Dungeon.hero && Random.Int(2) == 0) {
                Buff.prolong(this.enemy, Weakness.class, Weakness.duration(this.enemy));
            }
            int dmg = Random.Int(12, 18);
            this.enemy.damage(dmg, this);
            if (!this.enemy.isAlive() && this.enemy == Dungeon.hero) {
                Dungeon.fail(Utils.format("在第 %2$d 层被%1$s杀害", Utils.indefinite(this.name), Dungeon.depth));
                GLog.n(TXT_SHADOWBOLT_KILLED, this.name);
            }
        } else {
            this.enemy.sprite.showStatus(0xFFFF00, this.enemy.defenseVerb(), new Object[0]);
        }
    }

    public void onZapComplete() {
        this.zap();
        this.next();
    }

    @Override
    public void call() {
        this.next();
    }

    @Override
    public String description() {
        return "当矮人的兴趣从工程机械转向奥术秘法后，术士便在城中掌权。他们最初研习元素魔法，但很快就转投了恶魔学与死灵法术。";
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    static {
        RESISTANCES.add(Death.class);
    }
}

