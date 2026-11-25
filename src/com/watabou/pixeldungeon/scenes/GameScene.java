/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.scenes;

import com.badlogic.gdx.utils.IntMap;
import com.watabou.input.Cursor;
import com.watabou.input.NoosaInputProcessor;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.FogOfWar;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.hero.HeroAction;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.BannerSprites;
import com.watabou.pixeldungeon.effects.BlobEmitter;
import com.watabou.pixeldungeon.effects.EmoIcon;
import com.watabou.pixeldungeon.effects.Flare;
import com.watabou.pixeldungeon.effects.FloatingText;
import com.watabou.pixeldungeon.effects.Ripple;
import com.watabou.pixeldungeon.effects.SpellSprite;
import com.watabou.pixeldungeon.input.GameAction;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.items.wands.WandOfBlink;
import com.watabou.pixeldungeon.levels.RegularLevel;
import com.watabou.pixeldungeon.levels.features.Chasm;
import com.watabou.pixeldungeon.plants.Plant;
import com.watabou.pixeldungeon.scenes.CellSelector;
import com.watabou.pixeldungeon.scenes.InterlevelScene;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.sprites.DiscardedItemSprite;
import com.watabou.pixeldungeon.sprites.HeroSprite;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.sprites.PlantSprite;
import com.watabou.pixeldungeon.ui.AttackIndicator;
import com.watabou.pixeldungeon.ui.Banner;
import com.watabou.pixeldungeon.ui.BusyIndicator;
import com.watabou.pixeldungeon.ui.Compass;
import com.watabou.pixeldungeon.ui.GameLog;
import com.watabou.pixeldungeon.ui.HealthIndicator;
import com.watabou.pixeldungeon.ui.QuickSlot;
import com.watabou.pixeldungeon.ui.StatusPane;
import com.watabou.pixeldungeon.ui.Toast;
import com.watabou.pixeldungeon.ui.Toolbar;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.windows.WndBag;
import com.watabou.pixeldungeon.windows.WndGame;
import com.watabou.pixeldungeon.windows.WndHero;
import com.watabou.pixeldungeon.windows.WndInfoCell;
import com.watabou.pixeldungeon.windows.WndInfoItem;
import com.watabou.pixeldungeon.windows.WndInfoMob;
import com.watabou.pixeldungeon.windows.WndInfoPlant;
import com.watabou.pixeldungeon.windows.WndMessage;
import com.watabou.pixeldungeon.windows.WndStory;
import com.watabou.pixeldungeon.windows.WndTradeItem;
import com.watabou.utils.GameMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Signal;
import java.io.IOException;
import java.util.ArrayList;

