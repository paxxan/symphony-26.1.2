package net.paxxan.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.paxxan.Symphony;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> VALUABLE_BLOCKS = createTag("valuable_blocks");

        private static TagKey<Block> createTag(String name) {
            return TagKey.create(Registries.BLOCK, Symphony.id(name));
        }
    }

    public static class Items {
        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, Symphony.id(name));
        }
    }

}
