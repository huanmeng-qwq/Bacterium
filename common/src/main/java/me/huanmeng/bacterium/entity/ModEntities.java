package me.huanmeng.bacterium.entity;

import me.huanmeng.bacterium.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import java.util.function.Supplier;

public class ModEntities {
    public static Supplier<EntityType<PotionEntityBacteria>> POTION;

    public static class Namespaces {
        public static final ResourceLocation POTION = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "bacteria_potion");
    }
}
