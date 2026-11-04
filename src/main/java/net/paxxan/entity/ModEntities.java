package net.paxxan.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.paxxan.Symphony;
import net.paxxan.entity.custom.FluteSpirit;

public class ModEntities {
    public static final ResourceKey<EntityType<?>> FLUTE_SPIRIT_KEY = ResourceKey.create(Registries.ENTITY_TYPE,
            Symphony.id("flute_spirit"));

    public static final EntityType<FluteSpirit> FLUTE_SPIRIT = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Symphony.id("flute_spirit"),
            EntityType.Builder.of(FluteSpirit::new, MobCategory.CREATURE).sized(0.3f, 0.6f).build(FLUTE_SPIRIT_KEY));

    public static void registerEntities() {
        Symphony.LOGGER.info("Registering entities for " + Symphony.MOD_ID);
    }
}
