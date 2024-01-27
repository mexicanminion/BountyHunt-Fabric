package net.mexicanminion.bountyhunt.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.mexicanminion.bountyhunt.commands.SetBountyCommand;

public class Register {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(SetBountyCommand::register);
    }
}
