package net.paxxan.networking.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.paxxan.Symphony;

public record FlutePlayPayloadC2S(int note) implements CustomPacketPayload {
    public static final Type<FlutePlayPayloadC2S> TYPE = new Type<>(
            Symphony.id("flute_play_payload"));

    public static final StreamCodec<RegistryFriendlyByteBuf, FlutePlayPayloadC2S> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            FlutePlayPayloadC2S::note,
            FlutePlayPayloadC2S::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
