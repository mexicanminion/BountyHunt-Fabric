package net.mexicanminion.bountyhunt;

import com.electronwill.nightconfig.core.file.FileConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.mexicanminion.bountyhunt.managers.BountyDataManager;
import net.mexicanminion.bountyhunt.util.Register;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Paths;

public class BountyHuntMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("bountyhunt");
	public static FileConfig config;
	static String bountyPath = Paths.get("", "bountyhunt").toString();

	static private int fileVer = 1;

	// Register the server shutdown event
	static {
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			try {
				onServerShutdown();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("BountyHunt is initializing!");
		// Test commit
		// Create the directory for data files if it doesn't exist
		try {
			boolean exists = (new File(bountyPath)).exists();
			if (!exists) {
				new File(bountyPath).mkdir();
			}
			// Load the currency, bounty, and reward files

			BountyDataManager bountyDataManager = new BountyDataManager();

			// Load the config file
			loadConfig();
			int verConfirm = checkConfig();

			switch (verConfirm){
				case -1: // needs to throw error
					throw new Error("Please update to the latest BountyHunt Version! This mod does not support downgrading!");
				case 1:  // no number found
					bountyDataManager.loadBountyDataFileOLD();
					break;
				case 2:  // updated config num
					bountyDataManager.updateAndLoadBDFile();
					break;
				case 3:  // ver number matches
					bountyDataManager.loadBountyDataFile();
					break;
			}

			LOGGER.info("Loaded BountyHunt files.");

		}
		catch (Exception e) {
			if((new File(bountyPath)).exists()){
				LOGGER.info("Created `bountyhunt` directory.");
			}else{
				e.printStackTrace();
				LOGGER.info("Error loading BountyHunt files.");
			}
		}
		// Register the commands
		Register.register();
		LOGGER.info("Registered commands.");

		LOGGER.info("BountyHunt has been initialized!");
	}

	private void loadConfig() {
		boolean exists = (new File(bountyPath,"bountyConfig.json")).exists();
		if(exists){
			readJSON();
			LOGGER.info("Loaded config file.");
		}
		else{
			config = FileConfig.of(new File(bountyPath,"bountyConfig.json"));
			config.load();
			initConfigValues();
			config.save();
			LOGGER.info("Created config file.");
		}
	}

	private int checkConfig() {
		if(config.get("BHVersion") == null){			  // no number found
			config.set("BHVersion", fileVer); //The File System version
			return 1;
		}else if((int)config.get("BHVersion") < fileVer){ // update config num
			config.set("BHVersion", fileVer);
			return 2;
		}else if((int)config.get("BHVersion") > fileVer){ // needs to throw error
			return -1;
		}else{ 											  // ver number matches
			return 3;
		}

	}

	private static void writeJSON() throws IOException {
		config.save();
		config.close();
		LOGGER.info("Saved config file.");
	}

	private void readJSON(){
		config = FileConfig.of(new File(bountyPath,"bountyConfig.json"));// For now, the configuration is empty. We need to read its content:
		config.load(); // Reads the file and populates the config
		if(config.get("itemIngot") == null){
			config.load();
		}
	}

	public static void initConfigValues() {
		//TODO: for future updated to config, add a check to see if the value is already set, if not, set it
		config.set("itemIngot", Registries.ITEM.getRawId(Items.DIAMOND));
		config.set("itemBlock", Registries.ITEM.getRawId(Items.DIAMOND_BLOCK));
		config.set("ingotToBlockAmount", 9);
		config.set("onlyIngot", false);
		config.set("announceAmount", 576); //1 stack of blocks
		config.set("BHVersion", fileVer); //The File System version
	}

	// Save the currency, bounty, and reward files when the server is shutting down
	public static void onServerShutdown() throws IOException {
		LOGGER.info("BountyHunt is shutting down!");
		BountyDataManager bountyDataManager = new BountyDataManager();
		bountyDataManager.saveBountyDataFile(LOGGER);

		try {
			writeJSON();
		} catch (IOException e) {
			LOGGER.info("Error saving config file.");
		}

		LOGGER.info("BountyHunt has been shut down!");
	}

}