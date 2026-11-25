/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.items.potions;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.Splash;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.ItemStatusHandler;
import com.watabou.pixeldungeon.items.potions.PotionOfExperience;
import com.watabou.pixeldungeon.items.potions.PotionOfFrost;
import com.watabou.pixeldungeon.items.potions.PotionOfHealing;
import com.watabou.pixeldungeon.items.potions.PotionOfInvisibility;
import com.watabou.pixeldungeon.items.potions.PotionOfLevitation;
import com.watabou.pixeldungeon.items.potions.PotionOfLiquidFlame;
import com.watabou.pixeldungeon.items.potions.PotionOfMight;
import com.watabou.pixeldungeon.items.potions.PotionOfMindVision;
import com.watabou.pixeldungeon.items.potions.PotionOfParalyticGas;
import com.watabou.pixeldungeon.items.potions.PotionOfPurity;
import com.watabou.pixeldungeon.items.potions.PotionOfStrength;
import com.watabou.pixeldungeon.items.potions.PotionOfToxicGas;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.windows.WndOptions;
import com.watabou.utils.Bundle;
import java.util.ArrayList;
import java.util.HashSet;

public class Potion
extends Item {
    public static final String AC_DRINK = "DRINK";
    private static final String TXT_HARMFUL = "有害药剂！";
    private static final String TXT_BENEFICIAL = "有益药剂";
    private static final String TXT_YES = "是的，我知道我在做什么";
    private static final String TXT_NO = "不，我改主意了";
    private static final String TXT_R_U_SURE_DRINK = "你确定要饮用它吗？大多数情况下你应该把它扔向敌人。";
    private static final String TXT_R_U_SURE_THROW = "你确定要投掷它吗？大多数情况下把它喝下去更有意义。";
    private static final float TIME_TO_DRINK = 1.0f;
    private static final Class<?>[] potions = new Class[]{PotionOfHealing.class, PotionOfExperience.class, PotionOfToxicGas.class, PotionOfLiquidFlame.class, PotionOfStrength.class, PotionOfParalyticGas.class, PotionOfLevitation.class, PotionOfMindVision.class, PotionOfPurity.class, PotionOfInvisibility.class, PotionOfMight.class, PotionOfFrost.class};
    private static final String[] colors = new String[]{"蓝绿", "绯红", "蔚蓝", "翡翠", "金黄", "洋红", "炭黑", "象牙", "琥珀", "赭褐", "靛青", "银白"};
    private static final Integer[] images = new Integer[]{56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67};
    private static ItemStatusHandler<Potion> handler;
    private String color;

    public static void initColors() {
        handler = new ItemStatusHandler(potions, colors, images);
    }

    public static void save(Bundle bundle) {
        handler.save(bundle);
    }

    public static void restore(Bundle bundle) {
        handler = new ItemStatusHandler(potions, colors, images, bundle);
    }

    public Potion() {
        this.stackable = true;
        this.defaultAction = AC_DRINK;
        this.image = handler.image(this);
        this.color = handler.label(this);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_DRINK);
        return actions;
    }

    @Override
    public void execute(final Hero hero, String action) {
        if (action.equals(AC_DRINK)) {
            if (this.isKnown() && (this instanceof PotionOfLiquidFlame || this instanceof PotionOfToxicGas || this instanceof PotionOfParalyticGas)) {
                GameScene.show(new WndOptions(TXT_HARMFUL, TXT_R_U_SURE_DRINK, new String[]{TXT_YES, TXT_NO}){

                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            Potion.this.drink(hero);
                        }
                    }
                });
            } else {
                this.drink(hero);
            }
        } else {
            super.execute(hero, action);
        }
    }

    @Override
    public void doThrow(final Hero hero) {
        if (this.isKnown() && (this instanceof PotionOfExperience || this instanceof PotionOfHealing || this instanceof PotionOfLevitation || this instanceof PotionOfMindVision || this instanceof PotionOfStrength || this instanceof PotionOfInvisibility || this instanceof PotionOfMight)) {
            GameScene.show(new WndOptions(TXT_BENEFICIAL, TXT_R_U_SURE_THROW, new String[]{TXT_YES, TXT_NO}){

                @Override
                protected void onSelect(int index) {
                    if (index == 0) {
                        Potion.super.doThrow(hero);
                    }
                }
            });
        } else {
            super.doThrow(hero);
        }
    }

    protected void drink(Hero hero) {
        this.detach(hero.belongings.backpack);
        hero.spend(1.0f);
        hero.busy();
        this.onThrow(hero.pos);
        Sample.INSTANCE.play("snd_drink.mp3");
        hero.sprite.operate(hero.pos);
    }

    @Override
    protected void onThrow(int cell) {
        if (Dungeon.hero.pos == cell) {
            this.apply(Dungeon.hero);
        } else if (Dungeon.level.map[cell] == 34 || Level.pit[cell]) {
            super.onThrow(cell);
        } else {
            this.shatter(cell);
        }
    }

    protected void apply(Hero hero) {
        this.shatter(hero.pos);
    }

    public void shatter(int cell) {
        if (Dungeon.visible[cell]) {
            GLog.i("药瓶碎裂了，" + this.color() + "液体无害地四散飞溅", new Object[0]);
            GameScene.play("snd_shatter.mp3", cell);
            this.splash(cell);
        }
    }

    public boolean isKnown() {
        return handler.isKnown(this);
    }

    public void setKnown() {
        if (!this.isKnown()) {
            handler.know(this);
        }
        Badges.validateAllPotionsIdentified();
    }

    @Override
    public Item identify() {
        this.setKnown();
        return this;
    }

    protected String color() {
        return this.color;
    }

    @Override
    public String name() {
        return this.isKnown() ? this.name : this.color + "药剂";
    }

    @Override
    public String info() {
        return this.isKnown() ? this.desc() : "这个药瓶里装满了旋转着的" + this.color + "液体。谁知道喝下或扔出它会发生什么？";
    }

    @Override
    public boolean isIdentified() {
        return this.isKnown();
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    public static HashSet<Class<? extends Potion>> getKnown() {
        return handler.known();
    }

    public static HashSet<Class<? extends Potion>> getUnknown() {
        return handler.unknown();
    }

    public static boolean allKnown() {
        return handler.known().size() == potions.length;
    }

    protected void splash(int cell) {
        int color = ItemSprite.pick(this.image, 8, 10);
        Splash.at(cell, color, 5);
    }

    @Override
    public int price() {
        return 20 * this.quantity;
    }
}

