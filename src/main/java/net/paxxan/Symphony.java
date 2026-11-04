package net.paxxan;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.resources.Identifier;

import net.paxxan.entity.ModEntities;
import net.paxxan.entity.custom.FluteSpirit;
import net.paxxan.item.ModComponents;
import net.paxxan.item.ModItems;
import net.paxxan.networking.ModPackets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Symphony implements ModInitializer {
	public static final String MOD_ID = "symphony";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerItems();
		ModPackets.registerPackets();
		ModEntities.registerEntities();
		ModComponents.registerComponents();
		FabricDefaultAttributeRegistry.register(ModEntities.FLUTE_SPIRIT, FluteSpirit.createAttributes());
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
