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

        if(dAmount % 64 != 0){
            if (dAmount < 64){
                dStacks = 0;
                dRemainder = dAmount;
                stackAmount = 1;
            }else {
                dStacks = (dAmount / 64);
                dRemainder = dAmount % 64;
                stackAmount = dStacks+1;
            }
        }else {
            dStacks = (dAmount / 64);
            stackAmount = dStacks+1;
        }

        if(dRemainder == 0){
            for(int i = 22-dStacks; i < dStacks + 23 ; i++){
                this.setSlot(i, new GuiElementBuilder(Items.DIAMOND, 64));
            }
        }else {
            for(int i = 22-dStacks; i < dStacks + 23 ; i++){
                if(stackAmount == 1){
                    this.setSlot(i, new GuiElementBuilder(Items.DIAMOND, dRemainder));
                    break;
                }else {
                    this.setSlot(i, new GuiElementBuilder(Items.DIAMOND, 64));
                    stackAmount--;
                }
            }
        }

    }
}
