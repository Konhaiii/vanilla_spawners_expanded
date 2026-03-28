package konhaiii.vanilla_spawners_expanded;

import konhaiii.vanilla_spawners_expanded.items.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.function.Function;

public class ModItems {
	public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
		// Create the item key.
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(VanillaSpawnersExpanded.MOD_ID, name));

		// Create the item instance.
		T item = itemFactory.apply(settings.setId(itemKey));

		// Register the item.
		Registry.register(BuiltInRegistries.ITEM, itemKey, item);

		return item;
	}

	public static Item SPAWNER_CALIBRATOR = register("spawner_calibrator", SpawnerCalibratorItem::new,
			new Item.Properties().rarity(Rarity.UNCOMMON));
	public static Item SPAWNER_IGNITER = register("spawner_igniter", SpawnerIgniterItem::new,
			new Item.Properties().rarity(Rarity.EPIC));
	public static Item CURSED_BOTTLE = register("cursed_bottle", CursedBottleItem::new,
			new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON));
	public static Item SOUL_DESINTEGRATOR = register("soul_desintegrator", SoulDesintegratorItem::new,
			new Item.Properties());
	public static Item SPAWNER_UPGRADE_REDSTONE = register("spawner_upgrade_redstone", SpawnerUpgradeRedstoneItem::new,
			new Item.Properties());
	public static Item SPAWNER_UPGRADE_CROWD = register("spawner_upgrade_crowd", SpawnerUpgradeCrowdItem::new,
			new Item.Properties());
	public static Item SPAWNER_UPGRADE_RANGE = register("spawner_upgrade_range", SpawnerUpgradeRangeItem::new,
			new Item.Properties());
	public static Item SPAWNER_UPGRADE_SPEED = register("spawner_upgrade_speed", SpawnerUpgradeSpeedItem::new,
			new Item.Properties());
	public static void initialize() {
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, VANILLA_SPAWNERS_EXPANDED_CREATIVE_TAB_KEY, VANILLA_SPAWNERS_EXPANDED_CREATIVE_TAB);
	}

	public static final ResourceKey<CreativeModeTab> VANILLA_SPAWNERS_EXPANDED_CREATIVE_TAB_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(VanillaSpawnersExpanded.MOD_ID, "creative_tab"));
	public static final CreativeModeTab VANILLA_SPAWNERS_EXPANDED_CREATIVE_TAB = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
			.icon(() -> new ItemStack(SPAWNER_CALIBRATOR))
			.title(Component.translatable("itemGroup.vanilla_spawners_expanded"))
			.displayItems((_, output) -> {
				output.accept(SPAWNER_CALIBRATOR);
				output.accept(SPAWNER_IGNITER);
				output.accept(CURSED_BOTTLE);
				output.accept(SOUL_DESINTEGRATOR);
				output.accept(SPAWNER_UPGRADE_REDSTONE);
				output.accept(SPAWNER_UPGRADE_CROWD);
				output.accept(SPAWNER_UPGRADE_RANGE);
				output.accept(SPAWNER_UPGRADE_SPEED);
			})
			.build();
}
