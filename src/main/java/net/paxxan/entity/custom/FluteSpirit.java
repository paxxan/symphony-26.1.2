package net.paxxan.entity.custom;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

public class FluteSpirit extends PathfinderMob implements InventoryCarrier, VibrationSystem {
    private static final Vec3i ITEM_PICKUP_REACH = new Vec3i(1, 1, 1);
    private static final int LIFTING_ITEM_ANIMATION_DURATION = 5;
    private static final float DANCING_LOOP_DURATION = 55.0F;
    private static final float SPINNING_ANIMATION_DURATION = 15.0F;
    public static final int MAX_NOTEBLOCK_DISTANCE = 1024;
    private static final EntityDataAccessor<Boolean> DATA_DANCING = SynchedEntityData.defineId(FluteSpirit.class, EntityDataSerializers.BOOLEAN);
    private static final Brain.Provider<FluteSpirit> BRAIN_PROVIDER = Brain.provider(
            List.of(MemoryModuleType.LIKED_PLAYER),
            List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.NEAREST_ITEMS),
            var0 -> FluteSpiritAi.getActivities()
    );
    public static final ImmutableList<Float> THROW_SOUND_PITCHES = ImmutableList.of(
            0.5625F, 0.625F, 0.75F, 0.9375F, 1.0F, 1.0F, 1.125F, 1.25F, 1.5F, 1.875F, 2.0F, 2.25F, 2.5F, 3.0F, 3.75F, 4.0F
    );
    private final DynamicGameEventListener<VibrationSystem.Listener> dynamicVibrationListener;
    private VibrationSystem.Data vibrationData;
    private final VibrationSystem.User vibrationUser;
    private final SimpleContainer inventory = new SimpleContainer(1);
    private float holdingItemAnimationTicks;
    private float holdingItemAnimationTicks0;
    private float dancingAnimationTicks;
    private float spinningAnimationTicks;
    private float spinningAnimationTicks0;

    public FluteSpirit(final EntityType<? extends FluteSpirit> type, final Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setCanPickUpLoot(this.canPickUpLoot());
        this.vibrationUser = new FluteSpirit.VibrationUser();
        this.vibrationData = new VibrationSystem.Data();
        this.dynamicVibrationListener = new DynamicGameEventListener<>(new VibrationSystem.Listener(this));
    }

    @Override
    protected Brain<FluteSpirit> makeBrain(final Brain.Packed packedBrain) {
        return BRAIN_PROVIDER.makeBrain(this, packedBrain);
    }

    @Override
    public Brain<FluteSpirit> getBrain() {
        return (Brain<FluteSpirit>) super.getBrain();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.FLYING_SPEED, 0.1F)
                .add(Attributes.MOVEMENT_SPEED, 0.1F)
                .add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected PathNavigation createNavigation(final Level level) {
        FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, level);
        flyingPathNavigation.setCanOpenDoors(false);
        flyingPathNavigation.setCanFloat(true);
        flyingPathNavigation.setRequiredPathLength(48.0F);
        return flyingPathNavigation;
    }

    @Override
    protected void defineSynchedData(final SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_DANCING, false);
    }

    @Override
    public void travel(final Vec3 input) {
        this.travelFlying(input, this.getSpeed());
    }

    @Override
    public boolean hurtServer(final ServerLevel level, final DamageSource source, final float damage) {
        return super.hurtServer(level, source, damage);
    }

    @Override
    protected boolean considersEntityAsAlly(final Entity other) {
        return this.isLikedPlayer(other) || super.considersEntityAsAlly(other);
    }

    private boolean isLikedPlayer(final @Nullable Entity other) {
        if (!(other instanceof Player player)) {
            return false;
        } else {
            Optional<UUID> likedPlayer = this.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
            return likedPlayer.isPresent() && player.getUUID().equals(likedPlayer.get());
        }
    }

    @Override
    protected void playStepSound(final BlockPos pos, final BlockState blockState) {
    }

    @Override
    protected void checkFallDamage(final double ya, final boolean onGround, final BlockState onState, final BlockPos pos) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.hasItemInSlot(EquipmentSlot.MAINHAND) ? SoundEvents.ALLAY_AMBIENT_WITH_ITEM : SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM;
    }

    @Override
    protected SoundEvent getHurtSound(final DamageSource source) {
        return SoundEvents.ALLAY_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ALLAY_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected void customServerAiStep(final ServerLevel level) {
        ProfilerFiller profiler = Profiler.get();
        profiler.push("fluteSpiritBrain");
        this.getBrain().tick(level, this);
        profiler.pop();
        profiler.push("fluteSpiritActivityUpdate");
        FluteSpiritAi.updateActivity(this);
        profiler.pop();
        super.customServerAiStep(level);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide() && this.isAlive() && this.tickCount % 10 == 0) {
            this.heal(1.0F);
        }

        if (this.isDancing() && this.tickCount % 20 == 0) {
            this.setDancing(false);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            if(this.level().getGameTime() >= 13000 && this.level().getGameTime() <= 23000) {
                if (this.tickCount % 4 == 0) {
                    for (int i = 0; i < 2; i++) {
                        this.level()
                                .addParticle(
                                        ParticleTypes.FIREFLY,
                                        this.getRandomX(0.5),
                                        this.getRandomY() + 0.25,
                                        this.getRandomZ(0.5),
                                        (this.random.nextDouble() - 0.5) * 2.0,
                                        -this.random.nextDouble(),
                                        (this.random.nextDouble() - 0.5) * 2.0
                                );
                    }
                }
            }
            this.holdingItemAnimationTicks0 = this.holdingItemAnimationTicks;
            if (this.hasItemInHand()) {
                this.holdingItemAnimationTicks = Mth.clamp(this.holdingItemAnimationTicks + 1.0F, 0.0F, 5.0F);
            } else {
                this.holdingItemAnimationTicks = Mth.clamp(this.holdingItemAnimationTicks - 1.0F, 0.0F, 5.0F);
            }

            if (this.isDancing()) {
                this.dancingAnimationTicks++;
                this.spinningAnimationTicks0 = this.spinningAnimationTicks;
                if (this.isSpinning()) {
                    this.spinningAnimationTicks++;
                } else {
                    this.spinningAnimationTicks--;
                }

                this.spinningAnimationTicks = Mth.clamp(this.spinningAnimationTicks, 0.0F, 15.0F);
            } else {
                this.dancingAnimationTicks = 0.0F;
                this.spinningAnimationTicks = 0.0F;
                this.spinningAnimationTicks0 = 0.0F;
            }
        } else {
            VibrationSystem.Ticker.tick(this.level(), this.vibrationData, this.vibrationUser);
            if (this.isPanicking()) {
                this.setDancing(false);
            }
        }
    }

    @Override
    public boolean canPickUpLoot() {
        return !this.isOnPickupCooldown() && this.hasItemInHand();
    }

    public boolean hasItemInHand() {
        return !this.getItemInHand(InteractionHand.MAIN_HAND).isEmpty();
    }

    @Override
    protected boolean canDispenserEquipIntoSlot(final EquipmentSlot slot) {
        return false;
    }

    private boolean isOnPickupCooldown() {
        return this.getBrain().checkMemory(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryStatus.VALUE_PRESENT);
    }

    @Override
    protected InteractionResult mobInteract(final Player player, final InteractionHand hand) {
        ItemStack interactionItem = player.getItemInHand(hand);
        ItemStack itemInHand = this.getItemInHand(InteractionHand.MAIN_HAND);

        if (itemInHand.isEmpty() && !interactionItem.isEmpty()) {
            ItemStack itemToGive = interactionItem.copyWithCount(1);
            this.setItemInHand(InteractionHand.MAIN_HAND, itemToGive);
            this.removeInteractionItem(player, interactionItem);
            this.level().playSound(player, this, SoundEvents.ALLAY_ITEM_GIVEN, SoundSource.NEUTRAL, 2.0F, 1.0F);
            this.getBrain().setMemory(MemoryModuleType.LIKED_PLAYER, player.getUUID());
            return InteractionResult.SUCCESS;
        }

        if (!itemInHand.isEmpty() && hand == InteractionHand.MAIN_HAND && interactionItem.isEmpty()) {
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            this.level().playSound(player, this, SoundEvents.ALLAY_ITEM_TAKEN, SoundSource.NEUTRAL, 2.0F, 1.0F);
            this.swing(InteractionHand.MAIN_HAND);

            for (ItemStack itemStack : this.getInventory().removeAllItems()) {
                BehaviorUtils.throwItem(this, itemStack, this.position());
            }

            this.getBrain().eraseMemory(MemoryModuleType.LIKED_PLAYER);
            player.addItem(itemInHand);
            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    protected Vec3i getPickupReach() {
        return ITEM_PICKUP_REACH;
    }

    @Override
    public boolean isFlapping() {
        return !this.onGround();
    }

    @Override
    public void updateDynamicGameEventListener(final BiConsumer<DynamicGameEventListener<?>, ServerLevel> action) {
        if (this.level() instanceof ServerLevel serverLevel) {
            action.accept(this.dynamicVibrationListener, serverLevel);
        }
    }

    public boolean isDancing() {
        return this.entityData.get(DATA_DANCING);
    }

    public void setDancing(final boolean isDancing) {
        if (!this.level().isClientSide() && this.isEffectiveAi() && (!isDancing || !this.isPanicking())) {
            this.entityData.set(DATA_DANCING, isDancing);
        }
    }

    public float getHoldingItemAnimationProgress(final float a) {
        return Mth.lerp(a, this.holdingItemAnimationTicks0, this.holdingItemAnimationTicks) / 5.0F;
    }

    public boolean isSpinning() {
        float spinningProgress = this.dancingAnimationTicks % 55.0F;
        return spinningProgress < 15.0F;
    }

    public float getSpinningProgress(final float a) {
        return Mth.lerp(a, this.spinningAnimationTicks0, this.spinningAnimationTicks) / 15.0F;
    }

    @Override
    protected void dropEquipment(final ServerLevel level) {
        super.dropEquipment(level);
        this.inventory.removeAllItems().forEach(stack -> this.spawnAtLocation(level, stack));
        ItemStack itemStack = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!itemStack.isEmpty() && !EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
            this.spawnAtLocation(level, itemStack);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean removeWhenFarAway(final double distSqr) {
        return false;
    }

    @Override
    protected void addAdditionalSaveData(final ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.writeInventoryToTag(output);
        output.store("listener", VibrationSystem.Data.CODEC, this.vibrationData);
    }

    @Override
    protected void readAdditionalSaveData(final ValueInput input) {
        super.readAdditionalSaveData(input);
        this.readInventoryFromTag(input);
        this.vibrationData = input.read("listener", VibrationSystem.Data.CODEC).orElseGet(VibrationSystem.Data::new);
    }

    @Override
    protected boolean shouldStayCloseToLeashHolder() {
        return false;
    }

    private void removeInteractionItem(final Player player, final ItemStack interactionItem) {
        interactionItem.consume(1, player);
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, this.getEyeHeight() * 0.6, this.getBbWidth() * 0.1);
    }

    @Override
    public void handleEntityEvent(final byte id) {
        if (id == 18) {
            for (int i = 0; i < 3; i++) {
                this.spawnHeartParticle();
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    private void spawnHeartParticle() {
        double xd = this.random.nextGaussian() * 0.02;
        double yd = this.random.nextGaussian() * 0.02;
        double zd = this.random.nextGaussian() * 0.02;
        this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), xd, yd, zd);
    }

    @Override
    public VibrationSystem.Data getVibrationData() {
        return this.vibrationData;
    }

    @Override
    public VibrationSystem.User getVibrationUser() {
        return this.vibrationUser;
    }

    private class VibrationUser implements VibrationSystem.User {
        private static final int VIBRATION_EVENT_LISTENER_RANGE = 16;
        private final PositionSource positionSource = new EntityPositionSource(FluteSpirit.this, FluteSpirit.this.getEyeHeight());

        @Override
        public int getListenerRadius() {
            return 16;
        }

        @Override
        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public boolean canReceiveVibration(final ServerLevel level, final BlockPos pos, final Holder<GameEvent> event, final GameEvent.Context context) {
            if (FluteSpirit.this.isNoAi()) {
                return false;
            }
            Optional<GlobalPos> maybeGlobalPos = FluteSpirit.this.getBrain().getMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION);
            if (maybeGlobalPos.isEmpty()) {
                return true;
            }

            GlobalPos globalPos = maybeGlobalPos.get();
            return globalPos.isCloseEnough(level.dimension(), FluteSpirit.this.blockPosition(), 1024) && globalPos.pos().equals(pos);
        }

        @Override
        public void onReceiveVibration(
                final ServerLevel level,
                final BlockPos pos,
                final Holder<GameEvent> event,
                final @Nullable Entity sourceEntity,
                final @Nullable Entity projectileOwner,
                final float receivingDistance
        ) {
        }

        @Override
        public TagKey<GameEvent> getListenableEvents() {
            return GameEventTags.ALLAY_CAN_LISTEN;
        }
    }
}
