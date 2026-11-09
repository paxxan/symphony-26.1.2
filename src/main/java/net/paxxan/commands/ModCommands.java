package net.paxxan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.paxxan.Symphony;
import net.paxxan.spells.BaseFluteSpell;
import net.paxxan.spells.FluteSpells;
import net.paxxan.spells.PlayerSpellManager;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class ModCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, buildContext, selection) ->
                dispatcher.register(
                        Commands.literal("symphony")
                                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                                .then(Commands.literal("learn")
                                        .then(Commands.argument("targets", EntityArgument.players())
                                        .then(Commands.argument("spell", StringArgumentType.word())
                                                .suggests(SPELL_SUGGESTIONS)
                                                .executes(ctx -> run(ctx, true)))))
                                .then(Commands.literal("forget")
                                        .then(Commands.argument("targets", EntityArgument.players())
                                        .then(Commands.argument("spell", StringArgumentType.word())
                                                .suggests(SPELL_SUGGESTIONS)
                                                .executes(ctx -> run(ctx, false)))))
                                )));
    }
    private static final SuggestionProvider<CommandSourceStack> SPELL_SUGGESTIONS =
            (ctx, builder) -> SharedSuggestionProvider.suggest(
                    Stream.concat(Stream.of("all"), FluteSpells.ALL_SPELLS.stream().map(BaseFluteSpell::getID)),
                    builder);

    private static final SuggestionProvider<CommandSourceStack> ALL_SPELLS_SUGGESTION =
            (ctx, builder) -> SharedSuggestionProvider.suggest(
                    "all".lines(),
                    builder);

    private static int run(CommandContext<CommandSourceStack> ctx, boolean learn) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
        String spellID = StringArgumentType.getString(ctx, "spell");
        Identifier id = Symphony.id(spellID);
        int changed = 0;
        boolean all = spellID.equals("all") ? true : false;
        if (!FluteSpells.ALL_SPELL_ID.contains(id) && !spellID.equals("all")) {
            ctx.getSource().sendFailure(Component.literal("Unknown spell: " + spellID));
            return 0;
        }
        for (ServerPlayer player : targets) {
            boolean ok = learn ?
                    (all ? PlayerSpellManager.learnAllSpells(player) : PlayerSpellManager.learnSpell(player, id)) :
                    (all ? PlayerSpellManager.forgetAllSpells(player) : PlayerSpellManager.forgetSpell(player, id));
            if (ok) {
                ++changed;
            }
        }
        int count = changed;
        ctx.getSource().sendSuccess(() -> Component.literal(
                (learn ? "Learned " : "Forgot ") + spellID), true);
        return changed;

    }

    public static void registerCommands() {
        Symphony.LOGGER.info("Registering Commands for " + Symphony.MOD_ID);
        register();
    }
}
