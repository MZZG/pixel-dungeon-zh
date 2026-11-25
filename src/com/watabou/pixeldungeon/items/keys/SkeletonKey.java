/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.keys;

import com.watabou.pixeldungeon.items.keys.Key;

public class SkeletonKey
extends Key {
    public SkeletonKey() {
        this.name = "骷髅钥匙";
        this.image = 8;
    }

    @Override
    public String info() {
        return "这把钥匙看上去非同寻常，钥匙柄雕成了骷髅的形状，想必能打开某扇重要的门。";
    }
}

