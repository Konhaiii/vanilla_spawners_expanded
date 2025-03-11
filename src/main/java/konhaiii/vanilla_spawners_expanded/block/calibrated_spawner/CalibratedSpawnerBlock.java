package konhaiii.vanilla_spawners_expanded.block.calibrated_spawner;

import com.mojang.serialization.MapCodec;
import konhaiii.vanilla_spawners_expanded.block.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
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
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class CalibratedSpawnerBlock extends BlockWithEntity {
	public static final MapCodec<CalibratedSpawnerBlock> CODEC = createCodec(CalibratedSpawnerBlock::new);
	public static final BooleanProperty POWERED = Properties.POWERED;

	@Override
	public MapCodec<CalibratedSpawnerBlock> getCodec() {
		return CODEC;
	}
	public CalibratedSpawnerBlock(AbstractBlock.Settings settings) {
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
		if (!spawnerNbt.getBoolean("IsLit")) {
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
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
		NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.DEFAULT);
		NbtCompound nbtCompound = nbtComponent.copyNbt();
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
			if (redstoneUpgrade) { tooltip.add(ScreenTexts.space().append(Text.literal("-").formatted(Formatting.GRAY)).append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc10").formatted(Formatting.RED))); }
			if (crowdUpgrade) { tooltip.add(ScreenTexts.space().append(Text.literal("-").formatted(Formatting.GRAY)).append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc7").formatted(Formatting.GREEN))); }
			if (rangeUpgrade) { tooltip.add(ScreenTexts.space().append(Text.literal("-").formatted(Formatting.GRAY)).append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc8").formatted(Formatting.YELLOW))); }
			if (speedUpgrade) { tooltip.add(ScreenTexts.space().append(Text.literal("-").formatted(Formatting.GRAY)).append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc9").formatted(Formatting.AQUA))); }
		} else {
			tooltip.add(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc5").formatted(Formatting.GRAY).append(ScreenTexts.SPACE)
					.append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc6").formatted(Formatting.GRAY)));
		}
		super.appendTooltip(stack, context, tooltip, options);
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
