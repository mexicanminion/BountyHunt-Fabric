package net.mexicanminion.bountyhunt.gui;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.mexicanminion.bountyhunt.managers.CurrencyManager;
import net.mexicanminion.bountyhunt.managers.RewardManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class ClaimBountyGUI extends SimpleGui {

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

        this.setSlot(4, new GuiElementBuilder(Items.DIAMOND_SWORD)
                .setName(Text.literal("Reward: Diamonds").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .addLoreLine(Text.literal("Make sure you have enough inventory space!").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .hideFlags()
                .setCallback(((index, clickType, action) -> {RewardManager.setReward(player.getUuid(), 0);})));

        addDiamonds();

    }

    public void addDiamonds(){
        int dAmount = RewardManager.getReward(this.player.getUuid());
        int dStacks = 0;
        int dRemainder = 0;
        int stackAmount = 0;
        int row = 0;
        int rowRemainder = 0;

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
                rowRemainder = dStacks;
            }else {
                row = (dStacks / 9)+1;
                rowRemainder = dStacks % 9;
            }
        }else {
            row = (dStacks / 9);
        }

        if(dRemainder == 0){
            for(int i = 18 + (4-(dStacks/2)); i < 23 + (dStacks/2); i++){
                this.setSlot(i, new GuiElementBuilder(Items.DIAMOND, 64));
            }
        }else {
            //row
            for(int i = 18; i <= 9 + (row*9); i += 9){
                //column
                for(int j = i + (4-(dStacks/2)); j < 23 + (dStacks/2); i++){
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
