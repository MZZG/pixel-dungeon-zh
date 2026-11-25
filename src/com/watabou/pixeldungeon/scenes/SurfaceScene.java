/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.scenes;

import com.watabou.gltextures.Gradient;
import com.watabou.gltextures.SmartTexture;
import com.watabou.glwrap.Matrix;
import com.watabou.glwrap.Quad;
import com.watabou.input.NoosaInputProcessor;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Music;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.scenes.TitleScene;
import com.watabou.pixeldungeon.ui.Archs;
import com.watabou.pixeldungeon.ui.RedButton;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import java.nio.FloatBuffer;

public class SurfaceScene
extends PixelScene {
    private static final int FRAME_WIDTH = 88;
    private static final int FRAME_HEIGHT = 125;
    private static final int FRAME_MARGIN_TOP = 9;
    private static final int FRAME_MARGIN_X = 4;
    private static final int BUTTON_HEIGHT = 20;
    private static final int SKY_WIDTH = 80;
    private static final int SKY_HEIGHT = 112;
    private static final int NSTARS = 100;
    private static final int NCLOUDS = 5;
    private Camera viewport;

    @Override
    public void create() {
        super.create();
        Music.INSTANCE.play("surface.mp3", true);
        Music.INSTANCE.volume(1.0f);
        int w = Camera.main.width;
        int h = Camera.main.height;
        Archs archs = new Archs();
        archs.reversed = true;
        archs.setSize(w, h);
        this.add(archs);
        float vx = SurfaceScene.align((w - 80) / 2);
        float vy = SurfaceScene.align((h - 112 - 20) / 2);
        Point s = Camera.main.cameraToScreen(vx, vy);
        this.viewport = new Camera(s.x, s.y, 80, 112, defaultZoom);
        Camera.add(this.viewport);
        Group window = new Group();
        window.camera = this.viewport;
        this.add(window);
        boolean dayTime = !Dungeon.nightMode;
        Sky sky = new Sky(dayTime);
        sky.scale.set(80.0f, 112.0f);
        window.add(sky);
        if (!dayTime) {
            for (int i = 0; i < 100; ++i) {
                float size = Random.Float();
                ColorBlock star = new ColorBlock(size, size, -1);
                star.x = Random.Float(80.0f) - size / 2.0f;
                star.y = Random.Float(112.0f) - size / 2.0f;
                star.am = size * (1.0f - star.y / 112.0f);
                window.add(star);
            }
        }
        float range = 74.0f;
        for (int i = 0; i < 5; ++i) {
            Cloud cloud = new Cloud((float)(4 - i) * (range / 5.0f) + Random.Float(range / 5.0f), dayTime);
            window.add(cloud);
        }
        int nPatches = (int)(sky.width() / 16.0f + 1.0f);
        for (int i = 0; i < nPatches * 4; ++i) {
            GrassPatch patch = new GrassPatch(((float)i - 0.75f) * 16.0f / 4.0f, 113.0f, dayTime);
            patch.brightness(dayTime ? 0.7f : 0.4f);
            window.add(patch);
        }
        Avatar a = new Avatar(Dungeon.hero.heroClass);
        a.x = PixelScene.align((80.0f - a.width) / 2.0f);
        a.y = 112.0f - a.height;
        window.add(a);
        final Pet pet = new Pet();
        pet.bm = 1.2f;
        pet.gm = 1.2f;
        pet.rm = 1.2f;
        pet.x = 42.0f;
        pet.y = 112.0f - pet.height;
        window.add(pet);
        window.add(new TouchArea(sky){

            @Override
            protected void onClick(NoosaInputProcessor.Touch touch) {
                pet.jump();
            }
        });
        for (int i = 0; i < nPatches; ++i) {
            GrassPatch patch = new GrassPatch(((float)i - 0.5f) * 16.0f, 112.0f, dayTime);
            patch.brightness(dayTime ? 1.0f : 0.8f);
            window.add(patch);
        }
        Image frame = new Image("surface.png");
        frame.frame(0, 0, 88, 125);
        frame.x = vx - 4.0f;
        frame.y = vy - 9.0f;
        this.add(frame);
        if (dayTime) {
            a.brightness(1.2f);
            pet.brightness(1.2f);
        } else {
            frame.hardlight(0xDDEEFF);
        }
        RedButton gameOver = new RedButton("游戏结束"){

            @Override
            protected void onClick() {
                Game.switchScene(TitleScene.class);
            }
        };
        gameOver.setSize(72.0f, 20.0f);
        gameOver.setPos(frame.x + 8.0f, frame.y + frame.height + 4.0f);
        this.add(gameOver);
        Badges.validateHappyEnd();
        this.fadeIn();
    }

    @Override
    public void destroy() {
        Badges.saveGlobal();
        Camera.remove(this.viewport);
        super.destroy();
    }

    @Override
    protected void onBackPressed() {
    }

    private static class GrassPatch
    extends Image {
        public static final int WIDTH = 16;
        public static final int HEIGHT = 14;
        private float tx;
        private float ty;
        private double a = Random.Float(5.0f);
        private double angle;
        private boolean forward;

        public GrassPatch(float tx, float ty, boolean forward) {
            super("surface.png");
            this.frame(88 + Random.Int(4) * 16, 60, 16, 14);
            this.tx = tx;
            this.ty = ty;
            this.forward = forward;
        }

        @Override
        public void update() {
            super.update();
            this.a += (double)Random.Float(Game.elapsed * 5.0f);
            this.angle = (2.0 + Math.cos(this.a)) * (this.forward ? 0.2 : -0.2);
            this.scale.y = (float)Math.cos(this.angle);
            this.x = this.tx + (float)Math.tan(this.angle) * this.width;
            this.y = this.ty - this.scale.y * this.height;
        }

        @Override
        protected void updateMatrix() {
            super.updateMatrix();
            Matrix.skewX(this.matrix, (float)(this.angle / 0.01745329238474369));
        }
    }

    private static class Pet
    extends MovieClip
    implements MovieClip.Listener {
        private MovieClip.Animation idle;
        private MovieClip.Animation jump;

        public Pet() {
            super("pet.png");
            TextureFilm frames = new TextureFilm(this.texture, 16, 16);
            this.idle = new MovieClip.Animation(2, true);
            this.idle.frames(frames, 0, 0, 0, 0, 0, 0, 1);
            this.jump = new MovieClip.Animation(10, false);
            this.jump.frames(frames, 2, 3, 4, 5, 6);
            this.listener = this;
            this.play(this.idle);
        }

        public void jump() {
            this.play(this.jump);
        }

        @Override
        public void onComplete(MovieClip.Animation anim) {
            if (anim == this.jump) {
                this.play(this.idle);
            }
        }
    }

    private static class Avatar
    extends Image {
        private static final int WIDTH = 24;
        private static final int HEIGHT = 28;

        public Avatar(HeroClass cl) {
            super("avatars.png");
            this.frame(new TextureFilm(this.texture, 24, 28).get(cl.ordinal()));
        }
    }

    private static class Cloud
    extends Image {
        private static int lastIndex = -1;

        public Cloud(float y, boolean dayTime) {
            super("surface.png");
            int index;
            while ((index = Random.Int(3)) == lastIndex) {
            }
            switch (index) {
                case 0: {
                    this.frame(88, 0, 49, 20);
                    break;
                }
                case 1: {
                    this.frame(88, 20, 49, 22);
                    break;
                }
                case 2: {
                    this.frame(88, 42, 50, 18);
                }
            }
            lastIndex = index;
            this.y = y;
            this.scale.set(1.0f - y / 112.0f);
            this.x = Random.Float(80.0f + this.width()) - this.width();
            this.speed.x = this.scale.x * (float)(dayTime ? 8 : -8);
            if (dayTime) {
                this.tint(0xCCEEFF, 1.0f - this.scale.y);
            } else {
                this.bm = 3.0f;
                this.gm = 3.0f;
                this.rm = 3.0f;
                this.ba = -2.1f;
                this.ga = -2.1f;
                this.ra = -2.1f;
            }
        }

        @Override
        public void update() {
            super.update();
            if (this.speed.x > 0.0f && this.x > 80.0f) {
                this.x = -this.width();
            } else if (this.speed.x < 0.0f && this.x < -this.width()) {
                this.x = 80.0f;
            }
        }
    }

    private static class Sky
    extends Visual {
        private static final int[] day = new int[]{-12285697, -3346689};
        private static final int[] night = new int[]{-16772779, -13411968};
        private SmartTexture texture;
        private FloatBuffer verticesBuffer;

        public Sky(boolean dayTime) {
            super(0.0f, 0.0f, 1.0f, 1.0f);
            this.texture = new Gradient(dayTime ? day : night);
            float[] vertices = new float[16];
            this.verticesBuffer = Quad.create();
            vertices[2] = 0.25f;
            vertices[6] = 0.25f;
            vertices[10] = 0.75f;
            vertices[14] = 0.75f;
            vertices[3] = 0.0f;
            vertices[7] = 1.0f;
            vertices[11] = 1.0f;
            vertices[15] = 0.0f;
            vertices[0] = 0.0f;
            vertices[1] = 0.0f;
            vertices[4] = 1.0f;
            vertices[5] = 0.0f;
            vertices[8] = 1.0f;
            vertices[9] = 1.0f;
            vertices[12] = 0.0f;
            vertices[13] = 1.0f;
            this.verticesBuffer.position(0);
            this.verticesBuffer.put(vertices);
        }

        @Override
        public void draw() {
            super.draw();
            NoosaScript script = NoosaScript.get();
            this.texture.bind();
            script.camera(this.camera());
            script.uModel.valueM4(this.matrix);
            script.lighting(this.rm, this.gm, this.bm, this.am, this.ra, this.ga, this.ba, this.aa);
            script.drawQuad(this.verticesBuffer);
        }
    }
}

