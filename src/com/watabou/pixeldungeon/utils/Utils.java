/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pixeldungeon.utils;

import java.util.Locale;

public class Utils {
    public static String VOWELS = "aoeiu";

    public static String capitalize(String str) {
        if (str == null || str.length() == 0) return "";
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static String format(String format, Object ... args) {
        return String.format(Locale.ENGLISH, format, args);
    }

    public static String indefinite(String noun) {
        return noun == null ? "" : noun;
    }
}

