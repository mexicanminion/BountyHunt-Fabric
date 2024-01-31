package net.mexicanminion.bountyhunt.gui;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.mexicanminion.bountyhunt.managers.BountyManager;
import net.mexicanminion.bountyhunt.managers.CurrencyManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SetBountyGUI extends SimpleGui {

    public int amount = 0;
    public boolean isPlayerDone = false;
    public boolean enteringBlocks = false;
    public ServerCommandSource contextServer;
    public ServerPlayerEntity target;
    /**
     * Constructs a new simple container gui for the supplied player.
     *
     * @param player                the player to server this gui to
     * @param manipulatePlayerSlots if <code>true</code> the players inventory
     *                              will be treated as slots of this gui
     */
    public SetBountyGUI(ServerPlayerEntity player, boolean manipulatePlayerSlots, ServerCommandSource contextServer, ServerPlayerEntity target) {
        super(ScreenHandlerType.GENERIC_9X6, player, manipulatePlayerSlots);

        this.setLockPlayerInventory(false);
        this.setTitle(Text.of("Set Bounty"));
        this.reOpen = true;
        this.contextServer = contextServer;
        this.target = target;

        for(int i = 0; i < 54; i++){
            this.setSlot(i, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE).setName(Text.empty()));
        }

        //todo: make block and item so player knwows what they are adding when red condcr2qr
        this.setSlot(13, new GuiElementBuilder(Items.DIAMOND_SWORD)
                .setName(Text.literal("Select Amount").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .setCallback(((index, clickType, action) -> {purchaseType(false);})).hideFlags());

        purchaseType(true);

        this.setSlot(48, new GuiElementBuilder()
                .setItem(Items.LIME_CONCRETE)
                .setName(Text.literal("Confirm with " + amount + " diamonds?").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .setCallback(((index, clickType, action) -> confirmDiamondsUpdate())));

        this.setSlot(50, new GuiElementBuilder()
                .setItem(Items.BARRIER)
                .setName(Text.literal("Exit").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .setCallback(((index, clickType, action) -> this.close())));

    }

    public void confirmDiamondsUpdate(){
        CurrencyManager.setCurrency(target.getUuid(), amount);
        BountyManager.setBounty(target.getUuid(), true);
        for (ServerPlayerEntity players : contextServer.getServer().getPlayerManager().getPlayerList()) {
            players.networkHandler.sendPacket(new TitleS2CPacket(Text.literal("Bounty set on " + target.getEntityName()).formatted(Formatting.RED)));
            players.networkHandler.sendPacket(new SubtitleS2CPacket(Text.literal("For the amount of " + amount + " diamond(s)").formatted(Formatting.YELLOW)));
        }
        isPlayerDone = true;
        this.close();

    }

    public void purchaseType(boolean initial){
        if (!initial){
            enteringBlocks = !enteringBlocks;
        }

        //todo: Redconcrete is does not spawn in once valideAmount is false
        validateAmount(29,1, true);
        validateAmount(30,2, true);
        validateAmount(31,10, true);
        validateAmount(32,32, true);
        validateAmount(33,64, true);

        if (enteringBlocks){
            this.setSlot(29, new GuiElementBuilder(Items.DIAMOND_BLOCK, 1).setName(Text.of("1")).setCallback(((index, clickType, action) -> validateAmount(29, 1, false))));
            this.setSlot(30, new GuiElementBuilder(Items.DIAMOND_BLOCK, 2).setName(Text.of("2")).setCallback(((index, clickType, action) -> validateAmount(30, 2, false))));
            this.setSlot(31, new GuiElementBuilder(Items.DIAMOND_BLOCK, 10).setName(Text.of("10")).setCallback(((index, clickType, action) -> validateAmount(31, 10, false))));
            this.setSlot(32, new GuiElementBuilder(Items.DIAMOND_BLOCK, 32).setName(Text.of("32")).setCallback(((index, clickType, action) -> validateAmount(32, 32, false))));
            this.setSlot(33, new GuiElementBuilder(Items.DIAMOND_BLOCK, 64).setName(Text.of("64")).setCallback(((index, clickType, action) -> validateAmount(33, 64, false))));
        }
        else {
            this.setSlot(29, new GuiElementBuilder(Items.DIAMOND, 1).setName(Text.of("1")).setCallback(((index, clickType, action) -> validateAmount(29, 1, false))));
            this.setSlot(30, new GuiElementBuilder(Items.DIAMOND, 2).setName(Text.of("2")).setCallback(((index, clickType, action) -> validateAmount(30, 2, false))));
            this.setSlot(31, new GuiElementBuilder(Items.DIAMOND, 10).setName(Text.of("10")).setCallback(((index, clickType, action) -> validateAmount(31, 10, false))));
            this.setSlot(32, new GuiElementBuilder(Items.DIAMOND, 32).setName(Text.of("32")).setCallback(((index, clickType, action) -> validateAmount(32, 32, false))));
            this.setSlot(33, new GuiElementBuilder(Items.DIAMOND, 64).setName(Text.of("64")).setCallback(((index, clickType, action) -> validateAmount(33, 64, false))));
        }

        validateAmount(29,1, true);
        validateAmount(30,2, true);
        validateAmount(31,10, true);
        validateAmount(32,32, true);
        validateAmount(33,64, true);
    }

    public void validateAmount(int slot, int count, boolean check){
        if(enteringBlocks){
            count *= 9;
            if(amount + count > 2304){ //2304 is 36 stacks which is the temp limit
                this.setSlot(slot, new GuiElementBuilder(Items.RED_CONCRETE, count/9).setName(Text.of("This would exceed the limit!")));
                return;
            }
            count /= 9;
        }else {
            if(amount + count > 2304){ //2304 is 36 stacks which is the temp limit
                this.setSlot(slot, new GuiElementBuilder(Items.RED_CONCRETE, count).setName(Text.of("This would exceed the limit!")));
                return;
            }
        }

        if(!check)
            setDiamonds(count);
    }

    public void setDiamonds(int dAmount){
        if(enteringBlocks){
            //dAmount *= 9;
            if(removeItemFromInventory(player, Items.DIAMOND_BLOCK, dAmount)){
                player.sendMessage(Text.of("You have added " + dAmount + " diamond blocks to " + player.getDisplayName().getString() + "'s bounty"), false);
            } else {
                player.sendMessage(Text.of("You do not have enough diamond blocks to add " + dAmount + " to " + player.getDisplayName().getString() + "'s bounty"), false);
            }
        }else {
            if(removeItemFromInventory(player, Items.DIAMOND, dAmount)){
                player.sendMessage(Text.of("You have added " + dAmount + " diamonds to " + player.getDisplayName().getString() + "'s bounty"), false);
            } else {
                player.sendMessage(Text.of("You do not have enough diamonds to add " + dAmount + " to " + player.getDisplayName().getString() + "'s bounty"), false);
            }
        }

        this.setSlot(48, new GuiElementBuilder()
                .setItem(Items.LIME_CONCRETE)
                .setName(Text.literal("Confirm with " + amount + " diamonds?").setStyle(Style.EMPTY.withItalic(true).withBold(true)))
                .setCallback(((index, clickType, action) -> confirmDiamondsUpdate())));
    }

    public boolean removeItemFromInventory (ServerPlayerEntity player, Item itemToRemove, int quantity){
        int i = 0;
        if(player.getInventory().count(itemToRemove) >= quantity) {
            if(itemToRemove.equals(Items.DIAMOND)){
                amount += quantity;
            } else {
                amount += quantity * 9;
            }
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
        if(!isPlayerDone){
            for(int i = 0; i < amount; i++){
                player.getInventory().insertStack(new ItemStack(Items.DIAMOND, 1));
            }
        }

    }

}
