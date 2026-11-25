/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.input.NoosaInputProcessor;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Game;
import com.watabou.noosa.TouchArea;
import com.watabou.pixeldungeon.Chrome;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.utils.SparseArray;

public class WndStory
extends Window {
    private static final int WIDTH = 120;
    private static final int MARGIN = 6;
    private static final float bgR = 0.77f;
    private static final float bgG = 0.73f;
    private static final float bgB = 0.62f;
    public static final int ID_SEWERS = 0;
    public static final int ID_PRISON = 1;
    public static final int ID_CAVES = 2;
    public static final int ID_METROPOLIS = 3;
    public static final int ID_HALLS = 4;
    private static final SparseArray<String> CHAPTERS = new SparseArray();
    private BitmapTextMultiline tf;
    private float delay;

    public WndStory(String text) {
        super(0, 0, Chrome.get(Chrome.Type.SCROLL));
        this.tf = PixelScene.createMultiline(text, 7.0f);
        this.tf.maxWidth = 108;
        this.tf.measure();
        this.tf.ra = 0.77f;
        this.tf.ga = 0.73f;
        this.tf.ba = 0.62f;
        this.tf.rm = -0.77f;
        this.tf.gm = -0.73f;
        this.tf.bm = -0.62f;
        this.tf.x = 6.0f;
        this.add(this.tf);
        this.add(new TouchArea(this.chrome){

            @Override
            protected void onClick(NoosaInputProcessor.Touch touch) {
                WndStory.this.hide();
            }
        });
        this.resize((int)(this.tf.width() + 12.0f), (int)Math.min(this.tf.height(), 180.0f));
    }

    @Override
    public void update() {
        super.update();
        if (this.delay > 0.0f) {
            if ((this.delay -= Game.elapsed) <= 0.0f) {
                this.tf.visible = true;
                this.chrome.visible = true;
                this.shadow.visible = true;
            }
        }
    }

    public static void showChapter(int id) {
        if (Dungeon.chapters.contains(id)) {
            return;
        }
        String text = (String)CHAPTERS.get(id);
        if (text != null) {
            WndStory wnd = new WndStory(text);
            wnd.delay = 0.6f;
            if (0.6f > 0.0f) {
                wnd.tf.visible = false;
                wnd.chrome.visible = false;
                wnd.shadow.visible = false;
            }
            Game.scene().add(wnd);
            Dungeon.chapters.add(id);
        }
    }

    static {
        CHAPTERS.put(0, "这座地牢正位于城市正下方，最上几层实际上就是城市的下水道系统。名义上它们仍属于城市的一部分，因此并不算十分致命。虽然没人会称这里为安全之地，但至少你暂时还不用面对邪恶的魔法。");
        CHAPTERS.put(1, "多年以前，这里曾修建了一座地下监狱，专门关押最穷凶极恶的罪犯。当时这看似是个绝妙的主意，因为此地确实极难逃脱。但很快，地底深处开始渗透出黑暗的瘴气，将囚犯与狱卒统统逼向疯狂。最终监狱被彻底废弃，只有一部分重刑犯仍被遗留并锁死在这里。");
        CHAPTERS.put(2, "在废弃监狱下方延伸开的洞窟人迹罕至。它们太深，城市无力开发；矿藏又太过贫瘠，无法吸引矮人的兴趣。过去在连接两座城邦的商路上曾设有贸易前哨，但在矮人都城衰落之后也随之消亡。如今这里只剩下无处不在的豺狼人与地下野兽。");
        CHAPTERS.put(3, "矮人都城曾是矮人诸邦中最辉煌的城邦。在鼎盛时期，矮人的机械大军曾成功击退古神及其恶魔军团的入侵。但据说凯旋的战士们带回了腐化的种子，而那场胜利，正是这座地下王国走向覆灭的开端。");
        CHAPTERS.put(4, "过去，这些楼层曾是矮人都城的外围区域。在与古神的惨烈战争后，精疲力竭的矮人再也无力清剿残余的恶魔。久而久之，恶魔彻底掌控了这里，如今人们称之为恶魔大厅。\n\n极少有冒险者能够深入至此……");
    }
}

