package net.mexicanminion.bountyhunt.util;

import java.util.UUID;

public class BountyData {

    UUID uuid;
    boolean hasBounty;
    boolean hasReward;
    int value;

    public BountyData(UUID uuid, boolean hasBounty, boolean hasReward, int value){
        this.uuid = uuid;
        this.hasBounty = hasBounty;
        this.hasReward = hasReward;
        this.value = value;
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

    public int getValue(){
        return value;
    }

    public void setHasBounty(boolean hasBounty){
        this.hasBounty = hasBounty;
    }

    public void setHasReward(boolean hasReward){
        this.hasReward = hasReward;
    }

    public void setValue(int value){
        this.value = value;
    }

}
