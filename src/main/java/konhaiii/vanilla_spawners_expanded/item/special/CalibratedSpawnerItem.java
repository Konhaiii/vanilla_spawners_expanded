package konhaiii.vanilla_spawners_expanded.item.special;

import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Objects;
import java.util.function.Consumer;

public class CalibratedSpawnerItem extends BlockItem {
	public CalibratedSpawnerItem(Block block, Settings settings) {
		super(block, settings);
	}
	@SuppressWarnings("deprecation")
	@Override
	public void appendTooltip(
			ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type
	) {
		NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.DEFAULT);
		NbtCompound nbtCompound = nbtComponent.copyNbt();
		NbtCompound nbtCompoundEntity = nbtCompound.getCompound("SpawnData").orElse(new NbtCompound()).getCompound("entity").orElse(new NbtCompound());
		if (nbtCompound.getBoolean("IsLit").orElse(false)) {
			textConsumer.accept(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc1").formatted(Formatting.GRAY).append(ScreenTexts.SPACE)
					.append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc3").formatted(Formatting.WHITE)));
		} else {
			textConsumer.accept(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc1").formatted(Formatting.GRAY).append(ScreenTexts.SPACE)
					.append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc2").formatted(Formatting.GRAY)));
		}
		if (nbtCompoundEntity.contains("id")) {
			textConsumer.accept(Text.translatable("keyword.vanilla_spawners_expanded.soul_type").formatted(Formatting.GRAY).append(ScreenTexts.SPACE)
					.append(Text.translatable(Objects.requireNonNull(Identifier.tryParse(nbtCompoundEntity.getString("id").orElse("minecraft:pig"))).toTranslationKey("entity")).formatted(Formatting.WHITE)));
		} else {
			textConsumer.accept(Text.translatable("keyword.vanilla_spawners_expanded.soul_type").formatted(Formatting.GRAY).append(ScreenTexts.SPACE)
					.append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc4").formatted(Formatting.GRAY)));
		}
		boolean redstoneUpgrade = nbtCompound.getBoolean("HasRedstoneUpgrade").orElse(false);
		boolean crowdUpgrade = nbtCompound.getBoolean("HasCrowdUpgrade").orElse(false);
		boolean rangeUpgrade = nbtCompound.getBoolean("HasRangeUpgrade").orElse(false);
		boolean speedUpgrade = nbtCompound.getBoolean("HasSpeedUpgrade").orElse(false);
		if (redstoneUpgrade || crowdUpgrade || rangeUpgrade || speedUpgrade) {
			textConsumer.accept(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc5").formatted(Formatting.GRAY));
			if (redstoneUpgrade) { textConsumer.accept(ScreenTexts.space().append(Text.literal("-").formatted(Formatting.GRAY)).append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc10").formatted(Formatting.RED))); }
			if (crowdUpgrade) { textConsumer.accept(ScreenTexts.space().append(Text.literal("-").formatted(Formatting.GRAY)).append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc7").formatted(Formatting.GREEN))); }
			if (rangeUpgrade) { textConsumer.accept(ScreenTexts.space().append(Text.literal("-").formatted(Formatting.GRAY)).append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc8").formatted(Formatting.YELLOW))); }
			if (speedUpgrade) { textConsumer.accept(ScreenTexts.space().append(Text.literal("-").formatted(Formatting.GRAY)).append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc9").formatted(Formatting.AQUA))); }
		} else {
			textConsumer.accept(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc5").formatted(Formatting.GRAY).append(ScreenTexts.SPACE)
					.append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc6").formatted(Formatting.GRAY)));
		}
		super.appendTooltip(stack, context, displayComponent, textConsumer, type);
	}
}
