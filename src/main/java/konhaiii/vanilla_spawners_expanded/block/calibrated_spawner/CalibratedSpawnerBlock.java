package konhaiii.vanilla_spawners_expanded.block.calibrated_spawner;

import konhaiii.vanilla_spawners_expanded.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class CalibratedSpawnerBlock extends BlockWithEntity {
	public static final BooleanProperty POWERED = Properties.POWERED;

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
		return checkType(type, ModBlocks.CALIBRATED_SPAWNER_BLOCK_ENTITY, world.isClient ? CalibratedSpawnerBlockEntity::clientTick : CalibratedSpawnerBlockEntity::serverTick);
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
		BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
		CalibratedSpawnerBlockEntity calibratedSpawnerBlockEntity = (CalibratedSpawnerBlockEntity) blockEntity;
		if (state.getBlock() == ModBlocks.CALIBRATED_SPAWNER && blockEntity != null) {
			CalibratedSpawnerLogic calibratedSpawnerLogic = calibratedSpawnerBlockEntity.getLogic();
			NbtCompound nbt = new NbtCompound();
			calibratedSpawnerLogic.writeNbt(nbt);
			calibratedSpawnerLogic.droppingNbt(!nbt.getCompound("SpawnData").getCompound("entity").contains("id"));
		}
		return super.getDroppedStacks(state, builder);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		CalibratedSpawnerBlockEntity calibratedSpawnerBlockEntity = (CalibratedSpawnerBlockEntity) blockEntity;
		ItemStack stack = player.getStackInHand(hand);
		assert blockEntity != null;
		for (SpawnEggItem spawnEggItem : SpawnEggItem.getAll()) {
			if (stack.isOf(spawnEggItem)) {
				EntityType<?> entityType = spawnEggItem.getEntityType(stack.getNbt());
				calibratedSpawnerBlockEntity.setEntityType(entityType, world.getRandom());
				NbtCompound spawnerNbt = blockEntity.createNbt();
				spawnerNbt.putBoolean("IsLit", true);
				blockEntity.readNbt(spawnerNbt);
				world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
				world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
				if (!player.isCreative()) {
					stack.decrement(1);
				}
				blockEntity.markDirty();
				return ActionResult.SUCCESS;
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		if (stack.getNbt() != null) {
			NbtCompound nbtCompound = stack.getNbt().getCompound("BlockEntityTag");
			NbtCompound nbtCompoundEntity = nbtCompound.getCompound("SpawnData").getCompound("entity");
			if (nbtCompound.getBoolean("IsLit")) {
				tooltip.add(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc1").formatted(Formatting.GRAY).append(ScreenTexts.SPACE)
						.append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc3").formatted(Formatting.WHITE)));
			} else {
				tooltip.add(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc1").formatted(Formatting.GRAY).append(ScreenTexts.SPACE)
						.append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc2").formatted(Formatting.GRAY)));
			}
			if (nbtCompoundEntity.contains("id")) {
				tooltip.add(Text.translatable("keyword.vanilla_spawners_expanded.soul_type").formatted(Formatting.GRAY).append(ScreenTexts.SPACE)
						.append(Text.translatable(Objects.requireNonNull(Identifier.tryParse(nbtCompoundEntity.getString("id"))).toTranslationKey("entity")).formatted(Formatting.WHITE)));
			} else {
				tooltip.add(Text.translatable("keyword.vanilla_spawners_expanded.soul_type").formatted(Formatting.GRAY).append(ScreenTexts.SPACE)
						.append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc4").formatted(Formatting.GRAY)));
			}
			boolean redstoneUpgrade = nbtCompound.getBoolean("HasRedstoneUpgrade");
			boolean crowdUpgrade = nbtCompound.getBoolean("HasCrowdUpgrade");
			boolean rangeUpgrade = nbtCompound.getBoolean("HasRangeUpgrade");
			boolean speedUpgrade = nbtCompound.getBoolean("HasSpeedUpgrade");
			if (redstoneUpgrade || crowdUpgrade || rangeUpgrade || speedUpgrade) {
				tooltip.add(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc5").formatted(Formatting.GRAY));
				if (redstoneUpgrade) {
					tooltip.add(ScreenTexts.space().append(Text.literal("-").formatted(Formatting.GRAY)).append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc10").formatted(Formatting.RED)));
				}
				if (crowdUpgrade) {
					tooltip.add(ScreenTexts.space().append(Text.literal("-").formatted(Formatting.GRAY)).append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc7").formatted(Formatting.GREEN)));
				}
				if (rangeUpgrade) {
					tooltip.add(ScreenTexts.space().append(Text.literal("-").formatted(Formatting.GRAY)).append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc8").formatted(Formatting.YELLOW)));
				}
				if (speedUpgrade) {
					tooltip.add(ScreenTexts.space().append(Text.literal("-").formatted(Formatting.GRAY)).append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc9").formatted(Formatting.AQUA)));
				}
			} else {
				tooltip.add(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc5").formatted(Formatting.GRAY).append(ScreenTexts.SPACE)
						.append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc6").formatted(Formatting.GRAY)));
			}
		} else {
			tooltip.add(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc1").formatted(Formatting.GRAY).append(ScreenTexts.SPACE)
					.append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc2").formatted(Formatting.GRAY)));
			tooltip.add(Text.translatable("keyword.vanilla_spawners_expanded.soul_type").formatted(Formatting.GRAY).append(ScreenTexts.SPACE)
					.append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc4").formatted(Formatting.GRAY)));
			tooltip.add(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc5").formatted(Formatting.GRAY).append(ScreenTexts.SPACE)
					.append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc6").formatted(Formatting.GRAY)));
		}
		super.appendTooltip(stack, world, tooltip, options);
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

	@SuppressWarnings( "deprecation" )
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
		boolean bl = world.isReceivingRedstonePower(pos);
		if (!this.getDefaultState().isOf(sourceBlock) && bl != state.get(POWERED)) {

			world.setBlockState(pos, state.with(POWERED, bl), Block.NOTIFY_LISTENERS);
		}
	}
}
