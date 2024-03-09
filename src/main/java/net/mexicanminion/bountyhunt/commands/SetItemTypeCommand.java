package net.mexicanminion.bountyhunt.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.mexicanminion.bountyhunt.managers.BountyDataManager;
import net.mexicanminion.bountyhunt.util.CommonMethods;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.mexicanminion.bountyhunt.BountyHuntMod.config;
import static net.minecraft.server.command.CommandManager.argument;

public class SetItemTypeCommand {

    static boolean needsConfirm = false;
    static Item itemIngot;
    static Item itemBlock;
    static int amount;
    static boolean onlyIngot;
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("bountyItemType")
            .requires(source -> source.hasPermissionLevel(3))
            .then(CommandManager.literal("diamond")
                    .executes(context -> setItemType(context, Items.DIAMOND, Items.DIAMOND_BLOCK, 9, false))
            )
            .then(CommandManager.literal("iron")
                    .executes(context -> setItemType(context, Items.IRON_INGOT, Items.IRON_BLOCK, 9, false))
            )
            .then(CommandManager.literal("gold")
                    .executes(context -> setItemType(context, Items.GOLD_INGOT, Items.GOLD_BLOCK, 9, false))
            )
            .then(CommandManager.literal("emerald")
                    .executes(context -> setItemType(context, Items.EMERALD, Items.EMERALD_BLOCK, 9, false))
            )
            .then(CommandManager.literal("lapis")
                    .executes(context -> setItemType(context, Items.LAPIS_LAZULI, Items.LAPIS_BLOCK, 9, false))
            )
            .then(CommandManager.literal("copper")
                    .executes(context -> setItemType(context, Items.COPPER_INGOT, Items.COPPER_BLOCK, 9, false))
            )
            .then(CommandManager.literal("netherite")
                    .executes(context -> setItemType(context, Items.NETHERITE_INGOT, Items.NETHERITE_BLOCK, 9, false))
            )
            .then(CommandManager.literal("custom")
                    .then(argument("ingot", ItemStackArgumentType.itemStack(commandRegistryAccess))
                            .then(argument("block", ItemStackArgumentType.itemStack(commandRegistryAccess))
                                    .then(argument("ingotToBlockAmount", IntegerArgumentType.integer(1, 64))
                                            .executes(context -> setItemType(context,
                                                    ItemStackArgumentType.getItemStackArgument(context, "ingot").getItem(),
                                                    ItemStackArgumentType.getItemStackArgument(context, "block").getItem(),
                                                    IntegerArgumentType.getInteger(context, "ingotToBlockAmount"),
                                                    false))
                                    )
                            )
                            .executes(context -> setItemType(context,
                                    ItemStackArgumentType.getItemStackArgument(context, "ingot").getItem(),
                                    Items.BARRIER,
                                    9,
                                    true)
                            )
                    )
            )
            .then(CommandManager.literal("confirm")
                    .executes(SetItemTypeCommand::hardConfirm)
            )
        );

        //registers the command setbounty with the perameter player then calls the setBounty method
    }

    public static int setItemType(CommandContext<ServerCommandSource> context, Item ingot, Item block, int amount, boolean onlyIngot) {
        // Gets the source of the command assigns it to a ServerPlayerEntity object
        final ServerCommandSource source = context.getSource();
        final ServerPlayerEntity sender = source.getPlayer();

        // Checks if the sender is null, is so its from console; disallow
        if (sender == null) {
            source.sendFeedback(() -> Text.literal("You must be a player to use this command!"), false);
            return 0;
        }
        needsConfirm = false;
        if(!BountyDataManager.getBountyData().isEmpty()){
            source.sendFeedback(() -> Text.literal("There are bounties currently active! You can't change the item right now!"), false);
            source.sendFeedback(() -> Text.literal("To force the change type /bountyItemType confirm"), false);
            needsConfirm = true;
            itemIngot = ingot;
            itemBlock = block;
            SetItemTypeCommand.amount = amount;
            SetItemTypeCommand.onlyIngot = onlyIngot;
            return 0;
        }

        config.update("itemIngot", Registries.ITEM.getRawId(ingot));
        config.update("itemBlock", Registries.ITEM.getRawId(block));
        config.update("ingotToBlockAmount", amount);
        config.update("onlyIngot", onlyIngot);
        config.save();

        CommonMethods.updateConfigCommon();

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

        if(!needsConfirm){
            source.sendFeedback(() -> Text.literal("Nothing here to confirm"), false);
            return 0;
        }

        needsConfirm = false;
        config.update("itemIngot", Registries.ITEM.getRawId(itemIngot));
        config.update("itemBlock", Registries.ITEM.getRawId(itemBlock));
        config.update("ingotToBlockAmount", amount);
        config.update("onlyIngot", onlyIngot);
        config.save();

        CommonMethods.updateConfigCommon();

        source.sendFeedback(() -> Text.literal("Currency has been FORCED changed!! Notify your server members so they can stay in the know!"), true);
        return 0;
    }

}
