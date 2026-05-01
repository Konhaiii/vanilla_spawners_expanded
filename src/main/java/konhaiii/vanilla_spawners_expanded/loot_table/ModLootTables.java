package konhaiii.vanilla_spawners_expanded.loot_table;

import konhaiii.vanilla_spawners_expanded.VanillaSpawnersExpanded;
import konhaiii.vanilla_spawners_expanded.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;

public class ModLootTables {

	public static void modifyLootTables() {

		if (!VanillaSpawnersExpanded.config.addToLootTables) {
			return;
		}

		LootTableEvents.MODIFY.register((resourceKey, tableBuilder, source, registries) -> {

			if (!source.isBuiltin()) return;

			if (resourceKey == LootTables.SIMPLE_DUNGEON_CHEST) {

				LootPool pool = LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(VanillaSpawnersExpanded.config.lootTableValues.dungeonRolls))
						.conditionally(RandomChanceLootCondition.builder(VanillaSpawnersExpanded.config.lootTableValues.dungeonChance))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_CROWD).weight(VanillaSpawnersExpanded.config.lootTableValues.dungeonUpgradeCrowdWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_RANGE).weight(VanillaSpawnersExpanded.config.lootTableValues.dungeonUpgradeRangeWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_SPEED).weight(VanillaSpawnersExpanded.config.lootTableValues.dungeonUpgradeSpeedWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_REDSTONE).weight(VanillaSpawnersExpanded.config.lootTableValues.dungeonUpgradeRedstoneWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_CALIBRATOR).weight(VanillaSpawnersExpanded.config.lootTableValues.dungeonCalibratorWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_IGNITER).weight(VanillaSpawnersExpanded.config.lootTableValues.dungeonIgniterWeight))
						.with(ItemEntry.builder(ModItems.CURSED_BOTTLE).weight(VanillaSpawnersExpanded.config.lootTableValues.dungeonCursedBottleWeight))
						.build();

				tableBuilder.pool(pool);
			}

			if (resourceKey == LootTables.END_CITY_TREASURE_CHEST) {

				LootPool pool = LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(VanillaSpawnersExpanded.config.lootTableValues.endCityRolls))
						.conditionally(RandomChanceLootCondition.builder(VanillaSpawnersExpanded.config.lootTableValues.endCityChance))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_CROWD).weight(VanillaSpawnersExpanded.config.lootTableValues.endCityUpgradeCrowdWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_RANGE).weight(VanillaSpawnersExpanded.config.lootTableValues.endCityUpgradeRangeWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_SPEED).weight(VanillaSpawnersExpanded.config.lootTableValues.endCityUpgradeSpeedWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_REDSTONE).weight(VanillaSpawnersExpanded.config.lootTableValues.endCityUpgradeRedstoneWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_CALIBRATOR).weight(VanillaSpawnersExpanded.config.lootTableValues.endCityCalibratorWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_IGNITER).weight(VanillaSpawnersExpanded.config.lootTableValues.endCityIgniterWeight))
						.with(ItemEntry.builder(ModItems.CURSED_BOTTLE).weight(VanillaSpawnersExpanded.config.lootTableValues.endCityCursedBottleWeight))
						.build();

				tableBuilder.pool(pool);
			}

			if (resourceKey == LootTables.BASTION_TREASURE_CHEST) {

				LootPool pool = LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(VanillaSpawnersExpanded.config.lootTableValues.bastionRolls))
						.conditionally(RandomChanceLootCondition.builder(VanillaSpawnersExpanded.config.lootTableValues.bastionChance))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_CROWD).weight(VanillaSpawnersExpanded.config.lootTableValues.bastionUpgradeCrowdWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_RANGE).weight(VanillaSpawnersExpanded.config.lootTableValues.bastionUpgradeRangeWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_SPEED).weight(VanillaSpawnersExpanded.config.lootTableValues.bastionUpgradeSpeedWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_REDSTONE).weight(VanillaSpawnersExpanded.config.lootTableValues.bastionUpgradeRedstoneWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_CALIBRATOR).weight(VanillaSpawnersExpanded.config.lootTableValues.bastionCalibratorWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_IGNITER).weight(VanillaSpawnersExpanded.config.lootTableValues.bastionIgniterWeight))
						.with(ItemEntry.builder(ModItems.CURSED_BOTTLE).weight(VanillaSpawnersExpanded.config.lootTableValues.bastionCursedBottleWeight))
						.build();

				tableBuilder.pool(pool);
			}

			if (resourceKey == LootTables.ANCIENT_CITY_CHEST) {

				LootPool pool = LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(VanillaSpawnersExpanded.config.lootTableValues.ancientCityRolls))
						.conditionally(RandomChanceLootCondition.builder(VanillaSpawnersExpanded.config.lootTableValues.ancientCityChance))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_CROWD).weight(VanillaSpawnersExpanded.config.lootTableValues.ancientCityUpgradeCrowdWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_RANGE).weight(VanillaSpawnersExpanded.config.lootTableValues.ancientCityUpgradeRangeWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_SPEED).weight(VanillaSpawnersExpanded.config.lootTableValues.ancientCityUpgradeSpeedWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_REDSTONE).weight(VanillaSpawnersExpanded.config.lootTableValues.ancientCityUpgradeRedstoneWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_CALIBRATOR).weight(VanillaSpawnersExpanded.config.lootTableValues.ancientCityCalibratorWeight))
						.with(ItemEntry.builder(ModItems.SPAWNER_IGNITER).weight(VanillaSpawnersExpanded.config.lootTableValues.ancientCityIgniterWeight))
						.with(ItemEntry.builder(ModItems.CURSED_BOTTLE).weight(VanillaSpawnersExpanded.config.lootTableValues.ancientCityCursedBottleWeight))
						.build();

				tableBuilder.pool(pool);
			}
		});
	}
}