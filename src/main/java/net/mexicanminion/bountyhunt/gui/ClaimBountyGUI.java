package net.mexicanminion.bountyhunt.gui;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.mexicanminion.bountyhunt.managers.RewardManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class ClaimBountyGUI extends SimpleGui {

    int dAmount = RewardManager.getReward(this.player.getUuid());

    /**
     * Constructs a new simple container gui for the supplied player.
     *
     * @param player                the player to server this gui to
     * @param manipulatePlayerSlots if <code>true</code> the players inventory
     *                              will be treated as slots of this gui
     */
    public ClaimBountyGUI(ServerPlayerEntity player, boolean manipulatePlayerSlots, ServerCommandSource contextServer) {
        super(ScreenHandlerType.GENERIC_9X6, player, manipulatePlayerSlots);

        this.setLockPlayerInventory(false);
        this.setTitle(Text.of("Set Bounty"));
        this.reOpen = true;

        for(int i = 0; i < 54; i++){
            this.setSlot(i, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE).setName(Text.empty()));
        }

        this.setSlot(3, new GuiElementBuilder(Items.LIME_CONCRETE)
                .setName(Text.literal("Reward: " + dAmount + " diamonds!").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .addLoreLine(Text.literal("Make sure you have enough inventory space!").setStyle(Style.EMPTY.withItalic(true).withBold(false)))
                .hideFlags()
                .setCallback(((index, clickType, action) -> {addDiamondsToPlayer();})));

        this.setSlot(5, new GuiElementBuilder()
                .setItem(Items.BARRIER)
                .setName(Text.literal("Exit").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .addLoreLine(Text.literal("Coming back when you have more Inventory space?").setStyle(Style.EMPTY.withItalic(true).withBold(false)))
                .addLoreLine(Text.literal("Your reward will still be here!").setStyle(Style.EMPTY.withItalic(true).withBold(false)))
                .setCallback(((index, clickType, action) -> this.close())));


        addDiamondsToGUI();

    }

    public void addDiamondsToGUI(){
        int dAmount = RewardManager.getReward(this.player.getUuid());
        int dStacks = 0;
        int dRemainder = 0;
        int stackAmount = 0;
        int stackRemainder = 0;
        int row = 0;

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

        stackAmount = dStacks;

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

        if(dRemainder == 0){
            /*for(int i = 18 + (4-(dStacks/2)); i <= i + dStacks; i++){
                this.setSlot(i, new GuiElementBuilder(Items.DIAMOND, 64));
            }*/
            for(int i = 18; i <= 9 + (row*9); i += 9){
                player.sendMessage(Text.of("Row: " + i), false);
                //column
                if(stackAmount > stackRemainder){
                    player.sendMessage(Text.of("Some row"), false);
                    for(int j = i; j <= (i+8); j++){
                        player.sendMessage(Text.of("Slot: " + j), false);
                        if (stackAmount == 0){
                            break;
                        }else {
                            this.setSlot(j, new GuiElementBuilder(Items.DIAMOND, 64));
                            stackAmount--;
                        }
                    }
                }else{
                    player.sendMessage(Text.of("Final row"), false);
                    for(int j = i + (4-(stackRemainder/2)); j <= (j + stackRemainder); j++){
                        player.sendMessage(Text.of("Slot: " + j), false);
                        if (stackAmount == 0){
                            break;
                        }else {
                            this.setSlot(j, new GuiElementBuilder(Items.DIAMOND, 64));
                            stackAmount--;
                        }
                    }
                }
            }
        }else {
            //row
            for(int i = 18; i <= 9 + (row*9); i += 9){
                player.sendMessage(Text.of("Row: " + i), false);
                //column
                if(stackAmount > stackRemainder){
                    player.sendMessage(Text.of("Some row"), false);
                    for(int j = i; j <= (i+8); j++){
                        player.sendMessage(Text.of("Slot: " + j), false);
                        if (stackAmount == 0){
                            break;
                        }
                        else if(stackAmount == 1){
                            this.setSlot(j, new GuiElementBuilder(Items.DIAMOND, dRemainder));
                            break;
                        }else {
                            this.setSlot(j, new GuiElementBuilder(Items.DIAMOND, 64));
                            stackAmount--;
                        }
                    }
                }else{
                    player.sendMessage(Text.of("Final row"), false);
                    for(int j = i + (4-(stackRemainder/2)); j <= (j + stackRemainder); j++){
                        player.sendMessage(Text.of("Slot: " + j), false);
                        if (stackAmount == 0){
                            break;
                        }
                        else if(stackAmount == 1){
                            this.setSlot(j, new GuiElementBuilder(Items.DIAMOND, dRemainder));
                            break;
                        }else {
                            this.setSlot(j, new GuiElementBuilder(Items.DIAMOND, 64));
                            stackAmount--;
                        }
                    }
                }
            }
        }

    }

    public void addDiamondsToPlayer(){
        int dStacks = 0;
        int dRemainder = 0;
        int stackAmount = 0;
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
        stackAmount = dStacks;

        for(int i = 0; i < dStacks; i++){

            if(player.getInventory().getEmptySlot() != -1){
                if(stackAmount == 1 && dRemainder != 0){
                    player.getInventory().setStack(player.getInventory().getEmptySlot(), new ItemStack(Items.DIAMOND, dRemainder));
                } else {
                    player.getInventory().setStack(player.getInventory().getEmptySlot(), new ItemStack(Items.DIAMOND, 64));
                }
                stackAmount--;
            } else {
                if(stackAmount == 1 && dRemainder != 0){
                    player.dropItem(new ItemStack(Items.DIAMOND, dRemainder), true);
                } else {
                    player.dropItem(new ItemStack(Items.DIAMOND, 64), true);
                }
                stackAmount--;
            }
        }
        RewardManager.setReward(player.getUuid(), 0);
        this.close();
    }
}
