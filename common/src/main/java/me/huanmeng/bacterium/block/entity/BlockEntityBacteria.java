package me.huanmeng.bacterium.block.entity;

import me.huanmeng.bacterium.BacteriumCache;
import me.huanmeng.bacterium.Constants;
import me.huanmeng.bacterium.block.ModBlock;
import me.huanmeng.bacterium.block.ModBlocks;
import me.huanmeng.bacterium.type.ModBlockType;
import me.huanmeng.bacterium.util.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BlockEntityBacteria extends BlockEntity {
    protected int id;
    protected final List<Entry> infected = new ArrayList<>();

    public BlockEntityBacteria(BlockPos pos, BlockState blockState) {
        this(ModBlocks.BLOCK_ENTITY_BACTERIA.get(), pos, blockState);
    }

    public BlockEntityBacteria(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.id = BacteriumCache.freeId();
    }

    public void findInfectBlcoks() {
        BlockState state;
        BlockPos pos = worldPosition.above();
        while (!(state = level.getBlockState(pos)).isAir()) {
            final BlockPos finalPos = pos;
            addInfectBlock(state, () -> level.getBlockEntity(finalPos));
            pos = pos.above();
        }
    }

    public void addInfectBlock(BlockState state, Supplier<BlockEntity> blockEntity) {
        if (isInvalidBlock(state)) {
            return;
        }
        infected.add(new Entry(state, level, blockEntity.get()));
    }

    public static boolean isInvalidBlock(BlockState state) {
        Block block = state.getBlock();
        return state.isAir() ||
                block == Blocks.BEDROCK ||
                block == ModBlocks.BACTERIA.get() ||
                block == ModBlocks.REPLACER.get() ||
                block == ModBlocks.JAMMER.get() ||
                block == Blocks.BRICKS
                ;
    }

    public boolean isInfectBlock(BlockState state) {
        if (BacteriumCache.isUsed(id)) return false;
        if (infected.isEmpty()) return false;
        if (state.getBlock() instanceof ModBlock block && block.getModBlockType() == ModBlockType.JAMMER) {
            // remove all of this id
            BacteriumCache.markUsed(id);
            return false;
        }
        return infected.stream().anyMatch(entry -> matchState(entry, state));
    }

    protected static boolean matchState(Entry a, BlockState state) {
        BlockState excepted = a.state;
        if (excepted == state) {
            return true;
        } else if (state == Blocks.DIRT.defaultBlockState()) {
            return excepted == Blocks.GRASS_BLOCK.defaultBlockState();
        } else if (state == Blocks.GRASS_BLOCK.defaultBlockState()) {
            return excepted == Blocks.DIRT.defaultBlockState();
        } else if (state == Blocks.WATER.defaultBlockState()) {
            return excepted.getFluidState().getType() == Fluids.FLOWING_WATER;
        } else if (state.getFluidState().getType() == Fluids.FLOWING_WATER) {
            return excepted == Blocks.WATER.defaultBlockState();
        } else if (state == Blocks.LAVA.defaultBlockState()) {
            return excepted.getFluidState().getType() == Fluids.FLOWING_LAVA;
        } else if (state.getFluidState().getType() == Fluids.FLOWING_LAVA) {
            return excepted == Blocks.LAVA.defaultBlockState();
        } else if (state.is(BlockTags.LEAVES)) {
            return excepted.getBlock() == state.getBlock();
        }

        return false;
    }

    public void expand(BlockPos pos) {
        if (isOutside(pos)) {
            return;
        }
        BlockState blockState = level.getBlockState(pos);
        if (blockState.isAir()) {
            return;
        }
        if (!isInfectBlock(blockState)) {
            return;
        }
        level.setBlockAndUpdate(pos, getBlockState());
        BlockEntityBacteria blockEntity = (BlockEntityBacteria) level.getBlockEntity(pos);
        assert blockEntity != null;
        blockEntity.id = id;
        blockEntity.infected.addAll(infected);
    }


    public void expandAndRemove() {
        expand(worldPosition.north());
        expand(worldPosition.south());
        expand(worldPosition.west());
        expand(worldPosition.east());
        expand(worldPosition.above());
        expand(worldPosition.below());
        remove();
    }

    public void remove() {
        level.setBlockAndUpdate(worldPosition, Blocks.AIR.defaultBlockState());
    }

    public boolean isOutside(BlockPos pos) {
        return pos.getY() > level.getMaxY() || pos.getY() < level.getMinY();
    }

    @Override
    protected void loadAdditional(final ValueInput input) {
        super.loadAdditional(input);
        if (input.child(Constants.MOD_ID).isEmpty()) {
            return;
        }
        final ValueInput main = input.child(Constants.MOD_ID).orElseThrow();
        if (main.getInt("id").isPresent()) {
            this.id = main.getInt("id").orElseThrow();
        }
        if (main.childrenList("entries").isPresent()) {
            final ValueInput.ValueInputList entries = main.childrenList("entries").orElseThrow();
            for (final ValueInput entry : entries) {
                entry.read(Entry.CODEC).map(this.infected::add);
            }
        }
    }

    @Override
    protected void saveAdditional(final ValueOutput output) {
        super.saveAdditional(output);
        final ValueOutput main = output.child(Constants.MOD_ID);
        main.putInt("id", id);
        final ValueOutput.ValueOutputList entries = main.childrenList("entries");
        for (final Entry entry : infected) {
            entry.write(entries.addChild());
        }
    }
}
