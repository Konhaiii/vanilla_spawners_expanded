package konhaiii.vanilla_spawners_expanded.block.calibrated_spawner;

import konhaiii.vanilla_spawners_expanded.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.Spawner;
import net.minecraft.block.spawner.MobSpawnerEntry;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CalibratedSpawnerBlockEntity extends BlockEntity implements Spawner {
	private final CalibratedSpawnerLogic logic = new CalibratedSpawnerLogic() {
		@Override
		public void sendStatus(World world, BlockPos pos, int status) {
			world.addSyncedBlockEvent(pos, Blocks.SPAWNER, status, 0);
		}

		@Override
		public void setSpawnEntry(@Nullable World world, BlockPos pos, MobSpawnerEntry spawnEntry) {
			super.setSpawnEntry(world, pos, spawnEntry);
			if (world != null) {
				BlockState blockState = world.getBlockState(pos);
				world.updateListeners(pos, blockState, blockState, Block.NO_REDRAW);
			}
		}
	};

	public CalibratedSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlocks.CALIBRATED_SPAWNER_BLOCK_ENTITY, pos, state);
	}

	public static NbtComponent getDefaultComponent() {
		NbtCompound nbtCompound = new NbtCompound();
		nbtCompound.putShort("Delay", (short)200);
		nbtCompound.putBoolean("HasRedstoneUpgrade", false);
		nbtCompound.putBoolean("IsLit", false);
		nbtCompound.putBoolean("HasSpeedUpgrade", false);
		nbtCompound.putShort("SpawnCount", (short)4);
		nbtCompound.putBoolean("HasCrowdUpgrade", false);
		nbtCompound.putBoolean("HasRangeUpgrade", false);
		nbtCompound.putShort("SpawnRange", (short)4);
		nbtCompound.putString("id", "vanilla_spawners_expanded:calibrated_spawner");
		NbtCompound nbtSpawnData = new NbtCompound();
		nbtSpawnData.put("entity", new NbtCompound());
		nbtCompound.put("SpawnData", nbtSpawnData);
		return NbtComponent.of(nbtCompound);
	}
	public void addCurrentComponents(ComponentMap.Builder builder) {
		super.addComponents(builder);
		NbtCompound nbtCompound = logic.dropStackNbt(new NbtCompound());
		builder.add(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.of(nbtCompound));
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
		super.readNbt(nbt, registries);
		this.logic.readNbt(this.world, this.pos, nbt);
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
		super.writeNbt(nbt, registries);
		this.logic.writeNbt(nbt);
	}
	public final ComponentMap createCurrentComponentMap() {
		ComponentMap.Builder builder = ComponentMap.builder();
		builder.addAll(this.getComponents());
		this.addCurrentComponents(builder);
		return builder.build();
	}
	public static void clientTick(World world, BlockPos pos, BlockState ignoredState, CalibratedSpawnerBlockEntity blockEntity) {
		blockEntity.logic.clientTick(world, pos);
	}

	public static void serverTick(World world, BlockPos pos, BlockState ignoredState, CalibratedSpawnerBlockEntity blockEntity) {
		blockEntity.logic.serverTick((ServerWorld)world, pos);
	}

	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
		NbtCompound nbtCompound = this.createComponentlessNbt(registries);
		nbtCompound.remove("SpawnPotentials");
		return nbtCompound;
	}

	@Override
	public boolean onSyncedBlockEvent(int type, int data) {
		return this.logic.handleStatus(this.world, type) || super.onSyncedBlockEvent(type, data);
	}

	@Override
	public void setEntityType(EntityType<?> type, Random random) {
		this.logic.setEntityId(type, this.world, random, this.pos);
		this.markDirty();
	}

	public CalibratedSpawnerLogic getLogic() {
		return this.logic;
	}
}
