/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.armor;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.items.armor.ClassArmor;
import com.watabou.pixeldungeon.items.weapon.missiles.Shuriken;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.sprites.MissileSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import java.util.HashMap;

public class HuntressArmor
extends ClassArmor {
    private static final String TXT_NO_ENEMIES = "视野内没有敌人";
    private static final String TXT_NOT_HUNTRESS = "只有女猎手才能使用这件护甲！";
    private static final String AC_SPECIAL = "SPECTRAL BLADES";
    private HashMap<Callback, Mob> targets;

    public HuntressArmor() {
        this.name = "女猎手披风";
        this.image = 99;
        this.targets = new HashMap();
    }

    @Override
    public String special() {
        return AC_SPECIAL;
    }

    @Override
    public void doSpecial() {
        Shuriken proto = new Shuriken();
        for (Mob mob : Dungeon.level.mobs) {
            if (!Level.fieldOfView[mob.pos]) continue;
            Callback callback = new Callback(){

                @Override
                public void call() {
                    curUser.attack((Char)HuntressArmor.this.targets.get(this));
                    HuntressArmor.this.targets.remove(this);
                    if (HuntressArmor.this.targets.isEmpty()) {
                        curUser.spendAndNext(curUser.attackDelay());
                    }
                }
            };
            ((MissileSprite)HuntressArmor.curUser.sprite.parent.recycle(MissileSprite.class)).reset(HuntressArmor.curUser.pos, mob.pos, proto, callback);
            this.targets.put(callback, mob);
        }
        if (this.targets.size() == 0) {
            GLog.w(TXT_NO_ENEMIES, new Object[0]);
            return;
        }
        HuntressArmor.curUser.HP -= HuntressArmor.curUser.HP / 3;
        HuntressArmor.curUser.sprite.zap(HuntressArmor.curUser.pos);
        curUser.busy();
    }

    @Override
    public boolean doEquip(Hero hero) {
        if (hero.heroClass == HeroClass.HUNTRESS) {
            return super.doEquip(hero);
        }
        GLog.w(TXT_NOT_HUNTRESS, new Object[0]);
        return false;
    }

    @Override
    public String desc() {
        return "身穿这件披风的女猎手可以扇形射出一排灵魂飞刃，每把飞刃都会追踪视野内的一名敌人，并依据当前装备的近战武器造成伤害。";
    }
}

