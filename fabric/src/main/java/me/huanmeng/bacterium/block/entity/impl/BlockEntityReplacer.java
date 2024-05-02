package me.huanmeng.bacterium.block.entity.impl;

import me.huanmeng.bacterium.Bacterium;
import me.huanmeng.bacterium.block.entity.BlockEntityTypeEnum;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

/**
 * 2024/5/2<br>
 * Bacterium<br>
 *
 * @author huanmeng_qwq
 */
public class BlockEntityReplacer extends BlockEntityBacteria {
    public BlockEntityReplacer(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public BlockEntityReplacer(BlockPos pos, BlockState state) {
        super(BlockEntityTypeEnum.REPLACER.entityType, pos, state);
    }

    @Override
    public void onClean() {
        postContagion();
        ++Bacterium.removeCount;
    }

    @Override
    public void postContagion() {
        BlockState state = entry.state();
        world.setBlockState(pos, state);
        if (entry.nbt != null) {
            if (world.getBlockState(pos).hasBlockEntity()) {
                try {
                    world.addBlockEntity(BlockEntity.createFromNbt(pos, state, entry.nbt, world.getRegistryManager()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
