package net.mexicanminion.bountyhunt.gui;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.mexicanminion.bountyhunt.managers.BountyDataManager;
import net.mexicanminion.bountyhunt.util.BountyData;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;

public class BountyBoardGUI extends SimpleGui {

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
     *                              will be treated as slots of this gui
     */
    public BountyBoardGUI(ServerPlayerEntity player, boolean manipulatePlayerSlots, ServerCommandSource contextServer) {
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
                .setCallback(((index, clickType, action) -> {this.close();})));

        setMaxPage();
        this.setSlot(49, new GuiElementBuilder()
                .setItem(Items.PLAYER_HEAD)
                .setName(Text.literal("Page: " + currPage + " of " + maxPage).setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .hideFlags()
                .setSkullOwner(PlayerHeads.INFO)
                .setCallback(((index, clickType, action) -> this.close())));

        this.setSlot(50, new GuiElementBuilder()
                .setItem(Items.PLAYER_HEAD)
                .setName(Text.literal("Next Page").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .hideFlags()
                .setSkullOwner(PlayerHeads.RIGHT)
                .setCallback(((index, clickType, action) -> this.close())));

        spawnHeads();
    }

    public void setMaxPage(){
        int BountyDataSize = BountyDataManager.getActiveBountyAmount();
        int maxPage = BountyDataSize / maxHeadPerPage;
        if(BountyDataSize % maxHeadPerPage != 0){
            maxPage++;
        }
        this.maxPage = maxPage;
    }

    public void spawnHeads(){
        List<BountyData> bountyList = BountyDataManager.getBountyData();

        //check if bountyList is null or empty
        if(bountyList == null || bountyList.isEmpty()){
            this.setSlot(22, new GuiElementBuilder(Items.PLAYER_HEAD)
                    .setName(Text.literal("No Bounties Available!").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                    .hideFlags());
            return;
        }


        //server.getPlayerManager().getPlayer(bountyList.get(0).getUUID()).getEntityName(); GRABS FROM ONLINE PLAYERS ONLY, NOT FROM OFFLINE
        //server.getPlayerManager().getPlayer(bountyList.get(0).getUUID()).getGameProfile(); GRABS FROM ONLINE PLAYERS ONLY, NOT FROM OFFLINE

        /*for(int i = 0; i < bountyList.size(); i++){
            this.setSlot(i, new GuiElementBuilder(Items.PLAYER_HEAD)
                    .setName(Text.literal(bountyList.get(i).getPlayerName()).setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                    .hideFlags()
                    .setSkullOwner(bountyList.get(i).getGameProfile(), server));

        }*/

        for (int i = 10; i <= 44; i++) {
            if(i == 17 || i == 26 || i == 35 || i == 44){
                i += 1;
                continue;
            }
            if(currHead > bountyList.size()-1){
                //empty slot
                this.setSlot(i, new GuiElementBuilder(Items.AIR));
            }else {
                this.setSlot(i, new GuiElementBuilder(Items.PLAYER_HEAD)
                        .setName(Text.literal(bountyList.get(currHead).getPlayerName()).setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                        .hideFlags()
                        .setSkullOwner(bountyList.get(currHead).getGameProfile(), server));
            }
            currHead++;
        }
    }
}
