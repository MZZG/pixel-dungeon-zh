/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs.npcs;

import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Journal;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.buffs.Roots;
import com.watabou.pixeldungeon.actors.mobs.CursePersonification;
import com.watabou.pixeldungeon.actors.mobs.FetidRat;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.actors.mobs.npcs.NPC;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.effects.particles.ShadowParticle;
import com.watabou.pixeldungeon.items.Generator;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.armor.ClothArmor;
import com.watabou.pixeldungeon.items.quest.DriedRose;
import com.watabou.pixeldungeon.items.quest.RatSkull;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.pixeldungeon.levels.SewerLevel;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.GhostSprite;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndQuest;
import com.watabou.pixeldungeon.windows.WndSadGhost;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import java.util.HashSet;

public class Ghost
extends NPC {
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet();
    private static final QuestHandler roseQuest;
    private static final QuestHandler ratQuest;
    private static final QuestHandler curseQuest;

    public Ghost() {
        this.name = "悲伤幽灵";
        this.spriteClass = GhostSprite.class;
        this.flying = true;
        this.state = this.WANDERING;
        Sample.INSTANCE.load("snd_ghost.mp3");
    }

    @Override
    public int defenseSkill(Char enemy) {
        return 1000;
    }

    @Override
    public String defenseVerb() {
        return "闪避";
    }

    @Override
    public float speed() {
        return 0.5f;
    }

    @Override
    protected Char chooseEnemy() {
        return null;
    }

    @Override
    public void damage(int dmg, Object src) {
    }

    @Override
    public void add(Buff buff) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public void interact() {
        this.sprite.turnTo(this.pos, Dungeon.hero.pos);
        GameScene.play("snd_ghost.mp3", this.pos);
        Quest.type.handler.interact(this);
    }

    @Override
    public String description() {
        return "这个幽灵几乎不可见，看起来像是一团带有悲伤面孔的朦胧微光。";
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    public static void replace(final Mob a, Mob b) {
        float FADE_TIME = 0.5f;
        a.destroy();
        a.sprite.parent.add(new AlphaTweener(a.sprite, 0.0f, 0.5f){

            @Override
            protected void onComplete() {
                a.sprite.killAndErase();
                this.parent.erase(this);
            }
        });
        b.pos = a.pos;
        GameScene.add(b);
        b.sprite.flipHorizontal = a.sprite.flipHorizontal;
        b.sprite.alpha(0.0f);
        b.sprite.parent.add(new AlphaTweener(b.sprite, 1.0f, 0.5f));
    }

    static /* synthetic */ QuestHandler access$100() {
        return roseQuest;
    }

    static /* synthetic */ QuestHandler access$200() {
        return ratQuest;
    }

    static /* synthetic */ QuestHandler access$300() {
        return curseQuest;
    }

    static {
        IMMUNITIES.add(Paralysis.class);
        IMMUNITIES.add(Roots.class);
        roseQuest = new QuestHandler(){
            private static final String TXT_ROSE1 = "你好，冒险者……曾经我也像你一样强大而自信……可如今我已死去……但在拿回我的_干枯玫瑰_之前，我无法离开这里……它对我非常重要……有只怪物从我的尸体上偷走了它……";
            private static final String TXT_ROSE2 = "求求你……帮帮我……_找到那朵玫瑰_……";
            private static final String TXT_ROSE3 = "对！就是它！！！快交给我吧！作为报答，你可以从这些物品中任选一件，或许能在旅途中帮到你……";

            @Override
            public void interact(Ghost ghost) {
                if (Quest.given) {
                    DriedRose item = Dungeon.hero.belongings.getItem(DriedRose.class);
                    if (item != null) {
                        GameScene.show(new WndSadGhost(ghost, item, TXT_ROSE3));
                    } else {
                        GameScene.show(new WndQuest(ghost, TXT_ROSE2, new String[0]));
                        this.relocate(ghost);
                    }
                } else {
                    GameScene.show(new WndQuest(ghost, TXT_ROSE1, new String[0]));
                    Quest.given = true;
                    Journal.add(Journal.Feature.GHOST);
                }
            }
        };
        ratQuest = new QuestHandler(){
            private static final String TXT_RAT1 = "你好，冒险者……曾经我也像你一样强大而自信……可如今我已死去……但在大仇得报之前，我无法离开这里……请帮我消灭那只夺走我性命的_腐臭老鼠_……";
            private static final String TXT_RAT2 = "求求你……帮帮我……_消灭那个孽障_……";
            private static final String TXT_RAT3 = "太好了！那只丑陋的怪物终于伏诛，我也可以安息了……请挑一件我的遗物吧，或许能在你的旅途中派上用场……";

            @Override
            public void interact(Ghost ghost) {
                if (Quest.given) {
                    RatSkull item = Dungeon.hero.belongings.getItem(RatSkull.class);
                    if (item != null) {
                        GameScene.show(new WndSadGhost(ghost, item, TXT_RAT3));
                    } else {
                        GameScene.show(new WndQuest(ghost, TXT_RAT2, new String[0]));
                        this.relocate(ghost);
                    }
                } else {
                    GameScene.show(new WndQuest(ghost, TXT_RAT1, new String[0]));
                    Quest.given = true;
                    Journal.add(Journal.Feature.GHOST);
                }
            }
        };
        curseQuest = new QuestHandler(){
            private static final String TXT_CURSE1 = "你好，冒险者……曾经我也像你一样强大而自信……可如今我已死去……可怕的诅咒将我束缚于此无法安息……求求你……帮帮我……_破除这个诅咒_……";
            private static final String TXT_CURSE2 = "谢谢你，%s！诅咒已经破除，我终于可以安息了……请收下其中一件物品吧，或许能在旅途中助你一臂之力……";
            private static final String TXT_YES = "好的，我这就去帮你";
            private static final String TXT_NO = "抱歉，我帮不了你";

            @Override
            public void interact(final Ghost ghost) {
                if (Quest.given) {
                    GameScene.show(new WndSadGhost(ghost, null, Utils.format(TXT_CURSE2, Dungeon.hero.className())));
                } else {
                    GameScene.show(new WndQuest(ghost, TXT_CURSE1, new String[]{TXT_YES, TXT_NO}){

                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                Quest.given = true;
                                CursePersonification d = new CursePersonification();
                                Ghost.replace(ghost, d);
                                d.sprite.emitter().burst(ShadowParticle.CURSE, 5);
                                Sample.INSTANCE.play("snd_ghost.mp3");
                                Dungeon.hero.next();
                            } else {
                                relocate(ghost);
                            }
                        }
                    });
                    Journal.add(Journal.Feature.GHOST);
                }
            }
        };
    }

    public static abstract class QuestHandler {
        public abstract void interact(Ghost var1);

        protected void relocate(Ghost ghost) {
            int newPos = -1;
            for (int i = 0; i < 10 && (newPos = Dungeon.level.randomRespawnCell()) == -1; ++i) {
            }
            if (newPos != -1) {
                Actor.freeCell(ghost.pos);
                CellEmitter.get(ghost.pos).start(Speck.factory(2), 0.2f, 3);
                ghost.pos = newPos;
                ghost.sprite.place(ghost.pos);
                ghost.sprite.visible = Dungeon.visible[ghost.pos];
            }
        }
    }

    public static class Quest {
        private static Type type;
        private static boolean spawned;
        private static boolean given;
        private static boolean processed;
        private static int depth;
        private static int left2kill;
        public static Weapon weapon;
        public static Armor armor;
        private static final String NODE = "sadGhost";
        private static final String SPAWNED = "spawned";
        private static final String TYPE = "type";
        private static final String ALTERNATIVE = "alternative";
        private static final String LEFT2KILL = "left2kill";
        private static final String GIVEN = "given";
        private static final String PROCESSED = "processed";
        private static final String DEPTH = "depth";
        private static final String WEAPON = "weapon";
        private static final String ARMOR = "armor";

        public static void reset() {
            spawned = false;
            weapon = null;
            armor = null;
        }

        public static void storeInBundle(Bundle bundle) {
            Bundle node = new Bundle();
            node.put(SPAWNED, spawned);
            if (spawned) {
                node.put(TYPE, type.toString());
                if (type == Type.ROSE) {
                    node.put(LEFT2KILL, left2kill);
                }
                node.put(GIVEN, given);
                node.put(DEPTH, depth);
                node.put(PROCESSED, processed);
                node.put(WEAPON, weapon);
                node.put(ARMOR, armor);
            }
            bundle.put(NODE, node);
        }

        public static void restoreFromBundle(Bundle bundle) {
            Bundle node = bundle.getBundle(NODE);
            if (!node.isNull() && (spawned = node.getBoolean(SPAWNED))) {
                type = node.getEnum(TYPE, Type.class);
                if (type == Type.ILLEGAL) {
                    Type type = Quest.type = node.getBoolean(ALTERNATIVE) ? Type.RAT : Type.ROSE;
                }
                if (type == Type.ROSE) {
                    left2kill = node.getInt(LEFT2KILL);
                }
                given = node.getBoolean(GIVEN);
                depth = node.getInt(DEPTH);
                processed = node.getBoolean(PROCESSED);
                weapon = (Weapon)node.get(WEAPON);
                armor = (Armor)node.get(ARMOR);
            } else {
                Quest.reset();
            }
        }

        public static void spawn(SewerLevel level) {
            if (!spawned && Dungeon.depth > 1 && Random.Int(5 - Dungeon.depth) == 0) {
                Item another;
                int i;
                Ghost ghost = new Ghost();
                do {
                    ghost.pos = level.randomRespawnCell();
                } while (ghost.pos == -1);
                level.mobs.add(ghost);
                Actor.occupyCell(ghost);
                spawned = true;
                switch (Random.Int(3)) {
                    case 0: {
                        type = Type.ROSE;
                        left2kill = 8;
                        break;
                    }
                    case 1: {
                        type = Type.RAT;
                        break;
                    }
                    case 2: {
                        type = Type.CURSE;
                    }
                }
                given = false;
                processed = false;
                depth = Dungeon.depth;
                for (i = 0; i < 4; ++i) {
                    while ((another = Generator.random(Generator.Category.WEAPON)) instanceof MissileWeapon) {
                    }
                    if (weapon != null && another.level() <= weapon.level()) continue;
                    weapon = (Weapon)another;
                }
                if (Dungeon.isChallenged(2)) {
                    armor = (Armor)new ClothArmor().degrade();
                } else {
                    armor = (Armor)Generator.random(Generator.Category.ARMOR);
                    for (i = 0; i < 3; ++i) {
                        another = Generator.random(Generator.Category.ARMOR);
                        if (another.level() <= armor.level()) continue;
                        armor = (Armor)another;
                    }
                }
                weapon.identify();
                armor.identify();
            }
        }

        public static void processSewersKill(int pos) {
            if (spawned && given && !processed && depth == Dungeon.depth) {
                switch (type) {
                    case ROSE: {
                        if (Random.Int(left2kill) == 0) {
                            Dungeon.level.drop((Item)new DriedRose(), (int)pos).sprite.drop();
                            processed = true;
                            break;
                        }
                        --left2kill;
                        break;
                    }
                    case RAT: {
                        FetidRat rat = new FetidRat();
                        rat.pos = Dungeon.level.randomRespawnCell();
                        if (rat.pos == -1) break;
                        GameScene.add(rat);
                        processed = true;
                        break;
                    }
                }
            }
        }

        public static void complete() {
            weapon = null;
            armor = null;
            Journal.remove(Journal.Feature.GHOST);
        }

        static enum Type {
            ILLEGAL(null),
            ROSE(Ghost.access$100()),
            RAT(Ghost.access$200()),
            CURSE(Ghost.access$300());

            public QuestHandler handler;

            private Type(QuestHandler handler) {
                this.handler = handler;
            }
        }
    }
}

