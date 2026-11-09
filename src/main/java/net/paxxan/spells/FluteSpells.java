package net.paxxan.spells;

import com.google.common.collect.ImmutableList;
import net.minecraft.resources.Identifier;
import net.paxxan.Symphony;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FluteSpells {
    public static final CropGrowthSpell CROP_GROWTH = new CropGrowthSpell();
    public static final HealPrayerSpell HEAL_PRAYER = new HealPrayerSpell();
    public static final WarHornSpell WAR_HORN = new WarHornSpell();
    public static final HomeSpell HOME = new HomeSpell();
    public static final SHCSpell SHC = new SHCSpell();
    public static final PacifismSpell PACIFISM = new PacifismSpell();
    public static final LeapSpell LEAP = new LeapSpell();
    public static final IllusionSpell ILLUSION = new IllusionSpell();
    public static final SpelunkerSpell SPELUNKER = new SpelunkerSpell();

    public static final List<BaseFluteSpell> ALL_SPELLS = ImmutableList.of(
            CROP_GROWTH,
            HEAL_PRAYER,
            WAR_HORN,
            HOME,
            SHC,
            PACIFISM,
            LEAP,
            ILLUSION,
            SPELUNKER

    );

    public static final List<String> ALL_SPELL_NAMES = ALL_SPELLS.stream().map(BaseFluteSpell::getID).toList();

    public static final Set<Identifier> ALL_SPELL_ID = ALL_SPELLS.stream().map(spell -> Symphony.id(spell.getID()))
            .collect(Collectors.toUnmodifiableSet());

}
