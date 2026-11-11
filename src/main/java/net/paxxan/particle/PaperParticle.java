package net.paxxan.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class PaperParticle extends SingleQuadParticle {
    private final float swayPhase;
    private final float spin;
    private static final float FADE_START = 0.6F;

    protected PaperParticle(ClientLevel level, double x, double y, double z,
                            double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd, sprites.get(level.getRandom()));
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.lifetime = 60 + random.nextInt(40);
        this.quadSize = 0.16f + random.nextFloat() * 0.08f;
        this.swayPhase = random.nextFloat() * Mth.TWO_PI;
        this.spin = (random.nextFloat() - 0.5f) * 0.3f;
        this.roll = random.nextFloat() * Mth.TWO_PI;
        this.oRoll = this.roll;
        this.hasPhysics = true;

    }

    private float lifeProgress(float partialTick) {
        return Mth.clamp((this.age + partialTick) / this.lifetime, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        xo = x; yo = y; zo = z;
        oRoll = roll;
        if (age++ >= lifetime) { remove(); return; }
        float t = (float) this.age / this.lifetime;
        if (t < FADE_START) {
            this.alpha = 1.0F;
        } else {
            this.alpha = 1.0F - (t - FADE_START) / (1.0F - FADE_START);
        }
        this.alpha = Mth.clamp(this.alpha, 0.0F, 1.0F);

        if (!onGround) {
            xd *= 0.85; zd *= 0.85;
            yd = Math.max(yd * 0.90 - 0.008, -0.06);
            xd += Mth.sin(age * 0.30f + swayPhase) * 0.008;
            zd += Mth.cos(age * 0.30f + swayPhase) * 0.008;
            roll += spin;
        } else {
            xd = 0; yd = 0; zd = 0;
        }
        move(xd, yd, zd);
    }

    @Override
    protected Layer getLayer() {
        return SingleQuadParticle.Layer.TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public Provider(SpriteSet sprites) { this.sprites = sprites; }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xd, double yd, double zd, RandomSource random) {
            return new PaperParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }
}