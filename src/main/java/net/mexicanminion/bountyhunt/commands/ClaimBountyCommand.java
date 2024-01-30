package net.mexicanminion.bountyhunt.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.mexicanminion.bountyhunt.gui.ClaimBountyGUI;
import net.mexicanminion.bountyhunt.gui.SetBountyGUI;
import net.mexicanminion.bountyhunt.managers.BountyManager;
import net.mexicanminion.bountyhunt.managers.RewardManager;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;

public class ClaimBountyCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("claimbounty")
                .executes(context -> claimBounty(context, context.getSource())));
    }

    public static int claimBounty(CommandContext<ServerCommandSource> context, ServerCommandSource contextServer) {
        final ServerCommandSource source = context.getSource();
        final ServerPlayerEntity sender = source.getPlayer();
        if(sender == null) {
            source.sendFeedback(()-> Text.literal("You must be a player to use this command!"), false);
            return 0;
        }

        if(RewardManager.getReward(sender.getUuid()) == 0 || RewardManager.getReward(sender.getUuid()) == -1) {
            source.sendFeedback(()-> Text.literal("You don't have a bounty to claim!"), false);
            return 0;
        }

        try {
            ClaimBountyGUI bountyGUI = new ClaimBountyGUI(sender, false, contextServer);
            bountyGUI.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
