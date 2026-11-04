package net.paxxan.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.paxxan.item.ModComponents;
import net.paxxan.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class FluteHoldPoseMixin {
    @Shadow
    public final ModelPart rightArm;

    @Shadow
    public final ModelPart head;

    public FluteHoldPoseMixin(ModelPart rightArm, ModelPart head) {
        this.rightArm = rightArm;
        this.head = head;
    }

    @Inject(at = @At("TAIL"), method = "poseRightArm")
    private void init(HumanoidRenderState state, CallbackInfo info) {
        if(state.rightHandItemStack.is(ModItems.FLUTE)) {
            if(state.rightHandItemStack.get(ModComponents.FLUTE_PLAYING_COMPONENT) == true) {
                this.head.xRot = 0;
                this.rightArm.y += 4;
                this.rightArm.z += 1;
                this.rightArm.xRot = Mth.clamp(this.head.xRot - 2.2F - (state.isCrouching ? (float) (Math.PI / 12) : 0.0F), -2.4F, 3.3F);
            }
        }
    }
}

