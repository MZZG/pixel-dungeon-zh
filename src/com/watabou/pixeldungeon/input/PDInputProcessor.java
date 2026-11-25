/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.watabou.input.NoosaControllerProcessor;
import com.watabou.input.NoosaInputProcessor;
import com.watabou.pixeldungeon.input.GameAction;
import java.util.Map;

public abstract class PDInputProcessor
extends NoosaInputProcessor<GameAction> {
    public String keycodeToName(int keycode) {
        if (keycode < 4096) {
            return Input.Keys.toString(keycode);
        }
        if (keycode < 8192) {
            int button = 0xFFF & keycode;
            switch (button) {
                case 0: {
                    return "鼠标左键";
                }
                case 1: {
                    return "鼠标右键";
                }
                case 2: {
                    return "鼠标中键";
                }
            }
            return "鼠标按键 " + button;
        }
        int button = 0x1FFF & keycode;
        NoosaControllerProcessor controllerProcessor = this.getControllerProcessor();
        if (controllerProcessor != null) {
            return controllerProcessor.buttonName(button);
        }
        return "手柄按键 " + button;
    }

    public Map<Integer, GameActionWrapper> getKeyMappings() {
        return null;
    }

    public void resetKeyMappings() {
    }

    public GameActionWrapper setKeyMapping(GameAction action, boolean defaultKey, int code) {
        return null;
    }

    public GameActionWrapper removeKeyMapping(GameAction action, boolean defaultKey, int code) {
        return null;
    }

    @Override
    protected GameAction keycodeToGameAction(int keycode) {
        switch (keycode) {
            case 4: 
            case 131: {
                return GameAction.BACK;
            }
        }
        return null;
    }

    public int getKeyForAction(GameAction action) {
        for (Map.Entry<Integer, GameActionWrapper> entry : this.getKeyMappings().entrySet()) {
            GameActionWrapper value = entry.getValue();
            if (!value.defaultKey || value.gameAction != action) continue;
            return entry.getKey();
        }
        return -1;
    }

    public boolean isKeyPressed(GameAction action) {
        for (Map.Entry<Integer, GameActionWrapper> entry : this.getKeyMappings().entrySet()) {
            if (entry.getValue().gameAction != action || !Gdx.input.isKeyPressed(entry.getKey())) continue;
            return true;
        }
        return false;
    }

    public static final class GameActionWrapper {
        public final boolean defaultKey;
        public final GameAction gameAction;

        public GameActionWrapper(GameAction gameAction, boolean defaultKey) {
            this.defaultKey = defaultKey;
            this.gameAction = gameAction;
        }
    }
}

