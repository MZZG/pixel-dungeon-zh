package com.watabou.pixeldungeon.ui;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.ui.Component;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Signal;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class GameLog extends Component implements Signal.Listener<String> {
    private static final int MAX_LINES = 8;
    private static final Pattern PUNCTUATION = Pattern.compile(".*[.,;?! 。！？，；：”’…]$");
    private BitmapTextMultiline lastEntry;
    private int lastColor;
    private static ArrayList<Entry> entries = new ArrayList<Entry>();

    public GameLog() {
        GLog.update.add(this);
        this.recreateLines();
    }

    private void recreateLines() {
        for (Entry entry : entries) {
            this.lastEntry = PixelScene.createMultiline(entry.text, 6.0f);
            this.lastColor = entry.color;
            this.lastEntry.hardlight(this.lastColor);
            this.add(this.lastEntry);
        }
    }

    public void newLine() {
        this.lastEntry = null;
    }

    @Override
    public void onSignal(String text) {
        int color = 0xFFFFFF;
        if (text.startsWith("++ ")) {
            text = text.substring("++ ".length());
            color = 65280;
        } else if (text.startsWith("-- ")) {
            text = text.substring("-- ".length());
            color = 0xFF0000;
        } else if (text.startsWith("** ")) {
            text = text.substring("** ".length());
            color = 0xFF8800;
        } else if (text.startsWith("@@ ")) {
            text = text.substring("@@ ".length());
            color = 0xFFFF00;
        }
        text = Utils.capitalize(text) + (PUNCTUATION.matcher(text).matches() ? "" : "。");
        if (this.lastEntry != null && color == this.lastColor && this.lastEntry.nLines < 4) {
            String lastMessage = this.lastEntry.text();
            String sep = (lastMessage.length() == 0 || lastMessage.endsWith("。") || lastMessage.endsWith("！") || lastMessage.endsWith("？")) ? "" : " ";
            this.lastEntry.text(lastMessage.length() == 0 ? text : lastMessage + sep + text);
            this.lastEntry.measure();
            GameLog.entries.get(GameLog.entries.size() - 1).text = this.lastEntry.text();
        } else {
            this.lastEntry = PixelScene.createMultiline(text, 6.0f);
            this.lastEntry.hardlight(color);
            this.lastColor = color;
            this.add(this.lastEntry);
            entries.add(new Entry(text, color));
        }
        if (this.length > 0) {
            int nLines;
            do {
                nLines = 0;
                for (int i = 0; i < this.length; ++i) {
                    nLines += ((BitmapTextMultiline) this.members.get(i)).nLines;
                }
                if (nLines <= 8) continue;
                this.remove((Gizmo) this.members.get(0));
                entries.remove(0);
            } while (nLines > 8);
        }
        this.layout();
    }

    @Override
    protected void layout() {
        float pos = this.y;
        for (int i = this.length - 1; i >= 0; --i) {
            BitmapTextMultiline entry = (BitmapTextMultiline) this.members.get(i);
            entry.maxWidth = (int) this.width;
            entry.measure();
            entry.x = this.x;
            entry.y = pos - entry.height();
            pos -= entry.height();
        }
    }

    @Override
    public void destroy() {
        GLog.update.remove(this);
        super.destroy();
    }

    public static void wipe() {
        entries.clear();
    }

    private static class Entry {
        public String text;
        public int color;

        public Entry(String text, int color) {
            this.text = text;
            this.color = color;
        }
    }
}
