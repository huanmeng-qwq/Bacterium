package me.huanmeng.bacterium.block;

import me.huanmeng.bacterium.Constants;
import me.huanmeng.bacterium.block.entity.BlockEntityBacteria;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.function.Supplier;

public class ModBlocks {
    public static Supplier<Block> BACTERIA;
    public static Supplier<Block> REPLACER;
    public static Supplier<Block> JAMMER;
    public static Supplier<Block> MUST;
    public static Supplier<BlockEntityType<?>> BLOCK_ENTITY_BACTERIA;
    public static Supplier<BlockEntityType<?>> BLOCK_ENTITY_REPLACER;

    public static class Namespaces {
        public static final ResourceLocation BACTERIA = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "bacterium_colony");
        public static final ResourceLocation REPLACER = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "replacer");
        public static final ResourceLocation JAMMER = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "jammer");
        public static final ResourceLocation MUST = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "must");
    }
}
