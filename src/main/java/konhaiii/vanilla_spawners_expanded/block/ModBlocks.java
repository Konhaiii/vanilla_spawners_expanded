package konhaiii.vanilla_spawners_expanded.block;

import konhaiii.vanilla_spawners_expanded.VanillaSpawnersExpanded;
import konhaiii.vanilla_spawners_expanded.block.calibrated_spawner.CalibratedSpawnerBlockEntity;
import konhaiii.vanilla_spawners_expanded.block.calibrated_spawner.CalibratedSpawnerBlock;
import konhaiii.vanilla_spawners_expanded.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.function.Function;

public class ModBlocks {

	public static final Block CALIBRATED_SPAWNER = register(
			CalibratedSpawnerBlock::new,
			AbstractBlock.Settings.create()
					.mapColor(MapColor.STONE_GRAY)
					.instrument(NoteBlockInstrument.BASEDRUM)
					.requiresTool()
					.strength(5.0F)
					.sounds(BlockSoundGroup.SPAWNER)
					.nonOpaque()
	);
	public static final BlockEntityType<CalibratedSpawnerBlockEntity> CALIBRATED_SPAWNER_BLOCK_ENTITY =
			register(CalibratedSpawnerBlockEntity::new, CALIBRATED_SPAWNER);
	private static Block register(Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings) {
		RegistryKey<Block> blockKey = keyOfBlock();
		Block block = blockFactory.apply(settings.registryKey(blockKey));
		RegistryKey<Item> itemKey = keyOfItem();

		BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey).rarity(Rarity.EPIC).component(DataComponentTypes.BLOCK_ENTITY_DATA, CalibratedSpawnerBlockEntity.getDefaultComponent()));
		Registry.register(Registries.ITEM, itemKey, blockItem);

		return Registry.register(Registries.BLOCK, blockKey, block);
	}

	private static RegistryKey<Block> keyOfBlock() {
		return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(VanillaSpawnersExpanded.MOD_ID, "calibrated_spawner"));
	}

	private static RegistryKey<Item> keyOfItem() {
		return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(VanillaSpawnersExpanded.MOD_ID, "calibrated_spawner"));
	}

	private static <T extends BlockEntity> BlockEntityType<T> register(
			FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
			Block... blocks
	) {
		Identifier id = Identifier.of(VanillaSpawnersExpanded.MOD_ID, "calibrated_spawner");
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
	}

	public static void initialize() {
		ItemGroupEvents.modifyEntriesEvent(ModItems.MOD_ITEM_GROUP_KEY).register((itemGroup) -> itemGroup.add(CALIBRATED_SPAWNER.asItem()));
	}

}