package net.paxxan.spells;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.phys.Vec3;
import net.paxxan.Symphony;
import net.paxxan.effect.ModEffects;

import java.util.Optional;
import java.util.function.Function;

public class SpelunkerSpell extends BaseFluteSpell {
    int EFFECT_DUR_TICKS = 400;

    public SpelunkerSpell() {
        super();
        this.name = "Spelunker";
        this.id = "spelunker";
        this.description = "Allows the user to see valuables around them.";
        this.sequence = "DLDUDDD";
        this.book_texture = Symphony.id("textures/item/spellbook/2d/" + id + ".png");
        this.post_spell_cooldown = (int) (20 * 60);
        this.charge = 2;
    }

    @Override
    public void cast(Level level, Player player) {
        super.cast(level, player);
        player.addEffect(new MobEffectInstance(ModEffects.SPELUNKER, EFFECT_DUR_TICKS, 0), player);;
    }
}
