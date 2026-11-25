/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.wands;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.mobs.npcs.NPC;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.MagicMissile;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.SheepSprite;
import com.watabou.pixeldungeon.utils.BArray;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WandOfFlock
extends Wand {
    public WandOfFlock() {
        this.name = "羊群法杖";
    }

    @Override
    protected void onZap(int cell) {
        int level = this.power();
        int n = (level + 5) / 2;
        if (Actor.findChar(cell) != null && Ballistica.distance > 2) {
            cell = Ballistica.trace[Ballistica.distance - 2];
        }
        boolean[] passable = BArray.or(Level.passable, Level.avoid, null);
        for (Actor actor : Actor.all()) {
            if (!(actor instanceof Char)) continue;
            passable[((Char)actor).pos] = false;
        }
        PathFinder.buildDistanceMap(cell, passable, n);
        int dist = 0;
        if (Actor.findChar(cell) != null) {
            PathFinder.distance[cell] = Integer.MAX_VALUE;
            dist = 1;
        }
        float lifespan = level + 4;
        block1: for (int i = 0; i < n; ++i) {
            do {
                for (int j = 0; j < 1024; ++j) {
                    if (PathFinder.distance[j] != dist) continue;
                    Sheep sheep = new Sheep();
                    sheep.lifespan = lifespan;
                    sheep.pos = j;
                    GameScene.add(sheep);
                    Dungeon.level.mobPress(sheep);
                    CellEmitter.get(j).burst(Speck.factory(7), 4);
                    PathFinder.distance[j] = Integer.MAX_VALUE;
                    continue block1;
                }
            } while (++dist < n);
        }
        Dungeon.observe();
    }

    @Override
    protected void fx(int cell, Callback callback) {
        MagicMissile.wool(WandOfFlock.curUser.sprite.parent, WandOfFlock.curUser.pos, cell, callback);
        Sample.INSTANCE.play("snd_zap.mp3");
    }

    @Override
    public String desc() {
        return "轻挥这根法杖能召唤出一群魔法绵羊，制造出临时的不可穿越障碍。";
    }

    public static class Sheep
    extends NPC {
        private static final String[] QUOTES = new String[]{"咩！", "咩？", "咩。", "咩..."};
        public float lifespan;
        private boolean initialized;

        public Sheep() {
            this.name = "绵羊";
            this.spriteClass = SheepSprite.class;
            this.losBlocking = true;
            this.initialized = false;
        }

        @Override
        protected boolean act() {
            if (this.initialized) {
                this.HP = 0;
                this.destroy();
                this.sprite.die();
            } else {
                this.initialized = true;
                this.spend(this.lifespan + Random.Float(2.0f));
            }
            return true;
        }

        @Override
        public void destroy() {
            super.destroy();
            Dungeon.observe();
        }

        @Override
        public void damage(int dmg, Object src) {
        }

        @Override
        public String description() {
            return "这是一只魔法绵羊。它神奇在哪？你根本杀不死它。它会呆呆地站在原地反刍并朝你翻白眼，直到魔法消散。";
        }

        @Override
        public void interact() {
            this.yell(Random.element(QUOTES));
        }
    }
}

