/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Pushing;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.Gold;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.MimicSprite;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Mimic
extends Mob {
    private int level;
    public ArrayList<Item> items;
    private static final String LEVEL = "level";
    private static final String ITEMS = "items";
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet();

    public Mimic() {
        this.name = "宝箱怪";
        this.spriteClass = MimicSprite.class;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ITEMS, this.items);
        bundle.put(LEVEL, this.level);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.items = new ArrayList<Item>();
        for (com.watabou.utils.Bundlable b : bundle.getCollection(ITEMS)) {
            this.items.add((Item)b);
        }
        this.adjustStats(bundle.getInt(LEVEL));
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(this.HT / 10, this.HT / 4);
    }

    @Override
    public int attackSkill(Char target) {
        return 9 + this.level;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        Gold gold;
        if (enemy == Dungeon.hero && Random.Int(3) == 0 && (gold = new Gold(Random.Int(Dungeon.gold / 10, Dungeon.gold / 2))).quantity() > 0) {
            Dungeon.gold -= gold.quantity();
            Dungeon.level.drop((Item)gold, (int)Dungeon.hero.pos).sprite.drop();
        }
        return super.attackProc(enemy, damage);
    }

    public void adjustStats(int level) {
        this.level = level;
        this.HT = (3 + level) * 4;
        this.EXP = 2 + 2 * (level - 1) / 5;
        this.defenseSkill = this.attackSkill(null) / 2;
        this.enemySeen = true;
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        if (this.items != null) {
            for (Item item : this.items) {
                Dungeon.level.drop((Item)item, (int)this.pos).sprite.drop();
            }
        }
    }

    @Override
    public boolean reset() {
        this.state = this.WANDERING;
        return true;
    }

    @Override
    public String description() {
        return "宝箱怪是可以任意变换形态的魔法生物。在地牢中它们几乎总是伪装成宝箱，因为它们深谙如何引诱冒险者上钩。";
    }

    public static Mimic spawnAt(int pos, List<Item> items) {
        Char ch = Actor.findChar(pos);
        if (ch != null) {
            ArrayList<Integer> candidates = new ArrayList<Integer>();
            for (int n : Level.NEIGHBOURS8) {
                int cell = pos + n;
                if (!Level.passable[cell] && !Level.avoid[cell] || Actor.findChar(cell) != null) continue;
                candidates.add(cell);
            }
            if (candidates.size() > 0) {
                int newPos = (Integer)Random.element(candidates);
                Actor.addDelayed(new Pushing(ch, ch.pos, newPos), -1.0f);
                ch.pos = newPos;
                if (ch instanceof Mob) {
                    Dungeon.level.mobPress((Mob)ch);
                } else {
                    Dungeon.level.press(newPos, ch);
                }
            } else {
                return null;
            }
        }
        Mimic m = new Mimic();
        m.items = new ArrayList<Item>(items);
        m.adjustStats(Dungeon.depth);
        m.HP = m.HT;
        m.pos = pos;
        m.state = m.HUNTING;
        GameScene.add(m, 1.0f);
        m.sprite.turnTo(pos, Dungeon.hero.pos);
        if (Dungeon.visible[m.pos]) {
            CellEmitter.get(pos).burst(Speck.factory(1), 10);
            GameScene.play("snd_mimic.mp3", pos);
        }
        return m;
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    static {
        IMMUNITIES.add(ScrollOfPsionicBlast.class);
    }
}

