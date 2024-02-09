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

    public static List<BountyData> bountyData;

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

            if(!(readObject instanceof List)){
                throw new IOException("Data is not a BountyData[]");
            }

            bountyData = (List<BountyData>) readObject;

        }
    }

    public static void setBountyData(UUID player, BountyData playerData){
        if(getBountyData(player) == null){
            bountyData.add(playerData);
            return;
        }

        for(int i = 0; i < bountyData.size(); i++){
            if(bountyData.get(i).getUUID().equals(player)){
                bountyData.set(i, playerData);
                return;
            }
        }
    }

    public static BountyData getBountyData(UUID player){
        for(BountyData data : bountyData) {
            if (data.getUUID().equals(player)) {
                return data;
            }
        }
        return null;
    }


}
