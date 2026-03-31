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

		LootTableEvents.MODIFY.register((resourceManager, lootManager, lootTableSource, provider) -> {

			if (BuiltInLootTables.SIMPLE_DUNGEON.equals(resourceManager)) {

				LootPool pool = LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.when(LootItemRandomChanceCondition.randomChance(0.35f))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_CALIBRATOR).setWeight(5))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_CROWD).setWeight(2))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_RANGE).setWeight(1))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_REDSTONE).setWeight(2))
						.build();

				lootManager.pool(pool);
			}

			if (BuiltInLootTables.END_CITY_TREASURE.equals(resourceManager)) {

				LootPool pool = LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.when(LootItemRandomChanceCondition.randomChance(0.2f))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_CALIBRATOR).setWeight(1))
						.add(LootItem.lootTableItem(ModItems.CURSED_BOTTLE).setWeight(2))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_IGNITER).setWeight(2))
						.build();

				lootManager.pool(pool);
			}

			if (BuiltInLootTables.BASTION_TREASURE.equals(resourceManager)) {

				LootPool pool = LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.when(LootItemRandomChanceCondition.randomChance(0.25f))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_CALIBRATOR).setWeight(1))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_RANGE).setWeight(3))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_CROWD).setWeight(3))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_SPEED).setWeight(1))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_IGNITER).setWeight(2))
						.build();

				lootManager.pool(pool);
			}

			if (BuiltInLootTables.ANCIENT_CITY.equals(resourceManager)) {

				LootPool pool = LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.when(LootItemRandomChanceCondition.randomChance(0.3f))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_CALIBRATOR).setWeight(1))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_RANGE).setWeight(1))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_UPGRADE_SPEED).setWeight(1))
						.add(LootItem.lootTableItem(ModItems.SPAWNER_IGNITER).setWeight(2))
						.build();

				lootManager.pool(pool);
			}
		});
	}
}
