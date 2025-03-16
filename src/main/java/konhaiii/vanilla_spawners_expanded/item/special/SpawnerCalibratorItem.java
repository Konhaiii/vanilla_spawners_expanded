package konhaiii.vanilla_spawners_expanded.item.special;

import konhaiii.vanilla_spawners_expanded.VanillaSpawnersExpanded;
import konhaiii.vanilla_spawners_expanded.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpawnerCalibratorItem extends Item {
	public SpawnerCalibratorItem(Settings settings) {
		super(settings);
	}
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		if (world.isClient()) {
			return ActionResult.PASS;
		} else {
			ItemStack itemStack = context.getStack();
			BlockPos blockPos = context.getBlockPos();
			BlockState blockState = world.getBlockState(blockPos);
			BlockState newBlockState = ModBlocks.CALIBRATED_SPAWNER.getDefaultState();
			BlockEntity blockEntity = world.getBlockEntity(blockPos);
			if (blockState.getBlock() == Blocks.SPAWNER) {
				assert blockEntity != null;
				NbtCompound spawnerNbt = blockEntity.createNbt();
				spawnerNbt.remove("SpawnPotentials");
				spawnerNbt.remove("RequiredPlayerRange");
				spawnerNbt.remove("MaxNearbyEntities");
				spawnerNbt.remove("MaxSpawnDelay");
				spawnerNbt.remove("MinSpawnDelay");
				if (spawnerNbt.getCompound("SpawnData").getCompound("entity").contains("id")) {
					spawnerNbt.putBoolean("IsLit", true);
				}
				spawnerNbt.putShort("Delay", (short) VanillaSpawnersExpanded.config.speedDefaultMaxValue);
				spawnerNbt.putString("id", "vanilla_spawners_expanded:calibrated_spawner");
				world.setBlockState(blockPos, newBlockState);
				BlockEntity newBlockEntity = ModBlocks.CALIBRATED_SPAWNER_BLOCK_ENTITY.get(world, blockPos);
				assert newBlockEntity != null;
				newBlockEntity.readNbt(spawnerNbt);
				world.updateListeners(blockPos, newBlockState, newBlockState, Block.NOTIFY_ALL);
				world.emitGameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, blockPos);
				world.playSound(null, blockPos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 1.0f, 1.4f);
				((ServerWorld) world).spawnParticles(ParticleTypes.WAX_OFF,
						blockPos.getX()+0.5, blockPos.getY()+0.5, blockPos.getZ()+0.5, 20, 0.6, 0.6, 0.6, 0.05);
				itemStack.decrement(1);
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.FAIL;
	}
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable("item.vanilla_spawners_expanded.spawner_calibrator.desc1").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.vanilla_spawners_expanded.spawner_calibrator.desc2").formatted(Formatting.GRAY));
		super.appendTooltip(stack, world, tooltip, context);
	}
}
