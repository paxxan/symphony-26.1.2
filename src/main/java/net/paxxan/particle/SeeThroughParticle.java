package net.paxxan.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.Lightmap;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.RandomSource;
import net.paxxan.rendering.ModRenderPipelines;
import org.jspecify.annotations.Nullable;

public class SeeThroughParticle extends SingleQuadParticle {
    private static final Layer LAYER = new Layer(
            true, TextureAtlas.LOCATION_PARTICLES, ModRenderPipelines.SPELUNKER_PARTICLE);

    private final SpriteSet sprites;

    protected SeeThroughParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z, sprites.first());
        this.sprites = sprites;
        this.lifetime = 20;
        this.gravity = 0;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    protected Layer getLayer() {
        return LAYER;
    }

    @Override
    protected int getLightCoords(float a) {
        return 0xF000F0;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new SeeThroughParticle(level, x, y, z, this.sprites);
        }
    }
}
