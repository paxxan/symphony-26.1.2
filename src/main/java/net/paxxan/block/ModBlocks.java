package net.paxxan.block;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.paxxan.Symphony;
import net.paxxan.block.custom.SpellbookStackBlock;
import net.paxxan.block.custom.SpellbookStackBlockEntity;
import net.paxxan.sound.ModSounds;

public class ModBlocks {
    public static final Block SPELLBOOK_STACK = Registry.register(
            BuiltInRegistries.BLOCK,
            Symphony.id("spellbook"), new SpellbookStackBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, Symphony.id("spellbook")))
                    .strength(0.5f)
                    .noOcclusion()
                    .sound(ModSounds.PAPER)
                    .noLootTable()));

    public static final BlockEntityType<SpellbookStackBlockEntity> SPELLBOOK_STACK_BE = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Symphony.id("spellbook"),
            FabricBlockEntityTypeBuilder
                    .create(SpellbookStackBlockEntity::new, SPELLBOOK_STACK).build());

    public static void registerBlocks(){
        Symphony.LOGGER.info("Registering Blocks for " + Symphony.MOD_ID);
    }
}
