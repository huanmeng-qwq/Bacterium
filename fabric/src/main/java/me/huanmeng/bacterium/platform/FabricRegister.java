package me.huanmeng.bacterium.platform;

import com.google.common.base.Suppliers;
import me.huanmeng.bacterium.block.BlockBacteria;
import me.huanmeng.bacterium.block.ModBlocks;
import me.huanmeng.bacterium.block.entity.BlockEntityBacteria;
import me.huanmeng.bacterium.platform.services.IRegister;
import me.huanmeng.bacterium.type.ModBlockType;
import me.huanmeng.bacterium.type.ModEntityType;
import me.huanmeng.bacterium.type.ModItemType;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import java.util.function.Supplier;

public class FabricRegister implements IRegister {

    public static final BlockBehaviour.Properties settings = BlockBehaviour.Properties.of().mapColor(
            MapColor.COLOR_PURPLE).strength(1, 6);

    @Override
    public Supplier<Block> registerBlock(final ModBlockType block) {
        Block obj = switch (block) {
            case BACTERIA -> new BlockBacteria(FabricBlockSettings.copyOf(settings).mapColor(MapColor.COLOR_PURPLE));
            default -> throw new IllegalArgumentException("Unsupported block " + block);
        };
        Registry.register(BuiltInRegistries.BLOCK, ModBlocks.Namespaces.BACTERIA, obj);
        return Suppliers.memoize(() -> obj);
    }

    @Override
    public Supplier<BlockEntityType<?>> registerBlockEntityType(final ModBlockType block) {
        BlockEntityType<?> type = switch (block) {
            case BACTERIA -> BlockEntityType.Builder.of(BlockEntityBacteria::new, ModBlocks.BACTERIA.get()).build(null);
            default -> throw new IllegalArgumentException("Unsupported block " + block);
        };
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ModBlocks.Namespaces.BACTERIA, type);
        return Suppliers.memoize(() -> type);
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
