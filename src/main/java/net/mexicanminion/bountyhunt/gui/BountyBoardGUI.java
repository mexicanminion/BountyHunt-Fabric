package net.mexicanminion.bountyhunt.gui;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.mexicanminion.bountyhunt.managers.BountyDataManager;
import net.mexicanminion.bountyhunt.util.BountyData;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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
                .setCallback(((index, clickType, action) -> prevPage())));

        setMaxPage();
        setPageButton();

        this.setSlot(50, new GuiElementBuilder()
                .setItem(Items.PLAYER_HEAD)
                .setName(Text.literal("Next Page").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .hideFlags()
                .setSkullOwner(PlayerHeads.RIGHT)
                .setCallback(((index, clickType, action) -> nextPage())));

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

    public void nextPage(){
        if(currPage == maxPage){
            return;
        }
        currPage++;
        currFirstHead = nextFirstHead;
        currHead = nextFirstHead;
        spawnHeads();
        setPageButton();
    }

    public void prevPage(){
        if(currPage == 1){
            return;
        }
        currPage--;
        currHead = currFirstHead - maxHeadPerPage;
        spawnHeads();
        setPageButton();
    }

    public void spawnHeads(){
        List<BountyData> bountyList = BountyDataManager.getBountyData();

        //check if bountyList empty
        if(bountyList.isEmpty()){
            this.setSlot(22, new GuiElementBuilder(Items.PLAYER_HEAD)
                    .setName(Text.literal("No Bounties Available!").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                    .hideFlags());
            return;
        }
        //server.getPlayerManager().getPlayer(bountyList.get(0).getUUID()).getEntityName(); GRABS FROM ONLINE PLAYERS ONLY, NOT FROM OFFLINE
        //server.getPlayerManager().getPlayer(bountyList.get(0).getUUID()).getGameProfile(); GRABS FROM ONLINE PLAYERS ONLY, NOT FROM OFFLINE

        currFirstHead = currHead;
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
                        .addLoreLine(getLoreValueAmount(bountyList.get(currHead).getBountyValue()))
                        .addLoreLine(getLoreOnlineState(bountyList.get(currHead).getPlayerName()))
                        .hideFlags()
                        .setSkullOwner(bountyList.get(currHead).getGameProfile(), server));
            }
            currHead++;
        }
        nextFirstHead = currHead+1;
    }

    private void setPageButton(){
        this.setSlot(49, new GuiElementBuilder()
                .setItem(Items.PLAYER_HEAD)
                .setName(Text.literal("Page: " + currPage + " of " + maxPage).setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .hideFlags()
                .setSkullOwner(PlayerHeads.INFO));
    }

    public Text getLoreValueAmount(int amount){
        MutableText amountText = Text.literal("");

        amountText.append(Text.literal("Amount: ").setStyle(Style.EMPTY.withItalic(true)))
                .append(Text.literal(amount + " Diamond(s)").formatted(Formatting.YELLOW));

        return amountText;
    }

    public Text getLoreOnlineState(String name){
        String[] onlinePlayers = server.getPlayerNames();
        MutableText onlineText = Text.literal("");

        for (String player : onlinePlayers) {
            if(player.equals(name)){
                onlineText.append(Text.literal("Online?: ").setStyle(Style.EMPTY.withItalic(true)))
                        .append(Text.literal("Yes").formatted(Formatting.GREEN));
                return onlineText;
            }
        }

        onlineText.append(Text.literal("Online?: ").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .append(Text.literal("No").formatted(Formatting.RED));

        return onlineText;
    }
}
