package net.mexicanminion.bountyhunt.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mexicanminion.bountyhunt.managers.BountyDataManager;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.Inject;

import static net.mexicanminion.bountyhunt.BountyHuntMod.config;
import static net.minecraft.server.command.CommandManager.argument;

public class SetItemTypeCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("bountyItemType")
            .then(argument("ingot", ItemStackArgumentType.itemStack(commandRegistryAccess))
                .then(argument("block", ItemStackArgumentType.itemStack(commandRegistryAccess))
                    .executes(context -> setItemType(context, ItemStackArgumentType.getItemStackArgument(context, "ingot"), ItemStackArgumentType.getItemStackArgument(context, "block"), context.getSource()))
                )
            )
            .then(CommandManager.literal("confirm")
                    .executes(SetItemTypeCommand::hardConfirm)
            )
        );

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

        if(!BountyDataManager.getBountyData().isEmpty()){
            source.sendFeedback(() -> Text.literal("There are bounties currently active! You can't change the item right now!"), true);
            source.sendFeedback(() -> Text.literal("To force the change type /bountyItemType confirm"), true);
            return 0;
        }

        config.set("itemIngot", Registries.ITEM.getRawId(ingot.getItem()));
        config.set("itemBlock", Registries.ITEM.getRawId(block.getItem()));

        source.sendFeedback(() -> Text.literal("Currency has been changed!! Notify your server members so they can stay in the know!"), true);

        return 0;
    }

    public static int hardConfirm(CommandContext<ServerCommandSource> context) {
        // Gets the source of the command assigns it to a ServerPlayerEntity object
        final ServerCommandSource source = context.getSource();
        final ServerPlayerEntity sender = source.getPlayer();

        // Checks if the sender is null, is so its from console; disallow
        if (sender == null) {
            source.sendFeedback(() -> Text.literal("You must be a player to use this command!"), false);
            return 0;
        }

        config.set("itemIngot", Registries.ITEM.getRawId(Items.IRON_INGOT));
        config.set("itemBlock", Registries.ITEM.getRawId(Items.IRON_BLOCK));

        return 0;
    }

}
