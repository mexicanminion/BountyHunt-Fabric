package net.mexicanminion.bountyhunt.gui;

import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;

public class IncreaseBountyGUI extends SimpleGui {

    /**
     * Constructs a new simple container gui for the supplied player.
     *
     * @param player                the player to server this gui to
     * @param manipulatePlayerSlots if <code>true</code> the players inventory
     */
    public IncreaseBountyGUI(ServerPlayerEntity player, boolean manipulatePlayerSlots) {
        super(ScreenHandlerType.GENERIC_9X6, player, manipulatePlayerSlots);

    }
}
