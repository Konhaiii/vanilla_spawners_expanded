package konhaiii.vanilla_spawners_expanded.items;

import konhaiii.vanilla_spawners_expanded.ModBlocks;
import konhaiii.vanilla_spawners_expanded.ModItems;
import konhaiii.vanilla_spawners_expanded.VanillaSpawnersExpanded;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.TagValueInput;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CursedBottleItem extends Item {
	public CursedBottleItem(Properties properties) {
		super(properties);
	}

	private ItemStack createMobSoul(EntityType<?> entityType) {
		ItemStack outputStack = new ItemStack(ModItems.CURSED_BOTTLE);

		CompoundTag entityTag = new CompoundTag();
		entityTag.putString("id", EntityType.getKey(entityType).toString());

		outputStack.set(DataComponents.ENTITY_DATA, TypedEntityData.of(entityType, entityTag));

		outputStack.set(DataComponents.ITEM_NAME,
				Component.translatable("item.vanilla_spawners_expanded.mob_soul"));

		outputStack.set(DataComponents.RARITY, Rarity.EPIC);
		outputStack.set(DataComponents.MAX_STACK_SIZE, 1);
		outputStack.set(DataComponents.ITEM_MODEL,
				Identifier.fromNamespaceAndPath(VanillaSpawnersExpanded.MOD_ID, "mob_soul"));

		return outputStack;
	}

	@Override
	public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack itemStack, @NotNull Player player, @NotNull LivingEntity livingEntity, @NotNull InteractionHand interactionHand) {
		EntityType<?> entityType = livingEntity.getType();
		Identifier mobEntityPath = EntityType.getKey(entityType);
		if (!player.level().isClientSide() && livingEntity.isAlive() && entityType != EntityType.PLAYER) {
			if (!VanillaSpawnersExpanded.config.mob_whitelist.isEmpty()) {
				if (!VanillaSpawnersExpanded.config.mob_whitelist.contains(mobEntityPath.toString())) {
					return InteractionResult.PASS;
				}
			} else {
				if (VanillaSpawnersExpanded.config.mob_blacklist.contains(mobEntityPath.toString())) {
					return InteractionResult.PASS;
				}
			}
			TypedEntityData<?> entityData = itemStack.get(DataComponents.ENTITY_DATA);
			if (entityData == null) {
				ItemStack outputStack = createMobSoul(entityType);
				ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player, outputStack, false);
				player.setItemInHand(interactionHand, itemStack3);
				livingEntity.playSound(SoundEvents.ZOMBIE_INFECT, 1f, 0.5f);
				Level level = player.level();
				((ServerLevel) level).sendParticles(new DustColorTransitionOptions(13915476, 2105376, 1.5F),
						livingEntity.getX(), livingEntity.getY() + livingEntity.getBbHeight() / 2, livingEntity.getZ(), 20, 0.3, 0.3, 0.3, 1.0);
				livingEntity.discard();
				return InteractionResult.SUCCESS_SERVER;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public @NotNull InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		if (level.isClientSide()) {
			return InteractionResult.PASS;
		} else {
			Player player = context.getPlayer();
			ItemStack itemStack = context.getItemInHand();
			BlockPos blockPos = context.getClickedPos();
			HolderLookup.Provider registryManager = level.registryAccess();
			BlockState blockState = level.getBlockState(blockPos);
			BlockEntity blockEntity = level.getBlockEntity(blockPos);
			TypedEntityData<?> entityData = itemStack.get(DataComponents.ENTITY_DATA);
			if (blockState.getBlock() == ModBlocks.CALIBRATED_SPAWNER && entityData != null) {
				assert blockEntity != null;
				CompoundTag spawnerNbt = blockEntity.getUpdateTag(registryManager);
				if (!spawnerNbt.getCompound("SpawnData").orElse(new CompoundTag()).getCompound("entity").orElse(new CompoundTag()).contains("id")) {
					spawnerNbt.getCompound("SpawnData").orElse(new CompoundTag()).getCompound("entity").orElse(new CompoundTag()).putString("id",
							EntityType.getKey((EntityType<?>) entityData.type()).toString());
					short speedUpgradeMaxValue = (short) VanillaSpawnersExpanded.config.speedUpgradeMaxValue;
					short speedDefaultMaxValue = (short) VanillaSpawnersExpanded.config.speedDefaultMaxValue;
					if (spawnerNbt.getBoolean("HasSpeedUpgrade").orElse(false)) {
						spawnerNbt.putShort("Delay", speedUpgradeMaxValue);
					} else {
						spawnerNbt.putShort("Delay", speedDefaultMaxValue);
					}
					ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), VanillaSpawnersExpanded.LOGGER);
					blockEntity.loadWithComponents(TagValueInput.create(scopedCollector, registryManager, spawnerNbt));
					level.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_CLIENTS);
					level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);
					level.playSound(null, blockPos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 1.0f, 1.25f);
					((ServerLevel) level).sendParticles(new DustColorTransitionOptions(13915476, 2105376, 1.5F),
							blockPos.getX()+0.5, blockPos.getY()+0.5, blockPos.getZ()+0.5, 20, 0.5, 0.5, 0.5, 0.05);
					ItemStack outputStack;
					if (VanillaSpawnersExpanded.config.cursedBottleIsReusable) {
						outputStack = new ItemStack(ModItems.CURSED_BOTTLE);
					} else {
						outputStack = new ItemStack(Items.GLASS_BOTTLE);
					}
					assert player != null;
					ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player, outputStack, false);
					player.setItemInHand(context.getHand(), itemStack3);
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
		TypedEntityData<?> entityData = itemStack.get(DataComponents.ENTITY_DATA);
		if (entityData != null) {
			consumer.accept(Component.translatable("keyword.vanilla_spawners_expanded.soul_type").withStyle(ChatFormatting.GRAY).append(CommonComponents.SPACE)
					.append(Component.translatable(EntityType.getKey((EntityType<?>) entityData.type()).toLanguageKey("entity")).withStyle(ChatFormatting.WHITE)));
			consumer.accept(CommonComponents.EMPTY);
			consumer.accept(Component.translatable("item.vanilla_spawners_expanded.cursed_bottle.desc3").withStyle(ChatFormatting.GRAY));
			consumer.accept(Component.translatable("item.vanilla_spawners_expanded.cursed_bottle.desc4").withStyle(ChatFormatting.GRAY));
		} else {
			consumer.accept(Component.translatable("item.vanilla_spawners_expanded.cursed_bottle.desc1").withStyle(ChatFormatting.GRAY));
			consumer.accept(Component.translatable("item.vanilla_spawners_expanded.cursed_bottle.desc2").withStyle(ChatFormatting.GRAY));
		}
		super.appendHoverText(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag);
	}
}
