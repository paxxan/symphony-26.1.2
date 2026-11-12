package net.paxxan.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.paxxan.Symphony;

public class ModSounds {

    public static final SoundType PAPER = new SoundType(1.0f, 1.0f,
            SoundEvents.PAINTING_BREAK,
            SoundEvents.BOOK_PUT,
            SoundEvents.BOOK_PAGE_TURN,
            SoundEvents.BOOK_PUT,
            SoundEvents.BOOK_PUT);

    public static final SoundEvent POOF = register("poof");

    public static final SoundEvent FLUTE_CHARGE = register("flute_charge");

    public static final SoundEvent FLUTE_FULL_CHARGE = register("flute_full_charge");

    private static SoundEvent register(String name) {
        Identifier id = Symphony.id(name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void registerSounds() {
        Symphony.LOGGER.info("Registering sounds for " + Symphony.MOD_ID);
    }
}
