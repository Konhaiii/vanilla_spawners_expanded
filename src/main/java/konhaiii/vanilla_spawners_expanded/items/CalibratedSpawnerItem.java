package konhaiii.vanilla_spawners_expanded.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public class CalibratedSpawnerItem extends BlockItem {
	public CalibratedSpawnerItem(Block block, Properties properties) {
		super(block, properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(
			@NotNull ItemStack itemStack, Item.@NotNull TooltipContext tooltipContext, @NotNull TooltipDisplay tooltipDisplay, @NotNull Consumer<Component> consumer, @NotNull TooltipFlag tooltipFlag
	) {
		if (itemStack.get(DataComponents.BLOCK_ENTITY_DATA) != null) {

			CompoundTag compoundTag = Objects.requireNonNull(itemStack.get(DataComponents.BLOCK_ENTITY_DATA)).copyTagWithoutId();
			CompoundTag nbtCompoundEntity = compoundTag.getCompound("SpawnData").orElse(new CompoundTag()).getCompound("entity").orElse(new CompoundTag());
			if (compoundTag.getBoolean("IsLit").orElse(false)) {
				consumer.accept(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc1").withStyle(ChatFormatting.GRAY).append(CommonComponents.SPACE)
						.append(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc3").withStyle(ChatFormatting.WHITE)));
			} else {
				consumer.accept(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc1").withStyle(ChatFormatting.GRAY).append(CommonComponents.SPACE)
						.append(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc2").withStyle(ChatFormatting.GRAY)));
			}
			if (nbtCompoundEntity.contains("id")) {
				consumer.accept(Component.translatable("keyword.vanilla_spawners_expanded.soul_type").withStyle(ChatFormatting.GRAY).append(CommonComponents.SPACE)
						.append(Component.translatable(Objects.requireNonNull(Identifier.tryParse(nbtCompoundEntity.getString("id").orElse("minecraft:pig"))).toLanguageKey("entity")).withStyle(ChatFormatting.WHITE)));
			} else {
				consumer.accept(Component.translatable("keyword.vanilla_spawners_expanded.soul_type").withStyle(ChatFormatting.GRAY).append(CommonComponents.SPACE)
						.append(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc4").withStyle(ChatFormatting.GRAY)));
			}
			boolean redstoneUpgrade = compoundTag.getBoolean("HasRedstoneUpgrade").orElse(false);
			boolean crowdUpgrade = compoundTag.getBoolean("HasCrowdUpgrade").orElse(false);
			boolean rangeUpgrade = compoundTag.getBoolean("HasRangeUpgrade").orElse(false);
			boolean speedUpgrade = compoundTag.getBoolean("HasSpeedUpgrade").orElse(false);
			if (redstoneUpgrade || crowdUpgrade || rangeUpgrade || speedUpgrade) {
				consumer.accept(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc5").withStyle(ChatFormatting.GRAY));
				if (redstoneUpgrade) {
					consumer.accept(CommonComponents.space().append(Component.literal("-").withStyle(ChatFormatting.GRAY)).append(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc10").withStyle(ChatFormatting.RED)));
				}
				if (crowdUpgrade) {
					consumer.accept(CommonComponents.space().append(Component.literal("-").withStyle(ChatFormatting.GRAY)).append(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc7").withStyle(ChatFormatting.GREEN)));
				}
				if (rangeUpgrade) {
					consumer.accept(CommonComponents.space().append(Component.literal("-").withStyle(ChatFormatting.GRAY)).append(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc8").withStyle(ChatFormatting.YELLOW)));
				}
				if (speedUpgrade) {
					consumer.accept(CommonComponents.space().append(Component.literal("-").withStyle(ChatFormatting.GRAY)).append(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc9").withStyle(ChatFormatting.AQUA)));
				}
			} else {
				consumer.accept(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc5").withStyle(ChatFormatting.GRAY).append(CommonComponents.SPACE)
						.append(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc6").withStyle(ChatFormatting.GRAY)));
			}
		}
		super.appendHoverText(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag);
	}
}
