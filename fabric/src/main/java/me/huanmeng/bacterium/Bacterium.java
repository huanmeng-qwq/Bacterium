package me.huanmeng.bacterium;

import me.huanmeng.bacterium.block.BlockType;
import me.huanmeng.bacterium.block.entity.BlockEntityTypeEnum;
import me.huanmeng.bacterium.entity.PotionEntityBacterium;
import me.huanmeng.bacterium.item.ItemType;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * 2024/5/2<br>
 * Bacterium<br>
 *
 * @author huanmeng_qwq
 */
public class Bacterium implements ModInitializer {
    public static final String MOD_ID = "bacterium";
    public static final int MAX_TICK = 50;// 2.5s

    public static boolean removeAll = false;
    public static int removeCount = 0;

    @Override
    public void onInitialize() {
        for (BlockType value : BlockType.values()) {
            Registry.register(Registries.BLOCK, value.id, value.block);
        }
        for (ItemType value : ItemType.values()) {
            Registry.register(Registries.ITEM, value.id, value.item);
        }
        for (BlockEntityTypeEnum value : BlockEntityTypeEnum.values()) {
            Registry.register(Registries.BLOCK_ENTITY_TYPE, value.id, value.entityType);
        }

        final EntityType<PotionEntityBacterium> potion = EntityType.Builder
                .<PotionEntityBacterium>create(PotionEntityBacterium::new, SpawnGroup.MISC)
                .dimensions(0.25F, 0.25F)
                .maxTrackingRange(4)
                .trackingTickInterval(10).build("bacteria_potion");
        Registry.register(Registries.ENTITY_TYPE, id("bacteria_potion"), potion);
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}
