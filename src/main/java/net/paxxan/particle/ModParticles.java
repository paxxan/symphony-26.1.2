package net.paxxan.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.paxxan.Symphony;

public class ModParticles {
    public static final SimpleParticleType FLUTE_SPIRIT_PARTICLE = Registry.register(
            BuiltInRegistries.PARTICLE_TYPE, Symphony.id("flute_spirit_particle"),
            FabricParticleTypes.simple()
    );

    public static final SimpleParticleType PAPER_PARTICLE = Registry.register(
            BuiltInRegistries.PARTICLE_TYPE, Symphony.id("paper"),
            FabricParticleTypes.simple()
    );

    public static final SimpleParticleType SPELUNKER_PARTICLE = Registry.register(
            BuiltInRegistries.PARTICLE_TYPE, Symphony.id("spelunker_particle"),
            FabricParticleTypes.simple()
    );

    public static void registerParticles() {
        Symphony.LOGGER.info("Registering Particles for " + Symphony.MOD_ID);
    }
}
