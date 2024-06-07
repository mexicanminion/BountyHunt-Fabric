package net.mexicanminion.bountyhunt.managers;

import com.mojang.authlib.GameProfile;
import net.mexicanminion.bountyhunt.util.BountyDataImproved;
import java.util.UUID;


public class BountyManager {

    public static void setBounty(UUID bounty, boolean setBounty, int value, GameProfile gameProfile, String playerName, UUID bountier) {
        if(BountyDataManager.getBountyData(bounty) == null){
            BountyDataManager.setBountyData(new BountyDataImproved(bounty, setBounty, false, value, 0, gameProfile, playerName, bountier));
            return;
        }
        BountyDataManager.getBountyData(bounty).setHasBounty(setBounty);
        BountyDataManager.getBountyData(bounty).setBountyValue(value);
        BountyDataManager.getBountyData(bounty).setBountier(bountier);
    }

    public static void addToBountyList(UUID bountySet, UUID bountyHead) {
        BountyDataManager.getBountyData(bountySet).addToBountyList(bountyHead);
    }

    public static boolean removeFromBountyList(UUID bountySet, UUID bountyHead) {
        return BountyDataManager.getBountyData(bountySet).removeFromBountyList(bountyHead);
    }

    public static boolean getBounty(UUID bounty) {
        if(BountyDataManager.getBountyData(bounty) == null){
            return false;
        }
        return BountyDataManager.getBountyData(bounty).getHasBounty();
    }

    public static BountyDataImproved getBountyData(UUID bounty) {
        return BountyDataManager.getBountyData(bounty);
    }

    public static int getBountyValue(UUID bounty) {
        if(BountyDataManager.getBountyData(bounty) == null){
            return 0;
        }
        return BountyDataManager.getBountyData(bounty).getBountyValue();
    }

}
