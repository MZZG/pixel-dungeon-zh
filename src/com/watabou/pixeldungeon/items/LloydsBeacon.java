/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items;

import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.wands.WandOfBlink;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.InterlevelScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import java.util.ArrayList;

public class LloydsBeacon
extends Item {
    private static final String TXT_PREVENTING = "这里强大的魔力场使你无法使用时空道标！";
    private static final String TXT_CREATURES = "附近生物的精神波动正在干扰你的时空道标，此刻无法使用。";
    private static final String TXT_RETURN = "时空道标已成功设置在你当前的位置，现在你可以随时返回这里。";
    private static final String TXT_INFO = "时空道标是一件精密的魔法装置，能让你瞬间返回曾经去过的地方。";
    private static final String TXT_SET = "\n\n此道标已被设定在像素地牢第 %d 层的某处。";
    public static final float TIME_TO_USE = 1.0f;
    public static final String AC_SET = "SET";
    public static final String AC_RETURN = "RETURN";
    private int returnDepth = -1;
    private int returnPos;
    private static final String DEPTH = "depth";
    private static final String POS = "pos";
    private static final ItemSprite.Glowing WHITE = new ItemSprite.Glowing(0xFFFFFF);

    public LloydsBeacon() {
        this.name = "时空道标";
        this.image = 85;
        this.unique = true;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DEPTH, this.returnDepth);
        if (this.returnDepth != -1) {
            bundle.put(POS, this.returnPos);
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.returnDepth = bundle.getInt(DEPTH);
        this.returnPos = bundle.getInt(POS);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SET);
        if (this.returnDepth != -1) {
            actions.add(AC_RETURN);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action == AC_SET || action == AC_RETURN) {
            if (Dungeon.bossLevel()) {
                hero.spend(1.0f);
                GLog.w(TXT_PREVENTING, new Object[0]);
                return;
            }
            for (int i = 0; i < Level.NEIGHBOURS8.length; ++i) {
                if (Actor.findChar(hero.pos + Level.NEIGHBOURS8[i]) == null) continue;
                GLog.w(TXT_CREATURES, new Object[0]);
                return;
            }
        }
        if (action == AC_SET) {
            this.returnDepth = Dungeon.depth;
            this.returnPos = hero.pos;
            hero.spend(1.0f);
            hero.busy();
            hero.sprite.operate(hero.pos);
            Sample.INSTANCE.play("snd_beacon.mp3");
            GLog.i(TXT_RETURN, new Object[0]);
        } else if (action == AC_RETURN) {
            if (this.returnDepth == Dungeon.depth) {
                this.reset();
                WandOfBlink.appear(hero, this.returnPos);
                Dungeon.level.press(this.returnPos, hero);
                Dungeon.observe();
            } else {
                InterlevelScene.mode = InterlevelScene.Mode.RETURN;
                InterlevelScene.returnDepth = this.returnDepth;
                InterlevelScene.returnPos = this.returnPos;
                this.reset();
                Game.switchScene(InterlevelScene.class);
            }
        } else {
            super.execute(hero, action);
        }
    }

    public void reset() {
        this.returnDepth = -1;
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
    public ItemSprite.Glowing glowing() {
        return this.returnDepth != -1 ? WHITE : null;
    }

    @Override
    public String info() {
        return TXT_INFO + (this.returnDepth == -1 ? "" : Utils.format(TXT_SET, this.returnDepth));
    }
}

