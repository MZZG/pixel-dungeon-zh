package com.watabou.pd.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.watabou.input.NoosaInputProcessor;
import com.watabou.pixeldungeon.PixelDungeon;

public class StandaloneLauncher {
    public static void main(String[] arg) {
        Package dlPackage = StandaloneLauncher.class.getPackage();
        String version = dlPackage.getSpecificationVersion();
        version = version == null ? "1.9.2-zh" : version + " - " + dlPackage.getImplementationVersion();

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        if (SharedLibraryLoader.isMac) {
            config.preferencesDirectory = "Library/Application Support/Pixel Dungeon/";
        } else if (SharedLibraryLoader.isLinux) {
            config.preferencesDirectory = ".watabou/pixel-dungeon/";
        } else if (SharedLibraryLoader.isWindows) {
            config.preferencesDirectory = "Saved Games/";
        }

        LwjglPreferences prefs = new LwjglPreferences("pd-prefs", config.preferencesDirectory);
        boolean isFullscreen = prefs.getBoolean("windowFullscreen", false);
        config.fullscreen = isFullscreen;
        if (!isFullscreen) {
            config.width = prefs.getInteger("winWidth", 1152);
            config.height = prefs.getInteger("winHeight", 648);
        }

        config.addIcon("ic_launcher_128.png", Files.FileType.Internal);
        config.addIcon("ic_launcher_32.png", Files.FileType.Internal);
        config.addIcon("ic_launcher_16.png", Files.FileType.Internal);
        config.title = "像素地牢 (Pixel Dungeon)";

        new LwjglApplication(
            (ApplicationListener) new PixelDungeon(
                new OfflineDesktopSupport(
                    version,
                    config.preferencesDirectory,
                    (NoosaInputProcessor) new DesktopInputProcessor()
                )
            ),
            config
        );
    }
}
