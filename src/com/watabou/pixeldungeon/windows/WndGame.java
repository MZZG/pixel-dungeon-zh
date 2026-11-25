/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.scenes.InterlevelScene;
import com.watabou.pixeldungeon.scenes.RankingsScene;
import com.watabou.pixeldungeon.scenes.TitleScene;
import com.watabou.pixeldungeon.ui.Icons;
import com.watabou.pixeldungeon.ui.RedButton;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.windows.WndChallenges;
import com.watabou.pixeldungeon.windows.WndSettings;
import java.io.IOException;

public class WndGame
extends Window {
    private static final String TXT_SETTINGS = "设置";
    private static final String TXT_CHALLEGES = "挑战";
    private static final String TXT_RANKINGS = "排行榜";
    private static final String TXT_START = "踏上征途";
    private static final String TXT_MENU = "主菜单";
    private static final String TXT_EXIT = "退出游戏";
    private static final String TXT_RETURN = "继续冒险";
    private static final int WIDTH = 120;
    private static final int BTN_HEIGHT = 20;
    private static final int GAP = 2;
    private int pos;

    public WndGame() {
        this.addButton(new RedButton(TXT_SETTINGS){

            @Override
            protected void onClick() {
                WndGame.this.hide();
                GameScene.show(new WndSettings(true));
            }
        });
        if (Dungeon.challenges > 0) {
            this.addButton(new RedButton(TXT_CHALLEGES){

                @Override
                protected void onClick() {
                    WndGame.this.hide();
                    GameScene.show(new WndChallenges(Dungeon.challenges, false));
                }
            });
        }
        if (!Dungeon.hero.isAlive()) {
            RedButton btnStart = new RedButton(TXT_START){

                @Override
                protected void onClick() {
                    Dungeon.hero = null;
                    PixelDungeon.challenges(Dungeon.challenges);
                    InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                    InterlevelScene.noStory = true;
                    Game.switchScene(InterlevelScene.class);
                }
            };
            this.addButton(btnStart);
            btnStart.icon(Icons.get(Dungeon.hero.heroClass));
            if (!Dungeon.legacy) {
                this.addButton(new RedButton(TXT_RANKINGS){

                    @Override
                    protected void onClick() {
                        InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                        Game.switchScene(RankingsScene.class);
                    }
                });
            }
        }
        this.addButtons(new RedButton(TXT_MENU){

            @Override
            protected void onClick() {
                try {
                    Dungeon.saveAll();
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                Game.switchScene(TitleScene.class);
            }
        }, new RedButton(TXT_EXIT){

            @Override
            protected void onClick() {
                if (SharedLibraryLoader.isIos) {
                    Game.scene().pause();
                }
                Game.instance.finish();
            }
        });
        this.addButton(new RedButton(TXT_RETURN){

            @Override
            protected void onClick() {
                WndGame.this.hide();
            }
        });
        this.resize(120, this.pos);
    }

    private void addButton(RedButton btn) {
        this.add(btn);
        btn.setRect(0.0f, this.pos > 0 ? (float)(this.pos += 2) : 0.0f, 120.0f, 20.0f);
        this.pos += 20;
    }

    private void addButtons(RedButton btn1, RedButton btn2) {
        this.add(btn1);
        btn1.setRect(0.0f, this.pos > 0 ? (float)(this.pos += 2) : 0.0f, 59.0f, 20.0f);
        this.add(btn2);
        btn2.setRect(btn1.right() + 2.0f, btn1.top(), 120.0f - btn1.right() - 2.0f, 20.0f);
        this.pos += 20;
    }
}

