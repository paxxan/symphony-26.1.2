package net.paxxan;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.paxxan.attachments.ModDataAttachments;
import net.paxxan.commands.ModCommands;
import net.paxxan.effect.ModEffects;
import net.paxxan.entity.ModEntities;
import net.paxxan.entity.custom.FluteSpirit;
import net.paxxan.entity.custom.IllusionEntity;
import net.paxxan.item.ModComponents;
import net.paxxan.item.ModItems;
import net.paxxan.networking.ModPackets;
import net.paxxan.sound.ModSounds;
import net.paxxan.tags.ModTags;
import net.paxxan.worldgen.gen.ModWorldGeneration;
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
		ModEffects.registerEffects();
		ModSounds.registerSounds();
		FabricDefaultAttributeRegistry.register(ModEntities.FLUTE_SPIRIT, FluteSpirit.createAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.ILLUSION, IllusionEntity.createAttributes());
		ModWorldGeneration.generateModWorldGen();
		ModDataAttachments.registerAttachments();
		ModCommands.registerCommands();
		SpawnPlacements.register(ModEntities.FLUTE_SPIRIT, SpawnPlacementTypes.ON_GROUND,
				Heightmap.Types.MOTION_BLOCKING, FluteSpirit::checkFluteSpiritSpawnRules);

		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
			if (entity instanceof IllusionEntity) {
				entity.discard();
				return false;
			}
			return true;
		});
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
