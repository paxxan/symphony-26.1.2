package net.paxxan.item.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.paxxan.item.ModItems;
import net.paxxan.sound.ModSounds;

public class EssenceItem extends Item {
    public EssenceItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean overrideStackedOnOther(final ItemStack self, final Slot slot, final ClickAction clickAction, final Player player) {
        ItemStack other = slot.getItem();
        if (/*clickAction == ClickAction.PRIMARY && */!other.isEmpty()) {
            if (other.is(ModItems.FLUTE)) {
                if (other.getDamageValue() > 0) {
                    other.setDamageValue(other.getDamageValue() - 8);
                    this.broadcastChangesOnContainerMenu(player);
                    playInsertSound(player, 1.5F - ((float) other.getDamageValue() / 100), other.getDamageValue() == 0);
                    self.consume(1, player);
                    return true;
                }
            }
        }
        return false;
    }

    private void broadcastChangesOnContainerMenu(final Player player) {
        AbstractContainerMenu containerMenu = player.containerMenu;
        if (containerMenu != null) {
            containerMenu.slotsChanged(player.getInventory());
        }
    }

    private static void playInsertSound(final Entity entity, float pitch, boolean full) {
        if (full) {
            entity.playSound(ModSounds.FLUTE_FULL_CHARGE, 0.8F, 1F);
        } else {
            entity.playSound(ModSounds.FLUTE_CHARGE, 0.8F, pitch);
        }
    }
}
