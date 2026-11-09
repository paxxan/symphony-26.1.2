package net.paxxan.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.paxxan.tags.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
    public ModBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        valueLookupBuilder(ModTags.Blocks.VALUABLE_BLOCKS)
                .addOptionalTag(BlockTags.DIAMOND_ORES)
                .addOptionalTag(BlockTags.GOLD_ORES)
                .addOptionalTag(BlockTags.IRON_ORES)
                .addOptionalTag(BlockTags.EMERALD_ORES)
                .addOptionalTag(BlockTags.LAPIS_ORES)
                .addOptionalTag(BlockTags.REDSTONE_ORES)
                .addOptionalTag(BlockTags.COPPER_CHESTS)
                .addOptional(Blocks.DIAMOND_BLOCK)
                .addOptional(Blocks.GOLD_BLOCK)
                .addOptional(Blocks.EMERALD_BLOCK)
                .addOptional(Blocks.ANCIENT_DEBRIS)
                .addOptional(Blocks.NETHERITE_BLOCK)
                .addOptional(Blocks.BEACON)
                .addOptional(Blocks.CHEST)
                .addOptional(Blocks.BARREL)
                .addOptional(Blocks.TRAPPED_CHEST);

    }
}
