/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.noosa.Camera;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.particles.SparkParticle;
import com.watabou.pixeldungeon.items.Generator;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.traps.LightningTrap;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.sprites.ShamanSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import java.util.HashSet;

public class Shaman
extends Mob
implements Callback {
    private static final float TIME_TO_ZAP = 2.0f;
    private static final String TXT_LIGHTNING_KILLED = "%s的闪电箭杀害了你...";
    private static final HashSet<Class<?>> RESISTANCES = new HashSet();

    public Shaman() {
        this.name = "豺狼萨满";
        this.spriteClass = ShamanSprite.class;
        this.HT = 18;
        this.HP = 18;
        this.defenseSkill = 8;
        this.EXP = 6;
        this.maxLvl = 14;
        this.loot = Generator.Category.SCROLL;
        this.lootChance = 0.33f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(2, 6);
    }

    @Override
    public int attackSkill(Char target) {
        return 11;
    }

    @Override
    public int dr() {
        return 4;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return Ballistica.cast(this.pos, enemy.pos, false, true) == enemy.pos;
    }

    @Override
    protected boolean doAttack(Char enemy) {
        boolean visible;
        if (Level.distance(this.pos, enemy.pos) <= 1) {
            return super.doAttack(enemy);
        }
        boolean bl = visible = Level.fieldOfView[this.pos] || Level.fieldOfView[enemy.pos];
        if (visible) {
            ((ShamanSprite)this.sprite).zap(enemy.pos);
        }
        this.spend(2.0f);
        if (Shaman.hit(this, enemy, true)) {
            int dmg = Random.Int(2, 12);
            if (Level.water[enemy.pos] && !enemy.flying) {
                dmg = (int)((float)dmg * 1.5f);
            }
            enemy.damage(dmg, LightningTrap.LIGHTNING);
            enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
            enemy.sprite.flash();
            if (enemy == Dungeon.hero) {
                Camera.main.shake(2.0f, 0.3f);
                if (!enemy.isAlive()) {
                    Dungeon.fail(Utils.format("在第 %2$d 层被%1$s杀害", Utils.indefinite(this.name), Dungeon.depth));
                    GLog.n(TXT_LIGHTNING_KILLED, this.name);
                }
            }
        } else {
            enemy.sprite.showStatus(0xFFFF00, enemy.defenseVerb(), new Object[0]);
        }
        return !visible;
    }

    @Override
    public void call() {
        this.next();
    }

    @Override
    public String description() {
        return "最聪慧的豺狼人能够掌握萨满法术。豺狼萨满依靠战斗法术来弥补体格的不足，并会毫不犹豫地用它们惩罚敢于质疑自己部族地位的家伙。";
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    static {
        RESISTANCES.add(LightningTrap.Electricity.class);
    }
}

