package net.paxxan.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.paxxan.item.ModComponents;
import net.paxxan.item.custom.FluteItem;
import net.paxxan.networking.packet.FlutePlayPayloadC2S;
import net.paxxan.networking.packet.FluteServerInfoPayloadS2C;
import net.paxxan.networking.packet.SpellbookLearnDisplayPayloadS2C;
import net.paxxan.sound.ModSounds;


public class ClientboundPackets {
    public static String current_sequence = "";

    public static void handleFluteInfoPayload(FluteServerInfoPayloadS2C fluteServerInfoPayloadS2C, ClientPlayNetworking.Context context) {
        current_sequence = fluteServerInfoPayloadS2C.sequence();
    }

    public static void handleSpellbookLearnDisplayPayload(SpellbookLearnDisplayPayloadS2C spellbookLearnDisplayPayloadS2C, ClientPlayNetworking.Context context) {
        Minecraft client = Minecraft.getInstance();
        client.player.playSound(ModSounds.LEARN_SPELL);
        client.gameRenderer.displayItemActivation(spellbookLearnDisplayPayloadS2C.stack());
        client.gui.setTitle(Component.literal("Spell Learned!"));
        client.gui.setSubtitle(Component.translatable("spell.symphony." + spellbookLearnDisplayPayloadS2C.stack().get(ModComponents.SPELL_ID)));

    }
}
