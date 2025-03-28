package konhaiii.vanilla_spawners_expanded.item.special;

import konhaiii.vanilla_spawners_expanded.VanillaSpawnersExpanded;
import konhaiii.vanilla_spawners_expanded.block.ModBlocks;
import konhaiii.vanilla_spawners_expanded.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Objects;
import java.util.function.Consumer;

public class CursedBottleItem extends Item {
	public CursedBottleItem(Settings settings) {
		super(settings);
	}
	private ItemStack createMobSoul(Identifier mobEntityPath) {
		ItemStack outputStack = new ItemStack(ModItems.CURSED_BOTTLE);
		outputStack.setCount(1);
		NbtComponent.set(DataComponentTypes.ENTITY_DATA, outputStack, nbtCompound -> nbtCompound.putString("id", mobEntityPath.toString()));
		outputStack.set(DataComponentTypes.ITEM_NAME, Text.translatable("item.vanilla_spawners_expanded.mob_soul"));
		outputStack.set(DataComponentTypes.RARITY, Rarity.EPIC);
		outputStack.set(DataComponentTypes.MAX_STACK_SIZE, 1);
		outputStack.set(DataComponentTypes.ITEM_MODEL, Identifier.of(VanillaSpawnersExpanded.MOD_ID, "mob_soul"));
		return outputStack;
	}

	@Override
	public ActionResult useOnEntity(ItemStack itemStack, PlayerEntity user, LivingEntity entity, Hand hand) {
		EntityType<?> entityType = entity.getType();
		Identifier mobEntityPath = EntityType.getId(entityType);
		if (!user.getWorld().isClient && entity.isLiving() && entity.isAlive() && entityType != EntityType.PLAYER) {
			if (!VanillaSpawnersExpanded.config.mob_whitelist.isEmpty()) {
				if (!VanillaSpawnersExpanded.config.mob_whitelist.contains(mobEntityPath.toString())) {
					return ActionResult.PASS;
				}
			} else {
				if (VanillaSpawnersExpanded.config.mob_blacklist.contains(mobEntityPath.toString())) {
					return ActionResult.PASS;
				}
			}
			NbtComponent nbtComponent = itemStack.getOrDefault(DataComponentTypes.ENTITY_DATA, NbtComponent.DEFAULT);
			if (nbtComponent.isEmpty()) {
				ItemStack outputStack = createMobSoul(mobEntityPath);
				ItemStack itemStack3 = ItemUsage.exchangeStack(itemStack, user, outputStack, false);
				user.setStackInHand(hand, itemStack3);
				entity.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1f, 0.5f);
				World world = user.getWorld();
				((ServerWorld) world).spawnParticles(new DustColorTransitionParticleEffect(13915476, 2105376, 1.5F),
						entity.getX(), entity.getY() + entity.getHeight() / 2, entity.getZ(), 20, 0.3, 0.3, 0.3, 1.0);
				entity.discard();
				return ActionResult.SUCCESS_SERVER;
			}
		}
		return ActionResult.PASS;
	}
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		if (world.isClient()) {
			return ActionResult.PASS;
		} else {
			PlayerEntity player = context.getPlayer();
			ItemStack itemStack = context.getStack();
			BlockPos blockPos = context.getBlockPos();
			WrapperLookup registryManager = world.getRegistryManager();
			BlockState blockState = world.getBlockState(blockPos);
			BlockEntity blockEntity = world.getBlockEntity(blockPos);
			NbtComponent nbtComponent = itemStack.getOrDefault(DataComponentTypes.ENTITY_DATA, NbtComponent.DEFAULT);
			if (blockState.getBlock() == ModBlocks.CALIBRATED_SPAWNER && !nbtComponent.isEmpty()) {
				assert blockEntity != null;
				NbtCompound spawnerNbt = blockEntity.createNbt(registryManager);
				if (!spawnerNbt.getCompound("SpawnData").orElse(new NbtCompound()).getCompound("entity").orElse(new NbtCompound()).contains("id")) {
					spawnerNbt.getCompound("SpawnData").orElse(new NbtCompound()).getCompound("entity").orElse(new NbtCompound()).putString("id",
							Objects.requireNonNull(nbtComponent.getId()).toString());
					short speedUpgradeMaxValue = (short) VanillaSpawnersExpanded.config.speedUpgradeMaxValue;
					short speedDefaultMaxValue = (short) VanillaSpawnersExpanded.config.speedDefaultMaxValue;
					if (spawnerNbt.getBoolean("HasSpeedUpgrade").orElse(false)) {
						spawnerNbt.putShort("Delay", speedUpgradeMaxValue);
					} else {
						spawnerNbt.putShort("Delay", speedDefaultMaxValue);
					}
					blockEntity.read(spawnerNbt, registryManager);
					world.updateListeners(blockPos, blockState, blockState, Block.NOTIFY_ALL);
					world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);
					world.playSound(null, blockPos, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.BLOCKS, 1.0f, 1.25f);
					((ServerWorld) world).spawnParticles(new DustColorTransitionParticleEffect(13915476, 2105376, 1.5F),
							blockPos.getX()+0.5, blockPos.getY()+0.5, blockPos.getZ()+0.5, 20, 0.5, 0.5, 0.5, 0.05);
					ItemStack outputStack;
					if (VanillaSpawnersExpanded.config.cursedBottleIsReusable) {
						outputStack = new ItemStack(ModItems.CURSED_BOTTLE);
					} else {
						outputStack = new ItemStack(Items.GLASS_BOTTLE);
					}
					assert player != null;
					ItemStack itemStack3 = ItemUsage.exchangeStack(itemStack, player, outputStack, false);
					player.setStackInHand(context.getHand(), itemStack3);
					return ActionResult.SUCCESS_SERVER;
				}
			}
		}
		return ActionResult.FAIL;
	}
	@SuppressWarnings("deprecation")
	@Override
	public void appendTooltip(
			ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type
	) {
		NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.ENTITY_DATA, NbtComponent.DEFAULT);
		if (!nbtComponent.isEmpty()) {
			textConsumer.accept(Text.translatable("keyword.vanilla_spawners_expanded.soul_type").formatted(Formatting.GRAY).append(ScreenTexts.SPACE)
					.append(Text.translatable(Objects.requireNonNull(nbtComponent.getId()).toTranslationKey("entity")).formatted(Formatting.WHITE)));
			textConsumer.accept(ScreenTexts.EMPTY);
			textConsumer.accept(Text.translatable("item.vanilla_spawners_expanded.cursed_bottle.desc3").formatted(Formatting.GRAY));
			textConsumer.accept(Text.translatable("item.vanilla_spawners_expanded.cursed_bottle.desc4").formatted(Formatting.GRAY));
		} else {
			textConsumer.accept(Text.translatable("item.vanilla_spawners_expanded.cursed_bottle.desc1").formatted(Formatting.GRAY));
			textConsumer.accept(Text.translatable("item.vanilla_spawners_expanded.cursed_bottle.desc2").formatted(Formatting.GRAY));
		}
		super.appendTooltip(stack, context, displayComponent, textConsumer, type);
	}
}
