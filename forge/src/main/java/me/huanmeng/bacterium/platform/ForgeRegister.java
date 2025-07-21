package me.huanmeng.bacterium.platform;

import me.huanmeng.bacterium.block.ModBlocks;
import me.huanmeng.bacterium.platform.services.IRegister;
import me.huanmeng.bacterium.type.ModEntityType;
import me.huanmeng.bacterium.type.ModItemType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeRegister implements IRegister {
    @Override
    public BlockBehaviour.Properties createProperties(final float destroyTime, final float explosionResistance) {
        return BlockBehaviour.Properties.of().strength(destroyTime, explosionResistance);
    }

    @Override
    public Supplier<Block> registerBlock(final ResourceLocation location, final Function<BlockBehaviour.Properties, Block> blockFunction, final BlockBehaviour.Properties properties) {
        final Block block = blockFunction.apply(properties);
        Registry.register(BuiltInRegistries.BLOCK, location, block);
        return () -> block;
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<?>> registerBlockEntityType(final ResourceLocation location, final BiFunction<BlockPos, BlockState, T> blockEntityBiFunction, final Supplier<Block> blockSupplier) {
        BlockEntityType<?> type = BlockEntityType.Builder.of(blockEntityBiFunction::apply, blockSupplier.get()).build(null);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, location, type);
        return () -> type;
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
