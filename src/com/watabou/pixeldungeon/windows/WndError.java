/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.windows;

import com.watabou.pixeldungeon.ui.Icons;
import com.watabou.pixeldungeon.windows.WndTitledMessage;

public class WndError
extends WndTitledMessage {
    private static final String TXT_TITLE = "错误";

    public WndError(String message) {
        super(Icons.WARNING.get(), TXT_TITLE, message);
    }
}

