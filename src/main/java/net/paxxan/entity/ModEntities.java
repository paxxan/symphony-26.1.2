package net.paxxan.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.Mannequin;
import net.paxxan.Symphony;
import net.paxxan.entity.custom.FluteSpirit;
import net.paxxan.entity.custom.IllusionEntity;

public class ModEntities {
    public static final ResourceKey<EntityType<?>> FLUTE_SPIRIT_KEY = ResourceKey.create(Registries.ENTITY_TYPE,
            Symphony.id("flute_spirit"));

    public static final ResourceKey<EntityType<?>> ILLUSION_KEY = ResourceKey.create(Registries.ENTITY_TYPE,
            Symphony.id("illusion"));

    public static final EntityType<FluteSpirit> FLUTE_SPIRIT = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Symphony.id("flute_spirit"),
            EntityType.Builder.of(FluteSpirit::new, MobCategory.CREATURE)
                    .sized(0.3f, 0.6f)
                    .build(FLUTE_SPIRIT_KEY));

    public static final EntityType<IllusionEntity> ILLUSION = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            Symphony.id("illusion"),
            EntityType.Builder.<IllusionEntity>of(IllusionEntity::new, MobCategory.MISC)
                    .sized(0.6F, 1.8F)
                    .eyeHeight(1.62F)
                    .vehicleAttachment(Avatar.DEFAULT_VEHICLE_ATTACHMENT)
                    .clientTrackingRange(32)
                    .updateInterval(2)
                    .build(ILLUSION_KEY)
    );

    public static void registerEntities() {
        Symphony.LOGGER.info("Registering entities for " + Symphony.MOD_ID);
    }
}
