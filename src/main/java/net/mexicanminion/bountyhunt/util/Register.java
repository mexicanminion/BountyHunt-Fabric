package net.mexicanminion.bountyhunt.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.mexicanminion.bountyhunt.commands.BountyBoardCommand;
import net.mexicanminion.bountyhunt.commands.ClaimBountyCommand;
import net.mexicanminion.bountyhunt.commands.HelpBounty;
import net.mexicanminion.bountyhunt.commands.SetBountyCommand;

public class Register {

    /**
     * Registers the commands
     */
    public static void register() {
        CommandRegistrationCallback.EVENT.register(SetBountyCommand::register);
        CommandRegistrationCallback.EVENT.register(ClaimBountyCommand::register);
        CommandRegistrationCallback.EVENT.register(BountyBoardCommand::register);
        CommandRegistrationCallback.EVENT.register(HelpBounty::register);
    }
}
