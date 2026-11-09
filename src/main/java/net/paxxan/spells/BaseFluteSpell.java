package net.paxxan.spells;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.paxxan.Symphony;
import net.paxxan.item.ModComponents;

public class BaseFluteSpell {
    String name;
    String description;
    String sequence = "";
    String id = "";
    Identifier book_texture = Symphony.id("textures/item/spellbook/2d/home.png");
    int post_spell_cooldown;


    public BaseFluteSpell() {
        this.name = "Default Spell";
        this.description = "This spell does nothing. Why are you here?";

    }

    public void cast(Level level, Player player) {
        Symphony.LOGGER.info("Spell {} has unoverrided cast() function.", this.name);
    }

    public void preCast(Level level, Player player) {
        ;
    }

    public String getSequence() {
        return sequence;
    }

    public String getName() {
        return name;
    }

    public Identifier getBookTexture() {
        return book_texture;
    }

    public int getCooldown() {
        return post_spell_cooldown;
    }

    public String getID() {
        return id;
    }

    public Component getTranslatableName() {
        return Component.translatable("symphony.spells." + id);
    }
}
