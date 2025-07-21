package me.huanmeng.bacterium.platform;

import me.huanmeng.bacterium.Constants;
import me.huanmeng.bacterium.platform.services.IRegister;
import me.huanmeng.bacterium.type.ModEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeRegister implements IRegister {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_TYPES = DeferredRegister.create(
            ForgeRegistries.BLOCK_ENTITY_TYPES,
            Constants.MOD_ID
    );

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_TYPES.register(bus);
    }


    @Override
    public BlockBehaviour.Properties createProperties(final float destroyTime, final float explosionResistance) {
        return BlockBehaviour.Properties.of().strength(destroyTime, explosionResistance);
    }

    @Override
    public Supplier<Block> registerBlock(final ResourceLocation location, final Function<BlockBehaviour.Properties, Block> blockFunction, final BlockBehaviour.Properties properties) {
        return BLOCKS.register(
                location.getPath(),
                () -> blockFunction.apply(properties));
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<?>> registerBlockEntityType(final ResourceLocation location, final BiFunction<BlockPos, BlockState, T> blockEntityBiFunction, final Supplier<Block> blockSupplier) {
        return BLOCK_TYPES.register(
                location.getPath(),
                () -> BlockEntityType.Builder.of(blockEntityBiFunction::apply, blockSupplier.get()).build(null)
        );
    }

    @Override
    public Supplier<Item> registerItem(final ResourceLocation location, final Function<Item.Properties, Item> itemFunction, final Item.Properties properties) {
        return ITEMS.register(
                location.getPath(),
                () -> itemFunction.apply(properties));
    }

    @Override
    public Supplier<Entity> registerEntity(final ModEntityType entity) {
        return null;
    }
}
