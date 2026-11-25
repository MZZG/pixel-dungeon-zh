/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Terror;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.items.Gold;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.rings.RingOfHaggler;
import com.watabou.pixeldungeon.sprites.ThiefSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Thief
extends Mob {
    protected static final String TXT_STOLE = "%s偷走了你的%s！";
    protected static final String TXT_CARRIES = "\n\n%s正拿着一件_%s_，显然是偷来的。";
    public Item item;
    private static final String ITEM = "item";

    public Thief() {
        this.name = "疯狂小偷";
        this.spriteClass = ThiefSprite.class;
        this.HT = 20;
        this.HP = 20;
        this.defenseSkill = 12;
        this.EXP = 5;
        this.maxLvl = 10;
        this.loot = RingOfHaggler.class;
        this.lootChance = 0.01f;
        this.FLEEING = new Fleeing();
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ITEM, this.item);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.item = (Item)bundle.get(ITEM);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(1, 7);
    }

    @Override
    protected float attackDelay() {
        return 0.5f;
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        if (this.item != null) {
            Dungeon.level.drop((Item)this.item, (int)this.pos).sprite.drop();
        }
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public int dr() {
        return 3;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (this.item == null && enemy instanceof Hero && this.steal((Hero)enemy)) {
            this.state = this.FLEEING;
        }
        return damage;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        if (this.state == this.FLEEING) {
            Dungeon.level.drop((Item)new Gold(), (int)this.pos).sprite.drop();
        }
        return damage;
    }

    protected boolean steal(Hero hero) {
        Item item = hero.belongings.randomUnequipped();
        if (item != null) {
            GLog.w(TXT_STOLE, this.name, item.name());
            item.detachAll(hero.belongings.backpack);
            this.item = item;
            return true;
        }
        return false;
    }

    @Override
    public String description() {
        String desc = "地牢深处向来是各路罪犯的藏身之所。长期远离天日，并非所有人都能保持神智清醒。早在很久以前，这些疯癫的窃贼与强盗就已忘记自己是谁，也忘了为何而偷窃。";
        if (this.item != null) {
            desc = desc + String.format(TXT_CARRIES, Utils.capitalize(this.name), this.item.name());
        }
        return desc;
    }

    private class Fleeing
    extends Mob.Fleeing {
        private Fleeing() {
        }

        @Override
        protected void nowhereToRun() {
            if (Thief.this.buff(Terror.class) == null) {
                Thief.this.sprite.showStatus(0xFF0000, "#$%^", new Object[0]);
                Thief.this.state = Thief.this.HUNTING;
            } else {
                super.nowhereToRun();
            }
        }
    }
}

