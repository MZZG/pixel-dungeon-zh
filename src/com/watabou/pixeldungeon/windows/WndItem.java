/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.ui.RedButton;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.IconTitle;

public class WndItem
extends Window {
    private static final float BUTTON_WIDTH = 36.0f;
    private static final float BUTTON_HEIGHT = 16.0f;
    private static final float GAP = 2.0f;
    private static final int WIDTH = 120;

    public WndItem(final Window owner, final Item item) {
        IconTitle titlebar = new IconTitle();
        titlebar.icon(new ItemSprite(item.image(), item.glowing()));
        titlebar.label(Utils.capitalize(item.toString()));
        if (!Dungeon.legacy && item.isUpgradable() && item.levelKnown) {
            titlebar.health((float)item.durability() / (float)item.maxDurability());
        }
        titlebar.setRect(0.0f, 0.0f, 120.0f, 0.0f);
        this.add(titlebar);
        if (item.levelKnown) {
            if (item.level() < 0) {
                titlebar.color(0xFF4444);
            } else if (item.level() > 0) {
                titlebar.color(item.isBroken() ? 0xFF8800 : 0x44FF44);
            }
        }
        BitmapTextMultiline info = PixelScene.createMultiline(item.info(), 6.0f);
        info.maxWidth = 120;
        info.measure();
        info.x = titlebar.left();
        info.y = titlebar.bottom() + 2.0f;
        this.add(info);
        float y = info.y + info.height() + 2.0f;
        float x = 0.0f;
        if (Dungeon.hero.isAlive() && owner != null) {
            for (final String action : item.actions(Dungeon.hero)) {
                RedButton btn = new RedButton(actionLabel(action)){

                    @Override
                    protected void onClick() {
                        item.execute(Dungeon.hero, action);
                        WndItem.this.hide();
                        owner.hide();
                    }
                };
                btn.setSize(Math.max(36.0f, btn.reqWidth()), 16.0f);
                if (x + btn.width() > 120.0f) {
                    x = 0.0f;
                    y += 18.0f;
                }
                btn.setPos(x, y);
                this.add(btn);
                if (action == item.defaultAction) {
                    btn.textColor(0xFFFF44);
                }
                x += btn.width() + 2.0f;
            }
        }
        this.resize(120, (int)(y + (x > 0.0f ? 16.0f : 0.0f)));
    }

    private static String actionLabel(String action) {
        if ("DROP".equals(action)) return "丢弃";
        if ("THROW".equals(action)) return "投掷";
        if ("EQUIP".equals(action)) return "装备";
        if ("UNEQUIP".equals(action)) return "卸下";
        if ("DRINK".equals(action)) return "饮用";
        if ("READ".equals(action)) return "阅读";
        if ("ZAP".equals(action)) return "释放";
        if ("PLANT".equals(action)) return "种植";
        if ("EAT".equals(action)) return "食用";
        if ("APPLY".equals(action)) return "使用";
        if ("OPEN".equals(action)) return "打开";
        if ("LIGHT".equals(action)) return "点燃";
        if ("SHATTER".equals(action)) return "摔碎";
        if ("MERGE".equals(action)) return "合并";
        if ("MINE".equals(action)) return "采矿";
        if ("REFORGE".equals(action)) return "重铸";
        if ("DISENCHANT".equals(action)) return "祛魅";
        if ("SET".equals(action)) return "设置";
        if ("RETURN".equals(action)) return "返回";
        if ("END THE GAME".equals(action)) return "结束游戏";
        if ("HEROIC LEAP".equals(action)) return "英勇之跃";
        if ("MOLTEN EARTH".equals(action)) return "熔岩大地";
        if ("SMOKE BOMB".equals(action)) return "烟雾弹";
        if ("SPECTRAL BLADES".equals(action)) return "灵魂飞刃";
        return action;
    }
}

