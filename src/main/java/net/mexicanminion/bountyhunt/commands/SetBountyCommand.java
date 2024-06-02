package net.mexicanminion.bountyhunt.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mexicanminion.bountyhunt.gui.SetBountyGUI;
import net.mexicanminion.bountyhunt.managers.BountyDataManager;
import net.mexicanminion.bountyhunt.util.BountyData;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;


public class SetBountyCommand {

    // Registers the command setbounty
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("setbounty")
                        .then(argument("player", EntityArgumentType.player())
                                .executes(context -> setBounty(context, EntityArgumentType.getPlayer(context, "player"), context.getSource()))));
        //registers the command setbounty with the perameter player then calls the setBounty method
    }

    // Sets the bounty on a player
    public static int setBounty(CommandContext<ServerCommandSource> context, ServerPlayerEntity target, ServerCommandSource contextServer) throws CommandSyntaxException {
        // Gets the source of the command assigns it to a ServerPlayerEntity object
        final ServerCommandSource source = context.getSource();
        final ServerPlayerEntity sender = source.getPlayer();

        // Checks if the sender is null, is so its from console; disallow
        if(sender == null) {
            source.sendFeedback(()-> Text.literal("You must be a player to use this command!"), false);
            return 0;
        }

        // Checks if the sender is the target, if so disallow
        if(sender == target) {
            source.sendFeedback(()-> Text.literal("You cannot set a bounty on yourself!"), false);
            return 0;
        }

        // Checks if the target has a bounty, if so disallow
        if(BountyDataManager.getBountyData(target.getUuid()) == null) {
            BountyDataManager.setBountyData(new BountyData(target.getUuid(), false, false, 0, 0, target.getGameProfile(), target.getEntityName()));
        }

        // Checks if the target has a bounty, if so disallow
        if(BountyDataManager.getBountyData(target.getUuid()).getHasBounty()) {
            source.sendFeedback(()-> Text.literal("That player already has a bounty!"), false);
            return 0;
        }

        // Opens the SetBountyGUI
        try {
            SetBountyGUI bountyGUI = new SetBountyGUI(sender, false, contextServer, target);
            bountyGUI.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
