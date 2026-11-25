/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Group;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.HighlightedText;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndTabbed;

public class WndClass
extends WndTabbed {
    private static final String TXT_MASTERY = "专精";
    private static final int WIDTH = 110;
    private static final int TAB_WIDTH = 48;
    private HeroClass cl;
    private PerksTab tabPerks;
    private MasteryTab tabMastery;

    public WndClass(HeroClass cl) {
        this.cl = cl;
        this.tabPerks = new PerksTab();
        this.add(this.tabPerks);
        RankingTab tab = new RankingTab(Utils.capitalize(cl.title()), this.tabPerks);
        tab.setSize(48.0f, this.tabHeight());
        this.add(tab);
        if (Badges.isUnlocked(cl.masteryBadge())) {
            this.tabMastery = new MasteryTab();
            this.add(this.tabMastery);
            tab = new RankingTab(TXT_MASTERY, this.tabMastery);
            tab.setSize(48.0f, this.tabHeight());
            this.add(tab);
            this.resize((int)Math.max(this.tabPerks.width, this.tabMastery.width), (int)Math.max(this.tabPerks.height, this.tabMastery.height));
        } else {
            this.resize((int)this.tabPerks.width, (int)this.tabPerks.height);
        }
        this.select(0);
    }

    private class MasteryTab
    extends Group {
        private static final int MARGIN = 2;
        public float height;
        public float width;

        public MasteryTab() {
            String message = null;
            switch (WndClass.this.cl) {
                case WARRIOR: {
                    message = HeroSubClass.GLADIATOR.desc() + "\n\n" + HeroSubClass.BERSERKER.desc();
                    break;
                }
                case MAGE: {
                    message = HeroSubClass.BATTLEMAGE.desc() + "\n\n" + HeroSubClass.WARLOCK.desc();
                    break;
                }
                case ROGUE: {
                    message = HeroSubClass.FREERUNNER.desc() + "\n\n" + HeroSubClass.ASSASSIN.desc();
                    break;
                }
                case HUNTRESS: {
                    message = HeroSubClass.SNIPER.desc() + "\n\n" + HeroSubClass.WARDEN.desc();
                }
            }
            HighlightedText text = new HighlightedText(6.0f);
            text.text(message, 106);
            text.setPos(2.0f, 2.0f);
            this.add(text);
            this.height = text.bottom() + 2.0f;
            this.width = text.right() + 2.0f;
        }
    }

    private class PerksTab
    extends Group {
        private static final int MARGIN = 2;
        private static final int GAP = 2;
        private static final String DOT = "\u007f";
        public float height;
        public float width;

        public PerksTab() {
            float dotWidth = 0.0f;
            String[] items = WndClass.this.cl.perks();
            float pos = 2.0f;
            for (int i = 0; i < items.length; ++i) {
                if (i > 0) {
                    pos += 2.0f;
                }
                BitmapText dot = PixelScene.createText(DOT, 6.0f);
                dot.x = 2.0f;
                dot.y = pos;
                if (dotWidth == 0.0f) {
                    dot.measure();
                    dotWidth = dot.width();
                }
                this.add(dot);
                BitmapTextMultiline item = PixelScene.createMultiline(items[i], 6.0f);
                item.x = dot.x + dotWidth;
                item.y = pos;
                item.maxWidth = (int)(106.0f - dotWidth);
                item.measure();
                this.add(item);
                pos += item.height();
                float w = item.width();
                if (!(w > this.width)) continue;
                this.width = w;
            }
            this.width += 2.0f + dotWidth;
            this.height = pos + 2.0f;
        }
    }

    private class RankingTab
    extends WndTabbed.LabeledTab {
        private Group page;

        public RankingTab(String label, Group page) {
            super(label);
            this.page = page;
        }

        @Override
        protected void select(boolean value) {
            super.select(value);
            if (this.page != null) {
                this.page.visible = this.page.active = this.selected;
            }
        }
    }
}

