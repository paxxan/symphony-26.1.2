package net.paxxan.attachments;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.paxxan.Symphony;

import java.util.*;
import java.util.function.Function;

public class ModDataAttachments {
    private static final StreamCodec<ByteBuf, Set<Identifier>> SPELLS_STREAM_CODEC =
            ByteBufCodecs.<ByteBuf, Identifier, HashSet<Identifier>>collection(HashSet::new, Identifier.STREAM_CODEC)
                    .map(Set::copyOf, HashSet::new);

    private static final StreamCodec<ByteBuf, Map<Identifier, Integer>> COOLDOWNS_STREAM_CODEC =
            ByteBufCodecs.map(HashMap::new, Identifier.STREAM_CODEC, ByteBufCodecs.INT);


    public static final AttachmentType<Set<Identifier>> LEARNED_SPELLS = AttachmentRegistry.create(
            Symphony.id("learned_spells_attachment"),
            builder -> builder
                    .initializer(Set::of)
                    .persistent(Identifier.CODEC.listOf().xmap(Set::copyOf, List::copyOf))
                    .copyOnDeath()
                    .syncWith(
                            SPELLS_STREAM_CODEC,
                            AttachmentSyncPredicate.targetOnly()
                    )
    );

    public static final AttachmentType<Map<Identifier, Integer>> SPELL_COOLDOWNS = AttachmentRegistry.create(
            Symphony.id("spell_cooldowns_attachment"),
            builder -> builder
                    .initializer(HashMap::new)
                    .persistent(Codec.unboundedMap(Identifier.CODEC, Codec.INT)
                            .<Map<Identifier, Integer>>xmap(HashMap::new, m-> m))
                    .syncWith(
                            COOLDOWNS_STREAM_CODEC,
                            AttachmentSyncPredicate.targetOnly()
                    )
    );

    public static void registerAttachments() {
        Symphony.LOGGER.info("Registering Data Attachments for " + Symphony.MOD_ID);
    }
}
