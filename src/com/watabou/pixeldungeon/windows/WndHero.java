/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.input.NoosaInputProcessor;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Group;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.input.GameAction;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.BadgesList;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndTabbed;
import java.util.Locale;

public class WndHero
extends WndTabbed {
    private static final String TXT_STATS = "属性";
    private static final String TXT_BADGES = "徽章";
    private static final String TXT_EXP = "经验";
    private static final String TXT_STR = "力量";
    private static final String TXT_HEALTH = "生命";
    private static final String TXT_DURATION = "游戏时长";
    private static final String TXT_SLAIN = "击杀敌人数";
    private static final String TXT_GOLD = "金币收集数";
    private static final String TXT_ANKHS = "十字架使用";
    private static final int WIDTH = 100;
    private static final int TAB_WIDTH = 40;
    private StatsTab stats = new StatsTab();
    private BadgesTab badges;

    public WndHero() {
        this.add(this.stats);
        WndTabbed.LabeledTab tab = new WndTabbed.LabeledTab(TXT_STATS){

            @Override
            protected void select(boolean value) {
                super.select(value);
                ((WndHero)WndHero.this).stats.visible = ((WndHero)WndHero.this).stats.active = this.selected;
            }
        };
        tab.setSize(40.0f, this.tabHeight());
        this.add(tab);
        this.resize(100, (int)this.stats.height);
        if (!Dungeon.legacy) {
            this.badges = new BadgesTab();
            this.add(this.badges);
            tab = new WndTabbed.LabeledTab(TXT_BADGES){

                @Override
                protected void select(boolean value) {
                    super.select(value);
                    ((WndHero)WndHero.this).badges.visible = ((WndHero)WndHero.this).badges.active = this.selected;
                }
            };
            tab.setSize(40.0f, this.tabHeight());
            this.add(tab);
        }
        this.select(0);
    }

    protected void onKeyUp(NoosaInputProcessor.Key key) {
        if (key.action == GameAction.HERO_INFO) {
            this.hide();
        } else {
            super.onKeyUp(key);
        }
    }

    private class BadgesTab
    extends Group {
        public BadgesTab() {
            this.camera = WndHero.this.camera;
            BadgesList list = new BadgesList(false);
            this.add(list);
            list.setSize(100.0f, WndHero.this.height);
        }
    }

    private class StatsTab
    extends Group {
        private static final String TXT_TITLE = "%d级 %s";
        private static final int GAP = 5;
        private float height;

        public StatsTab() {
            Hero hero = Dungeon.hero;
            BitmapText title = PixelScene.createText(Utils.format(TXT_TITLE, hero.lvl, hero.className()).toUpperCase(Locale.ENGLISH), 9.0f);
            title.hardlight(0xFFFF44);
            title.measure();
            this.add(title);
            this.height = title.y + title.height() + 5.0f;
            this.statSlot(WndHero.TXT_STR, hero.STR());
            this.statSlot(WndHero.TXT_HEALTH, hero.HP + "/" + hero.HT);
            this.statSlot(WndHero.TXT_EXP, hero.exp + "/" + hero.maxExp());
            this.height += 5.0f;
            this.statSlot(WndHero.TXT_DURATION, (int)Statistics.getFloat(Statistics.Type.DURATION));
            this.statSlot(WndHero.TXT_SLAIN, Statistics.getInt(Statistics.Type.SLAIN));
            this.statSlot(WndHero.TXT_GOLD, Statistics.getInt(Statistics.Type.GOLD));
            this.statSlot(WndHero.TXT_ANKHS, Statistics.getInt(Statistics.Type.ANKHS));
        }

        private void statSlot(String label, String value) {
            BitmapText txt = PixelScene.createText(label, 8.0f);
            txt.y = this.height;
            this.add(txt);
            txt = PixelScene.createText(value, 8.0f);
            txt.measure();
            txt.x = PixelScene.align(65.0f);
            txt.y = this.height;
            this.add(txt);
            this.height += 5.0f + txt.baseLine();
        }

        private void statSlot(String label, int value) {
            this.statSlot(label, Integer.toString(value));
        }

        public float height() {
            return this.height;
        }
    }
}

