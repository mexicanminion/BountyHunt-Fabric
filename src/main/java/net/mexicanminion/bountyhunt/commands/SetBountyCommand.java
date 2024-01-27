package net.mexicanminion.bountyhunt.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
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
        ServerPlayerEntity target = player;
        if(sender == null) {
            source.sendFeedback(()-> Text.literal("You must be a player to use this command!"), false);
            return 0;
        }

        if(sender == target) {
            source.sendFeedback(()-> Text.literal("You cannot set a bounty on yourself!"), false);
            return 0;
        }

        for (ServerPlayerEntity players : contextServer.getServer().getPlayerManager().getPlayerList()) {
            players.networkHandler.sendPacket(new TitleS2CPacket(Text.literal("Bounty set" + Text.literal("test")).formatted(Formatting.RED)));
        }

        context.getSource().sendFeedback(()-> Text.literal("msg").formatted(Formatting.YELLOW), false);

        return 0;
    }
}
