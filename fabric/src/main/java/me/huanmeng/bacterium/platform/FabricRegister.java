package me.huanmeng.bacterium.platform;

import com.google.common.base.Suppliers;
import me.huanmeng.bacterium.platform.services.IRegister;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
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
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class FabricRegister implements IRegister {

    @Override
    public BlockBehaviour.Properties createProperties(float destroyTime, float explosionResistance) {
        return BlockBehaviour.Properties.of().strength(destroyTime, explosionResistance);
    }

    @Override
    public Supplier<Block> registerBlock(final ResourceLocation location, final Function<BlockBehaviour.Properties, Block> blockFunction, final BlockBehaviour.Properties properties) {
        final Block block = blockFunction.apply(properties);
        Registry.register(BuiltInRegistries.BLOCK, location, block);
        return Suppliers.memoize(() -> block);
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<?>> registerBlockEntityType(final ResourceLocation location, final BiFunction<BlockPos, BlockState, T> blockEntityBiFunction, final Supplier<Block> blockSupplier) {
        BlockEntityType<?> type = BlockEntityType.Builder.of(blockEntityBiFunction::apply, blockSupplier.get()).build(null);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, location, type);
        return Suppliers.memoize(() -> type);
    }

    @Override
    public Supplier<Item> registerItem(final ResourceLocation location, final Function<Item.Properties, Item> itemFunction, final Item.Properties properties) {
        final Item item = itemFunction.apply(properties);
        Registry.register(BuiltInRegistries.ITEM, location, item);
        return Suppliers.memoize(() -> item);
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> registerEntity(final ResourceLocation location, final MobCategory mobCategory, final BiFunction<EntityType<T>, Level, T> entityFunction, final float width, final float height, final int trackingRange, final int updateInterval) {
        final EntityType<T> type = EntityType.Builder.of(entityFunction::apply, mobCategory)
                .sized(width, height)
                .clientTrackingRange(trackingRange)
                .updateInterval(updateInterval)
                .build(location.getPath());
        Registry.register(BuiltInRegistries.ENTITY_TYPE, location, type);
        return Suppliers.memoize(() -> type);
    }

    @Override
    public void initCreativeModeTab(final ResourceLocation location, final Supplier<ItemStack> icon, final List<Supplier<Item>> items) {
        final CreativeModeTab creativeModeTab = FabricItemGroup.builder()
                .icon(icon::get)
                .title(Component.translatable("itemGroup.bacterium.bacterium"))
                .displayItems((itemDisplayParameters, output) -> {
                    items.forEach(item -> output.accept(item.get()));
                })
                .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, location, creativeModeTab);
    }
}
