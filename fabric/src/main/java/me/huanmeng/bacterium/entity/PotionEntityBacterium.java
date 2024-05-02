package me.huanmeng.bacterium.entity;

import me.huanmeng.bacterium.block.BlockType;
import me.huanmeng.bacterium.block.entity.impl.BlockEntityBacteria;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 2024/5/2<br>
 * Bacterium<br>
 *
 * @author huanmeng_qwq
 */
public class PotionEntityBacterium extends PotionEntity {
    public PotionEntityBacterium(EntityType<? extends PotionEntity> entityType, World world) {
        super(entityType, world);
    }

    public PotionEntityBacterium(World world, LivingEntity owner) {
        super(world, owner);
    }

    public PotionEntityBacterium(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        final BlockPos blockPos = blockHitResult.getBlockPos();
        final BlockState blockState = getWorld().getBlockState(blockPos);
        if (BlockEntityBacteria.checkInvalidState(blockState)) {
            return;
        }
        getWorld().setBlockState(blockPos, BlockType.BACTERIA.block.getDefaultState());
        final BlockEntityBacteria blockEntity = (BlockEntityBacteria) getWorld().getBlockEntity(blockPos);
        blockEntity.trySetEntry(blockState);
    }
}
