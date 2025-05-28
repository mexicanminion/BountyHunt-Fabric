package net.mexicanminion.bountyhunt.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class LoreLines {

    public static void LoreLines(){}

    public static  Text getLoreValueAmount(int amount){
        MutableText amountText = Text.literal("");
        amountText.append(Text.literal("Amount: ").setStyle(Style.EMPTY.withItalic(true).withBold(true).withColor(Formatting.WHITE)))
                .append(Text.literal(amount + " " + CommonMethods.itemIngotName +"(s)").formatted(Formatting.YELLOW));

        return amountText;
    }

    public static Text getLoreOnlineState(MinecraftServer server, String name){
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
}
