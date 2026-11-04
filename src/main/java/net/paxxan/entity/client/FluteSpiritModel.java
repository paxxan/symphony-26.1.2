package net.paxxan.entity.client;
// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.paxxan.Symphony;

public class FluteSpiritModel extends EntityModel<FluteSpiritRenderState> implements ArmedModel<FluteSpiritRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Symphony.id("forestspiritmodel"), "main");
	private final ModelPart head;
	private final ModelPart stalk;
	private final ModelPart left_moss;
	private final ModelPart right_moss;
	private final ModelPart body;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart left_wing;
	private final ModelPart right_wing;
	private static final float FLYING_ANIMATION_X_ROT = (float) (Math.PI / 4);
	private static final float MAX_HAND_HOLDING_ITEM_X_ROT_RAD = -1.134464F;
	private static final float MIN_HAND_HOLDING_ITEM_X_ROT_RAD = (float) (-Math.PI / 3);

	public FluteSpiritModel(ModelPart root) {
		super(root, RenderTypes::entityTranslucent);
		this.head = root.getChild("head");
		this.stalk = this.head.getChild("stalk");
		this.left_moss = this.head.getChild("left_moss");
		this.right_moss = this.head.getChild("right_moss");
		this.body = root.getChild("body");
		this.right_arm = root.getChild("right_arm");
		this.left_arm = root.getChild("left_arm");
		this.left_wing = root.getChild("left_wing");
		this.right_wing = root.getChild("right_wing");

	}


	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 0.0F));

		PartDefinition stalk = head.addOrReplaceChild("stalk", CubeListBuilder.create().texOffs(0, 23).addBox(-3.5F, -10.0F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_moss = head.addOrReplaceChild("left_moss", CubeListBuilder.create().texOffs(24, 13).addBox(0.0F, 0.0F, -2.0F, 0.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -3.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

		PartDefinition right_moss = head.addOrReplaceChild("right_moss", CubeListBuilder.create().texOffs(16, 13).addBox(0.0F, 0.0F, -2.0F, 0.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -3.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 10).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 18.0F, 0.0F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(23, 0).addBox(-0.75F, -0.5F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.75F, 18.5F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(23, 6).addBox(-0.25F, -0.5F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.75F, 18.5F, 0.0F));

		PartDefinition left_wing = partdefinition.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(16, 14).addBox(0.0F, 1.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 18.0F, 1.0F));

		PartDefinition right_wing = partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(16, 14).addBox(0.0F, 1.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 18.0F, 1.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(FluteSpiritRenderState state) {
		super.setupAnim(state);
		float animationSpeed = state.walkAnimationSpeed;
		float animationPos = state.walkAnimationPos;
		float flapSpeed = state.ageInTicks * 20.0F * (float) (Math.PI / 180.0) + animationPos;
		float flapAmount = Mth.cos(flapSpeed) * (float) Math.PI * 0.15F + animationSpeed;
		float idleBobSpeed = state.ageInTicks * 9.0F * (float) (Math.PI / 180.0);
		float flyingFactor = Math.min(animationSpeed / 0.3F, 1.0F);
		float idleBobFactor = 1.0F - flyingFactor;
		float holdingItemFactor = state.holdingAnimationProgress;
		if (state.isDancing) {
			float danceSpeed = state.ageInTicks * 8.0F * (float) (Math.PI / 180.0) + animationSpeed;
			float danceFrequency = Mth.cos(danceSpeed) * 16.0F * (float) (Math.PI / 180.0);
			float spinningRotation = state.spinningProgress;
			float headTiltZ = Mth.cos(danceSpeed) * 14.0F * (float) (Math.PI / 180.0);
			float headTiltY = Mth.cos(danceSpeed) * 30.0F * (float) (Math.PI / 180.0);
			this.root.yRot = state.isSpinning ? (float) (Math.PI * 4) * spinningRotation : this.root.yRot;
			this.root.zRot = danceFrequency * (1.0F - spinningRotation);
			this.head.yRot = headTiltY * (1.0F - spinningRotation);
			this.head.zRot = headTiltZ * (1.0F - spinningRotation);
		} else {
			this.head.xRot = state.xRot * (float) (Math.PI / 180.0);
			this.head.yRot = state.yRot * (float) (Math.PI / 180.0);
		}

		this.right_wing.xRot = 0.43633232F * (1.0F - flyingFactor);
		this.right_wing.yRot = (float) (-Math.PI / 4) + flapAmount;
		this.left_wing.xRot = 0.43633232F * (1.0F - flyingFactor);
		this.left_wing.yRot = (float) (Math.PI / 4) - flapAmount;
		this.body.xRot = flyingFactor * (float) (Math.PI / 4);
		float armFlyingRotX = holdingItemFactor * Mth.lerp(flyingFactor, (float) (-Math.PI / 3), -1.134464F);
		this.root.y = this.root.y + (float)Math.cos(idleBobSpeed) * 0.25F * idleBobFactor;
		this.right_arm.xRot = armFlyingRotX;
		this.left_arm.xRot = armFlyingRotX;
		float armIdleBobFactor = idleBobFactor * (1.0F - holdingItemFactor);
		float armIdleBobAmount = 0.43633232F - Mth.cos(idleBobSpeed + (float) (Math.PI * 3.0 / 2.0)) * (float) Math.PI * 0.075F * armIdleBobFactor;
		this.left_arm.zRot = -armIdleBobAmount;
		this.right_arm.zRot = armIdleBobAmount;
		this.right_arm.yRot = 0.27925268F * holdingItemFactor;
		this.left_arm.yRot = -0.27925268F * holdingItemFactor;
	}

	public void translateToHand(final FluteSpiritRenderState state, final HumanoidArm arm, final PoseStack poseStack) {
		float yOffset = 1.0F;
		float zOffset = 3.0F;
		this.root.translateAndRotate(poseStack);
		this.body.translateAndRotate(poseStack);
		poseStack.translate(0.0F, 0.0625F, 0.1875F);
		poseStack.mulPose(Axis.XP.rotation(this.right_arm.xRot));
		poseStack.scale(0.7F, 0.7F, 0.7F);
		poseStack.translate(0.0625F, 0.0F, 0.0F);
	}

}