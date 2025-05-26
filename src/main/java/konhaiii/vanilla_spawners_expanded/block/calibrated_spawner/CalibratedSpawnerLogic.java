package konhaiii.vanilla_spawners_expanded.block.calibrated_spawner;

import konhaiii.vanilla_spawners_expanded.VanillaSpawnersExpanded;
import konhaiii.vanilla_spawners_expanded.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

public abstract class CalibratedSpawnerLogic {
	private int spawnDelay = 20;
	private boolean lastWrite = false;
	private boolean isLit = false;
	private boolean hasRedstoneUpgrade = false;
	private boolean hasCrowdUpgrade = false;
	private boolean hasRangeUpgrade = false;
	private boolean hasSpeedUpgrade = false;
	private MobSpawnerEntry spawnEntry;
	private double rotation;
	private double lastRotation;
	private int minSpawnDelay = 200;
	private int maxSpawnDelay = 800;
	private int spawnCount = 4;
	@Nullable
	private Entity renderedEntity;
	private int maxNearbyEntities = 6;
	private int requiredPlayerRange = 16;
	private int spawnRange = 4;

	public void setEntityId(EntityType<?> type, @Nullable World world, Random random, BlockPos pos) {
		this.getSpawnEntry(world, random, pos).getNbt().putString("id", Registries.ENTITY_TYPE.getId(type).toString());
	}

	private boolean isPlayerInRange(World world, BlockPos pos) {
		return world.isPlayerInRange(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, this.requiredPlayerRange);

	}

	private void setUpgradeValues() {
		if (this.hasCrowdUpgrade) {
			this.maxNearbyEntities = VanillaSpawnersExpanded.config.crowdUpgradeValue;
		} else {
			this.maxNearbyEntities = VanillaSpawnersExpanded.config.crowdDefaultValue;
		}
		if (this.hasRangeUpgrade) {
			this.requiredPlayerRange = VanillaSpawnersExpanded.config.rangeUpgradeValue;
		} else {
			this.requiredPlayerRange = VanillaSpawnersExpanded.config.rangeDefaultValue;
		}
		if (this.hasSpeedUpgrade) {
			this.minSpawnDelay = VanillaSpawnersExpanded.config.speedUpgradeMinValue;
			this.maxSpawnDelay = VanillaSpawnersExpanded.config.speedUpgradeMaxValue;
		} else {
			this.minSpawnDelay = VanillaSpawnersExpanded.config.speedDefaultMinValue;
			this.maxSpawnDelay = VanillaSpawnersExpanded.config.speedDefaultMaxValue;
		}
	}

	public void clientTick(World world, BlockPos pos) {
		setUpgradeValues();
		BlockState blockstate = world.getBlockState(pos);
		if (!blockstate.isOf(ModBlocks.CALIBRATED_SPAWNER)) {
			return;
		}
		if (!this.isPlayerInRange(world, pos) || !this.isLit || (this.hasRedstoneUpgrade && blockstate.get(CalibratedSpawnerBlock.POWERED))) {
			this.lastRotation = this.rotation;
		} else {
			if (this.renderedEntity != null) {
				if (this.spawnDelay > 0) {
					this.spawnDelay--;
				}

				this.lastRotation = this.rotation;
				this.rotation = (this.rotation + 1000.0F / (this.spawnDelay + 200.0F)) % 360.0;
			}
		}
		if (this.isLit) {
			Random random = world.getRandom();
			double d = pos.getX() + random.nextDouble();
			double e = pos.getY() + random.nextDouble();
			double f = pos.getZ() + random.nextDouble();
			world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
			if (!this.hasRedstoneUpgrade || !blockstate.get(CalibratedSpawnerBlock.POWERED)) {
				world.addParticle(ParticleTypes.FLAME, d, e, f, 0.0, 0.0, 0.0);
			}
		}
	}

