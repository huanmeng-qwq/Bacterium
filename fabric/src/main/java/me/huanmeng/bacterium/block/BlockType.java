package me.huanmeng.bacterium.block;

import me.huanmeng.bacterium.Bacterium;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.util.Identifier;

/**
 * 2024/5/2<br>
 * Bacterium<br>
 *
 * @author huanmeng_qwq
 */
public enum BlockType {
    JAMMER(Bacterium.id("jammer"), new BlockJammer(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_GREEN)
                    .strength(1, 6)
    )),
    MUST(Bacterium.id("must"), new BlockMust(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .strength(1, 6)
    )),
    BACTERIA(Bacterium.id("bacterium_colony"), new BlockBacteria(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PURPLE)
                    .strength(1, 6)
    )),
    REPLACER(Bacterium.id("replacer"), new BlockReplacer(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PURPLE)
                    .strength(1, 6)
    )),
    ;

    public final Identifier id;
    public final Block block;

    BlockType(Identifier id, Block block) {
        this.id = id;
        this.block = block;
    }
}
