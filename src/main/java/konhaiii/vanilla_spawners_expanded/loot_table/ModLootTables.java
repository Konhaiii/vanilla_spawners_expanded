package konhaiii.vanilla_spawners_expanded.loot_table;

import konhaiii.vanilla_spawners_expanded.VanillaSpawnersExpanded;
import konhaiii.vanilla_spawners_expanded.item.ModItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
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

		LootTableEvents.MODIFY.register((resourceManager, manager, id, tableBuilder, source) -> {

			if (!source.isBuiltin()) return;

			if (id.equals(LootTables.SIMPLE_DUNGEON_CHEST)) {

				LootPool pool = LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1))
						.conditionally(RandomChanceLootCondition.builder(0.35f))
						.with(ItemEntry.builder(ModItems.SPAWNER_CALIBRATOR).weight(5))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_CROWD).weight(2))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_RANGE).weight(1))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_REDSTONE).weight(2))
						.build();

				tableBuilder.pool(pool);
			}

			if (id.equals(LootTables.END_CITY_TREASURE_CHEST)) {

				LootPool pool = LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1))
						.conditionally(RandomChanceLootCondition.builder(0.2f))
						.with(ItemEntry.builder(ModItems.SPAWNER_CALIBRATOR).weight(1))
						.with(ItemEntry.builder(ModItems.CURSED_BOTTLE).weight(2))
						.with(ItemEntry.builder(ModItems.SPAWNER_IGNITER).weight(2))
						.build();

				tableBuilder.pool(pool);
			}

			if (id.equals(LootTables.BASTION_TREASURE_CHEST)) {

				LootPool pool = LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1))
						.conditionally(RandomChanceLootCondition.builder(0.25f))
						.with(ItemEntry.builder(ModItems.SPAWNER_CALIBRATOR).weight(1))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_RANGE).weight(3))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_CROWD).weight(3))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_SPEED).weight(1))
						.with(ItemEntry.builder(ModItems.SPAWNER_IGNITER).weight(2))
						.build();

				tableBuilder.pool(pool);
			}

			if (id.equals(LootTables.ANCIENT_CITY_CHEST)) {

				LootPool pool = LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1))
						.conditionally(RandomChanceLootCondition.builder(0.3f))
						.with(ItemEntry.builder(ModItems.SPAWNER_CALIBRATOR).weight(1))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_RANGE).weight(1))
						.with(ItemEntry.builder(ModItems.SPAWNER_UPGRADE_SPEED).weight(1))
						.with(ItemEntry.builder(ModItems.SPAWNER_IGNITER).weight(2))
						.build();

				tableBuilder.pool(pool);
			}
		});
	}
}