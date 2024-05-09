package net.mexicanminion.bountyhunt.managers;

import net.mexicanminion.bountyhunt.util.BountyData;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class BountyDataManager {

    public static HashMap<UUID,BountyData> bountyData = new HashMap<>();

    Stack<BountyData> bountyData2 = new Stack<>();

    public void saveBountyDataFile(Logger logger) throws FileNotFoundException, IOException {
        File bountyDir = Paths.get("", "bountyhunt").toFile();
        File file = new File(bountyDir, "bountyData.dat");
        ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));

        int bountyCapacity = bountyData2.capacity();
        String[][] allDataArray = new String[bountyCapacity][8];

        try{
            output.writeObject(bountyData);
            output.flush();
            output.close();
            logger.info("Saved BountyHunt files.");
        } catch(IOException e){
            e.printStackTrace();
            logger.info("Failed to save BountyHunt files.");
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

    public static List<BountyData> getBountyData(){
        List<BountyData> bountyList = new ArrayList<>();
        for(UUID key : bountyData.keySet()){
            if(bountyData.get(key).getHasBounty()){
                bountyList.add(bountyData.get(key));
            }
        }
        return bountyList;
    }

    public static int getActiveBountyAmount(){
        int activeBountyAmount = 0;
        for(UUID key : bountyData.keySet()){
            if(bountyData.get(key).getHasBounty()){
                activeBountyAmount++;
            }
        }

        return activeBountyAmount;
    }

    /*
    public void saveBountyDataFile(Logger logger) throws FileNotFoundException, IOException {
        File bountyDir = Paths.get("", "bountyhunt").toFile();
        File file = new File(bountyDir, "bountyData.dat");

        ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));

        try{
            output.writeObject(bountyData);
            output.flush();
            output.close();
            logger.info("Saved BountyHunt files.");
        } catch(IOException e){
            e.printStackTrace();
            logger.info("Failed to save BountyHunt files.");
        }

        int amountOfB = bountyData2.capacity();
    }

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
    */

}
