package me.huanmeng.bacterium.item;

import me.huanmeng.bacterium.Bacterium;
import me.huanmeng.bacterium.block.BlockType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

/**
 * 2024/5/2<br>
 * Bacterium<br>
 *
 * @author huanmeng_qwq
 */
public enum ItemType {
    BUNCH(Bacterium.id("bunch"), new ItemBunch(new Item.Settings())),
    JAMMER(Bacterium.id("jammeritem"), new ItemJammer(new Item.Settings())),
    POTION(Bacterium.id("potion"), new ItemPotion(new Item.Settings())),
    // Block
    BACTERIA(Bacterium.id("bacterium_colony"), new BlockItem(BlockType.BACTERIA.block, new Item.Settings())),
    JAMMER_BLOCK(Bacterium.id("jammer"), new BlockItem(BlockType.JAMMER.block, new Item.Settings())),
    MUST(Bacterium.id("must"), new BlockItem(BlockType.MUST.block, new Item.Settings())),
    REPLACER(Bacterium.id("replacer"), new BlockItem(BlockType.REPLACER.block, new Item.Settings()));
    public final Identifier id;
    public final Item item;

    ItemType(Identifier id, Item item) {
        this.id = id;
        this.item = item;
    }
}
