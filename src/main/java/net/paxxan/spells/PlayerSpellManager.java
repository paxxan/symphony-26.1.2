package net.paxxan.spells;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.paxxan.attachments.ModDataAttachments;

import java.util.HashSet;
import java.util.Set;

public class PlayerSpellManager {
    public static boolean learnSpell(ServerPlayer player, Identifier spell) {
        Set<Identifier> knownSpells = player.getAttachedOrElse(ModDataAttachments.LEARNED_SPELLS, Set.of());
        if (knownSpells.contains(spell)) {
            return false;
        }
        Set<Identifier> newSpells = new HashSet<>(knownSpells);
        newSpells.add(spell);
        player.setAttached(ModDataAttachments.LEARNED_SPELLS, Set.copyOf(newSpells));
        return true;
    }

    public static boolean knowsSpell(Player player, Identifier spell) {
        return player.getAttachedOrElse(ModDataAttachments.LEARNED_SPELLS, Set.of()).contains(spell);
    }

    public static boolean forgetSpell(ServerPlayer player, Identifier spell) {
        Set<Identifier> knownSpells = player.getAttachedOrElse(ModDataAttachments.LEARNED_SPELLS, Set.of());
        if (!knownSpells.contains(spell)) {
            return false;
        }
        Set<Identifier> newSpells = new HashSet<>(knownSpells);
        newSpells.remove(spell);
        player.setAttached(ModDataAttachments.LEARNED_SPELLS, Set.copyOf(newSpells));
        return true;
    }

    public static boolean learnAllSpells(ServerPlayer player) {
        Set<Identifier> allSpells = new HashSet<>(FluteSpells.ALL_SPELL_ID);
        player.setAttached(ModDataAttachments.LEARNED_SPELLS, Set.copyOf(allSpells));
        return true;
    }

    public static boolean forgetAllSpells(ServerPlayer player) {
        player.setAttached(ModDataAttachments.LEARNED_SPELLS, Set.of());
        return false;
    }
}
