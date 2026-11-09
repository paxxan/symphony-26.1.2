package net.paxxan.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.paxxan.Symphony;

public class ModEffects {

    public static final Holder<MobEffect> PACIFISM_EFFECT = register("pacifism",
            new PacifismEffect(MobEffectCategory.NEUTRAL, 0xFFFFFF));

    public static final Holder<MobEffect> FULL_LUNGS = register("full_lungs",
            new FullLungsEffect(MobEffectCategory.BENEFICIAL, 0xFF7777));

    public static final Holder<MobEffect> SPELUNKER = register("spelunker",
            new SpelunkerEffect(MobEffectCategory.BENEFICIAL, 0xFFFF00));

    private static Holder<MobEffect> register(String name, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Symphony.id(name), effect);
    }

    public static void registerEffects() {
        Symphony.LOGGER.info("Registering Effects for " + Symphony.MOD_ID);
    }
}
