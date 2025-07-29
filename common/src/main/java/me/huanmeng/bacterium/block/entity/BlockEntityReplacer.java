package me.huanmeng.bacterium.block.entity;

import me.huanmeng.bacterium.BacteriumCache;
import me.huanmeng.bacterium.Constants;
import me.huanmeng.bacterium.block.ModBlock;
import me.huanmeng.bacterium.block.ModBlocks;
import me.huanmeng.bacterium.type.ModBlockType;
import me.huanmeng.bacterium.util.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class BlockEntityReplacer extends BlockEntity {
    protected int id;
    protected Entry bacteria;// up
    protected Entry sample;// down

    public BlockEntityReplacer(BlockPos pos, BlockState blockState) {
        this(ModBlocks.BLOCK_ENTITY_REPLACER.get(), pos, blockState);
    }

    public BlockEntityReplacer(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.id = BacteriumCache.freeId();
    }

    public void findInfectBlcoks() {
        final BlockPos above = worldPosition.above();
        final BlockPos below = worldPosition.below();

        final BlockState bacteriaState = level.getBlockState(above);
        final BlockState sampleState = level.getBlockState(below);
        if (!isInvalidBacteria(bacteriaState) && !BlockEntityBacteria.isInvalidBlock(sampleState) && bacteriaState != sampleState) {
            bacteria = new Entry(bacteriaState, level, level.getBlockEntity(above));
            sample = new Entry(sampleState, level, level.getBlockEntity(below));
            level.setBlockAndUpdate(above, Blocks.AIR.defaultBlockState());
        }
    }

    public static boolean isInvalidBacteria(BlockState state) {
        Block block = state.getBlock();
        return state.isAir() ||
                block == Blocks.BEDROCK ||
                block == ModBlocks.JAMMER.get() ||
                block == Blocks.BRICKS
                ;
    }

    public void addInfectBlock(BlockState state, BlockPos pos) {
        if (BlockEntityBacteria.isInvalidBlock(state)) {
            return;
        }
        bacteria = new Entry(state, level, level.getBlockEntity(pos));
    }

    public boolean isInfectBlock(BlockState state) {
        if (BacteriumCache.isUsed(id)) return false;
        if (sample == null) return false;
        if (state.getBlock() instanceof ModBlock block && block.getModBlockType() == ModBlockType.JAMMER) {
            // remove all of this id
            BacteriumCache.markUsed(id);
            return false;
        }
        return BlockEntityBacteria.matchState(sample, state);
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
        BlockEntityReplacer blockEntity = (BlockEntityReplacer) level.getBlockEntity(pos);
        if (blockEntity != null) {
            blockEntity.id = id;
            blockEntity.sample = sample;
            blockEntity.bacteria = bacteria;
        }
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
        if (bacteria != null) {
            bacteria.place(level, worldPosition);
        } else {
            level.setBlockAndUpdate(worldPosition, Blocks.AIR.defaultBlockState());
        }
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
        if (main.child("bacteria").isPresent()) {
            this.bacteria = main.child("bacteria").flatMap(e -> e.read(Entry.CODEC)).orElseThrow();

        }
        if (main.child("sample").isPresent()) {
            this.sample = main.child("sample").flatMap(e -> e.read(Entry.CODEC)).orElseThrow();
        }
    }

    @Override
    protected void saveAdditional(final ValueOutput output) {
        super.saveAdditional(output);
        final ValueOutput main = output.child(Constants.MOD_ID);
        main.putInt("id", id);
        if (bacteria != null)
            bacteria.write(main.child("bacteria"));
        if (sample != null)
            sample.write(main.child("sample"));
    }

}
