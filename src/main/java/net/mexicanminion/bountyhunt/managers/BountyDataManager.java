package net.mexicanminion.bountyhunt.managers;

import com.llamalad7.mixinextras.lib.apache.commons.ArrayUtils;
import net.mexicanminion.bountyhunt.util.BountyData;
import net.mexicanminion.bountyhunt.util.BountyData_OLD;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class BountyDataManager {

    public static HashMap<UUID,BountyData_OLD> bountyData_OLD = new HashMap<>();
    public static Stack<BountyData> bountyData = new Stack<>();

    int bountyCapacity = bountyData.capacity();
    int totalVarsStored = 8;

    public void saveBountyDataFile(Logger logger) throws FileNotFoundException, IOException {
        File bountyDir = Paths.get("", "bountyhunt").toFile();
        File file = new File(bountyDir, "bountyData.dat");
        ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));

        String[][] allDataArray = new String[bountyCapacity][totalVarsStored];

        int currCount = 0;
        for(BountyData data : bountyData){
            allDataArray[currCount] = data.getSaveData();
            currCount++;
        }

        try{
            output.writeObject(allDataArray);
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
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void loadBountyDataFile() throws IOException, ClassNotFoundException {
        File bountyDir = Paths.get("", "bountyhunt").toFile();
        File file = new File(bountyDir, "bountyData.dat");

        if(file != null){
            ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
            Object readObject = input.readObject();
            input.close();

            if(!(readObject instanceof String[][])){
                throw new IOException("Data is not a String[][]");
            }

            String[][] allDataArray;
            bountyData.empty();

            allDataArray = (String[][]) readObject;
            for(int i = 0; i < allDataArray.length; i++){
                bountyData.add(new BountyData(allDataArray[i]));
            }

        }
    }

    public void updateAndLoadBDFile() throws IOException, ClassNotFoundException {
        File bountyDir = Paths.get("", "bountyhunt").toFile();
        File file = new File(bountyDir, "bountyData.dat");

        if(file != null){
            ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
            Object readObject = input.readObject();
            input.close();

            if(!(readObject instanceof String[][])){
                throw new IOException("Data is not a String[][]");
            }

            String[][] allDataArray;
            bountyData.empty();
            allDataArray = (String[][]) readObject;

            int arrayDiff = new BountyData().getSaveData().length - allDataArray[0].length;
            String[] temp = new String[arrayDiff];

            //fill temp array with null vales
            for(int i = 0; i < arrayDiff; i++){
                temp[i] = null;
            }

            for(int r = 0; r < allDataArray.length; r++){
                bountyData.add(new BountyData(ArrayUtils.addAll(allDataArray[r], temp)));
            }

        }
    }

    public static void setBountyData(BountyData playerData){
        bountyData.add(playerData);
    }

    public static BountyData getBountyData(UUID player){
        for (BountyData data:bountyData) {
            if(data.getUUID() == player){
                return data;
            }
        }
        return null;
    }

    public static List<BountyData> getBountyData(){
        List<BountyData> bountyList = new ArrayList<>();
        for (BountyData data:bountyData) {
            if(data.getHasBounty()){
                bountyList.add(data);
            }
        }
        return bountyList;
    }

    public static int getActiveBountyAmount(){
        int activeBountyAmount = 0;
        for (BountyData data:bountyData) {
            if(data.getHasBounty()){
                activeBountyAmount++;
            }
        }

        return activeBountyAmount;
    }

    /*public void saveBountyDataFileOLD(Logger logger) throws IOException {
        File bountyDir = Paths.get("", "bountyhunt").toFile();
        File file = new File(bountyDir, "bountyData.dat");

        ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));

        try{
            output.writeObject(bountyData_OLD);
            output.flush();
            output.close();
            logger.info("Saved BountyHunt files.");
        } catch(IOException e){
            e.printStackTrace();
            logger.info("Failed to save BountyHunt files.");
        }

        int amountOfB = bountyData.capacity();
    }*/

    public void loadBountyDataFileOLD() throws IOException, ClassNotFoundException {
        File bountyDir = Paths.get("", "bountyhunt").toFile();
        File file = new File(bountyDir, "bountyData.dat");

        if(file != null){
            ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
            Object readObject = input.readObject();
            input.close();

            if(!(readObject instanceof HashMap)){
                throw new IOException("Data is not a HashMap");
            }

            bountyData_OLD = (HashMap<UUID, BountyData_OLD>) readObject;
            for(UUID key : bountyData_OLD.keySet()){
                BountyData_OLD tempBD = bountyData_OLD.get(key);

                int arrayDiff = new BountyData().getSaveData().length - 8;
                String[] tempNULL = new String[arrayDiff];

                //fill temp array with null vales
                for(int i = 0; i < arrayDiff; i++){
                    tempNULL[i] = null;
                }

                String hasBountyString;
                String hasRewardString;
                if(tempBD.getHasBounty()){
                    hasBountyString = "true";
                }else {
                    hasBountyString = "false";
                }
                if(tempBD.getHasReward()){
                    hasRewardString = "true";
                }else {
                    hasRewardString = "false";
                }

                String[] tempData = {tempBD.getUUID().toString(),
                        hasBountyString,
                        hasRewardString,
                        String.valueOf(tempBD.getBountyValue()),
                        String.valueOf(tempBD.getRewardValue()),
                        tempBD.getGameProfile().getId().toString(),
                        tempBD.getGameProfile().getName(),
                        tempBD.getPlayerName()
                };

                bountyData.add(new BountyData(ArrayUtils.addAll(tempData, tempNULL)));
            }
        }
    }

}
