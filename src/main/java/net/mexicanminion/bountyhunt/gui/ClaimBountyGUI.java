package net.mexicanminion.bountyhunt.gui;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.mexicanminion.bountyhunt.managers.CurrencyManager;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class ClaimBountyGUI extends SimpleGui {

    public ServerPlayerEntity target;


    /**
     * Constructs a new simple container gui for the supplied player.
     *
     * @param player                the player to server this gui to
     * @param manipulatePlayerSlots if <code>true</code> the players inventory
     *                              will be treated as slots of this gui
     */
    public ClaimBountyGUI(ServerPlayerEntity player, boolean manipulatePlayerSlots, ServerCommandSource contextServer, ServerPlayerEntity target) {
        super(ScreenHandlerType.GENERIC_9X6, player, manipulatePlayerSlots);

        this.target = target;

        this.setLockPlayerInventory(false);
        this.setTitle(Text.of("Set Bounty"));
        this.reOpen = true;

        for(int i = 0; i < 54; i++){
            this.setSlot(i, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE).setName(Text.empty()));
        }

        this.setSlot(5, new GuiElementBuilder(Items.DIAMOND_SWORD)
                .setName(Text.literal("Reward: Diamonds").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .addLoreLine(Text.literal("Make sure you have enough inventory space!").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .hideFlags());


    }

    public void addDiamonds(){
        int dAmount = CurrencyManager.getCurrency(target.getUuid());
        int dStacks = dAmount / 64;
    }
}
