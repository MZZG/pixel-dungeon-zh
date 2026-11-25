/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.ui;

import com.watabou.noosa.BitmapText;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.ui.Button;
import com.watabou.pixeldungeon.utils.Utils;

public class ItemSlot
extends Button {
    public static final int DEGRADED = 0xFF4444;
    public static final int UPGRADED = 0x44FF44;
    public static final int WARNING = 0xFF8800;
    private static final float ENABLED = 1.0f;
    private static final float DISABLED = 0.3f;
    protected ItemSprite icon;
    protected BitmapText topLeft;
    protected BitmapText topRight;
    protected BitmapText bottomRight;
    protected float margin = 0.0f;
    protected String tooltip;
    private static final String TXT_STRENGTH = ":%d";
    private static final String TXT_TYPICAL_STR = "%d?";
    private static final String TXT_LEVEL = "%+d";
    private static final String TXT_CURSED = "";
    public static final Item CHEST = new Item(){

        @Override
        public int image() {
            return 11;
        }

        @Override
        public String name() {
            return "宝箱";
        }
    };
    public static final Item LOCKED_CHEST = new Item(){

        @Override
        public int image() {
            return 12;
        }

        @Override
        public String name() {
            return "上锁的宝箱";
        }
    };
    public static final Item TOMB = new Item(){

        @Override
        public int image() {
            return 13;
        }

        @Override
        public String name() {
            return "坟墓";
        }
    };
    public static final Item SKELETON = new Item(){

        @Override
        public int image() {
            return 0;
        }

        @Override
        public String name() {
            return "遗骸";
        }
    };

    public ItemSlot(float margin) {
        this.margin = margin;
    }

    public ItemSlot() {
        this(0.0f);
    }

    public ItemSlot(Item item) {
        this(item, 0.0f);
    }

    public ItemSlot(Item item, float margin) {
        this(margin);
        this.item(item);
    }

    @Override
    protected void createChildren() {
        super.createChildren();
        this.icon = new ItemSprite();
        this.add(this.icon);
        this.topLeft = new BitmapText(PixelScene.font1x);
        this.add(this.topLeft);
        this.topRight = new BitmapText(PixelScene.font1x);
        this.add(this.topRight);
        this.bottomRight = new BitmapText(PixelScene.font1x);
        this.add(this.bottomRight);
    }

    @Override
    protected void layout() {
        super.layout();
        this.icon.x = this.x + (this.width - this.icon.width) / 2.0f;
        this.icon.y = this.y + (this.height - this.icon.height) / 2.0f;
        if (this.topLeft != null) {
            this.topLeft.x = this.x + this.margin;
            this.topLeft.y = this.y + this.margin;
        }
        if (this.topRight != null) {
            this.topRight.x = this.x + (this.width - this.topRight.width()) - this.margin;
            this.topRight.y = this.y + this.margin;
        }
        if (this.bottomRight != null) {
            this.bottomRight.x = this.x + (this.width - this.bottomRight.width()) - this.margin;
            this.bottomRight.y = this.y + (this.height - this.bottomRight.height()) - this.margin;
        }
    }

    public void item(Item item) {
        if (item == null) {
            this.active = false;
            this.bottomRight.visible = false;
            this.topRight.visible = false;
            this.topLeft.visible = false;
            this.icon.visible = false;
        } else {
            this.active = true;
            this.bottomRight.visible = true;
            this.topRight.visible = true;
            this.topLeft.visible = true;
            this.icon.visible = true;
            this.icon.view(item.image(), item.glowing());
            this.topLeft.text(item.status());
            boolean isArmor = item instanceof Armor;
            boolean isWeapon = item instanceof Weapon;
            if (isArmor || isWeapon) {
                if (item.levelKnown || isWeapon && !(item instanceof MeleeWeapon)) {
                    int str = isArmor ? ((Armor)item).STR : ((Weapon)item).STR;
                    this.topRight.text(Utils.format(TXT_STRENGTH, str));
                    if (str > Dungeon.hero.STR()) {
                        this.topRight.hardlight(0xFF4444);
                    } else {
                        this.topRight.resetColor();
                    }
                } else {
                    this.topRight.text(Utils.format(TXT_TYPICAL_STR, isArmor ? ((Armor)item).typicalSTR() : ((MeleeWeapon)item).typicalSTR()));
                    this.topRight.hardlight(0xFF8800);
                }
                this.topRight.measure();
            } else {
                this.topRight.text(null);
            }
            int level = item.visiblyUpgraded();
            if (level != 0 || item.cursed && item.cursedKnown) {
                this.bottomRight.text(item.levelKnown ? Utils.format(TXT_LEVEL, level) : TXT_CURSED);
                this.bottomRight.measure();
                this.bottomRight.hardlight(level > 0 ? (item.isBroken() ? 0xFF8800 : 0x44FF44) : 0xFF4444);
            } else {
                this.bottomRight.text(null);
            }
            this.tooltip = item.name();
            if (this.tooltip != null) {
                this.tooltip = Utils.capitalize(this.tooltip);
            }
            this.layout();
        }
    }

    public void enable(boolean value) {
        this.active = value;
        float alpha = value ? 1.0f : 0.3f;
        this.icon.alpha(alpha);
        this.topLeft.alpha(alpha);
        this.topRight.alpha(alpha);
        this.bottomRight.alpha(alpha);
    }

    public void showParams(boolean value) {
        if (value) {
            this.add(this.topRight);
            this.add(this.bottomRight);
        } else {
            this.remove(this.topRight);
            this.remove(this.bottomRight);
        }
    }

    @Override
    protected String getTooltip() {
        return this.tooltip;
    }
}

