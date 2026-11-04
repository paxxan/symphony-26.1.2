package net.paxxan;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.animal.allay.AllayModel;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.particle.SoulParticle;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.paxxan.entity.ModEntities;
import net.paxxan.entity.client.FluteSpiritModel;
import net.paxxan.entity.client.FluteSpiritRenderer;
import net.paxxan.entity.client.ModModelLayerLocations;
import net.paxxan.item.ModComponents;
import net.paxxan.item.ModItems;
import net.paxxan.item.custom.FluteItem;
import net.paxxan.keymapping.ModKeyMappings;
import net.paxxan.networking.packet.FlutePlayPayloadC2S;
import net.paxxan.particle.ModParticles;

public class SymphonyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModKeyMappings.register();
        ModelLayerRegistry.registerModelLayer(ModModelLayerLocations.FLUTE_SPIRIT, FluteSpiritModel::createBodyLayer);

        EntityRenderers.register(ModEntities.FLUTE_SPIRIT, FluteSpiritRenderer::new);
        ClientTickEvents.END_CLIENT_TICK.register(SymphonyClient::onEndTick);

    }

    public static void onEndTick(Minecraft client) {
        if (client.player != null) {
            ItemStack item = client.player.getMainHandItem();
            if (ModKeyMappings.FLUTE_UP_NOTE.consumeClick()) {
                if (item.is(ModItems.FLUTE)) {
                    if (item.get(ModComponents.FLUTE_PLAYING_COMPONENT) == Boolean.TRUE) {
                        ClientPlayNetworking.send(new FlutePlayPayloadC2S(0));
                    }
                }
            }
            if (ModKeyMappings.FLUTE_LEFT_NOTE.consumeClick()) {
                if (item.is(ModItems.FLUTE)) {
                    if (item.get(ModComponents.FLUTE_PLAYING_COMPONENT) == Boolean.TRUE) {
                        ClientPlayNetworking.send(new FlutePlayPayloadC2S(1));
                    }
                }
            }
            if (ModKeyMappings.FLUTE_RIGHT_NOTE.consumeClick()) {
                if (item.is(ModItems.FLUTE)) {
                    if (item.get(ModComponents.FLUTE_PLAYING_COMPONENT) == Boolean.TRUE) {
                        ClientPlayNetworking.send(new FlutePlayPayloadC2S(2));
                    }
                }
            }
            if (ModKeyMappings.FLUTE_DOWN_NOTE.consumeClick()) {
                if (item.is(ModItems.FLUTE)) {
                    if (item.get(ModComponents.FLUTE_PLAYING_COMPONENT) == Boolean.TRUE) {
                        ClientPlayNetworking.send(new FlutePlayPayloadC2S(3));
                    }
                }
            }
        }
    }
}
