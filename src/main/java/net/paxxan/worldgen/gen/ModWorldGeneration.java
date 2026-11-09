package net.paxxan.worldgen.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.client.entity.ClientMannequin;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.level.biome.Biomes;
import net.paxxan.entity.ModEntities;

public class ModWorldGeneration {
    public static void generateModWorldGen() {
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.MEADOW, Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.FLOWER_FOREST), MobCategory.CREATURE,
                ModEntities.FLUTE_SPIRIT, 5, 1, 1);
    }
}
