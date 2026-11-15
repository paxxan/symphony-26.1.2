package net.paxxan.item.custom;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ElderGuardianParticle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.paxxan.Symphony;
import net.paxxan.attachments.ModDataAttachments;
import net.paxxan.block.ModBlocks;
import net.paxxan.block.custom.SpellbookStackBlock;
import net.paxxan.item.ModComponents;
import net.paxxan.item.ModItems;
import net.paxxan.networking.packet.FlutePlayPayloadC2S;
import net.paxxan.networking.packet.SpellbookLearnDisplayPayloadS2C;
import net.paxxan.spells.PlayerSpellManager;

import java.util.function.Consumer;

public class SpellbookItem extends BlockItem {
    public SpellbookItem(Properties properties) {
        super(ModBlocks.SPELLBOOK_STACK, properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
            if (PlayerSpellManager.learnSpell(serverPlayer, Symphony.id(player.getItemInHand(hand).get(ModComponents.SPELL_ID)))) {
                ItemStack displayBook = new ItemStack(ModItems.DISPLAY_SPELLBOOK_2D);
                displayBook.set(ModComponents.SPELL_ID, player.getItemInHand(hand).get(ModComponents.SPELL_ID));
                ServerPlayNetworking.send(serverPlayer, new SpellbookLearnDisplayPayloadS2C(displayBook));
                player.getItemInHand(hand).shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(level, player, hand);
    }


    @Override
    public Component getName(ItemStack itemStack) {
        return Component.translatable("item.symphony.spellbook");
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
                                Consumer<Component> builder, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, builder, flag);
        String spell = stack.get(ModComponents.SPELL_ID);
        if (spell != null) {
            builder.accept(Component.translatable("spell.symphony." + spell).withStyle(ChatFormatting.GRAY));
        }
    }
}
