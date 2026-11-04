package net.paxxan.spells;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class FluteSpells {
    public static final CropGrowthSpell CROP_GROWTH = new CropGrowthSpell();
    public static final HealPrayerSpell HEAL_PRAYER = new HealPrayerSpell();
    public static final WarHornSpell WAR_HORN = new WarHornSpell();

    public static final List<BaseFluteSpell> ALL_SPELLS = ImmutableList.of(
            CROP_GROWTH,
            HEAL_PRAYER,
            WAR_HORN

    );
}
