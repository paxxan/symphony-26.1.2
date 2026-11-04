package net.paxxan.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.paxxan.networking.packet.FlutePlayPayloadC2S;

public class ModPackets {
    private static void registerClientbound(PayloadTypeRegistry<RegistryFriendlyByteBuf> registry) {
        // Server -> Client
    }

    private static void registerServerbound(PayloadTypeRegistry<RegistryFriendlyByteBuf> registry) {
        // Client -> Server
        registry.register(FlutePlayPayloadC2S.TYPE, FlutePlayPayloadC2S.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(FlutePlayPayloadC2S.TYPE, ServerboundPackets::handleFlutePayload);
    }

    public static void registerPackets() {
        registerClientbound(PayloadTypeRegistry.clientboundPlay());
        registerServerbound(PayloadTypeRegistry.serverboundPlay());
    }
}
