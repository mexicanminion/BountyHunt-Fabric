package net.mexicanminion.bountyhunt.util;

import net.minecraft.item.Item;

import static net.mexicanminion.bountyhunt.BountyHuntMod.config;

public class CommonMethods {

    public static Item itemIngot = Item.byRawId(config.get("itemIngot"));
    public static Item itemBlock = Item.byRawId(config.get("itemBlock"));
    public static String itemIngotName = itemIngot.getName().getString();
    public static String itemBlockName = itemBlock.getName().getString();
}
