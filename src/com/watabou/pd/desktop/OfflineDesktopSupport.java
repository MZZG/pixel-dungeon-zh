package com.watabou.pd.desktop;

import com.watabou.input.NoosaInputProcessor;
import com.watabou.utils.PDPlatformSupport;

public class OfflineDesktopSupport extends PDPlatformSupport {
    public OfflineDesktopSupport(String version, String basePath, NoosaInputProcessor inputProcessor) {
        super(version, basePath, inputProcessor);
    }

    @Override
    public boolean isFullscreenEnabled() {
        return true;
    }
}
