package com.watabou.noosa;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;
import com.watabou.gdx.GdxTexture;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Matrix;
import com.watabou.glwrap.Quad;
import com.watabou.utils.RectF;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.FloatBuffer;

public class BitmapText extends Visual {
    protected String text;
    protected Font font;
    protected float[] vertices = new float[16];
    protected FloatBuffer quads;
    public int realLength;
    protected boolean dirty = true;

    public BitmapText() {
        this("", null);
    }

    public BitmapText(Font font) {
        this("", font);
    }

    public BitmapText(String text, Font font) {
        super(0.0f, 0.0f, 0.0f, 0.0f);
        this.text = text;
        this.font = font;
    }

    @Override
    public void destroy() {
        this.text = null;
        this.font = null;
        this.vertices = null;
        this.quads = null;
        super.destroy();
    }

    @Override
    protected void updateMatrix() {
        Matrix.setIdentity(this.matrix);
        Matrix.translate(this.matrix, this.x, this.y);
        Matrix.scale(this.matrix, this.scale.x, this.scale.y);
        Matrix.rotate(this.matrix, this.angle);
    }

    @Override
    public void draw() {
        super.draw();
        if (this.dirty) {
            this.updateVertices();
        }
        NoosaScript script = NoosaScript.get();
        this.font.texture.bind();
        script.camera(this.camera());
        script.uModel.valueM4(this.matrix);
        script.lighting(this.rm, this.gm, this.bm, this.am, this.ra, this.ga, this.ba, this.aa);
        script.drawQuadSet(this.quads, this.realLength);
    }

    protected void updateVertices() {
        this.width = 0.0f;
        this.height = 0.0f;
        if (this.text == null) {
            this.text = "";
        }
        this.quads = Quad.createSet(this.text.length());
        this.realLength = 0;
        int length = this.text.length();
        for (int i = 0; i < length; ++i) {
            RectF rect = this.font.get(this.text.charAt(i));
            if (rect == null) {
                continue;
            }
            float w = this.font.width(rect);
            float h = this.font.height(rect);
            this.vertices[0] = this.width;
            this.vertices[1] = 0.0f;
            this.vertices[2] = rect.left;
            this.vertices[3] = rect.top;
            this.vertices[4] = this.width + w;
            this.vertices[5] = 0.0f;
            this.vertices[6] = rect.right;
            this.vertices[7] = rect.top;
            this.vertices[8] = this.width + w;
            this.vertices[9] = h;
            this.vertices[10] = rect.right;
            this.vertices[11] = rect.bottom;
            this.vertices[12] = this.width;
            this.vertices[13] = h;
            this.vertices[14] = rect.left;
            this.vertices[15] = rect.bottom;
            this.quads.put(this.vertices);
            ++this.realLength;
            this.width += w + this.font.tracking;
            if (h > this.height) {
                this.height = h;
            }
        }
        if (length > 0) {
            this.width -= this.font.tracking;
        }
        this.dirty = false;
    }

    public void measure() {
        this.width = 0.0f;
        this.height = 0.0f;
        if (this.text == null) {
            this.text = "";
        }
        int length = this.text.length();
        for (int i = 0; i < length; ++i) {
            RectF rect = this.font.get(this.text.charAt(i));
            if (rect == null) {
                continue;
            }
            float w = this.font.width(rect);
            float h = this.font.height(rect);
            this.width += w + this.font.tracking;
            if (h > this.height) {
                this.height = h;
            }
        }
        if (length > 0) {
            this.width -= this.font.tracking;
        }
    }

    public float baseLine() {
        return this.font.baseLine * this.scale.y;
    }

    public Font font() {
        return this.font;
    }

    public void font(Font value) {
        this.font = value;
    }

    public String text() {
        return this.text;
    }

    public void text(String str) {
        this.text = str;
        this.dirty = true;
    }

    public static class Font extends TextureFilm {
        public static final String LATIN_UPPER = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        public static final String LATIN_FULL = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u007f";
        public SmartTexture texture;
        public float tracking = 0.0f;
        public float baseLine;
        public boolean autoUppercase = false;
        public float lineHeight;

        private static java.awt.Font baseCjkFont;
        private int glyphHeight;
        private int atlasSize;
        private Pixmap atlasPixmap;
        private int packX = 0;
        private int packY = 0;
        private java.awt.Font awtFont;
        private FontMetrics awtMetrics;
        private int drawBaseY;

