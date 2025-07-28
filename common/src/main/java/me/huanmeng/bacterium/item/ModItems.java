package me.huanmeng.bacterium.item;

import me.huanmeng.bacterium.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import java.util.function.Supplier;

public class ModItems {
    public static Supplier<Item> BUNCH;
    public static Supplier<Item> JAMMER;
    public static Supplier<Item> POTION;

    // Block
    public static Supplier<Item> BACTERIA;
    public static Supplier<Item> REPLACER;
    public static Supplier<Item> BLOCK_JAMMER;
    public static Supplier<Item> MUST;

    public static class Namespaces {
        public static final ResourceLocation BUNCH = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "bunch");
        public static final ResourceLocation JAMMER = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "jammeritem");
        public static final ResourceLocation POTION = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "potion");
    }
}
