package net.mexicanminion.bountyhunt.gui;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.mexicanminion.bountyhunt.managers.RewardManager;
import net.mexicanminion.bountyhunt.util.CommonMethods;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.mexicanminion.bountyhunt.BountyHuntMod.config;

public class ClaimBountyGUI extends SimpleGui {

    int dAmount = RewardManager.getReward(this.player.getUuid());

    /**
     * Constructs a new simple container gui for the supplied player.
     *
     *
     * @param player                the player to server this gui to
     * @param manipulatePlayerSlots if <code>true</code> the players inventory
     *                              will be treated as slots of this gui
     */
    public ClaimBountyGUI(ServerPlayerEntity player, boolean manipulatePlayerSlots, ServerCommandSource contextServer) {
        super(ScreenHandlerType.GENERIC_9X6, player, manipulatePlayerSlots);

        this.setLockPlayerInventory(false);
        this.setTitle(Text.of("Claim Bounty"));
        this.reOpen = true;

        for(int i = 0; i < 54; i++){
            this.setSlot(i, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE).setName(Text.empty()));
        }

        this.setSlot(3, new GuiElementBuilder(Items.LIME_CONCRETE)
                .setName(Text.literal("Reward: " + dAmount + " " + CommonMethods.itemIngotName + "(s)!").setStyle(Style.EMPTY.withItalic(true).withBold(true).withColor(Formatting.WHITE)))
                .addLoreLine(Text.literal("Make sure you have enough inventory space!").setStyle(Style.EMPTY.withItalic(true).withBold(false).withColor(Formatting.WHITE)))
                .setCallback(((index, clickType, action) -> {addDiamondsToPlayer();})));

        this.setSlot(5, new GuiElementBuilder()
                .setItem(Items.BARRIER)
                .setName(Text.literal("Exit").setStyle(Style.EMPTY.withItalic(true).withBold(true).withColor(Formatting.WHITE)))
                .addLoreLine(Text.literal("Coming back when you have more Inventory space?").setStyle(Style.EMPTY.withItalic(true).withBold(false).withColor(Formatting.WHITE)))
                .addLoreLine(Text.literal("Your reward will still be here!").setStyle(Style.EMPTY.withItalic(true).withBold(false).withColor(Formatting.WHITE)))
                .setCallback(((index, clickType, action) -> this.close())));

