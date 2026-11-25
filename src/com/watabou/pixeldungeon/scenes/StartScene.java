/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.scenes;

import com.watabou.input.NoosaInputProcessor;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Component;
import com.watabou.noosa.ui.Tooltip;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Challenges;
import com.watabou.pixeldungeon.Chrome;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.GamesInProgress;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.effects.BadgeBanner;
import com.watabou.pixeldungeon.effects.BitmaskEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.scenes.InterlevelScene;
import com.watabou.pixeldungeon.scenes.IntroScene;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.scenes.TitleScene;
import com.watabou.pixeldungeon.ui.Archs;
import com.watabou.pixeldungeon.ui.Button;
import com.watabou.pixeldungeon.ui.CheckBox;
import com.watabou.pixeldungeon.ui.ExitButton;
import com.watabou.pixeldungeon.ui.RedButton;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndBadge;
import com.watabou.pixeldungeon.windows.WndChallenges;
import com.watabou.pixeldungeon.windows.WndClass;
import com.watabou.pixeldungeon.windows.WndOptions;
import com.watabou.utils.Callback;
import com.watabou.utils.Highlighter;
import java.util.HashMap;
import java.util.Locale;

public class StartScene
extends PixelScene {
    private static final float BUTTON_HEIGHT = 24.0f;
    private static final float MARGIN = 2.0f;
    private static final float GAP = 2.0f;
    private static final String TXT_TITLE = "选择你的英雄";
    private static final float WIDTH = 352.0f;
    private static final float HEIGHT = 200.0f;
    private static HashMap<HeroClass, ClassShield> shields = new HashMap();
    public static HeroClass curClass;
    private NewGamePane newGame;
    private LoadGamePane loadGame;

    @Override
    public void create() {
        super.create();
        Badges.loadGlobal();
        int w = Camera.main.width;
        int h = Camera.main.height;
        float width = 352.0f;
        float height = 200.0f;
        float left = ((float)w - width) / 2.0f;
        float top = ((float)h - height) / 2.0f;
        Archs archs = new Archs();
        archs.setSize(w, h);
        this.add(archs);
        float leftPaneW = width / 2.0f;
        float leftPaneH = height;
        BitmapText title = StartScene.createText(TXT_TITLE, 9.0f);
        title.hardlight(0xFFFF44);
        title.measure();
        title.x = StartScene.align(left + (leftPaneW - title.width()) / 2.0f);
        title.y = StartScene.align(top) + 9.0f;
        this.add(title);
        float shieldTop = title.y + title.height() + 4.0f;
        float shieldW = leftPaneW / 2.0f;
        float shieldH = (leftPaneH + top - shieldTop) / 2.0f;
        HeroClass[] classes = new HeroClass[]{HeroClass.WARRIOR, HeroClass.MAGE, HeroClass.ROGUE, HeroClass.HUNTRESS};
        int index = 0;
        for (HeroClass cl : classes) {
            ClassShield shield = new ClassShield(cl);
            shields.put(cl, shield);
            this.add(shield);
            shield.setPos(left + (float)(index % 2) * shieldW, shieldTop + (float)(index / 2) * shieldH);
            ++index;
        }
        float rightPaneW = width / 2.0f;
        float rightPaneH = height;
        NinePatch frame = Chrome.get(Chrome.Type.WINDOW);
        frame.size(rightPaneW, rightPaneH);
        frame.x = w / 2;
        frame.y = top;
        this.add(frame);
        this.newGame = new NewGamePane();
        this.newGame.setRect(frame.x + (float)frame.marginLeft(), frame.y + (float)frame.marginTop(), frame.innerWidth(), frame.innerHeight());
        this.add(this.newGame);
        this.loadGame = new LoadGamePane();
        this.loadGame.setRect(this.newGame.left(), this.newGame.top(), this.newGame.width(), this.newGame.height());
        this.add(this.loadGame);
        ExitButton btnExit = new ExitButton();
        btnExit.setPos((float)Camera.main.width - btnExit.width(), 0.0f);
        this.add(btnExit);
        curClass = null;
        this.updateClass(HeroClass.values()[PixelDungeon.lastClass()]);
        this.fadeIn();
        Badges.loadingListener = new Callback(){

            @Override
            public void call() {
                if (Game.scene() == StartScene.this) {
                    PixelDungeon.switchNoFade(StartScene.class);
                }
            }
        };
    }

    @Override
    public void destroy() {
        Badges.saveGlobal();
        Badges.loadingListener = null;
        super.destroy();
    }

    private void updateClass(HeroClass cl) {
        if (curClass != null) {
            shields.get((Object)curClass).highlight(false);
        }
        curClass = cl;
        shields.get((Object)curClass).highlight(true);
        GamesInProgress.Info info = GamesInProgress.check(curClass);
        if (info != null) {
            this.newGame.active = false;
            this.newGame.visible = false;
            this.loadGame.active = true;
            this.loadGame.visible = true;
            this.loadGame.updateClass(cl, info);
        } else {
            this.newGame.active = true;
            this.newGame.visible = true;
            this.loadGame.active = false;
            this.loadGame.visible = false;
            this.newGame.updateClass(cl);
        }
    }

    private void startNewGame(boolean legacy) {
        Dungeon.hero = null;
        Dungeon.legacy = legacy;
        InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
        if (PixelDungeon.intro()) {
            PixelDungeon.intro(false);
            Game.switchScene(IntroScene.class);
        } else {
            Game.switchScene(InterlevelScene.class);
        }
    }

    @Override
    protected void onBackPressed() {
        PixelDungeon.switchNoFade(TitleScene.class);
    }

    private static class BadgeButton
    extends Button {
        private Badges.Badge badge;
        private Image icon;

        public BadgeButton(Badges.Badge badge) {
            this.badge = badge;
            this.active = badge != null;
            this.icon = BadgeBanner.image(badge != null ? badge.image : 56);
            this.add(this.icon);
            this.setSize(this.icon.width(), this.icon.height());
        }

        @Override
        protected void layout() {
            super.layout();
            this.icon.x = PixelScene.align(this.x + (this.width - this.icon.width()) / 2.0f);
            this.icon.y = PixelScene.align(this.y + (this.height - this.icon.height()) / 2.0f);
        }

        @Override
        protected void onClick() {
            Sample.INSTANCE.play("snd_click.mp3", 0.7f, 0.7f, 1.2f);
            Game.scene().add(new WndBadge(this.badge));
        }

        @Override
        protected String getTooltip() {
            return this.badge.name;
        }
    }

    private class LoadGamePane
    extends Component {
        private static final String TXT_TITLE = "%d级 %s";
        private static final String TXT_DEPTH = "%s，第 %d 层";
        private static final String TXT_BADGES = "徽章：";
        private static final String TXT_LOAD_GAME = "载入游戏";
        private static final String TXT_ABANDON_GAME = "放弃存档";
        private static final String TXT_REALLY = "你真的要放弃当前游戏吗？";
        private static final String TXT_WARNING = "你当前的冒险进度将被永久清除。";
        private static final String TXT_YES = "是的，放弃这局游戏";
        private static final String TXT_NO = "不，我改主意了";
        private static final String TXT_DESC_TOOLTIP = "点击查看该职业特性";
        private BitmapText className;
        private BitmapText progress;
        private BitmapText mode;
        private BitmapTextMultiline challenges;
        private Group badges;
        private TouchArea classHover;
        private RedButton btnLoad;
        private RedButton btnAbandon;

        private LoadGamePane() {
        }

        @Override
        protected void createChildren() {
            this.className = PixelScene.createText(9.0f);
            this.className.hardlight(0xFFFF44);
            this.add(this.className);
            this.classHover = new TouchArea(this.className){

                @Override
                protected void onClick(NoosaInputProcessor.Touch touch) {
                    StartScene.this.add(new WndClass(curClass));
                }

                @Override
                public boolean onMouseMove(int x, int y) {
                    Tooltip.show(this, LoadGamePane.TXT_DESC_TOOLTIP);
                    return true;
                }
            };
            this.add(this.classHover);
            this.progress = PixelScene.createText(9.0f);
            this.add(this.progress);
            this.mode = PixelScene.createText(9.0f);
            this.mode.hardlight(0xFFFF44);
            this.add(this.mode);
            this.challenges = PixelScene.createMultiline(9.0f);
            this.add(this.challenges);
            this.badges = new Group();
            this.add(this.badges);
            this.btnAbandon = new RedButton(TXT_ABANDON_GAME){

                @Override
                protected void onClick() {
                    StartScene.this.add(new WndOptions(LoadGamePane.TXT_REALLY, LoadGamePane.TXT_WARNING, new String[]{LoadGamePane.TXT_YES, LoadGamePane.TXT_NO}){

                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                GamesInProgress.delete(curClass);
                                Dungeon.deleteGame(curClass, true);
                                PixelDungeon.lastClass(curClass.ordinal());
                                PixelDungeon.switchNoFade(StartScene.class);
                            }
                        }
                    });
                }
            };
            this.add(this.btnAbandon);
            this.btnLoad = new RedButton(TXT_LOAD_GAME){

                @Override
                protected void onClick() {
                    InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
                    Game.switchScene(InterlevelScene.class);
                }
            };
            this.add(this.btnLoad);
        }

        @Override
        protected void layout() {
            this.className.x = this.x + 2.0f;
            this.className.y = this.y + 2.0f;
            this.progress.x = this.className.x;
            this.mode.x = this.className.x;
            this.challenges.x = this.className.x;
            this.challenges.maxWidth = (int)(this.width - 4.0f);
            this.btnAbandon.setRect(this.x + 2.0f, this.bottom() - 24.0f - 2.0f, this.width - 4.0f, 24.0f);
            this.btnLoad.setRect(this.btnAbandon.left(), this.btnAbandon.top() - 24.0f - 2.0f, this.btnAbandon.width(), 24.0f);
        }

        public void updateClass(HeroClass cl, GamesInProgress.Info info) {
            String title = Utils.format(TXT_TITLE, info.level, (info.subClass != HeroSubClass.NONE ? info.subClass.title() : cl.title()).toUpperCase(Locale.ENGLISH));
            this.className.text(title);
            this.className.measure();
            this.classHover.x = this.className.x;
            this.classHover.y = this.className.y + this.className.height();
            String depth = Utils.capitalize(Utils.format(TXT_DEPTH, Dungeon.layerName(Dungeon.layer(info.depth)), info.depth));
            this.progress.text(depth);
            this.progress.measure();
            this.progress.y = this.className.y + this.className.height();
            String modeInfo = (info.legacy ? "传统模式，" : "标准模式，") + (info.challenges > 0 ? "已开启挑战：" : "无挑战");
            this.mode.text(modeInfo);
            this.mode.measure();
            this.mode.y = this.progress.y + this.progress.height() + 4.0f;
            if (info.challenges > 0) {
                StringBuilder list = new StringBuilder();
                for (int i = 0; i < Challenges.NAMES.length; ++i) {
                    if ((info.challenges & 1 << i) <= 0) continue;
                    list.append(list.length() > 0 ? "、" : "");
                    list.append(Challenges.NAMES[i]);
                }
                this.challenges.text(list.toString());
                this.challenges.measure();
                this.challenges.y = this.mode.y + this.mode.height();
                this.challenges.visible = true;
            } else {
                this.challenges.visible = false;
            }
            this.badges.clear();
            if (info.badges.size() > 0) {
                BitmapText label = PixelScene.createText(TXT_BADGES, 9.0f);
                label.hardlight(0xFFFF44);
                label.measure();
                label.x = this.className.x;
                label.y = (this.challenges.visible ? this.challenges.y + this.challenges.height() : this.mode.y + this.mode.height()) + 4.0f;
                this.badges.add(label);
                float xpos = label.x;
                float ypos = PixelScene.align(label.y + label.height()) + 2.0f;
                for (Badges.Badge badge : info.badges) {
                    BadgeButton button = new BadgeButton(badge);
                    if (xpos + button.width() > this.x + this.width) {
                        xpos = label.x;
                        ypos += button.height() + 2.0f;
                    }
                    button.setPos(xpos, ypos);
                    this.badges.add(button);
                    xpos += button.width() + 2.0f;
                }
            }
        }
    }

    private class NewGamePane
    extends Component {
        private static final String TXT_NEW_GAME = "踏上征途";
        private static final String TXT_LEGACY = "传统模式";
        private static final String TXT_LEGACY_TOOLTIP = "在此模式下装备不会因使用而磨损损坏，\n但无法获得徽章，成绩也不会计入排行榜。";
        private static final String TXT_CHALLENGES = "挑战";
        private static final String TXT_CHALLENGES_TOOLTIP = "使用任意职业通关一次游戏即可解锁\n\"挑战\"模式。";
        private static final String TXT_NO_MASTERY = "你尚未用该职业获得过精通之书。";
        private static final String TXT_UNLOCK = "使用任意其他职业击败第 3 位头目即可解锁该职业";
        private static final int THIN_BTN_HEIGHT = 18;
        private static final String DOT = "\u007f";
        private BitmapText className;
        private Group description;
        private CheckBox btnLegacy;
        private CheckBox btnChallenges;
        private RedButton btnStart;

        private NewGamePane() {
        }

        @Override
        protected void createChildren() {
            this.className = PixelScene.createText(9.0f);
            this.className.hardlight(0xFFFF44);
            this.add(this.className);
            this.btnLegacy = new CheckBox(TXT_LEGACY){

                @Override
                protected String getTooltip() {
                    return NewGamePane.TXT_LEGACY_TOOLTIP;
                }
            };
            this.add(this.btnLegacy);
            this.btnChallenges = new CheckBox(TXT_CHALLENGES){

                @Override
                protected String getTooltip() {
                    return NewGamePane.TXT_CHALLENGES_TOOLTIP;
                }

                @Override
                protected void onClick() {
                    if (Badges.isUnlocked(Badges.Badge.VICTORY)) {
                        StartScene.this.add(new WndChallenges(PixelDungeon.challenges(), true){

                            @Override
                            public void onBackPressed() {
                                super.onBackPressed();
                                NewGamePane.this.btnChallenges.checked(PixelDungeon.challenges() > 0);
                            }
                        });
                    } else {
                        this.checked(false);
                    }
                }
            };
            this.btnChallenges.checked(Badges.isUnlocked(Badges.Badge.VICTORY) && PixelDungeon.challenges() > 0);
            this.add(this.btnChallenges);
            this.btnStart = new RedButton(TXT_NEW_GAME){

                @Override
                protected void onClick() {
                    StartScene.this.startNewGame(NewGamePane.this.btnLegacy.checked());
                }
            };
            this.add(this.btnStart);
        }

        @Override
        protected void layout() {
            this.className.x = this.x + 2.0f;
            this.className.y = this.y + 2.0f;
            this.btnStart.setRect(this.x + 2.0f, this.bottom() - 24.0f - 2.0f, this.width - 4.0f, 24.0f);
            float w2 = (this.width - 6.0f) / 2.0f;
            this.btnLegacy.setSize(w2, 18.0f);
            this.btnLegacy.setRect(this.x + 2.0f, this.btnStart.top() - 2.0f - 18.0f, w2, 18.0f);
            this.btnChallenges.setSize(w2, 18.0f);
            this.btnChallenges.setRect(this.x + this.width - 2.0f - w2, this.btnLegacy.top(), w2, 18.0f);
        }

        public void updateClass(HeroClass cl) {
            Object text;
            this.className.text(cl.title().toUpperCase(Locale.ENGLISH));
            this.className.measure();
            if (this.description != null) {
                this.remove(this.description);
            }
            this.description = new Group();
            this.add(this.description);
            float dotWidth = 0.0f;
            String[] items = cl.perks();
            float pos = this.className.y + this.className.height() + 2.0f;
            for (int i = 0; i < items.length; ++i) {
                if (i > 0) {
                    pos += 2.0f;
                }
                BitmapText dot = PixelScene.createText(DOT, 6.0f);
                dot.x = this.className.x;
                dot.y = pos;
                if (dotWidth == 0.0f) {
                    dot.measure();
                    dotWidth = dot.width();
                }
                this.description.add(dot);
                BitmapTextMultiline item = PixelScene.createMultiline(items[i], 6.0f);
                item.x = dot.x + dotWidth;
                item.y = pos;
                item.maxWidth = (int)(this.x + this.width - 2.0f - item.x);
                item.measure();
                this.description.add(item);
                pos += item.height();
            }
            if (Badges.isUnlocked(cl.masteryBadge())) {
                text = null;
                switch (cl) {
                    case WARRIOR: {
                        text = HeroSubClass.GLADIATOR.desc() + "\n\n" + HeroSubClass.BERSERKER.desc();
                        break;
                    }
                    case MAGE: {
                        text = HeroSubClass.BATTLEMAGE.desc() + "\n\n" + HeroSubClass.WARLOCK.desc();
                        break;
                    }
                    case ROGUE: {
                        text = HeroSubClass.FREERUNNER.desc() + "\n\n" + HeroSubClass.ASSASSIN.desc();
                        break;
                    }
                    case HUNTRESS: {
                        text = HeroSubClass.SNIPER.desc() + "\n\n" + HeroSubClass.WARDEN.desc();
                    }
                }
                Highlighter hl = new Highlighter((String)text);
                BitmapTextMultiline normal = PixelScene.createMultiline(hl.text, 6.0f);
                normal.maxWidth = (int)(this.width - 4.0f);
                normal.measure();
                normal.x = this.className.x;
                normal.y = pos + 4.0f;
                this.description.add(normal);
                if (hl.isHighlighted()) {
                    normal.mask = hl.inverted();
                    BitmapTextMultiline highlighted = PixelScene.createMultiline(hl.text, 6.0f);
                    highlighted.maxWidth = normal.maxWidth;
                    highlighted.measure();
                    highlighted.x = normal.x;
                    highlighted.y = normal.y;
                    this.description.add(highlighted);
                    highlighted.mask = hl.mask;
                    highlighted.hardlight(0xFFFF44);
                }
            } else {
                BitmapTextMultiline mastery = PixelScene.createMultiline(TXT_NO_MASTERY, 6.0f);
                mastery.hardlight(0xFFFF44);
                mastery.maxWidth = (int)(this.width - 4.0f);
                mastery.measure();
                mastery.x = this.className.x;
                mastery.y = pos + 4.0f;
                this.description.add(mastery);
            }
            if (cl == HeroClass.HUNTRESS && !Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_3)) {
                BitmapTextMultiline unlockText = PixelScene.createMultiline(TXT_UNLOCK, 6.0f);
                unlockText.maxWidth = (int)(this.width - 4.0f);
                unlockText.measure();
                pos = unlockText.height() < this.btnStart.height() ? this.btnStart.top() + (this.btnStart.height() - unlockText.height()) / 2.0f : this.btnStart.bottom() - unlockText.height();
                for (BitmapText line : unlockText.new LineSplitter().split()) {
                    line.measure();
                    line.hardlight(0xFFFF00);
                    line.x = PixelScene.align(this.x + (this.width - line.width()) / 2.0f);
                    line.y = PixelScene.align(pos);
                    this.description.add(line);
                    pos += line.height();
                }
                this.btnLegacy.visible = false;
                this.btnChallenges.visible = false;
                this.btnStart.visible = false;
            } else {
                this.btnLegacy.visible = true;
                this.btnChallenges.visible = true;
                this.btnStart.visible = true;
            }
        }
    }

    private class ClassShield
    extends Button {
        private static final float MIN_BRIGHTNESS = 0.35f;
        private static final int WIDTH = 24;
        private static final int HEIGHT = 28;
        private static final int SCALE = 2;
        private HeroClass cl;
        private Image frame;
        private Image avatar;
        private BitmapText level;
        private Emitter emitter;
        private float brightness;

        public ClassShield(HeroClass cl) {
            this.cl = cl;
            this.avatar.frame(cl.ordinal() * 24, 0, 24, 28);
            GamesInProgress.Info info = GamesInProgress.check(cl);
            if (info != null) {
                this.level.text(Integer.toString(info.level));
                this.level.measure();
            }
            this.emitter = new BitmaskEmitter(this.avatar);
            this.addToBack(this.emitter);
            this.addToBack(this.frame);
            this.brightness = 0.35f;
            this.updateBrightness();
        }

        @Override
        protected void createChildren() {
            super.createChildren();
            this.frame = new Image("class_frame.png");
            this.avatar = new Image("avatars.png");
            this.avatar.scale.set(2.0f);
            this.add(this.avatar);
            this.level = new BitmapText(PixelScene.font1x);
            this.level.hardlight(16772004);
            this.add(this.level);
            this.width = this.frame.width;
            this.height = this.frame.height;
        }

        @Override
        protected void layout() {
            super.layout();
            this.frame.x = PixelScene.align(this.x);
            this.frame.y = PixelScene.align(this.y);
            this.avatar.x = PixelScene.align(this.x + (this.width - this.avatar.width()) / 2.0f);
            this.avatar.y = PixelScene.align(this.y + (this.height - this.avatar.height()) / 2.0f);
            this.level.x = PixelScene.align(this.frame.x + 73.5f - this.level.width() / 2.0f);
            this.level.y = this.frame.y + 3.0f;
        }

        @Override
        protected void onTouchDown() {
            Sample.INSTANCE.play("snd_click.mp3", 1.0f, 1.2f, 0.0f);
            StartScene.this.updateClass(this.cl);
        }

        @Override
        public void update() {
            super.update();
            if (this.brightness < 1.0f && this.brightness > 0.35f) {
                if ((this.brightness -= Game.elapsed) <= 0.35f) {
                    this.brightness = 0.35f;
                }
                this.updateBrightness();
            }
        }

        public void highlight(boolean value) {
            if (value) {
                this.brightness = 1.0f;
                this.emitter.revive();
                this.emitter.start(Speck.factory(111), 0.15f, 0);
            } else {
                this.brightness = 0.999f;
                this.emitter.on = false;
            }
            this.updateBrightness();
        }

        private void updateBrightness() {
            this.avatar.bm = this.avatar.rm = this.brightness;
            this.avatar.gm = this.avatar.rm;
        }

        @Override
        protected String getTooltip() {
            return "(cB)" + Utils.capitalize(this.cl.title());
        }
    }
}

