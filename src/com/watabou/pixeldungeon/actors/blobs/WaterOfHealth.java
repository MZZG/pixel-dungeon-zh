/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.blobs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Journal;
import com.watabou.pixeldungeon.actors.blobs.WellWater;
import com.watabou.pixeldungeon.actors.buffs.Hunger;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.BlobEmitter;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.effects.particles.ShaftParticle;
import com.watabou.pixeldungeon.items.DewVial;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.potions.PotionOfHealing;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;

public class WaterOfHealth
extends WellWater {
    private static final String TXT_PROCCED = "你小酌一口后，立刻感到所有伤口都痊愈了。";

    @Override
    protected boolean affectHero(Hero hero) {
        GameScene.play("snd_drink.mp3", hero.pos);
        PotionOfHealing.heal(hero);
        hero.belongings.uncurseEquipped();
        hero.buff(Hunger.class).satisfy(360.0f);
        CellEmitter.get(this.pos).start(ShaftParticle.FACTORY, 0.2f, 3);
        Dungeon.hero.interrupt();
        GLog.p(TXT_PROCCED, new Object[0]);
        Journal.remove(Journal.Feature.WELL_OF_HEALTH);
        return true;
    }

    @Override
    protected Item affectItem(Item item) {
        if (item instanceof DewVial && !((DewVial)item).isFull()) {
            ((DewVial)item).fill();
            Journal.remove(Journal.Feature.WELL_OF_HEALTH);
            return item;
        }
        return null;
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.start(Speck.factory(0), 0.5f, 0);
    }

    @Override
    public String tileDesc() {
        return "这口井中散发着生命之力。饮下一口可以治愈伤势并缓解饥饿。";
    }
}

