package konhaiii.vanilla_spawners_expanded.item;

import konhaiii.vanilla_spawners_expanded.VanillaSpawnersExpanded;
import konhaiii.vanilla_spawners_expanded.item.special.*;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModItems {

	public static final RegistryKey<ItemGroup> MOD_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(),
			Identifier.of(VanillaSpawnersExpanded.MOD_ID, "item_group"));
	public static final ItemGroup MOD_ITEM_GROUP = FabricItemGroup.builder()
			.icon(() -> new ItemStack(ModItems.SPAWNER_IGNITER))
			.displayName(Text.translatable("itemGroup.vanilla_spawners_expanded"))
			.build();

	public static Item SPAWNER_CALIBRATOR = registerItem("spawner_calibrator", new SpawnerCalibratorItem(new Item.Settings().rarity(Rarity.UNCOMMON)));
	public static Item SPAWNER_IGNITER = registerItem("spawner_igniter", new SpawnerIgniterItem(new Item.Settings().rarity(Rarity.EPIC)));
	public static Item CURSED_BOTTLE = registerItem("cursed_bottle", new CursedBottleItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON)));
	public static Item SOUL_DESINTEGRATOR = registerItem("soul_desintegrator", new SoulDesintegratorItem(new Item.Settings()));
	public static Item SPAWNER_UPGRADE_REDSTONE = registerItem("spawner_upgrade_redstone", new SpawnerUpgradeRedstoneItem(new Item.Settings()));
	public static Item SPAWNER_UPGRADE_CROWD = registerItem("spawner_upgrade_crowd", new SpawnerUpgradeCrowdItem(new Item.Settings()));
	public static Item SPAWNER_UPGRADE_RANGE = registerItem("spawner_upgrade_range", new SpawnerUpgradeRangeItem(new Item.Settings()));
	public static Item SPAWNER_UPGRADE_SPEED = registerItem("spawner_upgrade_speed", new SpawnerUpgradeSpeedItem(new Item.Settings()));

	public static void initialize() {
		VanillaSpawnersExpanded.LOGGER.info("ModItems: Initialize");
		itemGroupRegister();
	}

	public static Item registerItem(String id, Item item) {
		// Create the identifier for the item.
		Identifier itemID = Identifier.of(VanillaSpawnersExpanded.MOD_ID, id);

		// Return the registered item!
		return Registry.register(Registries.ITEM, itemID, item);
	}

	public static void itemGroupRegister() {
		Registry.register(Registries.ITEM_GROUP, MOD_ITEM_GROUP_KEY, MOD_ITEM_GROUP);

		ItemGroupEvents.modifyEntriesEvent(MOD_ITEM_GROUP_KEY).register(itemGroup -> {
			itemGroup.add(SPAWNER_CALIBRATOR);
			itemGroup.add(SPAWNER_IGNITER);
			itemGroup.add(CURSED_BOTTLE);
			itemGroup.add(SPAWNER_UPGRADE_CROWD);
			itemGroup.add(SPAWNER_UPGRADE_REDSTONE);
			itemGroup.add(SPAWNER_UPGRADE_RANGE);
			itemGroup.add(SPAWNER_UPGRADE_SPEED);
			itemGroup.add(SOUL_DESINTEGRATOR);
		});
	}
}
