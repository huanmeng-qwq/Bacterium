package me.huanmeng.bacterium.entity;

import me.huanmeng.bacterium.block.ModBlocks;
import me.huanmeng.bacterium.block.entity.BlockEntityBacteria;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownSplashPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class PotionEntityBacteria extends ThrownSplashPotion {

    public PotionEntityBacteria(final EntityType<? extends ThrownSplashPotion> p_400190_, final Level p_399722_) {
        super(p_400190_, p_399722_);
    }

    public PotionEntityBacteria(final Level p_399777_, final LivingEntity p_400264_, final ItemStack p_400186_) {
        super(p_399777_, p_400264_, p_400186_);
    }

    public PotionEntityBacteria(final Level p_400144_, final double p_400061_, final double p_400032_, final double p_399549_, final ItemStack p_400218_) {
        super(p_400144_, p_400061_, p_400032_, p_399549_, p_400218_);
    }

    @Override
    protected void onHitBlock(final BlockHitResult result) {
        if (level().isClientSide) return;
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
