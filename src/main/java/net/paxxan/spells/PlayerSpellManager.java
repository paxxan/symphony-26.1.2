package net.paxxan.spells;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.paxxan.attachments.ModDataAttachments;
import org.jspecify.annotations.Nullable;

import java.util.*;

import static net.paxxan.attachments.ModDataAttachments.SPELL_COOLDOWNS;

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

    public static void tickSpellCooldowns(Player player) {
        Map<Identifier, Integer> map = player.getAttached(SPELL_COOLDOWNS);
        if (map == null || map.isEmpty()) return;
        map.replaceAll((id, ticks) -> ticks - 1);
        map.values().removeIf(ticks -> ticks <= 0);
    }

    public static int getCooldown(Player player, Identifier spell) {
        Map<Identifier, Integer> map = player.getAttached(SPELL_COOLDOWNS);
        return map == null ? 0 : map.getOrDefault(spell, 0);
    }

    public static void setCooldown(Player player, Identifier spell, int ticks) {
        Map<Identifier, Integer> map = new HashMap<>(player.getAttachedOrElse(SPELL_COOLDOWNS, Map.of()));
        if (ticks > 0) map.put(spell, ticks);
        else map.remove(spell);
        player.setAttached(SPELL_COOLDOWNS, map);
    }

    public static void editCooldown(Player player, Identifier spell, int ticks) {
        setCooldown(player, spell, getCooldown(player, spell) + ticks);
    }

    public static boolean isReady(Player player, Identifier spellId) {
        return getCooldown(player, spellId) <= 0;
    }
}
