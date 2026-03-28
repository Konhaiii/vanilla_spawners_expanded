package konhaiii.vanilla_spawners_expanded.jade;

import konhaiii.vanilla_spawners_expanded.VanillaSpawnersExpanded;
import konhaiii.vanilla_spawners_expanded.blocks.BaseCalibratedSpawner;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueOutput;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public enum BlockComponentProvider implements IServerDataProvider<@NotNull BlockAccessor> {
	INSTANCE;

	@Override
	public @NotNull Identifier getUid() {
		return JadePlugin.UID_ENTITY;
	}

	@Override
	public void appendServerData(CompoundTag compoundTag, BlockAccessor accessor) {

		CompoundTag nbt = getCompoundTag(accessor);

		compoundTag.putBoolean("IsLit", nbt.getBoolean("IsLit").orElse(false));

		nbt.getCompound("SpawnData")
				.flatMap(spawn -> spawn.getCompound("entity"))
				.flatMap(entity -> entity.getString("id"))
				.ifPresent(id -> compoundTag.putString("Entity", id));

		compoundTag.putBoolean("HasRedstoneUpgrade", nbt.getBoolean("HasRedstoneUpgrade").orElse(false));
		compoundTag.putBoolean("HasCrowdUpgrade", nbt.getBoolean("HasCrowdUpgrade").orElse(false));
		compoundTag.putBoolean("HasRangeUpgrade", nbt.getBoolean("HasRangeUpgrade").orElse(false));
		compoundTag.putBoolean("HasSpeedUpgrade", nbt.getBoolean("HasSpeedUpgrade").orElse(false));
	}

	private static @NotNull CompoundTag getCompoundTag(BlockAccessor accessor) {
		konhaiii.vanilla_spawners_expanded.blocks.CalibratedSpawnerBlockEntity blockEntity = (konhaiii.vanilla_spawners_expanded.blocks.CalibratedSpawnerBlockEntity) accessor.getBlockEntity();
		assert blockEntity != null;
		BaseCalibratedSpawner logic = blockEntity.getSpawner();

		ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), VanillaSpawnersExpanded.LOGGER);
		TagValueOutput tagValueOutput = TagValueOutput.createWithoutContext(scopedCollector);
		logic.save(tagValueOutput);
		return tagValueOutput.buildResult();
	}
}