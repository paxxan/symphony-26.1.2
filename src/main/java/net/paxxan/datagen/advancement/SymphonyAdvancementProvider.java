package net.paxxan.datagen.advancement;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.ConsumeItemTrigger;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.paxxan.Symphony;
import net.paxxan.item.ModItems;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SymphonyAdvancementProvider extends FabricAdvancementProvider {

    public SymphonyAdvancementProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer) {
        final HolderLookup.RegistryLookup<Item> itemLookup = registryLookup.lookupOrThrow(Registries.ITEM);
        AdvancementHolder fluteObtain = Advancement.Builder.advancement()
                .display(
                        ModItems.FLUTE,
                        Component.literal("Floot"),
                        Component.literal("Obtain the Flute of the Winds"),
                        Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"),
                        AdvancementType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("obtain_flute_criteria", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FLUTE))
                .save(consumer, Symphony.id("obtain_flute"));

        AdvancementHolder soGreen = Advancement.Builder.advancement()
                .display(
                        ModItems.GREEN_APPLE,
                        Component.literal("It's Green Because it Can Be Placed Here"),
                        Component.literal("Eat a Green Apple"),
                        Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"),
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion("eat_green_apple_criteria", ConsumeItemTrigger.TriggerInstance.usedItem(itemLookup, ModItems.GREEN_APPLE))
                .parent(fluteObtain)
                .rewards(
                        new AdvancementRewards.Builder()
                                .addExperience(100)
                )
                .save(consumer, Symphony.id("so_green"));
    }
}
