package me.huanmeng.bacterium;

import me.huanmeng.bacterium.block.BlockBacteria;
import me.huanmeng.bacterium.block.BlockJammer;
import me.huanmeng.bacterium.block.BlockMust;
import me.huanmeng.bacterium.block.BlockReplacer;
import me.huanmeng.bacterium.block.ModBlocks;
import me.huanmeng.bacterium.block.entity.BlockEntityBacteria;
import me.huanmeng.bacterium.block.entity.BlockEntityReplacer;
import me.huanmeng.bacterium.item.ItemBunch;
import me.huanmeng.bacterium.item.ModItems;
import me.huanmeng.bacterium.platform.Services;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.MapColor;

public class CommonClass {
    public static void init() {
        registerItems();
        registerBlocks();
        registerEntities();
    }

    private static void registerItems() {
        ModItems.BUNCH = Services.REGISTER.registerItem(ModItems.Namespaces.BUNCH, ItemBunch::new, new Item.Properties());
    }

    private static void registerBlocks() {
        ModBlocks.BACTERIA = Services.REGISTER.registerBlock(ModBlocks.Namespaces.BACTERIA,
                BlockBacteria::new,
                Services.REGISTER.createProperties(1, 6).mapColor(MapColor.COLOR_PURPLE));
        ModBlocks.REPLACER = Services.REGISTER.registerBlock(ModBlocks.Namespaces.REPLACER,
                BlockReplacer::new,
                Services.REGISTER.createProperties(1, 6).mapColor(MapColor.TERRACOTTA_LIGHT_GREEN));
        ModBlocks.JAMMER = Services.REGISTER.registerBlock(ModBlocks.Namespaces.JAMMER,
                BlockJammer::new,
                Services.REGISTER.createProperties(1, 6).mapColor(MapColor.COLOR_GREEN));
        ModBlocks.MUST = Services.REGISTER.registerBlock(ModBlocks.Namespaces.MUST,
                BlockMust::new,
                Services.REGISTER.createProperties(1, 6).mapColor(MapColor.TERRACOTTA_GREEN));
        ModBlocks.BLOCK_ENTITY_BACTERIA = Services.REGISTER.registerBlockEntityType(ModBlocks.Namespaces.BACTERIA, BlockEntityBacteria::new, ModBlocks.BACTERIA);
        ModBlocks.BLOCK_ENTITY_REPLACER = Services.REGISTER.registerBlockEntityType(ModBlocks.Namespaces.REPLACER, BlockEntityReplacer::new, ModBlocks.REPLACER);
    }

    private static void registerEntities() {

    }
}
