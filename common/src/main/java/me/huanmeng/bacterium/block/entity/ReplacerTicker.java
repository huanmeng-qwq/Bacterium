package me.huanmeng.bacterium.block.entity;

import me.huanmeng.bacterium.BacteriumCache;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import java.util.concurrent.ThreadLocalRandom;
import org.jetbrains.annotations.NotNull;

public class ReplacerTicker implements BlockEntityTicker<BlockEntityReplacer> {
    public static final ReplacerTicker INSTANCE = new ReplacerTicker();

    @Override
    public void tick(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockEntityReplacer blockEntity) {
        if (level.isClientSide()) {
            return;
        }
        blockEntity.setChanged();
        if (BacteriumCache.jammedAll || BacteriumCache.isUsed(blockEntity.id)) {
            blockEntity.remove();
            BacteriumCache.jammedCount++;
            return;
        }
        if (blockEntity.bacteria == null || blockEntity.sample == null) {
            // main
            if (!level.hasNeighborSignal(pos)) {
                return;
            }
            blockEntity.findInfectBlcoks();
            return;
        }
        if (ThreadLocalRandom.current().nextInt(50) + 1 < 50) {
            return;
        }
        blockEntity.expandAndRemove();
    }
}
