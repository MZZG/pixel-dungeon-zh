/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.buffs.Blindness;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.armor.ClassArmor;
import com.watabou.pixeldungeon.items.wands.WandOfBlink;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.CellSelector;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;

public class RogueArmor
extends ClassArmor {
    private static final String TXT_FOV = "你只能跳跃到视野内的空地上";
    private static final String TXT_NOT_ROGUE = "只有盗贼才能使用这件护甲！";
    private static final String AC_SPECIAL = "SMOKE BOMB";
    protected static CellSelector.Listener teleporter = new CellSelector.Listener(){

        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                if (!Level.fieldOfView[target] || !Level.passable[target] && !Level.avoid[target] || Actor.findChar(target) != null) {
                    GLog.w(RogueArmor.TXT_FOV, new Object[0]);
                    return;
                }
                curUser.HP -= curUser.HP / 3;
                for (Mob mob : Dungeon.level.mobs) {
                    if (!Level.fieldOfView[mob.pos]) continue;
                    Buff.prolong(mob, Blindness.class, 2.0f);
                    mob.state = mob.WANDERING;
                    mob.sprite.emitter().burst(Speck.factory(2), 4);
                }
                WandOfBlink.appear(curUser, target);
                CellEmitter.get(target).burst(Speck.factory(7), 10);
                Sample.INSTANCE.play("snd_puff.mp3");
                Dungeon.level.press(target, curUser);
                Dungeon.observe();
                curUser.spendAndNext(1.0f);
            }
        }

        @Override
        public String prompt() {
            return "选择要跳跃的位置";
        }

        @Override
        public String cursor() {
            return "cursor_target.png";
        }
    };

    public RogueArmor() {
        this.name = "盗贼夜行衣";
        this.image = 96;
    }

    @Override
    public String special() {
        return AC_SPECIAL;
    }

    @Override
    public void doSpecial() {
        GameScene.selectCell(teleporter);
    }

    @Override
    public boolean doEquip(Hero hero) {
        if (hero.heroClass == HeroClass.ROGUE) {
            return super.doEquip(hero);
        }
        GLog.w(TXT_NOT_ROGUE, new Object[0]);
        return false;
    }

    @Override
    public String desc() {
        return "穿着这套深色夜行衣的盗贼可以施展名为\"烟雾弹\"的戏法（虽然并没有用到真炸药）：致盲视野内能看到他的敌人并借机闪身脱逃。";
    }
}

