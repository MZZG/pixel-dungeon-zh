/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.levels.features;

import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Cripple;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.levels.RegularLevel;
import com.watabou.pixeldungeon.levels.Room;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.scenes.InterlevelScene;
import com.watabou.pixeldungeon.sprites.MobSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndOptions;
import com.watabou.utils.Random;

public class Chasm {
    private static final String TXT_CHASM = "深渊";
    private static final String TXT_YES = "是的，我知道我在做什么";
    private static final String TXT_NO = "不，我改主意了";
    private static final String TXT_JUMP = "你真的要跳入深渊吗？这很可能会要了你的命。";
    public static boolean jumpConfirmed = false;

    public static void heroJump(final Hero hero) {
        GameScene.show(new WndOptions(TXT_CHASM, TXT_JUMP, new String[]{TXT_YES, TXT_NO}){

            @Override
            protected void onSelect(int index) {
                if (index == 0) {
                    jumpConfirmed = true;
                    hero.resume();
                }
            }
        });
    }

    public static void heroFall(int pos) {
        jumpConfirmed = false;
        Sample.INSTANCE.play("snd_falling.mp3");
        if (Dungeon.hero.isAlive()) {
            Room room;
            Dungeon.hero.interrupt();
            InterlevelScene.mode = InterlevelScene.Mode.FALL;
            InterlevelScene.fallIntoPit = Dungeon.level instanceof RegularLevel ? (room = ((RegularLevel)Dungeon.level).room(pos)) != null && room.type == Room.Type.WEAK_FLOOR : false;
            Game.switchScene(InterlevelScene.class);
        } else {
            Dungeon.hero.sprite.visible = false;
        }
    }

    public static void heroLand() {
        Hero hero = Dungeon.hero;
        hero.sprite.burst(hero.sprite.blood(), 10);
        Camera.main.shake(4.0f, 0.2f);
        Buff.prolong(hero, Cripple.class, 10.0f);
        hero.damage(Random.IntRange(hero.HT / 3, hero.HT / 2), new Hero.Doom(){

            @Override
            public void onDeath() {
                Badges.validateDeathFromFalling();
                Dungeon.fail(Utils.format("在第 %d 层坠落摔死", Dungeon.depth));
                GLog.n("你从高处跌落摔死了...", new Object[0]);
            }
        });
    }

    public static void mobFall(Mob mob) {
        mob.destroy();
        ((MobSprite)mob.sprite).fall();
    }
}

