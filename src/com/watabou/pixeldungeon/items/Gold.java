/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import java.util.ArrayList;

public class Gold
extends Item {
    private static final String TXT_COLLECT = "收集金币以便之后在商店消费。";
    private static final String TXT_INFO = "一堆共计 %d 枚的金币。收集金币以便之后在商店消费。";
    private static final String TXT_INFO_1 = "一枚金币。收集金币以便之后在商店消费。";
    private static final String TXT_VALUE = "%+d";
    private static final String VALUE = "value";

    public Gold() {
        this(1);
    }

    public Gold(int value) {
        this.name = "金币";
        this.image = 14;
        this.stackable = true;
        this.quantity = value;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        return new ArrayList<String>();
    }

    @Override
    public boolean doPickUp(Hero hero) {
        Dungeon.gold += this.quantity;
        Statistics.add(Statistics.Type.GOLD, this.quantity);
        Badges.validateGoldCollected();
        GameScene.pickUp(this);
        hero.sprite.showStatus(0xFFFF00, TXT_VALUE, this.quantity);
        hero.spendAndNext(1.0f);
        Sample.INSTANCE.play("snd_gold.mp3", 1.0f, Random.Float(0.9f, 1.1f), 0.0f);
        return true;
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
        switch (this.quantity) {
            case 0: {
                return TXT_COLLECT;
            }
            case 1: {
                return TXT_INFO_1;
            }
        }
        return Utils.format(TXT_INFO, this.quantity);
    }

    @Override
    public Item random() {
        this.quantity = Random.Int(20 + Dungeon.depth * 10, 40 + Dungeon.depth * 20);
        return this;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(VALUE, this.quantity);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.quantity = bundle.getInt(VALUE);
    }
}

