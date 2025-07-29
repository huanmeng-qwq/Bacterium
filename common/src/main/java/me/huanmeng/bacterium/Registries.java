package me.huanmeng.bacterium;

import me.huanmeng.bacterium.block.BlockBacteria;
import me.huanmeng.bacterium.block.BlockJammer;
import me.huanmeng.bacterium.block.BlockMust;
import me.huanmeng.bacterium.block.BlockReplacer;
import me.huanmeng.bacterium.block.ModBlocks;
import me.huanmeng.bacterium.block.entity.BlockEntityBacteria;
import me.huanmeng.bacterium.block.entity.BlockEntityReplacer;
import me.huanmeng.bacterium.entity.ModEntities;
import me.huanmeng.bacterium.entity.PotionEntityBacteria;
import me.huanmeng.bacterium.item.ItemBunch;
import me.huanmeng.bacterium.item.ItemJammer;
import me.huanmeng.bacterium.item.ItemSplashBacteria;
import me.huanmeng.bacterium.item.ModItems;
import me.huanmeng.bacterium.platform.Services;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.MapColor;
import java.util.Arrays;

public class Registries {
    public static void init() {
        registerItems();
        registerBlocks();
        registerEntities();
        Services.REGISTER.initCreativeModeTab(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, Constants.MOD_ID), () -> ModItems.REPLACER.get().getDefaultInstance(), Arrays.asList(
                ModItems.BUNCH,
                ModItems.JAMMER,
                ModItems.POTION,
                ModItems.BACTERIA,
                ModItems.REPLACER,
                ModItems.BLOCK_JAMMER,
                ModItems.MUST
        ));
    }

    private static ResourceKey<Item> createItemRegistryKey(ResourceLocation location) {
        return ResourceKey.create(net.minecraft.core.registries.Registries.ITEM, location);
    }

    private static void registerItems() {
        ModItems.BUNCH = Services.REGISTER.registerItem(ModItems.Namespaces.BUNCH, ItemBunch::new, new Item.Properties().setId(createItemRegistryKey(ModItems.Namespaces.BUNCH)));
        ModItems.JAMMER = Services.REGISTER.registerItem(ModItems.Namespaces.JAMMER, ItemJammer::new, new Item.Properties().durability(32).setId(createItemRegistryKey(ModItems.Namespaces.JAMMER)));
        ModItems.POTION = Services.REGISTER.registerItem(ModItems.Namespaces.POTION, ItemSplashBacteria::new, new Item.Properties().setId(createItemRegistryKey(ModItems.Namespaces.POTION)));
    }

    private static void registerBlocks() {
        ModBlocks.BACTERIA = Services.REGISTER.registerBlock(ModBlocks.Namespaces.BACTERIA,
                BlockBacteria::new,
                Services.REGISTER.createProperties(ModBlocks.Namespaces.BACTERIA, 1, 6).mapColor(MapColor.COLOR_PURPLE));
        ModBlocks.REPLACER = Services.REGISTER.registerBlock(ModBlocks.Namespaces.REPLACER,
                BlockReplacer::new,
                Services.REGISTER.createProperties(ModBlocks.Namespaces.REPLACER, 1, 6).mapColor(MapColor.TERRACOTTA_LIGHT_GREEN));
        ModBlocks.JAMMER = Services.REGISTER.registerBlock(ModBlocks.Namespaces.JAMMER,
                BlockJammer::new,
                Services.REGISTER.createProperties(ModBlocks.Namespaces.JAMMER, 1, 6).mapColor(MapColor.COLOR_GREEN));
        ModBlocks.MUST = Services.REGISTER.registerBlock(ModBlocks.Namespaces.MUST,
                BlockMust::new,
                Services.REGISTER.createProperties(ModBlocks.Namespaces.MUST, 1, 6).mapColor(MapColor.TERRACOTTA_GREEN));
        ModBlocks.BLOCK_ENTITY_BACTERIA = Services.REGISTER.registerBlockEntityType(ModBlocks.Namespaces.BACTERIA, BlockEntityBacteria::new, ModBlocks.BACTERIA);
        ModBlocks.BLOCK_ENTITY_REPLACER = Services.REGISTER.registerBlockEntityType(ModBlocks.Namespaces.REPLACER, BlockEntityReplacer::new, ModBlocks.REPLACER);

        // BlockItem
        ModItems.BACTERIA = Services.REGISTER.registerItem(
                ModBlocks.Namespaces.BACTERIA,
                settings -> new BlockItem(ModBlocks.BACTERIA.get(), settings),
                new Item.Properties().setId(createItemRegistryKey(ModBlocks.Namespaces.BACTERIA)).useBlockDescriptionPrefix()
        );
        ModItems.REPLACER = Services.REGISTER.registerItem(
                ModBlocks.Namespaces.REPLACER,
                settings -> new BlockItem(ModBlocks.REPLACER.get(), settings),
                new Item.Properties().setId(createItemRegistryKey(ModBlocks.Namespaces.REPLACER)).useBlockDescriptionPrefix()
        );
        ModItems.BLOCK_JAMMER = Services.REGISTER.registerItem(
                ModBlocks.Namespaces.JAMMER,
                settings -> new BlockItem(ModBlocks.JAMMER.get(), settings),
                new Item.Properties().setId(createItemRegistryKey(ModBlocks.Namespaces.JAMMER)).useBlockDescriptionPrefix()
        );
        ModItems.MUST = Services.REGISTER.registerItem(
                ModBlocks.Namespaces.MUST,
                settings -> new BlockItem(ModBlocks.MUST.get(), settings),
                new Item.Properties().setId(createItemRegistryKey(ModBlocks.Namespaces.MUST)).useBlockDescriptionPrefix()
        );
    }

    private static void registerEntities() {
        ModEntities.POTION = Services.REGISTER.registerEntity(
                ModEntities.Namespaces.POTION,
                MobCategory.MISC,
                PotionEntityBacteria::new,
                0.25F, 0.25F,
                4, 10
        );
    }
}
