/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.scenes;

import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.scenes.InterlevelScene;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.windows.WndStory;

public class IntroScene
extends PixelScene {
    private static final String TEXT = "在你之前，已有形形色色的英雄踏入过这座地牢。其中有些人带着财宝与魔法神器满载而归，但更多的人从此音讯全无。然而，从未有人成功取回传说藏在地牢最深处的Yendor护符。\n\n你觉得自己已经做好了迎接挑战的准备，更重要的是，你感到幸运女神正对你微笑。是时候开启属于你自己的冒险了！";

    @Override
    public void create() {
        super.create();
        this.add(new WndStory(TEXT){

            @Override
            public void hide() {
                super.hide();
                Game.switchScene(InterlevelScene.class);
            }
        });
        this.fadeIn();
    }
}