        addDiamondsToGUI();

    }

    /**
     * addDiamondsToGUI()
     * Description: This method adds the diamonds to the GUI
     *              based on the amount of diamonds the player has
     *              in their reward file. It will add the diamonds
     *              in stacks of 64 and then the remainder in the
     *              last slot. It adds from inside out, top to bottom
     * @return void
     */
    public void addDiamondsToGUI(){
        int dAmount = RewardManager.getReward(this.player.getUuid());
        int dStacks = 0;
        int dRemainder = 0;
        int stackAmount = 0;
        int stackRemainder = 0;
        int row = 0;

        //determine the amount of stacks and the remainder
        if(dAmount % 64 != 0){
            if (dAmount < 64){
                dStacks = 1;
                dRemainder = dAmount;
            }else {
                dStacks = (dAmount / 64)+1;
                dRemainder = dAmount % 64;
            }
        }else {
            dStacks = (dAmount / 64);
        }

        //stackAmount is used to keep track of the amount of stacks, mutable
        stackAmount = dStacks;

        //determine the amount of rows and the remainder
        if(dStacks % 9 != 0){
            if (dStacks < 9){
                row = 1;
                stackRemainder = dStacks;
            }else {
                row = (dStacks / 9)+1;
                stackRemainder = dStacks % 9;
            }
        }else {
            row = (dStacks / 9);
        }

        //determine if there are only stacks or some remainder and does the math
        if(dRemainder == 0){
            //if only stacks for the rows
            for(int i = 18; i <= 9 + (row*9); i += 9){
                if(stackAmount > stackRemainder){
                    //if working with full rows
                    // D********, DD*******, DDD******, etc VVVV
                    for(int j = i; j <= (i+8); j++){
                        if (stackAmount == 0){
                            break;
                        }else {
                            this.setSlot(j, new GuiElementBuilder(CommonMethods.itemIngot, 64));
                            stackAmount--;
                        }
                    }
                }else{
                    //if working with the remainder (not enough stacks for a full row)
                    // ****D****, ***DD****, ***DDD***, etc VVVV
                    for(int j = i + (4-(stackRemainder/2)); j <= (j + stackRemainder); j++){
                        if (stackAmount == 0){
                            //if there are no more stacks to add then break
                            break;
                        }else {
                            this.setSlot(j, new GuiElementBuilder(CommonMethods.itemIngot, 64));
                            stackAmount--;
                        }
                    }
                }
            }
        }else {
            //if there is some remainderfor the rows
            for(int i = 18; i <= 9 + (row*9); i += 9){
                //column
                if(stackAmount > stackRemainder){
                    //if working with full rows
                    // D********, DD*******, DDD******, etc VVVV
                    for(int j = i; j <= (i+8); j++){
                        if (stackAmount == 0){
                            break;
                        }
                        //if there is only one stack left then add the remainder (this should not happen in this case)
                        else if(stackAmount == 1){
                            this.setSlot(j, new GuiElementBuilder(CommonMethods.itemIngot, dRemainder));
                            break;
                        }else {
                            //add the stacks and decrement the stackAmount
                            this.setSlot(j, new GuiElementBuilder(CommonMethods.itemIngot, 64));
                            stackAmount--;
                        }
                    }
                }else{
                    //if working with the remainder (not enough stacks for a full row)
                    // ****D****, ***DD****, ***DDD***, etc VVVV
                    for(int j = i + (4-(stackRemainder/2)); j <= (j + stackRemainder); j++){
                        if (stackAmount == 0){
                            break;
                        }
                        //if there is only one stack left then add the remainder, then break
                        else if(stackAmount == 1){
                            this.setSlot(j, new GuiElementBuilder(CommonMethods.itemIngot, dRemainder));
                            break;
                        }else {
                            //add the stacks and decrement the stackAmount
                            this.setSlot(j, new GuiElementBuilder(CommonMethods.itemIngot, 64));
                            stackAmount--;
                        }
                    }
                }
            }
        }
    }

    /**
     * addDiamondsToPlayer()
     * Description: This method adds the diamonds to the player's inventory
     *              based on the amount of diamonds the player has in their
     *              reward file. It will add the diamonds in stacks of 64 and
     *              then the remainder in the last slot. It adds from inside out,
     *              top to bottom
     * @return void
     */
    public void addDiamondsToPlayer(){
        int dStacks = 0;
        int dRemainder = 0;
        int stackAmount = 0;

        //determine the amount of stacks and the remainder
        if(dAmount % 64 != 0){
            if (dAmount < 64){
                dStacks = 1;
                dRemainder = dAmount;
            }else {
                dStacks = (dAmount / 64)+1;
                dRemainder = dAmount % 64;
            }
        }else {
            dStacks = (dAmount / 64);
        }

        //stackAmount is used to keep track of the amount of stacks, mutable
        stackAmount = dStacks;

        //add the diamonds to the player's inventory
        for(int i = 0; i < dStacks; i++){
            //check if there is an empty slot in the player's inventory
            if(player.getInventory().getEmptySlot() != -1){
                //if there is only one stack left then add the remainder
                if(stackAmount == 1 && dRemainder != 0){
                    player.getInventory().setStack(player.getInventory().getEmptySlot(), new ItemStack(CommonMethods.itemIngot, dRemainder));
                } else {
                    player.getInventory().setStack(player.getInventory().getEmptySlot(), new ItemStack(CommonMethods.itemIngot, 64));
                }
                //decrement the stackAmount
                stackAmount--;
            } else {
                //if there is no empty slot then drop the diamonds
                if(stackAmount == 1 && dRemainder != 0){
                    player.dropItem(new ItemStack(CommonMethods.itemIngot, dRemainder), true);
                } else {
                    player.dropItem(new ItemStack(CommonMethods.itemIngot, 64), true);
                }
                //decrement the stackAmount
                stackAmount--;
            }
        }
        //set the reward to 0
        RewardManager.setReward(player.getUuid(), false, 0, player.getGameProfile(), player.getName().getString());//TODO marker again just incase it breaks, you know
        this.close();
    }
}
