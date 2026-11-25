/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.ui;

import com.watabou.noosa.Image;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Belongings;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.input.GameAction;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.Button;
import com.watabou.pixeldungeon.ui.HealthIndicator;
import com.watabou.pixeldungeon.ui.Icons;
import com.watabou.pixeldungeon.ui.ItemSlot;
import com.watabou.pixeldungeon.windows.WndBag;
import com.watabou.utils.Bundle;

public class QuickSlot
extends Button
implements WndBag.Listener {
    private static final String TXT_SELECT_ITEM = "选择要放入快捷栏的物品";
    private static final String TXT_TOOLTIP = "点击为此快捷栏\n指定一件物品";
    private static QuickSlot primary;
    private static QuickSlot secondary;
    private Item itemInSlot;
    private ItemSlot slot;
    private Image crossB;
    private Image crossM;
    private boolean targeting = false;
    private static Char lastTarget;
    public static Object primaryValue;
    public static Object secondaryValue;
    private static final String QUICKSLOT1 = "quickslot";
    private static final String QUICKSLOT2 = "quickslot2";

    public void primary() {
        primary = this;
        this.item(this.select());
        this.slot.hotKey = GameAction.QUICKSLOT_PRIMARY;
    }

    public void secondary() {
        secondary = this;
        this.item(this.select());
        this.slot.hotKey = GameAction.QUICKSLOT_SECONDARY;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (this == primary) {
            primary = null;
        } else {
            secondary = null;
        }
        lastTarget = null;
    }

    @Override
    protected void createChildren() {
        super.createChildren();
        this.slot = new ItemSlot(2.0f){

            @Override
            protected void onClick() {
                if (QuickSlot.this.targeting) {
                    GameScene.handleCell(lastTarget.pos);
                } else {
                    QuickSlot.this.useTargeting();
                    QuickSlot.this.select().execute(Dungeon.hero);
                }
            }

            @Override
            protected boolean onLongClick() {
                return QuickSlot.this.onLongClick();
            }

            @Override
            protected void onTouchDown() {
                this.icon.lightness(0.7f);
            }

            @Override
            protected void onTouchUp() {
                this.icon.resetColor();
            }
        };
        this.add(this.slot);
        this.crossB = Icons.TARGET.get();
        this.crossB.visible = false;
        this.add(this.crossB);
        this.crossM = new Image();
        this.crossM.copy(this.crossB);
    }

    @Override
    protected void layout() {
        super.layout();
        this.slot.fill(this);
        this.crossB.x = PixelScene.align(this.x + (this.width - this.crossB.width) / 2.0f);
        this.crossB.y = PixelScene.align(this.y + (this.height - this.crossB.height) / 2.0f);
    }

    @Override
    protected void onClick() {
        GameScene.selectItem(this, WndBag.Mode.QUICKSLOT, TXT_SELECT_ITEM);
    }

    @Override
    protected boolean onLongClick() {
        GameScene.selectItem(this, WndBag.Mode.QUICKSLOT, TXT_SELECT_ITEM);
        return true;
    }

    private Item select() {
        Object content;
        Object object = content = this == primary ? primaryValue : secondaryValue;
        if (content instanceof Item) {
            return (Item)content;
        }
        if (content != null) {
            Object item = Dungeon.hero.belongings.getItem((Class)content);
            return item != null ? (Item)item : Item.virtual((Class)content);
        }
        return null;
    }

    @Override
    protected String getTooltip() {
        return TXT_TOOLTIP;
    }

    @Override
    public void onSelect(Item item) {
        if (item != null) {
            if (this == primary) {
                primaryValue = item.stackable ? item.getClass() : item;
            } else {
                secondaryValue = item.stackable ? item.getClass() : item;
            }
            QuickSlot.refresh();
        }
    }

    public void item(Item item) {
        this.slot.item(item);
        this.itemInSlot = item;
        this.enableSlot();
    }

    public void enable(boolean value) {
        this.active = value;
        if (value) {
            this.enableSlot();
        } else {
            this.slot.enable(false);
        }
    }

    private void enableSlot() {
        this.slot.enable(this.itemInSlot != null && this.itemInSlot.quantity() > 0 && (Dungeon.hero.belongings.backpack.contains(this.itemInSlot) || this.itemInSlot.isEquipped(Dungeon.hero)));
    }

    private void useTargeting() {
        int pos;
        boolean bl = this.targeting = lastTarget != null && lastTarget.isAlive() && Dungeon.visible[QuickSlot.lastTarget.pos];
        if (this.targeting && (pos = Ballistica.cast(Dungeon.hero.pos, QuickSlot.lastTarget.pos, false, true)) != QuickSlot.lastTarget.pos) {
            lastTarget = null;
            this.targeting = false;
        }
        if (!this.targeting) {
            int n = Dungeon.hero.visibleEnemies();
            for (int i = 0; i < n; ++i) {
                Mob enemy = Dungeon.hero.visibleEnemy(i);
                int pos2 = Ballistica.cast(Dungeon.hero.pos, enemy.pos, false, true);
                if (pos2 != enemy.pos) continue;
                lastTarget = enemy;
                this.targeting = true;
                break;
            }
        }
        if (this.targeting) {
            if (Actor.all().contains(lastTarget)) {
                QuickSlot.lastTarget.sprite.parent.add(this.crossM);
                this.crossM.point(DungeonTilemap.tileToWorld(QuickSlot.lastTarget.pos));
                this.crossB.visible = true;
            } else {
                lastTarget = null;
            }
        }
    }

    public static void refresh() {
        if (primary != null) {
            primary.item(primary.select());
        }
        if (secondary != null) {
            secondary.item(secondary.select());
        }
    }

    public static void target(Item item, Char target) {
        if (target != Dungeon.hero) {
            lastTarget = target;
            HealthIndicator.instance.target(target);
        }
    }

    public static void cancel() {
        if (primary != null && QuickSlot.primary.targeting) {
            QuickSlot.primary.crossB.visible = false;
            QuickSlot.primary.crossM.remove();
            QuickSlot.primary.targeting = false;
        }
        if (secondary != null && QuickSlot.secondary.targeting) {
            QuickSlot.secondary.crossB.visible = false;
            QuickSlot.secondary.crossM.remove();
            QuickSlot.secondary.targeting = false;
        }
    }

    public static void save(Bundle bundle) {
        Belongings stuff = Dungeon.hero.belongings;
        if (primaryValue instanceof Class && stuff.getItem((Class)primaryValue) != null) {
            bundle.put(QUICKSLOT1, ((Class)primaryValue).getName());
        }
        if (secondaryValue instanceof Class && stuff.getItem((Class)secondaryValue) != null) {
            bundle.put(QUICKSLOT2, ((Class)secondaryValue).getName());
        }
    }

    public static void save(Bundle bundle, Item item) {
        if (item == primaryValue) {
            bundle.put(QUICKSLOT1, true);
        }
        if (item == secondaryValue) {
            bundle.put(QUICKSLOT2, true);
        }
    }

    public static void restore(Bundle bundle) {
        primaryValue = null;
        secondaryValue = null;
        String qsClass = bundle.getString(QUICKSLOT1);
        if (qsClass != null) {
            try {
                primaryValue = Class.forName(qsClass);
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
        }
        if ((qsClass = bundle.getString(QUICKSLOT2)) != null) {
            try {
                secondaryValue = Class.forName(qsClass);
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
        }
    }

    public static void restore(Bundle bundle, Item item) {
        if (bundle.getBoolean(QUICKSLOT1)) {
            primaryValue = item;
        }
        if (bundle.getBoolean(QUICKSLOT2)) {
            secondaryValue = item;
        }
    }

    public static void compress() {
        if (primaryValue == null && secondaryValue != null || primaryValue == secondaryValue) {
            primaryValue = secondaryValue;
            secondaryValue = null;
        }
    }

    static {
        lastTarget = null;
    }
}

