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
    public static final SimpleParticleType FOREST_SPIRIT_PARTICLE = FabricParticleTypes.simple();

    public static void registerParticles() {
        Symphony.LOGGER.info("Registering Particles for " + Symphony.MOD_ID);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Symphony.id("forest_spirit_particle"), FOREST_SPIRIT_PARTICLE);
    }
}
