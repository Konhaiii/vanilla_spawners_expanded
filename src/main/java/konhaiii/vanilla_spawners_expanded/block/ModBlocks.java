package konhaiii.vanilla_spawners_expanded.block;

import konhaiii.vanilla_spawners_expanded.VanillaSpawnersExpanded;
import konhaiii.vanilla_spawners_expanded.block.calibrated_spawner.CalibratedSpawnerBlock;
import konhaiii.vanilla_spawners_expanded.block.calibrated_spawner.CalibratedSpawnerBlockEntity;
import konhaiii.vanilla_spawners_expanded.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModBlocks {

	public static final Block CALIBRATED_SPAWNER = register(
			new CalibratedSpawnerBlock(AbstractBlock.Settings.create()
					.mapColor(MapColor.STONE_GRAY)
					.instrument(Instrument.BASEDRUM)
					.requiresTool()
					.strength(5.0f)
					.sounds(BlockSoundGroup.METAL)
					.nonOpaque()
	), "calibrated_spawner", true);
	public static final BlockEntityType<CalibratedSpawnerBlockEntity> CALIBRATED_SPAWNER_BLOCK_ENTITY =
			register(CalibratedSpawnerBlockEntity::new, ModBlocks.CALIBRATED_SPAWNER);
	public static Block register(Block block, String name, boolean shouldRegisterItem) {
		// Register the block and its item.
		Identifier id = Identifier.of(VanillaSpawnersExpanded.MOD_ID, name);

		// Sometimes, you may not want to register an item for the block.
		// Eg: if it's a technical block like `minecraft:air` or `minecraft:end_gateway`
		if (shouldRegisterItem) {
			BlockItem blockItem = new BlockItem(block, new Item.Settings().rarity(Rarity.EPIC));
			Registry.register(Registries.ITEM, id, blockItem);
		}

		return Registry.register(Registries.BLOCK, id, block);
	}

	private static <T extends BlockEntity> BlockEntityType<T> register(BlockEntityType.BlockEntityFactory<? extends T> entityFactory,
	                                                                   Block... blocks) {
		Identifier id = Identifier.of(VanillaSpawnersExpanded.MOD_ID, "calibrated_spawner");
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, BlockEntityType.Builder.<T>create(entityFactory, blocks).build(null));
	}

	public static void initialize() {
		ItemGroupEvents.modifyEntriesEvent(ModItems.MOD_ITEM_GROUP_KEY).register((itemGroup) -> itemGroup.add(CALIBRATED_SPAWNER.asItem()));
	}

}