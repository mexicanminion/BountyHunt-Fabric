package net.mexicanminion.bountyhunt.gui;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.mexicanminion.bountyhunt.managers.BountyManager;
import net.mexicanminion.bountyhunt.util.CommonMethods;
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
import java.util.UUID;

public class IncreaseBountyGUI extends SimpleGui {

    public MinecraftServer server;
    private ServerCommandSource contextServer;

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

        this.contextServer = contextServer;
        server = contextServer.getServer();
        this.setLockPlayerInventory(false);
        this.setTitle(Text.of("Press Heads to Change Values!"));
        this.reOpen = true;

        for(int i = 0; i < 54; i++){
            this.setSlot(i, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE).setName(Text.empty()));
        }

        this.setSlot(48, new GuiElementBuilder()
                .setItem(Items.PLAYER_HEAD)
                .setName(Text.literal("Back Page").setStyle(Style.EMPTY.withItalic(true).withBold(true).withColor(Formatting.WHITE)))
                .setSkullOwner(PlayerHeads.LEFT)
                .setCallback(((index, clickType, action) -> prevPage())));

        setMaxPage();
        setPageButton();

        this.setSlot(50, new GuiElementBuilder()
                .setItem(Items.PLAYER_HEAD)
                .setName(Text.literal("Next Page").setStyle(Style.EMPTY.withItalic(true).withBold(true).withColor(Formatting.WHITE)))
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

    private void setPageButton(){
        this.setSlot(49, new GuiElementBuilder()
                .setItem(Items.PLAYER_HEAD)
                .setName(Text.literal("Page: " + currPage + " of " + maxPage).setStyle(Style.EMPTY.withItalic(true).withBold(true).withColor(Formatting.WHITE)))
                .setSkullOwner(PlayerHeads.INFO));
    }

    public void spawnHeads(){
        List<UUID> setBountyList = BountyManager.getBountyData(player.getUuid()).getCreatedBounties();

        //check if bountyList empty
        if(setBountyList.isEmpty()){
            this.setSlot(22, new GuiElementBuilder(Items.PLAYER_HEAD)
                    .setName(Text.literal("No Bounties Available!").setStyle(Style.EMPTY.withItalic(true).withBold(true).withColor(Formatting.WHITE))));
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
                if(getOnlineState(BountyManager.getBountyData(setBountyList.get(currHead)).getPlayerName())){
                    this.setSlot(i, new GuiElementBuilder(Items.PLAYER_HEAD)
                            .setCallback(((index, clickType, action) -> openSetBountyGUI(setBountyList, index)))
                            .setName(Text.literal(BountyManager.getBountyData(setBountyList.get(currHead)).getPlayerName()).setStyle(Style.EMPTY.withItalic(true).withBold(true).withColor(Formatting.WHITE)))
                            .addLoreLine(getLoreValueAmount(BountyManager.getBountyData(setBountyList.get(currHead)).getBountyValue()))
                            .addLoreLine(getLoreOnlineState(BountyManager.getBountyData(setBountyList.get(currHead)).getPlayerName()))
                            .setSkullOwner(BountyManager.getBountyData(setBountyList.get(currHead)).getGameProfile(), server));
                }else{
                    this.setSlot(i, new GuiElementBuilder(Items.PLAYER_HEAD)
                            .setName(Text.literal(BountyManager.getBountyData(setBountyList.get(currHead)).getPlayerName()).setStyle(Style.EMPTY.withItalic(true).withBold(true).withColor(Formatting.WHITE)))
                            .addLoreLine(getLoreValueAmount(BountyManager.getBountyData(setBountyList.get(currHead)).getBountyValue()))
                            .addLoreLine(getLoreOnlineState(BountyManager.getBountyData(setBountyList.get(currHead)).getPlayerName()))
                            .addLoreLine(Text.literal("You can't change the bounty").setStyle(Style.EMPTY.withItalic(true).withBold(true).withColor(Formatting.RED)))
                            .addLoreLine(Text.literal("when the player is offline!").setStyle(Style.EMPTY.withItalic(true).withBold(true).withColor(Formatting.RED)))
                            .setSkullOwner(BountyManager.getBountyData(setBountyList.get(currHead)).getGameProfile(), server));
                }

            }
            currHead++;
        }
        nextFirstHead = currHead+1;
    }

    public Text getLoreValueAmount(int amount){
        MutableText amountText = Text.literal("");

        amountText.append(Text.literal("Amount: ").setStyle(Style.EMPTY.withItalic(true).withBold(true).withColor(Formatting.WHITE)))
                .append(Text.literal(amount + " " + CommonMethods.itemIngotName +"(s)").formatted(Formatting.YELLOW));

        return amountText;
    }

    public Text getLoreOnlineState(String name){
        String[] onlinePlayers = server.getPlayerNames();
        MutableText onlineText = Text.literal("");

        for (String player : onlinePlayers) {
            if(player.equals(name)){
                onlineText.append(Text.literal("Online?: ").setStyle(Style.EMPTY.withItalic(true).withBold(true).withColor(Formatting.WHITE)))
                        .append(Text.literal("Yes").formatted(Formatting.GREEN));
                return onlineText;
            }
        }

        onlineText.append(Text.literal("Online?: ").setStyle(Style.EMPTY.withItalic(true).withBold(true).withColor(Formatting.WHITE)))
                .append(Text.literal("No").formatted(Formatting.RED));

        return onlineText;
    }

    public boolean getOnlineState(String name){
        for (String player : server.getPlayerNames()) {
            if(player.equals(name)){
                return true;
            }
        }
        return false;
    }

    public void openSetBountyGUI (List<UUID> heads, int target){
        if(target > 37){
            target -= 6;
        }else if(target > 27){
            target -= 4;
        }else if(target > 18){
            target -= 2;
        }
        target -= 10;
        try {
            SetBountyGUI bountyGUI = new SetBountyGUI(player, false, contextServer, server.getPlayerManager().getPlayer(heads.get(target)), true);
            bountyGUI.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
