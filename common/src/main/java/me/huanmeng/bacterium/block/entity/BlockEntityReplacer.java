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
import net.minecraft.nbt.NbtOps;
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
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (!tag.contains(Constants.MOD_ID)) {
            return;
        }
        final CompoundTag main = tag.getCompound(Constants.MOD_ID).orElseThrow();
        if (main.contains("id")) {
            this.id = main.getInt("id").orElseThrow();
        }
        if (main.contains("bacteria")) {
            Entry.CODEC.decode(NbtOps.INSTANCE, main.get("bacteria")).result().ifPresent(e -> {
                this.bacteria = e.getFirst();
            });
        }
        if (main.contains("sample")) {
            Entry.CODEC.decode(NbtOps.INSTANCE, main.get("sample")).result().ifPresent(e -> {
                this.sample = e.getFirst();
            });
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        final CompoundTag main = new CompoundTag();
        main.putInt("id", id);
        if (bacteria != null)
            main.put("bacteria", bacteria.toNbt());
        if (sample != null)
            main.put("sample", sample.toNbt());
        tag.put(Constants.MOD_ID, main);
    }

}
