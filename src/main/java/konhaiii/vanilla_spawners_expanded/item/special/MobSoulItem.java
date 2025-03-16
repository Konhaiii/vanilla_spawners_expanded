package konhaiii.vanilla_spawners_expanded.item.special;

import konhaiii.vanilla_spawners_expanded.VanillaSpawnersExpanded;
import konhaiii.vanilla_spawners_expanded.block.calibrated_spawner.CalibratedSpawnerBlockEntity;
import konhaiii.vanilla_spawners_expanded.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.Objects;

public class MobSoulItem extends Item {
	public static final Vector3f PARTICLE_COLOR_START = Vec3d.unpackRgb(13915476).toVector3f();
	public static final Vector3f PARTICLE_COLOR_END = Vec3d.unpackRgb(2105376).toVector3f();
	public MobSoulItem(Settings settings) {
		super(settings);
	}
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		if (world.isClient()) {
			return ActionResult.FAIL;
		} else {
			PlayerEntity player = context.getPlayer();
			ItemStack itemStack = context.getStack();
			BlockPos blockPos = context.getBlockPos();
			BlockState blockState = world.getBlockState(blockPos);
			BlockEntity blockEntity = world.getBlockEntity(blockPos);
			NbtCompound nbtCompound = itemStack.getNbt();
			if (nbtCompound == null || !nbtCompound.contains("minecraft:entity_data")) {
				return ActionResult.FAIL;
			}
			if (blockEntity instanceof CalibratedSpawnerBlockEntity calibratedSpawnerBlockEntity) {
				NbtCompound spawnerNbt = blockEntity.createNbt();
				if (!spawnerNbt.getCompound("SpawnData").getCompound("entity").contains("id")) {
					EntityType<?> entityType = Registries.ENTITY_TYPE.get(Identifier.tryParse(nbtCompound.getCompound("minecraft:entity_data").getString("id")));
					calibratedSpawnerBlockEntity.setEntityType(entityType, world.getRandom());
					short speedUpgradeMaxValue = (short) VanillaSpawnersExpanded.config.speedUpgradeMaxValue;
					short speedDefaultMaxValue = (short) VanillaSpawnersExpanded.config.speedDefaultMaxValue;
					if (spawnerNbt.getBoolean("HasSpeedUpgrade")) {
						spawnerNbt.putShort("Delay", speedUpgradeMaxValue);
					} else {
						spawnerNbt.putShort("Delay", speedDefaultMaxValue);
					}
					blockEntity.readNbt(spawnerNbt);
					world.playSound(null, blockPos, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.BLOCKS, 1.0f, 1.25f);
					((ServerWorld) world).spawnParticles(new DustColorTransitionParticleEffect(PARTICLE_COLOR_START, PARTICLE_COLOR_END, 1.5F),
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
					world.updateListeners(blockPos, blockState, blockState, Block.NOTIFY_ALL);
					world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);
					return ActionResult.SUCCESS;
				}
			}
		}
		return ActionResult.FAIL;
	}
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (stack.getNbt() != null) {
			NbtCompound nbtComponentCompound = stack.getNbt().getCompound("minecraft:entity_data");
			Identifier soulEntityId = Identifier.tryParse(nbtComponentCompound.getString("id"));

			tooltip.add(Text.translatable("keyword.vanilla_spawners_expanded.soul_type").formatted(Formatting.GRAY).append(ScreenTexts.SPACE)
					.append(Text.translatable(Objects.requireNonNull(soulEntityId).toTranslationKey("entity")).formatted(Formatting.WHITE)));
			tooltip.add(ScreenTexts.EMPTY);
			tooltip.add(Text.translatable("item.vanilla_spawners_expanded.cursed_bottle.desc3").formatted(Formatting.GRAY));
			tooltip.add(Text.translatable("item.vanilla_spawners_expanded.cursed_bottle.desc4").formatted(Formatting.GRAY));
		}
		super.appendTooltip(stack, world, tooltip, context);
	}
}
