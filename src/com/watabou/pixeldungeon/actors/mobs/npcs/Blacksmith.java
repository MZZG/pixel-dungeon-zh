/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.mobs.npcs;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Journal;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.mobs.npcs.NPC;
import com.watabou.pixeldungeon.items.EquipableItem;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.quest.DarkGold;
import com.watabou.pixeldungeon.items.quest.Pickaxe;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.watabou.pixeldungeon.levels.Room;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.BlacksmithSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.windows.WndBlacksmith;
import com.watabou.pixeldungeon.windows.WndQuest;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import java.util.Collection;

public class Blacksmith
extends NPC {
    private static final String TXT_GOLD_1 = "喂，人类！想帮点忙吗？拿着这把镐子去给我挖些_暗金矿_，_15 块_就够了。什么，问我怎么付报酬？你这贪心的家伙……\n行吧行吧，我手头没钱付给你，但我可以帮你免费打铁。算你走运，这附近可就我这么一个铁匠。";
    private static final String TXT_BLOOD_1 = "喂，人类！想帮点忙吗？拿着这把镐子去_砸死一只蝙蝠_，我需要镐头沾上它的血。什么，问我怎么付报酬？你这贪心的家伙……\n行吧行吧，我手头没钱付给你，但我可以帮你免费打铁。算你走运，这附近可就我这么一个铁匠。";
    private static final String TXT2 = "你在逗我吗？我的镐子呢？！";
    private static final String TXT3 = "暗金矿，15 块。说真的，这有那么难吗？";
    private static final String TXT4 = "我说过镐头上要沾上蝙蝠血！动作快点！";
    private static final String TXT_COMPLETED = "哦，你终于回来了……总比不回来强。";
    private static final String TXT_GET_LOST = "我正忙着呢，滚开！";
    private static final String TXT_LOOKS_BETTER = "你的%s现在看起来好多了";

    public Blacksmith() {
        this.name = "巨魔铁匠";
        this.spriteClass = BlacksmithSprite.class;
    }

    @Override
    protected boolean act() {
        this.throwItem();
        return super.act();
    }

    @Override
    public void interact() {
        this.sprite.turnTo(this.pos, Dungeon.hero.pos);
        if (!Quest.given) {
            GameScene.show(new WndQuest(this, Quest.alternative ? TXT_BLOOD_1 : TXT_GOLD_1, new String[0]){

                @Override
                public void onBackPressed() {
                    super.onBackPressed();
                    Quest.given = true;
                    Quest.completed = false;
                    Pickaxe pick = new Pickaxe();
                    if (pick.doPickUp(Dungeon.hero)) {
                        GLog.i("你获得了%s", pick.name());
                    } else {
                        Dungeon.level.drop((Item)pick, (int)Dungeon.hero.pos).sprite.drop();
                    }
                }
            });
            Journal.add(Journal.Feature.TROLL, this.pos);
        } else if (!Quest.completed) {
            if (Quest.alternative) {
                Pickaxe pick = Dungeon.hero.belongings.getItem(Pickaxe.class);
                if (pick == null) {
                    this.tell(TXT2);
                } else if (!pick.bloodStained) {
                    this.tell(TXT4);
                } else {
                    if (pick.isEquipped(Dungeon.hero)) {
                        pick.doUnequip(Dungeon.hero, false);
                    }
                    pick.detach(Dungeon.hero.belongings.backpack);
                    this.tell(TXT_COMPLETED);
                    Quest.completed = true;
                    Quest.reforged = false;
                }
            } else {
                Pickaxe pick = Dungeon.hero.belongings.getItem(Pickaxe.class);
                DarkGold gold = Dungeon.hero.belongings.getItem(DarkGold.class);
                if (pick == null) {
                    this.tell(TXT2);
                } else if (gold == null || gold.quantity() < 15) {
                    this.tell(TXT3);
                } else {
                    if (pick.isEquipped(Dungeon.hero)) {
                        pick.doUnequip(Dungeon.hero, false);
                    }
                    pick.detach(Dungeon.hero.belongings.backpack);
                    gold.detachAll(Dungeon.hero.belongings.backpack);
                    this.tell(TXT_COMPLETED);
                    Quest.completed = true;
                    Quest.reforged = false;
                }
            }
        } else if (!Quest.reforged) {
            GameScene.show(new WndBlacksmith(this, Dungeon.hero));
        } else {
            this.tell(TXT_GET_LOST);
        }
    }

    private void tell(String text) {
        GameScene.show(new WndQuest(this, text, new String[0]));
    }

    public static String verify(Item item1, Item item2) {
        if (item1 == item2) {
            return "请选择两件不同的物品，别选同一件两次！";
        }
        if (item1.getClass() != item2.getClass()) {
            return "请选择两件同类型的物品！";
        }
        if (!item1.isIdentified() || !item2.isIdentified()) {
            return "我得先知道我在锻造什么，先把它们鉴定了吧！";
        }
        if (item1.cursed || item2.cursed) {
            return "我可不碰被诅咒的玩意儿！";
        }
        if (item1.level() < 0 || item2.level() < 0) {
            return "这东西就是废铁，品质太差了！";
        }
        if (!item1.isUpgradable() || !item2.isUpgradable()) {
            return "我没法重铸这些物品！";
        }
        return null;
    }

    public static void upgrade(Item item1, Item item2) {
        Item second;
        Item first;
        if (item2.level() > item1.level()) {
            first = item2;
            second = item1;
        } else {
            first = item1;
            second = item2;
        }
        Sample.INSTANCE.play("snd_evoke.mp3");
        ScrollOfUpgrade.upgrade(Dungeon.hero);
        Item.evoke(Dungeon.hero);
        if (first.isEquipped(Dungeon.hero)) {
            ((EquipableItem)first).doUnequip(Dungeon.hero, true);
        }
        first.upgrade();
        GLog.p(TXT_LOOKS_BETTER, first.name());
        Dungeon.hero.spendAndNext(2.0f);
        Badges.validateItemLevelAquired(first);
        if (second.isEquipped(Dungeon.hero)) {
            ((EquipableItem)second).doUnequip(Dungeon.hero, false);
        }
        second.detachAll(Dungeon.hero.belongings.backpack);
        Quest.reforged = true;
        Journal.remove(Journal.Feature.TROLL);
    }

    @Override
    public int defenseSkill(Char enemy) {
        return 1000;
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
    public String description() {
        return "这位巨魔铁匠长得和所有巨魔一样：高大精瘦，皮肤的颜色与质感犹如岩石。他正用小得不成比例的工具敲敲打打。";
    }

    public static class Quest {
        private static boolean spawned;
        private static boolean alternative;
        private static boolean given;
        private static boolean completed;
        private static boolean reforged;
        private static final String NODE = "blacksmith";
        private static final String SPAWNED = "spawned";
        private static final String ALTERNATIVE = "alternative";
        private static final String GIVEN = "given";
        private static final String COMPLETED = "completed";
        private static final String REFORGED = "reforged";

        public static void reset() {
            spawned = false;
            given = false;
            completed = false;
            reforged = false;
        }

        public static void storeInBundle(Bundle bundle) {
            Bundle node = new Bundle();
            node.put(SPAWNED, spawned);
            if (spawned) {
                node.put(ALTERNATIVE, alternative);
                node.put(GIVEN, given);
                node.put(COMPLETED, completed);
                node.put(REFORGED, reforged);
            }
            bundle.put(NODE, node);
        }

        public static void restoreFromBundle(Bundle bundle) {
            Bundle node = bundle.getBundle(NODE);
            if (!node.isNull() && (spawned = node.getBoolean(SPAWNED))) {
                alternative = node.getBoolean(ALTERNATIVE);
                given = node.getBoolean(GIVEN);
                completed = node.getBoolean(COMPLETED);
                reforged = node.getBoolean(REFORGED);
            } else {
                Quest.reset();
            }
        }

        public static void spawn(Collection<Room> rooms) {
            if (!spawned && Dungeon.depth > 11 && Random.Int(15 - Dungeon.depth) == 0) {
                for (Room r : rooms) {
                    if (r.type != Room.Type.STANDARD || r.width() <= 4 || r.height() <= 4) continue;
                    Room blacksmith = r;
                    blacksmith.type = Room.Type.BLACKSMITH;
                    spawned = true;
                    alternative = Random.Int(2) == 0;
                    given = false;
                    break;
                }
            }
        }
    }
}

