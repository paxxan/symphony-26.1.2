package net.paxxan;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.paxxan.attachments.ModDataAttachments;
import net.paxxan.item.ModComponents;
import net.paxxan.item.ModItems;
import net.paxxan.keymapping.ModKeyMappings;
import net.paxxan.spells.BaseFluteSpell;
import net.paxxan.spells.FluteSpells;
import net.paxxan.spells.PlayerSpellManager;

import java.util.Map;

import static net.paxxan.SymphonyClient.GALACTIC;

public class CooldownGUIClientHandler implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
    }

    private static final Identifier COOLDOWN_BG = Symphony.id("textures/gui/spell_cooldown.png");
    private static final int BG_WIDTH = 144;
    private static final int BG_HEIGHT = 18;

    public static void extract(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.options.hideGui) {
            return;
        }
        Map<Identifier, Integer> cooldowns = client.player.getAttached(ModDataAttachments.SPELL_COOLDOWNS);
        if (cooldowns == null || cooldowns.isEmpty()) {
            return;
        }
        Font font = client.font;
        int x = 8;
        int y = 8;
        boolean known = false;
        Identifier texture = Symphony.id("textures/item/spellbook/2d/unknown_book.png");
        for (Map.Entry<Identifier, Integer> entry : cooldowns.entrySet()) {
            Component name = Component.literal(entry.getKey().getPath());
            for (BaseFluteSpell spell : FluteSpells.ALL_SPELLS) {
                if (Symphony.id(spell.getID()).equals(entry.getKey())) {
                    known = PlayerSpellManager.knowsSpell(client.player, Symphony.id(spell.getID()));
                    name = known ? spell.getTranslatableName() : spell.getTranslatableName().copy().withStyle(GALACTIC);
                    texture = known ? spell.getBookTexture() : Symphony.id("textures/item/spellbook/2d/unknown_book.png");
                    break;
                }
            }

            graphics.blit(RenderPipelines.GUI_TEXTURED, COOLDOWN_BG,
                    x, y,
                    0f, 0f,
                    BG_WIDTH, BG_HEIGHT,
                    BG_WIDTH, BG_HEIGHT);
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture,
                    x + 4, y + 1,
                    0f, 0f,
                    16, 16,
                    16, 16);
            int textY = y + (BG_HEIGHT - font.lineHeight) / 2;
            graphics.text(font, name.copy(), x + 22, textY, 0xFFFFFFFF);
            graphics.text(font, String.format(": %.1fs", entry.getValue() / 20.0), x + 23 + font.width(name.copy()), textY, 0xFFFFFFFF);
            y += BG_HEIGHT + 2;
        }

    }
}

