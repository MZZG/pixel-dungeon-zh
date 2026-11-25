/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.hero;

import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Bones;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.GamesInProgress;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.SacrificialFire;
import com.watabou.pixeldungeon.actors.buffs.Barkskin;
import com.watabou.pixeldungeon.actors.buffs.Bleeding;
import com.watabou.pixeldungeon.actors.buffs.Blindness;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.actors.buffs.Charm;
import com.watabou.pixeldungeon.actors.buffs.Combo;
import com.watabou.pixeldungeon.actors.buffs.Cripple;
import com.watabou.pixeldungeon.actors.buffs.Fury;
import com.watabou.pixeldungeon.actors.buffs.GasesImmunity;
import com.watabou.pixeldungeon.actors.buffs.Hunger;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.buffs.Light;
import com.watabou.pixeldungeon.actors.buffs.Ooze;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.buffs.Regeneration;
import com.watabou.pixeldungeon.actors.buffs.Roots;
import com.watabou.pixeldungeon.actors.buffs.SnipersMark;
import com.watabou.pixeldungeon.actors.buffs.Vertigo;
import com.watabou.pixeldungeon.actors.buffs.Weakness;
import com.watabou.pixeldungeon.actors.hero.Belongings;
import com.watabou.pixeldungeon.actors.hero.HeroAction;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.actors.mobs.npcs.NPC;
import com.watabou.pixeldungeon.effects.CheckedCell;
import com.watabou.pixeldungeon.effects.Flare;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.input.GameAction;
import com.watabou.pixeldungeon.input.PDInputProcessor;
import com.watabou.pixeldungeon.items.Amulet;
import com.watabou.pixeldungeon.items.Ankh;
import com.watabou.pixeldungeon.items.DewVial;
import com.watabou.pixeldungeon.items.Dewdrop;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.KindOfWeapon;
import com.watabou.pixeldungeon.items.armor.Armor;
import com.watabou.pixeldungeon.items.keys.GoldenKey;
import com.watabou.pixeldungeon.items.keys.IronKey;
import com.watabou.pixeldungeon.items.keys.SkeletonKey;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.items.potions.PotionOfStrength;
import com.watabou.pixeldungeon.items.rings.RingOfAccuracy;
import com.watabou.pixeldungeon.items.rings.RingOfDetection;
import com.watabou.pixeldungeon.items.rings.RingOfElements;
import com.watabou.pixeldungeon.items.rings.RingOfEvasion;
import com.watabou.pixeldungeon.items.rings.RingOfHaste;
import com.watabou.pixeldungeon.items.rings.RingOfShadows;
import com.watabou.pixeldungeon.items.rings.RingOfThorns;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfEnchantment;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfRecharging;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;
import com.watabou.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.Terrain;
import com.watabou.pixeldungeon.levels.features.AlchemyPot;
import com.watabou.pixeldungeon.levels.features.Chasm;
import com.watabou.pixeldungeon.levels.features.Sign;
import com.watabou.pixeldungeon.plants.Earthroot;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.scenes.InterlevelScene;
import com.watabou.pixeldungeon.scenes.SurfaceScene;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.sprites.HeroSprite;
import com.watabou.pixeldungeon.ui.AttackIndicator;
import com.watabou.pixeldungeon.ui.BuffIndicator;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.windows.WndMessage;
import com.watabou.pixeldungeon.windows.WndResurrect;
import com.watabou.pixeldungeon.windows.WndTradeItem;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Hero
extends Char {
    private static final String TXT_LEAVE = "没有人能轻轻松松离开像素地牢。";
    private static final String TXT_LEVEL_UP = "升级！";
    private static final String TXT_NEW_LEVEL = "欢迎来到 %d 级！现在你更加强壮也更加专注，更容易击中敌人并闪避攻击了。";
    public static final String TXT_YOU_NOW_HAVE = "你获得了%s";
    private static final String TXT_SOMETHING_ELSE = "这里还有别的东西";
    private static final String TXT_LOCKED_CHEST = "这个宝箱锁着，你没有对应的钥匙";
    private static final String TXT_LOCKED_DOOR = "你没有对应的钥匙";
    private static final String TXT_NOTICED_SMTH = "你注意到了什么东西";
    private static final String TXT_WAIT = "...";
    private static final String TXT_SEARCH = "搜索";
    public static final int STARTING_STR = 10;
    private static final float TIME_TO_REST = 1.0f;
    private static final float TIME_TO_SEARCH = 2.0f;
    public HeroClass heroClass = HeroClass.ROGUE;
    public HeroSubClass subClass = HeroSubClass.NONE;
    private int attackSkill = 10;
    private int defenseSkill = 5;
    public boolean ready = false;
    public HeroAction curAction = null;
    public HeroAction lastAction = null;
    private Char enemy;
    public Armor.Glyph killerGlyph = null;
    private Item theKey;
    public boolean restoreHealth = false;
    public MissileWeapon rangedWeapon = null;
    public Belongings belongings;
    public int STR;
    public boolean weakened = false;
    public float awareness;
    public int lvl = 1;
    public int exp = 0;
    private ArrayList<Mob> visibleEnemies;
    private static final String ATTACK = "attackSkill";
    private static final String DEFENSE = "defenseSkill";
    private static final String STRENGTH = "STR";
    private static final String LEVEL = "lvl";
    private static final String EXPERIENCE = "exp";

    public Hero() {
        this.name = "你";
        this.HT = 20;
        this.HP = 20;
        this.STR = 10;
        this.awareness = 0.1f;
        this.belongings = new Belongings(this);
        this.visibleEnemies = new ArrayList();
    }

    public int STR() {
        return this.weakened ? this.STR - 2 : this.STR;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        this.heroClass.storeInBundle(bundle);
        this.subClass.storeInBundle(bundle);
        bundle.put(ATTACK, this.attackSkill);
        bundle.put(DEFENSE, this.defenseSkill);
        bundle.put(STRENGTH, this.STR);
        bundle.put(LEVEL, this.lvl);
        bundle.put(EXPERIENCE, this.exp);
        this.belongings.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.heroClass = HeroClass.restoreInBundle(bundle);
        this.subClass = HeroSubClass.restoreInBundle(bundle);
        this.attackSkill = bundle.getInt(ATTACK);
        this.defenseSkill = bundle.getInt(DEFENSE);
        this.STR = bundle.getInt(STRENGTH);
        this.updateAwareness();
        this.lvl = bundle.getInt(LEVEL);
        this.exp = bundle.getInt(EXPERIENCE);
        this.belongings.restoreFromBundle(bundle);
    }

    public static void preview(GamesInProgress.Info info, Bundle bundle) {
        info.subClass = HeroSubClass.restoreInBundle(bundle);
        info.level = bundle.getInt(LEVEL);
    }

    public String className() {
        return this.subClass == null || this.subClass == HeroSubClass.NONE ? this.heroClass.title() : this.subClass.title();
    }

    public void live() {
        Buff.affect(this, Regeneration.class);
        Buff.affect(this, Hunger.class);
    }

    public int tier() {
        return this.belongings.armor == null ? 0 : this.belongings.armor.tier;
    }

    public boolean shoot(Char enemy, MissileWeapon wep) {
        this.rangedWeapon = wep;
        boolean result = this.attack(enemy);
        this.rangedWeapon = null;
        return result;
    }

    @Override
    public int attackSkill(Char target) {
        KindOfWeapon kindOfWeapon;
        float accuracy;
        int bonus = 0;
        for (Buff buff : this.buffs(RingOfAccuracy.Accuracy.class)) {
            bonus += ((RingOfAccuracy.Accuracy)buff).level;
        }
        float f = accuracy = bonus == 0 ? 1.0f : (float)Math.pow(1.4, bonus);
        if (this.rangedWeapon != null && Level.distance(this.pos, target.pos) == 1) {
            accuracy *= 0.5f;
        }
        KindOfWeapon kindOfWeapon2 = kindOfWeapon = this.rangedWeapon != null ? this.rangedWeapon : this.belongings.weapon;
        if (kindOfWeapon != null) {
            return (int)((float)this.attackSkill * accuracy * kindOfWeapon.acuracyFactor(this));
        }
        return (int)((float)this.attackSkill * accuracy);
    }

    @Override
    public int defenseSkill(Char enemy) {
        int n;
        float evasion;
        int bonus = 0;
        for (Buff buff : this.buffs(RingOfEvasion.Evasion.class)) {
            bonus += ((RingOfEvasion.Evasion)buff).level;
        }
        float f = evasion = bonus == 0 ? 1.0f : (float)Math.pow(1.2, bonus);
        if (this.paralysed) {
            evasion /= 2.0f;
        }
        int n2 = n = this.belongings.armor != null ? this.belongings.armor.STR - this.STR() : 0;
        if (n > 0) {
            return (int)((double)((float)this.defenseSkill * evasion) / Math.pow(1.5, n));
        }
        if (this.heroClass == HeroClass.ROGUE) {
            if (this.curAction != null && this.subClass == HeroSubClass.FREERUNNER && !this.isStarving()) {
                evasion *= 2.0f;
            }
            return (int)((float)(this.defenseSkill - n) * evasion);
        }
        return (int)((float)this.defenseSkill * evasion);
    }

    @Override
    public int dr() {
        int dr = this.belongings.armor != null ? Math.max(this.belongings.armor.DR(), 0) : 0;
        Barkskin barkskin = this.buff(Barkskin.class);
        if (barkskin != null) {
            dr += barkskin.level();
        }
        return dr;
    }

    @Override
    public int damageRoll() {
        KindOfWeapon wep;
        KindOfWeapon kindOfWeapon = wep = this.rangedWeapon != null ? this.rangedWeapon : this.belongings.weapon;
        int dmg = wep != null ? wep.damageRoll(this) : (this.STR() > 10 ? Random.IntRange(1, this.STR() - 9) : 1);
        return this.buff(Fury.class) != null ? (int)((float)dmg * 1.5f) : dmg;
    }

    @Override
    public float speed() {
        int aEnc;
        int n = aEnc = this.belongings.armor != null ? this.belongings.armor.STR - this.STR() : 0;
        if (aEnc > 0) {
            return (float)((double)super.speed() * Math.pow(1.3, -aEnc));
        }
        float speed = super.speed();
        return ((HeroSprite)this.sprite).sprint(this.subClass == HeroSubClass.FREERUNNER && !this.isStarving()) ? 1.6f * speed : speed;
    }

    public float attackDelay() {
        KindOfWeapon wep = this.rangedWeapon != null ? this.rangedWeapon : this.belongings.weapon;
        float delay = wep != null ? wep.speedFactor(this) : 1.0f;
        switch (this.subClass) {
            case BERSERKER: {
                int count = 0;
                for (int n : Level.NEIGHBOURS8) {
                    if (Actor.findChar(this.pos + n) == null) continue;
                    ++count;
                }
                if (count <= 1) break;
                delay *= 1.0f - (float)count / 16.0f;
                break;
            }
        }
        return delay;
    }

    @Override
    public void spend(float time) {
        int hasteLevel = 0;
        for (Buff buff : this.buffs(RingOfHaste.Haste.class)) {
            hasteLevel += ((RingOfHaste.Haste)buff).level;
        }
        super.spend(hasteLevel == 0 ? time : (float)((double)time * Math.pow(1.1, -hasteLevel)));
    }

    public void spendAndNext(float time) {
        this.busy();
        this.spend(time);
        this.next();
    }

    @Override
    public boolean act() {
        super.act();
        if (this.paralysed) {
            this.curAction = null;
            this.spendAndNext(1.0f);
            return false;
        }
        this.checkVisibleMobs();
        AttackIndicator.updateState();
        if (this.curAction == null) {
            if (this.restoreHealth) {
                if (this.isStarving() || this.HP >= this.HT) {
                    this.restoreHealth = false;
                } else {
                    this.spend(1.0f);
                    this.next();
                    return false;
                }
            }
            this.ready();
            return false;
        }
        this.restoreHealth = false;
        this.ready = false;
        if (this.curAction instanceof HeroAction.Move) {
            return this.actMove((HeroAction.Move)this.curAction);
        }
        if (this.curAction instanceof HeroAction.Interact) {
            return this.actInteract((HeroAction.Interact)this.curAction);
        }
        if (this.curAction instanceof HeroAction.Buy) {
            return this.actBuy((HeroAction.Buy)this.curAction);
        }
        if (this.curAction instanceof HeroAction.PickUp) {
            return this.actPickUp((HeroAction.PickUp)this.curAction);
        }
        if (this.curAction instanceof HeroAction.OpenChest) {
            return this.actOpenChest((HeroAction.OpenChest)this.curAction);
        }
        if (this.curAction instanceof HeroAction.Unlock) {
            return this.actUnlock((HeroAction.Unlock)this.curAction);
        }
        if (this.curAction instanceof HeroAction.Descend) {
            return this.actDescend((HeroAction.Descend)this.curAction);
        }
        if (this.curAction instanceof HeroAction.Ascend) {
            return this.actAscend((HeroAction.Ascend)this.curAction);
        }
        if (this.curAction instanceof HeroAction.Attack) {
            return this.actAttack((HeroAction.Attack)this.curAction);
        }
        if (this.curAction instanceof HeroAction.Cook) {
            return this.actCook((HeroAction.Cook)this.curAction);
        }
        return false;
    }

    public void busy() {
        this.ready = false;
    }

    private void ready() {
        if (this.curAction instanceof HeroAction.Move) {
            PDInputProcessor ip = (PDInputProcessor)Game.instance.getInputProcessor();
            for (GameAction a : GameAction.MOVEMENT) {
                if (!ip.isKeyPressed(a)) continue;
                HeroAction action = this.getActionFor(this.pos + GameAction.action2Offset(a));
                if (action instanceof HeroAction.Descend || action instanceof HeroAction.Ascend) {
                    action = new HeroAction.Move(action.dst);
                }
                if (action instanceof HeroAction.Move && !Level.passable[action.dst]) continue;
                this.curAction = action;
                this.lastAction = null;
                this.next();
                return;
            }
        }
        this.sprite.idle();
        this.curAction = null;
        this.ready = true;
        CharSprite.slowmo = false;
        GameScene.ready();
    }

    public void interrupt() {
        if (this.isAlive() && this.curAction != null && this.curAction.dst != this.pos) {
            this.lastAction = this.curAction;
        }
        this.curAction = null;
    }

    public void resume() {
        this.curAction = this.lastAction;
        this.lastAction = null;
        this.act();
    }

    private boolean actMove(HeroAction.Move action) {
        if (this.getCloser(action.dst)) {
            return true;
        }
        if (Dungeon.level.map[this.pos] == 29) {
            Sign.read(this.pos);
        }
        this.ready();
        return false;
    }

    private boolean actInteract(HeroAction.Interact action) {
        NPC npc = action.npc;
        if (Level.adjacent(this.pos, npc.pos)) {
            this.ready();
            this.sprite.turnTo(this.pos, npc.pos);
            npc.interact();
            return false;
        }
        if (Level.fieldOfView[npc.pos] && this.getCloser(npc.pos)) {
            return true;
        }
        this.ready();
        return false;
    }

    private boolean actBuy(HeroAction.Buy action) {
        int dst = action.dst;
        if (this.pos == dst || Level.adjacent(this.pos, dst)) {
            this.ready();
            Heap heap = (Heap)Dungeon.level.heaps.get(dst);
            if (heap != null && heap.type == Heap.Type.FOR_SALE && heap.size() == 1) {
                GameScene.show(new WndTradeItem(heap, true));
            }
            return false;
        }
        if (this.getCloser(dst)) {
            return true;
        }
        this.ready();
        return false;
    }

    private boolean actCook(HeroAction.Cook action) {
        int dst = action.dst;
        if (Dungeon.visible[dst]) {
            this.ready();
            AlchemyPot.operate(this, dst);
            return false;
        }
        if (this.getCloser(dst)) {
            return true;
        }
        this.ready();
        return false;
    }

    private boolean actPickUp(HeroAction.PickUp action) {
        int dst = action.dst;
        if (this.pos == dst) {
            Heap heap = (Heap)Dungeon.level.heaps.get(this.pos);
            if (heap != null) {
                Item item = heap.pickUp();
                if (item.doPickUp(this)) {
                    if (!(item instanceof Dewdrop)) {
                        boolean important;
                        boolean bl = important = (item instanceof ScrollOfUpgrade || item instanceof ScrollOfEnchantment) && ((Scroll)item).isKnown() || item instanceof PotionOfStrength && ((Potion)item).isKnown();
                        if (important) {
                            GLog.p(TXT_YOU_NOW_HAVE, item.name());
                        } else {
                            GLog.i(TXT_YOU_NOW_HAVE, item.name());
                        }
                    }
                    if (!heap.isEmpty()) {
                        GLog.i(TXT_SOMETHING_ELSE, new Object[0]);
                    }
                    this.curAction = null;
                } else {
                    Dungeon.level.drop((Item)item, (int)this.pos).sprite.drop();
                    this.ready();
                }
            } else {
                this.ready();
            }
            return false;
        }
        if (this.getCloser(dst)) {
            return true;
        }
        this.ready();
        return false;
    }

    private boolean actOpenChest(HeroAction.OpenChest action) {
        int dst = action.dst;
        if (Level.adjacent(this.pos, dst) || this.pos == dst) {
            Heap heap = (Heap)Dungeon.level.heaps.get(dst);
            if (heap != null && heap.type != Heap.Type.HEAP && heap.type != Heap.Type.FOR_SALE) {
                this.theKey = null;
                if (heap.type == Heap.Type.LOCKED_CHEST || heap.type == Heap.Type.CRYSTAL_CHEST) {
                    this.theKey = this.belongings.getKey(GoldenKey.class, Dungeon.depth);
                    if (this.theKey == null) {
                        GLog.w(TXT_LOCKED_CHEST, new Object[0]);
                        this.ready();
                        return false;
                    }
                }
                switch (heap.type) {
                    case TOMB: {
                        Sample.INSTANCE.play("snd_tomb.mp3");
                        Camera.main.shake(1.0f, 0.5f);
                        break;
                    }
                    case SKELETON: {
                        break;
                    }
                    default: {
                        Sample.INSTANCE.play("snd_unlock.mp3");
                    }
                }
                this.spend(1.0f);
                this.sprite.operate(dst);
            } else {
                this.ready();
            }
            return false;
        }
        if (this.getCloser(dst)) {
            return true;
        }
        this.ready();
        return false;
    }

    private boolean actUnlock(HeroAction.Unlock action) {
        int doorCell = action.dst;
        if (Level.adjacent(this.pos, doorCell)) {
            this.theKey = null;
            int door = Dungeon.level.map[doorCell];
            if (door == 10) {
                this.theKey = this.belongings.getKey(IronKey.class, Dungeon.depth);
            } else if (door == 25) {
                this.theKey = this.belongings.getKey(SkeletonKey.class, Dungeon.depth);
            }
            if (this.theKey != null) {
                this.spend(1.0f);
                this.sprite.operate(doorCell);
                Sample.INSTANCE.play("snd_unlock.mp3");
            } else {
                GLog.w(TXT_LOCKED_DOOR, new Object[0]);
                this.ready();
            }
            return false;
        }
        if (this.getCloser(doorCell)) {
            return true;
        }
        this.ready();
        return false;
    }

    private boolean actDescend(HeroAction.Descend action) {
        int stairs = action.dst;
        if (this.pos == stairs && this.pos == Dungeon.level.exit) {
            this.curAction = null;
            Hunger hunger = this.buff(Hunger.class);
            if (hunger != null && !hunger.isStarving()) {
                hunger.satisfy(-36.0f);
            }
            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
            Game.switchScene(InterlevelScene.class);
            return false;
        }
        if (this.getCloser(stairs)) {
            return true;
        }
        this.ready();
        return false;
    }

    private boolean actAscend(HeroAction.Ascend action) {
        int stairs = action.dst;
        if (this.pos == stairs && this.pos == Dungeon.level.entrance) {
            if (Dungeon.depth == 1) {
                if (this.belongings.getItem(Amulet.class) == null) {
                    GameScene.show(new WndMessage(TXT_LEAVE));
                    this.ready();
                } else {
                    Dungeon.win("获得Yendor护符");
                    Dungeon.deleteGame(Dungeon.hero.heroClass, true);
                    Game.switchScene(SurfaceScene.class);
                }
            } else {
                this.curAction = null;
                Hunger hunger = this.buff(Hunger.class);
                if (hunger != null && !hunger.isStarving()) {
                    hunger.satisfy(-36.0f);
                }
                InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
                Game.switchScene(InterlevelScene.class);
            }
            return false;
        }
        if (this.getCloser(stairs)) {
            return true;
        }
        this.ready();
        return false;
    }

    private boolean actAttack(HeroAction.Attack action) {
        this.enemy = action.target;
        if (Level.adjacent(this.pos, this.enemy.pos) && this.enemy.isAlive() && !this.isCharmedBy(this.enemy)) {
            this.spend(this.attackDelay());
            this.sprite.attack(this.enemy.pos);
            return false;
        }
        if (Level.fieldOfView[this.enemy.pos] && this.getCloser(this.enemy.pos)) {
            return true;
        }
        this.ready();
        return false;
    }

    public void rest(boolean tillHealthy) {
        this.spendAndNext(1.0f);
        if (!tillHealthy) {
            this.sprite.showStatus(0xFFFFFF, TXT_WAIT, new Object[0]);
        }
        this.restoreHealth = tillHealthy;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        KindOfWeapon wep;
        SacrificialFire.Marked fire = this.buff(SacrificialFire.Marked.class);
        if (fire != null) {
            Buff.prolong(enemy, SacrificialFire.Marked.class, 5.0f);
        }
        KindOfWeapon kindOfWeapon = wep = this.rangedWeapon != null ? this.rangedWeapon : this.belongings.weapon;
        if (wep != null) {
            wep.proc(this, enemy, damage);
            switch (this.subClass) {
                case GLADIATOR: {
                    if (!(wep instanceof MeleeWeapon)) break;
                    damage += Buff.affect(this, Combo.class).hit(enemy, damage);
                    break;
                }
                case BATTLEMAGE: {
                    if (!(wep instanceof Wand)) break;
                    Wand wand = (Wand)wep;
                    if (wand.curCharges >= wand.maxCharges) {
                        wand.use();
                    } else if (damage > 0) {
                        ++wand.curCharges;
                        wand.updateQuickslot();
                        ScrollOfRecharging.charge(this);
                    }
                    damage += wand.curCharges;
                    break;
                }
                case SNIPER: {
                    if (this.rangedWeapon == null) break;
                    Buff.prolong((Char)this, SnipersMark.class, (float)(this.attackDelay() * 1.1f)).object = enemy.id();
                    break;
                }
            }
        }
        return damage;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        Earthroot.Armor armor;
        int dmg;
        RingOfThorns.Thorns thorns = this.buff(RingOfThorns.Thorns.class);
        if (thorns != null && (dmg = Random.IntRange(0, damage)) > 0) {
            enemy.damage(dmg, thorns);
        }
        if ((armor = this.buff(Earthroot.Armor.class)) != null) {
            damage = armor.absorb(damage);
        }
        if (this.belongings.armor != null) {
            damage = this.belongings.armor.proc(enemy, this, damage);
        }
        return damage;
    }

    @Override
    public void damage(int dmg, Object src) {
        this.restoreHealth = false;
        super.damage(dmg, src);
        if (this.subClass == HeroSubClass.BERSERKER && 0 < this.HP && (float)this.HP <= (float)this.HT * Fury.LEVEL) {
            Buff.affect(this, Fury.class);
        }
    }

    private void checkVisibleMobs() {
        ArrayList<Mob> visible = new ArrayList<Mob>();
        boolean newMob = false;
        for (Mob m : Dungeon.level.mobs) {
            if (!Level.fieldOfView[m.pos] || !m.hostile) continue;
            visible.add(m);
            if (this.visibleEnemies.contains(m)) continue;
            newMob = true;
        }
        if (newMob) {
            this.interrupt();
            this.restoreHealth = false;
        }
        this.visibleEnemies = visible;
    }

    public int visibleEnemies() {
        return this.visibleEnemies.size();
    }

    public Mob visibleEnemy(int index) {
        return this.visibleEnemies.get(index % this.visibleEnemies.size());
    }

    private boolean getCloser(int target) {
        if (this.rooted) {
            Camera.main.shake(1.0f, 1.0f);
            return false;
        }
        int step = -1;
        if (Level.adjacent(this.pos, target)) {
            if (Actor.findChar(target) == null) {
                if (Level.pit[target] && !this.flying && !Chasm.jumpConfirmed) {
                    Chasm.heroJump(this);
                    this.interrupt();
                    return false;
                }
                if (Level.passable[target] || Level.avoid[target]) {
                    step = target;
                }
            }
        } else {
            int len = 1024;
            boolean[] p = Level.passable;
            boolean[] v = Dungeon.level.visited;
            boolean[] m = Dungeon.level.mapped;
            boolean[] passable = new boolean[len];
            for (int i = 0; i < len; ++i) {
                passable[i] = p[i] && (v[i] || m[i]);
            }
            step = Dungeon.findPath(this, this.pos, target, passable, Level.fieldOfView);
        }
        if (step != -1) {
            int oldPos = this.pos;
            this.move(step);
            this.sprite.move(oldPos, this.pos);
            this.spend(1.0f / this.speed());
            return true;
        }
        return false;
    }

    public HeroAction getActionFor(int cell) {
        Heap heap;
        Char ch;
        if (cell == -1) {
            return null;
        }
        if (PDInputProcessor.modifier2) {
            return new HeroAction.Move(cell);
        }
        if (Dungeon.level.map[cell] == 42 && cell != this.pos) {
            return new HeroAction.Cook(cell);
        }
        if (Level.fieldOfView[cell] && (ch = Actor.findChar(cell)) instanceof Mob) {
            if (ch instanceof NPC) {
                return new HeroAction.Interact((NPC)ch);
            }
            return new HeroAction.Attack(ch);
        }
        if (Level.fieldOfView[cell] && (heap = (Heap)Dungeon.level.heaps.get(cell)) != null && heap.type != Heap.Type.HIDDEN) {
            switch (heap.type) {
                case HEAP: {
                    return new HeroAction.PickUp(cell);
                }
                case FOR_SALE: {
                    return heap.size() == 1 && heap.peek().price() > 0 ? new HeroAction.Buy(cell) : new HeroAction.PickUp(cell);
                }
            }
            return new HeroAction.OpenChest(cell);
        }
        if (Dungeon.level.map[cell] == 10 || Dungeon.level.map[cell] == 25) {
            return new HeroAction.Unlock(cell);
        }
        if (cell == Dungeon.level.exit) {
            return new HeroAction.Descend(cell);
        }
        if (cell == Dungeon.level.entrance) {
            return new HeroAction.Ascend(cell);
        }
        return new HeroAction.Move(cell);
    }

    public boolean handle(int cell) {
        HeroAction action = this.getActionFor(cell);
        if (action == null) {
            return false;
        }
        if (CharSprite.slowmo && (action instanceof HeroAction.Ascend || action instanceof HeroAction.Descend)) {
            action = new HeroAction.Move(action.dst);
        }
        this.curAction = action;
        if (this.curAction instanceof HeroAction.Move) {
            this.lastAction = null;
        }
        return this.act();
    }

    public void earnExp(int exp) {
        this.exp += exp;
        boolean levelUp = false;
        while (this.exp >= this.maxExp()) {
            this.exp -= this.maxExp();
            ++this.lvl;
            this.HT += 5;
            this.HP += 5;
            ++this.attackSkill;
            ++this.defenseSkill;
            if (this.lvl < 10) {
                this.updateAwareness();
            }
            levelUp = true;
        }
        if (levelUp) {
            GLog.p(TXT_NEW_LEVEL, this.lvl);
            this.sprite.showStatus(65280, TXT_LEVEL_UP, new Object[0]);
            Sample.INSTANCE.play("snd_levelup.mp3");
            Badges.validateLevelReached();
        }
        if (this.subClass == HeroSubClass.WARLOCK) {
            int value = Math.min(this.HT - this.HP, 1 + (Dungeon.depth - 1) / 5);
            if (value > 0) {
                this.HP += value;
                this.sprite.emitter().burst(Speck.factory(0), 1);
            }
            this.buff(Hunger.class).satisfy(10.0f);
        }
    }

    public int maxExp() {
        return 5 + this.lvl * 5;
    }

    void updateAwareness() {
        this.awareness = (float)(1.0 - Math.pow(this.heroClass == HeroClass.ROGUE ? 0.85 : 0.9, (double)(1 + Math.min(this.lvl, 9)) * 0.5));
    }

    public boolean isStarving() {
        return this.buff(Hunger.class).isStarving();
    }

    @Override
    public void add(Buff buff) {
        super.add(buff);
        if (this.sprite != null) {
            if (buff instanceof Burning) {
                GLog.w("你着火了！", new Object[0]);
                this.interrupt();
            } else if (buff instanceof Paralysis) {
                GLog.w("你被麻痹了！", new Object[0]);
                this.interrupt();
            } else if (buff instanceof Poison) {
                GLog.w("你中毒了！", new Object[0]);
                this.interrupt();
            } else if (buff instanceof Ooze) {
                GLog.w("腐蚀淤泥正在侵蚀你的血肉，快找水冲掉！", new Object[0]);
            } else if (buff instanceof Roots) {
                GLog.w("你不能移动了！", new Object[0]);
            } else if (buff instanceof Weakness) {
                GLog.w("你感到一阵虚弱！", new Object[0]);
            } else if (buff instanceof Blindness) {
                GLog.w("你失明了！", new Object[0]);
            } else if (buff instanceof Fury) {
                GLog.w("你陷入了狂怒！", new Object[0]);
                this.sprite.showStatus(65280, "狂怒", new Object[0]);
            } else if (buff instanceof Charm) {
                GLog.w("你被魅惑了！", new Object[0]);
            } else if (buff instanceof Cripple) {
                GLog.w("你残废了！", new Object[0]);
            } else if (buff instanceof Bleeding) {
                GLog.w("你正在流血！", new Object[0]);
            } else if (buff instanceof Vertigo) {
                GLog.w("四周的天地都在旋转！", new Object[0]);
                this.interrupt();
            } else if (buff instanceof Light) {
                this.sprite.add(CharSprite.State.ILLUMINATED);
            }
        }
        BuffIndicator.refreshHero();
    }

    @Override
    public void remove(Buff buff) {
        super.remove(buff);
        if (buff instanceof Light) {
            this.sprite.remove(CharSprite.State.ILLUMINATED);
        }
        BuffIndicator.refreshHero();
    }

    @Override
    public int stealth() {
        int stealth = super.stealth();
        for (Buff buff : this.buffs(RingOfShadows.Shadows.class)) {
            stealth += ((RingOfShadows.Shadows)buff).level;
        }
        return stealth;
    }

    @Override
    public void die(Object cause) {
        this.lastAction = null;
        this.curAction = null;
        DewVial.autoDrink(this);
        if (this.isAlive()) {
            new Flare(8, 32.0f).color(0xFFFF66, true).show(this.sprite, 2.0f);
            return;
        }
        Actor.fixTime();
        super.die(cause);
        Ankh ankh = this.belongings.getItem(Ankh.class);
        if (ankh == null) {
            Hero.reallyDie(cause);
        } else {
            Dungeon.deleteGame(Dungeon.hero.heroClass, false);
            GameScene.show(new WndResurrect(ankh, cause));
        }
    }

    public static void reallyDie(Object cause) {
        int length = 1024;
        int[] map = Dungeon.level.map;
        boolean[] visited = Dungeon.level.visited;
        boolean[] discoverable = Level.discoverable;
        for (int i = 0; i < length; ++i) {
            int terr = map[i];
            if (!discoverable[i]) continue;
            visited[i] = true;
            if ((Terrain.flags[terr] & 8) == 0) continue;
            Level.set(i, Terrain.discover(terr));
            GameScene.updateMap(i);
        }
        Bones.leave();
        Dungeon.observe();
        Dungeon.hero.belongings.identify();
        int pos = Dungeon.hero.pos;
        ArrayList<Integer> passable = new ArrayList<Integer>();
        int[] nArray = Level.NEIGHBOURS8;
        int n = nArray.length;
        for (int i = 0; i < n; ++i) {
            Integer ofs = nArray[i];
            int cell = pos + ofs;
            if (!Level.passable[cell] && !Level.avoid[cell] || Dungeon.level.heaps.get(cell) != null) continue;
            passable.add(cell);
        }
        Collections.shuffle(passable);
        ArrayList<Item> items = new ArrayList<Item>(Dungeon.hero.belongings.backpack.items);
        for (Integer cell : passable) {
            if (items.isEmpty()) break;
            Item item = Random.element(items);
            Dungeon.level.drop((Item)item, (int)cell.intValue()).sprite.drop(pos);
            items.remove(item);
        }
        GameScene.gameOver();
        if (cause instanceof Doom) {
            ((Doom)cause).onDeath();
        }
        Dungeon.deleteGame(Dungeon.hero.heroClass, true);
    }

    @Override
    public void move(int step) {
        super.move(step);
        if (!this.flying) {
            if (Level.water[this.pos]) {
                Sample.INSTANCE.play("snd_water.mp3", 1.0f, Random.Float(0.8f, 1.25f), 0.0f);
            } else {
                Sample.INSTANCE.play("snd_step.mp3");
            }
            Dungeon.level.press(this.pos, this);
        }
    }

    @Override
    public void onMotionComplete() {
        Dungeon.observe();
        this.search(false);
        super.onMotionComplete();
    }

    @Override
    public void onAttackComplete() {
        AttackIndicator.target(this.enemy);
        this.attack(this.enemy);
        this.curAction = null;
        Invisibility.dispel();
        super.onAttackComplete();
    }

    @Override
    public void onOperateComplete() {
        if (this.curAction instanceof HeroAction.Unlock) {
            int door;
            int doorCell;
            if (this.theKey != null) {
                this.theKey.detach(this.belongings.backpack);
                this.theKey = null;
            }
            doorCell = ((HeroAction.Unlock)this.curAction).dst;
            door = Dungeon.level.map[doorCell];
            Level.set(doorCell, door == 10 ? 5 : 26);
            GameScene.updateMap(doorCell);
        } else if (this.curAction instanceof HeroAction.OpenChest) {
            if (this.theKey != null) {
                this.theKey.detach(this.belongings.backpack);
                this.theKey = null;
            }
            Heap heap = (Heap)Dungeon.level.heaps.get(((HeroAction.OpenChest)this.curAction).dst);
            if (heap.type == Heap.Type.SKELETON) {
                Sample.INSTANCE.play("snd_bones.mp3");
            }
            heap.open(this);
        }
        this.curAction = null;
        super.onOperateComplete();
    }

    public boolean search(boolean intentional) {
        int by;
        int ay;
        int bx;
        float f;
        boolean smthFound = false;
        int positive = 0;
        int negative = 0;
        for (Buff buff : this.buffs(RingOfDetection.Detection.class)) {
            int bonus = ((RingOfDetection.Detection)buff).level;
            if (bonus > positive) {
                positive = bonus;
                continue;
            }
            if (bonus >= 0) continue;
            negative += bonus;
        }
        int distance = 1 + positive + negative;
        float f2 = f = intentional ? 2.0f * this.awareness - this.awareness * this.awareness : this.awareness;
        if (distance <= 0) {
            f /= (float)(2 - distance);
            distance = 1;
        }
        int cx = this.pos % 32;
        int cy = this.pos / 32;
        int ax = cx - distance;
        if (ax < 0) {
            ax = 0;
        }
        if ((bx = cx + distance) >= 32) {
            bx = 31;
        }
        if ((ay = cy - distance) < 0) {
            ay = 0;
        }
        if ((by = cy + distance) >= 32) {
            by = 31;
        }
        for (int y = ay; y <= by; ++y) {
            int x = ax;
            int p = ax + y * 32;
            while (x <= bx) {
                if (Dungeon.visible[p]) {
                    Heap heap;
                    if (intentional) {
                        this.sprite.parent.addToBack(new CheckedCell(p));
                    }
                    if (Level.secret[p] && (intentional || Random.Float() < f)) {
                        int oldValue = Dungeon.level.map[p];
                        GameScene.discoverTile(p, oldValue);
                        Level.set(p, Terrain.discover(oldValue));
                        GameScene.updateMap(p);
                        ScrollOfMagicMapping.discover(p);
                        smthFound = true;
                    }
                    if (intentional && (heap = (Heap)Dungeon.level.heaps.get(p)) != null && heap.type == Heap.Type.HIDDEN) {
                        heap.open(this);
                        smthFound = true;
                    }
                }
                ++x;
                ++p;
            }
        }
        if (intentional) {
            this.sprite.showStatus(0xFFFFFF, TXT_SEARCH, new Object[0]);
            this.sprite.operate(this.pos);
            if (smthFound) {
                this.spendAndNext(Random.Float() < f ? 2.0f : 4.0f);
            } else {
                this.spendAndNext(2.0f);
            }
        }
        if (smthFound) {
            GLog.w(TXT_NOTICED_SMTH, new Object[0]);
            Sample.INSTANCE.play("snd_secret.mp3");
            this.interrupt();
        }
        return smthFound;
    }

    public void resurrect(int resetLevel) {
        this.HP = this.HT;
        Dungeon.gold = 0;
        this.exp = 0;
        this.belongings.resurrect(resetLevel);
        this.live();
    }

    @Override
    public HashSet<Class<?>> resistances() {
        RingOfElements.Resistance r = this.buff(RingOfElements.Resistance.class);
        return r == null ? super.resistances() : r.resistances();
    }

    @Override
    public HashSet<Class<?>> immunities() {
        GasesImmunity buff = this.buff(GasesImmunity.class);
        return buff == null ? super.immunities() : GasesImmunity.IMMUNITIES;
    }

    @Override
    public void next() {
        super.next();
    }

    public static interface Doom {
        public void onDeath();
    }
}

