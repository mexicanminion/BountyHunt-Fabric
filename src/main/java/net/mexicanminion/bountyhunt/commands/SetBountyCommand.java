package net.mexicanminion.bountyhunt.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.argument;


public class SetBountyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("setbounty")
                        .then(argument("player", EntityArgumentType.player())
                                .executes(context -> setBounty(context, EntityArgumentType.getPlayer(context, "player"), context.getSource()))));
    }

    public static int setBounty(CommandContext<ServerCommandSource> context, ServerPlayerEntity player, ServerCommandSource contextServer) throws CommandSyntaxException {
        final ServerCommandSource source = context.getSource();
        final PlayerEntity sender = source.getPlayer();
        ServerPlayerEntity target = null;
        if(sender == null) {
            source.sendFeedback(()-> Text.literal("You must be a player to use this command!"), false);
            return 0;
        }

        if(PlayerLookup.all(contextServer.getServer()).contains(player))
            target = player;

        if(target == null) {
            context.getSource().sendFeedback(()-> Text.literal("Player Not online"), false);
        }else {
            context.getSource().sendFeedback(()-> Text.literal("Player is online"), false);
        }
        context.getSource().sendFeedback(()-> Text.literal("msg").formatted(Formatting.YELLOW), false);

        return 0;
    }
}
