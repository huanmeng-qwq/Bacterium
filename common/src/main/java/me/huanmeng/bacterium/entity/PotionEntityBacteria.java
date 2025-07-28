package me.huanmeng.bacterium.entity;

import me.huanmeng.bacterium.block.ModBlocks;
import me.huanmeng.bacterium.block.entity.BlockEntityBacteria;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class PotionEntityBacteria extends ThrownPotion {
    public PotionEntityBacteria(final EntityType<? extends ThrownPotion> entityType, final Level level) {
        super(entityType, level);
    }

    public PotionEntityBacteria(final Level level, final LivingEntity shooter) {
        super(level, shooter);
    }

    public PotionEntityBacteria(final Level level, final double x, final double y, final double z) {
        super(level, x, y, z);
    }

    @Override
    protected void onHitBlock(final BlockHitResult result) {
        final BlockPos blockPos = result.getBlockPos();
        final BlockState blockState = level().getBlockState(blockPos);
        if (BlockEntityBacteria.isInvalidBlock(blockState)) {
            return;
        }
        final BlockEntity oldBlockEntity = level().getBlockEntity(blockPos);
        level().setBlockAndUpdate(blockPos, ModBlocks.BACTERIA.get().defaultBlockState());
        final BlockEntityBacteria blockEntity = (BlockEntityBacteria) level().getBlockEntity(blockPos);
        blockEntity.addInfectBlock(blockState, () -> oldBlockEntity);
    }
}
