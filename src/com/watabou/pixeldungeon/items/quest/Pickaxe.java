/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.quest;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Hunger;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.Bat;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.quest.DarkGold;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.ui.BuffIndicator;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import java.util.ArrayList;

public class Pickaxe
extends Weapon {
    public static final String AC_MINE = "MINE";
    public static final float TIME_TO_MINE = 2.0f;
    private static final String TXT_NO_VEIN = "你附近没有可挖掘的暗金矿脉";
    private static final ItemSprite.Glowing BLOODY = new ItemSprite.Glowing(0x550000);
    public boolean bloodStained;
    private static final String BLOODSTAINED = "bloodStained";

    public Pickaxe() {
        this.name = "镐子";
        this.image = 101;
        this.unique = true;
        this.defaultAction = AC_MINE;
        this.STR = 14;
        this.bloodStained = false;
    }

    @Override
    public int min() {
        return 3;
    }

    @Override
    public int max() {
        return 12;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_MINE);
        return actions;
    }

    @Override
    public void execute(final Hero hero, String action) {
        if (action == AC_MINE) {
            if (Dungeon.depth < 11 || Dungeon.depth > 15) {
                GLog.w(TXT_NO_VEIN, new Object[0]);
                return;
            }
            for (int i = 0; i < Level.NEIGHBOURS8.length; ++i) {
                final int pos = hero.pos + Level.NEIGHBOURS8[i];
                if (Dungeon.level.map[pos] != 12) continue;
                hero.spend(2.0f);
                hero.busy();
                hero.sprite.attack(pos, new Callback(){

                    @Override
                    public void call() {
                        CellEmitter.center(pos).burst(Speck.factory(1), 7);
                        GameScene.play("snd_evoke.mp3", pos);
                        Level.set(pos, 4);
                        GameScene.updateMap(pos);
                        DarkGold gold = new DarkGold();
                        if (gold.doPickUp(Dungeon.hero)) {
                            GLog.i("你获得了%s", gold.name());
                        } else {
                            Dungeon.level.drop((Item)gold, (int)hero.pos).sprite.drop();
                        }
                        Hunger hunger = hero.buff(Hunger.class);
                        if (hunger != null && !hunger.isStarving()) {
                            hunger.satisfy(-36.0f);
                            BuffIndicator.refreshHero();
                        }
                        hero.onOperateComplete();
                    }
                });
                return;
            }
            GLog.w(TXT_NO_VEIN, new Object[0]);
        } else {
            super.execute(hero, action);
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
    public void proc(Char attacker, Char defender, int damage) {
        if (!this.bloodStained && defender instanceof Bat && defender.HP <= damage) {
            this.bloodStained = true;
            this.updateQuickslot();
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(BLOODSTAINED, this.bloodStained);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.bloodStained = bundle.getBoolean(BLOODSTAINED);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return this.bloodStained ? BLOODY : null;
    }

    @Override
    public String info() {
        return "这是一把结实沉重的碎石工具，或许也能拿来当武器使。";
    }
}

