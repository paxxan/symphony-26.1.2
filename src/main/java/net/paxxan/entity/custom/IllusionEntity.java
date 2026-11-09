package net.paxxan.entity.custom;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.paxxan.entity.ModEntities;
import net.paxxan.sound.ModSounds;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;


public class IllusionEntity extends Mannequin {
    public static final int LIFETIME_TICKS = 10 * 60 * 20;

    private static final EntityDataAccessor<String> SKIN_NAME =
            SynchedEntityData.defineId(IllusionEntity.class, EntityDataSerializers.STRING);


    private int lifeTicks;
    private @Nullable UUID ownerUuid;

    public @Nullable UUID getOwnerUuid() {
        return this.ownerUuid;
    }

    public void setOwnerUuid(UUID uuid) {
        this.ownerUuid = uuid;
    }

    @SuppressWarnings("unchecked")
    public IllusionEntity(EntityType<IllusionEntity> type, Level level) {
        super((EntityType<Mannequin>) (EntityType<?>) type, level);

    }


    public static IllusionEntity spawn(ServerLevel level, Player player) {
        for (ServerLevel l : level.getServer().getAllLevels()) {
            for (IllusionEntity old : l.getEntities(ModEntities.ILLUSION,
                    e -> player.getUUID().equals(e.getOwnerUuid()))) {
                old.poof(level);
            }
        }
        IllusionEntity illusion = ModEntities.ILLUSION.create(level, EntitySpawnReason.TRIGGERED);
        level.playSound(null, player.blockPosition(), ModSounds.POOF, SoundSource.NEUTRAL, 1f, 1f);
        level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, player.position().x, player.position().y + (ModEntities.ILLUSION.getHeight() / 2), player.position().z, 32, 0.2, (ModEntities.ILLUSION.getHeight() / 2), 0.2, 0.01);
        if (illusion == null) {
            return null;
        }
        illusion.setPos(player.position());
        illusion.copyRotationFrom(player);
        illusion.setOwnerUuid(player.getUUID());
        illusion.setSkinName(player.getName().getString());
        level.addFreshEntity(illusion);
        illusion.copyEquipmentFrom(player);
        return illusion;
    }

    public void copyRotationFrom(LivingEntity source) {
        float yaw = source.getYRot();
        float pitch = source.getXRot();
        float head = source.getYHeadRot();

        this.setYRot(yaw);
        this.setXRot(pitch);
        this.setYHeadRot(head);
        this.setYBodyRot(yaw);

        this.setPose(source.getPose());
        this.setShiftKeyDown(source.isShiftKeyDown());

        this.yRotO = yaw;
        this.xRotO = pitch;
        this.yHeadRotO = head;
        this.yBodyRotO = yaw;
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return switch (pose) {
            case CROUCHING -> EntityDimensions.scalable(0.6F, 1.5F);
            case SWIMMING, FALL_FLYING -> EntityDimensions.scalable(0.6F, 0.6F);
            default -> super.getDefaultDimensions(pose);
        };
    }



    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SKIN_NAME, "");
    }

    @Override
    public Component getName() {
        return Component.literal(this.entityData.get(SKIN_NAME));
    }

    public String getSkinName() {
        return this.entityData.get(SKIN_NAME);
    }

    public void setSkinName(String name) {
        this.entityData.set(SKIN_NAME, name);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putString("SkinName", this.getSkinName());
        output.putInt("LifeTicks", this.lifeTicks);
        if (this.ownerUuid != null) {
            output.store("Owner", UUIDUtil.CODEC, this.ownerUuid);
        }

    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setSkinName(input.getStringOr("SkinName", ""));
        this.lifeTicks = input.getIntOr("LifeTicks", 0);
        this.ownerUuid = input.read("Owner", UUIDUtil.CODEC).orElse(null);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide() && ++this.lifeTicks >= LIFETIME_TICKS) {
            this.discard();
        }
    }



    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        this.discard();
        level.playSound(null, this.blockPosition(), ModSounds.POOF, SoundSource.NEUTRAL, 1f, 1f);
        level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.position().x, this.position().y + (ModEntities.ILLUSION.getHeight() / 2), this.position().z, 32, 0.2, (ModEntities.ILLUSION.getHeight() / 2), 0.2, 0.01);
        return true;

    }

    public boolean poof(ServerLevel level) {
        this.discard();
        level.playSound(null, this.blockPosition(), ModSounds.POOF, SoundSource.NEUTRAL, 1f, 1f);
        level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.position().x, this.position().y + (ModEntities.ILLUSION.getHeight() / 2), this.position().z, 32, 0.2, (ModEntities.ILLUSION.getHeight() / 2), 0.2, 0.01);
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith(@Nullable Entity other) {
        return false;
    }

    @Override
    protected void pushEntities() {
        ;
    }

    @Override
    public void push(Entity entity) {
        ;
    }

    public void copyEquipmentFrom(LivingEntity source) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.isArmor() || slot.getType() == EquipmentSlot.Type.HAND) {
                this.setItemSlot(slot, source.getItemBySlot(slot).copy());
            }
        }
    }
}
