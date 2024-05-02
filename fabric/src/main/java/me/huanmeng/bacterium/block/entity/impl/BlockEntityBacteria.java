package me.huanmeng.bacterium.block.entity.impl;

import me.huanmeng.bacterium.Bacterium;
import me.huanmeng.bacterium.block.entity.AbstractBlockEntityBacteria;
import me.huanmeng.bacterium.block.entity.BlockEntityTypeEnum;
import me.huanmeng.bacterium.util.Entry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

/**
 * 2024/5/2<br>
 * Bacterium<br>
 *
 * @author huanmeng_qwq
 */
public class BlockEntityBacteria extends AbstractBlockEntityBacteria {
    private int tick;

    public BlockEntityBacteria(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public BlockEntityBacteria(BlockPos pos, BlockState state) {
        super(BlockEntityTypeEnum.BACTERIA.entityType, pos, state);
    }

    @Override
    public void tick() {
        if (world.isClient) {
            return;
        }
        markDirty();

        if (Bacterium.removeAll) {
            alive = false;
        }

        if (!alive) {
            onClean();
            return;
        }
        if (entry == null) {
            if (world.isReceivingRedstonePower(pos) && !world.isAir(pos.down())) {
                BlockPos upped = pos.up();
                BlockState upBlockState = world.getBlockState(upped);
                if (checkInvalidState(upBlockState)) {
                    return;
                }
                trySetEntry(upBlockState);
                world.setBlockState(upped, Blocks.AIR.getDefaultState());
            }
            return;
        }
        if (tick >= Bacterium.MAX_TICK) {
            contagion();
            return;
        }
        ++tick;
    }

    public void onClean() {
        removeThis();
    }

    @Override
    public void postContagion() {
        removeThis();
    }

    @Override
    public boolean contagion(BlockPos pos) {
        if (super.contagion(pos)) {
            BlockEntityBacteria blockEntity = (BlockEntityBacteria) world.getBlockEntity(pos);
            blockEntity.setRandomTick();
            return true;
        }
        return false;
    }

    private void setRandomTick() {
        tick = world.random.nextInt(Bacterium.MAX_TICK) + 1;
    }

    public void trySetEntry(BlockState blockState) {
        entry = new Entry(blockState);
        setRandomTick();
        checkType = world.getBlockState(pos.down());
    }

}
