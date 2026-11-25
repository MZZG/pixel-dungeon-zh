/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.blobs;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.Journal;
import com.watabou.pixeldungeon.actors.blobs.WellWater;
import com.watabou.pixeldungeon.actors.buffs.Awareness;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.BlobEmitter;
import com.watabou.pixeldungeon.effects.Identification;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.Terrain;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;

public class WaterOfAwareness
extends WellWater {
    private static final String TXT_PROCCED = "你小酌一口后，无数知识瞬间涌入脑海。你已完全知晓身上装备的一切，同时也感知到了本层的所有物品与秘密。";

    @Override
    protected boolean affectHero(Hero hero) {
        GameScene.play("snd_drink.mp3", hero.pos);
        this.emitter.parent.add(new Identification(DungeonTilemap.tileCenterToWorld(this.pos)));
        hero.belongings.observe();
        for (int i = 0; i < 1024; ++i) {
            int terr = Dungeon.level.map[i];
            if ((Terrain.flags[terr] & 8) == 0) continue;
            Level.set(i, Terrain.discover(terr));
            GameScene.updateMap(i);
            if (!Dungeon.visible[i]) continue;
            GameScene.discoverTile(i, terr);
        }
        Buff.affect(hero, Awareness.class, 2.0f);
        Dungeon.observe();
        Dungeon.hero.interrupt();
        GLog.p(TXT_PROCCED, new Object[0]);
        Journal.remove(Journal.Feature.WELL_OF_AWARENESS);
        return true;
    }

    @Override
    protected Item affectItem(Item item) {
        if (item.isIdentified()) {
            return null;
        }
        item.identify();
        Badges.validateItemLevelAquired(item);
        this.emitter.parent.add(new Identification(DungeonTilemap.tileCenterToWorld(this.pos)));
        Journal.remove(Journal.Feature.WELL_OF_AWARENESS);
        return item;
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.pour(Speck.factory(3), 0.3f);
    }

    @Override
    public String tileDesc() {
        return "这口井中散发着知识之力。饮下一口可以揭晓当前穿戴装备的全部秘密。";
    }
}

