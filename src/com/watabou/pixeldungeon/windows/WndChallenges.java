/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.noosa.BitmapText;
import com.watabou.pixeldungeon.Challenges;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.CheckBox;
import com.watabou.pixeldungeon.ui.Window;
import java.util.ArrayList;

public class WndChallenges
extends Window {
    private static final int WIDTH = 108;
    private static final int TTL_HEIGHT = 12;
    private static final int BTN_HEIGHT = 18;
    private static final int GAP = 1;
    private static final String TITLE = "挑战";
    private boolean editable;
    private ArrayList<CheckBox> boxes;

    public WndChallenges(int checked, boolean editable) {
        this.editable = editable;
        BitmapText title = PixelScene.createText(TITLE, 9.0f);
        title.hardlight(0xFFFF44);
        title.measure();
        title.x = PixelScene.align(this.camera, (108.0f - title.width()) / 2.0f);
        title.y = PixelScene.align(this.camera, (12.0f - title.height()) / 2.0f);
        this.add(title);
        this.boxes = new ArrayList();
        float pos = 12.0f;
        for (int i = 0; i < Challenges.NAMES.length; ++i) {
            CheckBox cb = new CheckBox(Challenges.NAMES[i]);
            cb.checked((checked & Challenges.MASKS[i]) != 0);
            cb.active = editable;
            if (i > 0) {
                pos += 1.0f;
            }
            cb.setRect(0.0f, pos, 108.0f, 18.0f);
            pos = cb.bottom();
            this.add(cb);
            this.boxes.add(cb);
        }
        this.resize(108, (int)pos);
    }

    @Override
    public void onBackPressed() {
        if (this.editable) {
            int value = 0;
            for (int i = 0; i < this.boxes.size(); ++i) {
                if (!this.boxes.get(i).checked()) continue;
                value |= Challenges.MASKS[i];
            }
            PixelDungeon.challenges(value);
        }
        super.onBackPressed();
    }
}

