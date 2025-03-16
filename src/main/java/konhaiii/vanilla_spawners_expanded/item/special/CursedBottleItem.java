package konhaiii.vanilla_spawners_expanded.item.special;

import konhaiii.vanilla_spawners_expanded.VanillaSpawnersExpanded;
import konhaiii.vanilla_spawners_expanded.item.ModItems;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;

public class CursedBottleItem extends Item {
	public static final Vector3f PARTICLE_COLOR_START = Vec3d.unpackRgb(13915476).toVector3f();
	public static final Vector3f PARTICLE_COLOR_END = Vec3d.unpackRgb(2105376).toVector3f();
	public CursedBottleItem(Settings settings) {
		super(settings);
	}
	private ItemStack createMobSoul(Identifier mobEntityPath) {
		ItemStack outputStack = new ItemStack(ModItems.MOB_SOUL);
		outputStack.setCount(1);
		NbtCompound outputStackNbt = new NbtCompound();
		outputStackNbt.putString("id", mobEntityPath.toString());
		outputStack.setSubNbt("minecraft:entity_data", outputStackNbt);
		return outputStack;
	}

	@Override
	public ActionResult useOnEntity(ItemStack itemStack, PlayerEntity user, LivingEntity entity, Hand hand) {
		EntityType<?> entityType = entity.getType();
		Identifier mobEntityPath = EntityType.getId(entityType);
		if (!user.getWorld().isClient && entity.isLiving() && entity.isAlive() && entityType != EntityType.PLAYER) {
			if (!VanillaSpawnersExpanded.config.mob_whitelist.isEmpty()) {
				if (!VanillaSpawnersExpanded.config.mob_whitelist.contains(mobEntityPath.toString())) {
					return ActionResult.PASS;
				}
			} else {
				if (VanillaSpawnersExpanded.config.mob_blacklist.contains(mobEntityPath.toString())) {
					return ActionResult.PASS;
				}
			}
			ItemStack outputStack = createMobSoul(mobEntityPath);
			ItemStack itemStack3 = ItemUsage.exchangeStack(itemStack, user, outputStack, false);
			user.setStackInHand(hand, itemStack3);
			entity.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1f, 0.5f);
			World world = user.getWorld();
			((ServerWorld) world).spawnParticles(new DustColorTransitionParticleEffect(PARTICLE_COLOR_START, PARTICLE_COLOR_END, 1.5F),
					entity.getX(), entity.getY() + entity.getHeight() / 2, entity.getZ(), 20, 0.3, 0.3, 0.3, 1.0);
			entity.discard();
			return ActionResult.SUCCESS;
		}
		return ActionResult.FAIL;
	}
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable("item.vanilla_spawners_expanded.cursed_bottle.desc1").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.vanilla_spawners_expanded.cursed_bottle.desc2").formatted(Formatting.GRAY));
		super.appendTooltip(stack, world, tooltip, context);
	}
}
