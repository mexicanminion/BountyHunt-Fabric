package net.mexicanminion.bountyhunt.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;

public class SetItemTypeCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("bountyItemType")
                .then(argument("ingot", ItemStackArgumentType.itemStack(commandRegistryAccess))
                .then(argument("block", ItemStackArgumentType.itemStack(commandRegistryAccess))
                .executes(context -> setItemType(context, ItemStackArgumentType.getItemStackArgument(context, "ingot"), ItemStackArgumentType.getItemStackArgument(context, "block"), context.getSource())))));
        //registers the command setbounty with the perameter player then calls the setBounty method
    }

    public static int setItemType(CommandContext<ServerCommandSource> context, ItemStackArgument ingot, ItemStackArgument block, ServerCommandSource contextServer) throws CommandSyntaxException {
        // Gets the source of the command assigns it to a ServerPlayerEntity object
        final ServerCommandSource source = context.getSource();
        final ServerPlayerEntity sender = source.getPlayer();

        // Checks if the sender is null, is so its from console; disallow
        if (sender == null) {
            source.sendFeedback(() -> Text.literal("You must be a player to use this command!"), false);
            return 0;
        }

        return 0;
    }

}
