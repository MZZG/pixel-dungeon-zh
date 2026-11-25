/*
 * Decompiled with CFR 0.152.
 */
package com.watabou.pd.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.codedisaster.steamworks.SteamAPI;
import com.watabou.input.NoosaInputProcessor;
import com.watabou.pd.desktop.DesktopInputProcessor;
import com.watabou.pd.desktop.DesktopSupport;
import com.watabou.pixeldungeon.PixelDungeon;
import javax.swing.JOptionPane;

public class DesktopLauncher {
    public static void main(String[] arg) {
        boolean isFullscreen;
        if (!SteamAPI.init()) {
            JOptionPane.showConfirmDialog(null, "启动本游戏前请先运行 Steam", "Steam 未运行", -1, 0);
            return;
        }
        Package dlPackage = DesktopLauncher.class.getPackage();
        String version = dlPackage.getSpecificationVersion();
        version = version == null ? "???" : version + " - " + dlPackage.getImplementationVersion();
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        if (SharedLibraryLoader.isMac) {
            config.preferencesDirectory = "Library/Application Support/Pixel Dungeon/";
        } else if (SharedLibraryLoader.isLinux) {
            config.preferencesDirectory = ".watabou/pixel-dungeon/";
        } else if (SharedLibraryLoader.isWindows) {
            config.preferencesDirectory = "Saved Games/";
        }
        LwjglPreferences prefs = new LwjglPreferences("pd-prefs", config.preferencesDirectory);
        config.fullscreen = isFullscreen = prefs.getBoolean("windowFullscreen", false);
        if (!isFullscreen) {
            config.width = prefs.getInteger("winWidth", 1152);
            config.height = prefs.getInteger("winHeight", 648);
        }
        config.addIcon("ic_launcher_128.png", Files.FileType.Internal);
        config.addIcon("ic_launcher_32.png", Files.FileType.Internal);
        config.addIcon("ic_launcher_16.png", Files.FileType.Internal);
        config.title = "像素地牢 (Pixel Dungeon)";
        Runtime.getRuntime().addShutdownHook(new Thread(){

            @Override
            public void run() {
                SteamAPI.shutdown();
            }
        });
        new LwjglApplication((ApplicationListener)new PixelDungeon(new DesktopSupport(version, config.preferencesDirectory, (NoosaInputProcessor)new DesktopInputProcessor())), config);
    }
}

