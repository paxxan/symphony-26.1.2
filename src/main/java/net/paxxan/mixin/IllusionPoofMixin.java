package net.paxxan.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.paxxan.entity.custom.IllusionEntity;
import net.paxxan.item.ModComponents;
import net.paxxan.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class IllusionPoofMixin {

    @Inject(at = @At("HEAD"), method = "actuallyHurt", cancellable = true)
    private void deleteIllusionOnHit(ServerLevel level, DamageSource source, float amount, CallbackInfo ci) {
        // Force-cast 'this' to check if the current living entity being hit is your illusion
        if ((Object) this instanceof IllusionEntity illusion) {
            illusion.discard(); // Instantly wipe it from the game world on the server
            ci.cancel();        // Stop Minecraft from processing ANY further damage/health math
        }
    }
}

