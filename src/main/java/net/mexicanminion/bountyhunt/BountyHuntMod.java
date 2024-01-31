package net.mexicanminion.bountyhunt;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.mexicanminion.bountyhunt.managers.BountyManager;
import net.mexicanminion.bountyhunt.managers.CurrencyManager;
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

		try {
			boolean exists = (new File(path)).exists();
			if (!exists) {
				new File(path).mkdir();
			}
			CurrencyManager currencyManager = new CurrencyManager();
			BountyManager bountyManager = new BountyManager();
			RewardManager rewardManager = new RewardManager();
			currencyManager.loadCurrencyFile();
			bountyManager.loadBountyFile();
			rewardManager.loadRewardFile();
			LOGGER.info("BountyHunt: Loaded currency, bounty, and reward files.");

		}
		catch (Exception e) {
			e.printStackTrace();
		}


		Register.register();
		LOGGER.info("BountyHunt has been initialized!");
	}

	public static void onServerShutdown() throws IOException {
		LOGGER.info("BountyHunt is shutting down!");

		CurrencyManager currencyManager = new CurrencyManager();
		BountyManager bountyManager = new BountyManager();
		RewardManager rewardManager = new RewardManager();
		currencyManager.saveCurrencyFile();
		bountyManager.saveBountyFile();
		rewardManager.saveRewardFile();
		LOGGER.info("BountyHunt: Saved currency, bounty, and reward files.");
	}

}