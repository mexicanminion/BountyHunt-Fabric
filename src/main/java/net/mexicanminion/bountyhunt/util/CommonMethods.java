package net.mexicanminion.bountyhunt.util;

import net.minecraft.item.Item;

import static net.mexicanminion.bountyhunt.BountyHuntMod.config;

public class CommonMethods {

    //Get ingot and block items from config
    public static Item itemIngot = Item.byRawId(config.get("itemIngot"));
    public static Item itemBlock = Item.byRawId(config.get("itemBlock"));
    public static String itemIngotName = itemIngot.getName().getString();
    public static String itemBlockName = itemBlock.getName().getString();

    //Get values from config
    public static int ingotToBlockAmount = config.get("ingotToBlockAmount");
    public static int announceAmount = config.get("announceAmount");
    public static boolean onlyIngot = config.get("onlyIngot");

    public static void updateConfigCommon(){
        itemIngot = Item.byRawId(config.get("itemIngot"));
        itemBlock = Item.byRawId(config.get("itemBlock"));
        itemIngotName = itemIngot.getName().getString();
        itemBlockName = itemBlock.getName().getString();

        //Get values from config
        ingotToBlockAmount = config.get("ingotToBlockAmount");
        announceAmount = config.get("announceAmount");
        onlyIngot = config.get("onlyIngot");
    }
}
