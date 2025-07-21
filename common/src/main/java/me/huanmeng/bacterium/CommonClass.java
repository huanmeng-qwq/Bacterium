package me.huanmeng.bacterium;

import me.huanmeng.bacterium.block.BlockBacteria;
import me.huanmeng.bacterium.block.BlockJammer;
import me.huanmeng.bacterium.block.ModBlocks;
import me.huanmeng.bacterium.block.entity.BlockEntityBacteria;
import me.huanmeng.bacterium.platform.Services;
import net.minecraft.world.level.material.MapColor;

public class CommonClass {
    public static void init() {
        registerItems();
        registerBlocks();
        registerEntities();
    }

    private static void registerItems() {

    }

    private static void registerBlocks() {
        ModBlocks.BACTERIA = Services.REGISTER.registerBlock(ModBlocks.Namespaces.BACTERIA,
                BlockBacteria::new,
                Services.REGISTER.createProperties(1, 6).mapColor(MapColor.COLOR_PURPLE));
        ModBlocks.JAMMER = Services.REGISTER.registerBlock(ModBlocks.Namespaces.JAMMER,
                BlockJammer::new,
                Services.REGISTER.createProperties(1, 6).mapColor(MapColor.COLOR_GREEN));
        ModBlocks.BLOCK_ENTITY_BACTERIA = Services.REGISTER.registerBlockEntityType(ModBlocks.Namespaces.BACTERIA, BlockEntityBacteria::new, ModBlocks.BACTERIA);
    }

    private static void registerEntities() {

    }
}
