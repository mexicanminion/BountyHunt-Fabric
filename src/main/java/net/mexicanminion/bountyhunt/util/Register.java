package net.mexicanminion.bountyhunt.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.mexicanminion.bountyhunt.commands.SetBountyCommand;
import net.mexicanminion.bountyhunt.commands.TestCommand;

public class Register {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(TestCommand::register);
        CommandRegistrationCallback.EVENT.register(SetBountyCommand::register);
        // TestCommand.register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment);
    }
}
