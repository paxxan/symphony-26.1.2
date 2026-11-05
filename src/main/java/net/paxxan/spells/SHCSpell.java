package net.paxxan.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.paxxan.Symphony;

public class SHCSpell extends BaseFluteSpell {

    public SHCSpell() {
        super();
        this.name = "Spontaneous Human Combustion";
        this.id = "shc";
        this.description = "Sets the user on fire?";
        this.sequence = "RLULD";
        this.book_texture = Symphony.id("textures/item/spellbook/2d/" + id + ".png");
    }

    @Override
    public void cast(Level level, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.setRemainingFireTicks(60);
        }
    }
}
