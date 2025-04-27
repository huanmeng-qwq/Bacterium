package me.huanmeng.bacterium;

import me.huanmeng.bacterium.platform.Services;

public class CommonClass {
    public static void init() {
        if (!Services.PLATFORM.isModLoaded("bacterium")) {
            Constants.LOG.error("bacterium is not loaded");
        }
    }
}
