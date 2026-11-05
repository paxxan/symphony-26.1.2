package net.paxxan.spells;

import dev.dino.sparkle.Sparkle;
import dev.dino.sparkle.client.SParticles.ParticleBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.paxxan.Symphony;

import java.awt.*;

public class HellbombSpell extends BaseFluteSpell {

    public HellbombSpell() {
        super();
        this.name = "Hellbomb";
        this.id = "hellbomb";
        this.description = "Spreads democracy";
        this.sequence = "DULDURDU";
        this.book_texture = Symphony.id("textures/item/spellbook/2d/" + id + ".png");
    }

    @Override
    public void cast(Level level, Player player) {
        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 100; i++) {
                float x = (float) Math.random()-0.5f;
                float y = (float) Math.random()-0.5f;
                float z = (float) Math.random()-0.5f;
                Vec3 velo = new Vec3(x, y, z).scale(10f).scale(Math.random());
                ParticleBuilder.builder(Sparkle.id("test"))
                        .setScale(0, 50, 50, 50)
                        .setColor(new Color(187, 75, 25, 100), new Color(187, 75, 25, 200), new Color(225, 75, 75, 0))
                        .setRoll(0.1f)
                        .setLifetime(20*20)
                        .setVelocity(new Vec3(0,0,0), velo)
                        .setPos(player.getX(), player.getY(), player.getZ())
                        .buildAndAdd(serverLevel);
                ;

            }
        }
    }
}
