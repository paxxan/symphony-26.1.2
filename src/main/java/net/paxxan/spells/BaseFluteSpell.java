package net.paxxan.spells;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.paxxan.Symphony;
import net.paxxan.attachments.ModDataAttachments;
import net.paxxan.effect.ModEffects;
import net.paxxan.item.ModComponents;

import java.util.List;
import java.util.Map;

public class BaseFluteSpell {
    String name;
    String description;
    String sequence = "";
    String id = "";
    Identifier book_texture = Symphony.id("textures/item/spellbook/2d/home.png");
    int post_spell_cooldown = 0;


    public BaseFluteSpell() {
        this.name = "Default Spell";
        this.description = "This spell does nothing. Why are you here?";

    }

    public void cast(Level level, Player player) {
        boolean full_lungs = false;
        MobEffectInstance effectInstance = null;
        int amplifier = 0;
        int multiplier = PlayerSpellManager.knowsSpell(player, Symphony.id(id)) ? 1 : 2;
        if (player instanceof ServerPlayer) {
            if (player.hasEffect(ModEffects.FULL_LUNGS)) {
                full_lungs = true;
                effectInstance = player.getEffect(ModEffects.FULL_LUNGS);
                assert effectInstance != null;
                amplifier = effectInstance.getAmplifier() + 1;
            }
            if (full_lungs) {
                PlayerSpellManager.setCooldown(player, Symphony.id(id), (int) (post_spell_cooldown / (amplifier * 1.5)) * multiplier);
            } else {
                PlayerSpellManager.setCooldown(player, Symphony.id(id), post_spell_cooldown * multiplier);
            }

        }
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
        return Component.translatable("spell.symphony." + id);
    }
}
