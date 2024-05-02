package me.huanmeng.bacterium.block.entity;

import me.huanmeng.bacterium.Bacterium;
import me.huanmeng.bacterium.block.BlockType;
import me.huanmeng.bacterium.util.Entry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * 2024/5/2<br>
 * Bacterium<br>
 *
 * @author huanmeng_qwq
 */
public abstract class AbstractBlockEntityBacteria extends BlockEntity {
    protected boolean alive = true;
    protected Entry entry;
    protected BlockState checkType;

    public AbstractBlockEntityBacteria(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public boolean checkInvalidPos(BlockPos pos) {
        return pos.getY() >= world.getHeight();
    }

    public static boolean checkInvalidState(BlockState state) {
        return state.isOf(Blocks.BEDROCK) || state.isOf(BlockType.BACTERIA.block) || state.isOf(BlockType.REPLACER.block) || state.isOf(Blocks.BRICKS) || state.isOf(BlockType.JAMMER.block);
    }

    public final void contagion() {
        for (Direction value : Direction.values()) {
            boolean fail = !contagion(pos.offset(value));
            if (fail && !alive) {
                return;
            }
        }
        postContagion();
    }

    public abstract void postContagion();

    public void removeThis() {
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
        alive = false;
        ++Bacterium.removeCount;
    }

    public boolean contagion(BlockPos pos) {
        if (checkInvalidPos(pos)) {
            return false;
        }

        if (world.isAir(pos)) {
            return false;
        }
        BlockState blockState = world.getBlockState(pos);
        if (blockState.isOf(BlockType.JAMMER.block)) {
            alive = false;
            return false;
        }
        if (!checkType(blockState)) {
            return false;
        }

        world.setBlockState(pos, getCachedState());
        AbstractBlockEntityBacteria blockEntity = (AbstractBlockEntityBacteria) world.getBlockEntity(pos);
        blockEntity.entry = entry;
        blockEntity.checkType = checkType;
        return true;
    }

    public boolean checkType(BlockState state) {
        if (checkType == null) {
            return false;
        }
        return state.isOf(checkType.getBlock());
    }

    public abstract void tick();

}
