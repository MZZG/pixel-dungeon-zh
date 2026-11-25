/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.input.NoosaInputProcessor;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Game;
import com.watabou.noosa.ui.Component;
import com.watabou.pixeldungeon.input.GameAction;
import com.watabou.pixeldungeon.input.PDInputProcessor;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.RedButton;
import com.watabou.pixeldungeon.ui.ScrollPane;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.windows.WndMessage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class WndKeymap
extends Window {
    private static final int MARGIN = 0;
    private static final float ITEM_HEIGHT = 12.0f;
    private static final int BTN_HEIGHT = 20;
    public static final String TXT_UNASSIGNED = "<未绑定>";
    private Component listContent;
    private int tempPos = 0;
    private final Map<GameAction, ListItem> items = new HashMap<GameAction, ListItem>(32);

    public WndKeymap() {
        this.resize(210, 190);
        RedButton btnReset = new RedButton("恢复默认设置"){

            @Override
            protected void onClick() {
                WndKeymap.this.resetToDefaults();
                WndKeymap.this.populateList();
            }
        };
        btnReset.setRect(0.0f, this.height - 20, this.width, 20.0f);
        this.add(btnReset);
        this.listContent = new Component();
        ScrollPane list = new ScrollPane(this.listContent){

            @Override
            public void onClick(float x, float y) {
                ListItem item;
                Iterator iterator = WndKeymap.this.items.values().iterator();
                while (iterator.hasNext() && !(item = (ListItem)iterator.next()).onClick(x, y)) {
                }
            }
        };
        this.populateList();
        this.add(list);
        list.setRect(0.0f, 0.0f, this.width, btnReset.top());
    }

    private void populateList() {
        this.listContent.clear();
        this.tempPos = 0;
        PDInputProcessor inputProcessor = (PDInputProcessor)Game.instance.getInputProcessor();
        Map<Integer, PDInputProcessor.GameActionWrapper> keyMappings = inputProcessor.getKeyMappings();
        TreeMap<GameAction, KeyPair> mappings = new TreeMap<GameAction, KeyPair>();
        for (GameAction action : GameAction.values()) {
            if (action.getDescription() == null) continue;
            mappings.put(action, new KeyPair());
        }
        for (Map.Entry entry : keyMappings.entrySet()) {
            Integer key = (Integer)entry.getKey();
            PDInputProcessor.GameActionWrapper value = (PDInputProcessor.GameActionWrapper)entry.getValue();
            GameAction action = value.gameAction;
            KeyPair keyPair = (KeyPair)mappings.get((Object)action);
            if (value.defaultKey) {
                keyPair.key1 = key;
                continue;
            }
            keyPair.key2 = key;
        }
        for (Map.Entry entry : mappings.entrySet()) {
            this.addKey(this.listContent, this.width, entry);
        }
        this.listContent.setSize(0.0f, this.tempPos);
    }

    private void addKey(Component content, int maxWidth, Map.Entry<GameAction, KeyPair> entry) {
        GameAction action = entry.getKey();
        ListItem keyItem = new ListItem(action, entry.getValue());
        keyItem.setRect(0.0f, this.tempPos, maxWidth, 12.0f);
        this.tempPos = (int)((float)this.tempPos + 12.0f);
        content.add(keyItem);
        this.items.put(action, keyItem);
    }

    private void resetToDefaults() {
        ((PDInputProcessor)Game.instance.getInputProcessor()).resetKeyMappings();
    }

    private class ListItem
    extends Component {
        private static final int BOUND = 0xFFFF44;
        private static final int NOT_BOUND = 13291458;
        private final GameAction gameAction;
        private final KeyPair keys;
        private BitmapText action;
        private BitmapText key1;
        private BitmapText key2;

        public ListItem(GameAction gameAction, KeyPair keys) {
            this.gameAction = gameAction;
            this.keys = keys;
            this.action.text(gameAction.getDescription());
            this.action.measure();
            this.reconfigureKeysText();
        }

        @Override
        protected void createChildren() {
            this.action = PixelScene.createText(9.0f);
            this.add(this.action);
            this.key1 = PixelScene.createText(9.0f);
            this.add(this.key1);
            this.key2 = PixelScene.createText(9.0f);
            this.add(this.key2);
        }

        @Override
        protected void layout() {
            float ty = PixelScene.align(this.y + (this.height - this.action.baseLine()) / 2.0f);
            float w3 = this.width / 3.0f;
            this.action.x = 0.0f;
            this.action.y = ty;
            this.key1.x = PixelScene.align(w3 + (w3 - this.key1.width()) / 2.0f);
            this.key1.y = ty;
            this.key2.x = PixelScene.align(w3 * 2.0f + (w3 - this.key2.width()) / 2.0f);
            this.key2.y = ty;
        }

        private void reconfigureKeysText() {
            PDInputProcessor inputProcessor = (PDInputProcessor)Game.instance.getInputProcessor();
            if (this.keys.key1 > 0) {
                this.key1.text(inputProcessor.keycodeToName(this.keys.key1));
                this.key1.hardlight(0xFFFF44);
            } else {
                this.key1.text(WndKeymap.TXT_UNASSIGNED);
                this.key1.hardlight(13291458);
            }
            this.key1.measure();
            if (this.keys.key2 > 0) {
                this.key2.text(inputProcessor.keycodeToName(this.keys.key2));
                this.key2.hardlight(0xFFFF44);
            } else {
                this.key2.text(WndKeymap.TXT_UNASSIGNED);
                this.key2.hardlight(13291458);
            }
            this.key2.measure();
            this.layout();
        }

        public int getKey(boolean defaultKey) {
            if (defaultKey) {
                return this.keys.key1;
            }
            return this.keys.key2;
        }

        public void setKey(boolean defaultKey, int keycode) {
            if (defaultKey) {
                this.keys.key1 = keycode;
            } else {
                this.keys.key2 = keycode;
            }
            this.reconfigureKeysText();
        }

        public boolean onClick(float x, float y) {
            if (x < this.x || x > this.x + this.width || y < this.y || y > this.y + this.height) {
                return false;
            }
            final boolean defaultKey = x < this.width * 3.0f / 4.0f;
            String message = "请按下用于\"" + this.gameAction.getDescription() + "\"的按键，或按 " + ((PDInputProcessor)Game.instance.getInputProcessor()).keycodeToName(129) + " 清除绑定。";
            Game.scene().add(new WndMessage(message){

                @Override
                protected void onKeyDown(NoosaInputProcessor.Key key) {
                    PDInputProcessor inputProcessor = (PDInputProcessor)Game.instance.getInputProcessor();
                    int oldKeycode = ListItem.this.getKey(defaultKey);
                    inputProcessor.removeKeyMapping(ListItem.this.gameAction, defaultKey, oldKeycode);
                    if (key.code == 129) {
                        ListItem.this.setKey(defaultKey, 0);
                    } else {
                        ListItem.this.setKey(defaultKey, key.code);
                        PDInputProcessor.GameActionWrapper replacedAction = inputProcessor.setKeyMapping(ListItem.this.gameAction, defaultKey, key.code);
                        if (replacedAction != null) {
                            ListItem replacedItem = (ListItem)WndKeymap.this.items.get((Object)replacedAction.gameAction);
                            replacedItem.setKey(replacedAction.defaultKey, 0);
                        }
                    }
                    this.hide();
                }
            });
            return true;
        }
    }

    private static class KeyPair {
        public int key1;
        public int key2;

        private KeyPair() {
        }
    }
}

