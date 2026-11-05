package net.paxxan.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.paxxan.Symphony;

public class CropGrowthSpell extends BaseFluteSpell {
    int RADIUS = 5;

    public CropGrowthSpell() {
        super();
        this.name = "Spell of Growth";
        this.id = "crop_growth";
        this.description = "Instantaneously grows crops in a " + RADIUS + "x" + RADIUS + " radius.";
        this.sequence = "UUD";
        this.book_texture = Symphony.id("textures/item/spellbook/2d/" + id + ".png");
        this.post_spell_cooldown = 60;
    }

    @Override
    public void cast(Level level, Player player) {
        Iterable<BlockPos> affected_blocks = BlockPos.withinManhattan(player.blockPosition(), RADIUS / 2, RADIUS / 2, RADIUS / 2);
        for (BlockPos blockPos : affected_blocks) {
            BlockState state = level.getBlockState(blockPos);
            Block block = state.getBlock();
            if (block instanceof BonemealableBlock && ((BonemealableBlock) block).isValidBonemealTarget(level, blockPos, state)) {
                if (level instanceof ServerLevel serverLevel) {
                    ((BonemealableBlock) block).performBonemeal(serverLevel, level.getRandom(), blockPos, state);
                    serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                            blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5,
                            16, 0.25, 0.25, 0.25, 0.05);
                }
            }
        }
        level.playSound(null, player.blockPosition(), SoundEvents.BONE_MEAL_USE, SoundSource.PLAYERS);
    }
}
