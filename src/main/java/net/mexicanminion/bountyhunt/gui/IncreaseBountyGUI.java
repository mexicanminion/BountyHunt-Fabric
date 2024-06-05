package net.mexicanminion.bountyhunt.gui;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.mexicanminion.bountyhunt.managers.BountyManager;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;
import java.util.UUID;

public class IncreaseBountyGUI extends SimpleGui {

    public MinecraftServer server;

    int currPage = 1;
    int maxPage;
    int currHead = 0;
    int maxHeadPerPage = 28;
    int currFirstHead = 0;
    int nextFirstHead = maxHeadPerPage;

    /**
     * Constructs a new simple container gui for the supplied player.
     *
     * @param player                the player to server this gui to
     * @param manipulatePlayerSlots if <code>true</code> the players inventory
     */
    public IncreaseBountyGUI(ServerPlayerEntity player, boolean manipulatePlayerSlots, ServerCommandSource contextServer) {
        super(ScreenHandlerType.GENERIC_9X6, player, manipulatePlayerSlots);

        server = contextServer.getServer();
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
                .setCallback(((index, clickType, action) -> prevPage())));

        setMaxPage();

        this.setSlot(50, new GuiElementBuilder()
                .setItem(Items.PLAYER_HEAD)
                .setName(Text.literal("Next Page").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .hideFlags()
                .setSkullOwner(PlayerHeads.RIGHT)
                .setCallback(((index, clickType, action) -> nextPage())));

        spawnHeads();
    }

    public void setMaxPage(){
        int BountyDataSize = BountyManager.getBountyData(player.getUuid()).getCreatedBounties().size();
        int maxPage = BountyDataSize / maxHeadPerPage;
        if(BountyDataSize % maxHeadPerPage != 0){
            maxPage++;
        }
        this.maxPage = maxPage;
    }

    public void nextPage(){
        if(currPage == maxPage){
            return;
        }
        currPage++;
        currFirstHead = nextFirstHead;
        currHead = nextFirstHead;
        spawnHeads();
    }

    public void prevPage(){
        if(currPage == 1){
            return;
        }
        currPage--;
        currHead = currFirstHead - maxHeadPerPage;
        spawnHeads();
    }

    public void spawnHeads(){
        List<UUID> setBountyList = BountyManager.getBountyData(player.getUuid()).getCreatedBounties();

        //check if bountyList empty
        if(setBountyList.isEmpty()){
            this.setSlot(22, new GuiElementBuilder(Items.PLAYER_HEAD)
                    .setName(Text.literal("No Bounties Available!").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                    .hideFlags());
            return;
        }

        currFirstHead = currHead;
        for (int i = 10; i <= 44; i++) {
            if(i == 17 || i == 26 || i == 35 || i == 44){
                i += 1;
                continue;
            }
            if(currHead > setBountyList.size()-1){
                //empty slot
                this.setSlot(i, new GuiElementBuilder(Items.AIR));
            }else {
                this.setSlot(i, new GuiElementBuilder(Items.PLAYER_HEAD)
                        .hideFlags()
                        .setSkullOwner(BountyManager.getBountyData(setBountyList.get(currHead)).getGameProfile(), server));
            }
            currHead++;
        }
        nextFirstHead = currHead+1;
    }
}
