package net.paxxan.spells;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.paxxan.Symphony;
import net.paxxan.effect.ModEffects;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class LeapSpell extends BaseFluteSpell {
    int LEAP_SPEED = 2;

    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(
            true, false, Optional.of(1.22F), BuiltInRegistries.BLOCK.get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())
    );

    public LeapSpell() {
        super();
        this.name = "Leap";
        this.id = "leap";
        this.description = "Propels the user in the direction they are looking.";
        this.sequence = "UUUURLRLU";
        this.book_texture = Symphony.id("textures/item/spellbook/2d/" + id + ".png");
        this.post_spell_cooldown = 50;
    }

    @Override
    public void cast(Level level, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            player.setDeltaMovement(new Vec3(player.getDeltaMovement().x + (player.getLookAngle().x * LEAP_SPEED), player.getLookAngle().y * LEAP_SPEED, player.getDeltaMovement().z + (player.getLookAngle().z * LEAP_SPEED)));
            level
                    .explode(
                            player,
                            null,
                            EXPLOSION_DAMAGE_CALCULATOR,
                            player.position().x(),
                            player.position().y(),
                            player.position().z(),
                            1.2F,
                            false,
                            Level.ExplosionInteraction.TRIGGER,
                            ParticleTypes.GUST_EMITTER_SMALL,
                            ParticleTypes.GUST_EMITTER_LARGE,
                            WeightedList.of(),
                            SoundEvents.WIND_CHARGE_BURST
                    );
            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
        }
    }
}
