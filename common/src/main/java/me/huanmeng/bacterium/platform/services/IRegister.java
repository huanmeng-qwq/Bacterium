package me.huanmeng.bacterium.platform.services;

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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IRegister {
    BlockBehaviour.Properties createProperties(float destroyTime, float explosionResistance);

    Supplier<Block> registerBlock(ResourceLocation location, Function<BlockBehaviour.Properties, Block> blockFunction, BlockBehaviour.Properties properties);

    <T extends BlockEntity> Supplier<BlockEntityType<?>> registerBlockEntityType(ResourceLocation location, BiFunction<BlockPos, BlockState, T> blockEntityBiFunction, Supplier<Block> blockSupplier);

    Supplier<Item> registerItem(ResourceLocation location, Function<Item.Properties, Item> itemFunction, Item.Properties properties);

    Supplier<Entity> registerEntity(ModEntityType entity);
}
