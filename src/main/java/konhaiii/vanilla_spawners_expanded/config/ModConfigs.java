package konhaiii.vanilla_spawners_expanded.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import konhaiii.vanilla_spawners_expanded.VanillaSpawnersExpanded;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ModConfigs {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path CONFIG_PATH = Path.of("config", "vanilla_spawners_expanded.json");

	public List<String> mob_blacklist = new ArrayList<>();
	public List<String> mob_whitelist = new ArrayList<>();
	public boolean calibratedSpawnerStayLitOnBreak = false;
	public boolean calibratedSpawnerKeepMobOnBreak = true;
	public boolean calibratedSpawnerKeepUpgradesOnBreak = true;
	public boolean cursedBottleIsReusable = false;
	public int crowdUpgradeValue = 18;
	public int rangeUpgradeValue = 64;
	public int speedUpgradeMaxValue = 400;
	public int speedUpgradeMinValue = 100;
	public int crowdDefaultValue = 6;
	public int rangeDefaultValue = 16;
	public int speedDefaultMaxValue = 800;
	public int speedDefaultMinValue = 200;
	public ModConfigs() {
		mob_blacklist.add("minecraft:allay");
		mob_blacklist.add("minecraft:armadillo");
		mob_blacklist.add("minecraft:axolotl");
		mob_blacklist.add("minecraft:bat");
		mob_blacklist.add("minecraft:camel");
		mob_blacklist.add("minecraft:cat");
		mob_blacklist.add("minecraft:chicken");
		mob_blacklist.add("minecraft:cod");
		mob_blacklist.add("minecraft:cow");
		mob_blacklist.add("minecraft:donkey");
		mob_blacklist.add("minecraft:frog");
		mob_blacklist.add("minecraft:glow_squid");
		mob_blacklist.add("minecraft:horse");
		mob_blacklist.add("minecraft:mooshroom");
		mob_blacklist.add("minecraft:mule");
		mob_blacklist.add("minecraft:ocelot");
		mob_blacklist.add("minecraft:parrot");
		mob_blacklist.add("minecraft:pig");
		mob_blacklist.add("minecraft:pufferfish");
		mob_blacklist.add("minecraft:rabbit");
		mob_blacklist.add("minecraft:salmon");
		mob_blacklist.add("minecraft:sheep");
		mob_blacklist.add("minecraft:skeleton_horse");
		mob_blacklist.add("minecraft:sniffer");
		mob_blacklist.add("minecraft:snowgolem");
		mob_blacklist.add("minecraft:squid");
		mob_blacklist.add("minecraft:strider");
		mob_blacklist.add("minecraft:tadpole");
		mob_blacklist.add("minecraft:tropical_fish");
		mob_blacklist.add("minecraft:turtle");
		mob_blacklist.add("minecraft:villager");
		mob_blacklist.add("minecraft:wandering_trader");

		mob_blacklist.add("minecraft:bee");
//		mob_blacklist.add("minecraft:cave_spider");
		mob_blacklist.add("minecraft:dolphin");
//		mob_blacklist.add("minecraft:drowned");
//		mob_blacklist.add("minecraft:enderman");
		mob_blacklist.add("minecraft:fox");
		mob_blacklist.add("minecraft:goat");
		mob_blacklist.add("minecraft:iron_golem");
		mob_blacklist.add("minecraft:llama");
		mob_blacklist.add("minecraft:panda");
		mob_blacklist.add("minecraft:piglin");
		mob_blacklist.add("minecraft:polar_bear");
//		mob_blacklist.add("minecraft:spider");
		mob_blacklist.add("minecraft:trader_llama");
		mob_blacklist.add("minecraft:wolf");
//		mob_blacklist.add("minecraft:zombified_piglin");

//		mob_blacklist.add("minecraft:blaze");
//		mob_blacklist.add("minecraft:bogged");
//		mob_blacklist.add("minecraft:breeze");
		mob_blacklist.add("minecraft:creaking");
//		mob_blacklist.add("minecraft:creeper");
		mob_blacklist.add("minecraft:elder_guardian");
		mob_blacklist.add("minecraft:endermite");
		mob_blacklist.add("minecraft:evoker");
		mob_blacklist.add("minecraft:ghast");
		mob_blacklist.add("minecraft:guardian");
//		mob_blacklist.add("minecraft:hoglin");
//		mob_blacklist.add("minecraft:husk");
//		mob_blacklist.add("minecraft:magma_cube");
		mob_blacklist.add("minecraft:phantom");
		mob_blacklist.add("minecraft:piglin_brute");
		mob_blacklist.add("minecraft:pillager");
		mob_blacklist.add("minecraft:ravager");
		mob_blacklist.add("minecraft:shulker");
//		mob_blacklist.add("minecraft:silverfish");
//		mob_blacklist.add("minecraft:skeleton");
//		mob_blacklist.add("minecraft:slime");
//		mob_blacklist.add("minecraft:stray");
		mob_blacklist.add("minecraft:vex");
		mob_blacklist.add("minecraft:vindicator");
		mob_blacklist.add("minecraft:warden");
//		mob_blacklist.add("minecraft:witch");
//		mob_blacklist.add("minecraft:wither_skeleton");
		mob_blacklist.add("minecraft:zoglin");
//		mob_blacklist.add("minecraft:zombie");
		mob_blacklist.add("minecraft:zombie_villager");

		mob_blacklist.add("minecraft:ender_dragon");
		mob_blacklist.add("minecraft:wither");

		mob_blacklist.add("minecraft:illusioner");
		mob_blacklist.add("minecraft:zombie_horse");
	}
	public static ModConfigs loadConfig() {
		if (!Files.exists(CONFIG_PATH)) {
			ModConfigs defaultConfig = new ModConfigs();
			defaultConfig.saveConfig();
			return defaultConfig;
		}

		try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
			return GSON.fromJson(reader, ModConfigs.class);
		} catch (IOException | JsonSyntaxException exception) {
			VanillaSpawnersExpanded.LOGGER.error(exception.getMessage());
			return new ModConfigs();
		}
	}

	public void saveConfig() {
		try {
			Files.createDirectories(CONFIG_PATH.getParent());
			try (Writer writer = Files.newBufferedWriter(CONFIG_PATH, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
				GSON.toJson(this, writer);
			}
		} catch (IOException exception) {
			VanillaSpawnersExpanded.LOGGER.error(exception.getMessage());
		}
	}
}