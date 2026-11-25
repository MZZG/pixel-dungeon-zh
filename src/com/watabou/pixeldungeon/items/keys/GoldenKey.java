/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.keys;

import com.watabou.pixeldungeon.items.keys.Key;

public class GoldenKey
extends Key {
    public GoldenKey() {
        this.name = "金钥匙";
        this.image = 10;
    }

    @Override
    public String info() {
        return "这把金钥匙的匙齿细密而精致，或许能打开某只宝箱上的锁？";
    }
}

