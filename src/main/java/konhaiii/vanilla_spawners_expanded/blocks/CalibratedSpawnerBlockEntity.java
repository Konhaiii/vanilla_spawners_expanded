package konhaiii.vanilla_spawners_expanded.blocks;

import konhaiii.vanilla_spawners_expanded.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class CalibratedSpawnerBlockEntity extends BlockEntity implements Spawner {
	private final BaseCalibratedSpawner calibrated_spawner = new BaseCalibratedSpawner() {
		@Override
		public void broadcastEvent(Level level, BlockPos blockPos, int i) {
			level.blockEvent(blockPos, ModBlocks.CALIBRATED_SPAWNER, i, 0);
		}

		@Override
		public void setNextSpawnData(@Nullable Level level, BlockPos blockPos, SpawnData spawnData) {
			super.setNextSpawnData(level, blockPos, spawnData);
			if (level != null) {
				BlockState blockState = level.getBlockState(blockPos);
				level.sendBlockUpdated(blockPos, blockState, blockState, 260);
			}
		}
	};

	public CalibratedSpawnerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ModBlocks.CALIBRATED_SPAWNER_BLOCK_ENTITY, blockPos, blockState);
	}

	public void collectImplicitComponents(DataComponentMap.@NotNull Builder builder) {
		super.collectImplicitComponents(builder);
		CompoundTag compoundTag = calibrated_spawner.dropStackNbt(new CompoundTag());
		builder.set(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.of(ModBlocks.CALIBRATED_SPAWNER_BLOCK_ENTITY, compoundTag));
	}

	@Override
	protected void loadAdditional(@NotNull ValueInput valueInput) {
		super.loadAdditional(valueInput);
		this.calibrated_spawner.load(this.level, this.worldPosition, valueInput);
	}

	@Override
	protected void saveAdditional(@NotNull ValueOutput valueOutput) {
		super.saveAdditional(valueOutput);
		this.calibrated_spawner.save(valueOutput);
	}

	public final DataComponentMap createCurrentComponentMap() {
		DataComponentMap.Builder builder = DataComponentMap.builder();
		builder.addAll(this.components());
		this.collectImplicitComponents(builder);
		return builder.build();
	}

	public static void clientTick(Level level, BlockPos blockPos, BlockState ignoredBlockState, CalibratedSpawnerBlockEntity calibratedSpawnerBlockEntity) {
		calibratedSpawnerBlockEntity.calibrated_spawner.clientTick(level, blockPos);
	}

	public static void serverTick(Level level, BlockPos blockPos, BlockState ignoredBlockState, CalibratedSpawnerBlockEntity calibratedSpawnerBlockEntity) {
		calibratedSpawnerBlockEntity.calibrated_spawner.serverTick((ServerLevel)level, blockPos);
	}

	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider provider) {
		CompoundTag compoundTag = this.saveCustomOnly(provider);
		compoundTag.remove("SpawnPotentials");
		return compoundTag;
	}

	@Override
	public boolean triggerEvent(int i, int j) {
		return this.calibrated_spawner.onEventTriggered(this.level, i) || super.triggerEvent(i, j);
	}

	@Override
	public void setEntityId(@NotNull EntityType<?> entityType, @NotNull RandomSource randomSource) {
		this.calibrated_spawner.setEntityId(entityType, this.level, randomSource, this.worldPosition);
		this.setChanged();
	}

	public BaseCalibratedSpawner getSpawner() {
		return this.calibrated_spawner;
	}
}