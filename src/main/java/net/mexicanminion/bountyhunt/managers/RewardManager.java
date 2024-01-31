package net.mexicanminion.bountyhunt.managers;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class RewardManager {

    public static HashMap<UUID, Integer> rewardMap = new HashMap<UUID, Integer>();


    public void saveRewardFile() throws FileNotFoundException, IOException {
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
    }

    public void loadRewardFile() throws FileNotFoundException, IOException, ClassNotFoundException {
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

    }

    public static void setReward(UUID uuid, Integer amount) {
        rewardMap.put(uuid, amount);
    }

    public static int getReward(UUID uuid) {
        if(rewardMap.get(uuid) == null) {
            return -1;
        }
        return rewardMap.get(uuid);
    }
}
