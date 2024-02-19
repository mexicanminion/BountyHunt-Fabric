package net.mexicanminion.bountyhunt.managers;

import com.mojang.authlib.GameProfile;
import net.mexicanminion.bountyhunt.util.BountyData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;


public class BountyManager {

    public static void setBounty(UUID bounty, boolean setBounty, int value, GameProfile gameProfile, String playerName) {
        if(BountyDataManager.getBountyData(bounty) == null){
            BountyDataManager.setBountyData(bounty, new BountyData(bounty, setBounty, BountyDataManager.getBountyData(bounty).getHasReward(), value, BountyDataManager.getBountyData(bounty).getRewardValue(), gameProfile, playerName));
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



    //public static HashMap<UUID,Boolean> bountyMap = new HashMap<>();

    /**
     * saveBountyFile()
     * Description: Save the bounty file
     * @throws FileNotFoundException
     * @throws IOException
     */
    /*public void saveBountyFile() throws FileNotFoundException, IOException {
        File bountyDir = Paths.get("", "bountyhunt").toFile();
        File file = new File(bountyDir, "bounty.dat");

        ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));

        try{
            output.writeObject(bountyMap);
            output.flush();
            output.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }*/

    /**
     * loadBountyFile()
     * Description: Load the bounty file
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    /*public void loadBountyFile() throws FileNotFoundException, IOException, ClassNotFoundException {
        File bountyDir = Paths.get("", "bountyhunt").toFile();
        File file = new File(bountyDir, "bounty.dat");

        if(file != null){
            ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
            Object readObject = input.readObject();
            input.close();

            if(!(readObject instanceof HashMap)){
                throw new IOException("Data is not a HashMap");
            }

            bountyMap = (HashMap<UUID, Boolean>) readObject;
            for(UUID key : bountyMap.keySet()){
                bountyMap.put(key, bountyMap.get(key));
            }

        }
    }*/

    /**
     * removeBounty()
     * Description: Remove the bounty buy setting it to null
     * @param bounty
     */
    /*public static void removeBounty(UUID bounty) {
        bountyMap.put(bounty, null);
    }
     */

    /**
     * setBounty()
     * Description: Set the bounty
     * @param bounty
     * @param setBounty
     */
    /*public static void setBounty(UUID bounty, boolean setBounty) {
        bountyMap.put(bounty, setBounty);
    }
     */

    /**
     * getBounty()
     * Description: Get the bounty status
     * @param bounty
     * @return
     */
    /*public static boolean getBounty(UUID bounty) {
        if (bountyMap.get(bounty) == null) {
            return false;
        }
        return bountyMap.get(bounty);
    }
     */

}
