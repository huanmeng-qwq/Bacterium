package me.huanmeng.bacterium.block.entity;

import me.huanmeng.bacterium.BacteriumCache;
import me.huanmeng.bacterium.Constants;
import me.huanmeng.bacterium.block.ModBlock;
import me.huanmeng.bacterium.block.ModBlocks;
import me.huanmeng.bacterium.type.ModBlockType;
import me.huanmeng.bacterium.util.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

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
        final BlockState bacteriaState = level.getBlockState(worldPosition.above());
        final BlockState sampleState = level.getBlockState(worldPosition.below());
        if (!isInvalidBacteria(bacteriaState) && !BlockEntityBacteria.isInvalidBlock(sampleState) && bacteriaState != sampleState) {
            bacteria = new Entry(bacteriaState);
            sample = new Entry(sampleState);
            level.setBlockAndUpdate(worldPosition.above(), Blocks.AIR.defaultBlockState());
        }
    }

    public static boolean isInvalidBacteria(BlockState state) {
        Block block = state.getBlock();
        return block == Blocks.BEDROCK ||
                block == ModBlocks.JAMMER.get() ||
                block == Blocks.BRICKS
                ;
    }

    public void addInfectBlock(BlockState state) {
        if (BlockEntityBacteria.isInvalidBlock(state)) {
            return;
        }
        bacteria = new Entry(state);
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
        assert blockEntity != null;
        blockEntity.id = id;
        blockEntity.sample = sample;
        blockEntity.bacteria = bacteria;
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
            final BlockState state = bacteria.state();
            level.setBlockAndUpdate(worldPosition, state);
            if (bacteria.nbt != null) {
                if (level.getBlockEntity(worldPosition) != null) {
                    final BlockEntity blockEntity = BlockEntity.loadStatic(
                            worldPosition, state,
                            bacteria.nbt,
                            level.registryAccess()
                    );
                    if (blockEntity != null) {
                        level.setBlockEntity(blockEntity);
                    }
                }
            }
        } else {
            level.setBlockAndUpdate(worldPosition, Blocks.AIR.defaultBlockState());
        }
    }

    public boolean isOutside(BlockPos pos) {
        return pos.getY() > level.getMaxBuildHeight() || pos.getY() < level.getMinBuildHeight();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        writeLegacy(tag);
    }

    public void writeLegacy(CompoundTag compound) {
        final CompoundTag main = new CompoundTag();
        main.putInt("identifier", id);
        final CompoundTag entries = new CompoundTag();
//todo        for (Entry e : mbs) {
//            entries.put(RandomStringUtils.random(8), e.toNbt());
//        }
        main.put("entries", entries);
        compound.put(Constants.MOD_ID, main);
    }
}
