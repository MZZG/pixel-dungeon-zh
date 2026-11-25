/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.actors.buffs.Roots;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.particles.ElmoParticle;
import com.watabou.pixeldungeon.items.armor.ClassArmor;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.utils.GLog;

public class MageArmor
extends ClassArmor {
    private static final String AC_SPECIAL = "MOLTEN EARTH";
    private static final String TXT_NOT_MAGE = "只有法师才能使用这件护甲！";

    public MageArmor() {
        this.name = "法师长袍";
        this.image = 98;
    }

    @Override
    public String special() {
        return AC_SPECIAL;
    }

    @Override
    public String desc() {
        return "穿着这件华丽法袍的法师可以施放熔岩大地法术：视野内的所有敌人都会被点燃并同时被定身在原地。";
    }

    @Override
    public void doSpecial() {
        for (Mob mob : Dungeon.level.mobs) {
            if (!Level.fieldOfView[mob.pos]) continue;
            Buff.affect(mob, Burning.class).reignite(mob);
            Buff.prolong(mob, Roots.class, 3.0f);
        }
        MageArmor.curUser.HP -= MageArmor.curUser.HP / 3;
        curUser.spend(1.0f);
        MageArmor.curUser.sprite.operate(MageArmor.curUser.pos);
        curUser.busy();
        MageArmor.curUser.sprite.centerEmitter().start(ElmoParticle.FACTORY, 0.15f, 4);
        Sample.INSTANCE.play("snd_read.mp3");
    }

    @Override
    public boolean doEquip(Hero hero) {
        if (hero.heroClass == HeroClass.MAGE) {
            return super.doEquip(hero);
        }
        GLog.w(TXT_NOT_MAGE, new Object[0]);
        return false;
    }
}

