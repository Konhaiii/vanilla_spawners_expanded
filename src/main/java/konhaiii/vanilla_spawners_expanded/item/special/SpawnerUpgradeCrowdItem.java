package konhaiii.vanilla_spawners_expanded.item.special;

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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.joml.Vector3f;

import java.util.List;
public class SpawnerUpgradeCrowdItem extends Item {
    public static final Vector3f PARTICLE_COLOR_START = Vec3d.unpackRgb(10544591).toVector3f();
    public static final Vector3f PARTICLE_COLOR_END = Vec3d.unpackRgb(2105376).toVector3f();
    public SpawnerUpgradeCrowdItem(Settings settings) {
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
                if (!spawnerNbt.getBoolean("HasCrowdUpgrade")) {
                    spawnerNbt.putBoolean("HasCrowdUpgrade", true);
                    blockEntity.read(spawnerNbt, registryManager);
                    world.updateListeners(blockPos, blockState, blockState, Block.NOTIFY_ALL);
                    world.emitGameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, blockPos);
                    world.playSound(null, blockPos, SoundEvents.BLOCK_AMETHYST_CLUSTER_STEP, SoundCategory.BLOCKS);
                    ((ServerWorld) world).spawnParticles(new DustColorTransitionParticleEffect(PARTICLE_COLOR_START, PARTICLE_COLOR_END, 1.5F),
                            blockPos.getX()+0.5, blockPos.getY()+0.5, blockPos.getZ()+0.5, 20, 0.5, 0.5, 0.5, 0.05);
                    itemStack.decrement(1);
                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.FAIL;
    }
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("item.vanilla_spawners_expanded.spawner_upgrade_crowd.desc1").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.vanilla_spawners_expanded.spawner_upgrade_crowd.desc2",
                VanillaSpawnersExpanded.config.crowdDefaultValue,
                VanillaSpawnersExpanded.config.crowdUpgradeValue)
                .formatted(Formatting.GRAY));
        super.appendTooltip(stack, context, tooltip, type);
    }
}