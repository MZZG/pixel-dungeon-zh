/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.hero;

import com.watabou.utils.Bundle;

public enum HeroSubClass {
    NONE(null, null),
    GLADIATOR("角斗士", "使用近战武器成功命中敌人后，_角斗士_会进入连击状态，每一次连续命中都将造成更高的伤害。"),
    BERSERKER("狂战士", "周围包围的敌人越多，_狂战士_的攻击速度就越快。身负重伤时，_狂战士_会进入狂怒状态，大幅提升自身伤害。"),
    WARLOCK("术士", "击杀敌人后，_术士_会吞噬对方的灵魂，以此治愈伤势并缓解饥饿。"),
    BATTLEMAGE("战斗法师", "手持法杖作战时，_战斗法师_会根据法杖当前剩余充能造成额外伤害，且每次成功命中都会为该法杖恢复 1 点充能。"),
    ASSASSIN("刺客", "发动偷袭时，_刺客_会对目标造成额外的致命伤害。"),
    FREERUNNER("疾行者", "_疾行者_的移动速度几乎是大多数怪物的两倍，奔跑时也更难被击中。前提是不能超重且未处于饥饿掉血状态。"),
    SNIPER("狙击手", "_狙击手_能够看穿敌人护甲的弱点，使用投掷武器攻击时可完全无视目标防御。"),
    WARDEN("守望者", "与自然之力的紧密联系让_守望者_能从草丛中采集更多露珠和种子，踩踏高草还能获得临时的树肤护甲增益。");

    private String title;
    private String desc;
    private static final String SUBCLASS = "subClass";

    private HeroSubClass(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public String title() {
        return this.title;
    }

    public String desc() {
        return this.desc;
    }

    public void storeInBundle(Bundle bundle) {
        bundle.put(SUBCLASS, this.toString());
    }

    public static HeroSubClass restoreInBundle(Bundle bundle) {
        String value = bundle.getString(SUBCLASS);
        try {
            return HeroSubClass.valueOf(value);
        }
        catch (Exception e) {
            return NONE;
        }
    }
}

