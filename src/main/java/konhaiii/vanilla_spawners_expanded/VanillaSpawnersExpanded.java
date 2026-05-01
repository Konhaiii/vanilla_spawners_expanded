package konhaiii.vanilla_spawners_expanded;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VanillaSpawnersExpanded implements ModInitializer {
	public static final String MOD_ID = "vanilla_spawners_expanded";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ModConfigs config;

	@Override
	public void onInitialize() {
		config = ModConfigs.loadConfig();
		ModLootTables.modifyLootTables();
		ModItems.initialize();
		ModBlocks.initialize();
		LOGGER.info("Vanilla Spawners Expanded: Initialized");
	}
}