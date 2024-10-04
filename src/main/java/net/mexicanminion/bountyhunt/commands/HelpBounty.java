package net.mexicanminion.bountyhunt.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class HelpBounty {

    // Registers the command claimbounty
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("helpbounty")
                .executes(context -> helpBounty(context, context.getSource())));
        //.executes(context -> claimBounty(context, context.getSource()))); calls the claimBounty method below
    }

    // Method to claim the bounty
    public static int helpBounty(CommandContext<ServerCommandSource> context, ServerCommandSource contextServer) {
        // Gets the source of the command and assigns to a ServerPlayerEntity object
        final ServerCommandSource source = context.getSource();
        final ServerPlayerEntity sender = source.getPlayer();

        source.sendFeedback(()-> Text.literal("/setbounty {player}: Set a bounty on a online player"), false);
        source.sendFeedback(()-> Text.literal("/adjustbounty: See the bounties you can increase"), false);
        source.sendFeedback(()-> Text.literal("/claimbounty: Claim your bounty"), false);
        source.sendFeedback(()-> Text.literal("/bountyboard: See all bounties"), false);
        source.sendFeedback(()-> Text.literal("/helpbounty: See list of Bounty Hunt commands"), false);

        if(sender.hasPermissionLevel(3)){
            source.sendFeedback(()-> Text.literal("/bountyitemtype {item}: Set the item type for bounties"), false);
            source.sendFeedback(()-> Text.literal("/bountyItemType custom {ingot}: Set custom item type for bounties, ingot only"), false);
            source.sendFeedback(()-> Text.literal("/bountyItemType custom <ingot> <block> <ingotToBlockAmount>: Set custom item type for bounties"), false);
            source.sendFeedback(()-> Text.literal("/bountyItemType confirm: confirm currency change if needed"), false);
            source.sendFeedback(()-> Text.literal("/bountyannouncement {amount}: Set the announcement amount for bounties"), false);
        }

        return 0;
    }
}
