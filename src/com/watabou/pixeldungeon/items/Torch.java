/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items;

import com.watabou.noosa.particles.Emitter;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Light;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.particles.FlameParticle;
import com.watabou.pixeldungeon.items.Item;
import java.util.ArrayList;

public class Torch
extends Item {
    public static final String AC_LIGHT = "LIGHT";
    public static final float TIME_TO_LIGHT = 1.0f;

    public Torch() {
        this.name = "火把";
        this.image = 84;
        this.stackable = true;
        this.defaultAction = AC_LIGHT;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_LIGHT);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action == AC_LIGHT) {
            hero.spend(1.0f);
            hero.busy();
            hero.sprite.operate(hero.pos);
            this.detach(hero.belongings.backpack);
            Buff.affect(hero, Light.class, 250.0f);
            Emitter emitter = hero.sprite.centerEmitter();
            emitter.start(FlameParticle.FACTORY, 0.2f, 3);
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
    public int price() {
        return 10 * this.quantity;
    }

    @Override
    public String info() {
        return "在以光线昏暗而闻名的恶魔大厅中，这是一件不可或缺的照明工具。";
    }
}

