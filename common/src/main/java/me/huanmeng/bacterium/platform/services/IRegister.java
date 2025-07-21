package me.huanmeng.bacterium.platform.services;

import me.huanmeng.bacterium.type.ModBlockType;
import me.huanmeng.bacterium.type.ModEntityType;
import me.huanmeng.bacterium.type.ModItemType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.function.Supplier;

public interface IRegister {
    Supplier<Block> registerBlock(ModBlockType block);

    Supplier<BlockEntityType<?>> registerBlockEntityType(ModBlockType block);

    Supplier<Item> registerItem(ModItemType item);

    Supplier<Entity> registerEntity(ModEntityType entity);
}
