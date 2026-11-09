package net.paxxan.item.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.paxxan.Symphony;
import net.paxxan.attachments.ModDataAttachments;
import net.paxxan.item.ModComponents;
import net.paxxan.spells.PlayerSpellManager;

public class SpellbookItem extends Item {
    public SpellbookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
            if (PlayerSpellManager.learnSpell(serverPlayer, Symphony.id(player.getItemInHand(hand).get(ModComponents.SPELL_ID)))) {
                player.sendSystemMessage(Component.literal("You learned " + player.getItemInHand(hand).get(ModComponents.SPELL_ID)));
                player.getItemInHand(hand).shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return Component.translatable("symphony.spells." + itemStack.get(ModComponents.SPELL_ID));
    }
}
