/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Charm;
import com.watabou.pixeldungeon.actors.buffs.Sleep;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfLullaby;
import com.watabou.pixeldungeon.items.wands.WandOfBlink;
import com.watabou.pixeldungeon.items.weapon.enchantments.Leech;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.SuccubusSprite;
import com.watabou.utils.Random;
import java.util.HashSet;

public class Succubus
extends Mob {
    private static final int BLINK_DELAY = 5;
    private int delay = 0;
    private static final HashSet<Class<?>> RESISTANCES = new HashSet();
    private static final HashSet<Class<?>> IMMUNITIES;

    public Succubus() {
        this.name = "魅魔";
        this.spriteClass = SuccubusSprite.class;
        this.HT = 80;
        this.HP = 80;
        this.defenseSkill = 25;
        this.viewDistance = 4;
        this.EXP = 12;
        this.maxLvl = 25;
        this.loot = new ScrollOfLullaby();
        this.lootChance = 0.05f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(15, 25);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (Random.Int(3) == 0) {
            Buff.affect((Char)enemy, Charm.class, (float)(Charm.durationFactor((Char)enemy) * (float)Random.IntRange((int)3, (int)7))).object = this.id();
            enemy.sprite.centerEmitter().start(Speck.factory(11), 0.2f, 5);
            GameScene.play("snd_charms.mp3", this.pos);
        }
        return damage;
    }

    @Override
    protected boolean getCloser(int target) {
        if (Level.fieldOfView[target] && Level.distance(this.pos, target) > 2 && this.delay <= 0) {
            this.blink(target);
            this.spend(-1.0f / this.speed());
            return true;
        }
        --this.delay;
        return super.getCloser(target);
    }

    private void blink(int target) {
        int cell = Ballistica.cast(this.pos, target, true, true);
        if (Actor.findChar(cell) != null && Ballistica.distance > 1) {
            cell = Ballistica.trace[Ballistica.distance - 2];
        }
        WandOfBlink.appear(this, cell);
        this.delay = 5;
    }

    @Override
    public int attackSkill(Char target) {
        return 40;
    }

    @Override
    public int dr() {
        return 10;
    }

    @Override
    public String description() {
        return "魅魔是一种外貌妖娆（略带哥特风格）的恶魔。她们能施展魔法魅惑英雄，使其在效果消退前无法发起任何攻击。";
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
        RESISTANCES.add(Leech.class);
        IMMUNITIES = new HashSet();
        IMMUNITIES.add(Sleep.class);
    }
}

