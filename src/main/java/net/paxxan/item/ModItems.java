package net.paxxan.item;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Block;
import net.paxxan.Symphony;
import net.paxxan.entity.ModEntities;
import net.paxxan.food.ModFoods;
import net.paxxan.item.custom.FluteItem;
import net.paxxan.item.custom.SpellbookItem;

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

    public static final Item SPELLBOOK = register(
            "spellbook",
            properties -> new SpellbookItem(properties
                    .component(ModComponents.SPELL_ID, "crop_growth")
                    .useBlockDescriptionPrefix()
                    .stacksTo(1)
                    .rarity(Rarity.RARE)
                    .setId(ResourceKey.create(Registries.ITEM, Symphony.id("spellbook")))));


    private static Item register(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, Symphony.id(name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Symphony.id(name)))));
    }


    public static final ResourceKey<CreativeModeTab> SYMPHONY_CREATIVE_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(), Symphony.id("a_symphony_creative_tab")
    );

    public static final CreativeModeTab SYMPHONY_CREATIVE_TAB = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(FLUTE))
            .title(Component.translatable("creativeTab.symphony.symphony"))
            .displayItems(((parameters, output) -> {
                output.accept(FLUTE);
                output.accept(GREEN_APPLE);
                output.accept(FLUTE_SPIRIT_SPAWN_EGG);
            }))
            .build();


    public static final ResourceKey<CreativeModeTab> SPELLBOOKS_CREATIVE_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(), Symphony.id("b_spellbooks_creative_tab")
    );


    public static final CreativeModeTab SPELLBOOKS_CREATIVE_TAB = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(SPELLBOOK))
            .title(Component.translatable("creativeTab.symphony.spellbooks"))
            .displayItems(((parameters, output) -> {
                addSpellbookToTab("crop_growth", output);
                addSpellbookToTab("heal_prayer", output);
                addSpellbookToTab("leap", output);
                addSpellbookToTab("pacifism", output);
                addSpellbookToTab("shc", output);
                addSpellbookToTab("spelunker", output);
                addSpellbookToTab("illusion", output);
                addSpellbookToTab("home", output);
                addSpellbookToTab("war_horn", output);
                addSpellbookToTab("beanstalk", output);
                addSpellbookToTab("food", output);
                addSpellbookToTab("poison", output);
            }))
            .build();



    private static void addSpellbookToTab(String id, CreativeModeTab.Output output) {
        ItemStack stack = new ItemStack(SPELLBOOK);
        stack.set(ModComponents.SPELL_ID, id);
        output.accept(stack);
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
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, SYMPHONY_CREATIVE_TAB_KEY, SYMPHONY_CREATIVE_TAB);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, SPELLBOOKS_CREATIVE_TAB_KEY, SPELLBOOKS_CREATIVE_TAB);


    }
}
