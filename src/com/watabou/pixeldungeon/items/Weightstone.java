/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.RedButton;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.IconTitle;
import com.watabou.pixeldungeon.windows.WndBag;
import java.util.ArrayList;

public class Weightstone
extends Item {
    private static final String TXT_SELECT_WEAPON = "选择要配重的武器";
    private static final String TXT_FAST = "你调整了%s的配重，使其攻速更快";
    private static final String TXT_ACCURATE = "你调整了%s的配重，使其更加精准";
    private static final float TIME_TO_APPLY = 2.0f;
    private static final String AC_APPLY = "APPLY";
    private final WndBag.Listener itemSelector;

    public Weightstone() {
        this.name = "平衡石";
        this.image = 123;
        this.stackable = true;
        this.itemSelector = new WndBag.Listener(){

            @Override
            public void onSelect(Item item) {
                if (item != null) {
                    GameScene.show(new WndBalance((Weapon)item));
                }
            }
        };
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_APPLY);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action == AC_APPLY) {
            curUser = hero;
            GameScene.selectItem(this.itemSelector, WndBag.Mode.WEAPON, TXT_SELECT_WEAPON, "cursor_reforge.png");
        } else {
            super.execute(hero, action);
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    private void apply(Weapon weapon, boolean forSpeed) {
        this.detach(Weightstone.curUser.belongings.backpack);
        weapon.fix();
        if (forSpeed) {
            weapon.imbue = Weapon.Imbue.SPEED;
            GLog.p(TXT_FAST, weapon.name());
        } else {
            weapon.imbue = Weapon.Imbue.ACCURACY;
            GLog.p(TXT_ACCURATE, weapon.name());
        }
        Weightstone.curUser.sprite.operate(Weightstone.curUser.pos);
        Sample.INSTANCE.play("snd_miss.mp3");
        curUser.spend(2.0f);
        curUser.busy();
    }

    @Override
    public int price() {
        return 40 * this.quantity;
    }

    @Override
    public String info() {
        return "使用平衡石可以调整近战武器的配重，从而提升其攻击速度或命中精准度。";
    }

    public class WndBalance
    extends Window {
        private static final String TXT_CHOICE = "你想如何为%s配重？";
        private static final String TXT_SPEED = "追求攻速";
        private static final String TXT_ACCURACY = "追求精准";
        private static final String TXT_CANCEL = "算了";
        private static final int WIDTH = 120;
        private static final int MARGIN = 2;
        private static final int BUTTON_WIDTH = 116;
        private static final int BUTTON_HEIGHT = 20;

        public WndBalance(final Weapon weapon) {
            IconTitle titlebar = new IconTitle(weapon);
            titlebar.setRect(0.0f, 0.0f, 120.0f, 0.0f);
            this.add(titlebar);
            BitmapTextMultiline tfMesage = PixelScene.createMultiline(Utils.format(TXT_CHOICE, weapon.name()), 8.0f);
            tfMesage.maxWidth = 116;
            tfMesage.measure();
            tfMesage.x = 2.0f;
            tfMesage.y = titlebar.bottom() + 2.0f;
            this.add(tfMesage);
            float pos = tfMesage.y + tfMesage.height();
            if (weapon.imbue != Weapon.Imbue.SPEED) {
                RedButton btnSpeed = new RedButton(TXT_SPEED){

                    @Override
                    protected void onClick() {
                        WndBalance.this.hide();
                        Weightstone.this.apply(weapon, true);
                    }
                };
                btnSpeed.setRect(2.0f, pos + 2.0f, 116.0f, 20.0f);
                this.add(btnSpeed);
                pos = btnSpeed.bottom();
            }
            if (weapon.imbue != Weapon.Imbue.ACCURACY) {
                RedButton btnAccuracy = new RedButton(TXT_ACCURACY){

                    @Override
                    protected void onClick() {
                        WndBalance.this.hide();
                        Weightstone.this.apply(weapon, false);
                    }
                };
                btnAccuracy.setRect(2.0f, pos + 2.0f, 116.0f, 20.0f);
                this.add(btnAccuracy);
                pos = btnAccuracy.bottom();
            }
            RedButton btnCancel = new RedButton(TXT_CANCEL){

                @Override
                protected void onClick() {
                    WndBalance.this.hide();
                }
            };
            btnCancel.setRect(2.0f, pos + 2.0f, 116.0f, 20.0f);
            this.add(btnCancel);
            this.resize(120, (int)btnCancel.bottom() + 2);
        }

        protected void onSelect(int index) {
        }
    }
}

