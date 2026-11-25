/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.items.Generator;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.weapon.enchantments.Death;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.SkeletonSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Random;
import java.util.HashSet;

public class Skeleton
extends Mob {
    private static final String TXT_HERO_KILLED = "你死于迸溅的碎骨...";
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet();

    public Skeleton() {
        this.name = "骷髅";
        this.spriteClass = SkeletonSprite.class;
        this.HT = 25;
        this.HP = 25;
        this.defenseSkill = 9;
        this.EXP = 5;
        this.maxLvl = 10;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(3, 8);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        boolean heroKilled = false;
        for (int i = 0; i < Level.NEIGHBOURS8.length; ++i) {
            Char ch = Skeleton.findChar(this.pos + Level.NEIGHBOURS8[i]);
            if (ch == null || !ch.isAlive()) continue;
            int damage = Math.max(0, this.damageRoll() - Random.IntRange(0, ch.dr() / 2));
            ch.damage(damage, this);
            if (ch != Dungeon.hero || ch.isAlive()) continue;
            heroKilled = true;
        }
        if (Dungeon.visible[this.pos]) {
            GameScene.play("snd_bones.mp3", this.pos);
        }
        if (heroKilled) {
            Dungeon.fail(Utils.format("在第 %2$d 层被%1$s杀害", Utils.indefinite(this.name), Dungeon.depth));
            GLog.n(TXT_HERO_KILLED, new Object[0]);
        }
    }

    @Override
    protected void dropLoot() {
        if (Random.Int(5) == 0) {
            Item loot = Generator.random(Generator.Category.WEAPON);
            for (int i = 0; i < 2; ++i) {
                Item l = Generator.random(Generator.Category.WEAPON);
                if (l.level() >= loot.level()) continue;
                loot = l;
            }
            Dungeon.level.drop((Item)loot, (int)this.pos).sprite.drop();
        }
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public int dr() {
        return 5;
    }

    @Override
    public String defenseVerb() {
        return "格挡";
    }

    @Override
    public String description() {
        return "骷髅由地牢中倒霉冒险者与原住民的尸骨构成，被深处涌出的邪恶魔力所驱动。遭受足够伤害后，它们会在碎骨爆裂中解体。";
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    static {
        IMMUNITIES.add(Death.class);
    }
}

