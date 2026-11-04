package net.paxxan.spells;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.phys.AABB;
import net.paxxan.Symphony;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class WarHornSpell extends BaseFluteSpell {
    static int BASE_KNOCKBACK_AMOUNT = 5;
    int RADIUS = 3;

    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(
            false, true, Optional.of((float) BASE_KNOCKBACK_AMOUNT), BuiltInRegistries.BLOCK.get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())
    );

    public WarHornSpell() {
        super();
        this.name = "War Horn";
        this.description = "Knocks back enemies in a " + RADIUS + " block radius.";
        this.sequence = "UULRLU";
    }

    @Override
    public void cast(Level level, Player player) {
        level.explode(
                player,
                null,
                EXPLOSION_DAMAGE_CALCULATOR,
                player.getX(),
                player.getY(),
                player.getZ(),
                (float) RADIUS / 2,
                false,
                Level.ExplosionInteraction.TRIGGER,
                ParticleTypes.GUST_EMITTER_SMALL,
                ParticleTypes.GUST_EMITTER_LARGE,
                WeightedList.of(),
                SoundEvents.RAID_HORN
        );
    }
}
