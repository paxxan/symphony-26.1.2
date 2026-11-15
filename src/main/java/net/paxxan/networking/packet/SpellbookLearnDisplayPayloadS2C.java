package net.paxxan.networking.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.paxxan.Symphony;

public record SpellbookLearnDisplayPayloadS2C(ItemStack stack) implements CustomPacketPayload {
    public static final Type<SpellbookLearnDisplayPayloadS2C> TYPE = new Type<>(
            Symphony.id("spellbook_learn_display_payload"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpellbookLearnDisplayPayloadS2C> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            SpellbookLearnDisplayPayloadS2C::stack,
            SpellbookLearnDisplayPayloadS2C::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
