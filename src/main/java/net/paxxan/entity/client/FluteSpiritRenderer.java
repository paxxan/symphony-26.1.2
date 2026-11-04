package net.paxxan.entity.client;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AllayRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.allay.Allay;
import net.paxxan.Symphony;
import net.paxxan.entity.custom.FluteSpirit;

public class FluteSpiritRenderer extends MobRenderer<FluteSpirit, FluteSpiritRenderState, FluteSpiritModel> {
    private static final Identifier FLUTE_SPIRIT_TEXTURE = Symphony.id("textures/entity/flute_spirit.png");

    public FluteSpiritRenderer(EntityRendererProvider.Context context) {
        super(context, new FluteSpiritModel(context.bakeLayer(ModModelLayerLocations.FLUTE_SPIRIT)), 0.4F);
        this.addLayer(new ItemInHandLayer<>(this));
    }


    @Override
    public Identifier getTextureLocation(FluteSpiritRenderState state) {
        return FLUTE_SPIRIT_TEXTURE;
    }

    @Override
    public FluteSpiritRenderState createRenderState() {
        return new FluteSpiritRenderState();
    }

    public void extractRenderState(final FluteSpirit entity, final FluteSpiritRenderState state, final float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        ArmedEntityRenderState.extractArmedEntityRenderState(entity, state, this.itemModelResolver, partialTicks);
        state.isDancing = entity.isDancing();
        state.isSpinning = entity.isSpinning();
        state.spinningProgress = entity.getSpinningProgress(partialTicks);
        state.holdingAnimationProgress = entity.getHoldingItemAnimationProgress(partialTicks);
    }

    protected int getBlockLightLevel(final FluteSpirit entity, final BlockPos blockPos) {
        return 15;
    }
}
