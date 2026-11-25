/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import java.util.ArrayList;

public class Journal {
    public static ArrayList<Record> records;
    private static final String JOURNAL = "journal";

    public static void reset() {
        records = new ArrayList();
    }

    public static void storeInBundle(Bundle bundle) {
        bundle.put(JOURNAL, records);
    }

    public static void restoreFromBundle(Bundle bundle) {
        records = new ArrayList();
        for (Bundlable rec : bundle.getCollection(JOURNAL)) {
            records.add((Record)rec);
        }
    }

    public static void add(Feature feature, int pos) {
        int size = records.size();
        for (int i = 0; i < size; ++i) {
            Record rec = records.get(i);
            if (rec.feature != feature || rec.depth != Dungeon.depth) continue;
            return;
        }
        records.add(new Record(feature, Dungeon.depth, pos));
    }

    public static void add(Feature feature) {
        Journal.add(feature, -1);
    }

    public static void remove(Feature feature) {
        int size = records.size();
        for (int i = 0; i < size; ++i) {
            Record rec = records.get(i);
            if (rec.feature != feature || rec.depth != Dungeon.depth) continue;
            records.remove(i);
            return;
        }
    }

    public static class Record
    implements Comparable<Record>,
    Bundlable {
        private static final String FEATURE = "feature";
        private static final String DEPTH = "depth";
        private static final String POS = "pos";
        public Feature feature;
        public int depth;
        public int pos;

        public Record() {
        }

        public Record(Feature feature, int depth, int pos) {
            this.feature = feature;
            this.depth = depth;
            this.pos = pos;
        }

        @Override
        public int compareTo(Record another) {
            return another.depth - this.depth;
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            this.feature = Feature.valueOf(bundle.getString(FEATURE));
            this.depth = bundle.getInt(DEPTH);
            this.pos = bundle.getInt(POS);
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            bundle.put(FEATURE, this.feature.toString());
            bundle.put(DEPTH, this.depth);
            bundle.put(POS, this.pos);
        }
    }

    public static enum Feature {
        WELL_OF_HEALTH("生命之泉"),
        WELL_OF_AWARENESS("觉察之泉"),
        WELL_OF_TRANSMUTATION("嬗变之泉"),
        ALCHEMY("炼金釜"),
        GARDEN("静谧花圃"),
        STATUE("活化石像"),
        SACRIFICIAL_CHAMBER("献祭密室"),
        GHOST("悲伤幽灵"),
        WANDMAKER("老杖匠"),
        TROLL("巨魔铁匠"),
        IMP("野心勃勃的小恶魔");

        public final String desc;

        private Feature(String desc) {
            this.desc = desc;
        }
    }
}