	public void serverTick(ServerWorld world, BlockPos pos) {
		setUpgradeValues();
		BlockState blockstate = world.getBlockState(pos);
		if (!this.isLit || (this.hasRedstoneUpgrade && blockstate.get(CalibratedSpawnerBlock.POWERED))) {
			return;
		}
		if (this.isPlayerInRange(world, pos)) {
			if (this.spawnDelay == -1) {
				this.updateSpawns(world, pos);
			}

			if (this.spawnDelay > 0) {
				this.spawnDelay--;
			} else {
				boolean bl = false;
				Random random = world.getRandom();
				MobSpawnerEntry mobSpawnerEntry = this.getSpawnEntry(world, random, pos);

				for (int i = 0; i < this.spawnCount; i++) {
					NbtCompound nbtCompound = mobSpawnerEntry.getNbt();
					Optional<EntityType<?>> optional = EntityType.fromNbt(nbtCompound);
					if (optional.isEmpty()) {
						this.updateSpawns(world, pos);
						return;
					}

					NbtList nbtList = nbtCompound.getList("Pos", NbtElement.DOUBLE_TYPE);
					int j = nbtList.size();
					double d = j >= 1 ? nbtList.getDouble(0) : pos.getX() + (random.nextDouble() - random.nextDouble()) * this.spawnRange + 0.5;
					double e = j >= 2 ? nbtList.getDouble(1) : pos.getY() + random.nextInt(3) - 1;
					double f = j >= 3 ? nbtList.getDouble(2) : pos.getZ() + (random.nextDouble() - random.nextDouble()) * this.spawnRange + 0.5;
					if (world.isSpaceEmpty(optional.get().createSimpleBoundingBox(d, e, f))) {
						BlockPos blockPos = BlockPos.ofFloored(d, e, f);
						if (mobSpawnerEntry.getCustomSpawnRules().isPresent()) {
							if (!optional.get().getSpawnGroup().isPeaceful() && world.getDifficulty() == Difficulty.PEACEFUL) {
								continue;
							}

							MobSpawnerEntry.CustomSpawnRules customSpawnRules = mobSpawnerEntry.getCustomSpawnRules().get();
							if (!customSpawnRules.blockLightLimit().contains(world.getLightLevel(LightType.BLOCK, blockPos))
									|| !customSpawnRules.skyLightLimit().contains(world.getLightLevel(LightType.SKY, blockPos))) {
								continue;
							}
						} else if (!SpawnRestriction.canSpawn((EntityType<?>)optional.get(), world, SpawnReason.SPAWNER, blockPos, world.getRandom())) {
							continue;
						}

						Entity entity = EntityType.loadEntityWithPassengers(nbtCompound, world, entityx -> {
							entityx.refreshPositionAndAngles(d, e, f, entityx.getYaw(), entityx.getPitch());
							return entityx;
						});
						if (entity == null) {
							this.updateSpawns(world, pos);
							return;
						}

						int k = world.getNonSpectatingEntities(
										entity.getClass(), new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expand(this.spawnRange)
								)
								.size();
						if (k >= this.maxNearbyEntities) {
							this.updateSpawns(world, pos);
							return;
						}

						entity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), random.nextFloat() * 360.0F, 0.0F);
						if (entity instanceof MobEntity mobEntity) {
							if (mobSpawnerEntry.getCustomSpawnRules().isEmpty() && !mobEntity.canSpawn(world, SpawnReason.SPAWNER) || !mobEntity.canSpawn(world)) {
								continue;
							}
							if (mobSpawnerEntry.getNbt().getSize() == 1) {
								mobSpawnerEntry.getNbt().contains("id", NbtElement.STRING_TYPE);
							}
							if (mobSpawnerEntry.getNbt().getSize() == 1 && mobSpawnerEntry.getNbt().contains("id", NbtElement.STRING_TYPE)) {
								((MobEntity)entity).initialize(world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.SPAWNER, null, null);
							}
						}

						if (!world.spawnNewEntityAndPassengers(entity)) {
							this.updateSpawns(world, pos);
							return;
						}

						world.syncWorldEvent(WorldEvents.SPAWNER_SPAWNS_MOB, pos, 0);
						world.emitGameEvent(entity, GameEvent.ENTITY_PLACE, blockPos);
						if (entity instanceof MobEntity) {
							((MobEntity)entity).playSpawnEffects();
						}

						bl = true;
					}
				}

				if (bl) {
					this.updateSpawns(world, pos);
				}
			}
		}
	}

	private void updateSpawns(World world, BlockPos pos) {
		Random random = world.random;
		if (this.maxSpawnDelay <= this.minSpawnDelay) {
			this.spawnDelay = this.minSpawnDelay;
		} else {
			this.spawnDelay = this.minSpawnDelay + random.nextInt(this.maxSpawnDelay - this.minSpawnDelay);
		}
		this.sendStatus(world, pos, 1);
	}

	public void readNbt(@Nullable World world, BlockPos pos, NbtCompound nbt) {
		this.spawnDelay = nbt.getShort("Delay");
		this.isLit = nbt.getBoolean("IsLit");
		this.hasRedstoneUpgrade = nbt.getBoolean("HasRedstoneUpgrade");
		boolean bl = nbt.contains("SpawnData", NbtElement.COMPOUND_TYPE);
		if (bl) {
			MobSpawnerEntry mobSpawnerEntry = MobSpawnerEntry.CODEC
					.parse(NbtOps.INSTANCE, nbt.getCompound("SpawnData"))
					.resultOrPartial(string -> VanillaSpawnersExpanded.LOGGER.warn("Invalid SpawnData: {}", string))
					.orElseGet(MobSpawnerEntry::new);
			this.setSpawnEntry(world, pos, mobSpawnerEntry);
		}

		if (nbt.contains("HasSpeedUpgrade", NbtElement.NUMBER_TYPE)) {
			this.hasSpeedUpgrade = nbt.getBoolean("HasSpeedUpgrade");
			this.spawnCount = nbt.getShort("SpawnCount");
		}

		if (nbt.contains("HasCrowdUpgrade", NbtElement.NUMBER_TYPE)) {
			this.hasCrowdUpgrade = nbt.getBoolean("HasCrowdUpgrade");
			this.hasRangeUpgrade = nbt.getBoolean("HasRangeUpgrade");
		}

		if (nbt.contains("SpawnRange", NbtElement.NUMBER_TYPE)) {
			this.spawnRange = nbt.getShort("SpawnRange");
		}

		this.renderedEntity = null;
	}

	public void droppingNbt(boolean clearSpawnEntry) {
		if (!VanillaSpawnersExpanded.config.calibratedSpawnerStayLitOnBreak) {
			this.isLit = false;
		}
		if (!VanillaSpawnersExpanded.config.calibratedSpawnerKeepMobOnBreak || clearSpawnEntry) {
			this.spawnEntry = null;
		}
		if (!VanillaSpawnersExpanded.config.calibratedSpawnerKeepUpgradesOnBreak) {
			this.hasCrowdUpgrade = false;
			this.hasRedstoneUpgrade = false;
			this.hasRangeUpgrade = false;
			this.hasSpeedUpgrade = false;
		}
		this.lastWrite = true;
	}

	public void writeNbt(NbtCompound nbt) {
		if (this.lastWrite) {
			if (this.isLit) { nbt.putBoolean("IsLit", true); }
			if (this.hasRedstoneUpgrade) { nbt.putBoolean("HasRedstoneUpgrade", true); }
			if (this.hasSpeedUpgrade) { nbt.putBoolean("HasSpeedUpgrade", true); }
			if (this.hasCrowdUpgrade) { nbt.putBoolean("HasCrowdUpgrade", true); }
			if (this.hasRangeUpgrade) { nbt.putBoolean("HasRangeUpgrade", true); }
		} else {
			nbt.putShort("Delay", (short)this.spawnDelay);
			nbt.putBoolean("IsLit", this.isLit);
			nbt.putBoolean("HasRedstoneUpgrade", this.hasRedstoneUpgrade);
			nbt.putBoolean("HasSpeedUpgrade", this.hasSpeedUpgrade);
			nbt.putShort("SpawnCount", (short)this.spawnCount);
			nbt.putBoolean("HasCrowdUpgrade", this.hasCrowdUpgrade);
			nbt.putBoolean("HasRangeUpgrade", this.hasRangeUpgrade);
			nbt.putShort("SpawnRange", (short)this.spawnRange);
		}
		if (this.spawnEntry != null) {
			nbt.put(
					"SpawnData",
					MobSpawnerEntry.CODEC.encodeStart(NbtOps.INSTANCE, this.spawnEntry).result().orElseThrow(() -> new IllegalStateException("Invalid SpawnData"))
			);
		}
	}

	@Nullable
	public Entity getRenderedEntity(World world, BlockPos pos) {
		if (this.renderedEntity == null) {
			NbtCompound nbtCompound = this.getSpawnEntry(world, world.getRandom(), pos).getNbt();
			if (!nbtCompound.contains("id", NbtElement.STRING_TYPE)) {
				return null;
			}

			this.renderedEntity = EntityType.loadEntityWithPassengers(nbtCompound, world, Function.identity());
			nbtCompound.getSize();
		}

		return this.renderedEntity;
	}

	public boolean handleStatus(World world, int status) {
		if (status == 1) {
			if (world.isClient) {
				this.spawnDelay = this.minSpawnDelay;
			}

			return true;
		} else {
			return false;
		}
	}
	protected void setSpawnEntry(@Nullable World world, BlockPos pos, MobSpawnerEntry spawnEntry) {
		this.spawnEntry = spawnEntry;
	}

	private MobSpawnerEntry getSpawnEntry(@Nullable World world, Random ignoredRandom, BlockPos pos) {
		if (this.spawnEntry == null) {
			MobSpawnerEntry mobSpawnerEntry = new MobSpawnerEntry();
			this.setSpawnEntry(world, pos, mobSpawnerEntry);
		}
		return this.spawnEntry;
	}

	public abstract void sendStatus(World world, BlockPos pos, int status);

	public double getRotation() {
		return this.rotation;
	}

	public double getLastRotation() {
		return this.lastRotation;
	}
}
