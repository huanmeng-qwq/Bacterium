package me.huanmeng.bacterium.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;

import java.util.List;

/**
 * 2024/5/2<br>
 * Bacterium<br>
 *
 * @author huanmeng_qwq
 */
public class BlockJammer extends Block {
    public BlockJammer(Settings settings) {
        super(settings);
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        return super.getDroppedStacks(state, builder);
    }
}
