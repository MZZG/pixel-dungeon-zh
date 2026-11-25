/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs.npcs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Journal;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.mobs.Golem;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.actors.mobs.Monk;
import com.watabou.pixeldungeon.actors.mobs.npcs.NPC;
import com.watabou.pixeldungeon.items.Generator;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.quest.DwarfToken;
import com.watabou.pixeldungeon.items.rings.Ring;
import com.watabou.pixeldungeon.levels.CityLevel;
import com.watabou.pixeldungeon.levels.Room;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.ImpSprite;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndImp;
import com.watabou.pixeldungeon.windows.WndQuest;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Imp
extends NPC {
    private static final String TXT_GOLEMS1 = "你是冒险者吗？我最喜欢冒险者了！只要有东西需要干掉，找他们准没错，对吧？当然是有赏金的 ;)\n我这边需要清理的是那些_魔像_。你看，我打算在这儿做点小买卖，但那些蠢魔像太妨碍生意了！跟四处晃悠的花岗岩疙瘩根本没法讲道理！所以请帮我干掉……就算 _6 只_吧，报酬绝对少不了你。";
    private static final String TXT_MONKS1 = "你是冒险者吗？我最喜欢冒险者了！只要有东西需要干掉，找他们准没错，对吧？当然是有赏金的 ;)\n我这边需要清理的是那些_武僧_。你看，我打算在这儿做点小买卖，但这些狂热分子自己啥也不买，还会吓跑其他客人。所以请帮我干掉……就算 _8 个_吧，报酬绝对少不了你。";
    private static final String TXT_GOLEMS2 = "你的魔像狩猎进展如何啦？";
    private static final String TXT_MONKS2 = "哦，你还活着呢！我就知道你的功夫更胜一筹 ;) 别忘了把那些武僧的徽记带回来。";
    private static final String TXT_CYA = "我们会再见的，%s！";
    private static final String TXT_HEY = "喂喂喂，%s！";
    private boolean seenBefore;

    public Imp() {
        this.name = "野心勃勃的小恶魔";
        this.spriteClass = ImpSprite.class;
        this.seenBefore = false;
    }

    @Override
    protected boolean act() {
        if (!Quest.given && Dungeon.visible[this.pos]) {
            if (!this.seenBefore) {
                this.yell(Utils.format(TXT_HEY, Dungeon.hero.className()));
            }
            this.seenBefore = true;
        } else {
            this.seenBefore = false;
        }
        this.throwItem();
        return super.act();
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
        if (Quest.given) {
            DwarfToken tokens = Dungeon.hero.belongings.getItem(DwarfToken.class);
            if (tokens != null && (tokens.quantity() >= 8 || !Quest.alternative && tokens.quantity() >= 6)) {
                GameScene.show(new WndImp(this, tokens));
            } else {
                this.tell(Quest.alternative ? TXT_MONKS2 : TXT_GOLEMS2, Dungeon.hero.className());
            }
        } else {
            this.tell(Quest.alternative ? TXT_MONKS1 : TXT_GOLEMS1, new Object[0]);
            Quest.given = true;
            Quest.completed = false;
            Journal.add(Journal.Feature.IMP, this.pos);
        }
    }

    private void tell(String format, Object ... args) {
        GameScene.show(new WndQuest(this, Utils.format(format, args), new String[0]));
    }

    public void flee() {
        this.yell(Utils.format(TXT_CYA, Dungeon.hero.className()));
        this.destroy();
        this.sprite.die();
    }

    @Override
    public String description() {
        return "小恶魔是低阶恶魔。它们既不以力量见长也没有魔法天赋，但十分聪明且善于交际，许多小恶魔更愿意混迹于非恶魔种族之中。";
    }

    public static class Quest {
        private static boolean alternative;
        private static boolean spawned;
        private static boolean given;
        private static boolean completed;
        public static Ring reward;
        private static final String NODE = "demon";
        private static final String ALTERNATIVE = "alternative";
        private static final String SPAWNED = "spawned";
        private static final String GIVEN = "given";
        private static final String COMPLETED = "completed";
        private static final String REWARD = "reward";

        public static void reset() {
            spawned = false;
            reward = null;
        }

        public static void storeInBundle(Bundle bundle) {
            Bundle node = new Bundle();
            node.put(SPAWNED, spawned);
            if (spawned) {
                node.put(ALTERNATIVE, alternative);
                node.put(GIVEN, given);
                node.put(COMPLETED, completed);
                node.put(REWARD, reward);
            }
            bundle.put(NODE, node);
        }

        public static void restoreFromBundle(Bundle bundle) {
            Bundle node = bundle.getBundle(NODE);
            if (!node.isNull() && (spawned = node.getBoolean(SPAWNED))) {
                alternative = node.getBoolean(ALTERNATIVE);
                given = node.getBoolean(GIVEN);
                completed = node.getBoolean(COMPLETED);
                reward = (Ring)node.get(REWARD);
            }
        }

        public static void spawn(CityLevel level, Room room) {
            if (!spawned && Dungeon.depth > 16 && Random.Int(20 - Dungeon.depth) == 0) {
                Imp npc = new Imp();
                do {
                    npc.pos = level.randomRespawnCell();
                } while (npc.pos == -1 || level.heaps.get(npc.pos) != null);
                level.mobs.add(npc);
                Actor.occupyCell(npc);
                spawned = true;
                alternative = Random.Int(2) == 0;
                given = false;
                do {
                    reward = (Ring)Generator.random(Generator.Category.RING);
                } while (Quest.reward.cursed);
                reward.upgrade(2);
                Quest.reward.cursed = true;
            }
        }

        public static void process(Mob mob) {
            if (spawned && given && !completed && (alternative && mob instanceof Monk || !alternative && mob instanceof Golem)) {
                Dungeon.level.drop((Item)new DwarfToken(), (int)mob.pos).sprite.drop();
            }
        }

        public static void complete() {
            reward = null;
            completed = true;
            Journal.remove(Journal.Feature.IMP);
        }

        public static boolean isCompleted() {
            return completed;
        }
    }
}

