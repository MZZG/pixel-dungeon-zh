/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Terror;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.particles.ShadowParticle;
import com.watabou.pixeldungeon.items.weapon.enchantments.Death;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.WraithSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import java.util.HashSet;

public class Wraith
extends Mob {
    private static final float SPAWN_DELAY = 2.0f;
    private int level;
    private static final String LEVEL = "level";
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet();

    public Wraith() {
        this.name = "怨灵";
        this.spriteClass = WraithSprite.class;
        this.HT = 1;
        this.HP = 1;
        this.EXP = 0;
        this.flying = true;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEVEL, this.level);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.level = bundle.getInt(LEVEL);
        this.adjustStats(this.level);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(1, 3 + this.level);
    }

    @Override
    public int attackSkill(Char target) {
        return 10 + this.level;
    }

    public void adjustStats(int level) {
        this.level = level;
        this.defenseSkill = this.attackSkill(null) * 5;
        this.enemySeen = true;
    }

    @Override
    public String defenseVerb() {
        return "闪避";
    }

    @Override
    public boolean reset() {
        this.state = this.WANDERING;
        return true;
    }

    @Override
    public String description() {
        return "怨灵是墓穴被惊扰后前来复仇的罪人之魂。作为虚无缥缈的灵体，它很难被普通武器击中。";
    }

    public static void spawnAround(int pos) {
        for (int n : Level.NEIGHBOURS4) {
            int cell = pos + n;
            if (!Level.passable[cell] || Actor.findChar(cell) != null) continue;
            Wraith.spawnAt(cell);
        }
    }

    public static Wraith spawnAt(int pos) {
        if (Level.passable[pos] && Actor.findChar(pos) == null) {
            Wraith w = new Wraith();
            w.adjustStats(Dungeon.depth);
            w.pos = pos;
            w.state = w.HUNTING;
            GameScene.add(w, 2.0f);
            w.sprite.alpha(0.0f);
            w.sprite.parent.add(new AlphaTweener(w.sprite, 1.0f, 0.5f));
            w.sprite.emitter().burst(ShadowParticle.CURSE, 5);
            return w;
        }
        return null;
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    static {
        IMMUNITIES.add(Death.class);
        IMMUNITIES.add(Terror.class);
    }
}

