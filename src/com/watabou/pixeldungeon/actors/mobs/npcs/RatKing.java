/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs.npcs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.mobs.npcs.NPC;
import com.watabou.pixeldungeon.sprites.RatKingSprite;

public class RatKing
extends NPC {
    public RatKing() {
        this.name = "鼠王";
        this.spriteClass = RatKingSprite.class;
        this.state = this.SLEEPEING;
    }

    @Override
    public int defenseSkill(Char enemy) {
        return 1000;
    }

    @Override
    public float speed() {
        return 2.0f;
    }

    @Override
    protected Char chooseEnemy() {
        return null;
    }

    @Override
    public void damage(int dmg, Object src) {
    }

    @Override
    public void add(Buff buff) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public void interact() {
        this.sprite.turnTo(this.pos, Dungeon.hero.pos);
        if (this.state == this.SLEEPEING) {
            this.notice();
            this.yell("我可不是在睡觉！");
            this.state = this.WANDERING;
        } else {
            this.yell("你这是想干什么？我可没时间管这些破事。我的王国可不会自己运转下去！");
        }
    }

    @Override
    public String description() {
        return "这只老鼠比普通啮齿小鼠略大一些，头上还戴着一顶小小的皇冠。";
    }
}