public class GameScene
extends PixelScene {
    private static final String TXT_WELCOME = "欢迎来到像素地牢第 %d 层！";
    private static final String TXT_WELCOME_BACK = "欢迎回到像素地牢第 %d 层！";
    private static final String TXT_NIGHT_MODE = "请务必小心，夜晚的地牢会变得更加危险！";
    private static final String TXT_CHASM = "你的脚步声在地牢中回荡不绝。";
    private static final String TXT_WATER = "你听到四周传来水流飞溅的声响。";
    private static final String TXT_GRASS = "浓郁的草木气息弥漫在空气中。";
    private static final String TXT_SECRETS = "这层地牢的气氛暗示着潜藏其中的秘密。";
    static GameScene scene;
    private SkinnedBlock water;
    private DungeonTilemap tiles;
    private FogOfWar fog;
    private HeroSprite hero;
    private GameLog log;
    private static CellSelector cellSelector;
    private Group terrain;
    private Group ripples;
    private Group plants;
    private Group heaps;
    private Group mobs;
    private Group emitters;
    private Group effects;
    private Group gases;
    private Group spells;
    private Group statuses;
    private Group emoicons;
    private Toolbar toolbar;
    private Toast prompt;
    private Signal.Listener<NoosaInputProcessor.Key<GameAction>> keyListener;
    private static final CellSelector.Listener defaultCellListener;

    @Override
    public void create() {
        Music.INSTANCE.play("game.mp3", true);
        Music.INSTANCE.volume(1.0f);
        PixelDungeon.lastClass(Dungeon.hero.heroClass.ordinal());
        super.create();
        Camera.main.zoom(GameMath.gate(minZoom, defaultZoom + (float)PixelDungeon.zoom(), maxZoom));
        this.keyListener = new Signal.Listener<NoosaInputProcessor.Key<GameAction>>(){

            @Override
            public void onSignal(NoosaInputProcessor.Key<GameAction> key) {
                if (Dungeon.hero.ready && key.pressed) {
                    switch ((GameAction)((Object)key.action)) {
                        case THROW: {
                            GameScene.selectItem(null, WndBag.Mode.THROW, null);
                            break;
                        }
                        case DRINK: {
                            GameScene.selectItem(null, WndBag.Mode.DRINK, null);
                            break;
                        }
                        case READ: {
                            GameScene.selectItem(null, WndBag.Mode.READ, null);
                            break;
                        }
                        case ZAP: {
                            WndBag.wandClass = null;
                            GameScene.selectItem(null, WndBag.Mode.ZAP, null);
                            break;
                        }
                        case PLANT: {
                            GameScene.selectItem(null, WndBag.Mode.PLANT, null);
                        }
                    }
                }
            }
        };
        Game.instance.getInputProcessor().addKeyListener(this.keyListener);
        scene = this;
        this.terrain = new Group();
        this.add(this.terrain);
        this.water = new SkinnedBlock(512.0f, 512.0f, Dungeon.level.waterTex());
        this.terrain.add(this.water);
        this.ripples = new Group();
        this.terrain.add(this.ripples);
        this.tiles = new DungeonTilemap();
        this.terrain.add(this.tiles);
        Dungeon.level.addVisuals(this);
        this.plants = new Group();
        this.add(this.plants);
        for (IntMap.Entry entry : Dungeon.level.plants) {
            this.addPlantSprite((Plant)entry.value);
        }
        this.heaps = new Group();
        this.add(this.heaps);
        for (IntMap.Entry entry : Dungeon.level.heaps) {
            this.addHeapSprite((Heap)entry.value);
        }
        this.emitters = new Group();
        this.effects = new Group();
        this.emoicons = new Group();
        this.mobs = new Group();
        this.add(this.mobs);
        for (Mob mob : Dungeon.level.mobs) {
            this.addMobSprite(mob);
            if (!Statistics.getBoolean(Statistics.Type.AMULET)) continue;
            mob.beckon(Dungeon.hero.pos);
        }
        this.add(this.emitters);
        this.add(this.effects);
        this.gases = new Group();
        this.add(this.gases);
        for (Blob blob : Dungeon.level.blobs.values()) {
            blob.emitter = null;
            this.addBlobSprite(blob);
        }
        this.fog = new FogOfWar(32, 32);
        this.fog.updateVisibility(Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped);
        this.add(this.fog);
        this.spells = new Group();
        this.add(this.spells);
        this.statuses = new Group();
        this.add(this.statuses);
        this.add(this.emoicons);
        this.hero = new HeroSprite();
        this.hero.place(Dungeon.hero.pos);
        this.hero.updateArmor();
        if (!Dungeon.hero.isAlive()) {
            this.hero.die();
        }
        this.mobs.add(this.hero);
        this.add(new HealthIndicator());
        cellSelector = new CellSelector(this.tiles);
        this.add(cellSelector);
        GameScene.cellSelector.listener = defaultCellListener;
        StatusPane sb = new StatusPane();
        sb.camera = uiCamera;
        sb.setSize(GameScene.uiCamera.width, 0.0f);
        this.add(sb);
        this.toolbar = new Toolbar();
        this.toolbar.camera = uiCamera;
        this.toolbar.setPos(GameScene.align(((float)GameScene.uiCamera.width - this.toolbar.width()) / 2.0f), GameScene.uiCamera.height);
        this.add(this.toolbar);
        AttackIndicator attackIndicator = new AttackIndicator();
        attackIndicator.camera = uiCamera;
        attackIndicator.setPos((float)GameScene.uiCamera.width - attackIndicator.width(), (float)GameScene.uiCamera.height - attackIndicator.height());
        AttackIndicator.updateState();
        this.add(attackIndicator);
        this.log = new GameLog();
        this.log.camera = uiCamera;
        this.log.setRect(0.0f, GameScene.uiCamera.height, this.toolbar.left(), 0.0f);
        this.add(this.log);
        this.add(new BusyIndicator());
        Compass compass = new Compass(Dungeon.level.exit);
        compass.camera = uiCamera;
        compass.x = ((float)GameScene.uiCamera.width - compass.width) / 2.0f;
        compass.y = ((float)GameScene.uiCamera.height - compass.height) / 2.0f;
        this.add(compass);
        switch (InterlevelScene.mode) {
            case RESURRECT: {
                WandOfBlink.appear(Dungeon.hero, Dungeon.level.entrance);
                new Flare(8, 32.0f).color(0xFFFF66, true).show(this.hero, 2.0f);
                break;
            }
            case RETURN: {
                WandOfBlink.appear(Dungeon.hero, Dungeon.hero.pos);
                break;
            }
            case FALL: {
                Chasm.heroLand();
                break;
            }
            case DESCEND: {
                switch (Dungeon.depth) {
                    case 1: {
                        WndStory.showChapter(0);
                        break;
                    }
                    case 6: {
                        WndStory.showChapter(1);
                        break;
                    }
                    case 11: {
                        WndStory.showChapter(2);
                        break;
                    }
                    case 16: {
                        WndStory.showChapter(3);
                        break;
                    }
                    case 22: {
                        WndStory.showChapter(4);
                    }
                }
                if (!Dungeon.hero.isAlive() || Dungeon.depth == 22) break;
                Badges.validateNoKilling();
                break;
            }
        }
        ArrayList dropped = (ArrayList)Dungeon.droppedItems.get(Dungeon.depth);
        if (dropped != null) {
            for (Object obj : dropped) {
                Item item = (Item)obj;
                int pos = Dungeon.level.randomRespawnCell();
                if (item instanceof Potion) {
                    ((Potion)item).shatter(pos);
                    continue;
                }
                if (item instanceof Plant.Seed) {
                    Dungeon.level.plant((Plant.Seed)item, pos);
                    continue;
                }
                Dungeon.level.drop(item, pos);
            }
            Dungeon.droppedItems.remove(Dungeon.depth);
        }
        Camera.main.target = this.hero;
        if (InterlevelScene.mode != InterlevelScene.Mode.NONE) {
            if (Dungeon.depth < Statistics.getInt(Statistics.Type.DEEPEST)) {
                GLog.h(TXT_WELCOME_BACK, Dungeon.depth);
            } else {
                GLog.h(TXT_WELCOME, Dungeon.depth);
                Sample.INSTANCE.play("snd_descend.mp3");
            }
            switch (Dungeon.level.feeling) {
                case CHASM: {
                    GLog.w(TXT_CHASM, new Object[0]);
                    break;
                }
                case WATER: {
                    GLog.w(TXT_WATER, new Object[0]);
                    break;
                }
                case GRASS: {
                    GLog.w(TXT_GRASS, new Object[0]);
                    break;
                }
            }
            if (Dungeon.level instanceof RegularLevel && ((RegularLevel)Dungeon.level).secretDoors > Random.IntRange(3, 4)) {
                GLog.w(TXT_SECRETS, new Object[0]);
            }
            if (Dungeon.nightMode && !Dungeon.bossLevel()) {
                GLog.w(TXT_NIGHT_MODE, new Object[0]);
            }
            InterlevelScene.mode = InterlevelScene.Mode.NONE;
            this.fadeIn();
        }
    }

    @Override
    public void destroy() {
        Game.instance.getInputProcessor().removeKeyListener(this.keyListener);
        scene = null;
        Badges.saveGlobal();
        super.destroy();
    }

    @Override
    public synchronized void pause() {
        try {
            Dungeon.saveAll();
            Badges.saveGlobal();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    @Override
    public synchronized void update() {
        if (Dungeon.hero == null) {
            return;
        }
        super.update();
        this.water.offset(0.0f, -4.0f * Game.elapsed);
        Actor.process();
        if (Dungeon.hero.ready && !Dungeon.hero.paralysed) {
            this.log.newLine();
        }
        GameScene.cellSelector.enabled = Dungeon.hero.ready;
    }

    @Override
    protected void onBackPressed() {
        if (!GameScene.cancel()) {
            this.add(new WndGame());
        }
    }

    @Override
    protected void onMenuPressed() {
        if (Dungeon.hero.ready) {
            GameScene.selectItem(null, WndBag.Mode.ALL, null);
        }
    }

    private void addHeapSprite(Heap heap) {
        ItemSprite sprite = heap.sprite = (ItemSprite)this.heaps.recycle(ItemSprite.class);
        sprite.revive();
        sprite.link(heap);
        this.heaps.add(sprite);
    }

    private void addDiscardedSprite(Heap heap) {
        heap.sprite = (DiscardedItemSprite)this.heaps.recycle(DiscardedItemSprite.class);
        heap.sprite.revive();
        heap.sprite.link(heap);
        this.heaps.add(heap.sprite);
    }

    private void addPlantSprite(Plant plant) {
        plant.sprite = (PlantSprite)this.plants.recycle(PlantSprite.class);
        plant.sprite.reset(plant);
    }

    private void addBlobSprite(Blob gas) {
        if (gas.emitter == null) {
            this.gases.add(new BlobEmitter(gas));
        }
    }

    private void addMobSprite(Mob mob) {
        CharSprite sprite = mob.sprite();
        sprite.visible = Dungeon.visible[mob.pos];
        this.mobs.add(sprite);
        sprite.link(mob);
    }

    private void prompt(String text) {
        if (this.prompt != null) {
            this.prompt.killAndErase();
            this.prompt = null;
        }
        if (text != null) {
            this.prompt = new Toast(text){

                @Override
                protected void onClose() {
                    GameScene.cancel();
                }
            };
            this.prompt.camera = uiCamera;
            this.prompt.setPos(((float)GameScene.uiCamera.width - this.prompt.width()) / 2.0f, GameScene.uiCamera.height - 60);
            this.add(this.prompt);
        }
    }

    private void showBanner(Banner banner) {
        banner.camera = uiCamera;
        banner.x = GameScene.align(uiCamera, ((float)GameScene.uiCamera.width - banner.width) / 2.0f);
        banner.y = GameScene.align(uiCamera, ((float)GameScene.uiCamera.height - banner.height) / 3.0f);
        this.add(banner);
    }

    @Override
    public float footer() {
        return this.toolbar.height();
    }

    public static PointF direction(int pos) {
        PointF scrCenter = Camera.main.center().offset(Camera.main.scroll);
        PointF cellCenter = DungeonTilemap.tileCenterToWorld(pos);
        float dx = cellCenter.x - scrCenter.x;
        float dy = cellCenter.y - scrCenter.y;
        return new PointF((float)Math.atan2(dy, dx), (float)Math.hypot(dx, dy) / 16.0f);
    }

    public static void play(String id, int pos) {
        GameScene.play(id, 1.0f, 1.0f, pos);
    }

    public static void play(String id, float volume, float rate, int pos) {
        float DLIMIT = 4.0f;
        PointF dir = GameScene.direction(pos);
        float side = (float)Math.cos(dir.x) * 0.8f;
        if (dir.y > 4.0f) {
            volume *= 4.0f / dir.y;
        } else {
            side *= dir.y / 4.0f;
        }
        Sample.INSTANCE.play(id, volume, rate, side);
    }

    public static void add(Plant plant) {
        if (scene != null) {
            scene.addPlantSprite(plant);
        }
    }

    public static void add(Blob gas) {
        Actor.add(gas);
        if (scene != null) {
            scene.addBlobSprite(gas);
        }
    }

    public static void add(Heap heap) {
        if (scene != null) {
            scene.addHeapSprite(heap);
        }
    }

    public static void discard(Heap heap) {
        if (scene != null) {
            scene.addDiscardedSprite(heap);
        }
    }

    public static void add(Mob mob) {
        Dungeon.level.mobs.add(mob);
        Actor.add(mob);
        Actor.occupyCell(mob);
        scene.addMobSprite(mob);
    }

    public static void add(Mob mob, float delay) {
        Dungeon.level.mobs.add(mob);
        Actor.addDelayed(mob, delay);
        Actor.occupyCell(mob);
        scene.addMobSprite(mob);
    }

    public static void add(EmoIcon icon) {
        GameScene.scene.emoicons.add(icon);
    }

    public static void effect(Visual effect) {
        GameScene.scene.effects.add(effect);
    }

    public static Ripple ripple(int pos) {
        Ripple ripple = (Ripple)GameScene.scene.ripples.recycle(Ripple.class);
        ripple.reset(pos);
        return ripple;
    }

    public static SpellSprite spellSprite() {
        return (SpellSprite)GameScene.scene.spells.recycle(SpellSprite.class);
    }

    public static Emitter emitter() {
        if (scene != null) {
            Emitter emitter = (Emitter)GameScene.scene.emitters.recycle(Emitter.class);
            emitter.revive();
            return emitter;
        }
        return null;
    }

    public static FloatingText status() {
        return scene != null ? (FloatingText)GameScene.scene.statuses.recycle(FloatingText.class) : null;
    }

    public static void pickUp(Item item) {
        GameScene.scene.toolbar.pickup(item);
    }

    public static void updateMap() {
        if (scene != null) {
            GameScene.scene.tiles.updated.set(0, 0, 32, 32);
        }
    }

    public static void updateMap(int cell) {
        if (scene != null) {
            GameScene.scene.tiles.updated.union(cell % 32, cell / 32);
        }
    }

    public static void discoverTile(int pos, int oldValue) {
        if (scene != null) {
            GameScene.scene.tiles.discover(pos, oldValue);
        }
    }

    public static void show(Window wnd) {
        GameScene.cancelCellSelector();
        scene.add(wnd);
    }

    public static void afterObserve() {
        if (scene != null) {
            GameScene.scene.fog.updateVisibility(Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped);
            for (Mob mob : Dungeon.level.mobs) {
                mob.sprite.visible = Dungeon.visible[mob.pos];
            }
        }
    }

    public static void flash(int color) {
        scene.fadeIn(0xFF000000 | color, true);
    }

    public static void gameOver() {
        Banner gameOver = new Banner(BannerSprites.get(BannerSprites.Type.GAME_OVER));
        gameOver.show(0, 1.0f);
        scene.showBanner(gameOver);
        Sample.INSTANCE.play("snd_death.mp3");
        Cursor.set("cursor_default.png");
    }

    public static void bossSlain() {
        if (Dungeon.hero.isAlive()) {
            Banner bossSlain = new Banner(BannerSprites.get(BannerSprites.Type.BOSS_SLAIN));
            bossSlain.show(0xFFFFFF, 0.3f, 5.0f);
            scene.showBanner(bossSlain);
            Sample.INSTANCE.play("snd_boss.mp3");
        }
    }

    public static void handleCell(int cell) {
        cellSelector.select(cell);
    }

    public static void selectCell(CellSelector.Listener listener) {
        GameScene.cellSelector.listener = listener;
        scene.prompt(listener.prompt());
        Game.instance.getInputProcessor().update();
    }

    private static boolean cancelCellSelector() {
        if (GameScene.cellSelector.listener != null && GameScene.cellSelector.listener != defaultCellListener) {
            cellSelector.cancel();
            return true;
        }
        return false;
    }

    public static WndBag selectItem(WndBag.Listener listener, WndBag.Mode mode, String title, String cursor) {
        WndBag wnd;
        GameScene.cancelCellSelector();
        switch (mode) {
            case READ: {
                wnd = WndBag.scrollHolder(listener, mode, title, cursor);
                break;
            }
            case WAND: 
            case ZAP: {
                wnd = WndBag.wandHolster(listener, mode, title, cursor);
                break;
            }
            case SEED: 
            case PLANT: {
                wnd = WndBag.seedPouch(listener, mode, title, cursor);
                break;
            }
            default: {
                wnd = WndBag.lastBag(listener, mode, title, cursor);
            }
        }
        scene.add(wnd);
        return wnd;
    }

    public static WndBag selectItem(WndBag.Listener listener, WndBag.Mode mode, String title) {
        return GameScene.selectItem(listener, mode, title, null);
    }

    static boolean cancel() {
        if (Dungeon.hero.curAction != null || Dungeon.hero.restoreHealth) {
            Dungeon.hero.curAction = null;
            Dungeon.hero.restoreHealth = false;
            return true;
        }
        return GameScene.cancelCellSelector();
    }

    public static void ready() {
        GameScene.selectCell(defaultCellListener);
        QuickSlot.cancel();
    }

    public static void examineCell(Integer cell) {
        if (cell == null) {
            return;
        }
        if (cell < 0 || cell > 1024 || !Dungeon.level.visited[cell] && !Dungeon.level.mapped[cell]) {
            GameScene.show(new WndMessage("你不清楚那里有什么。"));
            return;
        }
        if (!Dungeon.visible[cell]) {
            GameScene.show(new WndInfoCell(cell));
            return;
        }
        if (cell == Dungeon.hero.pos) {
            GameScene.show(new WndHero());
            return;
        }
        Mob mob = (Mob)Actor.findChar(cell);
        if (mob != null) {
            GameScene.show(new WndInfoMob(mob));
            return;
        }
        Heap heap = (Heap)Dungeon.level.heaps.get(cell);
        if (heap != null && heap.type != Heap.Type.HIDDEN) {
            if (heap.type == Heap.Type.FOR_SALE && heap.size() == 1 && heap.peek().price() > 0) {
                GameScene.show(new WndTradeItem(heap, false));
            } else {
                GameScene.show(new WndInfoItem(heap));
            }
            return;
        }
        Plant plant = (Plant)Dungeon.level.plants.get(cell);
        if (plant != null) {
            GameScene.show(new WndInfoPlant(plant));
            return;
        }
        GameScene.show(new WndInfoCell(cell));
    }

    static {
        defaultCellListener = new CellSelector.Listener(){
            private int lastCell = -1;

            @Override
            public String prompt() {
                return null;
            }

            @Override
            public String cursor() {
                return "cursor_default.png";
            }

            @Override
            public void onSelect(Integer cell) {
                if (cell != null && Dungeon.hero.handle(cell)) {
                    Dungeon.hero.next();
                }
            }

            @Override
            public void onHover(Integer cell) {
                if (cell != this.lastCell && Dungeon.hero.ready) {
                    HeroAction action = Dungeon.hero.getActionFor(cell);
                    if (action != null) {
                        Cursor.set(action.cursor());
                    } else {
                        Cursor.set("cursor_default.png");
                    }
                }
            }
        };
    }
}

