package net.paxxan.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.paxxan.Symphony;

import java.util.List;

public class HealPrayerSpell extends BaseFluteSpell {
    int HEAL_AMOUNT = 5;
    int RADIUS = 10;

    public HealPrayerSpell() {
        super();
        this.name = "Heal Prayer";
        this.id = "heal_prayer";
        this.description = "Heals entities in a " + RADIUS + " block radius approximately " + HEAL_AMOUNT + " health.";
        this.sequence = "LRLRD";
        this.book_texture = Symphony.id("textures/item/spellbook/2d/" + id + ".png");
    }

    @Override
    public void cast(Level level, Player player) {
        AABB box = new AABB(player.getX() - (RADIUS / 2), player.getY() - (RADIUS / 2), player.getZ() - (RADIUS / 2), player.getX() + (RADIUS / 2), player.getY() + (RADIUS / 2), player.getZ() + (RADIUS / 2));
        List<Entity> entities = level.getEntities(player, box);
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.heal(HEAL_AMOUNT / (entity.distanceTo(player) / 10));
                Symphony.LOGGER.info(entity.distanceTo(player) + " heals the entity " + HEAL_AMOUNT / entity.distanceTo(player));
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.HEART, livingEntity.getX(), livingEntity.getY() + (livingEntity.getEyeHeight() / 2), livingEntity.getZ(), 16, livingEntity.getBbWidth() * 0.4, livingEntity.getEyeHeight() * 0.4, livingEntity.getBbWidth() * 0.4, 0.05);
                }
            }
        }
        level.playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS);
    }
}
