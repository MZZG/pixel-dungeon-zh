/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.keys;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.items.bags.Bag;
import com.watabou.pixeldungeon.items.keys.Key;
import com.watabou.pixeldungeon.utils.Utils;

public class IronKey
extends Key {
    private static final String TXT_FROM_DEPTH = "第 %d 层的铁钥匙";
    public static int curDepthQuantity = 0;

    public IronKey() {
        this.name = "铁钥匙";
        this.image = 9;
    }

    @Override
    public boolean collect(Bag bag) {
        boolean result = super.collect(bag);
        if (result && this.depth == Dungeon.depth && Dungeon.hero != null) {
            Dungeon.hero.belongings.countIronKeys();
        }
        return result;
    }

    @Override
    public void onDetach() {
        if (this.depth == Dungeon.depth) {
            Dungeon.hero.belongings.countIronKeys();
        }
    }

    @Override
    public String toString() {
        return Utils.format(TXT_FROM_DEPTH, this.depth);
    }

    @Override
    public String info() {
        return "这把古老铁钥匙的匙齿已严重磨损，皮质挂绳也饱经岁月摧残。它对应的究竟是哪扇门呢？";
    }
}

