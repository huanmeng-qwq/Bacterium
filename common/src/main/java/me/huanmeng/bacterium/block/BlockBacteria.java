package me.huanmeng.bacterium.block;

import com.mojang.serialization.MapCodec;
import me.huanmeng.bacterium.block.entity.BacteriaTicker;
import me.huanmeng.bacterium.block.entity.BlockEntityBacteria;
import me.huanmeng.bacterium.type.ModBlockType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockBacteria extends BaseEntityBlock implements ModBlock {
    public static final MapCodec<BlockBacteria> CODEC = simpleCodec(BlockBacteria::new);

    public BlockBacteria(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntityBacteria(blockPos, blockState);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ((BlockEntityType<BlockEntityBacteria>) ModBlocks.BLOCK_ENTITY_BACTERIA.get()), BacteriaTicker.INSTANCE);
    }

    @Override
    public ModBlockType getModBlockType() {
        return ModBlockType.BACTERIA;
    }
}
