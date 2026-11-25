/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs.npcs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.actors.mobs.npcs.NPC;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.sprites.MirrorSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import java.util.HashSet;

public class MirrorImage
extends NPC {
    public int tier;
    private int attack;
    private int damage;
    private static final String TIER = "tier";
    private static final String ATTACK = "attack";
    private static final String DAMAGE = "damage";
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet();

    public MirrorImage() {
        this.name = "镜像";
        this.spriteClass = MirrorSprite.class;
        this.state = this.HUNTING;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(TIER, this.tier);
        bundle.put(ATTACK, this.attack);
        bundle.put(DAMAGE, this.damage);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.tier = bundle.getInt(TIER);
        this.attack = bundle.getInt(ATTACK);
        this.damage = bundle.getInt(DAMAGE);
    }

    public void duplicate(Hero hero) {
        this.tier = hero.tier();
        this.attack = hero.attackSkill(hero);
        this.damage = hero.damageRoll();
    }

    @Override
    public int attackSkill(Char target) {
        return this.attack;
    }

    @Override
    public int damageRoll() {
        return this.damage;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        int dmg = super.attackProc(enemy, damage);
        this.destroy();
        this.sprite.die();
        return dmg;
    }

    @Override
    protected Char chooseEnemy() {
        if (this.enemy == null || !this.enemy.isAlive()) {
            HashSet<Mob> enemies = new HashSet<Mob>();
            for (Mob mob : Dungeon.level.mobs) {
                if (!mob.hostile || !Level.fieldOfView[mob.pos]) continue;
                enemies.add(mob);
            }
            return enemies.size() > 0 ? (Mob)Random.element(enemies) : null;
        }
        return this.enemy;
    }

    @Override
    public String description() {
        return "这个幻象与你十分相像，只是面色更加苍白，还微微有些抽搐。";
    }

    @Override
    public CharSprite sprite() {
        CharSprite s = super.sprite();
        ((MirrorSprite)s).updateArmor(this.tier);
        return s;
    }

    @Override
    public void interact() {
        int curPos = this.pos;
        this.moveSprite(this.pos, Dungeon.hero.pos);
        this.move(Dungeon.hero.pos);
        Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
        Dungeon.hero.move(curPos);
        Dungeon.hero.spend(1.0f / Dungeon.hero.speed());
        Dungeon.hero.busy();
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    static {
        IMMUNITIES.add(ToxicGas.class);
        IMMUNITIES.add(Burning.class);
    }
}

