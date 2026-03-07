package konhaiii.vanilla_spawners_expanded.items;

import konhaiii.vanilla_spawners_expanded.ModBlocks;
import konhaiii.vanilla_spawners_expanded.VanillaSpawnersExpanded;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.TagValueInput;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class SpawnerUpgradeRangeItem extends Item {
	public SpawnerUpgradeRangeItem(Properties properties) {
		super(properties);
	}

	@Override
	public @NotNull InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		if (level.isClientSide()) {
			return InteractionResult.PASS;
		} else {
			ItemStack itemStack = context.getItemInHand();
			BlockPos blockPos = context.getClickedPos();
			HolderLookup.Provider registryManager = level.registryAccess();
			BlockState blockState = level.getBlockState(blockPos);
			BlockEntity blockEntity = level.getBlockEntity(blockPos);
			if (blockState.getBlock() == ModBlocks.CALIBRATED_SPAWNER) {
				assert blockEntity != null;
				CompoundTag spawnerNbt = blockEntity.getUpdateTag(registryManager);
				if (Objects.equals(spawnerNbt.getBoolean("HasRangeUpgrade"), Optional.of(false))) {
					spawnerNbt.putBoolean("HasRangeUpgrade", true);
					ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), VanillaSpawnersExpanded.LOGGER);
					blockEntity.loadWithComponents(TagValueInput.create(scopedCollector, registryManager, spawnerNbt));
					level.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_CLIENTS);
					level.gameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, blockPos);
					level.playSound(null, blockPos, SoundEvents.AMETHYST_CLUSTER_STEP, SoundSource.BLOCKS);
					((ServerLevel) level).sendParticles(new DustColorTransitionOptions(14869920, 2105376, 1.5F),
							blockPos.getX()+0.5, blockPos.getY()+0.5, blockPos.getZ()+0.5, 20, 0.5, 0.5, 0.5, 0.05);
					itemStack.shrink(1);
					return InteractionResult.SUCCESS_SERVER;
				}
			}
		}
		return InteractionResult.FAIL;
	}
	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(
			@NotNull ItemStack itemStack, Item.@NotNull TooltipContext tooltipContext, @NotNull TooltipDisplay tooltipDisplay, @NotNull Consumer<Component> consumer, @NotNull TooltipFlag tooltipFlag
	) {
		consumer.accept(Component.translatable("item.vanilla_spawners_expanded.spawner_upgrade_range.desc1").withStyle(ChatFormatting.GRAY));
		consumer.accept(Component.translatable("item.vanilla_spawners_expanded.spawner_upgrade_range.desc2",
						VanillaSpawnersExpanded.config.rangeDefaultValue,
						VanillaSpawnersExpanded.config.rangeUpgradeValue)
				.withStyle(ChatFormatting.GRAY));
		super.appendHoverText(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag);
	}
}