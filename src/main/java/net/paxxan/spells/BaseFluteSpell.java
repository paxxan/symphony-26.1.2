package net.paxxan.spells;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.paxxan.Symphony;

public class BaseFluteSpell {
    String name;
    String description;
    String sequence = "";

    public BaseFluteSpell() {
        this.name = "Default Spell";
        this.description = "This spell does nothing. Why are you here?";
    }

    public void cast(Level level, Player player) {
        Symphony.LOGGER.info("Spell {} has unoverrided cast() function.", this.name);
    }

    public String getSequence() {
        return sequence;
    }
}
