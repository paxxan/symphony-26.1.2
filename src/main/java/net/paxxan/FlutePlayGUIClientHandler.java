package net.paxxan;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.paxxan.item.ModComponents;
import net.paxxan.item.ModItems;
import net.paxxan.keymapping.ModKeyMappings;
import net.paxxan.spells.BaseFluteSpell;
import net.paxxan.spells.FluteSpells;
import net.paxxan.spells.PlayerSpellManager;

import static net.paxxan.SymphonyClient.GALACTIC;

public class FlutePlayGUIClientHandler implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
    }

    public static void extract(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {

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
                        boolean known = PlayerSpellManager.knowsSpell(client.player, Symphony.id(spell.getID()));
                        Component name = known ? spell.getTranslatableName() : spell.getTranslatableName().copy().withStyle(GALACTIC);
                        Identifier texture = known ? spell.getBookTexture() : Symphony.id("textures/item/spellbook/2d/unknown_book.png");
                        graphics.text(font, name, (graphics.guiWidth() / 2) - (font.width(name) / 2), (graphics.guiHeight() / 2) + 24, 0xFF00FF00, true);
                        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, (graphics.guiWidth() / 2) - 16, (graphics.guiHeight() / 2) - 16, 0, 0, 32, 32, 32, 32, 32, 32);
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
        String up_key_char = String.valueOf(Component.translatable(ModKeyMappings.FLUTE_UP_NOTE.getTranslatedKeyMessage().getString()).getString());
        String down_key_char = String.valueOf(Component.translatable(ModKeyMappings.FLUTE_DOWN_NOTE.getTranslatedKeyMessage().getString()).getString());
        String left_key_char = String.valueOf(Component.translatable(ModKeyMappings.FLUTE_LEFT_NOTE.getTranslatedKeyMessage().getString()).getString());
        String right_key_char = String.valueOf(Component.translatable(ModKeyMappings.FLUTE_RIGHT_NOTE.getTranslatedKeyMessage().getString()).getString());

        Identifier up_texture = Symphony.id("textures/gui/key_panel_up.png");
        Identifier up_h_texture = Symphony.id("textures/gui/key_panel_highlight_up.png");

        Identifier down_texture = Symphony.id("textures/gui/key_panel_down.png");
        Identifier down_h_texture = Symphony.id("textures/gui/key_panel_highlight_down.png");

        Identifier left_texture = Symphony.id("textures/gui/key_panel_left.png");
        Identifier left_h_texture = Symphony.id("textures/gui/key_panel_highlight_left.png");

        Identifier right_texture = Symphony.id("textures/gui/key_panel_right.png");
        Identifier right_h_texture = Symphony.id("textures/gui/key_panel_highlight_right.png");

        graphics.blit(RenderPipelines.GUI_TEXTURED, up_texture, (graphics.guiWidth() / 2) - 18, (graphics.guiHeight() / 2) - 72, 0, 0, 36, 36, 36, 36,36, 36);
        graphics.blit(RenderPipelines.GUI_TEXTURED, down_texture, (graphics.guiWidth() / 2) - 18, (graphics.guiHeight() / 2) + 36, 0, 0, 36, 36, 36, 36, 36, 36);
        graphics.blit(RenderPipelines.GUI_TEXTURED, left_texture, (graphics.guiWidth() / 2) - 72, (graphics.guiHeight() / 2) - 18, 0, 0, 36, 36, 36, 36, 36, 36);
        graphics.blit(RenderPipelines.GUI_TEXTURED, right_texture, (graphics.guiWidth() / 2) + 36, (graphics.guiHeight() / 2) - 18, 0, 0, 36, 36, 36, 36, 36, 36);

        if (ModKeyMappings.FLUTE_UP_NOTE.isDown()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, up_h_texture, (graphics.guiWidth() / 2) - 18, (graphics.guiHeight() / 2) - 72, 0, 0, 36, 36, 36, 36, 36, 36, 0xAAFF0000);
        }
        if (ModKeyMappings.FLUTE_DOWN_NOTE.isDown()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, down_h_texture, (graphics.guiWidth() / 2) - 18, (graphics.guiHeight() / 2) + 36, 0, 0, 36, 36, 36, 36, 36, 36, 0xAA00FF00);
        }
        if (ModKeyMappings.FLUTE_LEFT_NOTE.isDown()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, left_h_texture, (graphics.guiWidth() / 2) - 72, (graphics.guiHeight() / 2) - 18, 0, 0, 36, 36, 36, 36, 36, 36, 0xAA0000FF);
        }
        if (ModKeyMappings.FLUTE_RIGHT_NOTE.isDown()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, right_h_texture, (graphics.guiWidth() / 2) + 36, (graphics.guiHeight() / 2) - 18, 0, 0, 36, 36, 36, 36, 36, 36, 0xAAFF00FF);
        }

        graphics.text(font, up_key_char, (graphics.guiWidth() / 2) - (font.width(up_key_char) / 2), (graphics.guiHeight() / 2) - 44, 0xFFFFFFFF, true);
        graphics.text(font, down_key_char, (graphics.guiWidth() / 2) - (font.width(down_key_char) / 2), (graphics.guiHeight() / 2) + 64, 0xFFFFFFFF, true);
        graphics.text(font, left_key_char, ((graphics.guiWidth() / 2) - 55) - (font.width(down_key_char) / 2), (graphics.guiHeight() / 2) + 10, 0xFFFFFFFF, true);
        graphics.text(font, right_key_char, ((graphics.guiWidth() / 2) + 55) - (font.width(down_key_char) / 2), (graphics.guiHeight() / 2) + 10, 0xFFFFFFFF, true);
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
}
