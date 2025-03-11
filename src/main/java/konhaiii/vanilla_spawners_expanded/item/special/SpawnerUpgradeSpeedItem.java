package konhaiii.vanilla_spawners_expanded.item.special;

import java.util.List;

import konhaiii.vanilla_spawners_expanded.VanillaSpawnersExpanded;
import konhaiii.vanilla_spawners_expanded.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class SpawnerUpgradeSpeedItem extends Item {
    public SpawnerUpgradeSpeedItem(Settings settings) {
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
            RegistryWrapper.WrapperLookup registryManager = world.getRegistryManager();
            BlockState blockState = world.getBlockState(blockPos);
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockState.getBlock() == ModBlocks.CALIBRATED_SPAWNER) {
                assert blockEntity != null;
                NbtCompound spawnerNbt = blockEntity.createNbt(registryManager);
                short speedUpgradeMaxValue = (short) VanillaSpawnersExpanded.config.speedUpgradeMaxValue;
                if (!spawnerNbt.getBoolean("HasSpeedUpgrade")) {
                    spawnerNbt.putBoolean("HasSpeedUpgrade", true);
                    spawnerNbt.putShort("Delay", speedUpgradeMaxValue);
                    blockEntity.read(spawnerNbt, registryManager);
                    world.updateListeners(blockPos, blockState, blockState, Block.NOTIFY_ALL);
                    world.emitGameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, blockPos);
                    world.playSound(null, blockPos, SoundEvents.BLOCK_AMETHYST_CLUSTER_STEP, SoundCategory.BLOCKS);
                    ((ServerWorld) world).spawnParticles(new DustColorTransitionParticleEffect(10533093, 2105376, 1.5F),
                            blockPos.getX()+0.5, blockPos.getY()+0.5, blockPos.getZ()+0.5, 20, 0.5, 0.5, 0.5, 0.05);
                    itemStack.decrement(1);
                    return ActionResult.SUCCESS_SERVER;
                }
            }
        }
        return ActionResult.FAIL;
    }
    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("item.vanilla_spawners_expanded.spawner_upgrade_speed.desc1").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.vanilla_spawners_expanded.spawner_upgrade_speed.desc2",
                VanillaSpawnersExpanded.config.speedDefaultMinValue,
                VanillaSpawnersExpanded.config.speedUpgradeMinValue)
                .formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.vanilla_spawners_expanded.spawner_upgrade_speed.desc3",
                        VanillaSpawnersExpanded.config.speedDefaultMaxValue,
                        VanillaSpawnersExpanded.config.speedUpgradeMaxValue)
                .formatted(Formatting.GRAY));
        super.appendTooltip(stack, context, tooltip, type);
    }
}