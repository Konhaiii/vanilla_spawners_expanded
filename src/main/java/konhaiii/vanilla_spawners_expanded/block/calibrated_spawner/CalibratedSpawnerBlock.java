package konhaiii.vanilla_spawners_expanded.block.calibrated_spawner;

import com.mojang.serialization.MapCodec;
import konhaiii.vanilla_spawners_expanded.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CalibratedSpawnerBlock extends BlockWithEntity {
	public static final MapCodec<CalibratedSpawnerBlock> CODEC = createCodec(CalibratedSpawnerBlock::new);
	public static final BooleanProperty POWERED = Properties.POWERED;

	@Override
	public MapCodec<CalibratedSpawnerBlock> getCodec() {
		return CODEC;
	}
	public CalibratedSpawnerBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
				this.stateManager.getDefaultState().with(POWERED, false)
		);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CalibratedSpawnerBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return validateTicker(type, ModBlocks.CALIBRATED_SPAWNER_BLOCK_ENTITY, world.isClient ? CalibratedSpawnerBlockEntity::clientTick : CalibratedSpawnerBlockEntity::serverTick);
	}

	@Override
	protected List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
		World world = builder.getWorld();
		RegistryWrapper.WrapperLookup registryManager = world.getRegistryManager();
		BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
		CalibratedSpawnerBlockEntity calibratedSpawnerBlockEntity = (CalibratedSpawnerBlockEntity) blockEntity;
		assert blockEntity != null;
		NbtComponent.of(blockEntity.createNbt(registryManager));
		if (state.getBlock() == ModBlocks.CALIBRATED_SPAWNER) {
			ComponentMap componentMap = calibratedSpawnerBlockEntity.createCurrentComponentMap();
			blockEntity.setComponents(componentMap);
		}
		return super.getDroppedStacks(state, builder);
	}

	@Override
	public ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		RegistryWrapper.WrapperLookup registryManager = world.getRegistryManager();
		assert blockEntity != null;
		NbtCompound spawnerNbt = blockEntity.createNbt(registryManager);
		if (!spawnerNbt.getBoolean("IsLit").orElse(false)) {
			for (SpawnEggItem spawnEggItem : SpawnEggItem.getAll()) {
				if (stack.isOf(spawnEggItem)) {
					spawnerNbt.putBoolean("IsLit", true);
					blockEntity.read(spawnerNbt, registryManager);
					world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
					world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
				}
			}
		}
		return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(POWERED, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
	}

	@Override
	protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
		boolean bl = world.isReceivingRedstonePower(pos);
		if (!this.getDefaultState().isOf(sourceBlock) && bl != state.get(POWERED)) {

			world.setBlockState(pos, state.with(POWERED, bl), Block.NOTIFY_LISTENERS);
		}
	}
}
