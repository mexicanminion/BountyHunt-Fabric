package net.mexicanminion.bountyhunt.gui;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.mexicanminion.bountyhunt.managers.BountyManager;
import net.mexicanminion.bountyhunt.util.CommonMethods;
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
    public boolean isEditing;
    public ServerCommandSource contextServer;
    public ServerPlayerEntity target;

    /**
     * Constructs a new simple container gui for the supplied player.
     *
     * @param player                the player to server this gui to
     * @param manipulatePlayerSlots if <code>true</code> the players inventory
     *                              will be treated as slots of this gui
     */
    public SetBountyGUI(ServerPlayerEntity player, boolean manipulatePlayerSlots, ServerCommandSource contextServer, ServerPlayerEntity target, boolean isEditing) {
        super(ScreenHandlerType.GENERIC_9X6, player, manipulatePlayerSlots);

        this.setLockPlayerInventory(false);
        this.reOpen = true;
        this.contextServer = contextServer;
        this.target = target;
        this.isEditing = isEditing;

        if(isEditing){
            this.setTitle(Text.of("Edit Bounty"));
        }else{
            this.setTitle(Text.of("Set Bounty"));
        }

        for(int i = 0; i < 54; i++){
            this.setSlot(i, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE).setName(Text.empty()));
        }

        updateSelectAmountButton(CommonMethods.itemIngot, CommonMethods.itemIngotName, CommonMethods.itemBlockName);

        purchaseType(false);

        this.setSlot(48, new GuiElementBuilder()
                .setItem(Items.LIME_CONCRETE)
                .setName(Text.literal("Confirm with " + amount + " "+CommonMethods.itemIngotName+"(s)?").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .setCallback(((index, clickType, action) -> confirmDiamondsUpdate())));

        if(isEditing){
            this.setSlot(50, new GuiElementBuilder()
                    .setItem(Items.BARRIER)
                    .setName(Text.literal("Back/Cancel").setStyle(Style.EMPTY.withItalic(true).withBold(true).withColor(Formatting.WHITE)))
                    .setCallback(((index, clickType, action) -> {
                        try {
                            this.close();
                            IncreaseBountyGUI increaseBountyGUI = new IncreaseBountyGUI(player, false, contextServer);
                            increaseBountyGUI.open();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    })));

        }else {
            this.setSlot(50, new GuiElementBuilder()
                    .setItem(Items.BARRIER)
                    .setName(Text.literal("Exit").setStyle(Style.EMPTY.withItalic(true).withBold(true).withColor(Formatting.WHITE)))
                    .setCallback(((index, clickType, action) -> this.close())));

        }
    }

    /**
     * confirmDiamondsUpdate()
     * Description: This method confirms the amount of diamonds,
     *              Runs when the player clicks the confirm button
     */
    public void confirmDiamondsUpdate(){
        if(isEditing){
            if(amount == BountyManager.getBountyValue(target.getUuid())){
                player.sendMessage(Text.of("You must add at least 1 " + CommonMethods.itemIngotName + " to increase a bounty"), true);
                return;
            }
        }else{
            if(amount == 0){
                player.sendMessage(Text.of("You must add at least 1 " + CommonMethods.itemIngotName + " to set a bounty"), true);
                return;
            }
        }
        //update the currency and set the bounty
        //CurrencyManager.setCurrency(target.getUuid(), amount);

        if(isEditing){
            int totalValue = BountyManager.getBountyValue(target.getUuid()) + amount;
            int diffValue;

            if(BountyManager.getBountyValue(target.getUuid()) > amount){
                diffValue = BountyManager.getBountyValue(target.getUuid()) - amount;
            }else if(BountyManager.getBountyValue(target.getUuid()) < amount){
                diffValue = amount - BountyManager.getBountyValue(target.getUuid());
            }else{
                diffValue = amount;
            }
            BountyManager.setBounty(target.getUuid(), true, totalValue, target.getGameProfile(), target.getName().getString(), player.getUuid());
            //send the title and subtitle to everyone on the server
            if(amount >= CommonMethods.announceAmount){
                for (ServerPlayerEntity players : contextServer.getServer().getPlayerManager().getPlayerList()) {
                    players.networkHandler.sendPacket(new TitleS2CPacket(Text.literal("Bounty increased on " + target.getName().getString()).formatted(Formatting.RED)));
                    players.networkHandler.sendPacket(new SubtitleS2CPacket(Text.literal("For the added amount of " + diffValue +  " " + CommonMethods.itemIngotName
                            + "(s), Total value of " + totalValue +  " " + CommonMethods.itemIngotName + "(s)").formatted(Formatting.YELLOW)));
                }
            }else {
                player.sendMessage(Text.of("Bounty adjusted on " + target.getName().getString() + " for the added amount of " + amount + " " + CommonMethods.itemIngotName + "(s), Total value of " + totalValue +  " " + CommonMethods.itemIngotName + "(s)" ), false);
                target.sendMessage(Text.of("The bounty on you has increased by " + amount + " " + CommonMethods.itemIngotName + "(s) totaling " + totalValue +  " " + CommonMethods.itemIngotName + "(s)"), false);
            }
        }else{
            BountyManager.setBounty(target.getUuid(), true, amount, target.getGameProfile(), target.getName().getString(), player.getUuid());
            BountyManager.addToBountyList(player.getUuid(), target.getUuid());

            //send the title and subtitle to everyone on the server
            if(amount >= CommonMethods.announceAmount){
                for (ServerPlayerEntity players : contextServer.getServer().getPlayerManager().getPlayerList()) {
                    players.networkHandler.sendPacket(new TitleS2CPacket(Text.literal("Bounty set on " + target.getName().getString()).formatted(Formatting.RED)));
                    players.networkHandler.sendPacket(new SubtitleS2CPacket(Text.literal("For the amount of " + amount +  " " + CommonMethods.itemIngotName + "(s)").formatted(Formatting.YELLOW)));
                }
            }else {
                player.sendMessage(Text.of("Bounty set on " + target.getName().getString() + " for the amount of " + amount + " " + CommonMethods.itemIngotName + "(s)"), false);
                target.sendMessage(Text.of("A bounty has been set on you for the amount of " + amount + " diamond(s)"), false);
            }
        }

        isPlayerDone = true;
        this.close();
    }

    /**
     * purchaseType()
     * Description: This method switches the purchase type between diamonds and diamond blocks
     *              Runs when the player clicks the diamond or diamond block item
     * @param swapMaterial: whether or not this is the first time the method is being called
     *                 (if it is, it will not switch the purchase type)
     */
    public void purchaseType(boolean swapMaterial){
        if (swapMaterial){
            enteringBlocks = !enteringBlocks;
        }

        if (enteringBlocks){
            updateSelectAmountButton(CommonMethods.itemBlock, CommonMethods.itemIngotName, CommonMethods.itemBlockName);

            validateAmount(29, CommonMethods.itemBlock,1, false);
            validateAmount(30, CommonMethods.itemBlock,2, false);
            validateAmount(31, CommonMethods.itemBlock,10, false);
            validateAmount(32, CommonMethods.itemBlock,32, false);
            validateAmount(33, CommonMethods.itemBlock,64, false);
        }

        else {
            updateSelectAmountButton(CommonMethods.itemIngot, CommonMethods.itemIngotName, CommonMethods.itemBlockName);

            validateAmount(29, CommonMethods.itemIngot,1, false);
            validateAmount(30, CommonMethods.itemIngot,2, false);
            validateAmount(31, CommonMethods.itemIngot,10, false);
            validateAmount(32, CommonMethods.itemIngot,32, false);
            validateAmount(33, CommonMethods.itemIngot,64, false);
        }


    }

    /**
     * validateAmount()
     * Description: This method validates the amount of diamonds or diamond blocks
     *              Runs when the player clicks the diamond or diamond block item
     * @param slot: the slot to set the item in
     * @param item: the item to set in the slot
     * @param count: the amount of the item to set in the slot
     * @param addDiamonds: whether or not to add the diamonds to the player's inventory
     */
    public void validateAmount(int slot, Item item,int count, boolean addDiamonds){
        int maxAmount = 2304; //2304 is 36 stacks of INGOTS which is the temp limit
        int ingotToBlockAmount = CommonMethods.ingotToBlockAmount;
        if(enteringBlocks){
            count *= ingotToBlockAmount;
            if(amount + count > maxAmount){
                this.setSlot(slot, new GuiElementBuilder(Items.RED_CONCRETE, count/ingotToBlockAmount).setName(Text.of("This would exceed the limit!")));
            }else {
                count /= ingotToBlockAmount;
                int finalCount = count;
                if (addDiamonds)
                    setDiamonds(count);
                this.setSlot(slot, new GuiElementBuilder(item, count).setName(Text.of(""+ count)).setCallback(((index, clickType, action) -> validateAmount(slot, item, finalCount, true))));
            }
        }else {
            if(amount + count > maxAmount){
                this.setSlot(slot, new GuiElementBuilder(Items.RED_CONCRETE, count).setName(Text.of("This would exceed the limit!")));
            }else {
                int finalCount = count;
                if (addDiamonds)
                    setDiamonds(count);
                this.setSlot(slot, new GuiElementBuilder(item, count).setName(Text.of(""+ count)).setCallback(((index, clickType, action) -> validateAmount(slot, item, finalCount, true))));
            }
        }
        if (addDiamonds)
            purchaseType(false);
    }

    public void setDiamonds(int dAmount){
        if(enteringBlocks){
            if (!removeItemFromInventory(player, CommonMethods.itemBlock, dAmount)) {
                player.sendMessage(Text.of("You do not have enough "+ CommonMethods.itemBlockName +"s to add " + dAmount + " to " + player.getDisplayName().getString() + "'s bounty"), true);
            }
        }else {
            if (!removeItemFromInventory(player, CommonMethods.itemIngot, dAmount)) {
                player.sendMessage(Text.of("You do not have enough " + CommonMethods.itemIngotName + "s to add " + dAmount + " to " + player.getDisplayName().getString() + "'s bounty"), false);
            }
        }

        this.setSlot(48, new GuiElementBuilder()
                .setItem(Items.LIME_CONCRETE)
                .setName(Text.literal("Confirm with " + amount + " "+CommonMethods.itemIngotName+"(s)?").setStyle(Style.EMPTY.withItalic(true).withBold(true).withColor(Formatting.WHITE)))
                .setCallback(((index, clickType, action) -> confirmDiamondsUpdate())));
    }

    /**
     * removeItemFromInventory()
     * Description: This method removes the specified item from the player's inventory
     * @param player: the player to remove the item from
     * @param itemToRemove: the item to remove
     * @param quantity: the amount of the item to remove
     * @return: whether or not the item was removed
     */
    public boolean removeItemFromInventory (ServerPlayerEntity player, Item itemToRemove, int quantity){
        int i = 0;
        if(player.getInventory().count(itemToRemove) >= quantity) {
            if(itemToRemove.equals(CommonMethods.itemIngot)){
                amount += quantity;
            } else {
                amount += quantity * CommonMethods.ingotToBlockAmount;
            }
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

    /**
     * onClose()
     * Description: This method runs when the player closes the GUI
     *              It returns the diamonds to the player's inventory
     */
    @Override
    public void onClose() {
        super.onClose();
        if(!isPlayerDone){
            for(int i = 0; i < amount; i++){
                if(player.getInventory().getEmptySlot() == -1){
                    player.dropItem(new ItemStack(CommonMethods.itemIngot, 1), true);
                }else {
                    player.getInventory().insertStack(new ItemStack(CommonMethods.itemIngot, 1));
                }
            }
        }
    }

    public void updateSelectAmountButton(Item mainItem, String ingotName, String blockName){
        if(CommonMethods.onlyIngot){
            this.setSlot(13, new GuiElementBuilder(mainItem)
                    .setName(Text.literal("Select Amount").setStyle(Style.EMPTY.withItalic(true).withBold(true).withColor(Formatting.WHITE)))
                    .setCallback(((index, clickType, action) -> {purchaseType(false);})));
        }
        else {
            this.setSlot(13, new GuiElementBuilder(mainItem)
                    .setName(Text.literal("Select Amount").setStyle(Style.EMPTY.withItalic(true).withBold(true).withColor(Formatting.WHITE)))
                    .addLoreLine(Text.literal("Click to switch between ").setStyle(Style.EMPTY.withItalic(true).withBold(false).withColor(Formatting.WHITE)))
                    .addLoreLine(Text.literal(ingotName + " and " + blockName).setStyle(Style.EMPTY.withItalic(true).withBold(false).withColor(Formatting.WHITE)))
                    .setCallback(((index, clickType, action) -> {purchaseType(true);})));

        }

    }

}
