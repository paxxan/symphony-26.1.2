package net.paxxan.entity.client;

import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class FluteSpiritRenderState extends ArmedEntityRenderState {
    public boolean isDancing;
    public boolean isSpinning;
    public float spinningProgress;
    public float holdingAnimationProgress;
}
