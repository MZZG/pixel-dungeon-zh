/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor;

import com.watabou.noosa.Camera;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Fury;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.armor.ClassArmor;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.scenes.CellSelector;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

public class WarriorArmor
extends ClassArmor {
    private static int LEAP_TIME = 1;
    private static int SHOCK_TIME = 3;
    private static final String AC_SPECIAL = "HEROIC LEAP";
    private static final String TXT_NOT_WARRIOR = "只有战士才能使用这件护甲！";
    protected static CellSelector.Listener leaper = new CellSelector.Listener(){

        @Override
        public void onSelect(Integer target) {
            if (target != null && target != curUser.pos) {
                int cell = Ballistica.cast(curUser.pos, target, false, true);
                if (Actor.findChar(cell) != null && cell != curUser.pos) {
                    cell = Ballistica.trace[Ballistica.distance - 2];
                }
                curUser.HP -= curUser.HP / 3;
                if (curUser.subClass == HeroSubClass.BERSERKER && (float)curUser.HP <= (float)curUser.HT * Fury.LEVEL) {
                    Buff.affect(curUser, Fury.class);
                }
                Invisibility.dispel();
                final int dest = cell;
                curUser.busy();
                curUser.sprite.jump(curUser.pos, cell, new Callback(){

                    @Override
                    public void call() {
                        curUser.move(dest);
                        Dungeon.level.press(dest, curUser);
                        Dungeon.observe();
                        for (int i = 0; i < Level.NEIGHBOURS8.length; ++i) {
                            Char mob = Actor.findChar(curUser.pos + Level.NEIGHBOURS8[i]);
                            if (mob == null || mob == curUser) continue;
                            Buff.prolong(mob, Paralysis.class, SHOCK_TIME);
                        }
                        CellEmitter.center(dest).burst(Speck.factory(109), 10);
                        Camera.main.shake(2.0f, 0.5f);
                        curUser.spendAndNext(LEAP_TIME);
                    }
                });
            }
        }

        @Override
        public String prompt() {
            return "选择飞跃的目标位置";
        }

        @Override
        public String cursor() {
            return "cursor_target.png";
        }
    };

    public WarriorArmor() {
        this.name = "战士重铠";
        this.image = 97;
    }

    @Override
    public String special() {
        return AC_SPECIAL;
    }

    @Override
    public void doSpecial() {
        GameScene.selectCell(leaper);
    }

    @Override
    public boolean doEquip(Hero hero) {
        if (hero.heroClass == HeroClass.WARRIOR) {
            return super.doEquip(hero);
        }
        GLog.w(TXT_NOT_WARRIOR, new Object[0]);
        return false;
    }

    @Override
    public String desc() {
        return "这副铠甲看似笨重，却能让战士向目标位置发动英勇之跃，重重落地并震晕周围所有敌人。";
    }
}

