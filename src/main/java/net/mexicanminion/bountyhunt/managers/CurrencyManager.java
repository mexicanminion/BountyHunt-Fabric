package net.mexicanminion.bountyhunt.managers;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CurrencyManager {

    public static HashMap<UUID, Integer> currencyMap = new HashMap<UUID, Integer>();

    /**
     * saveCurrencyFile()
     * Description: Save the currency file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void saveCurrencyFile() throws FileNotFoundException, IOException {
        File bountyDir = Paths.get("", "bountyhunt").toFile();
        File file = new File(bountyDir, "currency.dat");

        ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));

        try{
            output.writeObject(currencyMap);
            output.flush();
            output.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * loadCurrencyFile()
     * Description: Load the currency file
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void loadCurrencyFile() throws FileNotFoundException, IOException, ClassNotFoundException {
        File bountyDir = Paths.get("", "bountyhunt").toFile();
        File file = new File(bountyDir, "currency.dat");

        if(file != null){
            ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
            Object readObject = input.readObject();
            input.close();

            if(!(readObject instanceof HashMap)){
                throw new IOException("Data is not a HashMap");
            }

            currencyMap = (HashMap<UUID, Integer>) readObject;
            for(UUID key : currencyMap.keySet()){
                currencyMap.put(key, currencyMap.get(key));
            }

        }

    }

    /**
     * setCurrency()
     * Description: Set the currency
     * @param uuid
     * @param amount
     */
    public static void setCurrency(UUID uuid, Integer amount) {
        currencyMap.put(uuid, amount);
    }

    /**
     * getCurrency()
     * Description: Get the currency
     * @param uuid
     * @return
     */
    public static int getCurrency(UUID uuid) {
        if(currencyMap.get(uuid) == null) {
            return -1;
        }
        return currencyMap.get(uuid);
    }

    /**
     * emptyCurrency()
     * Description: Empty the currency buy setting it to null
     * @param uuid
     */
    public static void emptyCurrency(UUID uuid) {
        currencyMap.put(uuid, null);
    }
}
