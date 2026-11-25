/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Blindness;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Fury;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.effects.SpellSprite;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndChooseWay;
import java.util.ArrayList;

public class TomeOfMastery
extends Item {
    private static final String TXT_BLINDED = "失明时无法阅读";
    public static final float TIME_TO_READ = 10.0f;
    public static final String AC_READ = "READ";

    public TomeOfMastery() {
        this.stackable = false;
        this.name = Dungeon.hero != null && Dungeon.hero.subClass != HeroSubClass.NONE ? "重修之书" : "精通之书";
        this.image = 82;
        this.unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_READ);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action.equals(AC_READ)) {
            if (hero.buff(Blindness.class) != null) {
                GLog.w(TXT_BLINDED, new Object[0]);
                return;
            }
            curUser = hero;
            switch (hero.heroClass) {
                case WARRIOR: {
                    this.read(hero, HeroSubClass.GLADIATOR, HeroSubClass.BERSERKER);
                    break;
                }
                case MAGE: {
                    this.read(hero, HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK);
                    break;
                }
                case ROGUE: {
                    this.read(hero, HeroSubClass.ASSASSIN, HeroSubClass.FREERUNNER);
                    break;
                }
                case HUNTRESS: {
                    this.read(hero, HeroSubClass.SNIPER, HeroSubClass.WARDEN);
                }
            }
        } else {
            super.execute(hero, action);
        }
    }

    @Override
    public boolean doPickUp(Hero hero) {
        Badges.validateMastery();
        return super.doPickUp(hero);
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
    public String info() {
        return "这本破旧的皮面古书并不算厚，但你隐隐感到能从中领悟到许多奥秘。不过阅读它需要花费一些时间。";
    }

    private void read(Hero hero, HeroSubClass sc1, HeroSubClass sc2) {
        if (hero.subClass == sc1) {
            GameScene.show(new WndChooseWay(this, sc2));
        } else if (hero.subClass == sc2) {
            GameScene.show(new WndChooseWay(this, sc1));
        } else {
            GameScene.show(new WndChooseWay(this, sc1, sc2));
        }
    }

    public void choose(HeroSubClass way) {
        this.detach(TomeOfMastery.curUser.belongings.backpack);
        curUser.spend(10.0f);
        curUser.busy();
        TomeOfMastery.curUser.subClass = way;
        TomeOfMastery.curUser.sprite.operate(TomeOfMastery.curUser.pos);
        Sample.INSTANCE.play("snd_mastery.mp3");
        SpellSprite.show(curUser, 3);
        TomeOfMastery.curUser.sprite.emitter().burst(Speck.factory(103), 12);
        GLog.w("你选择了%s之道！", Utils.capitalize(way.title()));
        if (way == HeroSubClass.BERSERKER && (float)TomeOfMastery.curUser.HP <= (float)TomeOfMastery.curUser.HT * Fury.LEVEL) {
            Buff.affect(curUser, Fury.class);
        }
    }
}

