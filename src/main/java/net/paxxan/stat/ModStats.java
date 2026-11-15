package net.paxxan.stat;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.paxxan.Symphony;

public class ModStats {
    public static final Stat<?> FLUTE_SPELLS_CAST_STAT = makeCustomStat("flute_spells_cast");

    private static Stat<?> makeCustomStat(String key) {
        Identifier id = Symphony.id(key);
        Identifier newStat = Registry.register(BuiltInRegistries.CUSTOM_STAT, key, id);
        return Stats.CUSTOM.get(newStat, StatFormatter.DEFAULT);
    }
    public static void registerStats() {
        Symphony.LOGGER.info("Registering Stats for " + Symphony.MOD_ID);
    }
}
