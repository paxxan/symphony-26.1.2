package net.paxxan.item;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.paxxan.Symphony;

public class ModComponents {
    public static final DataComponentType<Boolean> FLUTE_PLAYING_COMPONENT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Symphony.id("flute_playing"),
            DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build()
    );
    public static final DataComponentType<String> FLUTE_SEQUENCE_COMPONENT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Symphony.id("flute_sequence"),
            DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build()
    );

    public static final DataComponentType<String> SPELL_ID = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Symphony.id("spell_id"),
            DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build()
    );


    public static void registerComponents() {
        Symphony.LOGGER.info("Registering data components for " + Symphony.MOD_ID);
    }
}
