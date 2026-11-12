package net.paxxan.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.paxxan.entity.ModEntities;
import net.paxxan.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModEntityLootTableProvider extends FabricEntityLootSubProvider {
    public ModEntityLootTableProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate() {
        this.add(ModEntities.FLUTE_SPIRIT,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(new UniformGenerator(new ConstantValue(0), new ConstantValue(1)))
                                .add(LootItem.lootTableItem(ModItems.GREEN_APPLE)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                ).when(LootItemKilledByPlayerCondition.killedByPlayer())
                        )
                        .withPool(LootPool.lootPool()
                                .setRolls(new UniformGenerator(new ConstantValue(4), new ConstantValue(8)))
                                .add(LootItem.lootTableItem(ModItems.MELODIC_ESSENCE)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                ).when(LootItemKilledByPlayerCondition.killedByPlayer())
                        )
        );
    }
}
