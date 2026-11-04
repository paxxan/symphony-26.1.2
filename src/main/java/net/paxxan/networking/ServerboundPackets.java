package net.paxxan.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.paxxan.item.ModComponents;
import net.paxxan.item.custom.FluteItem;
import net.paxxan.networking.packet.FlutePlayPayloadC2S;

public class ServerboundPackets {
    public static void handleFlutePayload(FlutePlayPayloadC2S flutePlayPayloadC2S, ServerPlayNetworking.Context context) {
        net.minecraft.server.level.ServerLevel level = context.player().level();
        Player player = context.player();
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.NOTE_BLOCK_FLUTE, SoundSource.PLAYERS, 1f, FluteItem.NOTES[flutePlayPayloadC2S.note()]);
        level.sendParticles(ParticleTypes.NOTE, player.getX(), player.getY() + 2, player.getZ(), 0, FluteItem.NOTES[flutePlayPayloadC2S.note()], 0.0, 0.0, 1.0);
        ItemStack stack = player.getMainHandItem();
        if (Boolean.TRUE.equals(stack.get(ModComponents.FLUTE_PLAYING_COMPONENT))) {
            String noteChar = getNoteCharacterFromId(flutePlayPayloadC2S.note());
            String currentSeq = stack.getOrDefault(ModComponents.FLUTE_SEQUENCE_COMPONENT, "");
            String updatedSeq = currentSeq + noteChar;
            stack.set(ModComponents.FLUTE_SEQUENCE_COMPONENT, updatedSeq);
        }
    }

    private static String getNoteCharacterFromId(int noteId) {
        if (noteId == 0) return "U";
        if (noteId == 1) return "L";
        if (noteId == 2) return "R";
        if (noteId == 3) return "D";
        return "";
    }
}
