package net.mexicanminion.bountyhunt.managers;

import com.mojang.authlib.GameProfile;
import net.mexicanminion.bountyhunt.util.BountyData;

import java.io.*;
import java.util.UUID;

public class RewardManager {


    public static void setReward(UUID rewarder, boolean hasReward, int value, GameProfile gameProfile, String playerName) {
        if(BountyDataManager.getBountyData(rewarder) == null){
            BountyDataManager.setBountyData(rewarder, new BountyData(rewarder, false, hasReward, value, gameProfile, playerName));
            return;
        }
        BountyDataManager.getBountyData(rewarder).setHasReward(hasReward);
        BountyDataManager.getBountyData(rewarder).setValue(value);
    }



    public static int getReward(UUID bounty) {
        if(BountyDataManager.getBountyData(bounty) == null || !BountyDataManager.getBountyData(bounty).getHasReward()){
            return -1;
        }
        return BountyDataManager.getBountyData(bounty).getValue();
    }




    //public static HashMap<UUID, Integer> rewardMap = new HashMap<UUID, Integer>();

    /**
     * saveRewardFile()
     * Description: Save the reward file
     * @throws FileNotFoundException
     * @throws IOException
     */
    /*public void saveRewardFile() throws FileNotFoundException, IOException {
        File bountyDir = Paths.get("", "bountyhunt").toFile();
        File file = new File(bountyDir, "reward.dat");

        ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));

        try{
            output.writeObject(rewardMap);
            output.flush();
            output.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }*/

    /**
     * loadRewardFile()
     * Description: Load the reward file
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    /*public void loadRewardFile() throws FileNotFoundException, IOException, ClassNotFoundException {
        File bountyDir = Paths.get("", "bountyhunt").toFile();
        File file = new File(bountyDir, "reward.dat");

        if(file != null){
            ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
            Object readObject = input.readObject();
            input.close();

            if(!(readObject instanceof HashMap)){
                throw new IOException("Data is not a HashMap");
            }

            rewardMap = (HashMap<UUID, Integer>) readObject;
            for(UUID key : rewardMap.keySet()){
                rewardMap.put(key, rewardMap.get(key));
            }

        }

    }*/



    /**
     * setReward()
     * Description: Set the reward
     * @param uuid
     * @param amount
     */
    /*public static void setReward(UUID uuid, Integer amount) {
        rewardMap.put(uuid, amount);
    }
     */

    /**
     * getReward()
     * Description: Get the reward
     * @param uuid
     * @return
     */
    /*public static int getReward(UUID uuid) {
        if(rewardMap.get(uuid) == null) {
            return -1;
        }
        return rewardMap.get(uuid);
    }*/
}
