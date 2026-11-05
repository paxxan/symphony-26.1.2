package net.paxxan.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.paxxan.Symphony;
import net.paxxan.effect.ModEffects;

import java.util.List;

import static net.minecraft.world.item.component.Consumables.defaultFood;

public class ModFoods {
    public static final FoodProperties GREEN_APPLE = new FoodProperties.Builder().nutrition(4).saturationModifier(1.2F).alwaysEdible().build();
    public static final Consumable GREEN_APPLE_CONSUMABLE = defaultFood()
            .onConsume(
                    new ApplyStatusEffectsConsumeEffect(
                            List.of(
                                    new MobEffectInstance(MobEffects.REGENERATION, 400, 1),
                                    new MobEffectInstance(MobEffects.RESISTANCE, 6000, 0),
                                    new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3),
                                    new MobEffectInstance(ModEffects.FULL_LUNGS, 1200, 2)
                            )
                    )
            )
            .build();

    public static void registerFoods() {
        Symphony.LOGGER.info("Registering Foods for " + Symphony.MOD_ID);
    }
}

