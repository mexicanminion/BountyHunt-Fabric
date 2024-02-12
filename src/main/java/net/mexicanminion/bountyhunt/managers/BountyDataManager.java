package net.mexicanminion.bountyhunt.managers;

import net.mexicanminion.bountyhunt.util.BountyData;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class BountyDataManager {

    //public static List<BountyData> bountyData;
    public static HashMap<UUID,BountyData> bountyData = new HashMap<>();

    public void saveBountyDataFile() throws FileNotFoundException, IOException {
        File bountyDir = Paths.get("", "bountyhunt").toFile();
        File file = new File(bountyDir, "bountyData.dat");

        ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));

        try{
            output.writeObject(bountyData);
            output.flush();
            output.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * loadBountyDataFile()
     * Description: Load the bounty file
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void loadBountyDataFile() throws FileNotFoundException, IOException, ClassNotFoundException {
        File bountyDir = Paths.get("", "bountyhunt").toFile();
        File file = new File(bountyDir, "bountyData.dat");

        if(file != null){
            ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
            Object readObject = input.readObject();
            input.close();

            if(!(readObject instanceof HashMap)){
                throw new IOException("Data is not a HashMap");
            }

            bountyData = (HashMap<UUID, BountyData>) readObject;
            for(UUID key : bountyData.keySet()){
                bountyData.put(key, bountyData.get(key));
            }

        }
    }

    public static void setBountyData(UUID player, BountyData playerData){
        bountyData.put(player, playerData);
    }

    public static BountyData getBountyData(UUID player){
        if (bountyData.get(player) == null) {
            return null;
        }
        return bountyData.get(player);
    }

    /*public static int getBountyValue(UUID player){
        for(BountyData data : bountyData) {
            if (data.getUUID().equals(player)) {
                return data.getValue();
            }
        }
        return -1;
    }

    public static boolean getBountyStatus(UUID player){
        for(BountyData data : bountyData) {
            if (data.getUUID().equals(player)) {
                return data.getHasBounty();
            }
        }
        return false;
    }

    public static boolean getRewardStatus(UUID player){
        for(BountyData data : bountyData) {
            if (data.getUUID().equals(player)) {
                return data.getHasReward();
            }
        }
        return false;
    }

    public static void setBountyStatus(UUID player, boolean status){
        for(int i = 0; i < bountyData.size(); i++){
            if(bountyData.get(i).getUUID().equals(player)){
                bountyData.get(i).setHasBounty(status);
                return;
            }
        }
    }

    public static void setRewardStatus(UUID player, boolean status){
        for(int i = 0; i < bountyData.size(); i++){
            if(bountyData.get(i).getUUID().equals(player)){
                bountyData.get(i).setHasReward(status);
                return;
            }
        }
    }

    public static void setBountyValue(UUID player, int value){
        for(int i = 0; i < bountyData.size(); i++){
            if(bountyData.get(i).getUUID().equals(player)){
                bountyData.get(i).setValue(value);
                return;
            }
        }
    }

     */


}
