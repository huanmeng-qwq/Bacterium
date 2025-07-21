package me.huanmeng.bacterium.platform;

import me.huanmeng.bacterium.Constants;
import me.huanmeng.bacterium.block.ModBlocks;
import me.huanmeng.bacterium.platform.services.IRegister;
import me.huanmeng.bacterium.type.ModEntityType;
import me.huanmeng.bacterium.type.ModItemType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class NeoForgeRegister implements IRegister {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            BuiltInRegistries.BLOCK,
            Constants.MOD_ID
    );
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_TYPES = DeferredRegister.create(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Constants.MOD_ID
    );

    @Override
    public BlockBehaviour.Properties createProperties(final float destroyTime, final float explosionResistance) {
        return BlockBehaviour.Properties.of().strength(destroyTime, explosionResistance);
    }

    @Override
    public Supplier<Block> registerBlock(final ResourceLocation location, final Function<BlockBehaviour.Properties, Block> blockFunction, final BlockBehaviour.Properties properties) {
        Supplier<Block> supplier = BLOCKS.register(
                location.getPath(),
                () -> blockFunction.apply(properties));
        return supplier;
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<?>> registerBlockEntityType(final ResourceLocation location, final BiFunction<BlockPos, BlockState, T> blockEntityBiFunction, final Supplier<Block> blockSupplier) {
        Supplier<BlockEntityType<?>> supplier = BLOCK_TYPES.register(
                location.getPath(),
                () -> BlockEntityType.Builder.of(blockEntityBiFunction::apply, blockSupplier.get()).build(null)
        );
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