        private static synchronized java.awt.Font getBaseCjkFont() {
            if (baseCjkFont == null) {
                try {
                    InputStream is = BitmapText.class.getResourceAsStream("/fonts/droid_sans.ttf");
                    if (is != null) {
                        baseCjkFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, is);
                        is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (baseCjkFont == null) {
                    baseCjkFont = new java.awt.Font("Dialog", java.awt.Font.PLAIN, 14);
                }
            }
            return baseCjkFont;
        }

        protected Font(SmartTexture tx) {
            super(tx);
            this.texture = tx;
        }

        private Font(SmartTexture atlasTex, Pixmap atlasPm, int atlasSize, int glyphHeight) {
            super(atlasTex);
            this.texture = atlasTex;
            this.atlasPixmap = atlasPm;
            this.atlasSize = atlasSize;
            this.glyphHeight = glyphHeight;
            this.drawBaseY = (int) Math.round(glyphHeight * 0.77);

            float pt;
            if (glyphHeight <= 9) {
                pt = 7.5f;
                this.drawBaseY = 6;
            } else if (glyphHeight <= 12) {
                pt = 10.0f;
                this.drawBaseY = 9;
            } else if (glyphHeight <= 14) {
                pt = 11.5f;
                this.drawBaseY = 11;
            } else if (glyphHeight <= 17) {
                pt = 14.0f;
                this.drawBaseY = 13;
            } else {
                pt = 18.0f;
                this.drawBaseY = 17;
            }
            this.awtFont = getBaseCjkFont().deriveFont(java.awt.Font.PLAIN, pt);
            BufferedImage tmp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = tmp.createGraphics();
            g.setFont(this.awtFont);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            this.awtMetrics = g.getFontMetrics();
            g.dispose();
        }

        public Font(SmartTexture tx, int width, String chars) {
            this(tx, width, tx.height, chars);
        }

        public Font(SmartTexture tx, int width, int height, String chars) {
            super(tx);
            this.texture = tx;
            this.glyphHeight = height;
            this.autoUppercase = chars.equals(LATIN_UPPER);
            int length = chars.length();
            float uw = (float) width / (float) tx.width;
            float vh = (float) height / (float) tx.height;
            float left = 0.0f;
            float top = 0.0f;
            float bottom = vh;
            for (int i = 0; i < length; ++i) {
                RectF rect = new RectF(left, top, left += uw, bottom);
                this.add(Character.valueOf(chars.charAt(i)), rect);
                if (left >= 1.0f) {
                    left = 0.0f;
                    top = bottom;
                    bottom += vh;
                }
            }
            this.lineHeight = this.baseLine = (float) height;
        }

        private RectF packPixmapGlyph(Pixmap src, int srcX, int srcY, int gw, int gh) {
            if (gw <= 0) gw = 1;
            if (this.packX + gw + 1 > this.atlasSize) {
                this.packX = 0;
                this.packY += this.glyphHeight + 1;
            }
            if (this.packY + gh > this.atlasSize) {
                return null;
            }
            int dstX = this.packX;
            int dstY = this.packY;
            this.atlasPixmap.drawPixmap(src, dstX, dstY, srcX, srcY, gw, gh);
            this.packX += gw + 1;
            return new RectF(
                (float) dstX / (float) this.atlasSize,
                (float) dstY / (float) this.atlasSize,
                (float) (dstX + gw) / (float) this.atlasSize,
                (float) (dstY + gh) / (float) this.atlasSize
            );
        }

        protected void splitBy(GdxTexture bitmap, int height, int color, String chars) {
            int pos;
            this.autoUppercase = chars.equals(LATIN_UPPER);
            int length = chars.length();
            int width = bitmap.getWidth();
            TextureData td = bitmap.getTextureData();
            if (!td.isPrepared()) {
                td.prepare();
            }
            Pixmap pixmap = td.consumePixmap();
            for (pos = 0; pos < width; ++pos) {
                boolean match = true;
                for (int j = 0; j < height; ++j) {
                    if (this.colorNotMatch(pixmap, pos, j, color)) {
                        match = false;
                        break;
                    }
                }
                if (!match) {
                    break;
                }
            }
            if (this.atlasPixmap != null) {
                RectF spaceRect = this.packPixmapGlyph(pixmap, 0, 0, pos, height);
                this.add(Character.valueOf(' '), spaceRect);
            } else {
                float vHeight = (float) height / (float) bitmap.getHeight();
                this.add(Character.valueOf(' '), new RectF(0.0f, 0.0f, (float) pos / (float) width, vHeight));
            }
            for (int i = 0; i < length; ++i) {
                char ch = chars.charAt(i);
                if (ch == ' ') continue;
                int separator = pos;
                while (++separator < width) {
                    boolean found = true;
                    for (int j = 0; j < height; ++j) {
                        if (this.colorNotMatch(pixmap, separator, j, color)) {
                            found = false;
                            break;
                        }
                    }
                    if (found) break;
                }
                if (this.atlasPixmap != null) {
                    RectF r = this.packPixmapGlyph(pixmap, pos, 0, separator - pos, height);
                    this.add(Character.valueOf(ch), r);
                } else {
                    float vHeight = (float) height / (float) bitmap.getHeight();
                    this.add(Character.valueOf(ch), new RectF((float) pos / (float) width, 0.0f, (float) separator / (float) width, vHeight));
                }
                pos = separator + 1;
            }
            if (this.atlasPixmap != null) {
                this.texture.bitmap.draw(this.atlasPixmap, 0, 0);
            }
            pixmap.dispose();
            this.lineHeight = this.baseLine = this.height((RectF) this.frames.get(Character.valueOf(chars.charAt(0))));
        }

        private boolean colorNotMatch(Pixmap pixmap, int x, int y, int color) {
            int pixel = pixmap.getPixel(x, y);
            if ((pixel & 0xFF) == 0) {
                return color != 0;
            }
            return pixel != color;
        }

        public static Font colorMarked(GdxTexture bmp, int color, String chars) {
            return colorMarked(bmp, bmp.getHeight(), color, chars);
        }

        public static Font colorMarked(GdxTexture bmp, int height, int color, String chars) {
            int atlasSize = height <= 14 ? 1024 : 2048;
            Pixmap atlasPm = new Pixmap(atlasSize, atlasSize, Pixmap.Format.RGBA8888);
            atlasPm.setBlending(Pixmap.Blending.None);
            SmartTexture atlasTex = new SmartTexture(new GdxTexture(atlasPm));
            Font font = new Font(atlasTex, atlasPm, atlasSize, height);
            font.splitBy(bmp, height, color, chars);
            return font;
        }

        public RectF get(char ch) {
            char key = this.autoUppercase ? Character.toUpperCase(ch) : ch;
            RectF rect = super.get(Character.valueOf(key));
            if (rect != null) {
                return rect;
            }
            if (key < 32) {
                return super.get(Character.valueOf(' '));
            }
            return this.rasterizeGlyph(key);
        }

        private synchronized RectF rasterizeGlyph(char ch) {
            RectF existing = super.get(Character.valueOf(ch));
            if (existing != null) {
                return existing;
            }
            if (this.atlasPixmap == null || this.awtFont == null) {
                return super.get(Character.valueOf('?'));
            }
            if (!this.awtFont.canDisplay(ch)) {
                RectF fallback = super.get(Character.valueOf('?'));
                if (fallback != null) {
                    this.add(Character.valueOf(ch), fallback);
                    return fallback;
                }
                return super.get(Character.valueOf(' '));
            }
            int adv = Math.max(1, this.awtMetrics.charWidth(ch));
            int pad = Math.max(0, (int) Math.ceil(-this.tracking));
            int cellW = adv + 1 + pad;
            int cellH = this.glyphHeight;

            if (this.packX + cellW + 1 > this.atlasSize) {
                this.packX = 0;
                this.packY += this.glyphHeight + 1;
            }
            if (this.packY + cellH > this.atlasSize) {
                return super.get(Character.valueOf('?'));
            }

            BufferedImage img = new BufferedImage(cellW, cellH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.setFont(this.awtFont);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            String s = String.valueOf(ch);
            g.setColor(Color.BLACK);
            g.drawString(s, 1, this.drawBaseY + 1);
            g.setColor(Color.WHITE);
            g.drawString(s, 0, this.drawBaseY);
            g.dispose();

            Pixmap glyphPm = new Pixmap(cellW, cellH, Pixmap.Format.RGBA8888);
            glyphPm.setBlending(Pixmap.Blending.None);
            for (int py = 0; py < cellH; ++py) {
                for (int px = 0; px < cellW; ++px) {
                    int argb = img.getRGB(px, py);
                    int rgba = (argb << 8) | (argb >>> 24);
                    glyphPm.drawPixel(px, py, rgba);
                }
            }

            int dstX = this.packX;
            int dstY = this.packY;
            this.atlasPixmap.drawPixmap(glyphPm, dstX, dstY);
            this.texture.bitmap.draw(glyphPm, dstX, dstY);
            glyphPm.dispose();
            this.packX += cellW + 1;

            RectF r = new RectF(
                (float) dstX / (float) this.atlasSize,
                (float) dstY / (float) this.atlasSize,
                (float) (dstX + cellW) / (float) this.atlasSize,
                (float) (dstY + cellH) / (float) this.atlasSize
            );
            this.add(Character.valueOf(ch), r);
            return r;
        }
    }
}
