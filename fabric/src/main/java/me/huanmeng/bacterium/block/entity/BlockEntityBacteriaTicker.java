package me.huanmeng.bacterium.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 2024/5/2<br>
 * Bacterium<br>
 *
 * @author huanmeng_qwq
 */
public class BlockEntityBacteriaTicker<T extends BlockEntity> implements BlockEntityTicker<T> {

    @Override
    public void tick(World world, BlockPos pos, BlockState state, T blockEntity) {
        if (blockEntity instanceof AbstractBlockEntityBacteria e) {
            e.tick();
        }
    }
}
