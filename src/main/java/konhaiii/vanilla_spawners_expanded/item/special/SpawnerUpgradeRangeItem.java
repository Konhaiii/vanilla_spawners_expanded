package konhaiii.vanilla_spawners_expanded.item.special;

import konhaiii.vanilla_spawners_expanded.VanillaSpawnersExpanded;
import konhaiii.vanilla_spawners_expanded.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.type.TooltipDisplayComponent;
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

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class SpawnerUpgradeRangeItem extends Item {
    public SpawnerUpgradeRangeItem(Settings settings) {
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
                if (Objects.equals(spawnerNbt.getBoolean("HasRangeUpgrade"), Optional.of(false))) {
                    spawnerNbt.putBoolean("HasRangeUpgrade", true);
                    blockEntity.read(spawnerNbt, registryManager);
                    world.updateListeners(blockPos, blockState, blockState, Block.NOTIFY_ALL);
                    world.emitGameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, blockPos);
                    world.playSound(null, blockPos, SoundEvents.BLOCK_AMETHYST_CLUSTER_STEP, SoundCategory.BLOCKS);
                    ((ServerWorld) world).spawnParticles(new DustColorTransitionParticleEffect(14869920, 2105376, 1.5F),
                            blockPos.getX()+0.5, blockPos.getY()+0.5, blockPos.getZ()+0.5, 20, 0.5, 0.5, 0.5, 0.05);
                    itemStack.decrement(1);
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
        textConsumer.accept(Text.translatable("item.vanilla_spawners_expanded.spawner_upgrade_range.desc1").formatted(Formatting.GRAY));
        textConsumer.accept(Text.translatable("item.vanilla_spawners_expanded.spawner_upgrade_range.desc2",
                VanillaSpawnersExpanded.config.rangeDefaultValue,
                VanillaSpawnersExpanded.config.rangeUpgradeValue)
                .formatted(Formatting.GRAY));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
}