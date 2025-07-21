package me.huanmeng.bacterium.platform;

import me.huanmeng.bacterium.Constants;
import me.huanmeng.bacterium.block.BlockBacteria;
import me.huanmeng.bacterium.block.ModBlocks;
import me.huanmeng.bacterium.block.entity.BlockEntityBacteria;
import me.huanmeng.bacterium.platform.services.IRegister;
import me.huanmeng.bacterium.type.ModBlockType;
import me.huanmeng.bacterium.type.ModEntityType;
import me.huanmeng.bacterium.type.ModItemType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class NeoForgeRegister implements IRegister {
    private static BlockBehaviour.Properties createProperties() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(1, 6);
    }

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            BuiltInRegistries.BLOCK,
            Constants.MOD_ID
    );
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_TYPES = DeferredRegister.create(
            // The registry we want to use.
            // Minecraft's registries can be found in BuiltInRegistries, NeoForge's registries can be found in NeoForgeRegistries.
            // Mods may also add their own registries, refer to the individual mod's documentation or source code for where to find them.
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            // Our mod id.
            Constants.MOD_ID
    );

    @Override
    public Supplier<Block> registerBlock(final ModBlockType block) {
        final BlockBehaviour.Properties properties = createProperties();
        Supplier<Block> supplier = switch (block) {
            case BACTERIA -> BLOCKS.register(
                    ModBlocks.Namespaces.BACTERIA.getPath(),
                    () -> new BlockBacteria(properties.mapColor(MapColor.COLOR_PURPLE))
            );
            default -> throw new IllegalArgumentException("Unsupported block " + block);
        };
        return supplier;
    }

    @Override
    public Supplier<BlockEntityType<?>> registerBlockEntityType(final ModBlockType block) {
        Supplier<BlockEntityType<?>> supplier = switch (block) {
            case BACTERIA -> BLOCK_TYPES.register(
                    ModBlocks.Namespaces.BACTERIA.getPath(),
                    () -> BlockEntityType.Builder.of(BlockEntityBacteria::new, ModBlocks.BACTERIA.get()).build(null)
            );
            default -> throw new IllegalArgumentException("Unsupported block " + block);
        };
        return supplier;
    }

    @Override
    public Supplier<Item> registerItem(final ModItemType item) {
        return null;
    }

    @Override
    public Supplier<Entity> registerEntity(final ModEntityType entity) {
        return null;
    }
}
