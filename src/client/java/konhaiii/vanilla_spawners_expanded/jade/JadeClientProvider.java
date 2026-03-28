package konhaiii.vanilla_spawners_expanded.jade;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class JadeClientProvider implements IBlockComponentProvider {
	public static final JadeClientProvider INSTANCE = new JadeClientProvider();

	@Override
	public void appendTooltip(@NotNull ITooltip tooltip, BlockAccessor accessor, @NotNull IPluginConfig config) {

		boolean isLit = accessor.getServerData().getBoolean("IsLit").orElse(false);

		if (isLit) {
			tooltip.add(
					Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc1")
							.withStyle(ChatFormatting.GRAY)
							.append(CommonComponents.SPACE)
							.append(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc3").withStyle(ChatFormatting.WHITE))
			);
		} else {
			tooltip.add(
					Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc1")
							.withStyle(ChatFormatting.GRAY)
							.append(CommonComponents.SPACE)
							.append(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc2").withStyle(ChatFormatting.GRAY))
			);
		}

		if (accessor.getServerData().contains("Entity")) {

			Identifier entityId = Identifier.parse(accessor.getServerData().getString("Entity").orElse("minecraft:pig"));

			tooltip.add(
					Component.translatable("keyword.vanilla_spawners_expanded.soul_type")
							.withStyle(ChatFormatting.GRAY)
							.append(CommonComponents.SPACE)
							.append(Component.translatable(entityId.toLanguageKey("entity")).withStyle(ChatFormatting.WHITE))
			);

		} else {

			tooltip.add(
					Component.translatable("keyword.vanilla_spawners_expanded.soul_type")
							.append(CommonComponents.SPACE)
							.append(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc4"))
							.withStyle(ChatFormatting.GRAY)
			);
		}

		boolean redstoneUpgrade = accessor.getServerData().getBoolean("HasRedstoneUpgrade").orElse(false);
		boolean crowdUpgrade = accessor.getServerData().getBoolean("HasCrowdUpgrade").orElse(false);
		boolean rangeUpgrade = accessor.getServerData().getBoolean("HasRangeUpgrade").orElse(false);
		boolean speedUpgrade = accessor.getServerData().getBoolean("HasSpeedUpgrade").orElse(false);

		if (redstoneUpgrade || crowdUpgrade || rangeUpgrade || speedUpgrade) {

			boolean placeComma = false;

			tooltip.add(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc5").withStyle(ChatFormatting.GRAY));

			if (redstoneUpgrade) {
				tooltip.append(CommonComponents.space().append(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc10").withStyle(ChatFormatting.RED)));
				placeComma = true;
			}

			if (crowdUpgrade) {
				if (placeComma) tooltip.append(Component.literal(","));
				tooltip.append(CommonComponents.space().append(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc7").withStyle(ChatFormatting.GREEN)));
				placeComma = true;
			}

			if (rangeUpgrade) {
				if (placeComma) tooltip.append(Component.literal(","));
				tooltip.append(CommonComponents.space().append(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc8").withStyle(ChatFormatting.YELLOW)));
				placeComma = true;
			}

			if (speedUpgrade) {
				if (placeComma) tooltip.append(Component.literal(","));
				tooltip.append(CommonComponents.space().append(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc9").withStyle(ChatFormatting.AQUA)));
			}

		} else {

			tooltip.add(
					Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc5")
							.withStyle(ChatFormatting.GRAY)
							.append(CommonComponents.SPACE)
							.append(Component.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc6").withStyle(ChatFormatting.GRAY))
			);
		}
	}

	@Override
	public @NotNull Identifier getUid() {
		return JadePlugin.UID_ENTITY;
	}
}