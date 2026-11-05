package net.paxxan.item;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumables;
import net.paxxan.Symphony;
import net.paxxan.entity.ModEntities;
import net.paxxan.food.ModFoods;
import net.paxxan.item.custom.FluteItem;

import java.util.function.Function;

public class ModItems {

    public static final Item FLUTE = register(
            "flute",
            properties -> new FluteItem(properties.durability(64).component(ModComponents.FLUTE_PLAYING_COMPONENT, false).component(ModComponents.FLUTE_SEQUENCE_COMPONENT, "")));

    public static final Item FLUTE_SPIRIT_SPAWN_EGG = register(
            "flute_spirit_spawn_egg",
            properties -> new SpawnEggItem(properties.spawnEgg(ModEntities.FLUTE_SPIRIT)));

    public static final Item GREEN_APPLE = register(
            "green_apple",
            properties -> new Item(properties
                    .rarity(Rarity.RARE)
                    .food(ModFoods.GREEN_APPLE, ModFoods.GREEN_APPLE_CONSUMABLE)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
            )
    );

    private static Item register(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, Symphony.id(name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Symphony.id(name)))));
    }


    public static void registerItems() {
        Symphony.LOGGER.info("Registering items for " + Symphony.MOD_ID);
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register(output -> {
            output.accept(FLUTE_SPIRIT_SPAWN_EGG);
        });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(output -> {
            output.accept(FLUTE);
        });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(output -> {
            output.accept(GREEN_APPLE);
        });
    }
}
