package net.mexicanminion.bountyhunt.util;

import com.mojang.authlib.GameProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

public class BountyDataImproved implements java.io.Serializable {

    UUID uuid;
    boolean hasBounty;
    boolean hasReward;
    int bountyValue;
    int rewardValue;
    UUID GPid;
    String GPname;
    String playerName;
    ArrayList<UUID> createdBounties;
    UUID bountier;

    public BountyDataImproved(String[] getData){
        this.putSaveData(getData);
    }

    public BountyDataImproved(UUID uuid, boolean hasBounty, boolean hasReward, int bountyValue, int rewardValue, GameProfile gameProfile, String playerName, UUID bountier){
        this.uuid = uuid;
        this.hasBounty = hasBounty;
        this.hasReward = hasReward;
        this.bountyValue = bountyValue;
        this.rewardValue = rewardValue;
        this.GPid = gameProfile.getId();
        this.GPname = gameProfile.getName();
        this.playerName = playerName;
        this.createdBounties = new ArrayList<UUID>();
        this.bountier = bountier;
    }

    public BountyDataImproved(){
        this.uuid = UUID.randomUUID();
        this.hasBounty = true;
        this.hasReward = true;
        this.bountyValue = 0;
        this.rewardValue = 0;
        this.GPid = UUID.randomUUID();
        this.GPname = "THIS IS A NULL PLAYER";
        this.playerName = "THIS IS A NULL PLAYER";
        this.createdBounties = new ArrayList<UUID>();
        this.bountier = UUID.randomUUID();
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

    public List<UUID> getCreatedBounties(){
        return this.createdBounties;
    }

    public UUID getBountier(){
        return this.bountier;
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

    public void setBountier(UUID uuid){
        this.bountier = uuid;
    }

    public void setCreatedBounties (ArrayList<UUID> uuidList){
        this.createdBounties = uuidList;
    }

    public boolean addToBountyList(UUID placedBounty){
        return createdBounties.add(placedBounty);
    }

    public boolean removeFromBountyList(UUID placedBounty){
        for (int i = 0; i < createdBounties.size(); i++) {
            UUID temp = createdBounties.get(i);
            if(temp == placedBounty){
                createdBounties.remove(i);
                return true;
            }
        }
        return false;
    }

    public String[] getSaveData(){
        String hasBountyString;
        String hasRewardString;
        String bountyListString = "";
        String bountierString = "";

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

        for (UUID uuid: createdBounties){
            bountyListString += (uuid.toString() + " ");
        }

        if(this.bountier == null){
            bountierString = "NA";
        }else{
            bountierString = bountier.toString();
        }

        String[] data = {uuid.toString(),
                hasBountyString,
                hasRewardString,
                String.valueOf(bountyValue),
                String.valueOf(rewardValue),
                GPid.toString(),
                GPname,
                playerName,
                bountyListString,
                bountierString};
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

        String[] uuidList = data[8].split(" ");
        this.createdBounties = new ArrayList<UUID>();
        for (String uuid: uuidList) {
            if(uuid.equalsIgnoreCase(" ") || uuid.equalsIgnoreCase("")){
                continue;
            }else{
                this.createdBounties.add(UUID.fromString(uuid));
            }
        }

        if(data[9].equalsIgnoreCase("NA")){
            this.bountier = null;
        }else{
            this.bountier = UUID.fromString(data[9]);
        }
    }

}
