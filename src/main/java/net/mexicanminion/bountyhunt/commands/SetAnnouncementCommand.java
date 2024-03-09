package net.mexicanminion.bountyhunt.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.mexicanminion.bountyhunt.util.CommonMethods;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.mexicanminion.bountyhunt.BountyHuntMod.config;
import static net.minecraft.server.command.CommandManager.argument;

public class SetAnnouncementCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("bountyAnnouncement")
                .requires(source -> source.hasPermissionLevel(3))
                .then(argument("amount", IntegerArgumentType.integer())
                        .executes(context -> setAnnouncement(context, IntegerArgumentType.getInteger(context, "amount")))
                )
                .then(CommandManager.literal("Default")
                        .executes(context -> setAnnouncement(context, 576))
                )
        );

        //registers the command setbounty with the perameter player then calls the setBounty method
    }

    private static int setAnnouncement(CommandContext<ServerCommandSource> context, int amount) {
        final ServerCommandSource source = context.getSource();
        final ServerPlayerEntity sender = source.getPlayer();

        // Checks if the sender is null, is so its from console; disallow
        if (sender == null) {
            source.sendFeedback(() -> Text.literal("You must be a player to use this command!"), false);
            return 0;
        }

        config.update("announceAmount", amount);
        config.save();

        CommonMethods.updateConfigCommon();

        source.sendFeedback(() -> Text.literal("Announcement amount has been updated to " + amount), true);

        return 0;
    }
}
