package me.huanmeng.bacterium.block;

import me.huanmeng.bacterium.item.ItemType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 2024/5/2<br>
 * Bacterium<br>
 *
 * @author huanmeng_qwq
 */
public class BlockMust extends Block {
    public static final int MAX = 7;
    public static final IntProperty GROWN = IntProperty.of("grown", 0, MAX);

    public BlockMust(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(GROWN);
        super.appendProperties(builder);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        if (state.get(GROWN) >= MAX) {
            final ItemStack stack = ItemType.BUNCH.item.getDefaultStack();
            LootContextParameterSet lootContext = builder.add(LootContextParameters.BLOCK_STATE, state).build(LootContextTypes.BLOCK);
            Random random = ThreadLocalRandom.current();
            int r = random.nextInt(3);
            final ItemStack tool = lootContext.get(LootContextParameters.TOOL);
            if (tool != null && !tool.isEmpty()) {
                int m = Math.max(1, random.nextInt(4));
                r += EnchantmentHelper.getLevel(Enchantments.FORTUNE, tool) * m;
                r += (int) (lootContext.getLuck() * random.nextInt(2));
            }
            if (r > 0) {
                stack.setCount(r);
            }
            return Collections.singletonList(stack);
        }
        return super.getDroppedStacks(state, builder);
    }
}
