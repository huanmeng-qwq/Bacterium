package me.huanmeng.bacterium.block.entity;

import me.huanmeng.bacterium.BacteriumCache;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import java.util.concurrent.ThreadLocalRandom;
import org.jetbrains.annotations.NotNull;

public class BacteriaTicker implements BlockEntityTicker<BlockEntityBacteria> {
    public static final BacteriaTicker INSTANCE = new BacteriaTicker();

    @Override
    public void tick(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockEntityBacteria blockEntity) {
        if (level.isClientSide()) {
            return;
        }
        blockEntity.setChanged();
        if (BacteriumCache.jammedAll || BacteriumCache.isUsed(blockEntity.id)) {
            blockEntity.remove();
            BacteriumCache.jammedCount++;
            return;
        }
        if (blockEntity.infected.isEmpty()) {
            // main
            if (!level.hasNeighborSignal(pos)) {
                return;
            }
            blockEntity.findInfectBlcoks();
            blockEntity.expandAndRemove();
            return;
        }
        if (ThreadLocalRandom.current().nextInt(50) + 1 < 50) {
            return;
        }
        blockEntity.expandAndRemove();
    }
}
