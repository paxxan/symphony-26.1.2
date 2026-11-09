package net.paxxan.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.paxxan.Symphony;

public class ModSounds {

    public static final SoundEvent POOF = register("poof");

    private static SoundEvent register(String name) {
        Identifier id = Symphony.id(name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void registerSounds() {
        Symphony.LOGGER.info("Registering sounds for " + Symphony.MOD_ID);
    }
}
