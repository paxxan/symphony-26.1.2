package net.paxxan;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.paxxan.datagen.ModBlockTagsProvider;
import net.paxxan.datagen.ModEntityLootTableProvider;
import net.paxxan.datagen.SymphonySoundProvider;
import net.paxxan.datagen.advancement.SymphonyAdvancementProvider;

public class SymphonyDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(SymphonyAdvancementProvider::new);
		pack.addProvider(ModEntityLootTableProvider::new);
		pack.addProvider(SymphonySoundProvider::new);
		pack.addProvider(ModBlockTagsProvider::new);
	}
}
