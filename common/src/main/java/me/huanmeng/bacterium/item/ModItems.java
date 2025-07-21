package me.huanmeng.bacterium.item;

import me.huanmeng.bacterium.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import java.util.function.Supplier;

public class ModItems {
    public static Supplier<Item> BUNCH;

    public static class Namespaces {
        public static final ResourceLocation BUNCH = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "bunch");
    }
}
