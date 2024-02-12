package net.mexicanminion.bountyhunt;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.mexicanminion.bountyhunt.managers.BountyDataManager;
import net.mexicanminion.bountyhunt.managers.BountyManager;
import net.mexicanminion.bountyhunt.managers.RewardManager;
import net.mexicanminion.bountyhunt.util.Register;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class BountyHuntMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("bountyhunt");

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

		String path = Paths.get("", "bountyhunt").toString();

		// Create the directory for data files if it doesn't exist
		try {
			boolean exists = (new File(path)).exists();
			if (!exists) {
				new File(path).mkdir();
			}
			// Load the currency, bounty, and reward files

			BountyDataManager bountyDataManager = new BountyDataManager();
			bountyDataManager.loadBountyDataFile();
			LOGGER.info("BountyHunt: Loaded currency, bounty, and reward files.");

		}
		catch (Exception e) {
			e.printStackTrace();
		}


		// Register the commands
		Register.register();
		LOGGER.info("BountyHunt has been initialized!");
	}

	// Save the currency, bounty, and reward files when the server is shutting down
	public static void onServerShutdown() throws IOException {
		LOGGER.info("BountyHunt is shutting down!");
		BountyDataManager bountyDataManager = new BountyDataManager();
		bountyDataManager.saveBountyDataFile();

		LOGGER.info("BountyHunt: Saved currency, bounty, and reward files.");
	}

}