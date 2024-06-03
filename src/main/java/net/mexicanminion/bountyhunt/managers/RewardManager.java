package net.mexicanminion.bountyhunt.managers;

import com.mojang.authlib.GameProfile;
import net.mexicanminion.bountyhunt.util.BountyDataImproved;

import java.util.UUID;

public class RewardManager {


    public static void setReward(UUID rewarder, boolean hasReward, int value, GameProfile gameProfile, String playerName) {
        if(BountyDataManager.getBountyData(rewarder) == null){
            BountyDataManager.setBountyData(new BountyDataImproved(rewarder, false, hasReward, 0, value, gameProfile, playerName));
            return;
        }
        BountyDataManager.getBountyData(rewarder).setHasReward(hasReward);
        BountyDataManager.getBountyData(rewarder).setRewardValue(value);
    }



    public static int getReward(UUID bounty) {
        if(BountyDataManager.getBountyData(bounty) == null || !BountyDataManager.getBountyData(bounty).getHasReward()){
            return 0;
        }
        return BountyDataManager.getBountyData(bounty).getRewardValue();
    }

}
