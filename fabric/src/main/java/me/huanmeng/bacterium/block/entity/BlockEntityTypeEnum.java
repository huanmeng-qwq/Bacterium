package me.huanmeng.bacterium.block.entity;

import me.huanmeng.bacterium.Bacterium;
import me.huanmeng.bacterium.block.BlockType;
import me.huanmeng.bacterium.block.entity.impl.BlockEntityBacteria;
import me.huanmeng.bacterium.block.entity.impl.BlockEntityReplacer;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;

/**
 * 2024/5/2<br>
 * Bacterium<br>
 *
 * @author huanmeng_qwq
 */
public enum BlockEntityTypeEnum {
    BACTERIA(Bacterium.id("bacterium_colony"),
            BlockEntityType.Builder.create(BlockEntityBacteria::new, BlockType.BACTERIA.block).build(null)
    ),
    REPLACER(Bacterium.id("replacer"), BlockEntityType.Builder.create(BlockEntityReplacer::new, BlockType.REPLACER.block).build(null)),
    ;
    public final Identifier id;
    public final BlockEntityType<? extends AbstractBlockEntityBacteria> entityType;

    BlockEntityTypeEnum(Identifier id, BlockEntityType<? extends AbstractBlockEntityBacteria> entityType) {
        this.id = id;
        this.entityType = entityType;
    }
}
