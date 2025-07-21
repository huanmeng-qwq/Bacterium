package me.huanmeng.bacterium;

import me.huanmeng.bacterium.block.ModBlocks;
import me.huanmeng.bacterium.platform.Services;
import me.huanmeng.bacterium.type.ModBlockType;

public class CommonClass {
    public static void init() {
//        if (!Services.PLATFORM.isModLoaded("assets/bacterium")) {
//            Constants.LOG.error("bacterium is not loaded");
//            return;
//        }
        registerItems();
        registerBlocks();
        registerEntities();
    }

    private static void registerItems() {

    }

    private static void registerBlocks() {
        ModBlocks.BACTERIA = Services.REGISTER.registerBlock(ModBlockType.BACTERIA);
        ModBlocks.BLOCK_ENTITY_BACTERIA = Services.REGISTER.registerBlockEntityType(ModBlockType.BACTERIA);
    }

    private static void registerEntities() {

    }
}
