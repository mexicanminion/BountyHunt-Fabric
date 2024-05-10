package net.mexicanminion.bountyhunt.util;

import com.mojang.authlib.GameProfile;

import java.util.UUID;

public class BountyData implements java.io.Serializable {

    UUID uuid;
    boolean hasBounty;
    boolean hasReward;
    int bountyValue;
    int rewardValue;
    UUID GPid;
    String GPname;
    String playerName;

    public BountyData(String[] getData){
        this.putSaveData(getData);
    }

    public BountyData(UUID uuid, boolean hasBounty, boolean hasReward, int bountyValue, int rewardValue, GameProfile gameProfile, String playerName){
        this.uuid = uuid;
        this.hasBounty = hasBounty;
        this.hasReward = hasReward;
        this.bountyValue = bountyValue;
        this.rewardValue = rewardValue;
        this.GPid = gameProfile.getId();
        this.GPname = gameProfile.getName();
        this.playerName = playerName;
    }

    public UUID getUUID(){
        return uuid;
    }

    public boolean getHasBounty(){
        return hasBounty;
    }

    public boolean getHasReward(){
        return hasReward;
    }

    public int getBountyValue(){
        return bountyValue;
    }

    public int getRewardValue(){
        return rewardValue;
    }

    public GameProfile getGameProfile(){
        return new GameProfile(GPid, GPname);
    }

    public String getPlayerName(){
        return playerName;
    }

    public void setHasBounty(boolean hasBounty){
        this.hasBounty = hasBounty;
    }

    public void setHasReward(boolean hasReward){
        this.hasReward = hasReward;
    }

    public void setBountyValue(int value){
        this.bountyValue = value;
    }

    public void setRewardValue(int value){
        this.rewardValue = value;
    }

    public String[] getSaveData(){
        String hasBountyString;
        String hasRewardString;
        if(hasBounty){
            hasBountyString = "true";
        }else {
            hasBountyString = "false";
        }
        if(hasReward){
            hasRewardString = "true";
        }else {
            hasRewardString = "false";
        }

        String[] data = {uuid.toString(),
                hasBountyString,
                hasRewardString,
                String.valueOf(bountyValue),
                String.valueOf(rewardValue),
                GPid.toString(),
                GPname,
                playerName};
        return data;
    }

    public void putSaveData(String[] data){
        this.uuid = UUID.fromString(data[0]);
        if(data[1].equalsIgnoreCase("true")){
            this.hasBounty = true;
        }else {
            this.hasBounty = false;
        }
        if(data[2].equalsIgnoreCase("true")){
            this.hasReward = true;
        }else {
            this.hasReward = false;
        }
        this.bountyValue = Integer.parseInt(data[3]);
        this.rewardValue = Integer.parseInt(data[4]);
        this.GPid = UUID.fromString(data[5]);
        this.GPname = data[6];
        this.playerName = data[7];
    }

}
