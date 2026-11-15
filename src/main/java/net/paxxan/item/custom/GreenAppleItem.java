package net.paxxan.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.paxxan.Symphony;
import net.paxxan.block.ModBlocks;
import net.paxxan.item.ModComponents;
import net.paxxan.spells.PlayerSpellManager;

import java.util.function.Consumer;

public class GreenAppleItem extends BlockItem {
    public GreenAppleItem(Properties properties) {
        super(ModBlocks.GREEN_APPLE_BLOCK, properties);
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return Component.translatable("item.symphony.green_apple");
    }

}
