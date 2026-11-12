package net.paxxan.item.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.paxxan.Symphony;
import net.paxxan.effect.ModEffects;
import net.paxxan.item.ModComponents;
import net.paxxan.spells.BaseFluteSpell;
import net.paxxan.spells.FluteSpells;
import net.paxxan.spells.PlayerSpellManager;
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
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        if(slot != EquipmentSlot.MAINHAND || !owner.isAlive()) {
            itemStack.set(ModComponents.FLUTE_PLAYING_COMPONENT, false);
            itemStack.set(ModComponents.FLUTE_SEQUENCE_COMPONENT, "");
        }
        Minecraft client = Minecraft.getInstance();

        super.inventoryTick(itemStack, level, owner, slot);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        boolean spell_cast = false;
        boolean full_lungs = false;
        MobEffectInstance effectInstance = null;
        int amplifier = 1;

        if(hand == InteractionHand.MAIN_HAND) {
            ItemStack stack = player.getItemInHand(hand);
            String sequence = stack.getOrDefault(ModComponents.FLUTE_SEQUENCE_COMPONENT, "");
            if (!level.isClientSide()) {
                if (!Boolean.TRUE.equals(stack.get(ModComponents.FLUTE_PLAYING_COMPONENT))) {
                    stack.set(ModComponents.FLUTE_PLAYING_COMPONENT, Boolean.TRUE);
                    stack.set(ModComponents.FLUTE_SEQUENCE_COMPONENT, "");
                } else {
                    boolean spellFound = false;
                    stack.set(ModComponents.FLUTE_PLAYING_COMPONENT, Boolean.FALSE);
                    for (BaseFluteSpell spell : FluteSpells.ALL_SPELLS) {
                        if (sequence.equals(spell.getSequence())) {
                            spellFound = true;
                            if (PlayerSpellManager.isReady(player, Symphony.id(spell.getID()))) {
                                if (stack.getDamageValue() + spell.getCharge() < stack.getMaxDamage()) {
                                    spell.cast(level, player);
                                    spell_cast = true;
                                    stack.hurtAndBreak(spell.getCharge(), player, hand);
                                } else {
                                    player.sendOverlayMessage(Component.translatable("item.symphony.flute.low_charge_message"));
                                }

                            } else {
                                player.sendOverlayMessage(Component.translatable("item.symphony.flute.cooldown_message"));
                            }
                            break;
                        }
                    }
                    if (!spellFound) {
                        Symphony.LOGGER.info("No spell found.");
                    }
                    stack.set(ModComponents.FLUTE_SEQUENCE_COMPONENT, "");
                }
                if (!spell_cast) {
                    if (player.hasEffect(ModEffects.FULL_LUNGS)) {
                        player.getCooldowns().addCooldown(stack, 5);
                    } else {
                        player.getCooldowns().addCooldown(stack, 20);
                    }
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.NOTE_BLOCK_TRUMPET_OXIDIZED, SoundSource.PLAYERS, 1f, 1f);
                }
            }

            return InteractionResult.SUCCESS_SERVER;
        }
        return super.use(level, player, hand);
    }

}
