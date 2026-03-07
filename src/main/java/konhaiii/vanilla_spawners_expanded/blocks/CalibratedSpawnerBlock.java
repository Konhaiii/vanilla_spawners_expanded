package konhaiii.vanilla_spawners_expanded.blocks;

import com.mojang.serialization.MapCodec;
import konhaiii.vanilla_spawners_expanded.ModBlocks;
import konhaiii.vanilla_spawners_expanded.VanillaSpawnersExpanded;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class CalibratedSpawnerBlock extends BaseEntityBlock {
	public static final MapCodec<CalibratedSpawnerBlock> CODEC = simpleCodec(CalibratedSpawnerBlock::new);
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	@Override
	public @NotNull MapCodec<CalibratedSpawnerBlock> codec() {
		return CODEC;
	}

	public CalibratedSpawnerBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
	}

	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
		return new CalibratedSpawnerBlockEntity(blockPos, blockState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
		return createTickerHelper(
				blockEntityType, ModBlocks.CALIBRATED_SPAWNER_BLOCK_ENTITY, level.isClientSide() ? CalibratedSpawnerBlockEntity::clientTick : CalibratedSpawnerBlockEntity::serverTick
		);
	}

	@Override
	protected @NotNull List<ItemStack> getDrops(@NotNull BlockState blockState, LootParams.Builder builder) {
		BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		CalibratedSpawnerBlockEntity calibratedSpawnerBlockEntity = (CalibratedSpawnerBlockEntity) blockEntity;
		assert blockEntity != null;
		((CalibratedSpawnerBlockEntity) blockEntity).createCurrentComponentMap();
		if (blockState.getBlock() == ModBlocks.CALIBRATED_SPAWNER) {
			DataComponentMap dataComponentMap = calibratedSpawnerBlockEntity.createCurrentComponentMap();
			blockEntity.setComponents(dataComponentMap);
		}

		return super.getDrops(blockState, builder);
	}

	@Override
	public @NotNull InteractionResult useItemOn(
			@NotNull ItemStack itemStack, @NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult
	) {
		BlockEntity blockEntity = level.getBlockEntity(blockPos);
		HolderLookup.Provider registryManager = level.registryAccess();
		assert blockEntity != null;
		CompoundTag spawnerNbt = blockEntity.saveWithFullMetadata(registryManager);
		if (!spawnerNbt.getBoolean("IsLit").orElse(false)) {
			for (SpawnEggItem spawnEggItem : SpawnEggItem.eggs()) {
				if (itemStack.is(spawnEggItem)) {
					spawnerNbt.putBoolean("IsLit", true);
					ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), VanillaSpawnersExpanded.LOGGER);
					blockEntity.loadWithComponents(TagValueInput.create(scopedCollector, registryManager, spawnerNbt));
					level.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_ALL);
					level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);
				}
			}
		}

		return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, @NotNull BlockState> builder) {
		builder.add(POWERED);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
		return this.defaultBlockState().setValue(POWERED, blockPlaceContext.getLevel().hasNeighborSignal(blockPlaceContext.getClickedPos()));
	}

	@Override
	protected void neighborChanged(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Block block, @Nullable Orientation orientation, boolean bl) {
		boolean bl2 = level.hasNeighborSignal(blockPos);
		if (!this.defaultBlockState().is(block) && bl2 != blockState.getValue(POWERED)) {

			level.setBlock(blockPos, blockState.setValue(POWERED, bl2), Block.UPDATE_CLIENTS);
		}
	}

}
