package me.huanmeng.bacterium.block;

import com.mojang.serialization.MapCodec;
import me.huanmeng.bacterium.block.entity.BlockEntityBacteriaTicker;
import me.huanmeng.bacterium.block.entity.impl.BlockEntityReplacer;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * 2024/5/2<br>
 * Bacterium<br>
 *
 * @author huanmeng_qwq
 */
public class BlockReplacer extends BlockWithEntity {
    public static final MapCodec<BlockReplacer> CODEC = createCodec(BlockReplacer::new);

    protected BlockReplacer(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityReplacer(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return new BlockEntityBacteriaTicker<>();
    }
}
