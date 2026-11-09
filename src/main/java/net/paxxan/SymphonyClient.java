package net.paxxan;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientMannequin;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.client.model.animal.allay.AllayModel;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.particle.SoulParticle;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.paxxan.effect.ModEffects;
import net.paxxan.entity.ModEntities;
import net.paxxan.entity.client.*;
import net.paxxan.entity.custom.IllusionEntity;
import net.paxxan.item.ModComponents;
import net.paxxan.item.ModItems;
import net.paxxan.item.custom.FluteItem;
import net.paxxan.keymapping.ModKeyMappings;
import net.paxxan.networking.packet.FlutePlayPayloadC2S;
import net.paxxan.particle.EmissiveParticle;
import net.paxxan.particle.ModParticles;
import net.paxxan.particle.SeeThroughParticle;
import net.paxxan.spells.BaseFluteSpell;
import net.paxxan.spells.FluteSpells;
import net.paxxan.tags.ModTags;
import org.w3c.dom.Text;

import java.util.function.Predicate;

import static net.fabricmc.fabric.impl.client.renderer.RendererManager.getRenderer;

public class SymphonyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModKeyMappings.register();
        ModelLayerRegistry.registerModelLayer(ModModelLayerLocations.FLUTE_SPIRIT, FluteSpiritModel::createBodyLayer);

        EntityRenderers.register(ModEntities.FLUTE_SPIRIT, FluteSpiritRenderer::new);
        EntityRenderers.register(ModEntities.ILLUSION, IllusionRenderer::new);
        ClientTickEvents.END_CLIENT_TICK.register(SymphonyClient::onEndTick);

        HudElementRegistry.attachElementBefore(VanillaHudElements.CROSSHAIR, Symphony.id("key_panel"), FlutePlayGUIClientHandler::extract);
        ParticleProviderRegistry.getInstance().register(ModParticles.SPELUNKER_PARTICLE, SeeThroughParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(ModParticles.FLUTE_SPIRIT_PARTICLE, EmissiveParticle.Provider::new);

    }

    public static final Style GALACTIC = Style.EMPTY
            .withFont(new FontDescription.Resource(Identifier.withDefaultNamespace("alt")));

    private static void renderSpelunkerParticles(Minecraft client) {
        if (!client.isPaused()) {
            Level level = client.level;

            int radius = 10;
            Predicate<? super TagKey<Block>> isValuable = tagKey -> tagKey.equals(ModTags.Blocks.VALUABLE_BLOCKS);
            Iterable<BlockPos> affected_blocks = BlockPos.withinManhattan(client.player.blockPosition(), radius, radius, radius);
            for (BlockPos blockPos : affected_blocks) {
                BlockState state = level.getBlockState(blockPos);
                Block block = state.getBlock();
                Direction face = Direction.getRandom(level.getRandom());
                double x = blockPos.getX() + 0.5 + face.getStepX() * 0.5
                        + (face.getStepX() == 0 ? level.getRandom().nextDouble() - 0.5 : 0);
                double y = blockPos.getY() + 0.5 + face.getStepY() * 0.5
                        + (face.getStepY() == 0 ? level.getRandom().nextDouble() - 0.5 : 0);
                double z = blockPos.getZ() + 0.5 + face.getStepZ() * 0.5
                        + (face.getStepZ() == 0 ? level.getRandom().nextDouble() - 0.5 : 0);
                if (state.tags().anyMatch(isValuable)) {
                    if (level.getRandom().nextFloat() <= 0.2f) {
                        level.addParticle(ModParticles.SPELUNKER_PARTICLE, x, y, z, 0, 0, 0);
                    }
                }
            }
        }
    }

    public static void onEndTick(Minecraft client) {
        if (client.player != null) {
            if (client.player.hasEffect(ModEffects.SPELUNKER)) {
                renderSpelunkerParticles(client);
            }
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
