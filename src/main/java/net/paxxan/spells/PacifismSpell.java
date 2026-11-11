package net.paxxan.spells;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.PoisonMobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.skeleton.Bogged;
import net.minecraft.world.entity.monster.zombie.Husk;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.paxxan.Symphony;
import net.paxxan.effect.ModEffects;

import java.util.List;

public class PacifismSpell extends BaseFluteSpell {
    int EFFECT_DUR_TICKS = 100;
    int RADIUS = 5;

    public PacifismSpell() {
        super();
        this.name = "Pacifism";
        this.id = "pacifism";
        this.description = "Makes entities in a " + RADIUS + " block radius peaceful for " + EFFECT_DUR_TICKS + " ticks.";
        this.sequence = "DDDLRLRLU";
        this.book_texture = Symphony.id("textures/item/spellbook/2d/" + id + ".png");
        this.post_spell_cooldown = (int) (20 * 15);
    }

    @Override
    public void cast(Level level, Player player) {
        super.cast(level, player);
        AABB box = new AABB(player.getX() - RADIUS, player.getY() - RADIUS, player.getZ() - RADIUS, player.getX() + RADIUS, player.getY() + RADIUS, player.getZ() + RADIUS);
        List<Entity> entities = level.getEntities(player, box);
        for (Entity entity : entities) {
            if (entity instanceof PathfinderMob pathfinder) {
                pathfinder.forceAddEffect(new MobEffectInstance(ModEffects.PACIFISM_EFFECT, EFFECT_DUR_TICKS, 1), player);
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, pathfinder.getX(), pathfinder.getY() + (pathfinder.getEyeHeight() / 2), pathfinder.getZ(), 16, pathfinder.getBbWidth() * 0.4, pathfinder.getEyeHeight() * 0.4, pathfinder.getBbWidth() * 0.4, 0.05);
                }
            }
        }
        level.playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS);
    }
}
