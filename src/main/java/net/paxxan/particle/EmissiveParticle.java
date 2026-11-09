package net.paxxan.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.paxxan.Symphony;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class EmissiveParticle extends SingleQuadParticle {

    private final float startSize;
    private EmissiveParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
        this.lifetime = level.getRandom().nextIntBetweenInclusive(40, 140);
        this.quadSize = 0.15F;
        this.startSize = this.quadSize;
    }

    private float lifeProgress(float partialTick) {
        return Mth.clamp((this.age + partialTick) / this.lifetime, 0.0F, 1.0F);
    }

    @Override
    protected int getLightCoords(float a) {
        return 0xF000F0;
    }

    @Override
    public float getQuadSize(float partialTick) {
        return this.startSize * (1.0F - lifeProgress(partialTick));
    }

    @Override
    public void tick() {
        super.tick();
        float t = (float) this.age / this.lifetime;
        this.alpha = 1.0F - t;
        float shade = 1.0F - 0.5F * t;
        this.rCol = shade;
        this.gCol = shade;
        this.bCol = shade;
    }



    @Override
    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new EmissiveParticle(level, x, y, z, sprites.get(random));
        }
    }
}
