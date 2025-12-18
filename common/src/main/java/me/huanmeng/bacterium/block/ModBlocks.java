package me.huanmeng.bacterium.block;

import me.huanmeng.bacterium.Constants;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {
    public static Supplier<Block> BACTERIA;
    public static Supplier<Block> REPLACER;
    public static Supplier<Block> JAMMER;
    public static Supplier<Block> MUST;
    public static Supplier<BlockEntityType<?>> BLOCK_ENTITY_BACTERIA;
    public static Supplier<BlockEntityType<?>> BLOCK_ENTITY_REPLACER;

    public static List<Supplier<Block>> blocks() {
        return List.of(BACTERIA, REPLACER, JAMMER, MUST);
    }

    public static class Namespaces {
        public static final Identifier BACTERIA = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "bacterium_colony");
        public static final Identifier REPLACER = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "replacer");
        public static final Identifier JAMMER = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "jammer");
        public static final Identifier MUST = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "must");
    }
}
