package net.paxxan.item.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.paxxan.Symphony;
import net.paxxan.item.ModComponents;
import net.paxxan.spells.BaseFluteSpell;
import net.paxxan.spells.FluteSpells;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;


public class FluteItem extends Item {

    static float UP_NOTE_PITCH = (float) Math.pow(2,((double) -1 /12));
    static float LEFT_NOTE_PITCH = (float) Math.pow(2,((double) 3 /12));
    static float RIGHT_NOTE_PITCH = (float) Math.pow(2,((double) 5 /12));
    static float DOWN_NOTE_PITCH = (float) Math.pow(2,((double) 10 /12));

    public static float[] NOTES = {UP_NOTE_PITCH, LEFT_NOTE_PITCH, RIGHT_NOTE_PITCH, DOWN_NOTE_PITCH};

    public FluteItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(final ItemStack itemStack, final LivingEntity user) {
        return 72000;
    }

    @Override
    public ItemUseAnimation getUseAnimation(final ItemStack itemStack) {
        return ItemUseAnimation.SPYGLASS;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
        super.onUseTick(level, livingEntity, itemStack, ticksRemaining);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        if(slot != EquipmentSlot.MAINHAND) {
            itemStack.set(ModComponents.FLUTE_PLAYING_COMPONENT, false);
            itemStack.set(ModComponents.FLUTE_SEQUENCE_COMPONENT, "");
        }
        super.inventoryTick(itemStack, level, owner, slot);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if(hand == InteractionHand.MAIN_HAND) {
            ItemStack stack = player.getItemInHand(hand);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.NOTE_BLOCK_TRUMPET_OXIDIZED, SoundSource.PLAYERS, 1f, 1f);
            player.getCooldowns().addCooldown(stack, 20);
            String sequence = stack.getOrDefault(ModComponents.FLUTE_SEQUENCE_COMPONENT, "");
            if (!level.isClientSide()) {
                if (stack.get(ModComponents.FLUTE_PLAYING_COMPONENT) == Boolean.FALSE) {
                    stack.set(ModComponents.FLUTE_PLAYING_COMPONENT, Boolean.TRUE);
                    stack.set(ModComponents.FLUTE_SEQUENCE_COMPONENT, "");
                } else {
                    boolean spellFound = false;
                    stack.set(ModComponents.FLUTE_PLAYING_COMPONENT, Boolean.FALSE);
                    for (BaseFluteSpell spell : FluteSpells.ALL_SPELLS) {
                        if (sequence.equals(spell.getSequence())) {
                            spell.cast(level, player);
                            spellFound = true;
                            break;
                        }
                    }
                    if (!spellFound) {
                        Symphony.LOGGER.info("No spell found.");
                    }
                    stack.set(ModComponents.FLUTE_SEQUENCE_COMPONENT, "");
                }

            }
            return InteractionResult.SUCCESS_SERVER;
        }
        return super.use(level, player, hand);
    }
}
