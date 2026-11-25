package com.watabou.noosa;

import com.watabou.glwrap.Quad;
import com.watabou.utils.PointF;
import com.watabou.utils.RectF;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class BitmapTextMultiline extends BitmapText {
    public int maxWidth = Integer.MAX_VALUE;
    protected static final Pattern PARAGRAPH = Pattern.compile("\n");
    protected static final Pattern WORD = Pattern.compile("\\s+");
    protected float spaceSize;
    public int nLines = 0;
    public boolean[] mask;

    public BitmapTextMultiline(BitmapText.Font font) {
        this("", font);
    }

    public BitmapTextMultiline(String text, BitmapText.Font font) {
        super(text, font);
        this.spaceSize = font.width(font.get(' '));
    }

    private static boolean isCJK(char c) {
        return (c >= '\u4e00' && c <= '\u9fff')
            || (c >= '\u3400' && c <= '\u4dbf')
            || (c >= '\u3000' && c <= '\u303f')
            || (c >= '\uff00' && c <= '\uffef')
            || (c >= '\u2000' && c <= '\u206f')
            || (c >= '\uf900' && c <= '\ufaff');
    }

    private static boolean isOpeningPunct(char c) {
        return c == '（' || c == '《' || c == '【' || c == '「' || c == '『'
            || c == '“' || c == '‘' || c == '(' || c == '[' || c == '{';
    }

    private static boolean isClosingPunct(char c) {
        return c == '，' || c == '。' || c == '！' || c == '？' || c == '、'
            || c == '；' || c == '：' || c == '”' || c == '’' || c == '）'
            || c == '》' || c == '】' || c == '」' || c == '』' || c == '…'
            || c == ',' || c == '.' || c == '!' || c == '?' || c == ';'
            || c == ':' || c == ')' || c == ']' || c == '}' || c == '%';
    }

    private static boolean isSpace(char c) {
        return c == ' ' || c == '\t';
    }

    protected static final class Token {
        final String text;
        final boolean spaceAfter;

        Token(String text, boolean spaceAfter) {
            this.text = text;
            this.spaceAfter = spaceAfter;
        }
    }

    protected static ArrayList<Token> tokenizeParagraph(String p) {
        ArrayList<Token> tokens = new ArrayList<Token>();
        int n = p.length();
        int i = 0;
        while (i < n) {
            if (isSpace(p.charAt(i))) {
                ++i;
                continue;
            }
            StringBuilder sb = new StringBuilder();
            while (i < n && isOpeningPunct(p.charAt(i))) {
                sb.append(p.charAt(i++));
            }
            if (i < n && !isSpace(p.charAt(i))) {
                if (isCJK(p.charAt(i))) {
                    sb.append(p.charAt(i++));
                } else {
                    while (i < n && !isSpace(p.charAt(i)) && !isCJK(p.charAt(i))) {
                        sb.append(p.charAt(i++));
                    }
                }
            }
            while (i < n && isClosingPunct(p.charAt(i))) {
                sb.append(p.charAt(i++));
            }
            boolean spaceAfter = false;
            while (i < n && isSpace(p.charAt(i))) {
                spaceAfter = true;
                ++i;
            }
            if (sb.length() > 0) {
                tokens.add(new Token(sb.toString(), spaceAfter));
            }
        }
        return tokens;
    }

    @Override
    protected void updateVertices() {
        if (this.text == null) {
            this.text = "";
        }
        this.quads = Quad.createSet(this.text.length());
        this.realLength = 0;
        SymbolWriter writer = new SymbolWriter();
        PointF metrics = new PointF();
        String[] paragraphs = PARAGRAPH.split(this.text);
        int pos = 0;
        for (int i = 0; i < paragraphs.length; ++i) {
            ArrayList<Token> tokens = tokenizeParagraph(paragraphs[i]);
            for (int j = 0; j < tokens.size(); ++j) {
                Token tok = tokens.get(j);
                String word = tok.text;
                this.getWordMetrics(word, metrics);
                writer.addSymbol(metrics.x, metrics.y);
                int length = word.length();
                float shift = 0.0f;
                for (int k = 0; k < length; ++k) {
                    RectF rect = this.font.get(word.charAt(k));
                    if (rect != null) {
                        float w = this.font.width(rect);
                        float h = this.font.height(rect);
                        if (this.mask == null || (pos < this.mask.length && this.mask[pos])) {
                            this.vertices[0] = writer.x + shift;
                            this.vertices[1] = writer.y;
                            this.vertices[2] = rect.left;
                            this.vertices[3] = rect.top;
                            this.vertices[4] = writer.x + shift + w;
                            this.vertices[5] = writer.y;
                            this.vertices[6] = rect.right;
                            this.vertices[7] = rect.top;
                            this.vertices[8] = writer.x + shift + w;
                            this.vertices[9] = writer.y + h;
                            this.vertices[10] = rect.right;
                            this.vertices[11] = rect.bottom;
                            this.vertices[12] = writer.x + shift;
                            this.vertices[13] = writer.y + h;
                            this.vertices[14] = rect.left;
                            this.vertices[15] = rect.bottom;
                            this.quads.put(this.vertices);
                            ++this.realLength;
                        }
                        shift += w + this.font.tracking;
                    }
                    ++pos;
                }
                if (tok.spaceAfter && j + 1 < tokens.size()) {
                    writer.addSpace(this.spaceSize);
                }
            }
            writer.newLine(0.0f, this.font.lineHeight);
        }
        this.nLines = writer.nLines();
        this.dirty = false;
    }

    private void getWordMetrics(String word, PointF metrics) {
        float w = 0.0f;
        float h = 0.0f;
        int length = word.length();
        for (int i = 0; i < length; ++i) {
            RectF rect = this.font.get(word.charAt(i));
            if (rect == null) continue;
            w += this.font.width(rect) + (w > 0.0f ? this.font.tracking : 0.0f);
            h = Math.max(h, this.font.height(rect));
        }
        metrics.set(w, h);
    }

    @Override
    public void measure() {
        if (this.text == null) {
            this.text = "";
        }
        SymbolWriter writer = new SymbolWriter();
        PointF metrics = new PointF();
        String[] paragraphs = PARAGRAPH.split(this.text);
        for (int i = 0; i < paragraphs.length; ++i) {
            ArrayList<Token> tokens = tokenizeParagraph(paragraphs[i]);
            for (int j = 0; j < tokens.size(); ++j) {
                Token tok = tokens.get(j);
                this.getWordMetrics(tok.text, metrics);
                writer.addSymbol(metrics.x, metrics.y);
                if (tok.spaceAfter && j + 1 < tokens.size()) {
                    writer.addSpace(this.spaceSize);
                }
            }
            writer.newLine(0.0f, this.font.lineHeight);
        }
        this.width = writer.width;
        this.height = writer.height;
        this.nLines = writer.nLines();
    }

    @Override
    public float baseLine() {
        return (this.height - this.font.lineHeight + this.font.baseLine) * this.scale.y;
    }

    public class LineSplitter {
        private ArrayList<BitmapText> lines;
        private StringBuilder curLine;
        private float curLineWidth;
        private PointF metrics = new PointF();

        private void newLine(String str, float width) {
            BitmapText txt = new BitmapText(this.curLine.toString(), BitmapTextMultiline.this.font);
            txt.scale.set(BitmapTextMultiline.this.scale.x);
            this.lines.add(txt);
            this.curLine = new StringBuilder(str);
            this.curLineWidth = width;
        }

        private void append(String str, float width) {
            this.curLineWidth += (this.curLineWidth > 0.0f ? BitmapTextMultiline.this.font.tracking : 0.0f) + width;
            this.curLine.append(str);
        }

        public ArrayList<BitmapText> split() {
            this.lines = new ArrayList<BitmapText>();
            this.curLine = new StringBuilder();
            this.curLineWidth = 0.0f;
            float maxW = (float) BitmapTextMultiline.this.maxWidth / BitmapTextMultiline.this.scale.x;
            String[] paragraphs = PARAGRAPH.split(BitmapTextMultiline.this.text);
            for (int i = 0; i < paragraphs.length; ++i) {
                ArrayList<Token> tokens = tokenizeParagraph(paragraphs[i]);
                for (int j = 0; j < tokens.size(); ++j) {
                    Token tok = tokens.get(j);
                    String word = tok.text;
                    BitmapTextMultiline.this.getWordMetrics(word, this.metrics);
                    if (this.curLineWidth > 0.0f && this.curLineWidth + BitmapTextMultiline.this.font.tracking + this.metrics.x > maxW) {
                        this.newLine(word, this.metrics.x);
                    } else {
                        this.append(word, this.metrics.x);
                    }
                    if (tok.spaceAfter) {
                        if (this.curLineWidth > 0.0f && this.curLineWidth + BitmapTextMultiline.this.font.tracking + BitmapTextMultiline.this.spaceSize > maxW) {
                            this.newLine("", 0.0f);
                        } else {
                            this.append(" ", BitmapTextMultiline.this.spaceSize);
                        }
                    }
                }
                this.newLine("", 0.0f);
            }
            return this.lines;
        }
    }

    private class SymbolWriter {
        public float width = 0.0f;
        public float height = 0.0f;
        public int nLines = 0;
        public float lineWidth = 0.0f;
        public float lineHeight = 0.0f;
        public float x = 0.0f;
        public float y = 0.0f;

        private SymbolWriter() {
        }

        public void addSymbol(float w, float h) {
            if (this.lineWidth > 0.0f && this.lineWidth + BitmapTextMultiline.this.font.tracking + w > (float) BitmapTextMultiline.this.maxWidth / BitmapTextMultiline.this.scale.x) {
                this.newLine(w, h);
            } else {
                this.x = this.lineWidth;
                this.lineWidth += (this.lineWidth > 0.0f ? BitmapTextMultiline.this.font.tracking : 0.0f) + w;
                if (h > this.lineHeight) {
                    this.lineHeight = h;
                }
            }
        }

        public void addSpace(float w) {
            if (this.lineWidth > 0.0f && this.lineWidth + BitmapTextMultiline.this.font.tracking + w > (float) BitmapTextMultiline.this.maxWidth / BitmapTextMultiline.this.scale.x) {
                this.newLine(0.0f, 0.0f);
            } else {
                this.x = this.lineWidth;
                this.lineWidth += (this.lineWidth > 0.0f ? BitmapTextMultiline.this.font.tracking : 0.0f) + w;
            }
        }

        public void newLine(float w, float h) {
            this.height += this.lineHeight;
            if (this.width < this.lineWidth) {
                this.width = this.lineWidth;
            }
            this.lineWidth = w;
            this.lineHeight = h;
            this.x = 0.0f;
            this.y = this.height;
            ++this.nLines;
        }

        public int nLines() {
            return this.x == 0.0f ? this.nLines : this.nLines + 1;
        }
    }
}
