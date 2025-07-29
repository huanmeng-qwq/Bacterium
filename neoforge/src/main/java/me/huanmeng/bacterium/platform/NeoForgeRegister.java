package me.huanmeng.bacterium.platform;

import me.huanmeng.bacterium.Constants;
import me.huanmeng.bacterium.platform.services.IRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class NeoForgeRegister implements IRegister {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            BuiltInRegistries.BLOCK,
            Constants.MOD_ID
    );
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            BuiltInRegistries.ITEM,
            Constants.MOD_ID
    );
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_TYPES = DeferredRegister.create(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Constants.MOD_ID
    );
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(
            BuiltInRegistries.ENTITY_TYPE,
            Constants.MOD_ID
    );

    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            Constants.MOD_ID
    );


    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_TYPES.register(bus);
        ENTITIES.register(bus);
        CREATIVE_MODE_TAB.register(bus);
    }

    @Override
    public BlockBehaviour.Properties createProperties(final ResourceLocation location, final float destroyTime, final float explosionResistance) {
        return BlockBehaviour.Properties.of().strength(destroyTime, explosionResistance).setId(ResourceKey.create(Registries.BLOCK, location));
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
                () -> new BlockEntityType<>(blockEntityBiFunction::apply, blockSupplier.get())
        );
    }

    @Override
    public Supplier<Item> registerItem(final ResourceLocation location, final Function<Item.Properties, Item> itemFunction, final Item.Properties properties) {
        return ITEMS.register(
                location.getPath(),
                () -> itemFunction.apply(properties));
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> registerEntity(final ResourceLocation location, final MobCategory mobCategory, final BiFunction<EntityType<T>, Level, T> entityFunction, final float width, final float height, final int trackingRange, final int updateInterval) {
        return ENTITIES.register(
                location.getPath(),
                () -> EntityType.Builder.of(entityFunction::apply, mobCategory)
                        .sized(width, height)
                        .clientTrackingRange(trackingRange)
                        .updateInterval(updateInterval)
                        .build(ResourceKey.create(Registries.ENTITY_TYPE, location))
        );
    }

    @Override
    public void initCreativeModeTab(final ResourceLocation location, final Supplier<ItemStack> icon, final List<Supplier<Item>> items) {
        CREATIVE_MODE_TAB.register(location.getPath(), () -> CreativeModeTab.builder()
                .icon(icon)
                .title(Component.translatable("itemGroup.bacterium.bacterium"))
                .displayItems((itemDisplayParameters, output) -> {
                    items.forEach(item -> output.accept(item.get()));
                })
                .build());
    }
}
