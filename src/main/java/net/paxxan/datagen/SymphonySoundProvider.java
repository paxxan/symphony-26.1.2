package net.paxxan.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.paxxan.Symphony;
import net.paxxan.sound.ModSounds;

import java.util.concurrent.CompletableFuture;

public class SymphonySoundProvider extends FabricSoundsProvider {
    public SymphonySoundProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registryLookup, SoundExporter exporter) {
        exporter.add(ModSounds.POOF, SoundTypeBuilder.of(ModSounds.POOF)
                .subtitle("sounds.symphony.poof")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Symphony.id("poof1")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Symphony.id("poof2")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Symphony.id("poof3")))
        );

    }

    @Override
    public String getName() {
        return "Symphony Sounds";
    }
}
