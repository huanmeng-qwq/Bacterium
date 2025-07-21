package me.huanmeng.bacterium.block;

import me.huanmeng.bacterium.type.ModBlockType;
import net.minecraft.world.level.block.Block;

public class BlockJammer extends Block implements ModBlock {
    public BlockJammer(final Properties properties) {
        super(properties);
    }

    @Override
    public ModBlockType getModBlockType() {
        return ModBlockType.JAMMER;
    }
}
