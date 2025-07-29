package me.huanmeng.bacterium.block.entity;

import com.mojang.datafixers.util.Pair;
import me.huanmeng.bacterium.BacteriumCache;
import me.huanmeng.bacterium.Constants;
import me.huanmeng.bacterium.block.ModBlock;
import me.huanmeng.bacterium.block.ModBlocks;
import me.huanmeng.bacterium.type.ModBlockType;
import me.huanmeng.bacterium.util.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
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

    @SuppressWarnings("DuplicatedCode")
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (!tag.contains(Constants.MOD_ID, Tag.TAG_COMPOUND)) {
            return;
        }
        final CompoundTag main = tag.getCompound(Constants.MOD_ID);
        if (main.contains("id", Tag.TAG_INT)) {
            this.id = main.getInt("id");
        }
        if (main.contains("entries")) {
            final ListTag entries = main.getList("entries", Tag.TAG_COMPOUND);
            for (final Tag entry : entries) {
                final Pair<Entry, Tag> e = Entry.CODEC.decode(NbtOps.INSTANCE, entry).result().orElse(null);
                if (e != null) {
                    this.infected.add(e.getFirst());
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        final CompoundTag main = new CompoundTag();
        main.putInt("id", id);
        final ListTag entries = new ListTag();
        for (final Entry entry : infected) {
            entries.add(entry.toNbt());
        }
        main.put("entries", entries);
        tag.put(Constants.MOD_ID, main);
    }
}
