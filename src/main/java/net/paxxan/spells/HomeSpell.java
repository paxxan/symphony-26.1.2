package net.paxxan.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.TeleportTransition;
import net.paxxan.Symphony;

public class HomeSpell extends BaseFluteSpell {

    public HomeSpell() {
        super();
        this.name = "Spell of Homewards";
        this.id = "home";
        this.description = "Returns the user to their spawn point.";
        this.sequence = "LLLRLLRLRDR";
        this.book_texture = Symphony.id("textures/item/spellbook/2d/" + id + ".png");
        this.post_spell_cooldown = (int) (20 * 60);
        this.charge = 4;
    }

    @Override
    public void cast(Level level, Player player) {
        super.cast(level, player);
        BlockPos spawn_point = player.blockPosition();
        if (level instanceof ServerLevel serverLevel) {
            spawn_point = serverLevel.getRespawnData().pos();
            if (player instanceof ServerPlayer serverPlayer) {
                if (serverPlayer.getRespawnConfig() != null) {
                    spawn_point = serverPlayer.getRespawnConfig().respawnData().pos();
                }
            }
        }
        player.teleportTo(spawn_point.getX(), spawn_point.getY(), spawn_point.getZ());
        level.playSound(null, player.blockPosition(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.PLAYERS);
    }
}
