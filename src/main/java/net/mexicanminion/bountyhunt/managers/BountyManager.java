package net.mexicanminion.bountyhunt.managers;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class BountyManager {

    public static HashMap<UUID,UUID> bountyMap = new HashMap<>();

    public void saveBountyFile() throws FileNotFoundException, IOException {
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
    }

    public void loadBountyFile() throws FileNotFoundException, IOException, ClassNotFoundException {
        File bountyDir = Paths.get("", "bountyhunt").toFile();
        File file = new File(bountyDir, "bounty.dat");

        if(file != null){
            ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
            Object readObject = input.readObject();
            input.close();

            if(!(readObject instanceof HashMap)){
                throw new IOException("Data is not a HashMap");
            }

            bountyMap = (HashMap<UUID, UUID>) readObject;
            for(UUID key : bountyMap.keySet()){
                bountyMap.put(key, bountyMap.get(key));
            }

        }
    }

    public static void setBounty(UUID bounty, UUID rewarder) {
        bountyMap.put(bounty, rewarder);
    }

    public static UUID getBounty(UUID bounty) {
        return bountyMap.get(bounty);
    }

    public static void removeBounty(UUID bounty) {
        bountyMap.put(bounty, null);
    }

}
