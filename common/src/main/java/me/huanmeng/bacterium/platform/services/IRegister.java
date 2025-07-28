package me.huanmeng.bacterium.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
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

public interface IRegister {
    BlockBehaviour.Properties createProperties(float destroyTime, float explosionResistance);

    Supplier<Block> registerBlock(ResourceLocation location, Function<BlockBehaviour.Properties, Block> blockFunction, BlockBehaviour.Properties properties);

    <T extends BlockEntity> Supplier<BlockEntityType<?>> registerBlockEntityType(ResourceLocation location, BiFunction<BlockPos, BlockState, T> blockEntityBiFunction, Supplier<Block> blockSupplier);

    Supplier<Item> registerItem(ResourceLocation location, Function<Item.Properties, Item> itemFunction, Item.Properties properties);

    <T extends Entity> Supplier<EntityType<T>> registerEntity(final ResourceLocation location, MobCategory mobCategory, BiFunction<EntityType<T>, Level, T> entityFunction, final float width, final float height, final int trackingRange, final int updateInterval);

    void initCreativeModeTab(ResourceLocation location, Supplier<ItemStack> icon, List<Supplier<Item>> items);
}
