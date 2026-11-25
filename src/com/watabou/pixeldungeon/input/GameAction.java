/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.input;

public enum GameAction {
    BACK(null),
    HERO_INFO("英雄信息"),
    CATALOGUS("图鉴"),
    JOURNAL("日志"),
    REST("等待"),
    SEARCH("搜索"),
    CELL_INFO("查看格子"),
    RESUME("恢复行进"),
    BACKPACK("打开背包"),
    QUICKSLOT_PRIMARY("主快捷栏"),
    QUICKSLOT_SECONDARY("副快捷栏"),
    TAG_ATTACK("攻击"),
    TAG_DANGER("视野内敌人"),
    ZOOM_IN("放大视野"),
    ZOOM_OUT("缩小视野"),
    ZOOM_DEFAULT("默认缩放"),
    MOVE_UP("向上移动"),
    MOVE_DOWN("向下移动"),
    MOVE_LEFT("向左移动"),
    MOVE_RIGHT("向右移动"),
    MOVE_TOP_LEFT("向左上移动"),
    MOVE_TOP_RIGHT("向右上移动"),
    MOVE_BOTTOM_LEFT("向左下移动"),
    MOVE_BOTTOM_RIGHT("向右下移动"),
    OPERATE("调查脚下"),
    THROW("投掷物品"),
    DRINK("饮用药剂"),
    READ("阅读卷轴"),
    ZAP("释放法杖"),
    PLANT("种植种子"),
    UNKNOWN(null);

    private final String description;
    public static final GameAction[] MOVEMENT;

    private GameAction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public static int action2Offset(GameAction action) {
        switch (action) {
            case MOVE_UP: {
                return -32;
            }
            case MOVE_DOWN: {
                return 32;
            }
            case MOVE_LEFT: {
                return -1;
            }
            case MOVE_RIGHT: {
                return 1;
            }
            case MOVE_TOP_LEFT: {
                return -33;
            }
            case MOVE_TOP_RIGHT: {
                return -31;
            }
            case MOVE_BOTTOM_LEFT: {
                return 31;
            }
            case MOVE_BOTTOM_RIGHT: {
                return 33;
            }
        }
        return 0;
    }

    static {
        MOVEMENT = new GameAction[]{MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT, MOVE_TOP_LEFT, MOVE_TOP_RIGHT, MOVE_BOTTOM_LEFT, MOVE_BOTTOM_RIGHT};
    }
}

