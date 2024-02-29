package net.mexicanminion.bountyhunt.managers;

import com.mojang.authlib.GameProfile;
import net.mexicanminion.bountyhunt.util.BountyData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;


public class BountyManager {

    public static void setBounty(UUID bounty, boolean setBounty, int value, GameProfile gameProfile, String playerName) {
        if(BountyDataManager.getBountyData(bounty) == null){
            BountyDataManager.setBountyData(bounty, new BountyData(bounty, setBounty, false, value, 0, gameProfile, playerName));
            return;
        }
        BountyDataManager.getBountyData(bounty).setHasBounty(setBounty);
        BountyDataManager.getBountyData(bounty).setBountyValue(value);
    }



    public static boolean getBounty(UUID bounty) {
        if(BountyDataManager.getBountyData(bounty) == null){
            return false;
        }
        return BountyDataManager.getBountyData(bounty).getHasBounty();
    }

    public static int getBountyValue(UUID bounty) {
        if(BountyDataManager.getBountyData(bounty) == null){
            return 0;
        }
        return BountyDataManager.getBountyData(bounty).getBountyValue();
    }

}
