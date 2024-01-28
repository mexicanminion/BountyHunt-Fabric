package net.mexicanminion.bountyhunt.gui;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class SetBountyGUI extends SimpleGui {

    public int amount = 0;
    public boolean bountyNotConfirmed = true;
    ServerPlayerEntity playerTemp;
    /**
     * Constructs a new simple container gui for the supplied player.
     *
     * @param player                the player to server this gui to
     * @param manipulatePlayerSlots if <code>true</code> the players inventory
     *                              will be treated as slots of this gui
     */
    public SetBountyGUI(ServerPlayerEntity player, boolean manipulatePlayerSlots) {
        super(ScreenHandlerType.GENERIC_9X6, player, manipulatePlayerSlots);

        this.setLockPlayerInventory(false);
        this.setTitle(Text.of("Set Bounty"));
        this.reOpen = true;
        playerTemp = player;


        for(int i = 0; i < 54; i++){
            this.setSlot(i, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE).setName(Text.empty()));
        }

        this.setSlot(13, new GuiElementBuilder(Items.DIAMOND_SWORD).setName(Text.of("Select Amount")));

        this.setSlot(29, new GuiElementBuilder(Items.DIAMOND, 1).setName(Text.of("1")));
        this.setSlot(30, new GuiElementBuilder(Items.DIAMOND, 2).setName(Text.of("2")));
        this.setSlot(31, new GuiElementBuilder(Items.DIAMOND, 10).setName(Text.of("10")));
        this.setSlot(32, new GuiElementBuilder(Items.DIAMOND, 32).setName(Text.of("32")));
        this.setSlot(33, new GuiElementBuilder(Items.DIAMOND, 64).setName(Text.of("64"))
                .setCallback(((index, clickType, action) -> {
                    if(removeItemFromInventory(player, Items.DIAMOND, 64)){
                        player.sendMessage(Text.of("You have set a bounty of 64 diamonds on " + player.getDisplayName().getString()), false);
                    } else {
                        player.sendMessage(Text.of("You do not have enough diamonds to set a bounty of 64 on " + player.getDisplayName().getString()), false);
                    }
                })));


        this.setSlot(49, new GuiElementBuilder()
                .setItem(Items.BARRIER)
                .setName(Text.literal("Exit").setStyle(Style.EMPTY.withItalic(true)))
                .setCallback(((index, clickType, action) -> this.close())));

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
        playerTemp.sendMessage(Text.of("CLOSE " + player.getDisplayName().getString()), false);
        //playerTemp.getInventory().insertStack(new ItemStack(Items.DIAMOND, 1));
        if(bountyNotConfirmed){
            for(int i = 0; i < amount; i++){
                playerTemp.getInventory().insertStack(new ItemStack(Items.DIAMOND, 1));
            }
        }
    }


}
