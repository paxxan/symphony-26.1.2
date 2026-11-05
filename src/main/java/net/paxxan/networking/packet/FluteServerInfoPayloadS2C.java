package net.paxxan.networking.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.paxxan.Symphony;

public record FluteServerInfoPayloadS2C(String sequence) implements CustomPacketPayload {
    public static final Type<FluteServerInfoPayloadS2C> TYPE = new Type<>(
            Symphony.id("flute_server_info_payload"));

    public static final StreamCodec<RegistryFriendlyByteBuf, FluteServerInfoPayloadS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            FluteServerInfoPayloadS2C::sequence,
            FluteServerInfoPayloadS2C::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
