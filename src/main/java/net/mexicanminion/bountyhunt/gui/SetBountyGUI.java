package net.mexicanminion.bountyhunt.gui;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.mexicanminion.bountyhunt.managers.BountyManager;
import net.mexicanminion.bountyhunt.managers.CurrencyManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SetBountyGUI extends SimpleGui {

    public int amount = 0;
    public boolean isPlayerDone = false;
    public boolean enteringBlocks = false;
    public ServerCommandSource contextServer;
    public ServerPlayerEntity target;
    /**
     * Constructs a new simple container gui for the supplied player.
     *
     * @param player                the player to server this gui to
     * @param manipulatePlayerSlots if <code>true</code> the players inventory
     *                              will be treated as slots of this gui
     */
    public SetBountyGUI(ServerPlayerEntity player, boolean manipulatePlayerSlots, ServerCommandSource contextServer, ServerPlayerEntity target) {
        super(ScreenHandlerType.GENERIC_9X6, player, manipulatePlayerSlots);

        this.setLockPlayerInventory(false);
        this.setTitle(Text.of("Set Bounty"));
        this.reOpen = true;
        this.contextServer = contextServer;
        this.target = target;

        for(int i = 0; i < 54; i++){
            this.setSlot(i, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE).setName(Text.empty()));
        }

        this.setSlot(13, new GuiElementBuilder(Items.DIAMOND_SWORD)
                .setName(Text.literal("Select Amount").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .setCallback(((index, clickType, action) -> {purchaseType(false);})).hideFlags());

        purchaseType(true);

        this.setSlot(48, new GuiElementBuilder()
                .setItem(Items.LIME_CONCRETE)
                .setName(Text.literal("Confirm with " + amount + " diamonds?").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .setCallback(((index, clickType, action) -> confirmDiamondsUpdate())));

        this.setSlot(50, new GuiElementBuilder()
                .setItem(Items.BARRIER)
                .setName(Text.literal("Exit").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .setCallback(((index, clickType, action) -> this.close())));

    }

    public void confirmDiamondsUpdate(){
        CurrencyManager.setCurrency(target.getUuid(), amount);
        BountyManager.setBounty(target.getUuid(), true);
        for (ServerPlayerEntity players : contextServer.getServer().getPlayerManager().getPlayerList()) {
            players.networkHandler.sendPacket(new TitleS2CPacket(Text.literal("Bounty set on " + target.getEntityName()).formatted(Formatting.RED)));
            players.networkHandler.sendPacket(new SubtitleS2CPacket(Text.literal("For the amount of " + amount + " diamond(s)").formatted(Formatting.YELLOW)));
        }
        isPlayerDone = true;
        this.close();

    }

    public void purchaseType(boolean initial){
        if (!initial){
            enteringBlocks = !enteringBlocks;
        }

        if (enteringBlocks){
            this.setSlot(29, new GuiElementBuilder(Items.DIAMOND_BLOCK, 1).setName(Text.of("1")).setCallback(((index, clickType, action) -> setDiamonds(1))));
            this.setSlot(30, new GuiElementBuilder(Items.DIAMOND_BLOCK, 2).setName(Text.of("2")).setCallback(((index, clickType, action) -> setDiamonds(2))));
            this.setSlot(31, new GuiElementBuilder(Items.DIAMOND_BLOCK, 10).setName(Text.of("10")).setCallback(((index, clickType, action) -> setDiamonds(10))));
            this.setSlot(32, new GuiElementBuilder(Items.DIAMOND_BLOCK, 32).setName(Text.of("32")).setCallback(((index, clickType, action) -> setDiamonds(32))));
            this.setSlot(33, new GuiElementBuilder(Items.DIAMOND_BLOCK, 64).setName(Text.of("64")).setCallback(((index, clickType, action) -> setDiamonds(64))));
        }
        else {
            this.setSlot(29, new GuiElementBuilder(Items.DIAMOND, 1).setName(Text.of("1")).setCallback(((index, clickType, action) -> setDiamonds(1))));
            this.setSlot(30, new GuiElementBuilder(Items.DIAMOND, 2).setName(Text.of("2")).setCallback(((index, clickType, action) -> setDiamonds(2))));
            this.setSlot(31, new GuiElementBuilder(Items.DIAMOND, 10).setName(Text.of("10")).setCallback(((index, clickType, action) -> setDiamonds(10))));
            this.setSlot(32, new GuiElementBuilder(Items.DIAMOND, 32).setName(Text.of("32")).setCallback(((index, clickType, action) -> setDiamonds(32))));
            this.setSlot(33, new GuiElementBuilder(Items.DIAMOND, 64).setName(Text.of("64")).setCallback(((index, clickType, action) -> setDiamonds(64))));
        }
    }

    public void setDiamonds(int dAmount){
        if(enteringBlocks){
            dAmount *= 9;
            if(removeItemFromInventory(player, Items.DIAMOND_BLOCK, dAmount)){
                player.sendMessage(Text.of("You have added " + dAmount + " diamond blocks to " + player.getDisplayName().getString() + "'s bounty"), false);
            } else {
                player.sendMessage(Text.of("You do not have enough diamond blocks to add " + dAmount + " to " + player.getDisplayName().getString() + "'s bounty"), false);
            }
        }else {
            if(removeItemFromInventory(player, Items.DIAMOND, dAmount)){
                player.sendMessage(Text.of("You have added " + dAmount + " diamonds to " + player.getDisplayName().getString() + "'s bounty"), false);
            } else {
                player.sendMessage(Text.of("You do not have enough diamonds to add " + dAmount + " to " + player.getDisplayName().getString() + "'s bounty"), false);
            }
        }

        this.setSlot(48, new GuiElementBuilder()
                .setItem(Items.LIME_CONCRETE)
                .setName(Text.literal("Confirm with " + amount + " diamonds?").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .setCallback(((index, clickType, action) -> confirmDiamondsUpdate())));
    }

    public boolean removeItemFromInventory (ServerPlayerEntity player, Item itemToRemove, int quantity){
        int i = 0;
        if(player.getInventory().count(itemToRemove) >= quantity) {
            amount += quantity;
            while(quantity > 0){
                if(player.getInventory().getStack(i).getItem().equals(itemToRemove)){
                    if(player.getInventory().getStack(i).getCount() == quantity){
                        player.getInventory().removeStack(i);
                        quantity = 0;
                    } else if (player.getInventory().getStack(i).getCount() > quantity) {
                        ItemStack newItem = new ItemStack(itemToRemove, player.getInventory().getStack(i).getCount() - quantity);
                        player.getInventory().removeStack(i);
                        player.getInventory().setStack(i, newItem);
                        quantity = 0;
                    } else if (player.getInventory().getStack(i).getCount() < quantity) {
                        quantity -= player.getInventory().getStack(i).getCount();
                        player.getInventory().removeStack(i, player.getInventory().getStack(i).getCount());
                    }
                }
                i++;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        if(!isPlayerDone){
            for(int i = 0; i < amount; i++){
                player.getInventory().insertStack(new ItemStack(Items.DIAMOND, 1));
            }
        }

    }

}
