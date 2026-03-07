package konhaiii.vanilla_spawners_expanded;

import konhaiii.vanilla_spawners_expanded.blocks.CalibratedSpawnerBlock;
import konhaiii.vanilla_spawners_expanded.blocks.CalibratedSpawnerBlockEntity;
import konhaiii.vanilla_spawners_expanded.items.CalibratedSpawnerItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class ModBlocks {

	public static final Block CALIBRATED_SPAWNER = new CalibratedSpawnerBlock(BlockBehaviour.Properties.of().setId(keyOfBlock()).sound(SoundType.SPAWNER).instrument(NoteBlockInstrument.BASEDRUM).noOcclusion().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0F));

	public static CompoundTag getCalibratedSpawnerDefaultComponents() {
		CompoundTag compoundTag = new CompoundTag();
		compoundTag.putShort("Delay", (short)200);
		compoundTag.putBoolean("HasRedstoneUpgrade", false);
		compoundTag.putBoolean("IsLit", false);
		compoundTag.putBoolean("HasSpeedUpgrade", false);
		compoundTag.putShort("SpawnCount", (short)4);
		compoundTag.putBoolean("HasCrowdUpgrade", false);
		compoundTag.putBoolean("HasRangeUpgrade", false);
		compoundTag.putShort("SpawnRange", (short)4);
		compoundTag.putString("id", "vanilla_spawners_expanded:calibrated_spawner");

		CompoundTag spawnData = new CompoundTag();
		spawnData.put("entity", new CompoundTag());

		compoundTag.put("SpawnData", spawnData);

		return compoundTag;
	}

	public static final BlockEntityType<@org.jetbrains.annotations.NotNull CalibratedSpawnerBlockEntity> CALIBRATED_SPAWNER_BLOCK_ENTITY =
			register(CalibratedSpawnerBlockEntity::new, CALIBRATED_SPAWNER);

	public static final BlockItem CALIBRATED_SPAWNER_ITEM = new CalibratedSpawnerItem(
			CALIBRATED_SPAWNER,
			new Item.Properties()
					.setId(keyOfItem())
					.rarity(Rarity.EPIC)
					.component(
							DataComponents.BLOCK_ENTITY_DATA,
							TypedEntityData.of(CALIBRATED_SPAWNER_BLOCK_ENTITY, getCalibratedSpawnerDefaultComponents())
					)
	);

	private static ResourceKey<Block> keyOfBlock() {
		return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(VanillaSpawnersExpanded.MOD_ID, "calibrated_spawner"));
	}

	private static ResourceKey<Item> keyOfItem() {
		return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(VanillaSpawnersExpanded.MOD_ID, "calibrated_spawner"));
	}

	public static void initialize() {
		Registry.register(BuiltInRegistries.ITEM,
				Identifier.fromNamespaceAndPath(VanillaSpawnersExpanded.MOD_ID, "calibrated_spawner"),
				CALIBRATED_SPAWNER_ITEM
		);

		Registry.register(BuiltInRegistries.BLOCK,
				Identifier.fromNamespaceAndPath(VanillaSpawnersExpanded.MOD_ID, "calibrated_spawner"),
				CALIBRATED_SPAWNER
		);

		ItemGroupEvents.modifyEntriesEvent(ModItems.VANILLA_SPAWNERS_EXPANDED_CREATIVE_TAB_KEY).register((itemGroup) -> itemGroup.accept(CALIBRATED_SPAWNER.asItem()));
	}

	private static <T extends BlockEntity> BlockEntityType<T> register(
			FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
			Block... blocks
	) {
		Identifier id = Identifier.fromNamespaceAndPath(VanillaSpawnersExpanded.MOD_ID, "calibrated_spawner_block_entity");
		return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
	}
}
