package net.mexicanminion.bountyhunt.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.mexicanminion.bountyhunt.gui.ClaimBountyGUI;
import net.mexicanminion.bountyhunt.managers.RewardManager;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ClaimBountyCommand {

    // Registers the command claimbounty
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("claimbounty")
                .executes(context -> claimBounty(context, context.getSource())));
        //.executes(context -> claimBounty(context, context.getSource()))); calls the claimBounty method below
    }

    // Method to claim the bounty
    public static int claimBounty(CommandContext<ServerCommandSource> context, ServerCommandSource contextServer) {
        // Gets the source of the command and assigns to a ServerPlayerEntity object
        final ServerCommandSource source = context.getSource();
        final ServerPlayerEntity sender = source.getPlayer();

        // If the sender is null then its from console, display a message to the console
        if(sender == null) {
            source.sendFeedback(()-> Text.literal("You must be a player to use this command!"), false);
            return 0;
        }

        // If the sender has no bounty to claim, display a message to the sender
        if(RewardManager.getReward(sender.getUuid()) == 0 || RewardManager.getReward(sender.getUuid()) == -1) {
            source.sendFeedback(()-> Text.literal("You don't have a bounty to claim!"), false);
            return 0;
        }

        // Try to open the ClaimBountyGUI
        try {
            ClaimBountyGUI bountyGUI = new ClaimBountyGUI(sender, false, contextServer);
            bountyGUI.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
