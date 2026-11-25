/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs.npcs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Journal;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.mobs.npcs.NPC;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.quest.CorpseDust;
import com.watabou.pixeldungeon.items.quest.PhantomFish;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.items.wands.WandOfAmok;
import com.watabou.pixeldungeon.items.wands.WandOfAvalanche;
import com.watabou.pixeldungeon.items.wands.WandOfBlink;
import com.watabou.pixeldungeon.items.wands.WandOfDisintegration;
import com.watabou.pixeldungeon.items.wands.WandOfFirebolt;
import com.watabou.pixeldungeon.items.wands.WandOfLightning;
import com.watabou.pixeldungeon.items.wands.WandOfPoison;
import com.watabou.pixeldungeon.items.wands.WandOfReach;
import com.watabou.pixeldungeon.items.wands.WandOfRegrowth;
import com.watabou.pixeldungeon.items.wands.WandOfSlowness;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.PrisonLevel;
import com.watabou.pixeldungeon.levels.Room;
import com.watabou.pixeldungeon.plants.Rotberry;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.WandmakerSprite;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndQuest;
import com.watabou.pixeldungeon.windows.WndWandmaker;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import java.util.ArrayList;

public class Wandmaker
extends NPC {
    private static final QuestHandler berryQuest = new QuestHandler(){
        {
            this.txtQuest1 = "哎呀，能在这种地方遇见正派人真是意外之喜！我来此是为了寻找一种稀有素材——_腐莓种子_。作为施法者，对付这里的怪物倒不在话下，但我老是一下子就迷路，实在有些尴尬。你能帮帮我吗？我非常乐意用我最好的一根法杖作为酬谢。";
            this.txtQuest2 = "找到_腐莓种子_了吗，%s？还没？没关系，我不着急。";
        }

        @Override
        protected Item checkItem() {
            return Dungeon.hero.belongings.getItem(Rotberry.Seed.class);
        }

        @Override
        protected void placeItem() {
            int shrubPos = Dungeon.level.randomRespawnCell();
            while (Dungeon.level.heaps.get(shrubPos) != null) {
                shrubPos = Dungeon.level.randomRespawnCell();
            }
            Dungeon.level.plant(new Rotberry.Seed(), shrubPos);
        }
    };
    private static final QuestHandler dustQuest = new QuestHandler(){
        {
            this.txtQuest1 = "哎呀，能在这种地方遇见正派人真是意外之喜！我来此是为了寻找一种稀有素材——_尸尘_。地牢里遗骸众多，正可从中收集。作为施法者，对付这里的怪物倒不在话下，但我老是一下子就迷路，实在有些尴尬。你能帮帮我吗？我非常乐意用我最好的一根法杖作为酬谢。";
            this.txtQuest2 = "找到_尸尘_了吗，%s？白骨堆是最有可能找到的地方。";
        }

        @Override
        protected Item checkItem() {
            return Dungeon.hero.belongings.getItem(CorpseDust.class);
        }

        @Override
        protected void placeItem() {
            ArrayList<Heap> candidates = new ArrayList<Heap>();
            for (Heap heap : Dungeon.level.heaps.values()) {
                if (heap.type != Heap.Type.SKELETON || Dungeon.visible[heap.pos]) continue;
                candidates.add(heap);
            }
            if (candidates.size() > 0) {
                ((Heap)Random.element(candidates)).drop(new CorpseDust());
            } else {
                Heap heap;
                int pos = Dungeon.level.randomRespawnCell();
                while (Dungeon.level.heaps.get(pos) != null) {
                    pos = Dungeon.level.randomRespawnCell();
                }
                heap = Dungeon.level.drop(new CorpseDust(), pos);
                heap.type = Heap.Type.SKELETON;
                heap.sprite.link();
            }
        }
    };
    private static final QuestHandler fishQuest = new QuestHandler(){
        {
            this.txtQuest1 = "哎呀，能在这种地方遇见正派人真是意外之喜！我来此是为了寻找一种稀有素材——_幻影鱼_。徒手就能捉到它，只是它在水里很难被发现。作为施法者，对付这里的怪物倒不在话下，但我老是一下子就迷路，实在有些尴尬。你能帮帮我吗？我非常乐意用我最好的一根法杖作为酬谢。";
            this.txtQuest2 = "找到_幻影鱼_了吗，%s？你可以试着在附近的水池里搜寻一下。";
        }

        @Override
        protected Item checkItem() {
            return Dungeon.hero.belongings.getItem(PhantomFish.class);
        }

        @Override
        protected void placeItem() {
            for (int i = 0; i < 100; ++i) {
                int pos = Random.Int(1024);
                if (!Level.water[pos]) continue;
                Heap heap = Dungeon.level.drop(new PhantomFish(), pos);
                heap.type = Heap.Type.HIDDEN;
                heap.sprite.link();
                return;
            }
            int pos = Dungeon.level.randomRespawnCell();
            while (Dungeon.level.heaps.get(pos) != null) {
                pos = Dungeon.level.randomRespawnCell();
            }
            Dungeon.level.drop(new PhantomFish(), pos);
        }
    };

    public Wandmaker() {
        this.name = "老杖匠";
        this.spriteClass = WandmakerSprite.class;
    }

    @Override
    protected boolean act() {
        this.throwItem();
        return super.act();
    }

    @Override
    public int defenseSkill(Char enemy) {
        return 1000;
    }

    @Override
    public String defenseVerb() {
        return "吸收";
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
        Quest.type.handler.interact(this);
    }

    private void tell(String format, Object ... args) {
        GameScene.show(new WndQuest(this, Utils.format(format, args), new String[0]));
    }

    @Override
    public String description() {
        return "这位年迈却硬朗的老先生神情略显迷茫，周身笼罩着一层魔法护盾。";
    }

    static /* synthetic */ QuestHandler access$100() {
        return berryQuest;
    }

    static /* synthetic */ QuestHandler access$200() {
        return dustQuest;
    }

    static /* synthetic */ QuestHandler access$300() {
        return fishQuest;
    }

    public static abstract class QuestHandler {
        protected String txtQuest1;
        protected String txtQuest2;

        public void interact(Wandmaker wandmaker) {
            if (Quest.given) {
                Item item = this.checkItem();
                if (item != null) {
                    GameScene.show(new WndWandmaker(wandmaker, item));
                } else {
                    wandmaker.tell(this.txtQuest2, new Object[]{Dungeon.hero.className()});
                }
            } else {
                wandmaker.tell(this.txtQuest1, new Object[0]);
                Quest.given = true;
                this.placeItem();
                Journal.add(Journal.Feature.WANDMAKER);
            }
        }

        protected abstract Item checkItem();

        protected abstract void placeItem();
    }

    public static class Quest {
        private static Type type;
        private static boolean spawned;
        private static boolean given;
        public static Wand wand1;
        public static Wand wand2;
        private static final String NODE = "wandmaker";
        private static final String SPAWNED = "spawned";
        private static final String TYPE = "type";
        private static final String ALTERNATIVE = "alternative";
        private static final String GIVEN = "given";
        private static final String WAND1 = "wand1";
        private static final String WAND2 = "wand2";

        public static void reset() {
            spawned = false;
            wand1 = null;
            wand2 = null;
        }

        public static void storeInBundle(Bundle bundle) {
            Bundle node = new Bundle();
            node.put(SPAWNED, spawned);
            if (spawned) {
                node.put(TYPE, type.toString());
                node.put(GIVEN, given);
                node.put(WAND1, wand1);
                node.put(WAND2, wand2);
            }
            bundle.put(NODE, node);
        }

        public static void restoreFromBundle(Bundle bundle) {
            Bundle node = bundle.getBundle(NODE);
            if (!node.isNull() && (spawned = node.getBoolean(SPAWNED))) {
                type = node.getEnum(TYPE, Type.class);
                if (type == Type.ILLEGAL) {
                    type = node.getBoolean(ALTERNATIVE) ? Type.DUST : Type.BERRY;
                }
                given = node.getBoolean(GIVEN);
                wand1 = (Wand)node.get(WAND1);
                wand2 = (Wand)node.get(WAND2);
            } else {
                Quest.reset();
            }
        }

        public static void spawn(PrisonLevel level, Room room) {
            if (!spawned && Dungeon.depth > 6 && Random.Int(10 - Dungeon.depth) == 0) {
                Wandmaker npc = new Wandmaker();
                do {
                    npc.pos = room.random();
                } while (level.map[npc.pos] == 7 || level.map[npc.pos] == 29);
                level.mobs.add(npc);
                Actor.occupyCell(npc);
                spawned = true;
                block0 : switch (Random.Int(3)) {
                    case 0: {
                        type = Type.BERRY;
                        break;
                    }
                    case 1: {
                        type = Type.DUST;
                        break;
                    }
                    case 2: {
                        type = Type.FISH;
                        int water = 0;
                        for (int i = 0; i < 1024; ++i) {
                            if (!Level.water[i] || ++water <= 64) continue;
                            type = Random.Int(2) == 0 ? Type.BERRY : Type.DUST;
                            break block0;
                        }
                        break;
                    }
                }
                given = false;
                switch (Random.Int(5)) {
                    case 0: {
                        wand1 = new WandOfAvalanche();
                        break;
                    }
                    case 1: {
                        wand1 = new WandOfDisintegration();
                        break;
                    }
                    case 2: {
                        wand1 = new WandOfFirebolt();
                        break;
                    }
                    case 3: {
                        wand1 = new WandOfLightning();
                        break;
                    }
                    case 4: {
                        wand1 = new WandOfPoison();
                    }
                }
                wand1.random().upgrade();
                switch (Random.Int(5)) {
                    case 0: {
                        wand2 = new WandOfAmok();
                        break;
                    }
                    case 1: {
                        wand2 = new WandOfBlink();
                        break;
                    }
                    case 2: {
                        wand2 = new WandOfRegrowth();
                        break;
                    }
                    case 3: {
                        wand2 = new WandOfSlowness();
                        break;
                    }
                    case 4: {
                        wand2 = new WandOfReach();
                    }
                }
                wand2.random().upgrade();
            }
        }

        public static void complete() {
            wand1 = null;
            wand2 = null;
            Journal.remove(Journal.Feature.WANDMAKER);
        }

        static enum Type {
            ILLEGAL(null),
            BERRY(Wandmaker.access$100()),
            DUST(Wandmaker.access$200()),
            FISH(Wandmaker.access$300());

            public QuestHandler handler;

            private Type(QuestHandler handler) {
                this.handler = handler;
            }
        }
    }
}

