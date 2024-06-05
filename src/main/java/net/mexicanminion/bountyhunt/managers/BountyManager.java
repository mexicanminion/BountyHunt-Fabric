package net.mexicanminion.bountyhunt.managers;

import com.mojang.authlib.GameProfile;
import net.mexicanminion.bountyhunt.util.BountyDataImproved;
import java.util.UUID;


public class BountyManager {

    public static void setBounty(UUID bounty, boolean setBounty, int value, GameProfile gameProfile, String playerName) {
        if(BountyDataManager.getBountyData(bounty) == null){
            //TODO: add the bountier UUID
            BountyDataManager.setBountyData(new BountyDataImproved(bounty, setBounty, false, value, 0, gameProfile, playerName));
            return;
        }
        BountyDataManager.getBountyData(bounty).setHasBounty(setBounty);
        BountyDataManager.getBountyData(bounty).setBountyValue(value);
    }

    public static void addToBountyList(UUID bountySet, UUID bountyHead) {
        BountyDataManager.getBountyData(bountySet).addToBountyList(bountyHead);
    }

    public static void removeFromBountyList(UUID bountySet, UUID bountyHead) {
        BountyDataManager.getBountyData(bountySet).removeFromBountyList(bountyHead);
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
