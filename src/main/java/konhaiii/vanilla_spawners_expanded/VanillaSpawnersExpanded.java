package konhaiii.vanilla_spawners_expanded;

import konhaiii.vanilla_spawners_expanded.block.ModBlocks;
import konhaiii.vanilla_spawners_expanded.config.ModConfigs;
import konhaiii.vanilla_spawners_expanded.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VanillaSpawnersExpanded implements ModInitializer {
	public static final String MOD_ID = "vanilla_spawners_expanded";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ModConfigs config;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Vanilla Spawners Expanded: Initialize");
		config = ModConfigs.loadConfig();
		ModItems.initialize();
		ModBlocks.initialize();
	}
}