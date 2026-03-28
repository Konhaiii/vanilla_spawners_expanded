package konhaiii.vanilla_spawners_expanded.blocks;

import konhaiii.vanilla_spawners_expanded.ModBlocks;
import konhaiii.vanilla_spawners_expanded.VanillaSpawnersExpanded;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public abstract class BaseCalibratedSpawner {
	private int spawnDelay = 20;
	private boolean isLit = false;
	private boolean hasRedstoneUpgrade = false;
	private boolean hasCrowdUpgrade = false;
	private boolean hasRangeUpgrade = false;
	private boolean hasSpeedUpgrade = false;
	@Nullable
	private SpawnData nextSpawnData;
	private double spin;
	private double oSpin;
	private int minSpawnDelay = 200;
	private int maxSpawnDelay = 800;
	private int spawnCount = 4;
	@Nullable
	private Entity displayEntity;
	private int maxNearbyEntities = 6;
	private int requiredPlayerRange = 16;
	private int spawnRange = 4;

	public void setEntityId(EntityType<?> entityType, @Nullable Level level, RandomSource randomSource, BlockPos blockPos) {
		this.getOrCreateNextSpawnData(level, randomSource, blockPos).getEntityToSpawn().putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString());
	}

	private boolean isNearPlayer(Level level, BlockPos blockPos) {
		return level.hasNearbyAlivePlayer(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, this.requiredPlayerRange);
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


	public void clientTick(Level level, BlockPos blockPos) {
		setUpgradeValues();
		BlockState blockState = level.getBlockState(blockPos);
		if (!blockState.is(ModBlocks.CALIBRATED_SPAWNER)) {
			return;
		}
		if (!this.isNearPlayer(level, blockPos) || !this.isLit || (this.hasRedstoneUpgrade && blockState.getValue(CalibratedSpawnerBlock.POWERED))) {
			this.oSpin = this.spin;
		} else if (this.displayEntity != null) {
			if (this.spawnDelay > 0) {
				this.spawnDelay--;
			}

			this.oSpin = this.spin;
			this.spin = (this.spin + 1000.0F / (this.spawnDelay + 200.0F)) % 360.0;
		}
		if (this.isLit) {
			RandomSource random = level.getRandom();
			double d = blockPos.getX() + random.nextDouble();
			double e = blockPos.getY() + random.nextDouble();
			double f = blockPos.getZ() + random.nextDouble();
			level.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
			if (!this.hasRedstoneUpgrade || !blockState.getValue(CalibratedSpawnerBlock.POWERED)) {
				level.addParticle(ParticleTypes.FLAME, d, e, f, 0.0, 0.0, 0.0);
			}
		}
	}

	public void serverTick(ServerLevel serverLevel, BlockPos blockPos) {
		setUpgradeValues();
		BlockState blockstate = serverLevel.getBlockState(blockPos);
		if (!this.isLit || (this.hasRedstoneUpgrade && blockstate.getValue(CalibratedSpawnerBlock.POWERED))) {
			return;
		}
		if (this.isNearPlayer(serverLevel, blockPos) && serverLevel.isSpawnerBlockEnabled()) {
			if (this.spawnDelay == -1) {
				this.delay(serverLevel, blockPos);
			}

			if (this.spawnDelay > 0) {
				this.spawnDelay--;
			} else {
				boolean bl = false;
				RandomSource randomSource = serverLevel.getRandom();
				SpawnData spawnData = this.getOrCreateNextSpawnData(serverLevel, randomSource, blockPos);

				for (int i = 0; i < this.spawnCount; i++) {
					try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(this::toString, VanillaSpawnersExpanded.LOGGER)) {
						ValueInput valueInput = TagValueInput.create(scopedCollector, serverLevel.registryAccess(), spawnData.getEntityToSpawn());
						Optional<EntityType<?>> optional = EntityType.by(valueInput);
						if (optional.isEmpty()) {
							this.delay(serverLevel, blockPos);
							return;
						}

						Vec3 vec3 = valueInput.read("Pos", Vec3.CODEC)
								.orElseGet(
										() -> new Vec3(
												blockPos.getX() + (randomSource.nextDouble() - randomSource.nextDouble()) * this.spawnRange + 0.5,
												blockPos.getY() + randomSource.nextInt(3) - 1,
												blockPos.getZ() + (randomSource.nextDouble() - randomSource.nextDouble()) * this.spawnRange + 0.5
										)
								);
						if (serverLevel.noCollision(optional.get().getSpawnAABB(vec3.x, vec3.y, vec3.z))) {
							BlockPos blockPos2 = BlockPos.containing(vec3);
							if (spawnData.getCustomSpawnRules().isPresent()) {
								if (!optional.get().getCategory().isFriendly() && serverLevel.getDifficulty() == Difficulty.PEACEFUL) {
									continue;
								}

								SpawnData.CustomSpawnRules customSpawnRules = spawnData.getCustomSpawnRules().get();
								if (!customSpawnRules.isValidPosition(blockPos2, serverLevel)) {
									continue;
								}
							} else if (!SpawnPlacements.checkSpawnRules((EntityType<?>)optional.get(), serverLevel, EntitySpawnReason.SPAWNER, blockPos2, serverLevel.getRandom())) {
								continue;
							}

							Entity entity = EntityType.loadEntityRecursive(valueInput, serverLevel, EntitySpawnReason.SPAWNER, entityx -> {
								entityx.snapTo(vec3.x, vec3.y, vec3.z, entityx.getYRot(), entityx.getXRot());
								return entityx;
							});
							if (entity == null) {
								this.delay(serverLevel, blockPos);
								return;
							}

							int j = serverLevel.getEntities(
											EntityTypeTest.forExactClass(entity.getClass()),
											new AABB(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1).inflate(this.spawnRange),
											EntitySelector.NO_SPECTATORS
									)
									.size();
							if (j >= this.maxNearbyEntities) {
								this.delay(serverLevel, blockPos);
								return;
							}

							entity.snapTo(entity.getX(), entity.getY(), entity.getZ(), randomSource.nextFloat() * 360.0F, 0.0F);
							if (entity instanceof Mob mob) {
								if (spawnData.getCustomSpawnRules().isEmpty() && !mob.checkSpawnRules(serverLevel, EntitySpawnReason.SPAWNER)
										|| !mob.checkSpawnObstruction(serverLevel)) {
									continue;
								}

								boolean bl2 = spawnData.getEntityToSpawn().size() == 1 && spawnData.getEntityToSpawn().getString("id").isPresent();
								if (bl2) {
									((Mob)entity).finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(entity.blockPosition()), EntitySpawnReason.SPAWNER, null);
								}

								spawnData.getEquipment().ifPresent(mob::equip);
							}

							if (!serverLevel.tryAddFreshEntityWithPassengers(entity)) {
								this.delay(serverLevel, blockPos);
								return;
							}

							serverLevel.levelEvent(2004, blockPos, 0);
							serverLevel.gameEvent(entity, GameEvent.ENTITY_PLACE, blockPos2);
							if (entity instanceof Mob) {
								((Mob)entity).spawnAnim();
							}

							bl = true;
						}
					}
				}

				if (bl) {
					this.delay(serverLevel, blockPos);
				}
			}
		}
	}

	private void delay(Level level, BlockPos blockPos) {
		RandomSource randomSource = level.getRandom();
		if (this.maxSpawnDelay <= this.minSpawnDelay) {
			this.spawnDelay = this.minSpawnDelay;
		} else {
			this.spawnDelay = this.minSpawnDelay + randomSource.nextInt(this.maxSpawnDelay - this.minSpawnDelay);
		}
		this.broadcastEvent(level, blockPos, 1);
	}

	public void load(@Nullable Level level, BlockPos blockPos, ValueInput valueInput) {
		this.spawnDelay = valueInput.getShortOr("Delay", (short)spawnDelay);
		this.isLit = valueInput.getBooleanOr("IsLit", isLit);
		this.hasRedstoneUpgrade = valueInput.getBooleanOr("HasRedstoneUpgrade", hasRedstoneUpgrade);
		valueInput.read("SpawnData", SpawnData.CODEC).ifPresent(spawnData -> this.setNextSpawnData(level, blockPos, spawnData));

		if (valueInput.contains("HasSpeedUpgrade")) {
			this.hasSpeedUpgrade = valueInput.getBooleanOr("HasSpeedUpgrade", hasSpeedUpgrade);
			this.spawnCount = valueInput.getShortOr("SpawnCount", (short)spawnCount);
		}

		if (valueInput.contains("HasCrowdUpgrade")) {
			this.hasCrowdUpgrade = valueInput.getBooleanOr("HasCrowdUpgrade", hasCrowdUpgrade);
			this.hasRangeUpgrade = valueInput.getBooleanOr("HasRangeUpgrade", hasRangeUpgrade);
		}

		if (valueInput.contains("SpawnRange")) {
			this.spawnRange = valueInput.getShortOr("SpawnRange", (short)spawnRange);
		}

		this.displayEntity = null;
	}

	public void save(ValueOutput valueOutput) {
		valueOutput.putShort("Delay", (short)this.spawnDelay);
		valueOutput.putBoolean("IsLit", this.isLit);
		valueOutput.putBoolean("HasRedstoneUpgrade", this.hasRedstoneUpgrade);
		valueOutput.putBoolean("HasSpeedUpgrade", this.hasSpeedUpgrade);
		valueOutput.putShort("SpawnCount", (short)this.spawnCount);
		valueOutput.putBoolean("HasCrowdUpgrade", this.hasCrowdUpgrade);
		valueOutput.putBoolean("HasRangeUpgrade", this.hasRangeUpgrade);
		valueOutput.putShort("SpawnRange", (short)this.spawnRange);
		if (nextSpawnData != null) {
			valueOutput.storeNullable("SpawnData", SpawnData.CODEC, this.nextSpawnData);
		}
	}

	@Nullable
	public Entity getOrCreateDisplayEntity(Level level, BlockPos blockPos) {
		if (this.displayEntity == null) {
			CompoundTag compoundTag = this.getOrCreateNextSpawnData(level, level.getRandom(), blockPos).getEntityToSpawn();
			if (compoundTag.getString("id").isEmpty()) {
				return null;
			}

			this.displayEntity = EntityType.loadEntityRecursive(compoundTag, level, EntitySpawnReason.SPAWNER, EntityProcessor.NOP);
			compoundTag.size();
		}

		return this.displayEntity;
	}

	public CompoundTag dropStackNbt(CompoundTag compoundTag) {
		if (VanillaSpawnersExpanded.config.calibratedSpawnerKeepUpgradesOnBreak) {
			compoundTag.putShort("Delay", (short)this.minSpawnDelay);
			compoundTag.putBoolean("HasCrowdUpgrade", this.hasCrowdUpgrade);
			compoundTag.putBoolean("HasRangeUpgrade", this.hasRangeUpgrade);
			compoundTag.putBoolean("HasSpeedUpgrade", this.hasSpeedUpgrade);
			compoundTag.putBoolean("HasRedstoneUpgrade", this.hasRedstoneUpgrade);
		} else {
			compoundTag.putShort("Delay", (short)200);
			compoundTag.putBoolean("HasCrowdUpgrade", false);
			compoundTag.putBoolean("HasRangeUpgrade", false);
			compoundTag.putBoolean("HasSpeedUpgrade", false);
			compoundTag.putBoolean("HasRedstoneUpgrade", false);
		}
		if (VanillaSpawnersExpanded.config.calibratedSpawnerKeepMobOnBreak) {
			if (this.nextSpawnData != null) {
				compoundTag.storeNullable("SpawnData", SpawnData.CODEC, this.nextSpawnData);
			}
		} else {
			CompoundTag spawnData = new CompoundTag();
			spawnData.put("entity", new CompoundTag());

			compoundTag.put("SpawnData", spawnData);
		}
		if (VanillaSpawnersExpanded.config.calibratedSpawnerStayLitOnBreak) {
			compoundTag.putBoolean("IsLit", this.isLit);
		} else {
			compoundTag.putBoolean("IsLit", false);
		}
		compoundTag.putShort("SpawnCount", (short)this.spawnCount);
		compoundTag.putShort("SpawnRange", (short)this.spawnRange);
		compoundTag.putString("id", "vanilla_spawners_expanded:calibrated_spawner");
		return compoundTag;
	}

	public boolean onEventTriggered(Level level, int i) {
		if (i == 1) {
			if (level.isClientSide()) {
				this.spawnDelay = this.minSpawnDelay;
			}

			return true;
		} else {
			return false;
		}
	}

	protected void setNextSpawnData(@Nullable Level level, BlockPos blockPos, SpawnData spawnData) {
		this.nextSpawnData = spawnData;
	}

	private SpawnData getOrCreateNextSpawnData(@Nullable Level level, RandomSource ignoredRandomSource, BlockPos blockPos) {
		if (this.nextSpawnData == null) {
			SpawnData spawnData = new SpawnData();
			this.setNextSpawnData(level, blockPos, spawnData);
		}
		return this.nextSpawnData;
	}

	public abstract void broadcastEvent(Level level, BlockPos blockPos, int i);

	public double getSpin() {
		return this.spin;
	}

	public double getOSpin() {
		return this.oSpin;
	}
}
