/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.levels.features;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.particles.ElmoParticle;
import com.watabou.pixeldungeon.levels.DeadEndLevel;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.windows.WndMessage;

public class Sign {
    private static final String TXT_DEAD_END = "你怎么跑到这儿来了？！";
    private static final String[] TIPS = new String[]{"不要高估自己的力量，请使用力所能及的武器与护甲。", "地牢里的门并非都一眼可见。如果无路可走，试着搜索一下隐藏的暗门。", "记住，提升力量并非穿戴高阶装备的唯一办法——你也可以用升级卷轴降低它的力量需求。", "你可以在更深层的商店里花掉金币，第一家商店就在第 6 层。", "小心粘咕！", "像素百货——成功冒险所需的一切，这里应有尽有！", "尽早鉴定你的药剂和卷轴，别等到真正急用的时候才来赌运气。", "只是饥饿不会掉血，但饥肠辘辘会要你的命。", "偷袭具有极高的命中率。例如敌人靠近关着的门时，可以退到门后伏击它。", "千万别把天狗放出来！", "像素百货：多花钱，活更久。", "被多个怪物围攻时，试着退到门后逐个击破。", "如果你着火了，漂浮在空中踩进水里是灭不了火的。", "同时携带多个重生十字架毫无意义，因为复活时非装备物品会全部丢失。", "危险！重型机械可能造成受伤、断肢甚至死亡！", "像素百货：地牢生活更安全。", "升级带有附魔的武器时，有几率把原有的附魔震碎。", "武器和护甲比法杖和戒指磨损得更快，但修理它们的办法也更多。", "获得灭绝卷轴的唯一途径，是接受地牢幽魂的馈赠。", "国王陛下驾前，禁止携带武器！", "像素百货：恶魔猎手专享特惠价！"};
    private static final String TXT_BURN = "你刚想细看告示牌上的字，它就轰然燃起了幽绿的火焰。";

    public static void read(int pos) {
        if (Dungeon.level instanceof DeadEndLevel) {
            GameScene.show(new WndMessage(TXT_DEAD_END));
        } else {
            int index = Dungeon.depth - 1;
            if (index < TIPS.length) {
                GameScene.show(new WndMessage(TIPS[index]));
            } else {
                Dungeon.level.destroy(pos);
                GameScene.updateMap(pos);
                GameScene.discoverTile(pos, 29);
                CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
                GameScene.play("snd_burning.mp3", pos);
                GLog.w(TXT_BURN, new Object[0]);
            }
        }
    }
}

