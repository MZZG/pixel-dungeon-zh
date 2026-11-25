/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items;

import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.npcs.Bee;
import com.watabou.pixeldungeon.effects.Pushing;
import com.watabou.pixeldungeon.effects.Splash;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;
import java.util.ArrayList;

public class Honeypot
extends Item {
    public static final String AC_SHATTER = "SHATTER";

    public Honeypot() {
        this.name = "蜂蜜罐";
        this.image = 125;
        this.defaultAction = "THROW";
        this.stackable = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SHATTER);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action.equals(AC_SHATTER)) {
            hero.sprite.zap(hero.pos);
            this.shatter(hero.pos);
            this.detach(hero.belongings.backpack);
            hero.spendAndNext(1.0f);
        } else {
            super.execute(hero, action);
        }
    }

    @Override
    protected void onThrow(int cell) {
        if (Level.pit[cell]) {
            super.onThrow(cell);
        } else {
            this.shatter(cell);
        }
    }

    private void shatter(int pos) {
        GameScene.play("snd_shatter.mp3", pos);
        if (Dungeon.visible[pos]) {
            Splash.at(pos, 16766208, 5);
        }
        int newPos = pos;
        if (Actor.findChar(pos) != null) {
            ArrayList<Integer> candidates = new ArrayList<Integer>();
            boolean[] passable = Level.passable;
            for (int n : Level.NEIGHBOURS4) {
                int c = pos + n;
                if (!passable[c] || Actor.findChar(c) != null) continue;
                candidates.add(c);
            }
            int n = newPos = candidates.size() > 0 ? (Integer)Random.element(candidates) : -1;
        }
        if (newPos != -1) {
            Bee bee = new Bee();
            bee.spawn(Dungeon.depth);
            bee.HP = bee.HT;
            bee.pos = newPos;
            GameScene.add(bee);
            Actor.addDelayed(new Pushing(bee, pos, newPos), -1.0f);
            bee.sprite.alpha(0.0f);
            bee.sprite.parent.add(new AlphaTweener(bee.sprite, 1.0f, 0.15f));
            GameScene.play("snd_bee.mp3", newPos);
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int price() {
        return 50 * this.quantity;
    }

    @Override
    public String info() {
        return "这个小蜂蜜罐里没剩多少蜂蜜，但里面住着一只不肯离开的金色蜜蜂。";
    }
}

