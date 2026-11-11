package net.paxxan.spells;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.paxxan.Symphony;
import net.paxxan.entity.custom.IllusionEntity;

public class IllusionSpell extends BaseFluteSpell {

    public IllusionSpell() {
        super();
        this.name = "Illusion";
        this.id = "illusion";
        this.description = "Creates a decoy of the self.";
        this.sequence = "RDLD";
        this.book_texture = Symphony.id("textures/item/spellbook/2d/" + id + ".png");
        this.post_spell_cooldown = (int) (20 * 10);
    }

    @Override
    public void cast(Level level, Player player) {
        super.cast(level, player);
        if (level instanceof ServerLevel serverLevel) {
            IllusionEntity.spawn(serverLevel, player);
        }
    }
}
