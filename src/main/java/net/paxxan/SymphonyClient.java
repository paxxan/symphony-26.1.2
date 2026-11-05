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
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.client.model.animal.allay.AllayModel;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.particle.SoulParticle;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.paxxan.entity.ModEntities;
import net.paxxan.entity.client.FluteSpiritModel;
import net.paxxan.entity.client.FluteSpiritRenderState;
import net.paxxan.entity.client.FluteSpiritRenderer;
import net.paxxan.entity.client.ModModelLayerLocations;
import net.paxxan.item.ModComponents;
import net.paxxan.item.ModItems;
import net.paxxan.item.custom.FluteItem;
import net.paxxan.keymapping.ModKeyMappings;
import net.paxxan.networking.packet.FlutePlayPayloadC2S;
import net.paxxan.particle.ModParticles;
import net.paxxan.spells.BaseFluteSpell;
import net.paxxan.spells.FluteSpells;
import org.w3c.dom.Text;

public class SymphonyClient implements ClientModInitializer {
    private static boolean visible = false;
    private static float animationProgress = 0.0f;
    private static final float OPEN_SPEED = 0.10f;

    @Override
    public void onInitializeClient() {
        ModKeyMappings.register();
        ModelLayerRegistry.registerModelLayer(ModModelLayerLocations.FLUTE_SPIRIT, FluteSpiritModel::createBodyLayer);

        EntityRenderers.register(ModEntities.FLUTE_SPIRIT, FluteSpiritRenderer::new);
        ClientTickEvents.END_CLIENT_TICK.register(SymphonyClient::onEndTick);

        HudElementRegistry.attachElementBefore(VanillaHudElements.CROSSHAIR, Symphony.id("key_panel"), SymphonyClient::extract);

    }

    private static void extract(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
        Minecraft client = Minecraft.getInstance();

        Font font = client.font;
        if (client.player != null) {
            ItemStack item = client.player.getMainHandItem();
            if (item.is(ModItems.FLUTE)) {
                if (item.get(ModComponents.FLUTE_PLAYING_COMPONENT) == Boolean.TRUE) {
                    String rendered_sequence = item.getOrDefault(ModComponents.FLUTE_SEQUENCE_COMPONENT, "");
                    BaseFluteSpell spell = getSpellFromSequence(rendered_sequence);
                    int font_color = spell != null ? 0xFF00FF00 : 0xFFFFFFFF;
                    if (spell != null) {
                        graphics.text(font, spell.getName(), (graphics.guiWidth() / 2) - (font.width(spell.getName()) / 2), (graphics.guiHeight() / 2) + 24, 0xFFFFFFFF, true);
                        graphics.blit(RenderPipelines.GUI_TEXTURED, spell.getBookTexture(), (graphics.guiWidth() / 2) - 16, (graphics.guiHeight() / 2) - 16, 0, 0, 32, 32, 32, 32,32, 32);
                    }
                    rendered_sequence = convertSequenceToArrows(rendered_sequence);

                    graphics.text(font, rendered_sequence, (graphics.guiWidth() / 2) - (font.width(rendered_sequence) / 2), (graphics.guiHeight() / 2) - 32, font_color, true);
                    renderKeyPanels(graphics, tickCounter, client);
                }
            }
        }

    }

    private static void renderKeyPanels(GuiGraphicsExtractor graphics, DeltaTracker tickCounter, Minecraft client) {
        Font font = client.font;

        Identifier texture = Symphony.id("textures/gui/key_panel.png");
        Identifier h_texture = Symphony.id("textures/gui/key_panel_highlight.png");

        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, (graphics.guiWidth() / 2) - 18, (graphics.guiHeight() / 2) - 72, 0, 0, 36, 36, 36, 36,36, 36);
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, (graphics.guiWidth() / 2) - 18, (graphics.guiHeight() / 2) + 36, 0, 0, 36, 36, 36, 36, 36, 36);
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, (graphics.guiWidth() / 2) - 72, (graphics.guiHeight() / 2) - 18, 0, 0, 36, 36, 36, 36, 36, 36);
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, (graphics.guiWidth() / 2) + 36, (graphics.guiHeight() / 2) - 18, 0, 0, 36, 36, 36, 36, 36, 36);

        //graphics.text(font, String.valueOf(ModKeyMappings.FLUTE_UP_NOTE.getName().charAt(0)), (graphics.guiWidth() / 2) - (font.width(String.valueOf(ModKeyMappings.FLUTE_UP_NOTE.getName().charAt(0))) / 2), (graphics.guiHeight() / 2) + 64, 0xFFFFFFFF, true);
        if (ModKeyMappings.FLUTE_UP_NOTE.isDown()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, h_texture, (graphics.guiWidth() / 2) - 18, (graphics.guiHeight() / 2) - 72, 0, 0, 36, 36, 36, 36, 36, 36, 0xFFFF0000);
        }
        if (ModKeyMappings.FLUTE_DOWN_NOTE.isDown()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, h_texture, (graphics.guiWidth() / 2) - 18, (graphics.guiHeight() / 2) + 36, 0, 0, 36, 36, 36, 36, 36, 36, 0xFF00FF00);
        }
        if (ModKeyMappings.FLUTE_LEFT_NOTE.isDown()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, h_texture, (graphics.guiWidth() / 2) - 72, (graphics.guiHeight() / 2) - 18, 0, 0, 36, 36, 36, 36, 36, 36, 0xFF0000FF);
        }
        if (ModKeyMappings.FLUTE_RIGHT_NOTE.isDown()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, h_texture, (graphics.guiWidth() / 2) + 36, (graphics.guiHeight() / 2) - 18, 0, 0, 36, 36, 36, 36, 36, 36, 0xFFFF00FF);
        }
    }


    private static BaseFluteSpell getSpellFromSequence(String sequence) {
        for (BaseFluteSpell spell : FluteSpells.ALL_SPELLS) {
            if (sequence.equals(spell.getSequence())) {
                return spell;
            }
        }
        return null;
    }

    private static String convertSequenceToArrows(String sequence) {
        return sequence
                .replace("U", "⮝")
                .replace("L", "⮜")
                .replace("R", "⮞")
                .replace("D", "⮟");
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
