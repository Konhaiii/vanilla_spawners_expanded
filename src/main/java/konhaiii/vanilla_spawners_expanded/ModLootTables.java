package konhaiii.vanilla_spawners_expanded;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class ModLootTables {

	public static void modifyLootTables() {

		if (!VanillaSpawnersExpanded.config.addToLootTables) {
			return;
		}

		LootTableEvents.MODIFY.register((resourceManager, lootManager, _, _) -> {

			if (BuiltInLootTables.SIMPLE_DUNGEON.equals(resourceManager)) {

				LootPool pool = LootPool.lootPool()
						.setRolls(ConstantValue.exactly(VanillaSpawnersExpanded.config.lootTableValues.dungeonRolls))
						.when(LootItemRandomChanceCondition.randomChance(VanillaSpawnersExpanded.config.lootTableValues.dungeonChance))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_CROWD).setWeight(VanillaSpawnersExpanded.config.lootTableValues.dungeonUpgradeCrowdWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_RANGE).setWeight(VanillaSpawnersExpanded.config.lootTableValues.dungeonUpgradeRangeWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_SPEED).setWeight(VanillaSpawnersExpanded.config.lootTableValues.dungeonUpgradeSpeedWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_REDSTONE).setWeight(VanillaSpawnersExpanded.config.lootTableValues.dungeonUpgradeRedstoneWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_CALIBRATOR).setWeight(VanillaSpawnersExpanded.config.lootTableValues.dungeonCalibratorWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_IGNITER).setWeight(VanillaSpawnersExpanded.config.lootTableValues.dungeonIgniterWeight))
						.add(LootItem.lootTableItem(ModItems.CURSED_BOTTLE).setWeight(VanillaSpawnersExpanded.config.lootTableValues.dungeonCursedBottleWeight))
						.build();

				lootManager.pool(pool);
			}

			if (BuiltInLootTables.END_CITY_TREASURE.equals(resourceManager)) {

				LootPool pool = LootPool.lootPool()
						.setRolls(ConstantValue.exactly(VanillaSpawnersExpanded.config.lootTableValues.endCityRolls))
						.when(LootItemRandomChanceCondition.randomChance(VanillaSpawnersExpanded.config.lootTableValues.endCityChance))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_CROWD).setWeight(VanillaSpawnersExpanded.config.lootTableValues.endCityUpgradeCrowdWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_RANGE).setWeight(VanillaSpawnersExpanded.config.lootTableValues.endCityUpgradeRangeWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_SPEED).setWeight(VanillaSpawnersExpanded.config.lootTableValues.endCityUpgradeSpeedWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_REDSTONE).setWeight(VanillaSpawnersExpanded.config.lootTableValues.endCityUpgradeRedstoneWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_CALIBRATOR).setWeight(VanillaSpawnersExpanded.config.lootTableValues.endCityCalibratorWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_IGNITER).setWeight(VanillaSpawnersExpanded.config.lootTableValues.endCityIgniterWeight))
						.add(LootItem.lootTableItem(ModItems.CURSED_BOTTLE).setWeight(VanillaSpawnersExpanded.config.lootTableValues.endCityCursedBottleWeight))
						.build();

				lootManager.pool(pool);
			}

			if (BuiltInLootTables.BASTION_TREASURE.equals(resourceManager)) {

				LootPool pool = LootPool.lootPool()
						.setRolls(ConstantValue.exactly(VanillaSpawnersExpanded.config.lootTableValues.bastionRolls))
						.when(LootItemRandomChanceCondition.randomChance(VanillaSpawnersExpanded.config.lootTableValues.bastionChance))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_CROWD).setWeight(VanillaSpawnersExpanded.config.lootTableValues.bastionUpgradeCrowdWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_RANGE).setWeight(VanillaSpawnersExpanded.config.lootTableValues.bastionUpgradeRangeWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_SPEED).setWeight(VanillaSpawnersExpanded.config.lootTableValues.bastionUpgradeSpeedWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_REDSTONE).setWeight(VanillaSpawnersExpanded.config.lootTableValues.bastionUpgradeRedstoneWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_CALIBRATOR).setWeight(VanillaSpawnersExpanded.config.lootTableValues.bastionCalibratorWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_IGNITER).setWeight(VanillaSpawnersExpanded.config.lootTableValues.bastionIgniterWeight))
						.add(LootItem.lootTableItem(ModItems.CURSED_BOTTLE).setWeight(VanillaSpawnersExpanded.config.lootTableValues.bastionCursedBottleWeight))
						.build();

				lootManager.pool(pool);
			}

			if (BuiltInLootTables.ANCIENT_CITY.equals(resourceManager)) {

				LootPool pool = LootPool.lootPool()
						.setRolls(ConstantValue.exactly(VanillaSpawnersExpanded.config.lootTableValues.ancientCityRolls))
						.when(LootItemRandomChanceCondition.randomChance(VanillaSpawnersExpanded.config.lootTableValues.ancientCityChance))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_CROWD).setWeight(VanillaSpawnersExpanded.config.lootTableValues.ancientCityUpgradeCrowdWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_RANGE).setWeight(VanillaSpawnersExpanded.config.lootTableValues.ancientCityUpgradeRangeWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_SPEED).setWeight(VanillaSpawnersExpanded.config.lootTableValues.ancientCityUpgradeSpeedWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_REDSTONE).setWeight(VanillaSpawnersExpanded.config.lootTableValues.ancientCityUpgradeRedstoneWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_CALIBRATOR).setWeight(VanillaSpawnersExpanded.config.lootTableValues.ancientCityCalibratorWeight))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_IGNITER).setWeight(VanillaSpawnersExpanded.config.lootTableValues.ancientCityIgniterWeight))
						.add(LootItem.lootTableItem(ModItems.CURSED_BOTTLE).setWeight(VanillaSpawnersExpanded.config.lootTableValues.ancientCityCursedBottleWeight))
						.build();

				lootManager.pool(pool);
			}
		});
	}
}
