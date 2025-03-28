package konhaiii.vanilla_spawners_expanded.jade;

import konhaiii.vanilla_spawners_expanded.block.calibrated_spawner.CalibratedSpawnerBlockEntity;
import konhaiii.vanilla_spawners_expanded.block.calibrated_spawner.CalibratedSpawnerLogic;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.Objects;
import java.util.Optional;

public enum BlockComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
	INSTANCE;
	@Override
	public void appendTooltip(
			ITooltip tooltip,
			BlockAccessor accessor,
			IPluginConfig config
	) {
		if (Objects.equals(accessor.getServerData().getBoolean("IsLit"), Optional.of(true))) {
			tooltip.add(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc1").formatted(Formatting.GRAY).append(ScreenTexts.SPACE)
					.append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc3").formatted(Formatting.WHITE)));
		} else {
			tooltip.add(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc1").formatted(Formatting.GRAY).append(ScreenTexts.SPACE)
					.append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc2").formatted(Formatting.GRAY)));
		}
		if (accessor.getServerData().contains("Entity")) {
			Identifier entityIdentifier = Identifier.of(accessor.getServerData().getString("Entity").orElse("minecraft:pig"));
			tooltip.add(Text.translatable("keyword.vanilla_spawners_expanded.soul_type").formatted(Formatting.GRAY)
					.append(ScreenTexts.SPACE).append(Text.translatable(entityIdentifier.toTranslationKey("entity")).formatted(Formatting.WHITE)));
		} else {
			tooltip.add(Text.translatable("keyword.vanilla_spawners_expanded.soul_type")
					.append(ScreenTexts.SPACE).append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc4")).formatted(Formatting.GRAY));
		}
		boolean redstoneUpgrade = accessor.getServerData().getBoolean("HasRedstoneUpgrade").orElse(false);
		boolean crowdUpgrade = accessor.getServerData().getBoolean("HasCrowdUpgrade").orElse(false);
		boolean rangeUpgrade = accessor.getServerData().getBoolean("HasRangeUpgrade").orElse(false);
		boolean speedUpgrade = accessor.getServerData().getBoolean("HasSpeedUpgrade").orElse(false);
		if (redstoneUpgrade || crowdUpgrade || rangeUpgrade || speedUpgrade) {
			boolean placeComma = false;
			tooltip.add(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc5").formatted(Formatting.GRAY));
			if (redstoneUpgrade) {
				tooltip.append(ScreenTexts.space().append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc10").formatted(Formatting.RED)));
				placeComma = true;
			}
			if (crowdUpgrade) {
				if (placeComma) {
					tooltip.append(Text.literal(","));
				}
				tooltip.append(ScreenTexts.space().append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc7").formatted(Formatting.GREEN)));
				placeComma = true;
			}
			if (rangeUpgrade) {
				if (placeComma) {
					tooltip.append(Text.literal(","));
				}
				tooltip.append(ScreenTexts.space().append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc8").formatted(Formatting.YELLOW)));
				placeComma = true;
			}
			if (speedUpgrade) {
				if (placeComma) {
					tooltip.append(Text.literal(","));
				}
				tooltip.append(ScreenTexts.space().append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc9").formatted(Formatting.AQUA)));
			}
		} else {
			tooltip.add(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc5").formatted(Formatting.GRAY).append(ScreenTexts.SPACE)
					.append(Text.translatable("block.vanilla_spawners_expanded.calibrated_spawner.desc6").formatted(Formatting.GRAY)));
		}
	}

	@Override
	public Identifier getUid() {
		return JadePlugin.UID_ENTITY;
	}

	@Override
	public void appendServerData(NbtCompound nbtCompound, BlockAccessor blockAccessor) {
		CalibratedSpawnerBlockEntity calibratedSpawner = (CalibratedSpawnerBlockEntity) blockAccessor.getBlockEntity();
		CalibratedSpawnerLogic calibratedSpawnerLogic = calibratedSpawner.getLogic();
		NbtCompound nbt = new NbtCompound();
		calibratedSpawnerLogic.writeNbt(nbt);
		nbtCompound.putBoolean("IsLit", nbt.getBoolean("IsLit").orElse(false));
		if (nbt.getCompound("SpawnData").orElse(new NbtCompound()).getCompound("entity").orElse(new NbtCompound()).contains("id")) {
			nbtCompound.putString("Entity", nbt.getCompound("SpawnData").orElse(new NbtCompound()).getCompound("entity").orElse(new NbtCompound()).getString("id").orElse("minecraft:pig"));
		}
		nbtCompound.putBoolean("HasRedstoneUpgrade", nbt.getBoolean("HasRedstoneUpgrade").orElse(false));
		nbtCompound.putBoolean("HasCrowdUpgrade", nbt.getBoolean("HasCrowdUpgrade").orElse(false));
		nbtCompound.putBoolean("HasRangeUpgrade", nbt.getBoolean("HasRangeUpgrade").orElse(false));
		nbtCompound.putBoolean("HasSpeedUpgrade", nbt.getBoolean("HasSpeedUpgrade").orElse(false));
	}
}
