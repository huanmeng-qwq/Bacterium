package me.huanmeng.bacterium.datagen;

import me.huanmeng.bacterium.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class BacteriumDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(final FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(BlockDrops::new);
    }

    public static class BlockDrops extends FabricBlockLootSubProvider {

        protected BlockDrops(final FabricPackOutput packOutput, final CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(packOutput, registriesFuture);
        }

        @Override
        public void generate() {
            for (final Supplier<Block> blockSupplier : ModBlocks.blocks()) {
                final Block block = blockSupplier.get();
                dropSelf(block);
            }
        }
    }
}
