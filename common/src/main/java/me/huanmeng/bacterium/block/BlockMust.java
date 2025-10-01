package me.huanmeng.bacterium.block;

import me.huanmeng.bacterium.item.ModItems;
import me.huanmeng.bacterium.type.ModBlockType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BlockMust extends Block implements ModBlock {
    public static final int MAX_STAGE = 7;
    public static final IntegerProperty GROWN = IntegerProperty.create("grown", 0, MAX_STAGE);

    public BlockMust(final Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(GROWN, 0));
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(GROWN);
    }

    @Override
    public ModBlockType getModBlockType() {
        return ModBlockType.MUST;
    }

    @Override
    protected List<ItemStack> getDrops(final BlockState state, final LootParams.Builder params) {
        if (state.getValue(GROWN) >= MAX_STAGE) {
            LootParams lootparams = params.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
            Random random = ThreadLocalRandom.current();
            int r = random.nextInt(3);
            final ItemStack tool = lootparams.contextMap().getOptional(LootContextParams.TOOL);
            if (tool != null && !tool.isEmpty()) {
                int m = Math.max(1, random.nextInt(4));
                Holder.Reference<Enchantment> entry = lootparams.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
                r += EnchantmentHelper.getItemEnchantmentLevel(entry, tool) * m;
                r += (int) (lootparams.getLuck() * random.nextInt(2));
            }

            final ItemStack stack = ModItems.BUNCH.get().getDefaultInstance();
            if (r > 0) {
                stack.setCount(r);
            }
            return Collections.singletonList(stack);
        }
        return super.getDrops(state, params);
    }

    @Override
    protected void randomTick(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random) {
        if (level.isClientSide()) {
            return;
        }
        final FluidState fluidState = level.getFluidState(pos.above());

        int meta = state.getValue(GROWN);
        int old = meta;
        if (meta >= MAX_STAGE) {
            return;
        }
        if (random.nextInt(3) >= 1) {
            ++meta;
        }
        if (!fluidState.is(FluidTags.WATER)) {
            meta = 0;
        }
        if (meta != old) {
            level.setBlockAndUpdate(pos, state.setValue(GROWN, meta));
        }
    }

    @Override
    protected boolean isRandomlyTicking(final BlockState state) {
        return state.getValue(GROWN) < MAX_STAGE;
    }
}
