package net.mexicanminion.bountyhunt.gui;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class BountyBoardGUI extends SimpleGui {
    /**
     * Constructs a new simple container gui for the supplied player.
     *
     * @param player                the player to server this gui to
     * @param manipulatePlayerSlots if <code>true</code> the players inventory
     *                              will be treated as slots of this gui
     */
    public BountyBoardGUI(ServerPlayerEntity player, boolean manipulatePlayerSlots) {
        super(ScreenHandlerType.GENERIC_9X6, player, manipulatePlayerSlots);

        this.setLockPlayerInventory(false);
        this.setTitle(Text.of("Bounty Board"));
        this.reOpen = true;

        for(int i = 0; i < 54; i++){
            this.setSlot(i, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE).setName(Text.empty()).hideFlags());
        }

        this.setSlot(48, new GuiElementBuilder()
                .setItem(Items.PLAYER_HEAD)
                .setName(Text.literal("Back Page").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .hideFlags()
                .setSkullOwner(PlayerHeads.LEFT)
                .setCallback(((index, clickType, action) -> {this.close();})));

        this.setSlot(49, new GuiElementBuilder()
                .setItem(Items.PLAYER_HEAD)
                .setName(Text.literal("Page: ").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .hideFlags()
                .setSkullOwner(PlayerHeads.INFO)
                .setCallback(((index, clickType, action) -> this.close())));

        this.setSlot(50, new GuiElementBuilder()
                .setItem(Items.PLAYER_HEAD)
                .setName(Text.literal("Next Page").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .hideFlags()
                .setSkullOwner(PlayerHeads.RIGHT)
                .setCallback(((index, clickType, action) -> this.close())));
    }
}
