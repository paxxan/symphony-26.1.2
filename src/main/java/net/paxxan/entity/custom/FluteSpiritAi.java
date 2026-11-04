package net.paxxan.entity.custom;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FluteSpiritAi {
    private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F;
    private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.5F;
    private static final int CLOSE_ENOUGH_TO_TARGET = 4;
    private static final int TOO_FAR_FROM_TARGET = 16;
    private static final int MAX_LOOK_DISTANCE = 6;
    private static final int MIN_WAIT_DURATION = 30;
    private static final int MAX_WAIT_DURATION = 60;
    private static final int GIVE_ITEM_TIMEOUT_DURATION = 20;

    protected static List<ActivityData<FluteSpirit>> getActivities() {
        return List.of(initCoreActivity(), initIdleActivity());
    }

    private static ActivityData<FluteSpirit> initCoreActivity() {
        return ActivityData.create(
                Activity.CORE,
                0,
                ImmutableList.<BehaviorControl<? super FluteSpirit>>of(
                        new Swim<>(0.8F),
                        new AnimalPanic(2.5F),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink()
                )
        );
    }
    private static ActivityData<FluteSpirit> initIdleActivity() {
        return ActivityData.create(
                Activity.IDLE,
                0,
                ImmutableList.<BehaviorControl<? super FluteSpirit>>of(
                        SetEntityLookTargetSometimes.create(6.0F, UniformInt.of(30, 60)),
                        new RunOne(ImmutableList.of(Pair.of(RandomStroll.fly(1.0F), 2), Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 2), Pair.of(new DoNothing(30, 60), 1)))
                )
        );
    }

    public static void updateActivity(final FluteSpirit body) {
        body.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
    }

    private static Optional<PositionTracker> getLikedPlayerPositionTracker(final LivingEntity spirit) {
        return getLikedPlayer(spirit).map(serverPlayer -> new EntityTracker(serverPlayer, true));
    }

    public static Optional<ServerPlayer> getLikedPlayer(final LivingEntity spirit) {
        Level level = spirit.level();
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            Optional<UUID> likedPlayer = spirit.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
            if (likedPlayer.isPresent()) {
                if (serverLevel.getEntity(likedPlayer.get()) instanceof ServerPlayer serverPlayer
                        && (serverPlayer.gameMode.isSurvival() || serverPlayer.gameMode.isCreative())
                        && serverPlayer.closerThan(spirit, 64.0)) {
                    return Optional.of(serverPlayer);
                }

                return Optional.empty();
            }
        }

        return Optional.empty();
    }
}
