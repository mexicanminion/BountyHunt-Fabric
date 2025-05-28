package net.mexicanminion.bountyhunt.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.mexicanminion.bountyhunt.gui.BountyBoardGUI;
import net.mexicanminion.bountyhunt.gui.ShopGUI;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ShopCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("huntshop")
                .executes(context -> shop(context, context.getSource())));
    }

    public static int shop(CommandContext<ServerCommandSource> context, ServerCommandSource contextServer) {
        // Gets the source of the command assigns it to a ServerPlayerEntity object
        final ServerCommandSource source = context.getSource();
        final ServerPlayerEntity sender = source.getPlayer();

        // Checks if the sender is null, is so its from console; disallow
        if(sender == null) {
            source.sendFeedback(()-> Text.literal("You must be a player to use this command!"), false);
            return 0;
        }

        try {
            ShopGUI shopGUI = new ShopGUI(sender, false, contextServer);
            shopGUI.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
