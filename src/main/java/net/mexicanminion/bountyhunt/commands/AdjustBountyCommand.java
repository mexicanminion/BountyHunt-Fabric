package net.mexicanminion.bountyhunt.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.mexicanminion.bountyhunt.gui.IncreaseBountyGUI;
import net.mexicanminion.bountyhunt.managers.BountyDataManager;
import net.mexicanminion.bountyhunt.util.BountyDataImproved;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class AdjustBountyCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("adjustbounty")
                .executes(context -> adjustBounty(context, context.getSource())));
    }

    public static int adjustBounty(CommandContext<ServerCommandSource> context, ServerCommandSource contextServer) {
        // Gets the source of the command assigns it to a ServerPlayerEntity object
        final ServerCommandSource source = context.getSource();
        final ServerPlayerEntity sender = source.getPlayer();

        // Checks if the sender is null, is so its from console; disallow
        if(sender == null) {
            source.sendFeedback(()-> Text.literal("You must be a player to use this command!"), false);
            return 0;
        }

        // Checks if the player has an entry in the master list, if not it adds one
        if(BountyDataManager.getBountyData(sender.getUuid()) == null) {
            BountyDataManager.setBountyData(new BountyDataImproved(sender.getUuid(), false, false, 0, 0, sender.getGameProfile(), sender.getEntityName(), null));
        }

        try {
            IncreaseBountyGUI increaseBountyGUI = new IncreaseBountyGUI(sender, false, contextServer);
            increaseBountyGUI.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
