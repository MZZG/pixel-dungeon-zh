/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.actors.blobs;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Journal;
import com.watabou.pixeldungeon.actors.blobs.WellWater;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Disguise;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.BlobEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.Generator;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.items.potions.PotionOfMight;
import com.watabou.pixeldungeon.items.potions.PotionOfStrength;
import com.watabou.pixeldungeon.items.rings.Ring;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfEnchantment;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.items.weapon.melee.BattleAxe;
import com.watabou.pixeldungeon.items.weapon.melee.Dagger;
import com.watabou.pixeldungeon.items.weapon.melee.Glaive;
import com.watabou.pixeldungeon.items.weapon.melee.Knuckles;
import com.watabou.pixeldungeon.items.weapon.melee.Longsword;
import com.watabou.pixeldungeon.items.weapon.melee.Mace;
import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;
import com.watabou.pixeldungeon.items.weapon.melee.Quarterstaff;
import com.watabou.pixeldungeon.items.weapon.melee.Spear;
import com.watabou.pixeldungeon.items.weapon.melee.Sword;
import com.watabou.pixeldungeon.items.weapon.melee.WarHammer;
import com.watabou.pixeldungeon.plants.Plant;
import com.watabou.pixeldungeon.sprites.HeroSprite;
import com.watabou.pixeldungeon.utils.GLog;

public class WaterOfTransmutation
extends WellWater {
    private static final String TXT_PROCCED = "你小酌一口后，隐约感到体内有什么发生了变化。";

    @Override
    protected Item affectItem(Item item) {
        if ((item = item instanceof MeleeWeapon ? this.changeWeapon((MeleeWeapon)item) : (item instanceof Scroll ? this.changeScroll((Scroll)item) : (item instanceof Potion ? this.changePotion((Potion)item) : (item instanceof Ring ? this.changeRing((Ring)item) : (item instanceof Wand ? this.changeWand((Wand)item) : (item instanceof Plant.Seed ? this.changeSeed((Plant.Seed)item) : null)))))) != null) {
            Journal.remove(Journal.Feature.WELL_OF_TRANSMUTATION);
        }
        return item;
    }

    @Override
    protected boolean affectHero(Hero hero) {
        Sample.INSTANCE.play("snd_drink.mp3");
        Disguise disguise = hero.buff(Disguise.class);
        if (disguise != null) {
            Buff.affect(hero, Disguise.class, 100.0f).choose(hero.heroClass, disguise.costume);
        } else {
            Buff.affect(hero, Disguise.class, 100.0f).choose(hero.heroClass, null);
        }
        Dungeon.hero.interrupt();
        ((HeroSprite)hero.sprite).updateTexture();
        GLog.h(TXT_PROCCED, new Object[0]);
        Journal.remove(Journal.Feature.WELL_OF_TRANSMUTATION);
        return true;
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.start(Speck.factory(10), 0.2f, 0);
    }

    private MeleeWeapon changeWeapon(MeleeWeapon w) {
        MeleeWeapon n = null;
        if (w instanceof Knuckles) {
            n = new Dagger();
        } else if (w instanceof Dagger) {
            n = new Knuckles();
        } else if (w instanceof Spear) {
            n = new Quarterstaff();
        } else if (w instanceof Quarterstaff) {
            n = new Spear();
        } else if (w instanceof Sword) {
            n = new Mace();
        } else if (w instanceof Mace) {
            n = new Sword();
        } else if (w instanceof Longsword) {
            n = new BattleAxe();
        } else if (w instanceof BattleAxe) {
            n = new Longsword();
        } else if (w instanceof Glaive) {
            n = new WarHammer();
        } else if (w instanceof WarHammer) {
            n = new Glaive();
        }
        if (n != null) {
            int level = w.level();
            if (level > 0) {
                n.upgrade(level);
            } else if (level < 0) {
                n.degrade(-level);
            }
            if (w.isEnchanted()) {
                n.enchant();
            }
            n.levelKnown = w.levelKnown;
            n.cursedKnown = w.cursedKnown;
            n.cursed = w.cursed;
            Journal.remove(Journal.Feature.WELL_OF_TRANSMUTATION);
            return n;
        }
        return null;
    }

    private Ring changeRing(Ring r) {
        Ring n;
        while ((n = (Ring)Generator.random(Generator.Category.RING)).getClass() == r.getClass()) {
        }
        n.level(0);
        int level = r.level();
        if (level > 0) {
            n.upgrade(level);
        } else if (level < 0) {
            n.degrade(-level);
        }
        n.levelKnown = r.levelKnown;
        n.cursedKnown = r.cursedKnown;
        n.cursed = r.cursed;
        return n;
    }

    private Wand changeWand(Wand w) {
        Wand n;
        while ((n = (Wand)Generator.random(Generator.Category.WAND)).getClass() == w.getClass()) {
        }
        n.level(0);
        n.upgrade(w.level());
        n.levelKnown = w.levelKnown;
        n.cursedKnown = w.cursedKnown;
        n.cursed = w.cursed;
        return n;
    }

    private Plant.Seed changeSeed(Plant.Seed s) {
        Plant.Seed n;
        while ((n = (Plant.Seed)Generator.random(Generator.Category.SEED)).getClass() == s.getClass()) {
        }
        return n;
    }

    private Scroll changeScroll(Scroll s) {
        Scroll n;
        if (s instanceof ScrollOfUpgrade) {
            return new ScrollOfEnchantment();
        }
        if (s instanceof ScrollOfEnchantment) {
            return new ScrollOfUpgrade();
        }
        while ((n = (Scroll)Generator.random(Generator.Category.SCROLL)).getClass() == s.getClass()) {
        }
        return n;
    }

    private Potion changePotion(Potion p) {
        Potion n;
        if (p instanceof PotionOfStrength) {
            return new PotionOfMight();
        }
        if (p instanceof PotionOfMight) {
            return new PotionOfStrength();
        }
        while ((n = (Potion)Generator.random(Generator.Category.POTION)).getClass() == p.getClass()) {
        }
        return n;
    }

    @Override
    public String tileDesc() {
        return "这口井中散发着嬗变之力。将一件物品投入井中，就能将其转化为另一件物品。";
    }
}

