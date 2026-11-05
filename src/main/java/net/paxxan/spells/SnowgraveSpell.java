package net.paxxan.spells;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.phys.Vec3;
import net.paxxan.Symphony;

import java.util.Optional;
import java.util.function.Function;

public class SnowgraveSpell extends BaseFluteSpell {

    public SnowgraveSpell() {
        super();
        this.name = "Snowgrave";
        this.id = "snowgrave";
        this.description = "Lethal.";
        this.sequence = "UDUDLRUD";
        this.book_texture = Symphony.id("textures/item/spellbook/2d/" + id + ".png");
        this.post_spell_cooldown = 50;
    }

    @Override
    public void cast(Level level, Player player) {

    }
}
